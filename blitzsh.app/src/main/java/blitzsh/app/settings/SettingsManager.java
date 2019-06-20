package blitzsh.app.settings;

import blitzsh.app.common.model.enums.WorkDir;
import blitzsh.app.settings.model.TerminalConfigurationFolder;
import blitzsh.app.utils.StorageUtils;
import blitzsh.app.utils.json.JsonUtils;

import java.io.File;

public class SettingsManager {
    public static TerminalConfigurationFolder loadTerminalConfigurations() {
        File settingsFile = getTerminalConfigurationsFile();

        if (settingsFile.isFile()) {
            return JsonUtils.fromJsonFile(TerminalConfigurationFolder.class, settingsFile);
        } else {
            return new TerminalConfigurationFolder();
        }
    }

    public static void saveTerminalConfigurations(TerminalConfigurationFolder terminalConfigurations) {
        File settingsFile = getTerminalConfigurationsFile();
        JsonUtils.toJsonFile(terminalConfigurations, settingsFile);
    }

    private static File getTerminalConfigurationsFile() {
        File settingsDir = StorageUtils.getOrCreateWorkDir(WorkDir.SETTINGS);
        return new File(settingsDir, "terminal-configurations.json");
    }
}
