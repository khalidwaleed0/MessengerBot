package MessengerBot;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;

public class AppSetup {
	private static AppSetup  appSetup;
	public static AppSetup Singleton()
	{
		if (appSetup == null) 
        { 
            appSetup = new AppSetup(); 
        } 
        return appSetup; 
	}
	public void setup()
	{
		String launchFolder = System.getProperty("user.dir");
		launchFolder = launchFolder.substring(launchFolder.length()-7);
		if(!launchFolder.equals("Desktop"))
		{
			JOptionPane.showMessageDialog(null, "Please move the program to the desktop and open it again", "MessengerBot", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
		checkChromeInstallation();
		if(!isChromeDriverInstalled())
			extractChromeDriver();
		checkOriginalSound();
		createLoginStatus();
		checkUserName();
		AutoReplySettings.Singleton().checkBasicReplySettings();
	}
	private void checkChromeInstallation()
	{
		File chromePath32 = new File("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
		File chromePath64 = new File("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
		if(!(chromePath32.exists() || chromePath64.exists()))
		{
			JOptionPane.showMessageDialog(null, "This program requires Google Chrome to be installed\\nPlease install it and try again"
					, "MessengerBot", JOptionPane.WARNING_MESSAGE);
			try {
				Desktop.getDesktop().browse(new URI("https://www.google.com/intl/en_us/chrome/"));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
	}
	public boolean isAlreadyLoggedIn()
	{
		String line = "false";
		try {
			FileReader reader = new FileReader(new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\loginStatus.txt"));
			BufferedReader br = new BufferedReader(reader);
			line = br.readLine();
			br.close();
		}catch(Exception e) {
			System.out.println("problem in already logged in");
		}
		return Boolean.parseBoolean(line);
	}
	private boolean isChromeDriverInstalled()
	{
		File chromedriver = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\chromedriver83.exe");
		if(chromedriver.exists())
			return true;
		else
			return false;
	}
	private void extractChromeDriver()
	{
		InputStream input = (MainClass.class.getResourceAsStream("/importedFiles/chromedriver83.exe"));
		File chromedriver = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\chromedriver83.exe");
		try {
			FileUtils.copyInputStreamToFile(input, chromedriver);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void checkOriginalSound()
	{
		File originalSound = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\originalSound.mp3");
		if(!originalSound.exists())
		{
			InputStream input = (MainClass.class.getResourceAsStream("/importedFiles/originalSound.mp3"));
			try {
				FileUtils.copyInputStreamToFile(input, originalSound);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void createLoginStatus()
	{
		File loginStatus = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\loginStatus.txt");
		if(!loginStatus.exists())
		{
			submitLoginStatus(false);
		}
	}
	
	public void submitLoginStatus(boolean loginStatus)
	{
		try {
			FileWriter writer = new FileWriter(new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\loginStatus.txt"));
			writer.write(String.valueOf(loginStatus));
			writer.close();
		} catch (IOException e) {
			System.out.println("failed");
			e.printStackTrace();
		}
	}
	public boolean isCustomSoundExisted()
	{
		File customSoundFile = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\customSound.mp3");
		if(customSoundFile.exists())
			return true;
		return false;
	}
	private void checkUserName()
	{
		File nullUserNameFile = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\NoUserName.txt");
		if(!(isUserNameExisted() || nullUserNameFile.exists()))
		{
			String userName = JOptionPane.showInputDialog(null, "Please enter your first name (Optional)\n                 -Unchangeable-", "MessengerBot"
					, JOptionPane.INFORMATION_MESSAGE);
			
			if((userName!=null) && !userName.equals(""))
			{
				try {
					FileWriter writer = new FileWriter(new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\UserName.txt"));
					if(userName.equals("ahmed") || userName.equals("ahmad"))
						userName = "Hamada";
					writer.write(userName);
					writer.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			else
			{
				try {
					nullUserNameFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public boolean isUserNameExisted()
	{
		File userNameFile = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\UserName.txt");
		if(userNameFile.exists())
			return true;
		return false;
	}
	public void logout()
	{
		File cookiesFolder = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\Cookies");
		try {
			FileUtils.deleteDirectory(cookiesFolder);
			submitLoginStatus(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void uninstall()
	{
		Scraper.Singleton().quit();
		File installationFolder = new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\");
		try {
			FileUtils.deleteDirectory(installationFolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
