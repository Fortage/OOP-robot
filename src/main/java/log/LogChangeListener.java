package log;

import java.awt.event.WindowEvent;

public interface LogChangeListener {
    void onLogChanged();

    void windowClosing(WindowEvent we);
}
