package MessengerBot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
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
		driver.get("https://www.messenger.com/");
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
	    chromeOptions.addArguments("--disable-gpu");
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
		cancelCall();
		try {
	    	WebElement newChatElement = getNewChatElement();
	    	String newMessage = getNewChatMessage(newChatElement);
	    	if(isGroupChat(newMessage, newChatElement))
	    		return;
	    	String senderName = newChatElement.findElement(By.cssSelector("._1ht6._7st9")).getText();
	    	newChatElement.click();
	    	getTextFocus();
	    	if(importantSenders.contains(senderName))
	    	{
	    		this.overlay.newChats.add(newMessage);
	    		this.overlay.senderPhotos.add(getSenderPhoto(newChatElement).getPath());
	    	}
	    	else if(newMessage.toLowerCase().contains("important"))
	    	{
	    		importantSenders.add(senderName);
	    		this.overlay.newChats.add(newMessage);
	    		System.out.println("before");
	    		this.overlay.senderPhotos.add(getSenderPhoto(newChatElement).getPath());
	    		System.out.println("after");
	    	}
	    	else
	    	{
	    		writeMessage(AutoReplySettings.generalReply);
	    		sendMessage();
	    	}
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
		File senderPhoto = null;
		try {
			senderPhoto = newChatElement.findElement(By.cssSelector("img")).getScreenshotAs(OutputType.FILE);
			return senderPhoto;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return senderPhoto;
	}
	private WebElement getNewChatElement()
	{
    	WebElement newChatElement = driver.findElement(By.cssSelector("ul[role=\"grid\"] li[aria-live=\"polite\""));
    	return newChatElement;
	}
	private boolean isGroupChat(String newMessage, WebElement newChatElement)
	{
		String x = newChatElement.findElements(By.cssSelector("span")).get(2).getText();
		System.out.println("X = "+x);
		if(newMessage.equals(x))
			return false;
		else
			return true;
	}
	private String getNewChatMessage(WebElement newChatElement)
	{
    	String newMessage = newChatElement.findElement(By.cssSelector("span span")).getText();
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
		System.out.println(System.currentTimeMillis() - Recorder.x);
		System.out.println("first line make record");
		driver.findElement(By.cssSelector("._7mki")).click();
		
		WebDriverWait wait1 = new WebDriverWait(driver,1);
		wait1.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("._30yy._7oam:nth-of-type(3)")));
		System.out.println("second line make record");
		try {
			driver.findElement(By.cssSelector("._30yy._7oam:nth-of-type(3)")).click();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		WebDriverWait wait2 = new WebDriverWait(driver,1);
		wait2.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("._3z55")));
		System.out.println("third line making record");
		try {
			driver.findElement(By.cssSelector("._3z55")).click();						
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("finished making record");
	}
	public void stopRecord()
	{
		try {
			driver.findElement(By.cssSelector("._3z55")).click();			
		}catch(Exception e) {
			e.printStackTrace();
		}
		WebDriverWait wait3 = new WebDriverWait(driver,1);
		wait3.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("._7mki._7mkj")));
		try{
			driver.findElement(By.cssSelector("._7mki._7mkj")).click();
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
