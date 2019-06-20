package blitzsh.app.ui.terminal;

import com.jediterm.terminal.model.StyleState;
import com.jediterm.terminal.model.TerminalTextBuffer;
import com.jediterm.terminal.ui.TerminalPanel;
import com.jediterm.terminal.ui.settings.SettingsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Field;
import java.util.List;

public class BlitzShTerminalPanel extends TerminalPanel {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlitzShTerminalPanel.class);

    private List<KeyListener> keyListeners;

    public BlitzShTerminalPanel(SettingsProvider settingsProvider, TerminalTextBuffer terminalTextBuffer, StyleState styleState) {
        super(settingsProvider, terminalTextBuffer, styleState);

        try {
            Field myCustomKeyListeners = TerminalPanel.class.getDeclaredField("myCustomKeyListeners");
            myCustomKeyListeners.setAccessible(true);
            keyListeners = (List<KeyListener>)myCustomKeyListeners.get(this);
        } catch (Throwable ex) {
            LOGGER.error("Error on getting key listeners. Custom key will maybe not work correctly.", ex);
            keyListeners = null;
        }
    }

//    @Override
//    protected JPopupMenu createPopupMenu() {
//        return new JPopupMenu();
//    }

    @Override
    public void handleKeyEvent(KeyEvent e) {
        if (keyListeners == null) {
            super.handleKeyEvent(e);
            return;
        }

        final int id = e.getID();
        if (id == KeyEvent.KEY_PRESSED) {
            for (KeyListener keyListener : keyListeners) {
                keyListener.keyPressed(e);
                if (e.isConsumed()) {
                    break;
                }
            }
        } else if (id == KeyEvent.KEY_TYPED) {
            for (KeyListener keyListener : keyListeners) {
                keyListener.keyTyped(e);
                if (e.isConsumed()) {
                    break;
                }
            }
        }
    }
}
