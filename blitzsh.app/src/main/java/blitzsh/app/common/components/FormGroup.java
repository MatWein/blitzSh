package blitzsh.app.common.components;

import javax.swing.*;
import java.awt.*;

public class FormGroup<T extends Component> extends JPanel {
    public static final int PREF_HEIGHT = 45;

    private final T component;

    public FormGroup(String label, int leftSpace, T component) {
        this(label, leftSpace, PREF_HEIGHT, component, null);
    }

    public FormGroup(String label, int leftSpace, int prefHeight, T component) {
        this(label, leftSpace, prefHeight, component, null);
    }

    public FormGroup(String label, int leftSpace, T component, Component rightComponent) {
        this(label, leftSpace, PREF_HEIGHT, component, rightComponent);
    }

    public FormGroup(String label, int leftSpace, int prefHeight, T component, Component rightComponent) {
        super(new BorderLayout(5, 0));
        this.component = component;

        JLabel labelComponent = new JLabel(label);
        labelComponent.setMinimumSize(new Dimension(leftSpace, 0));
        labelComponent.setPreferredSize(new Dimension(leftSpace, 25));
        labelComponent.setMaximumSize(new Dimension(leftSpace, Integer.MAX_VALUE));
        this.add(labelComponent, BorderLayout.WEST);

        this.add(component, BorderLayout.CENTER);

        if (rightComponent != null) {
            this.add(rightComponent, BorderLayout.EAST);
        }

        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, prefHeight));
        this.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public T getComponent() {
        return component;
    }
}
