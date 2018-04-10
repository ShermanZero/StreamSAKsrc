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
	
	private final String StreamSAK_CURRENT_VERSION = "v4.1.1";
	private final String StreamSAK_PLUGIN_LIBRARY_BUILD = "0.1.1";
	
	public void start() {
		try { Handler.init(); } catch (Exception e) { e.printStackTrace(); }
		
		checkForNewVersion();
		
		GUI.generate(StreamSAK_CURRENT_VERSION, StreamSAK_PLUGIN_LIBRARY_BUILD);
	}
	
	public String getPluginLibraryBuild() { return StreamSAK_PLUGIN_LIBRARY_BUILD; }
	
	public String getCurrentVersion() { return StreamSAK_CURRENT_VERSION; }

	private boolean checkForNewVersion() {
		String version = StreamSAKFileHandler.readFromURL("https://raw.githubusercontent.com/ShermanZero/StreamSAK/master/version.dat");
		
		if(!version.equals(StreamSAK_CURRENT_VERSION)) {
			showUpdatePanel(version);
			return true;
		}
		
		return false;
	}
	
	private void showUpdatePanel(String version) {
		JFrame window = new JFrame();
		
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
			public void actionPerformed(ActionEvent arg0) { window.dispose(); }
		});
		
		GUI.generateNotification(window, header, updates, new JButton[] {show, exit});
	}
	
	public static void main(String [] args) {
		StreamSAK client = new StreamSAK();
		client.start();
	}
	
}

