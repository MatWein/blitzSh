package blitzsh.app.settings.model;

import java.io.Serializable;

public class ConfigurationWrapper<T> implements Serializable {
    private boolean encrypted;
    private T config;

    public ConfigurationWrapper() {
    }

    public ConfigurationWrapper(boolean encrypted, T config) {
        this.encrypted = encrypted;
        this.config = config;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public T getConfig() {
        return config;
    }

    public void setConfig(T config) {
        this.config = config;
    }
}
