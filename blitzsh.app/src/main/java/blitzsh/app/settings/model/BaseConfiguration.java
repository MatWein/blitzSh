package blitzsh.app.settings.model;

import blitzsh.app.utils.interfaces.IName;

import java.awt.*;
import java.io.Serializable;
import java.util.UUID;

public class BaseConfiguration implements Serializable, IName {
    private String id = UUID.randomUUID().toString();
    private String name;

    private int columns = 160;
    private int lines = 40;
    private int bufferMaxLinesCount = 10000;
    private int blinkRate = 505;
    private int alpha = 100;

    private Color terminalBackground = new Color(24, 24, 24);
    private Color terminalForeground = Color.WHITE;
    private Color selectionBackground = new Color(82, 109, 165);
    private Color selectionForeground = Color.WHITE;
    private Color foundPatternBackground = new Color(255, 255, 0);
    private Color foundPatternForeground = Color.BLACK;
    private Color hyperlinkBackground = Color.WHITE;
    private Color hyperlinkForeground = Color.BLUE;

    private Color mappingBlack = new Color(0x000000);
    private Color mappingRed = new Color(0xcd0000);
    private Color mappingGreen = new Color(0x00cd00);
    private Color mappingYellow = new Color(0xcdcd00);
    private Color mappingBlue = new Color(0x1e90ff);
    private Color mappingMagenta = new Color(0xcd00cd);
    private Color mappingCyan = new Color(0x00cdcd);
    private Color mappingWhite = new Color(0xe5e5e5);
    private Color mappingBrightBlack = new Color(0x4c4c4c);
    private Color mappingBrightRed = new Color(0xff0000);
    private Color mappingBrightGreen = new Color(0x00ff00);
    private Color mappingBrightYellow = new Color(0xffff00);
    private Color mappingBrightBlue = new Color(0x4682b4);
    private Color mappingBrightMagenta = new Color(0xff00ff);
    private Color mappingBrightCyan = new Color(0x00ffff);
    private Color mappingBrightWhite = new Color(0xffffff);

    public BaseConfiguration() {
    }

    public BaseConfiguration(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public int getBufferMaxLinesCount() {
        return bufferMaxLinesCount;
    }

    public void setBufferMaxLinesCount(int bufferMaxLinesCount) {
        this.bufferMaxLinesCount = bufferMaxLinesCount;
    }

    public int getBlinkRate() {
        return blinkRate;
    }

    public void setBlinkRate(int blinkRate) {
        this.blinkRate = blinkRate;
    }

    public Color getTerminalBackground() {
        return terminalBackground;
    }

    public void setTerminalBackground(Color terminalBackground) {
        this.terminalBackground = terminalBackground;
    }

    public Color getTerminalForeground() {
        return terminalForeground;
    }

    public void setTerminalForeground(Color terminalForeground) {
        this.terminalForeground = terminalForeground;
    }

    public Color getSelectionBackground() {
        return selectionBackground;
    }

    public void setSelectionBackground(Color selectionBackground) {
        this.selectionBackground = selectionBackground;
    }

    public Color getSelectionForeground() {
        return selectionForeground;
    }

    public void setSelectionForeground(Color selectionForeground) {
        this.selectionForeground = selectionForeground;
    }

    public Color getFoundPatternBackground() {
        return foundPatternBackground;
    }

    public void setFoundPatternBackground(Color foundPatternBackground) {
        this.foundPatternBackground = foundPatternBackground;
    }

    public Color getFoundPatternForeground() {
        return foundPatternForeground;
    }

    public void setFoundPatternForeground(Color foundPatternForeground) {
        this.foundPatternForeground = foundPatternForeground;
    }

    public Color getHyperlinkBackground() {
        return hyperlinkBackground;
    }

    public void setHyperlinkBackground(Color hyperlinkBackground) {
        this.hyperlinkBackground = hyperlinkBackground;
    }

    public Color getHyperlinkForeground() {
        return hyperlinkForeground;
    }

    public void setHyperlinkForeground(Color hyperlinkForeground) {
        this.hyperlinkForeground = hyperlinkForeground;
    }

    public Color getMappingBlack() {
        return mappingBlack;
    }

    public void setMappingBlack(Color mappingBlack) {
        this.mappingBlack = mappingBlack;
    }

    public Color getMappingRed() {
        return mappingRed;
    }

    public void setMappingRed(Color mappingRed) {
        this.mappingRed = mappingRed;
    }

    public Color getMappingGreen() {
        return mappingGreen;
    }

    public void setMappingGreen(Color mappingGreen) {
        this.mappingGreen = mappingGreen;
    }

    public Color getMappingYellow() {
        return mappingYellow;
    }

    public void setMappingYellow(Color mappingYellow) {
        this.mappingYellow = mappingYellow;
    }

    public Color getMappingBlue() {
        return mappingBlue;
    }

    public void setMappingBlue(Color mappingBlue) {
        this.mappingBlue = mappingBlue;
    }

    public Color getMappingMagenta() {
        return mappingMagenta;
    }

    public void setMappingMagenta(Color mappingMagenta) {
        this.mappingMagenta = mappingMagenta;
    }

    public Color getMappingCyan() {
        return mappingCyan;
    }

    public void setMappingCyan(Color mappingCyan) {
        this.mappingCyan = mappingCyan;
    }

    public Color getMappingWhite() {
        return mappingWhite;
    }

    public void setMappingWhite(Color mappingWhite) {
        this.mappingWhite = mappingWhite;
    }

    public Color getMappingBrightBlack() {
        return mappingBrightBlack;
    }

    public void setMappingBrightBlack(Color mappingBrightBlack) {
        this.mappingBrightBlack = mappingBrightBlack;
    }

    public Color getMappingBrightRed() {
        return mappingBrightRed;
    }

    public void setMappingBrightRed(Color mappingBrightRed) {
        this.mappingBrightRed = mappingBrightRed;
    }

    public Color getMappingBrightGreen() {
        return mappingBrightGreen;
    }

    public void setMappingBrightGreen(Color mappingBrightGreen) {
        this.mappingBrightGreen = mappingBrightGreen;
    }

    public Color getMappingBrightYellow() {
        return mappingBrightYellow;
    }

    public void setMappingBrightYellow(Color mappingBrightYellow) {
        this.mappingBrightYellow = mappingBrightYellow;
    }

    public Color getMappingBrightBlue() {
        return mappingBrightBlue;
    }

    public void setMappingBrightBlue(Color mappingBrightBlue) {
        this.mappingBrightBlue = mappingBrightBlue;
    }

    public Color getMappingBrightMagenta() {
        return mappingBrightMagenta;
    }

    public void setMappingBrightMagenta(Color mappingBrightMagenta) {
        this.mappingBrightMagenta = mappingBrightMagenta;
    }

    public Color getMappingBrightCyan() {
        return mappingBrightCyan;
    }

    public void setMappingBrightCyan(Color mappingBrightCyan) {
        this.mappingBrightCyan = mappingBrightCyan;
    }

    public Color getMappingBrightWhite() {
        return mappingBrightWhite;
    }

    public void setMappingBrightWhite(Color mappingBrightWhite) {
        this.mappingBrightWhite = mappingBrightWhite;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }
}
