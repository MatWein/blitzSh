package blitzsh.app.common.components.treeview;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.util.Optional;

public class JTreeView extends JTree {
    public JTreeView(TreeViewNode root) {
        super(root);

        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        JTreeViewCellRenderer cellRenderer = new JTreeViewCellRenderer();
        this.setRowHeight(0);
        this.setCellRenderer(cellRenderer);

        JTreeViewCellEditor cellEditor = new JTreeViewCellEditor(this, cellRenderer);
        this.setCellEditor(cellEditor);
    }

    @Override
    public Optional<TreeViewNode> getLastSelectedPathComponent() {
        return Optional.ofNullable((TreeViewNode)super.getLastSelectedPathComponent());
    }

    public void reload() {
        getModel().reload();
    }

    public void reload(TreeViewNode node) {
        getModel().reload(node);
    }

    public TreeViewNode getRootNode() {
        return (TreeViewNode)getModel().getRoot();
    }

    @Override
    public DefaultTreeModel getModel() {
        return (DefaultTreeModel)super.getModel();
    }

    public void startEditingAtNode(TreeViewNode node) {
        startEditingAtPath(new TreePath(node.getPath()));
    }

    public void removeNode(TreeViewNode node) {
        removeNode(node, true);
    }

    public void removeNode(TreeViewNode node, boolean selectParent) {
        TreeViewNode parent = node.getParent();
        if (parent == null) {
            return;
        }

        if (selectParent) {
            selectNode(parent, false, true, false);
        }

        parent.remove(node);
        reload(parent);
    }

    public void addNode(Optional<TreeViewNode> parent, TreeViewNode node) {
        addNode(parent, node, true, true);
    }

    public void addNode(Optional<TreeViewNode> parent, TreeViewNode node, boolean select, boolean expand) {
        if (parent.isPresent()) {
            parent.get().add(node);
            reload(parent.get());
        } else {
            getRootNode().add(node);
            reload();
        }

        selectNode(node, false, select, expand);
    }

    public void selectNode(TreeViewNode node, boolean reloadBefore, boolean select, boolean expand) {
        if (reloadBefore) {
            reload();
        }

        if (expand) {
            expandPath(new TreePath(node.getPath()));
        }

        if (select) {
            setSelectionPath(new TreePath(node.getPath()));
        }
    }
}
