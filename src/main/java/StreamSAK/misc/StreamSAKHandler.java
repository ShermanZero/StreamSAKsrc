package main.java.StreamSAK.misc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import main.java.StreamSAK.GUI.components.logandinput.Input;
import main.java.StreamSAK.misc.actions.StreamSAKAction;
import main.java.StreamSAK.misc.actions.StreamSAKActionThread;

public class StreamSAKHandler {
	
	private static StreamSAKActionThread at;
	private static Properties prop = new Properties();
	
	public static void init() throws Exception {
		loadProperties();
	}
	
	public static boolean setLink(String key, String link) {
		if(key == null || link == null)
			return false;
		
		prop.setProperty(key, link);
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
	
	public static void doOnInput(StreamSAKAction a, String info) {
		Input.allowInput(info);
		
		if(at != null)
			at.end();
		
		at = new StreamSAKActionThread(a);
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
	
	private static void loadProperties() {
		InputStream in = null;
		
		try {
			in = new FileInputStream(StreamSAKFileHandler.propertiesFilePath);
			prop.load(in);
		} catch (Exception e) { e.printStackTrace(); } finally {
			if(in != null)
				try { in.close(); } catch (Exception e) { e.printStackTrace(); }
		}
	}
	
}
