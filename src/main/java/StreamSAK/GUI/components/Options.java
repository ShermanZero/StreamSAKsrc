package main.java.StreamSAK.GUI.components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.java.StreamSAK.GUI.GUI;
import main.java.StreamSAK.GUI.components.countersadjustersplugins.Adjuster;
import main.java.StreamSAK.GUI.components.countersadjustersplugins.Counter;
import main.java.StreamSAK.GUI.components.countersadjustersplugins.CountersAdjustersPlugins;
import main.java.StreamSAK.GUI.components.logandinput.Input;
import main.java.StreamSAK.GUI.components.logandinput.Log;
import main.java.StreamSAK.GUI.components.misc.CustomButton;
import main.java.StreamSAK.misc.StreamSAKFileHandler;
import main.java.StreamSAK.misc.StreamSAKFileHandler.Directory;
import main.java.StreamSAK.misc.StreamSAKHandler;
import main.java.StreamSAK.misc.actions.StreamSAKAction;

public class Options extends JPanel {
	private static final long serialVersionUID = 1L;

	public Options() {
		this.setBackground(Color.DARK_GRAY);
		this.setLayout(new GridBagLayout());
		this.setBorder(new EmptyBorder(10, 8, 10, 8));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		
		JButton createCounter = new CustomButton("Create New Counter", Counter.counterForegroundColor);
		createCounter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { try { createNewCounter(); } catch (Exception e) { e.printStackTrace(); } }
		});
		
		JButton resetCounters = new CustomButton("Reset All Counters", GUI.defaultRedColor);
		resetCounters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { CountersAdjustersPlugins.resetAllCounters(); }
		});

		JButton createAdjuster = new CustomButton("Create New Adjuster", Adjuster.adjusterForegroundColor);
		createAdjuster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { try { createNewAdjuster(); } catch (Exception e) { e.printStackTrace(); } }
		});
		
		JButton resetAdjusters = new CustomButton("Reset All Adjusters", GUI.defaultRedColor);
		resetAdjusters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { CountersAdjustersPlugins.resetAllAdjusters(); }
		});
		
		JButton[] buttons = {createCounter, resetCounters, createAdjuster, resetAdjusters};
		for(JButton b : buttons) {
			gbc.gridy++;
			this.add(b, gbc);
			
			if(b.equals(resetCounters)) {
				gbc.gridy++;
				this.add(Box.createVerticalStrut(30), gbc);
			}
		}
	}
	
	private static void createNewCounter() {
		StreamSAKHandler.doOnInput(new StreamSAKAction() {
			public void run() throws Exception {
				String newCounter = Input.getLastInput();
				if(newCounter.equals(""))
					return;
				
				if(StreamSAKFileHandler.findFile(newCounter, Directory.MAIN) != null) {
					String entry = "Cannot create counter with that name";
					Log.write(entry);
					return;
				}
				
				File f = new File(StreamSAKFileHandler.countersDirectoryPath+File.separator+newCounter+(newCounter.contains(".txt") ? "" : ".txt"));
				f.createNewFile();
				
				FileWriter fw = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("0");
				bw.flush();
				bw.close();
				
				String entry = "Created new counter: "+newCounter;
				Log.write(entry);
				
				StreamSAKFileHandler.addFile(f);
				CountersAdjustersPlugins.createCounterButton(f);
			}
		}, "Create a New Counter");
	}
	
	private static void createNewAdjuster() {
		StreamSAKHandler.doOnInput(new StreamSAKAction() {
			public void run() throws Exception {
				String newAdjuster = Input.getLastInput();
				if(newAdjuster.equals(""))
					return;
				
				if(StreamSAKFileHandler.findFile(newAdjuster, Directory.MAIN) != null) {
					String entry = "Cannot create adjuster with that name";
					Log.write(entry);
					return;
				}
				
				File f = new File(StreamSAKFileHandler.adjustersDirectoryPath+File.separator+newAdjuster+(newAdjuster.contains(".txt") ? "" : ".txt"));
				f.createNewFile();
				
				String entry = "Created new adjuster: "+newAdjuster;
				Log.write(entry);
				
				StreamSAKFileHandler.addFile(f);
				Adjuster a = CountersAdjustersPlugins.createAdjusterButton(f);
				
				a.changeAdjuster();
			}
		}, "Create a New Adjuster");
	}

}
