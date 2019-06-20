package blitzsh.app.common.components.table;

import blitzsh.app.utils.Messages;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

import static blitzsh.app.utils.Messages.MessageKey.APP_NAME;

public class ColorEditor extends AbstractCellEditor implements TableCellEditor {
    private Color currentColor;

    public Object getCellEditorValue() {
        return currentColor;
    }

    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column) {
        currentColor = (Color)value;

        JColorChooser colorChooser = new JColorChooser();
        colorChooser.setColor(currentColor);

        JDialog dialog = JColorChooser.createDialog(
                table.getRootPane(),
                Messages.get(APP_NAME),
                true,
                colorChooser,
                (e) -> currentColor = colorChooser.getColor(),
                null);

        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setBackground(currentColor);
        button.addActionListener(((e) -> {
            dialog.setVisible(true);
            fireEditingStopped();
        }));

        return button;
    }
}
