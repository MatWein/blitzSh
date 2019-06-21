package blitzsh.app.settings;

import blitzsh.app.settings.model.BaseConfiguration;
import blitzsh.app.settings.model.SshConfiguration;
import blitzsh.app.settings.model.TerminalConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;

public class ConfigurationValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationValidator.class);

    public static final int MIN_COLUMNS = 10;
    public static final int MAX_COLUMNS = 1000;
    public static final int MIN_LINES = 10;
    public static final int MAX_LINES = 500;
    public static final int MIN_BUFFER_MAX_LINES_COUNT = 100;
    public static final int MAX_BUFFER_MAX_LINES_COUNT = 100000;
    public static final int MIN_PORT = 0;
    public static final int MAX_PORT = 65535;

    public static boolean isValid(BaseConfiguration configuration) {
        boolean result = true;

        result &= StringUtils.isNotBlank(configuration.getId());
        result &= StringUtils.isNotBlank(configuration.getName());
        result &= configuration.getColumns() >= MIN_COLUMNS;
        result &= configuration.getColumns() <= MAX_COLUMNS;
        result &= configuration.getLines() >= MIN_LINES;
        result &= configuration.getLines() <= MAX_LINES;
        result &= configuration.getBufferMaxLinesCount() >= MIN_BUFFER_MAX_LINES_COUNT;
        result &= configuration.getBufferMaxLinesCount() <= MAX_BUFFER_MAX_LINES_COUNT;
        result &= configuration.getTerminalBackground() != null;
        result &= configuration.getTerminalForeground() != null;

        if (configuration instanceof TerminalConfiguration) {
            var terminalConfiguration = (TerminalConfiguration) configuration;

            result &= terminalConfiguration.getCommand() != null && terminalConfiguration.getCommand().length > 0;
            result &= StringUtils.isNotBlank(terminalConfiguration.getWorkingDir()) && new File(terminalConfiguration.getWorkingDir()).isDirectory();

            try {
                Charset.forName(terminalConfiguration.getCharset());
            } catch (Throwable e) {
                LOGGER.warn("Charset '{}' is invalid.", terminalConfiguration.getCharset());
                return false;
            }
        } else if (configuration instanceof SshConfiguration) {
            var sshConfiguration = (SshConfiguration) configuration;

            result &= StringUtils.isNotBlank(sshConfiguration.getHost());
            result &= StringUtils.isNotBlank(sshConfiguration.getUserName());
            result &= sshConfiguration.getPort() > MIN_PORT;
            result &= sshConfiguration.getPort() < MAX_PORT;
        }

        return result;
    }
}
