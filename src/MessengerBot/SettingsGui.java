package MessengerBot;

import java.awt.Color;
import java.awt.Desktop;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JEditorPane;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.SwingConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SettingsGui extends JFrame {

	public static boolean finished = false;
	private JPanel contentPane;

	public SettingsGui() {
		setTitle("MessengerBot");
		Border border = BorderFactory.createLineBorder(Color.decode("#acacac"));
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/importedFiles/robot64p.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 462, 305);
		setLocationRelativeTo(null);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu aboutMenu = new JMenu("About");
		aboutMenu.setHorizontalAlignment(SwingConstants.RIGHT);
		menuBar.add(aboutMenu);
		
		JMenuItem githugbMenuItem = new JMenuItem("Github Project");
		githugbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/khalidwaleed0/MessengerBot"));
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		aboutMenu.add(githugbMenuItem);
		
		JMenuItem contactMenuItem = new JMenuItem("Contact me");
		contactMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Feel free to contact me\nEmail : khalidwaleed0@outlook.com");
			}
		});
		contactMenuItem.setHorizontalAlignment(SwingConstants.LEFT);
		aboutMenu.add(contactMenuItem);
		
		JMenu uninstallMenu = new JMenu("Uninstall");
		menuBar.add(uninstallMenu);
		
		JMenuItem uninstallMenuItem = new JMenuItem("Uninstall");
		uninstallMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AppSetup.Singleton().uninstall();
				dispose();
				System.exit(0);
			}
		});
		uninstallMenu.add(uninstallMenuItem);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("automatic reply for any message");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBounds(10, 11, 268, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("automatic reply for important messages");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1.setBounds(10, 98, 268, 25);
		contentPane.add(lblNewLabel_1);
		
		JEditorPane generalReplyText = new JEditorPane();
		generalReplyText.setText(AutoReplySettings.generalReply);
		generalReplyText.setBounds(10, 36, 365, 51);
		generalReplyText.setBorder(BorderFactory.createCompoundBorder(border,BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		contentPane.add(generalReplyText);
		
		JEditorPane importantReplyText = new JEditorPane();
		importantReplyText.setText(AutoReplySettings.importantReply);
		importantReplyText.setBounds(10, 134, 365, 51);
		importantReplyText.setBorder(BorderFactory.createCompoundBorder(border,BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		contentPane.add(importantReplyText);
		
		JButton btnApply = new JButton("Apply Changes");
		btnApply.setVisible(false);
		btnApply.setBounds(316, 98, 120, 23);
		contentPane.add(btnApply);
		
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AutoReplySettings.Singleton().submitBasicReplySettings(generalReplyText.getText(), importantReplyText.getText());
				JOptionPane.showMessageDialog(null, "Applied Changes Successfully", "MessengerBot", JOptionPane.INFORMATION_MESSAGE);
				btnApply.setVisible(false);
			}
		});
		
		generalReplyText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				btnApply.setVisible(true);
			}
		});
		
		importantReplyText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				btnApply.setVisible(true);
			}
		});
		
		JButton btnStart = new JButton("start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finished = true;
				dispose();
			}
		});
		btnStart.setBounds(10, 207, 89, 23);
		contentPane.add(btnStart);
		
		JButton btnAutoReply = new JButton("Auto Reply for specific words");
		btnAutoReply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnAutoReply.setBounds(223, 206, 201, 25);
		contentPane.add(btnAutoReply);
	}
}
