package blitzsh.app.ui.settings.model;

import blitzsh.app.settings.model.BaseConfiguration;
import blitzsh.app.utils.Messages;

import javax.swing.table.AbstractTableModel;
import java.awt.*;

import static blitzsh.app.utils.Messages.MessageKey.*;

public class ConfigurationColorTableModel extends AbstractTableModel {
    private final BaseConfiguration configuration;

    public ConfigurationColorTableModel(BaseConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public int getRowCount() {
        return 24;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            switch (rowIndex) {
                case 0: return Messages.get(SETTINGS_PANEL_TERMINAL_BACKGROUND);
                case 1: return Messages.get(SETTINGS_PANEL_TERMINAL_FOREGROUND);
                case 2: return Messages.get(SETTINGS_PANEL_SELECTION_BACKGROUND);
                case 3: return Messages.get(SETTINGS_PANEL_SELECTION_FOREGROUND);
                case 4: return Messages.get(SETTINGS_PANEL_SEARCH_BACKGROUND);
                case 5: return Messages.get(SETTINGS_PANEL_SEARCH_FOREGROUND);
                case 6: return Messages.get(SETTINGS_PANEL_HYPERLINK_BACKGROUND);
                case 7: return Messages.get(SETTINGS_PANEL_HYPERLINK_FOREGROUND);
                case 8: return Messages.get(SETTINGS_PANEL_MAPPING_BLACK);
                case 9: return Messages.get(SETTINGS_PANEL_MAPPING_RED);
                case 10: return Messages.get(SETTINGS_PANEL_MAPPING_GREEN);
                case 11: return Messages.get(SETTINGS_PANEL_MAPPING_YELLOW);
                case 12: return Messages.get(SETTINGS_PANEL_MAPPING_BLUE);
                case 13: return Messages.get(SETTINGS_PANEL_MAPPING_MAGENTA);
                case 14: return Messages.get(SETTINGS_PANEL_MAPPING_CYAN);
                case 15: return Messages.get(SETTINGS_PANEL_MAPPING_WHITE);
                case 16: return Messages.get(SETTINGS_PANEL_MAPPING_BRIGHT_BLACK);
                case 17: return Messages.get(SETTINGS_PANEL_MAPPING_BRIGHT_RED);
                case 18: return Messages.get(SETTINGS_PANEL_MAPPING_BRIGHT_GREEN);
                case 19: return Messages.get(SETTINGS_PANEL_MAPPING_BRIGHT_YELLOW);
                case 20: return Messages.get(SETTINGS_PANEL_MAPPING_BRIGHT_BLUE);
                case 21: return Messages.get(SETTINGS_PANEL_MAPPING_BRIGHT_MAGENTA);
                case 22: return Messages.get(SETTINGS_PANEL_MAPPING_BRIGHT_CYAN);
                case 23: return Messages.get(SETTINGS_PANEL_MAPPING_BRIGHT_WHITE);
            }
        } else if (columnIndex == 1) {
            switch (rowIndex) {
                case 0: return configuration.getTerminalBackground();
                case 1: return configuration.getTerminalForeground();
                case 2: return configuration.getSelectionBackground();
                case 3: return configuration.getSelectionForeground();
                case 4: return configuration.getFoundPatternBackground();
                case 5: return configuration.getFoundPatternForeground();
                case 6: return configuration.getHyperlinkBackground();
                case 7: return configuration.getHyperlinkForeground();
                case 8: return configuration.getMappingBlack();
                case 9: return configuration.getMappingRed();
                case 10: return configuration.getMappingGreen();
                case 11: return configuration.getMappingYellow();
                case 12: return configuration.getMappingBlue();
                case 13: return configuration.getMappingMagenta();
                case 14: return configuration.getMappingCyan();
                case 15: return configuration.getMappingWhite();
                case 16: return configuration.getMappingBrightBlack();
                case 17: return configuration.getMappingBrightRed();
                case 18: return configuration.getMappingBrightGreen();
                case 19: return configuration.getMappingBrightYellow();
                case 20: return configuration.getMappingBrightBlue();
                case 21: return configuration.getMappingBrightMagenta();
                case 22: return configuration.getMappingBrightCyan();
                case 23: return configuration.getMappingBrightWhite();
            }
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Color newColor = (Color) aValue;
        if (columnIndex == 1) {
            switch (rowIndex) {
                case 0: configuration.setTerminalBackground(newColor); break;
                case 1: configuration.setTerminalForeground(newColor); break;
                case 2: configuration.setSelectionBackground(newColor); break;
                case 3: configuration.setSelectionForeground(newColor); break;
                case 4: configuration.setFoundPatternBackground(newColor); break;
                case 5: configuration.setFoundPatternForeground(newColor); break;
                case 6: configuration.setHyperlinkBackground(newColor); break;
                case 7: configuration.setHyperlinkForeground(newColor); break;
                case 8: configuration.setMappingBlack(newColor); break;
                case 9: configuration.setMappingRed(newColor); break;
                case 10: configuration.setMappingGreen(newColor); break;
                case 11: configuration.setMappingYellow(newColor); break;
                case 12: configuration.setMappingBlue(newColor); break;
                case 13: configuration.setMappingMagenta(newColor); break;
                case 14: configuration.setMappingCyan(newColor); break;
                case 15: configuration.setMappingWhite(newColor); break;
                case 16: configuration.setMappingBrightBlack(newColor); break;
                case 17: configuration.setMappingBrightRed(newColor); break;
                case 18: configuration.setMappingBrightGreen(newColor); break;
                case 19: configuration.setMappingBrightYellow(newColor); break;
                case 20: configuration.setMappingBrightBlue(newColor); break;
                case 21: configuration.setMappingBrightMagenta(newColor); break;
                case 22: configuration.setMappingBrightCyan(newColor); break;
                case 23: configuration.setMappingBrightWhite(newColor); break;
            }
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }
}
