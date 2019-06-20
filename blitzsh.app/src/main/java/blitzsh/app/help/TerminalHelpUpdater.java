package blitzsh.app.help;

import blitzsh.app.common.model.enums.TerminalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TerminalHelpUpdater {
    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalHelpUpdater.class);

    public static void updateHelp(TerminalType terminalType, String terminalExec) {
        switch (terminalType) {
            case SHELL_MAN:
                ShellManTerminalHelpUpdater.updateHelp("bash", terminalExec, (current, max, message) -> System.out.println(message));
                break;
            case WINDOWS_CMD:
            case WINDOWS_POWERSHELL:
            case BLITZSH:
                String message = String.format("Generating help for unknown type '%s' is not supported.", terminalType);
                LOGGER.error(message);
                throw new IllegalArgumentException(message);
        }
    }
}
