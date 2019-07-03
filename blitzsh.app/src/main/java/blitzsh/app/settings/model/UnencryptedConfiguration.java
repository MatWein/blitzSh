package blitzsh.app.settings.model;

public class UnencryptedConfiguration extends ConfigurationWrapper<TerminalConfigurationFolder> {
    public UnencryptedConfiguration() {
    }

    public UnencryptedConfiguration(TerminalConfigurationFolder config) {
        super(false, config);
    }
}
