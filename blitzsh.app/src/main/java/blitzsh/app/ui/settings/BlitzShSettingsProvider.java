package blitzsh.app.ui.settings;

import blitzsh.app.settings.model.BaseConfiguration;
import com.jediterm.terminal.TerminalColor;
import com.jediterm.terminal.TextStyle;
import com.jediterm.terminal.emulator.ColorPalette;
import com.jediterm.terminal.ui.settings.DefaultSettingsProvider;

import java.awt.*;

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
}
