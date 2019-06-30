package blitzsh.app.ui.settings;

import blitzsh.app.common.components.FormGroup;
import blitzsh.app.common.components.Separator;
import blitzsh.app.common.components.table.ColorEditor;
import blitzsh.app.common.components.table.ColorRenderer;
import blitzsh.app.settings.model.BaseConfiguration;
import blitzsh.app.settings.model.TerminalConfiguration;
import blitzsh.app.ui.settings.model.ConfigurationColorTableModel;
import blitzsh.app.ui.settings.model.ConfigurationNumberTableModel;
import blitzsh.app.utils.Messages;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.function.Consumer;

import static blitzsh.app.utils.Messages.MessageKey.*;

public abstract class ConfigurationPanel<T extends BaseConfiguration> extends JPanel {
    static final int LEFT_SPACE = 200;

    private final T configuration;
    private final JLabel headerLabel;

    public ConfigurationPanel(T configuration) {
        super(new BorderLayout(0, 0));
        this.configuration = configuration;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        headerLabel = new JLabel(configuration.getName());
        headerLabel.setFont(headerLabel.getFont().deriveFont(headerLabel.getFont().getStyle() | Font.BOLD));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(headerLabel);

        mainPanel.add(new Separator(JSeparator.HORIZONTAL));

        initPanel(mainPanel);

        JTable numberTable = new JTable();
        numberTable.setFillsViewportHeight(true);
        numberTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        numberTable.setModel(new ConfigurationNumberTableModel(configuration));
        numberTable.getColumnModel().getColumn(0).setHeaderValue(Messages.get(SETTINGS_PANEL_NUMBERS_HEADERS_DESC));
        numberTable.getColumnModel().getColumn(1).setHeaderValue(Messages.get(SETTINGS_PANEL_NUMBERS_HEADERS_VALUE));
        numberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        FormGroup<JScrollPane> numbersFormGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_NUMBERS), LEFT_SPACE, 150, new JScrollPane(numberTable));
        mainPanel.add(numbersFormGroup);

        JButton resetNumbersButton = new JButton(Messages.get(SETTINGS_PANEL_RESET_VALUES));
        resetNumbersButton.addActionListener((e) -> {
            TerminalConfiguration newConfig = new TerminalConfiguration();

            configuration.setAlpha(newConfig.getAlpha());
            configuration.setBlinkRate(newConfig.getBlinkRate());
            configuration.setBufferMaxLinesCount(newConfig.getBufferMaxLinesCount());
            configuration.setColumns(newConfig.getColumns());
            configuration.setLines(newConfig.getLines());

            numberTable.revalidate();
            numberTable.repaint();
        });
        Box resetNumbersButtonBox = Box.createHorizontalBox();
        resetNumbersButtonBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        resetNumbersButtonBox.add(Box.createHorizontalGlue());
        resetNumbersButtonBox.add(resetNumbersButton);
        mainPanel.add(resetNumbersButtonBox);

        JTable colorTable = new JTable();
        colorTable.setFillsViewportHeight(true);
        colorTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        ColorRenderer colorRenderer = new ColorRenderer(true);
        colorTable.setDefaultRenderer(Color.class, colorRenderer);
        ColorEditor colorEditor = new ColorEditor();
        colorTable.setDefaultEditor(Color.class, colorEditor);
        colorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        colorTable.setModel(new ConfigurationColorTableModel(configuration));
        colorTable.getColumnModel().getColumn(1).setCellRenderer(colorRenderer);
        colorTable.getColumnModel().getColumn(1).setCellEditor(colorEditor);
        colorTable.getColumnModel().getColumn(0).setHeaderValue(Messages.get(SETTINGS_PANEL_COLORS_HEADERS_DESC));
        colorTable.getColumnModel().getColumn(1).setHeaderValue(Messages.get(SETTINGS_PANEL_COLORS_HEADERS_VALUE));
        FormGroup<JScrollPane> colorFormGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_COLORS), LEFT_SPACE, 150, new JScrollPane(colorTable));
        mainPanel.add(colorFormGroup);

        JButton resetColorsButton = new JButton(Messages.get(SETTINGS_PANEL_RESET_VALUES));
        resetColorsButton.addActionListener((e) -> {
            TerminalConfiguration newConfig = new TerminalConfiguration();
            
            configuration.setTerminalBackground(newConfig.getTerminalBackground());
            configuration.setTerminalForeground(newConfig.getTerminalForeground());
            configuration.setSelectionBackground(newConfig.getSelectionBackground());
            configuration.setSelectionForeground(newConfig.getSelectionForeground());
            configuration.setFoundPatternBackground(newConfig.getFoundPatternBackground());
            configuration.setFoundPatternForeground(newConfig.getFoundPatternForeground());
            configuration.setHyperlinkBackground(newConfig.getHyperlinkBackground());
            configuration.setHyperlinkForeground(newConfig.getHyperlinkForeground());
            configuration.setMappingBlack(newConfig.getMappingBlack());
            configuration.setMappingRed(newConfig.getMappingRed());
            configuration.setMappingGreen(newConfig.getMappingGreen());
            configuration.setMappingYellow(newConfig.getMappingYellow());
            configuration.setMappingBlue(newConfig.getMappingBlue());
            configuration.setMappingMagenta(newConfig.getMappingMagenta());
            configuration.setMappingCyan(newConfig.getMappingCyan());
            configuration.setMappingWhite(newConfig.getMappingWhite());
            configuration.setMappingBrightBlack(newConfig.getMappingBrightBlack());
            configuration.setMappingBrightRed(newConfig.getMappingBrightRed());
            configuration.setMappingBrightGreen(newConfig.getMappingBrightGreen());
            configuration.setMappingBrightYellow(newConfig.getMappingBrightYellow());
            configuration.setMappingBrightBlue(newConfig.getMappingBrightBlue());
            configuration.setMappingBrightMagenta(newConfig.getMappingBrightMagenta());
            configuration.setMappingBrightCyan(newConfig.getMappingBrightCyan());
            configuration.setMappingBrightWhite(newConfig.getMappingBrightWhite());

            colorTable.revalidate();
            colorTable.repaint();
        });
        Box resetColorsButtonBox = Box.createHorizontalBox();
        resetColorsButtonBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        resetColorsButtonBox.add(Box.createHorizontalGlue());
        resetColorsButtonBox.add(resetColorsButton);
        mainPanel.add(resetColorsButtonBox);

        mainPanel.add(Box.createVerticalStrut(10));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    protected abstract void initPanel(JPanel mainPanel);

    public T getConfiguration() {
        return configuration;
    }

    public JLabel getHeaderLabel() {
        return headerLabel;
    }

    protected void addKeyBindingListener(JTextComponent textField, Consumer<String> onChangeAction) {
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

    protected ActionListener createFileChooseListener(JTextComponent textField, boolean onlyDirectories) {
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
}
