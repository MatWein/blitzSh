package blitzsh.app.settings.model;

public class EncryptedConfiguration extends ConfigurationWrapper<String> {
    public EncryptedConfiguration() {
    }

    public EncryptedConfiguration(String config) {
        super(true, config);
    }
}
