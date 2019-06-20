package blitzsh.app.settings.model;

import java.io.File;

public class SshConfiguration extends BaseConfiguration {
    private String host;
    private int port;
    private String userName;
    private String password;
    private File privateKey;
    private File publicKey;
    private String passphraseForPrivateKey;

    public SshConfiguration() {
    }

    public SshConfiguration(String name) {
        super(name);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public File getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(File privateKey) {
        this.privateKey = privateKey;
    }

    public File getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(File publicKey) {
        this.publicKey = publicKey;
    }

    public String getPassphraseForPrivateKey() {
        return passphraseForPrivateKey;
    }

    public void setPassphraseForPrivateKey(String passphraseForPrivateKey) {
        this.passphraseForPrivateKey = passphraseForPrivateKey;
    }
}
