package MessengerBot;

import java.awt.*;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;

public class LoginGui extends JFrame {
	private static final long serialVersionUID = -251690788837449454L;
	public static boolean finished = false;
	private final JPanel contentPane;
	private final JTextField emailField;
	private final JPasswordField passwordField;
	private final JLabel passwordLabel;
	private final JButton signButton;
	
	public LoginGui() {
		UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/importedFiles/robot64p.png")));
		setBounds(100, 100, 450, 282);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
		        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit ?"
						, "MessengerBot",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		        if(response == JOptionPane.YES_OPTION)
		        {
		        	Scraper.Singleton().quit();
		        	AppSetup.Singleton().deleteCookies();
					dispose();
		        	System.exit(0);
		        }
		    }
		});
		JLabel imgLabel = new JLabel();
		imgLabel.setBounds(182, 25, 64, 64);
		ImageIcon img = new ImageIcon(LoginGui.class.getResource("/importedFiles/robot64p.png"));
		imgLabel.setIcon(img);
		contentPane.add(imgLabel);
		
		emailField = new JTextField();
		emailField.addActionListener(e -> login());
		emailField.setBounds(125, 118, 188, 20);
		contentPane.add(emailField);
		emailField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.addActionListener(e -> login());
		passwordField.setBounds(125, 164, 188, 20);
		contentPane.add(passwordField);
		
		JLabel emailLabel = new JLabel("Facebook Email");
		emailLabel.setBounds(23, 121, 92, 14);
		contentPane.add(emailLabel);
		
		passwordLabel = new JLabel("password");
		passwordLabel.setBounds(23, 167, 69, 14);
		contentPane.add(passwordLabel);

		signButton = new JButton("sign in");
		signButton.setBounds(175, 209, 78, 23);
		contentPane.add(signButton);

		signButton.addActionListener(e -> login());
	}
	private void login()
	{
		String email = emailField.getText();
		char[] password = passwordField.getPassword();
		LoginWorker worker = new LoginWorker(email, new String(password));
		worker.execute();
		password = null;
	}
	private class LoginWorker extends SwingWorker<Void,Void>{
		String email,password;
		public LoginWorker(String email, String password)
		{
			this.email = email;
			this.password = password;
		}
		@Override
		protected Void doInBackground() throws Exception {
			signButton.setEnabled(false);
			signButton.setText("Signing in..");
			Scraper.Singleton().login(email, password);
			password = null;
			if(Scraper.Singleton().isLoggedInSuccessfully())
			{
				AppSetup.Singleton().submitLoginStatus(true);
				finished = true;
				dispose();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Wrong email or password", "MessengerBot", 
						JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/importedFiles/robot64p.png")));
			}
			signButton.setText("Sign in");
			signButton.setEnabled(true);
			return null;
		}
	}
}
