package gui;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

public class GameWindow extends JInternalFrame implements VetoableChangeListener {
    public GameWindow() {
        super("Игровое поле", true, true, true, true);
        GameVisualizer m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        addVetoableChangeListener(this);
    }

    public void vetoableChange(PropertyChangeEvent pce)
            throws PropertyVetoException {
        if (pce.getPropertyName().equals(IS_CLOSED_PROPERTY)) {
            boolean changed = (Boolean) pce.getNewValue();
            if (changed) {
                if (MainApplicationFrame.confirmClosing(this)) {
                    throw new PropertyVetoException("Cancelled", null);
                }
            }
        }
    }

    private Object writeReplace() {
        int state = isIcon() ? 1 : 0;
        System.out.println("Game window state " + state);
        Point location = isIcon() ? null : getLocation();
        System.out.println("Game window position " + location);
        return new Settings(getSize(), location, state, getClass().getSimpleName());
    }
}
