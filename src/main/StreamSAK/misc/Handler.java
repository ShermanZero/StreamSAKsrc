package main.StreamSAK.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import main.StreamSAK.GUI.components.logandinput.Input;
import main.StreamSAK.misc.actions.Action;
import main.StreamSAK.misc.actions.ActionThread;

public class Handler {
	
	private static ActionThread at;
	private static Properties prop;
	
	public static void init() throws Exception {
		StreamSAKFileHandler.init();
		
		prop = new Properties();
		InputStream in = null;
		
		try {
			File propertiesFile = new File(StreamSAKFileHandler.propertiesFilePath);
			if(!propertiesFile.exists())
				propertiesFile.createNewFile();
			
			in = new FileInputStream(StreamSAKFileHandler.propertiesFilePath);
			
			prop.load(in);
		} catch (Exception e) { e.printStackTrace(); } finally {
			if(in != null)
				try { in.close(); } catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	public static boolean setLink(String object, String link) {
		if(object == null || link == null)
			return false;
		
		prop.setProperty(object, link);
		writeProperties();
		
		return true;
	}
	
	public static String getLink(String key) {
		return prop.getProperty(key);
	}
	
	public static void removeLink(String key) {
		prop.remove(key);
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
			out = new FileOutputStream(StreamSAKFileHandler.propertiesFilePath);
			prop.store(out, null);
		} catch (Exception e) { e.printStackTrace(); } finally { 
			if(out != null)
				try { out.close(); } catch (Exception e) { e.printStackTrace(); }
		}
	}
	
}
