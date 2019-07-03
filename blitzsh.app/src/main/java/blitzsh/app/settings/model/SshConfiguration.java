package blitzsh.app.settings.model;

public class SshConfiguration extends BaseConfiguration {
    private String host;
    private int port = 22;
    private String userName;
    private String password;
    private String privateKey;
    private String publicKey;
    private String passphraseForPrivateKey;

    private boolean promptIdentityYesNo;
    private boolean promptPassphrase;
    private boolean promptPassword;
    private boolean promptMessages;

    private boolean keepSshSessionAlive;

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

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPassphraseForPrivateKey() {
        return passphraseForPrivateKey;
    }

    public void setPassphraseForPrivateKey(String passphraseForPrivateKey) {
        this.passphraseForPrivateKey = passphraseForPrivateKey;
    }

    public boolean isPromptIdentityYesNo() {
        return promptIdentityYesNo;
    }

    public void setPromptIdentityYesNo(boolean promptIdentityYesNo) {
        this.promptIdentityYesNo = promptIdentityYesNo;
    }

    public boolean isPromptPassphrase() {
        return promptPassphrase;
    }

    public void setPromptPassphrase(boolean promptPassphrase) {
        this.promptPassphrase = promptPassphrase;
    }

    public boolean isPromptPassword() {
        return promptPassword;
    }

    public void setPromptPassword(boolean promptPassword) {
        this.promptPassword = promptPassword;
    }

    public boolean isPromptMessages() {
        return promptMessages;
    }

    public void setPromptMessages(boolean promptMessages) {
        this.promptMessages = promptMessages;
    }

    public boolean isKeepSshSessionAlive() {
        return keepSshSessionAlive;
    }

    public void setKeepSshSessionAlive(boolean keepSshSessionAlive) {
        this.keepSshSessionAlive = keepSshSessionAlive;
    }
}
