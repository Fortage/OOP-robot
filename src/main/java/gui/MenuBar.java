package gui;

import log.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

public class MenuBar extends JMenuBar
{
    private MainApplicationFrame ApplicationFrame;
    private ResourceBundle resourceBundle;

    public MenuBar(MainApplicationFrame mainApplicationFrame) {
        ApplicationFrame = mainApplicationFrame;
    }

    public JMenuBar initMenu(){
        JMenuBar menuBar = new JMenuBar();
        //Set up the lone menu.

        createMenuDocuments(menuBar);
        //createLookAndFeelMenu(menuBar);
        createTestMenu(menuBar);
        createLocalizationMenu(menuBar);


        return menuBar;
    }

    private void createMenuDocuments(JMenuBar menuBar){
        JMenu menu = new JMenu("Документ");
        menu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menu);

        //Set up the first menu item.

        JMenuItem menuItem = new JMenuItem("Новое окно", KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("new");
        //        menuItem.addActionListener(this);
        menu.add(menuItem);

        //Set up the second menu item.
        menuItem = new JMenuItem("Выход", KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("quit");
        menuItem.addActionListener((event) -> {
            if (!MainApplicationFrame.confirmClosing(this)) {
                this.ApplicationFrame.setVisible(false);
                this.ApplicationFrame.dispose();
                System.exit(0);
            }
        });

        menu.add(menuItem);
    }

   /* private void createLookAndFeelMenu(JMenuBar menuBar){
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                //this.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                //this.mainFrame1.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                //this.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                //this.mainFrame1.invalidate();

            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        menuBar.add(lookAndFeelMenu);
    }*/

    private void createTestMenu(JMenuBar menuBar) {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Тест");
            });
            testMenu.add(addLogMessageItem);
        }

        menuBar.add(testMenu);
    }

    private void createLocalizationMenu(JMenuBar menuBar) {
        JMenu testMenu = new JMenu("Язык");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Выбор языка");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Русский", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Тест");
            });
            testMenu.add(addLogMessageItem);
        }

        menuBar.add(testMenu);
    }

    public void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e)
        {

            // just ignore
        }
    }
}
