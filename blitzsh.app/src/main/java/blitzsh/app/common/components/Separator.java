package blitzsh.app.common.components;

import javax.swing.*;
import java.awt.*;

public class Separator extends JSeparator {
    public static final int MAX_HEIGHT = 3;

    public Separator() {
        this(JSeparator.HORIZONTAL);
    }

    public Separator(int orientation) {
        super(orientation);

        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, MAX_HEIGHT));
    }
}
