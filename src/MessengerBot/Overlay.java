package MessengerBot;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.JWindow;

public class Overlay implements Runnable {

	public ArrayList<String> newChats = new ArrayList<String>();
	public ArrayList<String> senderPhotos = new ArrayList<String>();
	private JWindow window = new JWindow();
	private JLabel newChatLabel = new JLabel("");
	private JLabel senderPhotoLabel = new JLabel("");
	private JPanel contentPane;
	
	@Override
	public void run() {
		while(true)
		{
			if(newChats.size() != 0)
			{
				overlaySetup();
				displayNewChat();
				String newChat = newChats.get(0);
				removeSeenChat();
				if(newChat.length() < 40)
				{
					newChatLabel.setSize(346, 96);
					sleep(5000);
				}
				else
					sleep(9000);
			} 
			else
				sleep(1000);
		}
	}
	private void overlaySetup()
	{
		window.setAlwaysOnTop(true);
		window.setBackground(new Color(0,0,0,0));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();
		window.setLocation((int) screenWidth-450, (int) screenHeight-300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0,0,0,0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		window.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		newChatLabel.setBackground(new Color(0,0,0,110));
		newChatLabel.setForeground(Color.WHITE);
		newChatLabel.setOpaque(true);
		newChatLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		newChatLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		newChatLabel.setVerticalAlignment(SwingConstants.TOP);
		newChatLabel.setBounds(78, 11, 346, 154);
		contentPane.add(newChatLabel);

		senderPhotoLabel.setBounds(8, 11, 60, 60);
		contentPane.add(senderPhotoLabel);
	}
	private void displayNewChat()
	{
		newChatLabel.setText(newChats.get(0));
		senderPhotoLabel.setIcon(new ImageIcon(senderPhotos.get(0)));
	}
	private void removeSeenChat()
	{
		newChats.remove(0);
		senderPhotos.remove(0);	
	}
	private void sleep(int time)
	{
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

