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
import javax.swing.SwingConstants;
import javax.swing.JWindow;

public class Overlay implements Runnable {

    private JWindow window;
    private JLabel newChatLabel;
    private JLabel senderPhotoLabel;
    @Override
    public void run() {
        EventQueue.invokeLater(() -> {
            try {
                setupOverlay();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        while (true) {
            try {
                if (Chat.Singleton().getMessagesSize() != 0)
                    popupOverlay();
                else
                {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void popupOverlay(){
        try {
            showOverlay();
            Thread.sleep(128 * Chat.Singleton().getMessage().length());  //Assuming that one character is read in 128ms
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        hideOverlay();
        Chat.Singleton().removeSeenChat();
    }

    private void showOverlay() throws IOException {
        newChatLabel.setText("<html><div style='text-align: right;'>" + Chat.Singleton().getMessage() + "</div></html>");
        if(Chat.Singleton().getPhotosSize() != 0)
            senderPhotoLabel.setIcon(getCircularImage());
        else
            senderPhotoLabel.setIcon(new ImageIcon(Overlay.class.getResource("/importedFiles/robot64p.png")));
        window.setVisible(true);
    }

    private void hideOverlay() {
        window.setVisible(false);
    }

    public void setupOverlay() {
        window = new JWindow();
        window.setVisible(false);
        newChatLabel = new JLabel("");
        senderPhotoLabel = new JLabel("");
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

        //senderPhotoLabel.setIcon(new ImageIcon(senderPhotos.get(0)));
    }

    private ImageIcon getCircularImage() throws IOException {
        BufferedImage mask = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mask.createGraphics();
        g2d.fillOval(0, 0, 50, 50);
        g2d.dispose();
        BufferedImage masked = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        g2d = masked.createGraphics();
        g2d.drawImage(ImageIO.read(new File(Chat.Singleton().getPhotoPath())), 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g2d.drawImage(mask, 0, 0, null);
        g2d.dispose();
        return new ImageIcon(masked);
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

