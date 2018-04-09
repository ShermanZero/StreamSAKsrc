package main.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;

import main.StreamSAK;

public class FileHandler {
	
	public static String jarPath, countersDirectoryPath, adjustersDirectoryPath, propertiesFilePath;
	
	private static ArrayList<File> files = new ArrayList<File>();
	
	public enum Directory {
		COUNTERS("counters"), ADJUSTERS("adjusters"), MAIN("");
		
		private String value;
		private Directory(String str) {
			value = str;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	static {
		String path = null;
		try {
			path = new File(StreamSAK.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getAbsolutePath();
		} catch (URISyntaxException e) { e.printStackTrace(); }
		jarPath = path.substring(0, path.lastIndexOf(File.separator));
		
		propertiesFilePath = jarPath+File.separator+"links.properties";
		countersDirectoryPath = jarPath+File.separator+Directory.COUNTERS.getValue();
		adjustersDirectoryPath = jarPath+File.separator+Directory.ADJUSTERS.getValue();
	}
	
	public static void init() throws Exception {
		//create the log file
		File logFile = new File(jarPath+File.separator+"log.txt");
		if(!logFile.exists())
			logFile.createNewFile();
		files.add(logFile);

		File dir = null, sr = null, wins, losses, draws;
		
		//check to see if the adjuster directory exists
		dir = new File(adjustersDirectoryPath);
		if(!dir.exists()) {
			dir.mkdirs();
			
			sr = new File(adjustersDirectoryPath+File.separator+"sr.txt");
			sr.createNewFile();
		}
		
		//check to see if the counters directory exists
		dir = new File(countersDirectoryPath);
		if(!dir.exists()) {
			dir.mkdirs();
			
			wins = new File(countersDirectoryPath+File.separator+"wins.txt");
			wins.createNewFile();
			Handler.setAdjusterLink(wins, sr);
			writeToFile(wins, "0");
			
			losses = new File(countersDirectoryPath+File.separator+"losses.txt");
			losses.createNewFile();
			Handler.setAdjusterLink(losses, sr);
			writeToFile(losses, "0");
			
			draws = new File(countersDirectoryPath+File.separator+"draws.txt");
			draws.createNewFile();
			writeToFile(draws, "0");
		}
		
		//get all the files within the counter folder
		File counterFolder = new File(countersDirectoryPath);
		File[] counterList = counterFolder.listFiles();
		for(File f : counterList)
			if(f.isFile())
				addFile(f);
		
		//get all the files within the adjuster folder
		File adjusterFolder = new File(adjustersDirectoryPath);
		File[] adjusterList = adjusterFolder.listFiles();
		for(File f : adjusterList)
			if(f.isFile())
				addFile(f);
	}
	
	public static File findFile(String fileName) {	
		return findFile(fileName, Directory.MAIN);
	}
	
	public static File findFile(String fileName, Directory d) {	
		fileName = fileName.toLowerCase();
		
		for(File f : files) {
			String filePath = f.getPath().toLowerCase();

			if(d != Directory.MAIN && !filePath.contains(d.getValue()))
				continue;
			
			if(getFileFormattedName(f).equalsIgnoreCase(fileName))
				return f;
		}
		
		return null;
	}
	
	public static String getFileFormattedName(File f) {
		String fileName = f.getName();
		return fileName.substring((fileName.contains(File.separator) ? fileName.indexOf(File.separator)+1 : 0), fileName.lastIndexOf(".")).toLowerCase();
	}
	
	public static String getFileData(File f) {
		if(f == null)
			return null;
		
		String data = null;
		
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
			data = br.readLine();
			br.close();
		} catch (Exception e) { e.printStackTrace(); }
		
		return (data == null ? "" : data);
	}
	
	public static void writeToFile(File f, String data) {
		writeToFile(f, data, false);
	}
	
	public static void writeToFile(File f, int data) {
		writeToFile(f, data+"", false);
	}
	
	public static void writeToFile(File f, String data, boolean append) {
		if(f == null)
			return;
		
		try {
			FileWriter fw = new FileWriter(f, append);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(data);
			bw.flush();
			bw.close();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public static void addFile(File f) {
		files.add(f);
	}
	
	public static void removeFile(File f) {
		files.remove(f);
	}
	
	public static void removeFile(String fileName) {
		for(File f : files)
			if(getFileFormattedName(f).equalsIgnoreCase(fileName)) {
				files.remove(f);
				break;
			}
	}
	
	public static ArrayList<File> getFiles() {
		return files;
	}

}
