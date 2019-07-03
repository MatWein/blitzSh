package com.jediterm.ssh.jsch;

import blitzsh.app.settings.model.SshConfiguration;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class JSchIdentityShellTtyConnector extends JSchShellTtyConnector {
    private final SshConfiguration configuration;
    private final File privateKey;
    private final File publicKey;
    private final String passphraseForPrivateKey;

    public JSchIdentityShellTtyConnector(SshConfiguration configuration) {
        super(configuration.getHost(), configuration.getPort(), configuration.getUserName(), configuration.getPassword());

        this.configuration = configuration;
        this.privateKey = StringUtils.isBlank(configuration.getPrivateKey()) ? null : new File(configuration.getPrivateKey());
        this.publicKey = StringUtils.isBlank(configuration.getPublicKey()) ? null : new File(configuration.getPublicKey());
        this.passphraseForPrivateKey = configuration.getPassphraseForPrivateKey();
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

        session.setUserInfo(new UserInfoWrapper(configuration, session.getUserInfo()));
        session.setDaemonThread(true);

        if (configuration.isKeepSshSessionAlive()) {
            session.setServerAliveInterval((int)TimeUnit.SECONDS.toMillis(60));
        }
    }
}
