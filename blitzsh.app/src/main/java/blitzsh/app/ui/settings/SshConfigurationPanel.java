package blitzsh.app.ui.settings;

import blitzsh.app.common.components.FormGroup;
import blitzsh.app.settings.model.SshConfiguration;
import blitzsh.app.utils.Messages;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;

import static blitzsh.app.utils.Messages.MessageKey.*;

public class SshConfigurationPanel extends ConfigurationPanel<SshConfiguration> {
    public SshConfigurationPanel(SshConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected void initPanel(JPanel mainPanel) {
        JTextField hostTextfield = new JTextField(getConfiguration().getHost());
        addKeyBindingListener(hostTextfield, (newValue) -> getConfiguration().setHost(newValue));
        FormGroup<JTextField> hostGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_SSH_HOST), LEFT_SPACE, hostTextfield);
        mainPanel.add(hostGroup);

        JTextField portTextfield = new JTextField(String.valueOf(getConfiguration().getPort()));
        addKeyBindingListener(portTextfield, (newValue) -> { 
            try {
                getConfiguration().setPort(Integer.valueOf(newValue));
            } catch (Throwable ignore) {}
        });
        FormGroup<JTextField> portGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_SSH_PORT), LEFT_SPACE, portTextfield);
        mainPanel.add(portGroup);

        JTextField usernameTextfield = new JTextField(getConfiguration().getUserName());
        addKeyBindingListener(usernameTextfield, (newValue) -> getConfiguration().setUserName(newValue));
        FormGroup<JTextField> usernameGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_SSH_USERNAME), LEFT_SPACE, usernameTextfield);
        mainPanel.add(usernameGroup);

        JPasswordField passwordTextfield = new JPasswordField(getConfiguration().getPassword());
        addKeyBindingListener(passwordTextfield, (newValue) -> getConfiguration().setPassword(newValue));
        FormGroup<JPasswordField> passwordGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_SSH_PASSWORD), LEFT_SPACE, passwordTextfield);
        mainPanel.add(passwordGroup);

        JTextField privateKeyTextfield = new JTextField(StringUtils.join(getConfiguration().getPrivateKey()));
        addKeyBindingListener(privateKeyTextfield, (newValue) -> getConfiguration().setPrivateKey(newValue));
        JButton privateKeyBrowseButton = new JButton(Messages.get(SETTINGS_PANEL_SSH_PRIVATE_KEY_BROWSE));
        privateKeyBrowseButton.addActionListener(createFileChooseListener(privateKeyTextfield, false));
        FormGroup<JTextField> privateKeyFormGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_SSH_PRIVATE_KEY), LEFT_SPACE, privateKeyTextfield, privateKeyBrowseButton);
        mainPanel.add(privateKeyFormGroup);

        JTextField publicKeyTextfield = new JTextField(StringUtils.join(getConfiguration().getPublicKey()));
        addKeyBindingListener(publicKeyTextfield, (newValue) -> getConfiguration().setPublicKey(newValue));
        JButton publicKeyBrowseButton = new JButton(Messages.get(SETTINGS_PANEL_SSH_PUBLIC_KEY_BROWSE));
        publicKeyBrowseButton.addActionListener(createFileChooseListener(publicKeyTextfield, false));
        FormGroup<JTextField> publicKeyFormGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_SSH_PUBLIC_KEY), LEFT_SPACE, publicKeyTextfield, publicKeyBrowseButton);
        mainPanel.add(publicKeyFormGroup);

        JPasswordField passphraseTextfield = new JPasswordField(getConfiguration().getPassphraseForPrivateKey());
        addKeyBindingListener(passphraseTextfield, (newValue) -> getConfiguration().setPassphraseForPrivateKey(newValue));
        FormGroup<JPasswordField> passphraseGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_SSH_PASSPHRASE), LEFT_SPACE, passphraseTextfield);
        mainPanel.add(passphraseGroup);
    }
}
