package com.jediterm.ssh.jsch;

import com.jcraft.jsch.UserInfo;

public class UserInfoWrapper implements UserInfo {
    private final UserInfo userInfo;

    public UserInfoWrapper(UserInfo userInfo) {
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
        return userInfo.promptPassword(message);
    }

    @Override
    public boolean promptPassphrase(String message) {
        return userInfo.promptPassphrase(message);
    }

    @Override
    public boolean promptYesNo(String message) {
        return true;
    }

    @Override
    public void showMessage(String message) {
        userInfo.showMessage(message);
    }
}
