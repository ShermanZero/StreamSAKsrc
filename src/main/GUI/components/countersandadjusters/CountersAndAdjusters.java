package main.GUI.components.countersandadjusters;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import main.GUI.GUI;
import main.GUI.components.misc.CustomButton;
import main.GUI.logandinput.Log;
import main.misc.FileHandler;
import main.misc.FileHandler.Directory;
import main.misc.Handler;

public class CountersAndAdjusters extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static JPanel counterPanel;
	private static JPanel adjusterPanel;
	
	private static ArrayList<Counter> counters = new ArrayList<Counter>();
	private static ArrayList<Adjuster> adjusters = new ArrayList<Adjuster>();
	
	private static GridBagConstraints counterGBC = new GridBagConstraints();
	private static GridBagConstraints adjusterGBC = new GridBagConstraints();
	
	public CountersAndAdjusters() {
		this.setBackground(Color.DARK_GRAY);
		this.setLayout(new GridBagLayout());
		this.setBorder(new CompoundBorder(new MatteBorder(2, 2, 2, 2, Color.GRAY), new EmptyBorder(10, 8, 10, 8)));
		
		for(GridBagConstraints gbc : new GridBagConstraints[] {counterGBC, adjusterGBC}) {
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.gridx = 1;
			gbc.gridy = 1;
		}
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		JButton clear = new CustomButton("CLEAR LOG", GUI.defaultRedColor);
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { Log.clear(); }
		});
		
		Component [] components = {generateCounterPanel(), generateAdjusterPanel(), clear};
		for(Component c : components) {
			gbc.gridy++;
			this.add(c, gbc);
		}
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
	}
	
	public static void deleteCounter(String counterName) {
		for(Counter c : counters)
			if(c.getName().equalsIgnoreCase(counterName)) {
				counters.remove(c);
				break;
			}
		
		for(int i = 0; i < counterPanel.getComponentCount(); i++) {
			Component c = counterPanel.getComponent(i);
			JButton b = (JButton)c;
			
			if(b.getText().equalsIgnoreCase(counterName)) {
				for(int j = 0; j < 4; j++)
					counterPanel.remove(i);
				break;
			}
		}
		
		counterPanel.revalidate();
		
		Handler.removeAdjusterLink(FileHandler.findFile(counterName, Directory.COUNTERS));
	}
	
	public static void deleteAdjuster(String adjusterName) {
		for(Adjuster a : adjusters)
			if(a.getName().equalsIgnoreCase(adjusterName)) {
				adjusters.remove(a);
				break;
			}
		
		for(int i = 0; i < adjusterPanel.getComponentCount(); i++) {
			Component c = adjusterPanel.getComponent(i);
			JButton b = (JButton)c;
			
			if(b.getText().equalsIgnoreCase(adjusterName)) {
				adjusterPanel.remove(i);
				break;
			}
		}
		
		adjusterPanel.revalidate();
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
	
	private static JPanel generateCounterPanel() {
		counterPanel = new JPanel(new GridBagLayout());
		counterPanel.setBackground(null);
		
		for(File f : FileHandler.getFiles())
			if(f.getPath().contains("counters"))
				createCounterButton(f);
		
		return counterPanel;
	}
	
	private static JPanel generateAdjusterPanel() {
		adjusterPanel = new JPanel(new GridBagLayout());
		adjusterPanel.setBackground(null);
		
		for(File f : FileHandler.getFiles())
			 if(f.getPath().contains("adjusters"))
				 createAdjusterButton(f);
		
		return adjusterPanel;
	}
	
}
