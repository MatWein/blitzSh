package blitzsh.app.ui.settings;

import blitzsh.app.common.components.FormGroup;
import blitzsh.app.settings.model.TerminalConfiguration;
import blitzsh.app.utils.Messages;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static blitzsh.app.help.ShellManTerminalHelpUpdater.splitLines;
import static blitzsh.app.utils.Messages.MessageKey.*;

public class TerminalConfigurationPanel extends ConfigurationPanel<TerminalConfiguration> {
    private JTextField commandTextfield;
    private JTextField workingDirTextfield;
    private JTextField charsetTextfield;
    private JTextArea environmentTextArea;
    private JCheckBox consoleCheckbox;
    private JCheckBox cygwinCheckbox;
    private FormGroup<JTextField> commandFormGroup;
    private JButton commandBrowseButton;
    private FormGroup<JTextField> workingDirGroup;
    private JButton workingDirBrowseButton;
    private FormGroup<JTextField> charsetGroup;
    private FormGroup<JScrollPane> environmentGroup;
    private FormGroup<JPanel> hintGroup;
    private FormGroup<JCheckBox> consoleGroup;
    private FormGroup<JCheckBox> cygwinGroup;

    public TerminalConfigurationPanel(TerminalConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected void initPanel(JPanel mainPanel) {
        commandTextfield = new JTextField(StringUtils.join(getConfiguration().getCommand()));
        addKeyBindingListener(commandTextfield, (newValue) -> getConfiguration().setCommand(new String[] { newValue }));
        commandBrowseButton = new JButton(Messages.get(SETTINGS_PANEL_COMMAND_BROWSE));
        commandBrowseButton.addActionListener(createFileChooseListener(commandTextfield, false));
        commandFormGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_COMMAND), LEFT_SPACE, commandTextfield, commandBrowseButton);
        mainPanel.add(commandFormGroup);

        workingDirTextfield = new JTextField(getConfiguration().getWorkingDir());
        addKeyBindingListener(workingDirTextfield, (newValue) -> getConfiguration().setWorkingDir(newValue));
        workingDirBrowseButton = new JButton(Messages.get(SETTINGS_PANEL_WORKING_DIR_BROWSE));
        workingDirBrowseButton.addActionListener(createFileChooseListener(workingDirTextfield, true));
        workingDirGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_WORKING_DIR), LEFT_SPACE, workingDirTextfield, workingDirBrowseButton);
        mainPanel.add(workingDirGroup);

        charsetTextfield = new JTextField(getConfiguration().getCharset());
        addKeyBindingListener(charsetTextfield, (newValue) -> getConfiguration().setCharset(newValue));
        charsetGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_CHARSET), LEFT_SPACE, charsetTextfield);
        mainPanel.add(charsetGroup);

        environmentTextArea = new JTextArea(formatEnvironment(getConfiguration().getEnvironment()));
        addKeyBindingListener(environmentTextArea, (newValue) -> {
            try {
                getConfiguration().setEnvironment(parseEnvironment(newValue));
            } catch (Throwable ignored){}
        });
        environmentGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_ENVIRONMENT), LEFT_SPACE, 100, new JScrollPane(environmentTextArea));
        mainPanel.add(environmentGroup);

        JPanel hintPanel = new JPanel();
        hintPanel.setLayout(new BoxLayout(hintPanel, BoxLayout.Y_AXIS));

        JLabel hint1Label = new JLabel(Messages.get(SETTINGS_PANEL_ENVIRONMENT_HINT1));
        hint1Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        hintPanel.add(hint1Label);

        JLabel hint2Label = new JLabel(Messages.get(SETTINGS_PANEL_ENVIRONMENT_HINT2));
        hint2Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        hintPanel.add(hint2Label);

        JLabel hint3Label = new JLabel(Messages.get(SETTINGS_PANEL_ENVIRONMENT_HINT3));
        hint3Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        hintPanel.add(hint3Label);

        hintGroup = new FormGroup<>("", LEFT_SPACE, 57, hintPanel);
        hintGroup.setBorder(null);
        mainPanel.add(hintGroup);

        consoleCheckbox = new JCheckBox();
        consoleCheckbox.setSelected(getConfiguration().isConsole());
        consoleCheckbox.addChangeListener(e -> getConfiguration().setConsole(consoleCheckbox.isSelected()));
        consoleGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_CONSOLE), LEFT_SPACE, consoleCheckbox);
        mainPanel.add(consoleGroup);

        cygwinCheckbox = new JCheckBox();
        cygwinCheckbox.setSelected(getConfiguration().isCygwin());
        cygwinCheckbox.addChangeListener(e -> getConfiguration().setCygwin(cygwinCheckbox.isSelected()));
        cygwinGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_CYGWIN), LEFT_SPACE, cygwinCheckbox);
        mainPanel.add(cygwinGroup);
    }

    private ActionListener createFileChooseListener(JTextComponent textField, boolean onlyDirectories) {
        return e -> {
            JFileChooser chooser = new JFileChooser();
            if (onlyDirectories) {
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            }

            int result = chooser.showOpenDialog(getRootPane());
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                textField.setText(selectedFile.getAbsolutePath());
            }
        };
    }

    private Map<String, String> parseEnvironment(String value) {
        if (StringUtils.isBlank(value)) {
            return new HashMap<>();
        }

        return Arrays.stream(splitLines(value))
                .map((line) -> line.split("="))
                .collect(Collectors.toMap((parts) -> parts[0], (parts) -> parts[1]));
    }

    private String formatEnvironment(Map<String, String> environment) {
        return environment.entrySet().stream()
                .map((entry) -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private void addKeyBindingListener(JTextComponent textField, Consumer<String> onChangeAction) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                callAction();
            }

            public void removeUpdate(DocumentEvent e) {
                callAction();
            }

            public void insertUpdate(DocumentEvent e) {
                callAction();
            }

            public void callAction() {
                onChangeAction.accept(textField.getText());
            }
        });
    }
}
