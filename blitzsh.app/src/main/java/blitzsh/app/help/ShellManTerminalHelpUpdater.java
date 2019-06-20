package blitzsh.app.help;

import blitzsh.app.help.model.CommandHelp;
import blitzsh.app.help.model.CommandOption;
import blitzsh.app.help.model.HelpIndex;
import blitzsh.app.utils.ProcessUtils;
import blitzsh.app.utils.StorageUtils;
import blitzsh.app.utils.interfaces.IStatusMonitor;
import blitzsh.app.utils.json.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static blitzsh.app.common.model.enums.WorkDir.HELP;

public class ShellManTerminalHelpUpdater {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShellManTerminalHelpUpdater.class);

    public static void updateHelp(String configId, String shellExec, IStatusMonitor statusMonitor) {
        statusMonitor.log(null, null, "Start indexing...");
        HelpIndex helpIndex = loadHelpIndex(shellExec, statusMonitor);

        statusMonitor.log(null, null, "Saving help index...");
        File helpDir = StorageUtils.getOrCreateWorkDir(HELP);
        saveHelp(helpDir, configId, helpIndex);

        statusMonitor.log(null, null, "Help index successfully created.");
    }

    private static HelpIndex loadHelpIndex(String shellExec, IStatusMonitor statusMonitor) {
        HelpIndex helpIndex = new HelpIndex();

        statusMonitor.log(null, null, "Requesting all available commands...");
        String commandLines = ProcessUtils.readOutputFromProcess(shellExec, "-c", "man -k .");

        String[] lines = splitLines(commandLines);
        int commandCount = lines.length;
        int current = 0;

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (String line : lines) {
            current++;

            String commandLine = StringUtils.splitByWholeSeparator(line, " - ")[0];
            String command = StringUtils.trim(StringUtils.substringBeforeLast(commandLine, "("));

            if (helpIndex.containsKey(command)) {
                continue;
            }

            final int finalCurrent = current;
            executorService.submit(() -> {
                statusMonitor.log(finalCurrent, commandCount, String.format("Parsing command %s (%s / %s)", command, finalCurrent, commandCount));

                String manPage = ProcessUtils.readOutputFromProcess(shellExec, "-c", "man " + command);
                CommandHelp commandHelp = parseCommandHelp(manPage);
                helpIndex.put(command, commandHelp);
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            LOGGER.error("Error on waiting for threads.", e);
            executorService.shutdownNow();
        }

        return helpIndex;
    }

    private static void saveHelp(File helpDir, String configId, HelpIndex helpIndex) {
        File helpIndexFile = new File(helpDir, configId + ".json");
        JsonUtils.toJsonFile(helpIndex, helpIndexFile);
    }

    private static CommandHelp parseCommandHelp(String manPage) {
        Map<String, String> sections = parseSections(manPage);

        CommandHelp commandHelp = new CommandHelp();

        commandHelp.setManual(manPage);
        commandHelp.setDescription(sections.get("DESCRIPTION"));
        commandHelp.setSyntax(Optional.ofNullable(sections.get("SYNTAX")).orElse(sections.get("SYNOPSIS")));
        commandHelp.setOptions(parseOptions(sections.get("OPTIONS")));

        return commandHelp;
    }

    private static List<CommandOption> parseOptions(String options) {
        if (StringUtils.isBlank(options)) {
            return new ArrayList<>();
        }

        List<CommandOption> result = new ArrayList<>();

        String currentOption = null;
        StringBuilder currentDescription = null;

        String[] lines = splitLines(options);
        for (String line : lines) {
            boolean isOptionLine = line.startsWith("-");
            if (isOptionLine) {
                if (StringUtils.isNotBlank(currentOption) && StringUtils.isNotBlank(currentDescription)) {
                    String[] aliases = currentOption.split("\\s*,\\s*");
                    result.add(new CommandOption(trimWhiteSpaceFromTheBeginingAndEndOFEveryLine(currentDescription.toString()), aliases));
                }

                currentOption = line;
                currentDescription = new StringBuilder();
            } else {
                if (StringUtils.isNotBlank(currentOption)) {
                    currentDescription.append(line).append("\n");
                }
            }
        }

        return result;
    }

    private static Map<String, String> parseSections(String manPage) {
        Map<String, String> sections = new HashMap<>();

        String currentSection = null;
        StringBuilder currentContent = null;

        try (var stringReader = new StringReader(manPage); var bufReader = new BufferedReader(stringReader)) {
            String line;
            while ((line = bufReader.readLine()) != null) {
                boolean isHeaderLine = StringUtils.isNotBlank(line) && !StringUtils.isWhitespace(String.valueOf(line.charAt(0)));
                if (isHeaderLine) {
                    if (currentSection != null && currentContent != null && StringUtils.isNotBlank(currentContent.toString())) {
                        sections.put(StringUtils.trim(currentSection), trimWhiteSpaceFromTheBeginingAndEndOFEveryLine(currentContent.toString()));
                    }
                    currentSection = line;
                    currentContent = new StringBuilder();
                } else {
                    if (StringUtils.isNotBlank(currentSection)) {
                        currentContent.append(line).append("\n");
                    }
                }
            }
        } catch (IOException e) {
            String message = String.format("Error on parsing man page: %s", manPage);
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }

        return sections;
    }

    public static String[] splitLines(String str) {
        return str.split("\r\n|\r|\n");
    }

    private static String trimWhiteSpaceFromTheBeginingAndEndOFEveryLine(String value) {
        String [] lines = splitLines(value);
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            result.append(line.trim()).append("\n");
        }

        return result.toString().trim();
    }
}
