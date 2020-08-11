package MessengerBot;

import java.util.ArrayList;
import java.util.HashMap;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Scraper {
	private ChromeDriver driver;
	String newChatName;
	private static Scraper sc = null;
	private static ArrayList<String> mutedSenders = new ArrayList<String>();
	public static Scraper Singleton() 
    { 
        if (sc == null) 
        { 
            sc = new Scraper(); 
        } 
        return sc; 
    }
	
	public void setup()
	{
		long x = System.currentTimeMillis();
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\chromedriver83.exe");
		HashMap<String, Object> chromePrefs = createChromePrefs();
		ChromeOptions chromeOptions = createChromeOptions(chromePrefs);
		driver = new ChromeDriver(chromeOptions);
		driver.manage().window().setSize(new Dimension(1900,980));
		driver.get("https://www.messenger.com/");
		System.out.println(System.currentTimeMillis() - x);
		if(isArabic())
			newChatName = "ul[aria-label=\"قائمة المحادثات\"] li[aria-live=\"polite\"";
		else
			newChatName = "ul[aria-label=\"Conversation List\"] li[aria-live=\"polite\"";
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
		return chromeOptions;
	}
	private boolean isArabic()
	{
		if(driver.findElement(By.cssSelector("._6-xo")).getText().equals("الدردشات"))
			return true;
		else
			return false;
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
	    	if(newMessage.toLowerCase().contains("important"))
	    	{
		    	if(!mutedSenders.contains(senderName))
		    	{
		    		mutedSenders.add(senderName);
		    		writeMessage(AutoReplySettings.importantReply);
		    		Tray.Singleton().notifyUser();
		    	}
		    	else
		    		writeMessage("please wait..");
	    	}
	    	else
	    		writeMessage(AutoReplySettings.generalReply);
	    	sendMessage();
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
	private WebElement getNewChatElement()
	{
    	WebElement newChatElement = driver.findElement(By.cssSelector(newChatName));
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
		driver.findElement(By.cssSelector("div[class=\"_1mf _1mj\"]")).click();
	}
	
	private void writeMessage(String message)
	{
		driver.findElementByCssSelector("div[class=\"_1mf _1mj\"]").sendKeys(message);
	}
	
	private void sendMessage()
	{
		driver.findElement(By.cssSelector("a[aria-label=\"Send\"]")).click();
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
