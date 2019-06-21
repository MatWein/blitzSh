package blitzsh.app.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
    private static final Logger LOGGER = LoggerFactory.getLogger(Messages.class);

    private static final Locale LOCALE = Locale.GERMAN;
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages.messages", LOCALE);

    public static String get(MessageKey key, Object... args) {
        try {
            return MessageFormat.format(RESOURCE_BUNDLE.getString(key.getKey()), args);
        } catch (MissingResourceException e) {
            LOGGER.warn("Message key '{}' not found. Will return key: {}", key, e.getMessage());
            return key.getKey();
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Invalid pattern found for key '{}'. Will return key: {}", key, e.getMessage());
            return key.getKey();
        }
    }

    public enum MessageKey {
        APP_NAME("app.name"),
        APP_VERSION("app.version"),

        MAIN_ERRORS_INIT("main.errors.init"),

        TRAY_MENU_EXIT("tray.menu.exit"),
        TRAY_MENU_SETTINGS("tray.menu.settings"),
        TRAY_MENU_CONFIG_NOT_VALID("tray.menu.configNotValid"),

        SETTINGS_TOOLBAR_ADD_FOLDER("settings.toolbar.addFolder"),
        SETTINGS_TOOLBAR_ADD_CONFIG("settings.toolbar.addConfig"),
        SETTINGS_TOOLBAR_ADD_SSH_CONFIG("settings.toolbar.addSshConfig"),
        SETTINGS_TOOLBAR_REMOVE("settings.toolbar.remove"),
        SETTINGS_TOOLBAR_REMOVE_QUESTION("settings.toolbar.remove.question"),
        SETTINGS_TREE_ROOT_NODE("settings.tree.rootNode"),
        SETTINGS_TREE_NEW_FOLDER("settings.tree.newFolder"),
        SETTINGS_TREE_NEW_CONFIG("settings.tree.newConfig"),
        SETTINGS_TREE_NEW_SSH_CONFIG("settings.tree.newSshConfig"),
        SETTINGS_INTRO("settings.intro"),
        SETTINGS_PANEL_COMMAND("settings.panel.command"),
        SETTINGS_PANEL_COMMAND_BROWSE("settings.panel.command.browse"),
        SETTINGS_PANEL_WORKING_DIR("settings.panel.workingDir"),
        SETTINGS_PANEL_WORKING_DIR_BROWSE("settings.panel.workingDir.browse"),
        SETTINGS_PANEL_CHARSET("settings.panel.charset"),
        SETTINGS_PANEL_ENVIRONMENT("settings.panel.environment"),
        SETTINGS_PANEL_ENVIRONMENT_HINT1("settings.panel.environment.hint1"),
        SETTINGS_PANEL_ENVIRONMENT_HINT2("settings.panel.environment.hint2"),
        SETTINGS_PANEL_ENVIRONMENT_HINT3("settings.panel.environment.hint3"),
        SETTINGS_PANEL_CONSOLE("settings.panel.console"),
        SETTINGS_PANEL_CYGWIN("settings.panel.cygwin"),
        SETTINGS_PANEL_COPY_ON_SELECT("settings.panel.copyOnSelect"),
        SETTINGS_PANEL_PASTE_ON_MIDDLE_MOUSE("settings.panel.pasteOnMiddleMouse"),
        SETTINGS_PANEL_PASTE_ON_SHIFT_INSERT("settings.panel.pasteOnShiftInsert"),
        SETTINGS_PANEL_COLORS("settings.panel.colors"),
        SETTINGS_PANEL_COLORS_HEADERS_DESC("settings.panel.colors.headers.description"),
        SETTINGS_PANEL_COLORS_HEADERS_VALUE("settings.panel.colors.headers.value"),
        SETTINGS_PANEL_TERMINAL_BACKGROUND("settings.panel.colors.terminalBackground"),
        SETTINGS_PANEL_TERMINAL_FOREGROUND("settings.panel.colors.terminalForeground"),
        SETTINGS_PANEL_SELECTION_BACKGROUND("settings.panel.colors.selectionBackground"),
        SETTINGS_PANEL_SELECTION_FOREGROUND("settings.panel.colors.selectionForeground"),
        SETTINGS_PANEL_SEARCH_BACKGROUND("settings.panel.colors.foundPatternBackground"),
        SETTINGS_PANEL_SEARCH_FOREGROUND("settings.panel.colors.foundPatternForeground"),
        SETTINGS_PANEL_HYPERLINK_BACKGROUND("settings.panel.colors.hyperlinkBackground"),
        SETTINGS_PANEL_HYPERLINK_FOREGROUND("settings.panel.colors.hyperlinkForeground"),
        SETTINGS_PANEL_MAPPING_BLACK("settings.panel.colors.mappingBlack"),
        SETTINGS_PANEL_MAPPING_RED("settings.panel.colors.mappingRed"),
        SETTINGS_PANEL_MAPPING_GREEN("settings.panel.colors.mappingGreen"),
        SETTINGS_PANEL_MAPPING_YELLOW("settings.panel.colors.mappingYellow"),
        SETTINGS_PANEL_MAPPING_BLUE("settings.panel.colors.mappingBlue"),
        SETTINGS_PANEL_MAPPING_MAGENTA("settings.panel.colors.mappingMagenta"),
        SETTINGS_PANEL_MAPPING_CYAN("settings.panel.colors.mappingCyan"),
        SETTINGS_PANEL_MAPPING_WHITE("settings.panel.colors.mappingWhite"),
        SETTINGS_PANEL_MAPPING_BRIGHT_BLACK("settings.panel.colors.mappingBrightBlack"),
        SETTINGS_PANEL_MAPPING_BRIGHT_RED("settings.panel.colors.mappingBrightRed"),
        SETTINGS_PANEL_MAPPING_BRIGHT_GREEN("settings.panel.colors.mappingBrightGreen"),
        SETTINGS_PANEL_MAPPING_BRIGHT_YELLOW("settings.panel.colors.mappingBrightYellow"),
        SETTINGS_PANEL_MAPPING_BRIGHT_BLUE("settings.panel.colors.mappingBrightBlue"),
        SETTINGS_PANEL_MAPPING_BRIGHT_MAGENTA("settings.panel.colors.mappingBrightMagenta"),
        SETTINGS_PANEL_MAPPING_BRIGHT_CYAN("settings.panel.colors.mappingBrightCyan"),
        SETTINGS_PANEL_MAPPING_BRIGHT_WHITE("settings.panel.colors.mappingBrightWhite"),
        SETTINGS_PANEL_NUMBERS("settings.panel.numbers"),
        SETTINGS_PANEL_NUMBERS_COLUMNS("settings.panel.numbers.columns"),
        SETTINGS_PANEL_NUMBERS_LINES("settings.panel.numbers.lines"),
        SETTINGS_PANEL_NUMBERS_BUFFER("settings.panel.numbers.bufferMaxLinesCount"),
        SETTINGS_PANEL_NUMBERS_BLINK_RATE("settings.panel.numbers.blinkRate"),
        SETTINGS_PANEL_NUMBERS_ALPHA("settings.panel.numbers.alpha"),
        SETTINGS_PANEL_NUMBERS_HEADERS_DESC("settings.panel.numbers.headers.description"),
        SETTINGS_PANEL_NUMBERS_HEADERS_VALUE("settings.panel.numbers.headers.value"),
        SETTINGS_PANEL_SSH_HOST("settings.panel.ssh.host"),
        SETTINGS_PANEL_SSH_PORT("settings.panel.ssh.port"),
        SETTINGS_PANEL_SSH_USERNAME("settings.panel.ssh.username"),
        SETTINGS_PANEL_SSH_PASSWORD("settings.panel.ssh.password"),
        SETTINGS_PANEL_SSH_PRIVATE_KEY_BROWSE("settings.panel.ssh.privateKey.browse"),
        SETTINGS_PANEL_SSH_PRIVATE_KEY("settings.panel.ssh.privateKey"),
        SETTINGS_PANEL_SSH_PUBLIC_KEY_BROWSE("settings.panel.ssh.publicKey.browse"),
        SETTINGS_PANEL_SSH_PUBLIC_KEY("settings.panel.ssh.publicKey"),
        SETTINGS_PANEL_SSH_PASSPHRASE("settings.panel.ssh.passphrase"),
        SETTINGS_PANEL_SSH_PROMPT_IDENTITY_YES_NO("settings.panel.ssh.prompt.identityYesNo"),
        SETTINGS_PANEL_SSH_PROMPT_PASSPHRASE("settings.panel.ssh.prompt.passphrase"),
        SETTINGS_PANEL_SSH_PROMPT_PASSWORD("settings.panel.ssh.prompt.password"),
        SETTINGS_PANEL_SSH_PROMPT_MESSAGES("settings.panel.ssh.prompt.messages")

        ;

        private final String key;

        MessageKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
