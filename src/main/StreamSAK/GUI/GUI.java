package main.StreamSAK.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import main.StreamSAK.StreamSAK;
import main.StreamSAK.GUI.components.Options;
import main.StreamSAK.GUI.components.countersadjustersplugins.Adjuster;
import main.StreamSAK.GUI.components.countersadjustersplugins.CountersAdjustersPlugins;
import main.StreamSAK.GUI.components.logandinput.LogAndInput;
import main.StreamSAK.GUI.components.misc.CustomButton;
import main.StreamSAK.GUI.components.misc.CustomLabel;
import main.StreamSAK.misc.StreamSAKFileHandler;

public class GUI {
	
	public static Font defaultFont = new Font("Tahoma", Font.PLAIN, 12);
	public static Color defaultRedColor = new Color(255, 75, 75);
	public static Color defaultColor = Color.LIGHT_GRAY;
	
	private static JFrame window;

	private static int WIDTH = 650, HEIGHT = 300;
	private static String currentVersion, libraryBuild;
	
	
	public static void generate(String currentVersion, String libraryBuild) {
		GUI.currentVersion = currentVersion;
		GUI.libraryBuild = libraryBuild;
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				window = new JFrame();
				
				Dimension d = new Dimension(WIDTH, HEIGHT);
				window.setMinimumSize(new Dimension(WIDTH, 275));
				window.setPreferredSize(d);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setUndecorated(true);
				
				JPanel main = new JPanel(new BorderLayout());
				main.add(new Options(), BorderLayout.WEST);
				main.add(new LogAndInput(), BorderLayout.CENTER);
				main.add(new CountersAdjustersPlugins(), BorderLayout.EAST);
				
				window.setJMenuBar(generateMenuBar());
				
				FrameDragListener fdl = new FrameDragListener(window);
				window.addMouseListener(fdl);
				window.addMouseMotionListener(fdl);
				
				window.add(main);
				window.setContentPane(main);
				
				window.pack();
				window.setLocationRelativeTo(null);
				window.setVisible(true);
				window.requestFocus();
			}
		});
	}
	
	public static int getHeight() {
		return HEIGHT;
	}
	
	public static int getWidth() {
		return WIDTH;
	}
	
	public static void setHeight(int height) {
		if(height < 275)
			height = 275;
		
		HEIGHT = height;
		resetWindowSize();
	}
	
	public static void setWidth(int width) {
		if(width < 500)
			width = 500;
		
		WIDTH = width;
		resetWindowSize();
	}
	
	public static void generateNotification(JFrame window, String header, String message, JButton[] buttons) {
		window.setPreferredSize(new Dimension(400, 200));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setAlwaysOnTop(true);
		window.setUndecorated(true);
		
		JPanel main = new JPanel(new BorderLayout());
		main.setBackground(Color.DARK_GRAY);
		main.setBorder(new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		
		JLabel headerLabel = new JLabel(header);
		headerLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerLabel.setBackground(Color.DARK_GRAY);
		headerLabel.setFont(GUI.defaultFont);
		headerLabel.setForeground(Adjuster.adjusterForegroundColor);
		main.add(headerLabel, BorderLayout.NORTH);
		
		JTextArea messageTextArea = new JTextArea(message);
		messageTextArea.setBackground(Color.DARK_GRAY);
		messageTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));
		messageTextArea.setLineWrap(true);
		messageTextArea.setWrapStyleWord(true);
		messageTextArea.setForeground(GUI.defaultColor);
		messageTextArea.setEditable(false);
		messageTextArea.setFocusable(false);
		messageTextArea.setFont(GUI.defaultFont);
		
		JScrollPane scrollPane = new JScrollPane(messageTextArea);
		scrollPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		scrollPane.setBackground(Color.DARK_GRAY);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		main.add(scrollPane, BorderLayout.CENTER);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
		
		for(int i = 0; i < buttons.length-1; i++) {
			JButton b = buttons[i];
			b.setBorder(new MatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
		}
		
		for(JButton b : buttons)
			buttonPanel.add(b, gbc);
		
		main.add(buttonPanel, BorderLayout.SOUTH);
		
		FrameDragListener fdl = new FrameDragListener(window);
		window.addMouseListener(fdl);
		window.addMouseMotionListener(fdl);
		
		window.add(main);
		window.setContentPane(main);
		
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
	private static JMenuBar generateMenuBar() {
		JMenuBar mb = new JMenuBar();
		mb.setBackground(Color.DARK_GRAY);
		
		JLabel label = new CustomLabel("StreamSAK");
		
		JButton version = new CustomButton(currentVersion);
		version.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { StreamSAK.checkForNewVersion(true); }
		});
		
		JButton build = new CustomButton(libraryBuild);
		build.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { StreamSAKFileHandler.openURL("https://github.com/ShermanZero/StreamSAK/raw/master/data/plugins/src/StreamSAKPluginLibrary.jar"); }
		});
		
		JButton dev = new CustomButton("Support the Developer", Adjuster.adjusterForegroundColor);
		dev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { StreamSAKFileHandler.openURL("https://www.twitch.tv/shermanzero"); }
		});
		
		JButton exit = new CustomButton("Exit StreamSAK", GUI.defaultRedColor);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { System.exit(0); }
		});
		
		mb.add(label);
		mb.add(version);
		mb.add(build);
		mb.add(Box.createHorizontalGlue());
		mb.add(dev);
		mb.add(exit);
		
		return mb;
	}
	
	private static void resetWindowSize() {
		window.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		window.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		
		window.revalidate();
		window.pack();
	}

	public static class FrameDragListener extends MouseAdapter {

        private final JFrame frame;
        private Point mouseDownCompCoords = null;

        public FrameDragListener(JFrame frame) {
            this.frame = frame;
        }

        public void mouseReleased(MouseEvent e) {
            mouseDownCompCoords = null;
        }

        public void mousePressed(MouseEvent e) {
            mouseDownCompCoords = e.getPoint();
        }

        public void mouseDragged(MouseEvent e) {
            Point currCoords = e.getLocationOnScreen();
            frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
        }
    }
	
}
