package MessengerBot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.SwingConstants;
import javax.swing.JWindow;

public class Overlay implements Runnable {

	public ArrayList<String> newChats = new ArrayList<String>();
	public ArrayList<String> senderPhotos = new ArrayList<String>();
	private boolean overLayFinished = false;
	
	@Override
	public void run() {
		while(true)
		{
			try {
				System.out.println("Overlay : "+newChats);
				System.out.println("Overlay Photos : "+senderPhotos);
				if((senderPhotos.size() != 0) && !Recorder.isRecording)
				{
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								overlaySetup(newChats.get(0),0);
								overLayFinished = true;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					while(!overLayFinished)
					{
						Thread.sleep(1000);
					}
					removeSeenChat();
					overLayFinished = false;
				}
				else
					sleep(1000);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void overlaySetup(String newChat, int sleepTime) throws Exception
	{
		JWindow window = new JWindow();
		JLabel newChatLabel = new JLabel("");
		JLabel senderPhotoLabel = new JLabel("");
		JPanel contentPane = new JPanel();
		
		window.setAlwaysOnTop(true);
		window.setBackground(new Color(0,0,0,0));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();
		window.setLocation((int) screenWidth-450, (int) screenHeight-300);
		window.setSize(450, 176);
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
		
		newChatLabel.setText("<html><div style='text-align: right;'>"+newChat+"</div></html>");
		if(!Recorder.isRecording)
			senderPhotoLabel.setIcon(new ImageIcon(senderPhotos.get(0)));
		window.setVisible(true);
		if(sleepTime == 0)
		{
			if(newChat.length() < 40)
				sleep(5000);
			else
				sleep(9000);
		}
		else
			sleep(sleepTime);
		window.dispose();
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

