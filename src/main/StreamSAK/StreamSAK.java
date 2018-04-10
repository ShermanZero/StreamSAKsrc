package main.StreamSAK;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.StreamSAK.GUI.GUI;
import main.StreamSAK.GUI.components.countersadjustersplugins.Adjuster;
import main.StreamSAK.GUI.components.misc.CustomButton;
import main.StreamSAK.misc.Handler;
import main.StreamSAK.misc.StreamSAKFileHandler;

public class StreamSAK {
	
	public static final String STREAMSAK_VERSION = "v4.1.1";
	public static final String PLUGIN_LIBRARY_BUILD = "0.1.2";
	
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
		
		if(!version.equals(STREAMSAK_VERSION)) {
			showUpdatePanel(version);
			return true;
		}
		
		return false;
	}
	
	private static void showUpdatePanel(String version) {
		JFrame window = GUI.createNotificationWindow();
		
		String header = "A newer version of StreamSAK is available!";
		String updates = StreamSAKFileHandler.readFromURL("https://raw.githubusercontent.com/ShermanZero/StreamSAK/master/data/misc/recent_release.dat");
		
		
		JButton show = new CustomButton("Download Update ("+version+")", Adjuster.adjusterForegroundColor);
		show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				try {
					Desktop.getDesktop().browse(URI.create("https://github.com/ShermanZero/StreamSAK/raw/master/data/StreamSAK.jar"));
				} catch (IOException e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
				window.dispose();
			}
		});
		
		JButton exit = new CustomButton("Skip Update", GUI.defaultRedColor);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { window.dispose(); GUI.generate(); }
		});
		
		
		GUI.generateNotificationWindow(window, header, updates, new JButton[] {show, exit});
	}
	
}

