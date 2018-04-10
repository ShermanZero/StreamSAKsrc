package main.src;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import main.src.GUI.GUI;
import main.src.GUI.components.countersadjustersplugins.Adjuster;
import main.src.GUI.components.misc.CustomButton;
import main.src.misc.Handler;
import main.src.misc.StreamSAKFileHandler;

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
		String version = StreamSAKFileHandler.readFromURL("https://raw.githubusercontent.com/ShermanZero/StreamSAK/master/version.dat");
		
		if(!version.equals(VERSION)) {
			showUpdatePanel(version);
			return true;
		}
		
		return false;
	}
	
	private static void showUpdatePanel(String version) {
		String header = "A newer version of StreamSAK is available!";
		String updates = StreamSAKFileHandler.readFromURL("https://raw.githubusercontent.com/ShermanZero/StreamSAK/master/data/misc/recent_release.dat");
		
		JButton show = new CustomButton("Download Update ("+version+")", Adjuster.adjusterForegroundColor);
		show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				try {
					Desktop.getDesktop().browse(URI.create("https://github.com/ShermanZero/StreamSAK/raw/master/data/StreamSAK.jar"));
				} catch (IOException e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
				GUI.getNotificationWindow().dispose();
			}
		});
		
		JButton exit = new CustomButton("Skip Update", GUI.defaultRedColor);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { GUI.getNotificationWindow().dispose(); GUI.generate(); }
		});
		
		GUI.generateNotificationWindow(header, updates, new JButton[] {show, exit});
	}
	
}

