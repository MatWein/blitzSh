package blitzsh.app.ui.settings;

import blitzsh.app.common.components.FormGroup;
import blitzsh.app.settings.model.SshConfiguration;
import blitzsh.app.utils.Messages;

import javax.swing.*;

import static blitzsh.app.utils.Messages.MessageKey.APP_NAME;

public class SshConfigurationPanel extends ConfigurationPanel<SshConfiguration> {
    private JTextField hostTextfield;
    private FormGroup<JTextField> hostGroup;

    public SshConfigurationPanel(SshConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected void initPanel(JPanel mainPanel) {
        hostTextfield = new JTextField(getConfiguration().getHost());
        addKeyBindingListener(hostTextfield, (newValue) -> getConfiguration().setHost(newValue));
        hostGroup = new FormGroup<>(Messages.get(APP_NAME), LEFT_SPACE, hostTextfield);
        mainPanel.add(hostGroup);
    }
}
