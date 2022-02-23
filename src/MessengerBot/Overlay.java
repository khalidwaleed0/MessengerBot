package MessengerBot;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.SwingConstants;
import javax.swing.JWindow;

public class Overlay implements Runnable {

    public ArrayList<String> displayedMessages = new ArrayList<>();
    public ArrayList<String> displayedPhotos = new ArrayList<>();
    private boolean overLayFinished = false;

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Overlay : " + displayedMessages);
                System.out.println("Overlay Photos : " + displayedPhotos);
                if ((displayedPhotos.size() != 0) && !Recorder.isRecording) {
                    EventQueue.invokeLater(() -> {
                        try {
                            setupOverlay(displayedMessages.get(0));
                            overLayFinished = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    while (!overLayFinished) {
                        Thread.sleep(1000);
                    }
                    removeSeenChat();
                    overLayFinished = false;
                } else
                    sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setupOverlay(String newChat) throws Exception {
        JWindow window = new JWindow();
        JLabel newChatLabel = new JLabel("");
        JLabel senderPhotoLabel = new JLabel("");
        JPanel contentPane = new JPanel();

        window.setAlwaysOnTop(true);
        window.setBackground(new Color(0, 0, 0, 0));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();
        window.setLocation((int) screenWidth - 450, (int) screenHeight - 300);
        window.setSize(450, 176);
        contentPane.setBackground(new Color(0, 0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        window.setContentPane(contentPane);
        contentPane.setLayout(null);

        newChatLabel.setBackground(new Color(0, 0, 0, 110));
        newChatLabel.setForeground(Color.WHITE);
        newChatLabel.setOpaque(true);
        newChatLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        newChatLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        newChatLabel.setVerticalAlignment(SwingConstants.TOP);
        newChatLabel.setBounds(78, 11, 346, 154);
        contentPane.add(newChatLabel);

        senderPhotoLabel.setBounds(8, 11, 60, 60);
        contentPane.add(senderPhotoLabel);

        newChatLabel.setText("<html><div style='text-align: right;'>" + newChat + "</div></html>");
        if (!Recorder.isRecording)
            senderPhotoLabel.setIcon(getCircularImage());
        //senderPhotoLabel.setIcon(new ImageIcon(senderPhotos.get(0)));
        window.setVisible(true);
        sleep(128 * newChat.length());  //Assuming that one character is read in 128ms
        window.dispose();
    }

    private ImageIcon getCircularImage() throws IOException {
        BufferedImage mask = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mask.createGraphics();
        g2d.fillOval(0, 0, 55, 55);
        g2d.dispose();
        BufferedImage masked = new BufferedImage(55, 55, BufferedImage.TYPE_INT_ARGB);
        g2d = masked.createGraphics();
        g2d.drawImage(ImageIO.read(new File(displayedPhotos.get(0))), 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g2d.drawImage(mask, 0, 0, null);
        g2d.dispose();
        return new ImageIcon(masked);
    }

    private void removeSeenChat() {
        displayedMessages.remove(0);
        displayedPhotos.remove(0);
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

