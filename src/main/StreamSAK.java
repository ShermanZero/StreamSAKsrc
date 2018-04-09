package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import main.GUI.GUI;
import main.GUI.components.countersadjustersplugins.Adjuster;
import main.GUI.components.misc.CustomButton;
import main.misc.Handler;

public class StreamSAK {
	
	public static final String VERSION = "v4.1.1";
	
	public static void main(String [] args) {
		try { Handler.init(); } catch (Exception e) { e.printStackTrace(); }
		
		if(!checkForNewVersion())
			GUI.generate();
	}
	
	public static void restart() {
		main(null);
	}
	
	private static boolean checkForNewVersion() {
		String version = readFromURL("https://raw.githubusercontent.com/ShermanZero/StreamSAK/master/version.dat");
		
		if(!version.equals(VERSION)) {
			showUpdatePanel(version);
			return true;
		}
		
		return false;
	}
	
	private static void showUpdatePanel(String version) {
		String updates = readFromURL("https://raw.githubusercontent.com/ShermanZero/StreamSAK/master/data/misc/recent_release.dat");
		
		JFrame updateWindow = new JFrame("StreamSAK Updater");
		updateWindow.setPreferredSize(new Dimension(400, 200));
		updateWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		updateWindow.setAlwaysOnTop(true);
		updateWindow.setUndecorated(true);
		
		JPanel main = new JPanel(new BorderLayout());
		main.setBackground(Color.DARK_GRAY);
		main.setBorder(new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		
		JLabel label = new JLabel("A newer version of StreamSAK is available!");
		label.setBorder(new EmptyBorder(10, 10, 10, 10));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBackground(Color.DARK_GRAY);
		label.setFont(GUI.defaultFont);
		label.setForeground(Adjuster.adjusterForegroundColor);
		main.add(label, BorderLayout.NORTH);
		
		JTextArea textArea = new JTextArea(updates);
		textArea.setBackground(Color.DARK_GRAY);
		textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setForeground(GUI.defaultColor);
		textArea.setEditable(false);
		textArea.setFocusable(false);
		textArea.setFont(GUI.defaultFont);
		
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setBorder(new EmptyBorder(5, 5, 5, 5));
		scroll.setBackground(Color.DARK_GRAY);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		main.add(scroll, BorderLayout.CENTER);
		
		JPanel buttons = new JPanel(new GridLayout());
		buttons.setBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
		
		JButton show = new CustomButton("Download Update ("+version+")", Adjuster.adjusterForegroundColor);
		show.setBorder(new MatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
		show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				try {
					Desktop.getDesktop().browse(URI.create("https://github.com/ShermanZero/StreamSAK/raw/master/data/StreamSAK.jar"));
				} catch (IOException e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
				updateWindow.dispose();
			}
		});
		buttons.add(show);
		
		JButton exit = new CustomButton("Skip Update", GUI.defaultRedColor);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { updateWindow.dispose(); GUI.generate(); }
		});
		buttons.add(exit);
		
		main.add(buttons, BorderLayout.SOUTH);
		
		updateWindow.add(main);
		updateWindow.pack();
		updateWindow.setLocationRelativeTo(null);
		updateWindow.setVisible(true);
	}
	
	private static String readFromURL(String urlPath) {
		String data = "", line;
		
		URL url = null;
		try {
			url = new URL(urlPath);
		} catch (MalformedURLException e) { e.printStackTrace(); }
		
		Scanner s = null;
		try {
			s = new Scanner(url.openStream());
			
			while(s.hasNextLine()) {
				line = s.nextLine();
				data += line+"\n";
			}
			
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			s.close();
		}
		
		return data.trim();
	}
	
}

