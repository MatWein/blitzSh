package blitzsh.app.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class ProcessUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessUtils.class);

    public static String readOutputFromProcess(String... commands) {
        StringBuilder output = new StringBuilder();

        Consumer<String> lineConsumer = (line) -> {
            output.append(line);
            output.append("\n");
        };
        streamProcessOutput(lineConsumer, commands);

        return output.toString();
    }

    public static void streamProcessOutput(Consumer<String> lineConsumer, String... commands) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (var inputStream = process.getInputStream();
                 var inputStreamReader = new InputStreamReader(inputStream);
                 var in = new BufferedReader(inputStreamReader)) {

                String line;
                while ((line = in.readLine()) != null) {
                    lineConsumer.accept(line);
                }
            }

            process.waitFor();
        } catch (Throwable e) {
            String message = "Error on running command";
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
}
