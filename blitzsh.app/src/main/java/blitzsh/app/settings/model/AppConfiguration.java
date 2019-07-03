package blitzsh.app.settings.model;

import java.io.Serializable;

public class AppConfiguration implements Serializable {
    private boolean encryptSettings;

    public boolean isEncryptSettings() {
        return encryptSettings;
    }

    public void setEncryptSettings(boolean encryptSettings) {
        this.encryptSettings = encryptSettings;
    }
}
