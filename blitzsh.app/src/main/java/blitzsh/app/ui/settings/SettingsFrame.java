package blitzsh.app.ui.settings;

import blitzsh.app.Resources;
import blitzsh.app.common.components.Separator;
import blitzsh.app.common.components.treeview.JTreeView;
import blitzsh.app.common.components.treeview.TreeViewNode;
import blitzsh.app.settings.SettingsManager;
import blitzsh.app.settings.model.*;
import blitzsh.app.ui.TrayManager;
import blitzsh.app.utils.Messages;
import blitzsh.app.utils.interfaces.IName;
import org.apache.commons.lang3.SerializationUtils;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeSelectionModel;
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
    private final AppConfiguration appSettings;

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
    private final JButton copySelectedNodeButton;

    private ConfigurationPanel<?> currentConfigurationPanel;

    private SettingsFrame() {
        this.terminalConfigurations = SettingsManager.loadTerminalConfigurations();
        this.appSettings = SettingsManager.loadAppConfiguration();

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
        copySelectedNodeButton = createToolbarButton(Resources.COPY, Messages.get(SETTINGS_TOOLBAR_COPY), (e) -> copySelected());

        treeToolbar = new JToolBar();
        treeToolbar.setFloatable(false);
        treeToolbar.add(addConfigButton);
        treeToolbar.add(addSshConfigButton);
        treeToolbar.add(addFolderButton);
        treeToolbar.add(removeSelectedNodeButton);
        treeToolbar.add(new Separator(JSeparator.VERTICAL));
        treeToolbar.add(copySelectedNodeButton);

        var rootNode = new TreeViewNode(Messages.get(SETTINGS_TREE_ROOT_NODE), terminalConfigurations);
        rootNode.setIcon(Resources.FOLDER);
        rootNode.setEditable(false);

        treeView = new JTreeView(rootNode);
        treeView.setEditable(true);
        treeView.setDragEnabled(true);
        treeView.setDropMode(DropMode.ON_OR_INSERT);
        treeView.setTransferHandler(new SettingsTransferHandler(terminalConfigurations));
        treeView.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeView.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        treeView.getSelectionModel().addTreeSelectionListener(e -> {
            Optional<TreeViewNode> selectedNode = treeView.getLastSelectedPathComponent();

            addConfigButton.setEnabled(true);
            addSshConfigButton.setEnabled(true);
            addFolderButton.setEnabled(true);
            removeSelectedNodeButton.setEnabled(true);
            copySelectedNodeButton.setEnabled(true);

            if (selectedNode.isEmpty()) {
                removeSelectedNodeButton.setEnabled(false);
                copySelectedNodeButton.setEnabled(false);
            } else if (selectedNode.get().getData() instanceof BaseConfiguration) {
                addFolderButton.setEnabled(false);
                addConfigButton.setEnabled(false);
                addSshConfigButton.setEnabled(false);
            } else if (selectedNode.get().isRoot()) {
                removeSelectedNodeButton.setEnabled(false);
                copySelectedNodeButton.setEnabled(false);
            } else if (selectedNode.get().getData() instanceof TerminalConfigurationFolder) {
                copySelectedNodeButton.setEnabled(false);
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
        splitPane.setBackground(Color.WHITE);

        JPanel appConfigPane = new JPanel();
        appConfigPane.setBackground(Color.WHITE);
        appConfigPane.setLayout(new BoxLayout(appConfigPane, BoxLayout.Y_AXIS));
        appConfigPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JCheckBox encryptSettingsCheckbox = new JCheckBox(Messages.get(SETTINGS_APP_ENCRYPT_SETTINGS));
        encryptSettingsCheckbox.setSelected(appSettings.isEncryptSettings());
        encryptSettingsCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        encryptSettingsCheckbox.setBackground(appConfigPane.getBackground());
        encryptSettingsCheckbox.addChangeListener(e -> appSettings.setEncryptSettings(encryptSettingsCheckbox.isSelected()));

        appConfigPane.add(encryptSettingsCheckbox);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(Messages.get(SETTINGS_TABS_APP), appConfigPane);
        tabbedPane.addTab(Messages.get(SETTINGS_TABS_CONFIGS), splitPane);

        this.add(tabbedPane, BorderLayout.CENTER);
    }

    private void saveAndApplyChanges() {
        SettingsManager.saveAppConfiguration(appSettings);
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

        addConfiguration(selectedNode, configName, newConfig);
    }

    private void addConfiguration(Optional<TreeViewNode> selectedNode, String configName, TerminalConfiguration newConfig) {
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

        addSshConfiguration(selectedNode, configName, newConfig);
    }

    private void addSshConfiguration(Optional<TreeViewNode> selectedNode, String configName, SshConfiguration newConfig) {
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

    private void copySelected() {
        Optional<TreeViewNode> selectedNode = treeView.getLastSelectedPathComponent();
        if (selectedNode.isEmpty()) {
            return;
        }

        if (!(selectedNode.get().getData() instanceof BaseConfiguration)) {
            return;
        }

        TreeViewNode parent = selectedNode.get().getParent();
        if (parent == null) {
            return;
        }

        BaseConfiguration configToCopy = (BaseConfiguration) selectedNode.get().getData();
        BaseConfiguration clonedConfig = SerializationUtils.clone(configToCopy);

        if (configToCopy instanceof TerminalConfiguration) {
            addConfiguration(Optional.of(parent), configToCopy.getName(), (TerminalConfiguration)clonedConfig);
        } else {
            addSshConfiguration(Optional.of(parent), configToCopy.getName(), (SshConfiguration)clonedConfig);
        }
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
            parentFolder.getSshConfigurations().remove(selectedNode.get().getData());

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
