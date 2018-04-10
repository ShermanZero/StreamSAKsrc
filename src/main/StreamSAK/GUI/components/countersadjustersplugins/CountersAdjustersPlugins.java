package main.StreamSAK.GUI.components.countersadjustersplugins;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import main.StreamSAK.GUI.GUI;
import main.StreamSAK.GUI.components.logandinput.Log;
import main.StreamSAK.GUI.components.misc.CustomButton;
import main.StreamSAK.misc.Handler;
import main.StreamSAK.misc.StreamSAKFileHandler;
import main.StreamSAK.misc.StreamSAKFileHandler.Directory;
import main.StreamSAKPluginLibrary.StreamSAKPlugin;

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
		
		for(GridBagConstraints gbc : new GridBagConstraints[] {counterGBC, adjusterGBC, pluginGBC}) {
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.gridx = 1;
			gbc.gridy = 1;
		}
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		Component [] components = {generateCounterPanel(), generateAdjusterPanel(), generatePluginPanel()};
		for(Component c : components) {
			gbc.gridy++;
			this.add(c, gbc);
		}
		
		gbc.gridy++;
		this.add(new JLabel(" "), gbc);
		
		JButton clear = new CustomButton("CLEAR LOG", GUI.defaultRedColor);
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { Log.clear(); }
		});
		
		gbc.gridy++;
		this.add(clear, gbc);
		
		adjustWindowHeight();
	}
	
	public static void createCounterButton(File counterFile) {
		Counter c = new Counter(counterFile);
		counters.add(c);
		
		JButton[] buttons = c.generate();
		for(int i = 0; i < buttons.length; i++) {
			counterGBC.gridx = (i+1);
			counterPanel.add(buttons[i], counterGBC);
		}
		
		counterGBC.gridy++;
		counterPanel.revalidate();
		componentCount++;
		
		adjustWindowHeight();
	}
	
	public static void createAdjusterButton(File adjusterFile) {
		Adjuster a = new Adjuster(adjusterFile);
		adjusters.add(a); 
		
		JButton[] buttons = a.generate();
		for(int i = 0; i < buttons.length; i++) {
			adjusterGBC.gridx = (i+1);
			adjusterPanel.add(buttons[i], adjusterGBC);
		}
		
		adjusterGBC.gridy++;
		counterPanel.revalidate();
		componentCount++;
		
		adjustWindowHeight();
	}
	
	public static void createPluginButton(StreamSAKPlugin plugin) {
		Plugin p = new Plugin(plugin);
		
		JButton[] buttons = p.generate();
		for(int i = 0; i < buttons.length; i++) {
			pluginGBC.gridx = (i+1);
			pluginPanel.add(buttons[i], pluginGBC);
		}
		
		pluginGBC.gridy++;
		pluginPanel.revalidate();
		componentCount++;
		
		adjustWindowHeight();
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
		
		Handler.removeLink(StreamSAKFileHandler.findFile(counterName, Directory.COUNTERS));
		adjustWindowHeight();
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
		
		adjustWindowHeight();
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
		counterPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
		
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
		
		if(plugins.size() > 0)
			pluginPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.GRAY));
		
		for(Plugin p : plugins) {
			StreamSAKPlugin sp = p.getPlugin();
			createPluginButton(sp);
		}

		return pluginPanel;
	}
	
	private static void adjustWindowHeight() {
		int newHeight = 100+(componentCount+2)*CustomButton.getComponentHeight();
		GUI.setHeight(newHeight);
	}
	
}
