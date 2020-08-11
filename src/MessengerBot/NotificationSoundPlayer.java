package MessengerBot;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class NotificationSoundPlayer implements Runnable{

	@Override
	public void run() {
		File customSound = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\customSound.mp3");
		if(customSound.exists())
			playSoundFile(customSound);
		else
		{
			File originalSound = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\originalSound.mp3");
			playSoundFile(originalSound);
		}
		
	}
	private void playSoundFile(File soundFile)
	{
		try {
			FileInputStream fis = new FileInputStream(soundFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			Player player = new Player(bis);
			player.play();
		} catch (FileNotFoundException | JavaLayerException e) {
			e.printStackTrace();
		}
	}
}
