package main.StreamSAK.misc;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

import main.StreamSAK.StreamSAK;
import main.StreamSAK.GUI.GUI;
import main.StreamSAK.GUI.components.countersadjustersplugins.Adjuster;
import main.StreamSAK.GUI.components.countersadjustersplugins.CountersAdjustersPlugins;
import main.StreamSAK.GUI.components.countersadjustersplugins.Plugin;
import main.StreamSAK.GUI.components.misc.CustomButton;
import main.StreamSAKPluginLibrary.StreamSAKPlugin;

public class StreamSAKFileHandler {
	
	public static String jarPath, countersDirectoryPath, adjustersDirectoryPath, pluginsDirectoryPath, sourceDirectoryPath, propertiesFilePath;
	
	private static ArrayList<File> files = new ArrayList<File>();
	
	public enum Directory {
		COUNTERS("counters"), ADJUSTERS("adjusters"), PLUGINS("plugins"), SOURCE("src"), MAIN("");
		
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
		pluginsDirectoryPath = jarPath+File.separator+Directory.PLUGINS.getValue();
		sourceDirectoryPath = jarPath+File.separator+Directory.SOURCE.getValue();
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
			Handler.setLink(wins, sr);
			writeToFile(wins, "0");
			
			losses = new File(countersDirectoryPath+File.separator+"losses.txt");
			losses.createNewFile();
			Handler.setLink(losses, sr);
			writeToFile(losses, "0");
			
			draws = new File(countersDirectoryPath+File.separator+"draws.txt");
			draws.createNewFile();
			writeToFile(draws, "0");
		}
		
		//check to see if the plug-ins directory exists
		dir = new File(pluginsDirectoryPath);
		if(!dir.exists()) {
			dir.mkdirs();
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
        	Arrays.stream(pluginsList).forEach(file -> {
        		try {
        			JarFile jarFile = new JarFile(file);
                    urls.add(new URL("jar:file:"+pluginsDirectoryPath+File.separator+file.getName()+"!/"));
                    
                    jarFile.stream().forEach(jarEntry -> {
                        if(jarEntry.getName().endsWith(".class")) {
                            classes.add(jarEntry.getName());
                            System.out.println("adding ["+jarEntry.getName()+"]");
                        }
                    });
                    
                    jarFile.close();
                } catch (Exception e) { e.printStackTrace(); }
            });
        	
        	System.out.println("\nLoading...\n");
        	
            URLClassLoader pluginLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
            classes.forEach(s -> {
            	try {
            		Class<?> subClass = pluginLoader.loadClass(s.replaceAll("/", ".").replace(".class", ""));
            		Class<?> superClass = subClass.getSuperclass();
            		Class<?>[] interfaces = superClass.getInterfaces();
            		
            		System.out.println("  found ->\n    ["+subClass+"], super: ["+superClass+"], contains: ["+interfaces.length+"] interface(s)");
            		
            		for (Class<?> anInterface : interfaces) {
            			System.out.println("      --["+anInterface+"]");
            			
            			if(anInterface == StreamSAKPlugin.class) {
            				StreamSAKPlugin plugin = (StreamSAKPlugin)(subClass.newInstance());
            				
            				String pluginBuild = plugin.getLocalBuild();
            				String currentBuild = StreamSAK.StreamSAKLibrary_BUILD;
            				
            				System.out.println("         current build ["+currentBuild+"]\n"
            								 + "         plug-in build ["+pluginBuild+"]");
            				
            				if(!plugin.getLocalBuild().equals(currentBuild)) {
            					failPlugin(plugin, currentBuild);
            				} else {
            					System.out.println("   *loaded successfully*");
                				CountersAdjustersPlugins.addPlugin(new Plugin(plugin));
            				}
            				break;
            			} else { System.out.println("   *could not load*"); }
            		}
            		
        			System.out.println();
            	} catch (Exception e) { e.printStackTrace(); }
            });
            
            try { pluginLoader.close(); } catch (Exception e) { e.printStackTrace(); }
        }
        
    	System.out.println("...Done");
	}
	
	private static void failPlugin(StreamSAKPlugin p, String currentBuild) {
		JFrame window = GUI.createNotificationWindow();
		
		String pluginBuild = p.getLocalBuild();
		
		String header = p.getName()+" ("+p.getVersion()+") could not be loaded.";
		String message = p.getName()+" is currently using the StreamSAKPlugin library build of "+pluginBuild+"."+
				"  The library plug-in build of this StreamSAK client is "+currentBuild+".\n\nIt is up to the"+
				" developer to download the latest version of the StreamSAKPlugin library, and update their plug-in so that it"+
				" runs smoothly with the current StreamSAK client ("+StreamSAK.STREAMSAK_VERSION+").  If you are the developer, please"+
				" download the newest plug-in library below and update your plug-in.";
		
		JButton show = new CustomButton("Download StreamSAKPlugin Library ("+currentBuild+")", Adjuster.adjusterForegroundColor);
		show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				try {
					Desktop.getDesktop().browse(URI.create("https://github.com/ShermanZero/StreamSAK/raw/master/data/plugins/src/StreamSAKPluginLibrary.jar"));
				} catch (IOException e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
				
				window.dispose();
			}
		});
		
		JButton exit = new CustomButton("Skip Plugin", GUI.defaultRedColor);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { window.dispose(); }
		});
		
		GUI.generateNotificationWindow(window, header, message, new JButton[] {show, exit});
	}
	
}
