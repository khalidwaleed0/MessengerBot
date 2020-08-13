package MessengerBot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OptionsGui extends JFrame {

	private JPanel contentPane;
	private JTextField recordedKeyField;
	public OptionsGui() {
		
		setTitle("Options");
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/importedFiles/robot64p.png")));
		setAlwaysOnTop(true);
		setSize(450, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JCheckBox recordCheckBox = new JCheckBox("Enable Record Button (works even if you are in a game)");
		recordCheckBox.setBounds(6, 7, 347, 23);
		contentPane.add(recordCheckBox);
		
		recordedKeyField = new JTextField();
		recordedKeyField.setEditable(false);
		recordedKeyField.setBounds(10, 37, 121, 20);
		contentPane.add(recordedKeyField);
		recordedKeyField.setColumns(10);
		
		JButton btnListen = new JButton("listen");
		btnListen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recordedKeyField.setText("Listening..");
				addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if(e.getButton() != 1)
						{
							recordedKeyField.setText("Mouse "+e.getButton());
						}
					}
				});
			}
		});
		btnListen.setBounds(134, 36, 76, 23);
		contentPane.add(btnListen);
	}
}
