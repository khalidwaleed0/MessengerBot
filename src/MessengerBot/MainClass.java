package MessengerBot;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

import org.openqa.selenium.SessionNotCreatedException;

public class MainClass {

    public static void main(String[] args) {
        try {
            AppUpdater.update();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        AppSetup.Singleton().setup();
        SplashScreen splashScreen = new SplashScreen();
        Thread splashScreenThread = new Thread(splashScreen);
        splashScreenThread.start();
        try {
            Scraper.Singleton().setupAndInitializeSession();
        } catch (SessionNotCreatedException e) {
            deleteChromeDriver();
            showHelpGui();
        } catch (IllegalStateException e2) {
            showHelpGui();
        }
        if (Scraper.Singleton().isSessionCreated) {
            showLoginGui();
            showMainGui();
            generalSetup();
            scrapLoop();
        }
    }

    private static void deleteChromeDriver() {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
            File oldChromeDriver = new File(System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\MessengerBot\\chromedriver.exe");
            oldChromeDriver.delete();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private static void showLoginGui() {
        if (!AppSetup.Singleton().isAlreadyLoggedIn()) {
            EventQueue.invokeLater(() -> {
                try {
                    LoginGui frame = new LoginGui();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            while (!LoginGui.finished) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void showMainGui() {
        EventQueue.invokeLater(() -> {
            try {
                MainGui frame2 = new MainGui();
                frame2.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        while (!MainGui.finished) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void generalSetup() {
        AppSetup.Singleton().getRecordedKey();
        Tray.Singleton().createTrayIcon();
        Recorder recorder = new Recorder();
        Thread rec = new Thread(recorder);
        rec.start();
        Overlay overlay = new Overlay();
        Scraper.Singleton().passOverlay(overlay);
        Thread overlayThread = new Thread(overlay);
        overlayThread.start();
    }

    private static void scrapLoop() {
        while (true)
        {
            try
            {
                if (Recorder.isRecording || Tray.Singleton().isPaused)
                {
                    Thread.sleep(1000);
                }
                else
                {
                    Scraper.Singleton().scrap();
                    Thread.sleep(1000);
                }
            }
            catch (Exception ignored) {}
        }
    }

    private static void showHelpGui() {
        EventQueue.invokeLater(() -> {
            try {
                UpdateHelp uh = new UpdateHelp();
                uh.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
