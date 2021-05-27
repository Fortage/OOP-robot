package gui;

import log.Logger;
import log.RobotLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */

public class MainApplicationFrame extends JFrame implements Serializable, Settable, Observer {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        LogWindow logRobotWindow = createRobotLogWindow();
        addWindow(logRobotWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        MenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar.initMenu());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitMainWindow();
            }
        });
        readSettings();
    }

    protected static boolean confirmClosing(Component window) {
        Object[] options = {"Да", "Нет"};
        int answer = JOptionPane.showOptionDialog(window,
                "Закрыть окно?",
                "Выход",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        return answer == 1;
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    private void serialize() {
        File file = new File("data.bin");
        try (OutputStream os = new FileOutputStream(file)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
                oos.writeObject(this);
                for (JInternalFrame frame : desktopPane.getAllFrames()) {
                    oos.writeObject(frame);
                    System.out.println("Serialize frame: " + frame);
                }
                oos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object writeReplace() {
        return new Settings(getSize(), getLocationOnScreen(), getState(), getClass().getSimpleName());
    }

    private void exitMainWindow() {
        if (!confirmClosing(gui.MainApplicationFrame.this)) {
            serialize();
            System.exit(0);
        }
    }

    private void readSettings() {
        File file = new File("data.bin");
        if (file.exists()) {
            try (InputStream is = new FileInputStream(file)) {
                try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is))) {
                    Settings settings = (Settings) ois.readObject();
                    setSettings(settings);
                    for (int i = 0; i < desktopPane.getAllFrames().length; i++) {
                        settings = (Settings) ois.readObject();
                        for (JInternalFrame frame : desktopPane.getAllFrames()) {
                            if (frame.getClass().getSimpleName().equals(settings.getWindowName())) {
                                Settings.setSettings(settings, frame);
                            }
                        }
                    }
                } catch (EOFException ex) {
                    // just ignore
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setSettings(Settings settings) {

        setState(settings.getState());
        setBounds(settings.getLocation().x, settings.getLocation().y,
                settings.getScreenSize().width,
                settings.getScreenSize().height);
    }

    private MenuBar createMenuBar() {

        return new MenuBar(this);
    }

    private LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), "Протокол работы");
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    private LogWindow createRobotLogWindow() {
        LogWindow logWindow = new LogWindow(RobotLogger.getDefaultLogSource(), "Протокол работы робота");
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        return logWindow;
    }

    private void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
}
