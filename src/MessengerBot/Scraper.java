package MessengerBot;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Scraper {
	private ChromeDriver driver;
	private static Scraper sc = null;
	private static ArrayList<String> importantSenders = new ArrayList<String>();
	public static Scraper Singleton() 
    { 
        if (sc == null) 
        { 
            sc = new Scraper(); 
        } 
        return sc; 
    }
	public Scraper(Overlay overlay)
	{
		
	}
	public Scraper()
	{
		
	}
	
	public void setup()
	{
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\chromedriver83.exe");
		HashMap<String, Object> chromePrefs = createChromePrefs();
		ChromeOptions chromeOptions = createChromeOptions(chromePrefs);
		driver = new ChromeDriver(chromeOptions);
		driver.manage().window().setSize(new Dimension(1900,980));
		driver.get("https://www.messenger.com/");
	}
	
	private HashMap<String, Object> createChromePrefs()
	{
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("profile.default_content_setting_values.notifications", 2);
		return chromePrefs;
	}
	
	private ChromeOptions createChromeOptions(HashMap<String, Object> chromePrefs)
	{
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setExperimentalOption("prefs", chromePrefs);
	    chromeOptions.addArguments("--headless");
	    chromeOptions.addArguments("--disable-gpu");
		chromeOptions.addArguments("user-data-dir="+System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\Cookies");
		chromeOptions.addArguments("--mute-audio");
	    chromeOptions.addArguments("use-fake-ui-for-media-stream");
		return chromeOptions;
	}
//	public void screenshot()
//	{
//		TakesScreenshot scrShot =((TakesScreenshot)driver);
//		File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
//		SrcFile.renameTo(new File("D:\\screen.png"));
//		try {
//			FileUtils.moveFile(SrcFile, new File("D:\\newScreen.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public void login(String email,String password)
	{
		WebElement emailElement = driver.findElement(By.cssSelector("#email"));
		emailElement.click();
		emailElement.clear();
		emailElement.sendKeys(email);
		WebElement passwordElement = driver.findElement(By.cssSelector("#pass"));
		passwordElement.click();
		passwordElement.sendKeys(password);
		email = null;
		password = null;
		driver.findElement(By.cssSelector(".uiInputLabelInput")).click();
		driver.findElement(By.cssSelector("#loginbutton")).click();
	}
	
	public boolean isLoggedInSuccessfully()
	{
		try {
			driver.findElement(By.cssSelector("._3403._3404"));
			return false;
		}catch(Exception e) {
			return true;
		}
	}
	
	public void scrap()
	{
		cancelCall();
		try {
	    	WebElement newChatElement = getNewChatElement();
	    	String senderName = newChatElement.findElement(By.cssSelector("._1ht6._7st9")).getText();
	    	String newMessage = getNewChatMessage(newChatElement);
	    	getTextFocus();
	    	if(importantSenders.contains(senderName))
	    		showOverLay(newMessage,getSenderPhoto(newChatElement));
	    	else if(newMessage.toLowerCase().contains("important"))
	    	{
	    		importantSenders.add(senderName);
	    		showOverLay(newMessage,getSenderPhoto(newChatElement));
	    	}
	    	else
	    		writeMessage(AutoReplySettings.generalReply);
	    		
	    	
//	    	if(newMessage.toLowerCase().contains("important"))
//	    	{
//		    	if(!mutedSenders.contains(senderName))
//		    	{
//		    		mutedSenders.add(senderName);
//		    		writeMessage(AutoReplySettings.importantReply);
//		    		Tray.Singleton().notifyUser();
//		    	}
//		    	else
//		    		writeMessage("please wait..");
//	    	}
//	    	else
//	    		writeMessage(AutoReplySettings.generalReply);
//	    	sendMessage();
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	private void cancelCall()
	{
		try {
			driver.findElement(By.cssSelector("._3quh._30yy._2u0._5ixy")).click();
		}catch(Exception e) {
//			e.printStackTrace();
		}
	}
	private File getSenderPhoto(WebElement newChatElement)
	{
		File senderPhoto = newChatElement.findElement(By.cssSelector("img")).getScreenshotAs(OutputType.FILE);
		return senderPhoto;
	}
	private WebElement getNewChatElement()
	{
    	WebElement newChatElement = driver.findElement(By.cssSelector("ul[role=\"grid\"] li[aria-live=\"polite\""));
    	return newChatElement;
	}
	private String getNewChatMessage(WebElement newChatElement)
	{
    	String newMessage = newChatElement.findElement(By.cssSelector("span span")).getText();
    	newChatElement.click();
    	return newMessage;
	}
	
	private void getTextFocus()
	{
		driver.findElement(By.cssSelector("div[data-offset-key]")).click();
	}
	
	private void writeMessage(String message)
	{
		driver.findElement(By.cssSelector("div[data-offset-key]")).sendKeys(message);
	}
	
	private void sendMessage()
	{
		driver.findElement(By.cssSelector("div[data-offset-key]")).sendKeys("\n");
	}
	public void makeRecord()
	{
		driver.findElement(By.cssSelector("._7mki")).click();
		sleep();
		try {
			driver.findElement(By.cssSelector("._30yy._7oam:nth-of-type(3)")).click();
		}catch(Exception e) {
			e.printStackTrace();
		}
		sleep();
		try {
			driver.findElement(By.cssSelector("._3z55")).click();						
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void stopRecord()
	{
		try {
			driver.findElement(By.cssSelector("._3z55")).click();			
		}catch(Exception e) {
			e.printStackTrace();
		}
		sleep();
		try{
			driver.findElement(By.cssSelector("._7mki._7mkj")).click();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void sleep()
	{
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void quit()
	{
		try {
			driver.quit();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
