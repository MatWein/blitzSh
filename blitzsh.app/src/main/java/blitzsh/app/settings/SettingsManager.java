package blitzsh.app.settings;

import blitzsh.app.common.model.enums.WorkDir;
import blitzsh.app.settings.model.*;
import blitzsh.app.utils.EncryptionUtils;
import blitzsh.app.utils.Messages;
import blitzsh.app.utils.StorageUtils;
import blitzsh.app.utils.json.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.io.File;

import static blitzsh.app.utils.Messages.MessageKey.APP_NAME;
import static blitzsh.app.utils.Messages.MessageKey.PASSWORD_MANAGER_INVALID;

public class SettingsManager {
    public static final String TERMINAL_CONFIGURATIONS_JSON = "terminal-configurations.json";
    public static final String APP_CONFIGURATION_JSON = "app-configuration.json";

    public static AppConfiguration loadAppConfiguration() {
        File settingsFile = getConfigFile(APP_CONFIGURATION_JSON);

        if (settingsFile.isFile()) {
            return JsonUtils.fromJsonFile(AppConfiguration.class, settingsFile);
        } else {
            return new AppConfiguration();
        }
    }

    public static TerminalConfigurationFolder loadTerminalConfigurations() {
        File settingsFile = getConfigFile(TERMINAL_CONFIGURATIONS_JSON);

        if (settingsFile.isFile()) {
            ConfigurationWrapper configurationWrapper = JsonUtils.fromJsonFile(ConfigurationWrapper.class, settingsFile);

            boolean encrypted = configurationWrapper.isEncrypted();
            if (encrypted) {
                EncryptedConfiguration encryptedConfiguration = JsonUtils.fromJsonFile(EncryptedConfiguration.class, settingsFile);
                return decrypt(encryptedConfiguration.getConfig(), PasswordManager.getPasswordHash());
            } else {
                UnencryptedConfiguration unencryptedConfiguration = JsonUtils.fromJsonFile(UnencryptedConfiguration.class, settingsFile);
                return unencryptedConfiguration.getConfig();
            }
        } else {
            return new TerminalConfigurationFolder();
        }
    }

    public static void saveTerminalConfigurations(TerminalConfigurationFolder terminalConfigurations) {
        AppConfiguration appConfiguration = loadAppConfiguration();
        boolean encrypted = appConfiguration.isEncryptSettings();

        File settingsFile = getConfigFile(TERMINAL_CONFIGURATIONS_JSON);
        if (encrypted) {
            String password = PasswordManager.getPasswordHash();
            if (StringUtils.isBlank(password)) {
                JOptionPane.showMessageDialog(null, Messages.get(PASSWORD_MANAGER_INVALID), Messages.get(APP_NAME), JOptionPane.ERROR_MESSAGE);
                return;
            }

            String encryptedJson = encrypt(terminalConfigurations, password);
            EncryptedConfiguration encryptedConfiguration = new EncryptedConfiguration(encryptedJson);
            JsonUtils.toJsonFile(encryptedConfiguration, settingsFile);
        } else {
            UnencryptedConfiguration unencryptedConfiguration = new UnencryptedConfiguration(terminalConfigurations);
            JsonUtils.toJsonFile(unencryptedConfiguration, settingsFile);
        }
    }

    private static String encrypt(Object objectToEncrypt, String password) {
        String json = JsonUtils.toJson(objectToEncrypt);
        return EncryptionUtils.encryptString(json, password);
    }

    private static TerminalConfigurationFolder decrypt(String encryptedConfigJson, String password) {
        String json = EncryptionUtils.decryptString(encryptedConfigJson, password);
        if (json == null) {
            JOptionPane.showMessageDialog(null, Messages.get(PASSWORD_MANAGER_INVALID), Messages.get(APP_NAME), JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        return JsonUtils.fromJson(TerminalConfigurationFolder.class, json);
    }

    public static void saveAppConfiguration(AppConfiguration appConfiguration) {
        File settingsFile = getConfigFile(APP_CONFIGURATION_JSON);
        JsonUtils.toJsonFile(appConfiguration, settingsFile);
    }

    private static File getConfigFile(String fileName) {
        File settingsDir = StorageUtils.getOrCreateWorkDir(WorkDir.SETTINGS);
        return new File(settingsDir, fileName);
    }
}
