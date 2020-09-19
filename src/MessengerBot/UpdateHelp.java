package MessengerBot;

import java.awt.Color;
import java.awt.Desktop;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class UpdateHelp extends JFrame {
	private static final long serialVersionUID = -4052029979870716607L;
	private JPanel contentPane;

	public UpdateHelp() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(UpdateHelp.class.getResource("/importedFiles/robot64p.png")));
		setTitle("MessengerBot - Update Help");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(466, 444);
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel firstStep = new JLabel("1- Please download latest version of chromedriver");
		firstStep.setBounds(10, 11, 285, 14);
		contentPane.add(firstStep);
		
		JButton downloadBtn = new JButton("download");
		downloadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://chromedriver.chromium.org/"));
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		downloadBtn.setBounds(315, 7, 105, 23);
		contentPane.add(downloadBtn);
		
		JLabel secondStep = new JLabel("3- Put chromedriver.exe in this folder");
		secondStep.setBounds(10, 67, 211, 14);
		contentPane.add(secondStep);
		
		JButton folderBtn = new JButton("open folder");
		folderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(new File(System.getProperty("user.home")+"\\AppData\\Local\\Google\\Chrome\\MessengerBot\\"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		folderBtn.setBounds(315, 63, 105, 23);
		contentPane.add(folderBtn);
		
		JLabel chromeUpdateLabel = new JLabel("");
		chromeUpdateLabel.setBounds(20, 120, 400, 261);
		chromeUpdateLabel.setIcon(new ImageIcon(UpdateHelp.class.getResource("/importedFiles/AboutChrome.png")));
		contentPane.add(chromeUpdateLabel);
		
		JLabel thirdStep = new JLabel("4- Update your Google Chrome");
		thirdStep.setBounds(10, 95, 197, 14);
		contentPane.add(thirdStep);
		
		JLabel lblNewLabel = new JLabel("2- Extract chromedriver.zip");
		lblNewLabel.setBounds(10, 39, 197, 14);
		contentPane.add(lblNewLabel);
	}
}
