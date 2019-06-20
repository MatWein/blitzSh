package com.jediterm.ssh.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class JSchIdentityShellTtyConnector extends JSchShellTtyConnector {
    private File privateKey;
    private File publicKey;
    private String passphraseForPrivateKey;

    public JSchIdentityShellTtyConnector() {
    }

    public JSchIdentityShellTtyConnector(String host, String user, String password) {
        super(host, user, password);
    }

    public JSchIdentityShellTtyConnector(String host, int port, String user, String password) {
        super(host, port, user, password);
    }

    public JSchIdentityShellTtyConnector(String host, int port, String user, String password, File privateKey, File publicKey, String passphraseForPrivateKey) {
        super(host, port, user, password);

        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.passphraseForPrivateKey = passphraseForPrivateKey;
    }

    @Override
    protected void configureJSch(JSch jsch) throws JSchException {
        if (privateKey != null && passphraseForPrivateKey != null) {
            jsch.addIdentity(
                    privateKey.getAbsolutePath(),
                    publicKey == null ? null : publicKey.getAbsolutePath(),
                    passphraseForPrivateKey.getBytes(StandardCharsets.UTF_8));
        }
    }
}
