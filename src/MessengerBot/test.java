package MessengerBot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JWindow;

import java.awt.Canvas;
import javax.swing.JScrollPane;

public class test extends JWindow {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					test frame = new test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
		});
		try {
			Thread.sleep(9600);
			System.exit(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Create the frame.
	 */
	public test() {
		setAlwaysOnTop(true);
//		File senderPhoto = new File("C:\\Users\\lodaw\\Desktop\\basel.jpg");
		setBackground(new Color(0,0,0,0));
		ImageIcon img = new ImageIcon("C:\\Users\\lodaw\\Desktop\\basel.jpg");
		setSize(450, 176);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();
		setLocation((int) screenWidth-450, (int) screenHeight-300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0,0,0,0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		String message = "ده مثال على كلام عربي مكتوب وبيظهرلك وانت بتلعب وأنا قاعد أكتب أي بتاع في البتنجان عشان أملى سطور وأشوفه هيظهر ازاي وخلاص وشكرا";
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBackground(new Color(0,0,0,110));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setOpaque(true);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setText("<html><div style='text-align: right;'>"+message+"</div></html>");
		lblNewLabel.setBounds(78, 11, 346, 154);
		lblNewLabel.setAutoscrolls(true);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(img);
		lblNewLabel_1.setBounds(8, 11, 60, 60);
		contentPane.add(lblNewLabel_1);
		
	}
}
