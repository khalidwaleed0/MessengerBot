package MessengerBot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Scraper {
    private static final ArrayList<String> importantSenders = new ArrayList<>();
    private static Scraper sc = null;
    public boolean isSessionCreated = false;
    private ChromeDriver driver;
    private Overlay overlay;
    private String senderName, newMessage;
    private WebElement newChatElement;

    public static Scraper Singleton() {
        if (sc == null) {
            sc = new Scraper();
        }
        return sc;
    }

    public void passOverlay(Overlay overlay) {
        this.overlay = overlay;
    }

    public void setupAndInitializeSession() {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\MessengerBot\\chromedriver.exe");
        HashMap<String, Object> chromePrefs = createChromePrefs();
        ChromeOptions chromeOptions = createChromeOptions(chromePrefs);
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().setSize(new Dimension(1900, 980));
        lookAway();
        isSessionCreated = true;
    }

    private HashMap<String, Object> createChromePrefs() {
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("profile.default_content_setting_values.notifications", 2);
        return chromePrefs;
    }

    private ChromeOptions createChromeOptions(HashMap<String, Object> chromePrefs) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", chromePrefs);
        //chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("user-data-dir=" + System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\MessengerBot\\Cookies");
        chromeOptions.addArguments("--mute-audio");
        chromeOptions.addArguments("use-fake-ui-for-media-stream");
        return chromeOptions;
    }

    public void login(String email, String password) {
        WebElement emailTextBox = driver.findElement(By.cssSelector("#email"));
        emailTextBox.click();
        emailTextBox.clear();
        emailTextBox.sendKeys(email);
        WebElement passwordTextBox = driver.findElement(By.cssSelector("#pass"));
        passwordTextBox.click();
        passwordTextBox.sendKeys(password);
        driver.findElement(By.cssSelector(".uiInputLabelInput")).click(); //Keep me signed in checkbox
        //Maybe we need here thread.sleep 200ms or something
        driver.findElement(By.cssSelector("#loginbutton")).click();
    }

    public boolean isLoggedInSuccessfully() {
        try {
            driver.findElement(By.cssSelector("._3403._3404"));
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public void scrap() {
        try {
            cancelCall();
            lookAway();
        } catch (Exception ignored) {
        }
        try {
            newChatElement = getNewChatElement();
            if (isGroupChat()) return;             //MessengerBot shouldn't bother group chats
            newMessage = getNewChatMessage();
            senderName = getSenderName();
            openChat();
            respondToMessage();
            lookAway();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void respondToMessage() throws InterruptedException {
        if (importantSenders.contains(senderName))
            appendToDisplayedChats();
        else if (newMessage.toLowerCase().contains("important")) {
            importantSenders.add(senderName);
            appendToDisplayedChats();
        } else {    // The Period between clicking the chat element and writing the message should be >= 500ms
            Thread.sleep(500);
            sendMessage(AutoReplySettings.generalReply);
        }
    }

    private void appendToDisplayedChats() {
        this.overlay.displayedMessages.add(newMessage);
        this.overlay.displayedPhotos.add(getSenderPhoto().getPath());
    }

    private void cancelCall() throws NoSuchElementException {
        driver.findElement(By.cssSelector("div[aria-label=Close]")).click();
    }

    private File getSenderPhoto() {
        File senderPhoto = null;
        try {
            senderPhoto = newChatElement.findElement(By.cssSelector("img")).getScreenshotAs(OutputType.FILE);
        } catch (Exception ignored) {
        }
        return senderPhoto;
    }

    private WebElement getNewChatElement() {
        return driver.findElement(By.xpath(".//div[@role='gridcell'][.//div[@aria-label='Mark as read']]"));
    }

    private boolean isGroupChat() {
        String link = newChatElement.findElement(By.cssSelector("a")).getAttribute("href");
        link = link.replaceAll("\\D", "");
        return (link.length() >= 16);
    }

    private String getNewChatMessage() {
        return newChatElement.findElements(By.cssSelector("span[dir=auto] span")).get(1).getText();
    }

    private String getSenderName() {
        return newChatElement.findElement(By.cssSelector("span[dir=auto] span")).getText();
    }

    private void openChat() {
        newChatElement.findElement(By.cssSelector("div[aria-label='Mark as read']")).click();            // click 'mark as read' blue dot
        newChatElement.click();
    }

    public void clickFirstChat() { //opens the first chat which is not a messengerbot chat
        driver.findElement(By.cssSelector("div[role=gridcell] a:not(a[href='/t/107105541890654/'])")).click();
    }

    private void lookAway() {
        try{
            WebElement messengerBotChat = driver.findElement(By.cssSelector("div[role=gridcell] a[href='/t/107105541890654/']"));
            messengerBotChat.click();
        }catch (Exception e){
            driver.navigate().to("https://www.messenger.com/t/107105541890654");
        }
        // Gets away from the current chat by going to messengerbot chat.
        // This is useful as it guarantees that no message is seen by mistake.
    }

    private void sendMessage(String message) {
        driver.switchTo().activeElement().sendKeys(message + "\n");
    }

    public void startRecord() {
        Scraper.Singleton().clickFirstChat();    //This is a must because MessengerBot always goes to its FB page after seeing any message
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {}
        driver.findElement(By.cssSelector("div[aria-label='Open more actions']")).click();
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

        //wait.until(webDriver -> ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[role=dialog]")));
        try {
            Thread.sleep(200);
            driver.findElement(By.xpath(".//div[@role='dialog']//*[contains(text(), 'Send a voice clip')]")).click();
            Thread.sleep(100);
            driver.findElement(By.cssSelector("div[aria-label=OK]")).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecord() {
        try {
            driver.findElement(By.cssSelector("div[aria-label='Press enter to send']")).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        lookAway();
    }

    public void quit() {
        try {
            driver.quit();
        } catch (Exception e) {
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
