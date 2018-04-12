package main.StreamSAK;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.JarFile;

import javax.swing.JButton;
import javax.swing.JFrame;

import main.StreamSAK.GUI.GUI;
import main.StreamSAK.GUI.components.countersadjustersplugins.Adjuster;
import main.StreamSAK.GUI.components.misc.CustomButton;
import main.StreamSAK.misc.Handler;
import main.StreamSAK.misc.StreamSAKFileHandler;

public class StreamSAK {
	
	private static String StreamSAK_CURRENT_VERSION = "v4.3.1";
	private static String StreamSAK_PLUGIN_LIBRARY_BUILD;
	
	public void start() {
		loadLibraryBuild();
		
		try { Handler.init(); } catch (Exception e) { e.printStackTrace(); }
		
		checkForNewVersion(false);
		
		GUI.generate();
	}
	
	public static String getPluginLibraryBuild() { return StreamSAK_PLUGIN_LIBRARY_BUILD; }
	
	public static String getCurrentVersion() { return StreamSAK_CURRENT_VERSION; }

	public static boolean checkForNewVersion(boolean userPrompted) {
		String version = StreamSAKFileHandler.readFromURL("https://raw.githubusercontent.com/ShermanZero/StreamSAK/master/version.dat");
		
		if(!version.equals(StreamSAK_CURRENT_VERSION)) {
			showUpdatePanel(version);
			return true;
		} else if (userPrompted) {
			showNoUpdatePanel(version);
		}
		
		return false;
	}
	
	private static void loadLibraryBuild() {
		try {
			JarFile jarFile = new JarFile("StreamSAKPluginLibrary.jar");
			
			jarFile.stream().forEach(jarEntry -> {
				if(jarEntry.getName().endsWith(".dat")) {
					try {
						InputStream is = jarFile.getInputStream(jarEntry);
						BufferedReader br = new BufferedReader(new InputStreamReader(is));
						
						StreamSAK_PLUGIN_LIBRARY_BUILD = br.readLine();
						
						System.out.println("StreamSAK running plug-in library build ["+StreamSAK_PLUGIN_LIBRARY_BUILD+"]");
						
						br.close();
					} catch (IOException e) { e.printStackTrace(); }
				}
			});
			
			jarFile.close();
		} catch (Exception e1) { e1.printStackTrace(); }
	}
	
	private static void showUpdatePanel(String version) {
		JFrame window = new JFrame();
		
		String header = "A newer version of StreamSAK is available!";
		String updates = StreamSAKFileHandler.readFromURL("https://raw.githubusercontent.com/ShermanZero/StreamSAK/master/data/misc/recent_release.dat");
		
		JButton show = new CustomButton("Download Update ("+version+")", Adjuster.adjusterForegroundColor);
		show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				StreamSAKFileHandler.openURL("https://github.com/ShermanZero/StreamSAK/raw/master/data/StreamSAK.jar");
				window.dispose();
			}
		});
		
		JButton exit = new CustomButton("Skip Update", GUI.defaultRedColor);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { window.dispose(); }
		});
		
		GUI.generateNotification(window, header, updates, new JButton[] {show, exit});
	}
	
	private static void showNoUpdatePanel(String version) {
		JFrame window = new JFrame();
		
		String header = "You have the current version of StreamSAK ("+StreamSAK_CURRENT_VERSION+")";
		String updates = "Thanks for supporting me, you're awesome!  Don't worry, I'll continue to push out "+
						"new and awesome updates for as long as they're needed.";
		
		JButton show = new CustomButton("Close", Adjuster.adjusterForegroundColor);
		show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { window.dispose(); }
		});
		
		GUI.generateNotification(window, header, updates, new JButton[] {show});
	}

	public static void main(String [] args) {
		StreamSAK client = new StreamSAK();
		client.start();
	}
	
}