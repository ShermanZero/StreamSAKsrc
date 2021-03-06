package main.java.StreamSAK.GUI.components.countersadjustersplugins;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import main.java.StreamSAK.GUI.GUI;
import main.java.StreamSAK.GUI.components.misc.CustomButton;
import main.java.StreamSAK.misc.StreamSAKFileHandler;
import main.java.StreamSAK.misc.StreamSAKFileHandler.Directory;
import main.java.StreamSAK.misc.StreamSAKHandler;

public class CountersAdjustersPlugins extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static JPanel counterPanel;
	private static JPanel adjusterPanel;
	private static JPanel pluginPanel;
	
	private static GridBagConstraints counterGBC = new GridBagConstraints();
	private static GridBagConstraints adjusterGBC = new GridBagConstraints();
	private static GridBagConstraints pluginGBC = new GridBagConstraints();
	
	private static ArrayList<Counter> counters = new ArrayList<>();
	private static ArrayList<Adjuster> adjusters = new ArrayList<>();
	private static ArrayList<Plugin> plugins = new ArrayList<>();
	
	private static int componentCount;
	
	public CountersAdjustersPlugins() {
		this.setBackground(Color.DARK_GRAY);
		this.setLayout(new GridBagLayout());
		this.setBorder(new EmptyBorder(10, 8, 10, 8));
		
		for(GridBagConstraints gbc : new GridBagConstraints[] { counterGBC, adjusterGBC, pluginGBC }) {
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.gridx = 1;
			gbc.gridy = 1;
		}
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		Component [] components = { generateCounterPanel(), generateAdjusterPanel(), generatePluginPanel() };
		for(Component c : components) {
			gbc.gridy++;
			this.add(c, gbc);
		}
		
		gbc.gridy++;
		this.add(new JLabel(" "), gbc);
		
		adjustWindow();
	}
	
	public static Counter createCounterButton(File counterFile) {
		Counter c = new Counter(counterFile);
		counters.add(c);
		
		JButton[] buttons = c.generate();
		for(int i = 0; i < buttons.length; i++) {
			counterGBC.gridx = (i+1);
			counterGBC.weightx = 1;

			if(i == buttons.length-1)
				counterGBC.weightx = 0;
			
			counterPanel.add(buttons[i], counterGBC);
		}
		
		counterGBC.gridy++;
		counterPanel.revalidate();
		componentCount++;
		
		adjustWindow();
		
		return c;
	}
	
	public static Adjuster createAdjusterButton(File adjusterFile) {
		Adjuster a = new Adjuster(adjusterFile);
		adjusters.add(a); 
		
		JButton[] buttons = a.generate();
		for(int i = 0; i < buttons.length; i++) {
			adjusterGBC.gridx = (i+1);
			adjusterGBC.weightx = 1;
			
			if(i == buttons.length-1)
				adjusterGBC.weightx = 0;
			adjusterPanel.add(buttons[i], adjusterGBC);
		}
		
		adjusterGBC.gridy++;
		counterPanel.revalidate();
		componentCount++;
		
		adjustWindow();
		
		return a;
	}
	
	public static void createPluginButton(Plugin plugin) {
		JButton[] buttons = plugin.generate();
		pluginGBC.gridwidth = (buttons.length-1);
		
		for(int i = 0; i < buttons.length; i++) {
			pluginGBC.gridx = (i+1);
			pluginPanel.add(buttons[i], pluginGBC);
		}
		
		pluginGBC.gridy++;
		pluginPanel.revalidate();
		componentCount++;
		
		adjustWindow();
	}
	
	public static void closePlugins() {
		for(Plugin p : plugins)
			p.getPlugin().doOnApplicationExit();
	}
	
	public static void deleteCounter(String counterName) {
		for(int i = 0; i < counters.size(); i++) {
			Counter c = counters.get(i);
			if(c.getName().equalsIgnoreCase(counterName)) {
				counters.remove(c);
				break;
			}
		}
		
		for(int i = 0; i < counterPanel.getComponentCount(); i++) {
			Component c = counterPanel.getComponent(i);
			JButton b = (JButton)c;
			
			if(b.getText().equalsIgnoreCase(counterName)) {
				for(int j = 0; j < Counter.componentCount; j++)
					counterPanel.remove(i);
				break;
			}
		}
		
		counterPanel.revalidate();
		componentCount--;
		
		File f = StreamSAKFileHandler.findFile(counterName, Directory.COUNTERS);
		StreamSAKHandler.removeLink(StreamSAKFileHandler.getFileFormattedName(f));
		adjustWindow();
	}
	
	public static void deleteAdjuster(String adjusterName) {
		for(int i = 0; i < adjusters.size(); i++) {
			Adjuster a = adjusters.get(i);
			if(a.getName().equalsIgnoreCase(adjusterName)) {
				adjusters.remove(a);
				break;
			}
		}
		
		for(int i = 0; i < adjusterPanel.getComponentCount(); i++) {
			Component c = adjusterPanel.getComponent(i);
			JButton b = (JButton)c;
			
			if(b.getText().equalsIgnoreCase(adjusterName)) {
				for(int j = 0; j < Adjuster.componentCount; j++)
					adjusterPanel.remove(i);
				break;
			}
		}
		
		adjusterPanel.revalidate();
		componentCount--;
		
		adjustWindow();
	}
	
	public static void resetAllCounters() {
		for(Counter c : counters)
			c.resetCounter();
	}
	
	public static void resetAllAdjusters() {
		for(Adjuster a : adjusters)
			a.resetAdjuster();
	}

	public static ArrayList<Counter> getCounters() {
		return counters;
	}
	
	public static ArrayList<Adjuster> getAdjusters() {
		return adjusters;
	}
	
	public static ArrayList<Plugin> getPlugins() {
		return plugins;
	}
	
	public static void addPlugin(Plugin p) {
		plugins.add(p);
	}
	
	private static JPanel generateCounterPanel() {
		counterPanel = new JPanel(new GridBagLayout());
		counterPanel.setBackground(null);
		
		for(File f : StreamSAKFileHandler.getFiles())
			if(f.getPath().contains(Directory.COUNTERS.getValue()))
				createCounterButton(f);
		
		return counterPanel;
	}
	
	private static JPanel generateAdjusterPanel() {
		adjusterPanel = new JPanel(new GridBagLayout());
		adjusterPanel.setBackground(null);
		
		for(File f : StreamSAKFileHandler.getFiles())
			 if(f.getPath().contains(Directory.ADJUSTERS.getValue()))
				 createAdjusterButton(f);
		
		return adjusterPanel;
	}
	
	private static JPanel generatePluginPanel() {
		pluginPanel = new JPanel(new GridBagLayout());
		pluginPanel.setBackground(null);
		
		for(Plugin p : plugins)
			createPluginButton(p);
	
		return pluginPanel;
	}
	
	private static void adjustWindow() {
		if(adjusterPanel != null) {
			adjusterPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.GRAY));
			
			if(counters.size() > 0 && adjusters.size() > 0 && plugins.size() > 0)
				adjusterPanel.setBorder(new MatteBorder(1, 0, 1, 0, Color.GRAY));
			else if(counters.size() > 0 && adjusters.size() == 0 && plugins.size() == 0)
				adjusterPanel.setBorder(null);
			else if(counters.size() == 0 && adjusters.size() > 0 && plugins.size() == 0)
				adjusterPanel.setBorder(null);
			else if(counters.size() == 0 && adjusters.size() == 0 && plugins.size() > 0)
				adjusterPanel.setBorder(null);
		}
		
		int newHeight = 100+(componentCount+2)*CustomButton.getComponentHeight();
		GUI.setHeight(newHeight);
	}
}
