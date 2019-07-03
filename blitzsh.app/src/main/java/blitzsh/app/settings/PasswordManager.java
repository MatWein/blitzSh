package blitzsh.app.settings;

import blitzsh.app.utils.Messages;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;

import static blitzsh.app.utils.Messages.MessageKey.APP_NAME;
import static blitzsh.app.utils.Messages.MessageKey.PASSWORD_MANAGER_QUESTION;

public class PasswordManager {
    private static String PASSWORD_HASH;

    public static String getPasswordHash() {
        if (StringUtils.isBlank(PASSWORD_HASH)) {
            String password = JOptionPane.showInputDialog(null, Messages.get(PASSWORD_MANAGER_QUESTION), Messages.get(APP_NAME), JOptionPane.QUESTION_MESSAGE);
            if (StringUtils.isBlank(password)) {
                PASSWORD_HASH = null;
            } else {
                PASSWORD_HASH = DigestUtils.md5Hex(password);
            }

            return PASSWORD_HASH;
        }

        return PASSWORD_HASH;
    }
}
