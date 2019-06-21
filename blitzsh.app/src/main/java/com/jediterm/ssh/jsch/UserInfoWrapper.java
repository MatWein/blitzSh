package com.jediterm.ssh.jsch;

import blitzsh.app.settings.model.SshConfiguration;
import com.jcraft.jsch.UserInfo;

public class UserInfoWrapper implements UserInfo {
    private final SshConfiguration configuration;
    private final UserInfo userInfo;

    public UserInfoWrapper(SshConfiguration configuration, UserInfo userInfo) {
        this.configuration = configuration;
        this.userInfo = userInfo;
    }

    @Override
    public String getPassphrase() {
        return userInfo.getPassphrase();
    }

    @Override
    public String getPassword() {
        return userInfo.getPassword();
    }

    @Override
    public boolean promptPassword(String message) {
        if (configuration.isPromptPassword()) {
            return userInfo.promptPassword(message);
        }

        return true;
    }

    @Override
    public boolean promptPassphrase(String message) {
        if (configuration.isPromptPassphrase()) {
            return userInfo.promptPassphrase(message);
        }

        return true;
    }

    @Override
    public boolean promptYesNo(String message) {
        if (configuration.isPromptIdentityYesNo()) {
            return userInfo.promptYesNo(message);
        }

        return true;
    }

    @Override
    public void showMessage(String message) {
        if (configuration.isPromptMessages()) {
            userInfo.showMessage(message);
        }
    }
}
