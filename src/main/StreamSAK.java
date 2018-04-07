package main;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import javax.swing.JOptionPane;

import main.GUI.GUI;
import main.GUI.GUI_log;
import main.GUI.GUI_options;
import main.GUI.GUI_options_left;

public class StreamSAK {
	
	public static final String VERSION = "v2.2";
	
	public static void main(String [] args) {
		checkForNewVersion();
		
		GUI gui = new GUI();
		
		try { Handler.init(gui); } catch (Exception e) { e.printStackTrace(); }
		
		GUI_log logger = new GUI_log();
		GUI_options options = new GUI_options();
		GUI_options_left options_left = new GUI_options_left();
		gui.generate(options_left, options, logger);
	}
	
	public static void restart() {
		main(null);
	}
	
	private static void checkForNewVersion() {
		URL url = null;
		try {
			url = new URL("https://raw.githubusercontent.com/ShermanZero/StreamSAK/master/version.dat");
		} catch (MalformedURLException e) { e.printStackTrace(); }
		
		String v = "";
		Scanner s = null;
		try {
			s = new Scanner(url.openStream());
			v = s.nextLine().trim().toLowerCase();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			s.close();
		}
		
		if(!v.equals(VERSION)) {
			Object[] options = {"Take me there", "Cool, thanks"};
			int n = JOptionPane.showOptionDialog(null, "A newer version of StreamSAK is available.", "StreamSAK Update", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
		
			if(n == JOptionPane.YES_OPTION)
				try {
					Desktop.getDesktop().browse(URI.create("https://github.com/ShermanZero/StreamSAK"));
				} catch (IOException e1) { e1.printStackTrace(); }
		}
	}
	
}
