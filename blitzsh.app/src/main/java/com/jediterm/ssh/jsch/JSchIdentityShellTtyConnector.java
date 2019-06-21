package com.jediterm.ssh.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class JSchIdentityShellTtyConnector extends JSchShellTtyConnector {
    private final File privateKey;
    private final File publicKey;
    private final String passphraseForPrivateKey;

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

    @Override
    protected void configureSession(Session session, Properties config) throws JSchException {
        super.configureSession(session, config);

        session.setUserInfo(new UserInfoWrapper(session.getUserInfo()));
    }
}
