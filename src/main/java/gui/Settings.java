package gui;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.Serializable;

public class Settings implements Serializable {
    private String windowName;
    private Dimension screenSize;
    private Point location;
    private int state;

    public Settings(Dimension size, Point locol, int state, String name) {
        screenSize = size;
        location = locol;
        this.state = state;
        windowName = name;
    }

    protected Dimension getScreenSize()
    {
        return this.screenSize;
    }

    protected Point getLocation()
    {
        return this.location;
    }

    protected int getState()
    {
        return this.state;
    }

    protected String getWindowName()
    {
        return this.windowName;
    }

    protected static void setSettings(Settings settings, JInternalFrame window) {
        if (settings.state == 1) {
            try {
                window.setIcon(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
        }
        if (settings.location == null) {
            window.setSize(settings.screenSize);
        } else {
            window.setBounds(settings.location.x, settings.location.y,
                    settings.screenSize.width,
                    settings.screenSize.height);
        }
    }

}