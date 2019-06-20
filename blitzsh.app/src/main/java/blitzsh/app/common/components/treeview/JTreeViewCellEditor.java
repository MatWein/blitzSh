package blitzsh.app.common.components.treeview;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.EventObject;

public class JTreeViewCellEditor extends DefaultTreeCellEditor {
    private String currentValue;

    public JTreeViewCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
        super(tree, renderer);
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        TreeViewNode node = (TreeViewNode) value;
        this.currentValue = node.getText();

        return super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
    }

    @Override
    public boolean isCellEditable(EventObject event) {
        return super.isCellEditable(event) && ((TreeViewNode) lastPath.getLastPathComponent()).isEditable();
    }

    @Override
    public Object getCellEditorValue() {
        Object valueToSave = super.getCellEditorValue();
        if (valueToSave == null || StringUtils.isBlank(valueToSave.toString())) {
            return currentValue;
        } else {
            return valueToSave;
        }
    }
}
