package MessengerBot;

import java.awt.Color;
import java.awt.Desktop;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainGui extends JFrame {
    private static final long serialVersionUID = 5937481215987949886L;
    public static boolean finished = false;
    private final JPanel contentPane;

    public MainGui() {
        setTitle("MessengerBot");
        Border border = BorderFactory.createLineBorder(Color.decode("#acacac"));
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/importedFiles/robot64p.png")));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(462, 237);
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

        JTextArea generalReplyText = new JTextArea();
        generalReplyText.setLineWrap(true);
        generalReplyText.setText(AutoReplySettings.generalReply);
        generalReplyText.setBounds(10, 36, 426, 63);
        generalReplyText.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        contentPane.add(generalReplyText);

        JButton btnApply = new JButton("Apply Changes");
        btnApply.setVisible(false);
        btnApply.setBounds(151, 142, 120, 23);
        contentPane.add(btnApply);

        btnApply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AutoReplySettings.Singleton().submitBasicReplySettings(generalReplyText.getText());
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

        JButton btnStart = new JButton("start");
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                finished = true;
                dispose();
            }
        });
        btnStart.setBounds(10, 142, 89, 23);
        contentPane.add(btnStart);

        JButton btnOptions = new JButton("Options");
        btnOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OptionsGui options = new OptionsGui();
                options.setVisible(true);
            }
        });
        btnOptions.setBounds(316, 142, 120, 24);
        contentPane.add(btnOptions);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit", "Exit",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    setVisible(false);
                    dispose();
                    Scraper.Singleton().quit();
                    System.exit(0);
                }
            }
        });
    }
}
