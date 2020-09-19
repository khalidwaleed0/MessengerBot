package MessengerBot;

import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OptionsGui extends JDialog {
	private static final long serialVersionUID = -1098215928695680623L;
	private JPanel contentPane;
	private JTextField recordedKeyField;
	private JLabel startStopLabel = new JLabel("Start/Stop Record");
	private JButton btnListen = new JButton("listen");
	private MouseAdapter ma;
	private KeyListener kl;
	private String recordedKey;
	private boolean isRecording = false;
	public OptionsGui() {
		
		setTitle("Options");
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/importedFiles/robot64p.png")));
		setFocusable(true);
		setAlwaysOnTop(true);
		setSize(450, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
		UIManager.put("CheckBox.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		recordedKeyField = new JTextField();
		recordedKeyField.setEditable(false);
		recordedKeyField.setVisible(false);
		recordedKeyField.setBounds(125, 38, 121, 20);
		contentPane.add(recordedKeyField);
		recordedKeyField.setColumns(10);
		
		btnListen.setBounds(256, 37, 110, 23);
		btnListen.setVisible(false);
		contentPane.add(btnListen);
		
		startStopLabel.setBounds(6, 41, 109, 14);
		startStopLabel.setVisible(false);
		contentPane.add(startStopLabel);
		
		createMouseListener();
		createKeyboardListener();
		
		btnListen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!isRecording)
				{	recordedKeyField.setText("Listening..");
					requestFocus();
					addMouseListener(ma);
					addKeyListener(kl);
					isRecording = true;
					btnListen.setText("stop listening");
				}
				else
				{
					removeMouseListener(ma);
					removeKeyListener(kl);
					isRecording = false;
					btnListen.setText("Listen");
					if(recordedKeyField.getText().equals("Listening.."))
						recordedKeyField.setText("");
					AppSetup.Singleton().submitRecordedKey(recordedKey);
				}
			}
		});
		
	
		JCheckBox recordCheckBox = new JCheckBox("Enable Record Button (works even if you are in a game)");
		recordCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(recordCheckBox.isSelected())
					showRecordSettings(true);
				else
					showRecordSettings(false);
			}
		});
		if(AppSetup.Singleton().isRecordEnabled())
		{
			recordCheckBox.setSelected(true);
			showRecordSettings(true);
			recordedKey = AppSetup.Singleton().getRecordedKey();
			if(recordedKey.contains("Mouse"))
				recordedKeyField.setText(recordedKey);
			else
			{
				recordedKey = recordedKey.replaceAll("\\D", "");
				recordedKeyField.setText(KeyEvent.getKeyText(Integer.valueOf(recordedKey)));
			}
		}
		recordCheckBox.setBounds(6, 7, 347, 23);
		contentPane.add(recordCheckBox);
	}
	private void showRecordSettings(boolean state)
	{
		startStopLabel.setVisible(state);
		recordedKeyField.setVisible(state);
		btnListen.setVisible(state);
	}
	private void createMouseListener()
	{
		ma = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton() != 1)
				{
					recordedKeyField.setText("Mouse "+e.getButton());
					recordedKey = recordedKeyField.getText();
				}
			}
		};
	}
	private void createKeyboardListener()
	{
		kl = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			@Override
			public void keyPressed(KeyEvent e) {
				recordedKeyField.setText(KeyEvent.getKeyText(e.getKeyCode()));
				recordedKey = String.valueOf(e.getKeyCode());
			}
			@Override
			public void keyReleased(KeyEvent e) {}
		};
	}
}
