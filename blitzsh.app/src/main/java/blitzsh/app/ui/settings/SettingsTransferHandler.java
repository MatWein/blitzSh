package blitzsh.app.ui.settings;

import blitzsh.app.common.components.treeview.TreeViewNode;
import blitzsh.app.settings.SettingsManager;
import blitzsh.app.settings.model.BaseConfiguration;
import blitzsh.app.settings.model.SshConfiguration;
import blitzsh.app.settings.model.TerminalConfiguration;
import blitzsh.app.settings.model.TerminalConfigurationFolder;
import blitzsh.app.ui.TrayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsTransferHandler extends TransferHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsTransferHandler.class);

    private static final String NODE_MIMETYPE = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + TreeViewNode[].class.getName() + "\"";

    private final DataFlavor nodesFlavor;
    private final DataFlavor[] flavors;
    private final TerminalConfigurationFolder terminalConfigurations;

    private TreeViewNode[] nodesToRemove;

    public SettingsTransferHandler(TerminalConfigurationFolder terminalConfigurations) {
        this.terminalConfigurations = terminalConfigurations;
        try {
            nodesFlavor = new DataFlavor(NODE_MIMETYPE);
            flavors = new DataFlavor[] { nodesFlavor };
        } catch(ClassNotFoundException e) {
            String message = String.format("Error on initializing %s.", getClass().getSimpleName());
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public boolean canImport(TransferSupport support) {
        if(!support.isDrop()) {
            return false;
        }

        support.setShowDropLocation(true);
        if(!support.isDataFlavorSupported(nodesFlavor)) {
            return false;
        }

        // Do not allow a drop on the drag source selections.
        JTree.DropLocation dl = (JTree.DropLocation)support.getDropLocation();
        JTree tree = (JTree)support.getComponent();
        int dropRow = tree.getRowForPath(dl.getPath());
        int[] selRows = tree.getSelectionRows();
        if (selRows == null) {
            return false;
        }

        for (int selRow : selRows) {
            if (selRow == dropRow) {
                return false;
            }
        }

        // Do not allow a non-leaf node to be copied to a level
        // which is less than its source level.
        TreePath dest = dl.getPath();
        TreeViewNode target = (TreeViewNode)dest.getLastPathComponent();
        TreePath path = tree.getPathForRow(selRows[0]);
        TreeViewNode firstNode = (TreeViewNode)path.getLastPathComponent();

        if (!(target.getData() instanceof TerminalConfigurationFolder)) {
            return false;
        }

        // Do not allow MOVE-action drops if a non-leaf node is
        // selected unless all of its children are also selected.
        int action = support.getDropAction();
        if(action == MOVE) {
            return haveCompleteNode(tree);
        }

        return firstNode.getChildCount() <= 0 || target.getLevel() >= firstNode.getLevel();
    }

    private boolean haveCompleteNode(JTree tree) {
        int[] selRows = tree.getSelectionRows();
        if (selRows == null) {
            return false;
        }

        TreePath path = tree.getPathForRow(selRows[0]);
        TreeViewNode first = (TreeViewNode)path.getLastPathComponent();
        int childCount = first.getChildCount();

        // first has children and no children are selected.
        if(childCount > 0 && selRows.length == 1) {
            return false;
        }

        // first may have children.
        for(int i = 1; i < selRows.length; i++) {
            path = tree.getPathForRow(selRows[i]);
            TreeViewNode next = (TreeViewNode)path.getLastPathComponent();

            if(first.isNodeChild(next)) {
                // Found a child of first.
                if(childCount > selRows.length-1) {
                    // Not all children of first are selected.
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree)c;
        TreePath[] paths = tree.getSelectionPaths();

        if (paths != null) {
            // Make up a node array of copies for transfer and
            // another for/of the nodes that will be removed in
            // exportDone after a successful drop.
            List<TreeViewNode> copies = new ArrayList<>();
            List<TreeViewNode> toRemove = new ArrayList<>();
            TreeViewNode node = (TreeViewNode)paths[0].getLastPathComponent();

            TreeViewNode copy = copy(node);
            copies.add(copy);
            toRemove.add(node);

            for(int i = 1; i < paths.length; i++) {
                TreeViewNode next = (TreeViewNode)paths[i].getLastPathComponent();
                // Do not allow higher level nodes to be added to list.

                if(next.getLevel() < node.getLevel()) {
                    break;
                } else if(next.getLevel() > node.getLevel()) {  // child node
                    copy.add(copy(next));
                    // node already contains child
                } else {                                        // sibling
                    copies.add(copy(next));
                    toRemove.add(next);
                }
            }

            TreeViewNode[] nodes = copies.toArray(new TreeViewNode[] {});
            nodesToRemove = toRemove.toArray(new TreeViewNode[] {});
            return new NodesTransferable(nodes);
        }

        return null;
    }

    /** Defensive copy used in createTransferable. */
    private TreeViewNode copy(TreeViewNode node) {
        TreeViewNode copy = new TreeViewNode(node.getText());
        copy.setData(node.getData());
        copy.setIcon(node.getIcon());
        return copy;
    }

    protected void exportDone(JComponent source, Transferable data, int action) {
        if ((action & MOVE) == MOVE) {
            JTree tree = (JTree)source;
            DefaultTreeModel model = (DefaultTreeModel)tree.getModel();

            // Remove nodes saved in nodesToRemove in createTransferable.
            for (TreeViewNode treeViewNode : nodesToRemove) {
                model.removeNodeFromParent(treeViewNode);
            }
        }
    }

    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    public boolean importData(TransferHandler.TransferSupport support) {
        if(!canImport(support)) {
            return false;
        }

        // Extract transfer data.
        TreeViewNode[] nodes;
        try {
            Transferable t = support.getTransferable();
            nodes = (TreeViewNode[])t.getTransferData(nodesFlavor);
        } catch(UnsupportedFlavorException | IOException e) {
            String message = "Error on extracting transfer data.";
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }

        // Get drop location info.
        JTree.DropLocation dl = (JTree.DropLocation)support.getDropLocation();
        int childIndex = dl.getChildIndex();
        TreePath dest = dl.getPath();
        TreeViewNode parent = (TreeViewNode)dest.getLastPathComponent();
        JTree tree = (JTree)support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();

        // Configure for drop mode.
        int index = childIndex;    // DropMode.INSERT
        if(childIndex == -1) {     // DropMode.ON
            index = parent.getChildCount();
        }

        // Add data to model.
        for (TreeViewNode node : nodes) {
            model.insertNodeInto(node, parent, index++);

            BaseConfiguration configuration = (BaseConfiguration)node.getData();
            TerminalConfigurationFolder destFolder = (TerminalConfigurationFolder)parent.getData();

            removeConfigFromRootRecursive(terminalConfigurations, configuration);

            if (configuration instanceof TerminalConfiguration) {
                destFolder.getConfigurations().add((TerminalConfiguration) configuration);
            } else {
                destFolder.getSshConfigurations().add((SshConfiguration) configuration);
            }

            SettingsManager.saveTerminalConfigurations(terminalConfigurations);
            TrayManager.reloadMenu();
        }

        return true;
    }

    private void removeConfigFromRootRecursive(TerminalConfigurationFolder folder, BaseConfiguration configuration) {
        folder.getConfigurations().remove(configuration);
        folder.getSshConfigurations().remove(configuration);

        for (TerminalConfigurationFolder subFolder : folder.getFolders()) {
            removeConfigFromRootRecursive(subFolder, configuration);
        }
    }

    class NodesTransferable implements Transferable {
        TreeViewNode[] nodes;

        public NodesTransferable(TreeViewNode[] nodes) {
            this.nodes = nodes;
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if(!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }

            return nodes;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return nodesFlavor.equals(flavor);
        }
    }
}
