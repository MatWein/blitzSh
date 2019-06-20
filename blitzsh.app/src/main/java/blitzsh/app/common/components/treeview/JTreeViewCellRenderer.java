package blitzsh.app.common.components.treeview;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class JTreeViewCellRenderer extends DefaultTreeCellRenderer {
    public JTreeViewCellRenderer() {
        this.setBorder(BorderFactory.createEmptyBorder(2, 1, 2, 1));
    }

    @Override
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean selected,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        TreeViewNode node = (TreeViewNode) value;

        this.setText(node.getText());

        if (node.getIcon() != null) {
            this.setIcon(node.getIcon());
        } else {
            this.setIcon(null);
        }

        return this;
    }
}
