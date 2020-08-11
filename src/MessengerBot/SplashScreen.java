package MessengerBot;

import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

public class SplashScreen implements Runnable{
	private JLabel loadingText;
	@Override
	public void run() {
		JWindow window = windowSetup();    	
    	window.dispose();
	}
	private JWindow windowSetup()
	{
		JWindow window = new JWindow();
		window.setSize(256, 288);
	   	window.setLocationRelativeTo(null);
    	window.getContentPane().setBackground(new Color(0, 255, 0));
	   	loadingText = new JLabel();
		loadingText.setFont(new Font("Tahoma", Font.BOLD, 11));
		loadingText.setBounds(10, 262, 236, 14);
		window.add(loadingText);
    	JLabel loadingLabel = new JLabel(new ImageIcon(SplashScreen.class.getResource("/importedFiles/robotGif0.gif")));
    	loadingLabel.setBounds(0, 0, 256, 256);
    	window.getContentPane().add(loadingLabel);
    	window.setVisible(true);
    	loadingInfo();
    	return window;
	}
	private void loadingInfo()
	{
		loadingText.setText("Trying to remember your name ^_^");
		sleep();
		loadingText.setText("33% please wait..");
		sleep();
		loadingText.setText("checking installation files..");
		sleep();
		loadingText.setText("66% please wait..");
		sleep();
		loadingText.setText("opening chrome..");
		sleep();
		loadingText.setText("99% please wait..");
		sleep();
		loadingText.setText("opening messenger.com");
		sleep();
	}
	private void sleep()
	{
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}