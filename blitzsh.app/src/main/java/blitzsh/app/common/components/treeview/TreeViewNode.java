package blitzsh.app.common.components.treeview;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeViewNode extends DefaultMutableTreeNode {
    private Object data;
    private ImageIcon icon;
    private boolean editable = true;

    public TreeViewNode(String text) {
        super(text);
    }

    public TreeViewNode(String text, Object data) {
        super(text);
        setData(data);
    }

    @Override
    public TreeViewNode getParent() {
        return (TreeViewNode)super.getParent();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getText() {
        if (getUserObject() == null) {
            return "";
        } else {
            return getUserObject().toString();
        }
    }
}
