package blitzsh.app.ui;

import blitzsh.app.Resources;
import blitzsh.app.settings.ConfigurationValidator;
import blitzsh.app.settings.SettingsManager;
import blitzsh.app.settings.model.BaseConfiguration;
import blitzsh.app.settings.model.SshConfiguration;
import blitzsh.app.settings.model.TerminalConfiguration;
import blitzsh.app.settings.model.TerminalConfigurationFolder;
import blitzsh.app.ui.settings.SettingsFrame;
import blitzsh.app.utils.Messages;
import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import dorkbox.systemTray.*;

import java.awt.*;
import java.util.List;

import static blitzsh.app.utils.Messages.MessageKey.*;

public class TrayManager {

    private static SystemTray systemTray;

    public static void init() {
        systemTray = SystemTray.get();
        if (systemTray == null) {
            throw new RuntimeException("Unable to load SystemTray!");
        }

        reloadMenu();
    }

    private static void createDynamicMenu(
            Menu parent,
            List<TerminalConfigurationFolder> folders,
            List<TerminalConfiguration> configurations,
            List<SshConfiguration> sshConfigurations) {

        createDynamicMenuForFolders(parent, folders);
        createDynamicMenuForConfigs(parent, configurations, Resources.BLITZSH_16_IMAGE);
        createDynamicMenuForConfigs(parent, sshConfigurations, Resources.SSH_IMAGE);
    }

    private static void createDynamicMenuForFolders(Menu parent, List<TerminalConfigurationFolder> folders) {
        for (TerminalConfigurationFolder folder : folders) {
            if (folder.getConfigurations().isEmpty() && folder.getFolders().isEmpty() && folder.getSshConfigurations().isEmpty()) {
                continue;
            }

            Menu subMenu = new Menu(folder.getName() + "...");
            subMenu.setImage(Resources.FOLDER_IMAGE);
            parent.add(subMenu);

            createDynamicMenu(subMenu, folder.getFolders(), folder.getConfigurations(), folder.getSshConfigurations());
        }
    }

    private static void createDynamicMenuForConfigs(Menu parent, List<? extends BaseConfiguration> configurations, Image icon) {
        for (BaseConfiguration configuration : configurations) {
            MenuItem menuItem = new MenuItem(configuration.getName(), e -> new MainFrame(configuration).setVisible(true));
            menuItem.setImage(icon);

            boolean isConfigValid = ConfigurationValidator.isValid(configuration);
            menuItem.setEnabled(isConfigValid);
            if (!isConfigValid) {
                menuItem.setTooltip(Messages.get(TRAY_MENU_CONFIG_NOT_VALID));
            }

            parent.add(menuItem);
        }
    }

    public static void reloadMenu() {
        Entry firstEntry;
        while ((firstEntry = systemTray.getMenu().getFirst()) != null) {
            systemTray.getMenu().remove(firstEntry);
        }

        TerminalConfigurationFolder rootFolder = SettingsManager.loadTerminalConfigurations();

        systemTray.setTooltip(Messages.get(APP_NAME));
        systemTray.setImage(Resources.BLITZSH_512_IMAGE);
        systemTray.setStatus(Messages.get(APP_NAME) + " " + Messages.get(APP_VERSION));

        systemTray.getMenu().add(new Separator());
        createDynamicMenu(systemTray.getMenu(), rootFolder.getFolders(), rootFolder.getConfigurations(), rootFolder.getSshConfigurations());
        systemTray.getMenu().add(new Separator());
        systemTray.getMenu().add(new MenuItem(Messages.get(TRAY_MENU_SETTINGS), e -> SettingsFrame.INSTANCE.setVisible(true)));
        systemTray.getMenu().add(new Separator());
        systemTray.getMenu().add(new MenuItem(Messages.get(TRAY_MENU_EXIT), e -> System.exit(0)));
    }
}
