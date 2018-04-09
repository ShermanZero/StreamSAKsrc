package main.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import main.GUI.components.logandinput.Input;
import main.actions.Action;
import main.actions.ActionThread;

public class Handler {
	
	private static ActionThread at;
	private static Properties prop;

	public static void init() throws Exception {
		prop = new Properties();
		InputStream in = null;
		
		try {
			File propertiesFile = new File(FileHandler.propertiesFilePath);
			if(!propertiesFile.exists())
				propertiesFile.createNewFile();
			
			in = new FileInputStream(FileHandler.propertiesFilePath);
			
			prop.load(in);
		} catch (Exception e) { e.printStackTrace(); } finally {
			if(in != null)
				try { in.close(); } catch (Exception e) { e.printStackTrace(); }
		}
		
		FileHandler.init();
	}
	
	public static boolean setLink(File counterFile, File linkFile) {
		if(counterFile == null || linkFile == null)
			return false;
		
		prop.setProperty(FileHandler.getFileFormattedName(counterFile), FileHandler.getFileFormattedName(linkFile));
		writeProperties();
		
		return true;
	}
	
	public static boolean setLink(File counterFile, String pluginName) {
		if(counterFile == null || pluginName == null)
			return false;
		
		prop.setProperty(FileHandler.getFileFormattedName(counterFile), pluginName);
		writeProperties();
		
		return true;
	}
	
	public static String getLink(File counterFile) {
		return prop.getProperty(FileHandler.getFileFormattedName(counterFile));
	}
	
	public static void removeLink(File counterFile) {
		prop.remove(FileHandler.getFileFormattedName(counterFile));
		writeProperties();
	}
	
	public static void doOnInput(Action a, String info) {
		Input.allowInput(info);
		
		if(at != null)
			at.end();
		
		at = new ActionThread(a);
		at.start();
	}
	
	private static void writeProperties() {
		OutputStream out = null;
		
		try {
			out = new FileOutputStream(FileHandler.propertiesFilePath);
			prop.store(out, null);
		} catch (Exception e) { e.printStackTrace(); } finally { 
			if(out != null)
				try { out.close(); } catch (Exception e) { e.printStackTrace(); }
		}
	}
	
}
