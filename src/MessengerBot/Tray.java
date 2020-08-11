package MessengerBot;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tray {
	private TrayIcon trayIcon;
	private static Tray  tr;
	public static Tray Singleton()
	{
		if (tr == null) 
        { 
            tr = new Tray(); 
        } 
        return tr; 
	}
	
	public void createTrayIcon()
	{
		createIcon();
	    createMenu();
	}
	
	private void createIcon()
	{
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
	
	private void createMenu()
	{
		final PopupMenu popup = new PopupMenu();
	    MenuItem exitItem = new MenuItem("Exit (fast)");
	    MenuItem logoutItem = new MenuItem("Logout & Exit (slow)");
	    popup.add(logoutItem);
	    popup.add(exitItem);
	    trayIcon.setPopupMenu(popup);
	    exitItem.addActionListener(new ActionListener() {
	    	@Override
			public void actionPerformed(ActionEvent event) {
				Scraper.Singleton().quit();
	    		System.exit(0);
			}
	    });
	    logoutItem.addActionListener(new ActionListener() {
	    	@Override
			public void actionPerformed(ActionEvent event) {
				Scraper.Singleton().quit();
				AppSetup.Singleton().logout();
	    		System.exit(0);
			}
	    });
	}
	
	public void notifyUser()
	{
		try{
		    trayIcon.displayMessage("Messenger","Someone needs you urgently !", MessageType.NONE);
		}catch(Exception ex){
		    ex.printStackTrace();
		}
		playNotificationSound();
	}
	
	private void playNotificationSound()
	{
		NotificationSoundPlayer nsp = new NotificationSoundPlayer();
		Thread soundPlayerThread = new Thread(nsp);
		soundPlayerThread.start();
	}
}
