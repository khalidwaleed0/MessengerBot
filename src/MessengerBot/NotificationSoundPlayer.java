package MessengerBot;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class NotificationSoundPlayer implements Runnable{
	public File soundFile;
	
	@Override
	public void run() {
		playSoundFile(soundFile);
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
