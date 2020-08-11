package MessengerBot;

import java.awt.EventQueue;
import java.io.IOException;

public class MainClass {
	
	public static void main(String[] args) {
		try {
			Updater.update();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		AppSetup.Singleton().setup();
		if(!((AppSetup.Singleton().isCustomSoundExisted()) && AppSetup.Singleton().isUserNameExisted()))
		{
			CustomSoundDownloader csd = new CustomSoundDownloader();
			Thread csDownloader = new Thread(csd);
			csDownloader.start();
		}		
		SplashScreen ss = new SplashScreen();
		Thread splashScreen = new Thread(ss);
		splashScreen.start();
		Scraper.Singleton().setup();
		if(!AppSetup.Singleton().isAlreadyLoggedIn())
		{
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						LoginGui frame = new LoginGui();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			while(!LoginGui.finished)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SettingsGui frame2 = new SettingsGui();
					frame2.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		while(!SettingsGui.finished)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Tray.Singleton().createTrayIcon();
		Scraper.Singleton().checkMessengerLanguage();
		while(true)
		{
			Scraper.Singleton().scrap();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
