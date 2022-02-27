package MessengerBot;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tray {
    private static Tray tr;
    public boolean isPaused = false;
    private TrayIcon trayIcon;

    public static Tray Singleton() {
        if (tr == null) {
            tr = new Tray();
        }
        return tr;
    }

    public void createTrayIcon() {
        createIcon();
        createMenu();
    }

    private void createIcon() {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage(Tray.class.getResource("/importedFiles/robot.png"));
        trayIcon = new TrayIcon(image, "Java AWT Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("MessengerBot");
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void createMenu() {
        final PopupMenu popup = new PopupMenu();
        MenuItem pauseItem = new MenuItem("Pause");
        MenuItem logoutItem = new MenuItem("Logout & Exit");
        MenuItem exitItem = new MenuItem("Exit (fast)");
        popup.add(pauseItem);
        popup.add(logoutItem);
        //popup.add(exitItem);
        trayIcon.setPopupMenu(popup);
        pauseItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) {
                    isPaused = true;
                    pauseItem.setLabel("Resume");
                } else {
                    isPaused = false;
                    pauseItem.setLabel("Pause");
                }
            }
        });
        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Scraper.Singleton().quit();
                AppSetup.Singleton().deleteCookies();
                System.exit(0);
            }
        });
        /*exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Scraper.Singleton().quit();
                System.exit(0);
            }
        });*/

    }
}
