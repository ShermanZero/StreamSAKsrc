package StreamSAK.GUI.components;

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

import StreamSAK.GUI.GUI;
import StreamSAK.GUI.components.countersadjustersplugins.Adjuster;
import StreamSAK.GUI.components.countersadjustersplugins.Counter;
import StreamSAK.GUI.components.countersadjustersplugins.CountersAdjustersPlugins;
import StreamSAK.GUI.components.logandinput.Input;
import StreamSAK.GUI.components.logandinput.Log;
import StreamSAK.GUI.components.misc.CustomButton;
import StreamSAK.misc.StreamSAKFileHandler;
import StreamSAK.misc.StreamSAKHandler;
import StreamSAK.misc.StreamSAKFileHandler.Directory;
import StreamSAK.misc.actions.Action;

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
		
		JButton resetCounters = new CustomButton("Reset All Counters", Counter.counterForegroundColor);
		resetCounters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { CountersAdjustersPlugins.resetAllCounters(); }
		});

		JButton deleteCounter = new CustomButton("Delete Counter", GUI.defaultRedColor);
		deleteCounter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { try { deleteCounter(); } catch (Exception e) { e.printStackTrace(); } }
		});

		JButton createAdjuster = new CustomButton("Create New Adjuster", Adjuster.adjusterForegroundColor);
		createAdjuster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { try { createNewAdjuster(); } catch (Exception e) { e.printStackTrace(); } }
		});
		
		JButton resetAdjusters = new CustomButton("Reset All Adjusters", Adjuster.adjusterForegroundColor);
		resetAdjusters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { CountersAdjustersPlugins.resetAllAdjusters(); }
		});
		
		JButton deleteAdjuster = new CustomButton("Delete Adjuster", GUI.defaultRedColor);
		deleteAdjuster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { try { deleteAdjuster(); } catch (Exception e) { e.printStackTrace(); } }
		});
		
		JButton[] buttons = {createCounter, resetCounters, deleteCounter, createAdjuster, resetAdjusters, deleteAdjuster};
		for(JButton b : buttons) {
			gbc.gridy++;
			this.add(b, gbc);
			
			if(b.equals(deleteCounter)) {
				gbc.gridy++;
				this.add(Box.createVerticalStrut(30), gbc);
			}
		}
	}
	
	private static void createNewCounter() {
		StreamSAKHandler.doOnInput(new Action() {
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
	
	private static void deleteCounter() {
		StreamSAKHandler.doOnInput(new Action() {
			public void run() throws Exception {
				String counter = Input.getLastInput();
				if(counter.equals(""))
					return;
				
				for(File f : StreamSAKFileHandler.getFiles())
					if(StreamSAKFileHandler.getFileFormattedName(f).equalsIgnoreCase(counter) && f.getPath().toLowerCase().contains(Directory.COUNTERS.getValue())) {
						String fileName = f.getPath();
						fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1, fileName.lastIndexOf("."));
						
						CountersAdjustersPlugins.deleteCounter(fileName);
						StreamSAKFileHandler.removeFile(f);
						f.delete();

						String entry = "Deleted counter: "+fileName;
						Log.write(entry);
						return;
					}
				
				String entry = "Could not delete counter: "+counter;
				Log.write(entry);
			}
		}, "Delete a Counter");
	}
	
	private static void createNewAdjuster() {
		StreamSAKHandler.doOnInput(new Action() {
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
				CountersAdjustersPlugins.createAdjusterButton(f);
			}
		}, "Create a New Adjuster");
	}
	
	private static void deleteAdjuster() {
		StreamSAKHandler.doOnInput(new Action() {
			public void run() throws Exception {
				String adjuster = Input.getLastInput();
				if(adjuster.equals(""))
					return;
				
				for(File f : StreamSAKFileHandler.getFiles())
					if(StreamSAKFileHandler.getFileFormattedName(f).equalsIgnoreCase(adjuster) && f.getPath().toLowerCase().contains(Directory.ADJUSTERS.getValue())) {
						String fileName = f.getPath();
						fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1, fileName.lastIndexOf("."));
						
						CountersAdjustersPlugins.deleteAdjuster(fileName);
						StreamSAKFileHandler.removeFile(f);
						f.delete();
						
						String entry = "Deleted adjuster: "+fileName;
						Log.write(entry);
						return;
					}
				
				
				String entry = "Could not delete adjuster: "+adjuster;
				Log.write(entry);
			}
		}, "Delete an Adjuster");
	}

}
