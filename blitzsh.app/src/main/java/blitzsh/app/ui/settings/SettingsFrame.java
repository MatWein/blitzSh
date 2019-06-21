package blitzsh.app.ui.settings;

import blitzsh.app.Resources;
import blitzsh.app.common.components.treeview.JTreeView;
import blitzsh.app.common.components.treeview.TreeViewNode;
import blitzsh.app.settings.SettingsManager;
import blitzsh.app.settings.model.BaseConfiguration;
import blitzsh.app.settings.model.SshConfiguration;
import blitzsh.app.settings.model.TerminalConfiguration;
import blitzsh.app.settings.model.TerminalConfigurationFolder;
import blitzsh.app.ui.TrayManager;
import blitzsh.app.utils.Messages;
import blitzsh.app.utils.interfaces.IName;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Optional;

import static blitzsh.app.utils.Messages.MessageKey.*;

public class SettingsFrame extends JFrame {
    public static SettingsFrame INSTANCE = new SettingsFrame();

    private final TerminalConfigurationFolder terminalConfigurations;

    private final JLabel introLabel;

    private final JPanel treePanel;
    private final JToolBar treeToolbar;
    private final JTreeView treeView;

    private final JSplitPane splitPane;

    private final JPanel settingsPanel;
    private final JButton addConfigButton;
    private final JButton addSshConfigButton;
    private final JButton addFolderButton;
    private final JButton removeSelectedNodeButton;

    private ConfigurationPanel<?> currentConfigurationPanel;

