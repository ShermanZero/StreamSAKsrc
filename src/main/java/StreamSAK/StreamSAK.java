package main.java.StreamSAK;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import main.java.StreamSAK.GUI.GUI;
import main.java.StreamSAK.GUI.components.countersadjustersplugins.Adjuster;
import main.java.StreamSAK.GUI.components.countersadjustersplugins.Plugin;
import main.java.StreamSAK.GUI.components.misc.CustomButton;
import main.java.StreamSAK.misc.KeyBinds;
import main.java.StreamSAK.misc.StreamSAKFileHandler;
import main.java.StreamSAK.misc.StreamSAKHandler;

public class StreamSAK {
	
	private static String StreamSAK_CURRENT_VERSION = "v4.6.0";
	private static String StreamSAK_PLUGIN_LIBRARY_BUILD;
	
	public void start() {
		loadLibraryBuild();
		
		try {
			StreamSAKFileHandler.init();
			StreamSAKHandler.init(); 
		} catch (Exception e) { e.printStackTrace(); }
		
		checkForNewVersion(false);
		
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(e.getMessage());
			
			System.exit(1);
		}
		
		GlobalScreen.addNativeKeyListener(new KeyBinds());
		
		GUI.generate();
	}
	
	public static String getPluginLibraryBuild() { return StreamSAK_PLUGIN_LIBRARY_BUILD; }
	
	public static String getCurrentVersion() { return StreamSAK_CURRENT_VERSION; }

	public static void downloadLibraryBuild() {
		showLibraryBuildDownloadPanel(StreamSAK_PLUGIN_LIBRARY_BUILD);
	}
	
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
			InputStream in = StreamSAK.class.getResourceAsStream("/build.dat");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			StreamSAK_PLUGIN_LIBRARY_BUILD = br.readLine();
			br.close();
		} catch (Exception e1) { e1.printStackTrace(); }
	}
	
	private static void showLibraryBuildDownloadPanel(String build) {
		JFrame window = new JFrame();
		
		String header = "Download the current StreamSAKPluginLibrary build? ("+build+")";
		String updates = "You only need to do this if you're a plug-in developer and you need to update your plug-in.  "+
				"StreamSAK always has the latest version of the plug-in library installed.";
		
		JButton show = new CustomButton("Download Build ("+build+")", Plugin.pluginForegroundColor);
		show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				StreamSAKFileHandler.openURL("https://github.com/ShermanZero/StreamSAK/raw/master/data/plugins/src/StreamSAKPluginLibrary.jar");
				window.dispose();
			}
		});
		
		JButton exit = new CustomButton("Close", GUI.defaultRedColor);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { window.dispose(); }
		});
		
		GUI.generateNotification(window, header, updates, new JButton[] {show, exit});
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
		String thanks = "Thanks for supporting me, you're awesome!  Don't worry, I'll continue to push out "+
						"new and awesome updates for as long as they're needed.";
		String updates = StreamSAK_CURRENT_VERSION+": \n"+StreamSAKFileHandler.readFromURL("https://raw.githubusercontent.com/ShermanZero/StreamSAK/master/data/misc/recent_release.dat");
		
		JButton show = new CustomButton("Close", Adjuster.adjusterForegroundColor);
		show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { window.dispose(); }
		});
		
		GUI.generateNotification(window, header, thanks+"\n\n"+updates, new JButton[] {show});
	}

	public static void main(String [] args) {
		StreamSAK client = new StreamSAK();
		client.start();
	}
	
}