package main.java.StreamSAK.misc;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.jar.JarFile;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.java.StreamSAK.StreamSAK;
import main.java.StreamSAK.GUI.GUI;
import main.java.StreamSAK.GUI.components.countersadjustersplugins.Adjuster;
import main.java.StreamSAK.GUI.components.countersadjustersplugins.CountersAdjustersPlugins;
import main.java.StreamSAK.GUI.components.countersadjustersplugins.Plugin;
import main.java.StreamSAK.GUI.components.misc.CustomButton;
import main.java.src.StreamSAKPlugin;

public class StreamSAKFileHandler {
	
	public static String dataPath, countersDirectoryPath, adjustersDirectoryPath,
	pluginsDirectoryPath, sourceDirectoryPath, miscDirectoryPath, propertiesFilePath;
	
	private static boolean loadPlugin;
	private static ArrayList<File> files = new ArrayList<File>();
	
	public enum Directory {
		COUNTERS("counters"), ADJUSTERS("adjusters"), PLUGINS("plugins"), SOURCE("src"), MISC("misc"), MAIN("");
		
		private String value;
		private Directory(String str) {
			value = str;
		}
		
		public String getValue() {
			return value;
		}
	}

	public static void init() throws Exception {
		String path = null;
		try {
			path = new File(StreamSAK.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getAbsolutePath();
		} catch (URISyntaxException e) { e.printStackTrace(); }
		String jarPath = path.substring(0, path.lastIndexOf(File.separator));
		
		dataPath = jarPath+File.separator+"data";

		miscDirectoryPath = dataPath+File.separator+Directory.MISC.getValue();
		countersDirectoryPath = dataPath+File.separator+Directory.COUNTERS.getValue();
		adjustersDirectoryPath = dataPath+File.separator+Directory.ADJUSTERS.getValue();
		pluginsDirectoryPath = dataPath+File.separator+Directory.PLUGINS.getValue();
		sourceDirectoryPath = dataPath+File.separator+Directory.SOURCE.getValue();
		
		propertiesFilePath = miscDirectoryPath+File.separator+"links.properties";
		
		File dir = null, sr = null, wins, losses, draws, logFile, propertiesFile;

		//check to see if the misc directory exists
		dir = new File(miscDirectoryPath);
		if(!dir.exists()) {
			dir.mkdirs();
			
			logFile = new File(miscDirectoryPath+File.separator+"log.txt");
			if(!logFile.exists())
				logFile.createNewFile();
			
			propertiesFile = new File(propertiesFilePath);
			if(!propertiesFile.exists())
				propertiesFile.createNewFile();
		}
		
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
			writeToFile(wins, "0");
			StreamSAKHandler.setLink(getFileFormattedName(wins), getFileFormattedName(sr));
			
			losses = new File(countersDirectoryPath+File.separator+"losses.txt");
			losses.createNewFile();
			writeToFile(losses, "0");
			StreamSAKHandler.setLink(getFileFormattedName(losses), getFileFormattedName(sr));
			
			draws = new File(countersDirectoryPath+File.separator+"draws.txt");
			draws.createNewFile();
			writeToFile(draws, "0");
		}
		
		//check to see if the plug-ins directory exists
		dir = new File(pluginsDirectoryPath);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		//get all the files within the misc folder
		File miscFolder = new File(miscDirectoryPath);
		File[] miscList = miscFolder.listFiles();
			for(File f : miscList)
				if(f.isFile())
					addFile(f);
		
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
		
		loadPlugins();
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
	
	public static Plugin findPlugin(String pluginName) {
		for(Plugin p : CountersAdjustersPlugins.getPlugins())
			if(p.getName().equalsIgnoreCase(pluginName))
				return p;
		
		return null;
	}

	public static String getFileFormattedName(File f) {
		String fileName = f.getName();
		return fileName.substring((fileName.contains(File.separator) ? fileName.indexOf(File.separator)+1 : 0), fileName.lastIndexOf("."));
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

	public static String readFromURL(String urlPath) {
		String data = "", line;
		
		URL url = null;
		try {
			url = new URL(urlPath);
		} catch (MalformedURLException e) { e.printStackTrace(); }
		
		Scanner s = null;
		try {
			s = new Scanner(url.openStream());
			
			while(s.hasNextLine()) {
				line = s.nextLine();
				data += line+"\n";
			}
			
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			s.close();
		}
		
		return data.trim();
	}
	
	public static void openURL(String urlPath) {
		try {
			Desktop.getDesktop().browse(URI.create(urlPath)); 
		} catch (IOException e1) { e1.printStackTrace(); }
	}
	
	public static ArrayList<File> getFiles() {
		return files;
	}
	
	
	private static void loadPlugins() {
		//get all the files within the plug-ins folder
		File pluginsFolder = new File(pluginsDirectoryPath);
		File[] pluginsList = pluginsFolder.listFiles();
		for(File f : pluginsList)
			if(f.isFile() && f.getName().endsWith(".jar"))
				addFile(f);
			else
				JOptionPane.showMessageDialog(null, f.getName()+" in the plugins folder is not a .jar file");
		
		ArrayList<URL> urls = new ArrayList<>();
		ArrayList<String> classes = new ArrayList<>();
        
		if(pluginsList != null) {
			loadPlugin = false;
			
			Arrays.stream(pluginsList).forEach(file -> {
				try {
        			JarFile jarFile = new JarFile(file);
    				urls.add(new URL("jar:file:"+pluginsDirectoryPath+File.separator+file.getName()+"!/"));
    				
    				jarFile.stream().forEach(jarEntry -> {
    					if(jarEntry.getName().endsWith(".dat")) {
    						String pluginBuild = "";
    						try {
								InputStream is = jarFile.getInputStream(jarEntry);
								BufferedReader br = new BufferedReader(new InputStreamReader(is));
								
								pluginBuild = br.readLine();
								br.close();
							} catch (IOException e) { e.printStackTrace(); }
    						String jarFileName = jarFile.getName();

    						loadPlugin = pluginBuild.equals(StreamSAK.getPluginLibraryBuild());
    						if(!loadPlugin)
    							failPlugin(jarFileName.substring(jarFileName.lastIndexOf(File.separator)+1), pluginBuild);
    						
    						System.out.println(jarFileName+" running library build ["+pluginBuild+"]");
    					}
    				});
    				
    				if(loadPlugin) {
    					jarFile.stream().forEach(jarEntry -> {	
        					if(jarEntry.getName().endsWith(".class"))
        						classes.add(jarEntry.getName());
        				});
    				}
    				
    				jarFile.close();
				} catch (Exception e) { e.printStackTrace(); }
			});
			
			URLClassLoader pluginLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
			classes.forEach(s -> {
				try {
					Class<?> subClass = pluginLoader.loadClass(s.replaceAll("/", ".").replace(".class", ""));
					Class<?> superClass = subClass.getSuperclass();
					if(superClass != null && superClass == StreamSAKPlugin.class) {
						System.out.println("  found ->\n    ["+subClass+"], super: ["+superClass+"]");

						StreamSAKPlugin plugin = (StreamSAKPlugin)(subClass.newInstance());
						System.out.println("   *loaded successfully*\n");
						
						Plugin p = new Plugin(plugin);
						CountersAdjustersPlugins.addPlugin(p);
					}
				} catch (Exception e) { e.printStackTrace(); }
			});
            
			try { pluginLoader.close(); } catch (Exception e) { e.printStackTrace(); }
        }
	}
	
	private static void failPlugin(String pluginName, String pluginBuild) {
		JFrame window = new JFrame();
		
		String currentVersion = StreamSAK.getCurrentVersion();
		String currentBuild = StreamSAK.getPluginLibraryBuild();
		
		String header = pluginName+" could not be loaded.";
		String message = pluginName+" is currently using the StreamSAKPluginLibrary build of "+pluginBuild+"."+
				"  The StreamSAKPluginLibrary build of this StreamSAK client ("+currentVersion+") is "+currentBuild+
				".\n\nIt is up to the developer to download the latest version of the StreamSAKPluginLibrary, and update their plug-in so that it"+
				" runs smoothly with the current StreamSAK client ("+currentVersion+").  If you are the developer, please"+
				" download the newest library below and update your plug-in.";
		
		JButton show = new CustomButton("Download StreamSAKPluginLibrary ("+currentBuild+")", Adjuster.adjusterForegroundColor);
		show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				StreamSAKFileHandler.openURL("https://github.com/ShermanZero/StreamSAK/raw/master/data/plugins/src/StreamSAKPluginLibrary.jar");
				window.dispose();
			}
		});
		
		JButton exit = new CustomButton("I Understand", GUI.defaultRedColor);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { window.dispose(); }
		});
		
		GUI.generateNotification(window, header, message, new JButton[] {show, exit});
	}
	
}
