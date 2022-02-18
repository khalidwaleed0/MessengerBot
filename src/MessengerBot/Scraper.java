package MessengerBot;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Scraper {
	private ChromeDriver driver;
	private static Scraper sc = null;
	private static ArrayList<String> importantSenders = new ArrayList<String>();
	private Overlay overlay;
	public boolean isSessionCreated = false;
	public static Scraper Singleton() 
    {
        if (sc == null) 
        { 
            sc = new Scraper(); 
        } 
        return sc; 
    }
	public void passOverlay(Overlay overlay)
	{
		this.overlay = overlay;
	}
	public void setup()
	{
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\chromedriver.exe");
		HashMap<String, Object> chromePrefs = createChromePrefs();
		ChromeOptions chromeOptions = createChromeOptions(chromePrefs);
		driver = new ChromeDriver(chromeOptions);
		driver.manage().window().setSize(new Dimension(1900,980));
		driver.navigate().to("https://www.messenger.com/");
		isSessionCreated = true;
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
		chromeOptions.addArguments("user-data-dir="+System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\Cookies");
		chromeOptions.addArguments("--mute-audio");
	    chromeOptions.addArguments("use-fake-ui-for-media-stream");
		return chromeOptions;
	}	
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
		try {
			cancelCall();
			look_away();
		}catch (Exception e){}
		try {
	    	WebElement newChatElement = getNewChatElement();
			if(isGroupChat(newChatElement))
				return;
	    	String newMessage = getNewChatMessage(newChatElement);
	    	String senderName = newChatElement.findElement(By.cssSelector("span[dir=auto] span")).getText();
	    	newChatElement.findElement(By.cssSelector("div[aria-label='Mark as read']")).click();            // click mark as read blue dot
	    	newChatElement.click();
	    	respondToMessage(senderName,newMessage,newChatElement);
			look_away();
	    }catch(Exception e) {

	    }
	}

	private void respondToMessage(String senderName, String newMessage, WebElement newChatElement) throws InterruptedException {
		if(importantSenders.contains(senderName))
		{
			this.overlay.newChats.add(newMessage);
			this.overlay.senderPhotos.add(getSenderPhoto(newChatElement).getPath());
		}
		else if(newMessage.toLowerCase().contains("important"))
		{
			importantSenders.add(senderName);
			this.overlay.newChats.add(newMessage);
			this.overlay.senderPhotos.add(getSenderPhoto(newChatElement).getPath());
		}
		else
		{	// The Period between clicking the chat element and writing the message should be >= 500ms
			Thread.sleep(500);
			sendMessage(AutoReplySettings.generalReply);
		}
	}

	private void cancelCall() throws NoSuchElementException
	{
			driver.findElement(By.cssSelector("div[aria-label=Close]")).click();
	}
	private File getSenderPhoto(WebElement newChatElement)
	{
		File senderPhoto = null;
		try {
			senderPhoto = newChatElement.findElement(By.cssSelector("img")).getScreenshotAs(OutputType.FILE);
		}catch(Exception e) {}
		return senderPhoto;
	}
	private WebElement getNewChatElement()
	{
    	WebElement newChatElement = driver.findElement(By.xpath(".//div[@role='gridcell'][.//div[@aria-label='Mark as read']]"));
    	return newChatElement;
	}
	private boolean isGroupChat(WebElement newChatElement)
	{
		String link = newChatElement.findElement(By.cssSelector("a")).getAttribute("href");
		link = link.replaceAll("\\D","");
		return (link.length() >= 16);
	}
	private String getNewChatMessage(WebElement newChatElement)
	{
    	String newMessage = newChatElement.findElements(By.cssSelector("span[dir=auto] span")).get(1).getText();
    	return newMessage;
	}
	public void clickFirstChat(){driver.findElement(By.cssSelector("div[role=gridcell]")).click();}
	private void look_away()
	{
		driver.navigate().to("https://www.messenger.com/t/107105541890654");
		// Gets away from the current chat by going to messengerbot chat.
		// This is useful as it guarantees that no message is seen by mistake.
	}
	private void sendMessage(String message)
	{
		driver.switchTo().activeElement().sendKeys(message+"\n");
	}

	public void makeRecord()
	{
		driver.findElement(By.cssSelector("div[aria-label='Open more actions']")).click();
		//WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

		//wait.until(webDriver -> ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[role=dialog]")));
		try {
			Thread.sleep(1000);
			driver.findElement(By.xpath(".//div[@role='dialog']//*[contains(text(), 'Send a voice clip')]")).click();
			Thread.sleep(500);
			driver.findElement(By.cssSelector("div[aria-label=OK]")).click();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void stopRecord()
	{
		try {
			driver.findElement(By.cssSelector("div[aria-label='Press enter to send']")).click();
		}catch(Exception e) {
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
}
