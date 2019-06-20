package blitzsh.app;

import blitzsh.app.ui.TrayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                TrayManager.init();
            } catch (Throwable e) {
                LOGGER.error("Unexpected error occured.", e);
                System.exit(1);
            }
        });
    }
}
