package blitzsh.app.ui;

import blitzsh.app.Resources;
import blitzsh.app.settings.model.BaseConfiguration;
import blitzsh.app.settings.model.SshConfiguration;
import blitzsh.app.settings.model.TerminalConfiguration;
import blitzsh.app.ui.settings.BlitzShSettingsProvider;
import blitzsh.app.ui.terminal.BlitzShTerminalWidget;
import blitzsh.app.utils.Messages;
import com.jediterm.pty.PtyProcessTtyConnector;
import com.jediterm.ssh.jsch.JSchIdentityShellTtyConnector;
import com.jediterm.terminal.TtyConnector;
import com.pty4j.PtyProcess;
import com.pty4j.PtyProcessBuilder;
import com.sun.jna.platform.WindowUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.charset.Charset;

import static blitzsh.app.utils.Messages.MessageKey.APP_NAME;
import static blitzsh.app.utils.Messages.MessageKey.MAIN_ERRORS_INIT;

public class MainFrame extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrame.class);

    private final BlitzShTerminalWidget terminal;
    private final BaseConfiguration settings;

    private boolean fullscreenActive;
    private Point locationBeforeFullscreen;
    private Dimension sizeBeforeFullscreen;

    public MainFrame(BaseConfiguration settings) {
        this.settings = settings;
        try {
            terminal = new BlitzShTerminalWidget(
                    settings.getColumns(),
                    settings.getLines(),
                    new BlitzShSettingsProvider(settings));

            this.add(terminal);

            terminal.getTerminalPanel().addCustomKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_F11) {
                        toggleFullscreen();
                        e.consume();
                    }
                }
            });

            TtyConnector processConnector;
            if (settings instanceof TerminalConfiguration) {
                var terminalConfiguration = (TerminalConfiguration) settings;

                PtyProcess process = new PtyProcessBuilder()
                        .setCommand(terminalConfiguration.getCommand())
                        .setConsole(terminalConfiguration.isConsole())
                        .setCygwin(terminalConfiguration.isCygwin())
                        .setDirectory(terminalConfiguration.getWorkingDir())
                        .setInitialColumns(terminalConfiguration.getColumns())
                        .setInitialRows(terminalConfiguration.getLines())
                        .setEnvironment(terminalConfiguration.getEnvironment().isEmpty() ? null : terminalConfiguration.getEnvironment())
                        .setRedirectErrorStream(true)
                        .setWindowsAnsiColorEnabled(true)
                        .start();

                this.setTitle(Messages.get(APP_NAME) + " - " + StringUtils.join(terminalConfiguration.getCommand(), " "));
                processConnector = new PtyProcessTtyConnector(process, Charset.forName(terminalConfiguration.getCharset()));
            } else {
                var sshConfiguration = (SshConfiguration) settings;

                this.setTitle(Messages.get(APP_NAME) + " - SSH: " + sshConfiguration.getHost());
                processConnector = new JSchIdentityShellTtyConnector(sshConfiguration);
            }

            terminal.createTerminalSession(processConnector);
            terminal.start();

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    LOGGER.info("Closing terminal connection");
                    terminal.close();
                }

                @Override
                public void windowOpened(WindowEvent e) {
                    if (isAlphaSettingValid(settings)) {
                        System.setProperty("sun.java2d.noddraw", Boolean.TRUE.toString());
                        WindowUtils.setWindowAlpha(MainFrame.this, settings.getAlpha() / 100.0F);
                    }

                    if (!processConnector.isConnected()) {
                        return;
                    }

                    Thread thread = new Thread(() -> {
                        try {
                            processConnector.waitFor();
                        } catch (InterruptedException ex) {
                            LOGGER.error("Error on waiting for process termination.", ex);
                        }

                        MainFrame.this.dispatchEvent(new WindowEvent(MainFrame.this, WindowEvent.WINDOW_CLOSING));
                    });
                    thread.setDaemon(true);
                    thread.start();
                }
            });

            this.pack();
            this.setLocationRelativeTo(null);
            this.setIconImages(Resources.ICONS);
            this.setResizable(true);

            SwingUtilities.updateComponentTreeUI(this);
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(this, Messages.get(MAIN_ERRORS_INIT, e.getMessage()), Messages.get(APP_NAME), JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Error on initializing main frame.", e);
        }
    }

    private boolean isAlphaSettingValid(BaseConfiguration settings) {
        return settings.getAlpha() > 0 && settings.getAlpha() < 100;
    }

    private void toggleFullscreen() {
        if (fullscreenActive) {
            leaveFullscreen();
        } else {
            enterFullscreen();
        }
    }

    private void enterFullscreen() {
        GraphicsDevice device = getGraphicsConfiguration().getDevice();
        GraphicsConfiguration defaultConfiguration = device.getDefaultConfiguration();

        dispose();
        Rectangle bounds = defaultConfiguration.getBounds();

        locationBeforeFullscreen = getLocation();
        sizeBeforeFullscreen = getSize();

        setUndecorated(true);
        setLocation((int)bounds.getX(), (int)bounds.getY());
        setSize((int)bounds.getWidth(), (int)bounds.getHeight());

        terminal.toggleScrollBar(false);

        fullscreenActive = true;
        setVisible(true);

        WindowUtils.setWindowAlpha(MainFrame.this, 1.0F);
    }

    private void leaveFullscreen() {
        dispose();
        setUndecorated(false);
        setLocation((int)locationBeforeFullscreen.getX(), (int)locationBeforeFullscreen.getY());
        setSize((int)sizeBeforeFullscreen.getWidth(), (int)sizeBeforeFullscreen.getHeight());

        terminal.toggleScrollBar(true);

        fullscreenActive = false;
        locationBeforeFullscreen = null;
        sizeBeforeFullscreen = null;
        setVisible(true);

        if (isAlphaSettingValid(settings)) {
            WindowUtils.setWindowAlpha(MainFrame.this, settings.getAlpha() / 100.0F);
        }
    }
}