    private SettingsFrame() {
        this.terminalConfigurations = SettingsManager.loadTerminalConfigurations();

        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle(Messages.get(APP_NAME) + " - " + Messages.get(TRAY_MENU_SETTINGS));
        this.setIconImages(Resources.ICONS);
        this.setSize(1200, 700);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveAndApplyChanges();
            }
        });

        addConfigButton = createToolbarButton(Resources.TERMINAL_PLUS, Messages.get(SETTINGS_TOOLBAR_ADD_CONFIG), (e) -> addConfiguration());
        addSshConfigButton = createToolbarButton(Resources.SSH_PLUS, Messages.get(SETTINGS_TOOLBAR_ADD_SSH_CONFIG), (e) -> addSshConfiguration());
        addFolderButton = createToolbarButton(Resources.FOLDER_PLUS, Messages.get(SETTINGS_TOOLBAR_ADD_FOLDER), (e) -> addFolder());
        removeSelectedNodeButton = createToolbarButton(Resources.MINUS, Messages.get(SETTINGS_TOOLBAR_REMOVE), (e) -> removeSelected());

        treeToolbar = new JToolBar();
        treeToolbar.setFloatable(false);
        treeToolbar.add(addConfigButton);
        treeToolbar.add(addSshConfigButton);
        treeToolbar.add(addFolderButton);
        treeToolbar.add(removeSelectedNodeButton);

        var rootNode = new TreeViewNode(Messages.get(SETTINGS_TREE_ROOT_NODE), terminalConfigurations);
        rootNode.setIcon(Resources.FOLDER);
        rootNode.setEditable(false);

        treeView = new JTreeView(rootNode);
        treeView.setEditable(true);
        treeView.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        treeView.getSelectionModel().addTreeSelectionListener(e -> {
            Optional<TreeViewNode> selectedNode = treeView.getLastSelectedPathComponent();

            addConfigButton.setEnabled(true);
            addSshConfigButton.setEnabled(true);
            addFolderButton.setEnabled(true);
            removeSelectedNodeButton.setEnabled(true);

            if (selectedNode.isEmpty()) {
                removeSelectedNodeButton.setEnabled(false);
            } else if (selectedNode.get().getData() instanceof BaseConfiguration) {
                addFolderButton.setEnabled(false);
                addConfigButton.setEnabled(false);
                addSshConfigButton.setEnabled(false);
            } else if (selectedNode.get().isRoot()) {
                removeSelectedNodeButton.setEnabled(false);
            }
        });

        treeView.getSelectionModel().addTreeSelectionListener(e -> {
            Optional<TreeViewNode> selectedNode = treeView.getLastSelectedPathComponent();

            if (selectedNode.isPresent() && selectedNode.get().getData() instanceof BaseConfiguration) {
                selectConfiguration((BaseConfiguration) selectedNode.get().getData());
            }
        });

        treeView.getModel().addTreeModelListener(new TreeModelListener() {
            @Override
            public void treeNodesChanged(TreeModelEvent e) {
                Optional<TreeViewNode> selectedNode = treeView.getLastSelectedPathComponent();
                if (selectedNode.isPresent()) {
                    String newName = selectedNode.get().getText();

                    IName nodeData = (IName)selectedNode.get().getData();
                    nodeData.setName(newName);

                    if (currentConfigurationPanel != null) {
                        currentConfigurationPanel.getHeaderLabel().setText(newName);
                        settingsPanel.revalidate();
                    }

                    saveAndApplyChanges();
                }
            }

            @Override
            public void treeNodesInserted(TreeModelEvent e) {}

            @Override
            public void treeNodesRemoved(TreeModelEvent e) {}

            @Override
            public void treeStructureChanged(TreeModelEvent e) {}
        });

        addChildFolders(rootNode, terminalConfigurations.getFolders());
        addChildConfigurations(rootNode, terminalConfigurations.getConfigurations(), Resources.BLITZSH_16);
        addChildConfigurations(rootNode, terminalConfigurations.getSshConfigurations(), Resources.SSH);
        treeView.selectNode(rootNode, true, true, true);

        treePanel = new JPanel(new BorderLayout());
        treePanel.add(treeToolbar, BorderLayout.NORTH);
        treePanel.add(new JScrollPane(treeView), BorderLayout.CENTER);

        introLabel = new JLabel(Messages.get(SETTINGS_INTRO));
        introLabel.setVerticalAlignment(JLabel.CENTER);
        introLabel.setHorizontalAlignment(JLabel.CENTER);

        settingsPanel = new JPanel(new BorderLayout(10, 10));
        settingsPanel.add(introLabel, BorderLayout.CENTER);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, settingsPanel);
        splitPane.setResizeWeight(0.2);

        this.add(splitPane, BorderLayout.CENTER);
    }

    private void saveAndApplyChanges() {
        SettingsManager.saveTerminalConfigurations(terminalConfigurations);
        TrayManager.reloadMenu();
    }

    private void selectConfiguration(BaseConfiguration configuration) {
        saveAndApplyChanges();

        deselectCurrentConfiguration();
        removeCenter();

        ConfigurationPanel<?> configurationPanel;
        if (configuration instanceof TerminalConfiguration) {
            configurationPanel = new TerminalConfigurationPanel((TerminalConfiguration)configuration);
        } else {
            configurationPanel = new SshConfigurationPanel((SshConfiguration)configuration);
        }

        settingsPanel.add(configurationPanel, BorderLayout.CENTER);
        settingsPanel.revalidate();
        settingsPanel.repaint();

        currentConfigurationPanel = configurationPanel;
    }

    private void deselectCurrentConfiguration() {
        removeCenter();

        settingsPanel.add(introLabel, BorderLayout.CENTER);
        settingsPanel.revalidate();
        settingsPanel.repaint();
    }

    private void removeCenter() {
        BorderLayout layout = (BorderLayout)settingsPanel.getLayout();
        settingsPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
        settingsPanel.revalidate();
        settingsPanel.repaint();
    }

    private void addChildFolders(TreeViewNode node, List<TerminalConfigurationFolder> folders) {
        for (TerminalConfigurationFolder folder : folders) {
            TreeViewNode folderNode = new TreeViewNode(folder.getName(), folder);
            folderNode.setIcon(Resources.FOLDER);
            treeView.addNode(Optional.of(node), folderNode, false, false);

            addChildFolders(folderNode, folder.getFolders());
            addChildConfigurations(folderNode, folder.getConfigurations(), Resources.BLITZSH_16);
            addChildConfigurations(folderNode, folder.getSshConfigurations(), Resources.SSH);
        }
    }

    private void addChildConfigurations(TreeViewNode node, List<? extends BaseConfiguration> configurations, ImageIcon icon) {
        for (BaseConfiguration configuration : configurations) {
            TreeViewNode configNode = new TreeViewNode(configuration.getName(), configuration);
            configNode.setIcon(icon);
            treeView.addNode(Optional.of(node), configNode, false, false);
        }
    }

    private void addConfiguration() {
        var selectedNode = treeView.getLastSelectedPathComponent();
        if (selectedNode.isPresent() && !(selectedNode.get().getData() instanceof TerminalConfigurationFolder)) {
            return;
        }

        var configName = Messages.get(SETTINGS_TREE_NEW_CONFIG);
        var newConfig = new TerminalConfiguration(configName);

        var newConfigNode = new TreeViewNode(configName, newConfig);
        newConfigNode.setIcon(Resources.BLITZSH_16);

        treeView.addNode(selectedNode, newConfigNode);

        TerminalConfigurationFolder selectedFolder = (TerminalConfigurationFolder)(selectedNode.isPresent() ? selectedNode.get().getData() : treeView.getRootNode().getData());
        selectedFolder.getConfigurations().add(newConfig);

        saveAndApplyChanges();

        treeView.startEditingAtNode(newConfigNode);
    }

    private void addSshConfiguration() {
        var selectedNode = treeView.getLastSelectedPathComponent();
        if (selectedNode.isPresent() && !(selectedNode.get().getData() instanceof TerminalConfigurationFolder)) {
            return;
        }

        var configName = Messages.get(SETTINGS_TREE_NEW_SSH_CONFIG);
        var newConfig = new SshConfiguration(configName);

        var newConfigNode = new TreeViewNode(configName, newConfig);
        newConfigNode.setIcon(Resources.SSH);

        treeView.addNode(selectedNode, newConfigNode);

        TerminalConfigurationFolder selectedFolder = (TerminalConfigurationFolder)(selectedNode.isPresent() ? selectedNode.get().getData() : treeView.getRootNode().getData());
        selectedFolder.getSshConfigurations().add(newConfig);

        saveAndApplyChanges();

        treeView.startEditingAtNode(newConfigNode);
    }

    private void addFolder() {
        var selectedNode = treeView.getLastSelectedPathComponent();
        if (selectedNode.isPresent() && !(selectedNode.get().getData() instanceof TerminalConfigurationFolder)) {
            return;
        }

        var folderName = Messages.get(SETTINGS_TREE_NEW_FOLDER);
        var newFolder = new TerminalConfigurationFolder(folderName);

        var newFolderNode = new TreeViewNode(folderName, newFolder);
        newFolderNode.setIcon(Resources.FOLDER);

        treeView.addNode(selectedNode, newFolderNode);

        TerminalConfigurationFolder selectedFolder = (TerminalConfigurationFolder)(selectedNode.isPresent() ? selectedNode.get().getData() : treeView.getRootNode().getData());
        selectedFolder.getFolders().add(newFolder);

        saveAndApplyChanges();

        treeView.startEditingAtNode(newFolderNode);
    }

    private void removeSelected() {
        Optional<TreeViewNode> selectedNode = treeView.getLastSelectedPathComponent();
        if (selectedNode.isEmpty()) {
            return;
        }

        TreeViewNode parent = selectedNode.get().getParent();
        if (parent == null) {
            return;
        }

        int questionResult = JOptionPane.showConfirmDialog(this, Messages.get(SETTINGS_TOOLBAR_REMOVE_QUESTION), Messages.get(APP_NAME), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (questionResult == JOptionPane.YES_OPTION) {
            TerminalConfigurationFolder parentFolder = (TerminalConfigurationFolder)parent.getData();
            parentFolder.getFolders().remove(selectedNode.get().getData());
            parentFolder.getConfigurations().remove(selectedNode.get().getData());

            treeView.removeNode(selectedNode.get());

            saveAndApplyChanges();
        }

        deselectCurrentConfiguration();
    }

    private JButton createToolbarButton(Icon icon, String tooltip, ActionListener action) {
        JButton button = new JButton();

        button.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.actionPerformed(e);
            }
        });
        button.setToolTipText(tooltip);
        button.setIcon(icon);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setFocusable(false);

        return button;
    }
}
