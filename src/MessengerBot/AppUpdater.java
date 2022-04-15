package MessengerBot;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AppUpdater {
    private static JLabel lblDownloadInfo;
    private static JDialog dialog;

    protected static void update() throws IOException {
        Document doc = Jsoup.connect("https://github.com/khalidwaleed0/MessengerBot/releases").get();
        Element latestRelease = doc.selectFirst("h1 .Link--primary");
        if (!latestRelease.text().equals("v2.2.0")) {
            showUpdateWindow();
            String whatsNew = doc.selectFirst(".markdown-body p").wholeText();
            String latestReleaseLink = "https://github.com/" + doc.selectFirst(".Box.Box--condensed.mt-3 a").attr("href");
            String fullSize = doc.selectFirst(".float-right.color-fg-muted").text();
            downloadAndShowProgress(latestReleaseLink,fullSize);
            JOptionPane.showMessageDialog(null, whatsNew, "What's New", JOptionPane.INFORMATION_MESSAGE);
            try {
                Desktop.getDesktop().open(createBatch());
            } catch (IOException ignored) {
            }
            System.exit(0);
        }
    }

    private static void downloadAndShowProgress(String latestReleaseLink, String fullSize) {
        File downloadedFile = new File(System.getProperty("user.home") + "\\Desktop\\MessengerBot(Latest).exe");
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(latestReleaseLink).openStream());
             FileOutputStream fileOS = new FileOutputStream(downloadedFile)) {
            byte[] data = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
                lblDownloadInfo.setVisible(false);
                lblDownloadInfo.setText("Downloaded : " + String.format("%.2f", (float) downloadedFile.length() / (1024 * 1024)) + " MB of " + fullSize);
                lblDownloadInfo.setVisible(true);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while downloading latest Version\nPlease download it manually");
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/khalidwaleed0/MessengerBot/releases"));
            } catch (URISyntaxException | IOException ignored) {
            }
        }
        lblDownloadInfo.setText("Download Completed");
        dialog.dispose();
    }

    private static void showUpdateWindow() {
        dialog = new JDialog();
        JPanel contentPanel = new JPanel();
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setTitle("New Update");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
        dialog.setSize(450, 151);
        dialog.getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        dialog.setLocationRelativeTo(null);

        lblDownloadInfo = new JLabel("Downloading");
        lblDownloadInfo.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblDownloadInfo.setBounds(10, 45, 380, 21);
        contentPanel.add(lblDownloadInfo);

        JLabel lblTheNewVersion = new JLabel("The new Version will be on desktop and will open automatically");
        lblTheNewVersion.setBounds(10, 87, 370, 14);
        contentPanel.add(lblTheNewVersion);
    }

    private static File createBatch() {
        File batch = new File(System.getProperty("user.home") + "\\Desktop\\updater.bat");
        try {
            PrintWriter writer = new PrintWriter(batch);
            writer.print("@echo off\r\n" +
                    "echo installing a new version of MessengerBot...\r\n" +
                    "timeout 2 >nul\r\n" +
                    "TASKKILL /im javaw.exe\r\n" +
                    "DEL MessengerBot.exe\r\n" +
                    "rename MessengerBot(Latest).exe MessengerBot.exe" + "\r\n" +
                    "MessengerBot.exe\r\n" +
                    "del updater.bat\r\n" +
                    "");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return batch;
    }
}
