package blitzsh.app.ui.settings.model;

import blitzsh.app.settings.ConfigurationValidator;
import blitzsh.app.settings.model.BaseConfiguration;
import blitzsh.app.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;

import static blitzsh.app.utils.Messages.MessageKey.*;

public class ConfigurationNumberTableModel extends AbstractTableModel {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationNumberTableModel.class);

    private final BaseConfiguration configuration;

    public ConfigurationNumberTableModel(BaseConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public int getRowCount() {
        return 7;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            switch (rowIndex) {
                case 0: return Messages.get(SETTINGS_PANEL_NUMBERS_COLUMNS);
                case 1: return Messages.get(SETTINGS_PANEL_NUMBERS_LINES);
                case 2: return Messages.get(SETTINGS_PANEL_NUMBERS_BUFFER);
                case 3: return Messages.get(SETTINGS_PANEL_NUMBERS_BLINK_RATE);
                case 4: return Messages.get(SETTINGS_PANEL_NUMBERS_ALPHA);
                case 5: return Messages.get(SETTINGS_PANEL_NUMBERS_FONT);
                case 6: return Messages.get(SETTINGS_PANEL_NUMBERS_FONT_SIZE);
            }
        } else if (columnIndex == 1) {
            switch (rowIndex) {
                case 0: return configuration.getColumns();
                case 1: return configuration.getLines();
                case 2: return configuration.getBufferMaxLinesCount();
                case 3: return configuration.getBlinkRate();
                case 4: return configuration.getAlpha();
                case 5: return configuration.getFont();
                case 6: return configuration.getFontSize();
            }
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        String newValue = (String) aValue;
        if (columnIndex == 1) {
            switch (rowIndex) {
                case 0: configuration.setColumns(toInt(newValue, ConfigurationValidator.MIN_COLUMNS, ConfigurationValidator.MAX_COLUMNS, configuration.getColumns())); break;
                case 1: configuration.setLines(toInt(newValue, ConfigurationValidator.MIN_LINES, ConfigurationValidator.MAX_LINES, configuration.getLines())); break;
                case 2: configuration.setBufferMaxLinesCount(toInt(newValue, ConfigurationValidator.MIN_BUFFER_MAX_LINES_COUNT, ConfigurationValidator.MAX_BUFFER_MAX_LINES_COUNT, configuration.getBufferMaxLinesCount())); break;
                case 3: configuration.setBlinkRate(toInt(newValue, 0, 10 * 10000, configuration.getBlinkRate())); break;
                case 4: configuration.setAlpha(toInt(newValue, 10, 100, configuration.getAlpha())); break;
                case 5: configuration.setFont(newValue); break;
                case 6: configuration.setFontSize(toFloat(newValue, 8, 50, configuration.getFontSize())); break;
            }
        }
    }

    private int toInt(String newValue, int min, int max, int defaultValue) {
        try {
            int value = Integer.valueOf(newValue);
            if (value >= min && value <= max) {
                return value;
            } else {
                return defaultValue;
            }
        } catch (Throwable e) {
            LOGGER.warn(e.getMessage());
            return defaultValue;
        }
    }

    private float toFloat(String newValue, float min, float max, float defaultValue) {
        try {
            float value = Float.valueOf(newValue.replace(",", "."));
            if (value >= min && value <= max) {
                return value;
            } else {
                return defaultValue;
            }
        } catch (Throwable e) {
            LOGGER.warn(e.getMessage());
            return defaultValue;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }
}
