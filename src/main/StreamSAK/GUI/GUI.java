package main.StreamSAK.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import main.StreamSAK.GUI.components.Options;
import main.StreamSAK.GUI.components.countersadjustersplugins.Adjuster;
import main.StreamSAK.GUI.components.countersadjustersplugins.CountersAdjustersPlugins;
import main.StreamSAK.GUI.components.logandinput.LogAndInput;

public class GUI {
	
	public static Font defaultFont = new Font("Tahoma", Font.PLAIN, 12);
	public static Color defaultRedColor = new Color(255, 75, 75);
	public static Color defaultColor = Color.LIGHT_GRAY;
	
	private static JFrame window;

	private static int WIDTH = 650, HEIGHT = 300;
	
	public static void generate(String currentVersion, String libraryBuild) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				window = new JFrame("StreamSAK "+currentVersion+"   |   "+libraryBuild);
				
				Dimension d = new Dimension(WIDTH, HEIGHT);
				window.setMinimumSize(new Dimension(WIDTH, 275));
				window.setPreferredSize(d);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				JPanel main = new JPanel(new BorderLayout());
				main.add(new Options(), BorderLayout.WEST);
				main.add(new LogAndInput(), BorderLayout.CENTER);
				main.add(new CountersAdjustersPlugins(), BorderLayout.EAST);
				
				window.add(main);
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
		
		window.add(main);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
	private static void resetWindowSize() {
		window.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		window.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		
		window.revalidate();
		window.pack();
	}

}
