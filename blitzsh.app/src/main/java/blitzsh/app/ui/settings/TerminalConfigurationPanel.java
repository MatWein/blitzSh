package blitzsh.app.ui.settings;

import blitzsh.app.common.components.FormGroup;
import blitzsh.app.settings.model.TerminalConfiguration;
import blitzsh.app.utils.Messages;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static blitzsh.app.help.ShellManTerminalHelpUpdater.splitLines;
import static blitzsh.app.utils.Messages.MessageKey.*;

public class TerminalConfigurationPanel extends ConfigurationPanel<TerminalConfiguration> {
    public static final String COMMAND_SPLIT_REGEX = " (?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    public TerminalConfigurationPanel(TerminalConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected void initPanel(JPanel mainPanel) {
        JTextField commandTextfield = new JTextField(StringUtils.join(getConfiguration().getCommand()));
        addKeyBindingListener(commandTextfield, (newValue) -> getConfiguration().setCommand(splitCommand(newValue)));
        JButton commandBrowseButton = new JButton(Messages.get(SETTINGS_PANEL_COMMAND_BROWSE));
        commandBrowseButton.addActionListener(createFileChooseListener(commandTextfield, false));
        FormGroup<JTextField> commandFormGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_COMMAND), LEFT_SPACE, commandTextfield, commandBrowseButton);
        mainPanel.add(commandFormGroup);

        JTextField workingDirTextfield = new JTextField(getConfiguration().getWorkingDir());
        addKeyBindingListener(workingDirTextfield, (newValue) -> getConfiguration().setWorkingDir(newValue));
        JButton workingDirBrowseButton = new JButton(Messages.get(SETTINGS_PANEL_WORKING_DIR_BROWSE));
        workingDirBrowseButton.addActionListener(createFileChooseListener(workingDirTextfield, true));
        FormGroup<JTextField> workingDirGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_WORKING_DIR), LEFT_SPACE, workingDirTextfield, workingDirBrowseButton);
        mainPanel.add(workingDirGroup);

        JTextField charsetTextfield = new JTextField(getConfiguration().getCharset());
        addKeyBindingListener(charsetTextfield, (newValue) -> getConfiguration().setCharset(newValue));
        FormGroup<JTextField> charsetGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_CHARSET), LEFT_SPACE, charsetTextfield);
        mainPanel.add(charsetGroup);

        JTextArea environmentTextArea = new JTextArea(formatEnvironment(getConfiguration().getEnvironment()));
        addKeyBindingListener(environmentTextArea, (newValue) -> {
            try {
                getConfiguration().setEnvironment(parseEnvironment(newValue));
            } catch (Throwable ignored){}
        });
        FormGroup<JScrollPane> environmentGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_ENVIRONMENT), LEFT_SPACE, 100, new JScrollPane(environmentTextArea));
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

        FormGroup<JPanel> hintGroup = new FormGroup<>("", LEFT_SPACE, 57, hintPanel);
        hintGroup.setBorder(null);
        mainPanel.add(hintGroup);

        JCheckBox consoleCheckbox = new JCheckBox();
        consoleCheckbox.setSelected(getConfiguration().isConsole());
        consoleCheckbox.addChangeListener(e -> getConfiguration().setConsole(consoleCheckbox.isSelected()));
        FormGroup<JCheckBox> consoleGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_CONSOLE), LEFT_SPACE, consoleCheckbox);
        mainPanel.add(consoleGroup);

        JCheckBox cygwinCheckbox = new JCheckBox();
        cygwinCheckbox.setSelected(getConfiguration().isCygwin());
        cygwinCheckbox.addChangeListener(e -> getConfiguration().setCygwin(cygwinCheckbox.isSelected()));
        FormGroup<JCheckBox> cygwinGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_CYGWIN), LEFT_SPACE, cygwinCheckbox);
        mainPanel.add(cygwinGroup);

        JCheckBox copyOnSelectCheckbox = new JCheckBox();
        copyOnSelectCheckbox.setSelected(getConfiguration().isCopyOnSelect());
        copyOnSelectCheckbox.addChangeListener(e -> getConfiguration().setCopyOnSelect(copyOnSelectCheckbox.isSelected()));
        FormGroup<JCheckBox> copyOnSelectGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_COPY_ON_SELECT), LEFT_SPACE, copyOnSelectCheckbox);
        mainPanel.add(copyOnSelectGroup);

        JCheckBox pasteOnMiddleMouseClickCheckbox = new JCheckBox();
        pasteOnMiddleMouseClickCheckbox.setSelected(getConfiguration().isPasteOnMiddleMouseClick());
        pasteOnMiddleMouseClickCheckbox.addChangeListener(e -> getConfiguration().setPasteOnMiddleMouseClick(pasteOnMiddleMouseClickCheckbox.isSelected()));
        FormGroup<JCheckBox> pasteOnMiddleMouseClickGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_PASTE_ON_MIDDLE_MOUSE), LEFT_SPACE, pasteOnMiddleMouseClickCheckbox);
        mainPanel.add(pasteOnMiddleMouseClickGroup);

        JCheckBox pasteOnShiftInsertCheckbox = new JCheckBox();
        pasteOnShiftInsertCheckbox.setSelected(getConfiguration().isPasteOnShiftInsert());
        pasteOnShiftInsertCheckbox.addChangeListener(e -> getConfiguration().setPasteOnShiftInsert(pasteOnShiftInsertCheckbox.isSelected()));
        FormGroup<JCheckBox> pasteOnShiftInsertGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_PASTE_ON_SHIFT_INSERT), LEFT_SPACE, pasteOnShiftInsertCheckbox);
        mainPanel.add(pasteOnShiftInsertGroup);
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

    static String[] splitCommand(String command) {
        return Arrays.stream(command.split(COMMAND_SPLIT_REGEX))
                .map((in) -> StringUtils.removeStart(in, "\""))
                .map((in) -> StringUtils.removeEnd(in, "\""))
                .toArray(String[]::new);
    }
}
