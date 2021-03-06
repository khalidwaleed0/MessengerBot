package MessengerBot;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

import org.openqa.selenium.SessionNotCreatedException;

public class MainClass {
	
	public static void main(String[] args) {
		try {
			AppUpdater.update();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		AppSetup.Singleton().setup();
		SplashScreen ss = new SplashScreen();
		Thread splashScreen = new Thread(ss);
		splashScreen.start();
		try {
			Scraper.Singleton().setup();
		}catch(SessionNotCreatedException e) {
			try {
				Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
				File oldChromeDriver = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\chromedriver.exe");
				oldChromeDriver.delete();
				showHelpGui();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}catch(IllegalStateException e2) {
			showHelpGui();
		}
		if(Scraper.Singleton().isSessionCreated == true)
		{
			showLoginGui();
			showMainGui();
			generalSetup();
			scrapLoop();
		}
	}
	
	private static void showLoginGui()
	{
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
	}
	private static void showMainGui()
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui frame2 = new MainGui();
					frame2.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		while(!MainGui.finished)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private static void generalSetup()
	{
		AppSetup.Singleton().getRecordedKey();
		Tray.Singleton().createTrayIcon();
		Recorder recorder = new Recorder();
		Thread rec = new Thread(recorder);
		rec.start();
		Overlay overlay = new Overlay();
		Scraper.Singleton().passOverlay(overlay);
		Thread overlayThread = new Thread(overlay);
		overlayThread.start();
	}
	private static void scrapLoop()
	{
		while(true)
		{
			if(Recorder.isRecording || Tray.Singleton().isPaused)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else		
			{
				Scraper.Singleton().scrap();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private static void showHelpGui()
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdateHelp uh = new UpdateHelp();
					uh.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
