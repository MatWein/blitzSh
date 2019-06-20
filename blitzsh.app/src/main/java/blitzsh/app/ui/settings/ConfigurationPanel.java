package blitzsh.app.ui.settings;

import blitzsh.app.common.components.FormGroup;
import blitzsh.app.common.components.Separator;
import blitzsh.app.common.components.table.ColorEditor;
import blitzsh.app.common.components.table.ColorRenderer;
import blitzsh.app.settings.model.BaseConfiguration;
import blitzsh.app.ui.settings.model.ConfigurationColorTableModel;
import blitzsh.app.ui.settings.model.ConfigurationNumberTableModel;
import blitzsh.app.utils.Messages;

import javax.swing.*;
import java.awt.*;

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
        FormGroup<JScrollPane> numbersFormGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_NUMBERS), LEFT_SPACE, 150, new JScrollPane(numberTable));
        mainPanel.add(numbersFormGroup);

        JTable colorTable = new JTable();
        colorTable.setFillsViewportHeight(true);
        colorTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        ColorRenderer colorRenderer = new ColorRenderer(true);
        colorTable.setDefaultRenderer(Color.class, colorRenderer);
        ColorEditor colorEditor = new ColorEditor();
        colorTable.setDefaultEditor(Color.class, colorEditor);
        colorTable.setModel(new ConfigurationColorTableModel(configuration));
        colorTable.getColumnModel().getColumn(1).setCellRenderer(colorRenderer);
        colorTable.getColumnModel().getColumn(1).setCellEditor(colorEditor);
        colorTable.getColumnModel().getColumn(0).setHeaderValue(Messages.get(SETTINGS_PANEL_COLORS_HEADERS_DESC));
        colorTable.getColumnModel().getColumn(1).setHeaderValue(Messages.get(SETTINGS_PANEL_COLORS_HEADERS_VALUE));
        FormGroup<JScrollPane> colorFormGroup = new FormGroup<>(Messages.get(SETTINGS_PANEL_COLORS), LEFT_SPACE, 150, new JScrollPane(colorTable));
        mainPanel.add(colorFormGroup);

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
}
