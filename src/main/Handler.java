package main;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import main.GUI.GUI;
import main.GUI.GUI_log;

public class Handler {
	
	public static ArrayList<File> files = new ArrayList<File>();
	public static String jarPath, counterFilesPath, adjusterFilePath, adjustmentFile;
	private static ActionThread at; 
	public static boolean stopThread = false;
	
	private static GUI gui;
	
	public static void init(GUI gui) throws Exception {
		Handler.gui = gui;
		
		String path = new File(StreamSAK.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getAbsolutePath();
		jarPath = path.substring(0, path.lastIndexOf("\\"));
		counterFilesPath = jarPath+"\\counters";
		adjusterFilePath = jarPath+"\\adjusters";
		
		File dir = new File(counterFilesPath);
		if(!dir.exists()) {
			dir.mkdirs();
			
			new File(counterFilesPath+"\\wins--sr.txt").createNewFile();
			new File(counterFilesPath+"\\losses--sr.txt").createNewFile();
			new File(counterFilesPath+"\\draws.txt").createNewFile();
		}
		
		dir = new File(adjusterFilePath);
		if(!dir.exists()) {
			dir.mkdirs();
			
			new File(adjusterFilePath+"\\sr.txt").createNewFile();
		}
		
		File logFile = new File(jarPath+"\\log.txt");
		if(!logFile.exists())
			logFile.createNewFile();
		
		files.add(logFile);
		
		File counterFolder = new File(counterFilesPath);
		File[] counterList = counterFolder.listFiles();
		
		for(File f : counterList)
			if(f.isFile())
				files.add(f);
		
		File adjusterFolder = new File(adjusterFilePath);
		File[] adjusterList = adjusterFolder.listFiles();
		
		for(File f : adjusterList)
			if(f.isFile())
				files.add(f);
	}
	
	public static void displayCounter(String fileName) {
		File file = findFile(fileName, "");
		
		String name = getFileName(file);
		String entry = "["+name+"]: ";
		
		int count = 0;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			String line = br.readLine();
			
			if(line == null)
				line = "0";
			
			count = Integer.parseInt(line);
			
			br.close();
			
			entry += count;
		} catch (IOException e1) { e1.printStackTrace(); }
		
		gui.getGUI_l().write(entry);
		
		System.out.println("Value: "+count+" of "+file.getAbsolutePath());
	}
	
	public static void incrementCounter(String fileName, boolean increase) {
		File file = findFile(fileName, "");
		
		String name = getFileName(file);
		String entry = "["+name+"]: ";
		
		int count = 0;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			String line = br.readLine();
			
			if(line == null)
				line = "0";
			
			count = Integer.parseInt(line);
			
			br.close();
			
			entry += count+" -> "+(increase ? ++count : --count);
			
			FileWriter fw = new FileWriter(file, false);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(""+count);
			bw.flush();
			bw.close();
		} catch (IOException e1) { e1.printStackTrace(); }
		
		gui.getGUI_l().write(entry);
		
		System.out.println("Succesfully wrote: "+count+" to "+file.getAbsolutePath());
	}

	public static void clearLog() { gui.getGUI_l().clear(); reset("log", "", false); }
	
	public static void resetCounters() { reset("counters", "0", true); }
	
	public static void resetAdjusters() { reset("adjusters", "", true); }
	
	public static void resetSpecific(String fileName, String directory) {
		fileName = fileName.toUpperCase();
		directory = directory.toUpperCase();
		
		for(int i = 0; i < files.size(); i++) {
			File f = files.get(i);
			String filePath = f.getPath().toUpperCase();
			if(filePath.contains(fileName) && (directory.equals("") || filePath.contains(directory)) ) {
				try {
					FileWriter fw = new FileWriter(f, false);
					BufferedWriter bw = new BufferedWriter(fw);
					
					bw.write(directory.equals("adjusters") ? "" : "0");
					bw.flush();
					bw.close();
				} catch (IOException e) { e.printStackTrace(); }
				
				String entry = "RESET ["+getFileName(f)+"]";
				gui.getGUI_l().write(entry);
			
				System.out.println("Successfully reset: "+f.getPath());
			}
		}
	}
	
	public static File findFile(String fileName, String directory) {	
		fileName = fileName.toUpperCase();
		directory = directory.toUpperCase();
		
		for(File f : files) {
			String filePath = f.getPath().toUpperCase();

			if(!directory.equals("") && !filePath.contains(directory))
				continue;
			
			if(getFileName(f).equals(fileName))
				return f;
		}
		
		return null;
	}
	
	public static String getFileName(File f) {
		String fileName = f.getName();
		return fileName.substring(0, fileName.lastIndexOf(".")).toUpperCase();
	}

	public static void doOnInput(Action a, String info) {
		allowInput(info);
		
		if(at != null)
			at.end();
		
		at = new ActionThread(a);
		at.start();
	}
	
	public static void allowInput(String info) {
		GUI_log.textInput.setBackground(Color.LIGHT_GRAY);
		GUI_log.textInput.setEditable(true);
		GUI_log.textInput.setFocusable(true);
		GUI_log.textInput.requestFocus();
		GUI_log.textInput.selectAll();
		
		GUI_log.textInfo.setText(info);
	}
	
	public static void disableInput() {
		GUI_log.textInput.setText("");
		GUI_log.textInput.setFocusable(false);
		GUI_log.textInput.setEditable(false);
		GUI_log.textInput.setBackground(Color.GRAY);
		
		GUI_log.textInfo.setText("");
	}
	
	private static void reset(String key, String newValue, boolean log) {
		for(int i = 0; i < files.size(); i++) {
			File f = files.get(i);
			if(f.getPath().contains(key)) {
				try {
					FileWriter fw = new FileWriter(f, false);
					BufferedWriter bw = new BufferedWriter(fw);
					
					bw.write(newValue);
					bw.flush();
					bw.close();
				} catch (IOException e) { e.printStackTrace(); }
				
				if(log) {
					String entry = "RESET ["+getFileName(f)+"]";
					gui.getGUI_l().write(entry);
				}
			
				System.out.println("Successfully reset: "+f.getPath());
			}
		}
	}
	
}
