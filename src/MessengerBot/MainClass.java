package MessengerBot;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

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
		AppSetup.Singleton().getRecordedKey();
		Tray.Singleton().createTrayIcon();
		Recorder recorder = new Recorder();
		Thread rec = new Thread(recorder);
		rec.start();
		if(AppSetup.Singleton().isOverLayEnabled)
		{
			Overlay overlay = new Overlay();
			Thread overlayThread = new Thread(overlay);
			overlayThread.start();
		}
		while(true)
		{
			if(recorder.isRecording || Tray.Singleton().isPaused)
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
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
