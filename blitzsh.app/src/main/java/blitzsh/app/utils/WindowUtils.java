package blitzsh.app.utils;

import javax.swing.*;

public class WindowUtils {
    public static void applyWindowTransparency(JFrame frame, int alpha) {
        System.setProperty("sun.java2d.noddraw", Boolean.TRUE.toString());
        com.sun.jna.platform.WindowUtils.setWindowAlpha(frame, alpha / 100.0F);
    }
}
