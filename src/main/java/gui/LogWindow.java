package gui;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

public class LogWindow extends JInternalFrame implements LogChangeListener, VetoableChangeListener {
    private final LogWindowSource m_logSource;
    private final TextArea m_logContent;

    public LogWindow(LogWindowSource logSource, String name) {
        super(name, true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(400, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
        addVetoableChangeListener(this);
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.setLength(0);
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public void windowClosing(WindowEvent we) {
        m_logSource.unregisterListener(this);
        System.out.println("test");
    }

    public void vetoableChange(PropertyChangeEvent pce)
            throws PropertyVetoException {
        if (pce.getPropertyName().equals(IS_CLOSED_PROPERTY)) {
            boolean changed = (Boolean) pce.getNewValue();
            if (changed) {
                if (MainApplicationFrame.confirmClosing(this)) {
                    m_logSource.unregisterAllListener();
                    throw new PropertyVetoException("Cancelled", null);
                }
            }
        }
    }

    private Object writeReplace() {
        int state = isIcon() ? 1 : 0;
        System.out.println("Log window state " + state);
        Point location = isIcon() ? null : getLocation();
        System.out.println("Log window position " + location);
        return new Settings(getSize(), location, state, getClass().getSimpleName());
    }
}
