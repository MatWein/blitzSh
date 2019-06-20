package blitzsh.app.ui.terminal;

import com.jediterm.terminal.model.StyleState;
import com.jediterm.terminal.model.TerminalTextBuffer;
import com.jediterm.terminal.ui.JediTermWidget;
import com.jediterm.terminal.ui.TerminalPanel;
import com.jediterm.terminal.ui.settings.SettingsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.lang.reflect.Field;

public class BlitzShTerminalWidget extends JediTermWidget {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlitzShTerminalWidget.class);

    private JLayeredPane innerPanel;
    private JScrollBar scrollBar;

    public BlitzShTerminalWidget(int columns, int lines, SettingsProvider settingsProvider) {
        super(columns, lines, settingsProvider);

        try {
            Field myInnerPanel = JediTermWidget.class.getDeclaredField("myInnerPanel");
            myInnerPanel.setAccessible(true);
            innerPanel = (JLayeredPane)myInnerPanel.get(this);
        } catch (Throwable ex) {
            LOGGER.error("Error on getting inner pane. Scrollbar will maybe not work correctly.", ex);
            innerPanel = null;
        }
    }

    public void toggleScrollBar(boolean show) {
        removeScrollBar();

        if (show) {
            innerPanel.add(createScrollBar(), "SCROLL");
            SwingUtilities.updateComponentTreeUI(this);
        }
    }

    public void removeScrollBar() {
        if (innerPanel != null) {
            innerPanel.remove(scrollBar);
        }
    }

    @Override
    protected TerminalPanel createTerminalPanel(SettingsProvider settingsProvider, StyleState styleState, TerminalTextBuffer terminalTextBuffer) {
        return new BlitzShTerminalPanel(settingsProvider, terminalTextBuffer, styleState);
    }

    @Override
    protected JScrollBar createScrollBar() {
        scrollBar = super.createScrollBar();
        return scrollBar;
    }
}
