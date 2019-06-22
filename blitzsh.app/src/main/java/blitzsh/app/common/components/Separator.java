package blitzsh.app.common.components;

import javax.swing.*;
import java.awt.*;

public class Separator extends JSeparator {
    public static final int MAX_VALUE = 3;

    public Separator() {
        this(JSeparator.HORIZONTAL);
    }

    public Separator(int orientation) {
        super(orientation);

        if (orientation == JSeparator.HORIZONTAL) {
            this.setMaximumSize(new Dimension(Integer.MAX_VALUE, MAX_VALUE));
        } else {
            this.setMaximumSize(new Dimension(MAX_VALUE, Integer.MAX_VALUE));
        }
    }
}
