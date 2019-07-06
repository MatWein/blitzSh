package blitzsh.app.ui.settings;

import blitzsh.app.settings.model.BaseConfiguration;
import com.jediterm.terminal.TerminalColor;
import com.jediterm.terminal.TextStyle;
import com.jediterm.terminal.emulator.ColorPalette;
import com.jediterm.terminal.ui.UIUtil;
import com.jediterm.terminal.ui.settings.DefaultSettingsProvider;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class BlitzShSettingsProvider extends DefaultSettingsProvider {
    private final BaseConfiguration settings;

    public BlitzShSettingsProvider(BaseConfiguration baseConfiguration) {
        this.settings = baseConfiguration;
    }

    @Override
    public TextStyle getDefaultStyle() {
        return new TextStyle(
                TerminalColor.awt(settings.getTerminalForeground()),
                TerminalColor.awt(settings.getTerminalBackground()));
    }

    @Override
    public TextStyle getSelectionColor() {
        return new TextStyle(
                TerminalColor.awt(settings.getSelectionForeground()),
                TerminalColor.awt(settings.getSelectionBackground()));
    }

    @Override
    public TextStyle getFoundPatternColor() {
        return new TextStyle(
                TerminalColor.awt(settings.getFoundPatternForeground()),
                TerminalColor.awt(settings.getFoundPatternBackground()));
    }

    @Override
    public TextStyle getHyperlinkColor() {
        return new TextStyle(
                TerminalColor.awt(settings.getHyperlinkForeground()),
                TerminalColor.awt(settings.getHyperlinkBackground()));
    }

    @Override
    public int getBufferMaxLinesCount() {
        return settings.getBufferMaxLinesCount();
    }

    @Override
    public int caretBlinkingMs() {
        return settings.getBlinkRate();
    }

    @Override
    public ColorPalette getTerminalColorPalette() {
        return new ColorPalette() {
            @Override
            public Color[] getIndexColors() {
                return new Color[] {
                        settings.getMappingBlack(),
                        settings.getMappingRed(),
                        settings.getMappingGreen(),
                        settings.getMappingYellow(),
                        settings.getMappingBlue(),
                        settings.getMappingMagenta(),
                        settings.getMappingCyan(),
                        settings.getMappingWhite(),
                        settings.getMappingBrightBlack(),
                        settings.getMappingBrightRed(),
                        settings.getMappingBrightGreen(),
                        settings.getMappingBrightYellow(),
                        settings.getMappingBrightBlue(),
                        settings.getMappingBrightMagenta(),
                        settings.getMappingBrightCyan(),
                        settings.getMappingBrightWhite()
                };
            }
        };
    }

    @Override
    public boolean copyOnSelect() {
        return settings.isCopyOnSelect();
    }

    @Override
    public boolean pasteOnMiddleMouseClick() {
        return settings.isPasteOnMiddleMouseClick();
    }

    @Override
    public KeyStroke[] getPasteKeyStrokes() {
        if (settings.isPasteOnShiftInsert()) {
            KeyStroke macOsKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.META_DOWN_MASK);
            KeyStroke windowsKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
            KeyStroke shiftInsertKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, InputEvent.SHIFT_DOWN_MASK);

            return new KeyStroke[] { (UIUtil.isMac ? macOsKeyStroke : windowsKeyStroke), shiftInsertKeyStroke };
        } else {
            return super.getPasteKeyStrokes();
        }
    }

    @Override
    public Font getTerminalFont() {
        String font = settings.getFont();
        if (StringUtils.isBlank(font) || BaseConfiguration.DEFAULT_FONT.equalsIgnoreCase(font)) {
            return super.getTerminalFont();
        }

        return Font.decode(font).deriveFont(getTerminalFontSize());
    }

    @Override
    public float getTerminalFontSize() {
        return settings.getFontSize();
    }
}
