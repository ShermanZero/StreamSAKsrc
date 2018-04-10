package main.StreamSAK.GUI.components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.StreamSAK.GUI.GUI;
import main.StreamSAK.GUI.components.countersadjustersplugins.Adjuster;
import main.StreamSAK.GUI.components.countersadjustersplugins.Counter;
import main.StreamSAK.GUI.components.countersadjustersplugins.CountersAdjustersPlugins;
import main.StreamSAK.GUI.components.logandinput.Input;
import main.StreamSAK.GUI.components.logandinput.Log;
import main.StreamSAK.GUI.components.misc.CustomButton;
import main.StreamSAK.misc.Handler;
import main.StreamSAK.misc.StreamSAKFileHandler;
import main.StreamSAK.misc.StreamSAKFileHandler.Directory;
import main.StreamSAK.misc.actions.Action;

public class Options extends JPanel {
	private static final long serialVersionUID = 1L;

	public Options() {
		this.setBackground(Color.DARK_GRAY);
		this.setLayout(new GridBagLayout());
		this.setBorder(new EmptyBorder(10, 8, 10, 8));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		
		JButton createCounter = new CustomButton("CREATE NEW COUNTER", Counter.counterForegroundColor);
		createCounter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { try { createNewCounter(); } catch (Exception e) { e.printStackTrace(); } }
		});
		
		JButton resetCounters = new CustomButton("RESET ALL COUNTERS", Counter.counterForegroundColor);
		resetCounters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { CountersAdjustersPlugins.resetAllCounters(); }
		});

		JButton deleteCounter = new CustomButton("DELETE COUNTER", GUI.defaultRedColor);
		deleteCounter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { try { deleteCounter(); } catch (Exception e) { e.printStackTrace(); } }
		});

		JButton createAdjuster = new CustomButton("CREATE NEW ADJUSTER", Adjuster.adjusterForegroundColor);
		createAdjuster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { try { createNewAdjuster(); } catch (Exception e) { e.printStackTrace(); } }
		});
		
		JButton resetAdjusters = new CustomButton("RESET ALL ADJUSTERS", Adjuster.adjusterForegroundColor);
		resetAdjusters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { CountersAdjustersPlugins.resetAllAdjusters(); }
		});
		
		JButton deleteAdjuster = new CustomButton("DELETE ADJUSTER", GUI.defaultRedColor);
		deleteAdjuster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { try { deleteAdjuster(); } catch (Exception e) { e.printStackTrace(); } }
		});
		
		JButton[] buttons = {createCounter, resetCounters, deleteCounter, createAdjuster, resetAdjusters, deleteAdjuster};
		for(JButton b : buttons) {
			gbc.gridy++;
			this.add(b, gbc);
			
			if(b.equals(deleteCounter)) {
				gbc.gridy++;
				this.add(new JLabel(" "), gbc);
			}
		}
	}
	
	private static void createNewCounter() {
		Handler.doOnInput(new Action() {
			public void run() throws Exception {
				String newCounter = Input.getLastInput();
				if(newCounter.equals(""))
					return;
				
				if(StreamSAKFileHandler.findFile(newCounter, Directory.MAIN) != null) {
					String entry = "CANNOT CREATE COUNTER WITH THAT NAME";
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
				
				String entry = "CREATED NEW COUNTER: "+newCounter;
				Log.write(entry);
				
				StreamSAKFileHandler.addFile(f);
				CountersAdjustersPlugins.createCounterButton(f);
				
				System.out.println("Succesfully created new counter: "+f.getPath());
			}
		}, "CREATE A NEW COUNTER");
	}
	
	private static void deleteCounter() {
		Handler.doOnInput(new Action() {
			public void run() throws Exception {
				String counter = Input.getLastInput();
				if(counter.equals(""))
					return;
				
				for(File f : StreamSAKFileHandler.getFiles())
					if(StreamSAKFileHandler.getFileFormattedName(f).equalsIgnoreCase(counter) && f.getPath().toLowerCase().contains(Directory.COUNTERS.getValue())) {
						String fileName = f.getPath();
						fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1, fileName.lastIndexOf(".")).toUpperCase();
						
						CountersAdjustersPlugins.deleteCounter(fileName);
						StreamSAKFileHandler.removeFile(f);
						f.delete();

						String entry = "DELETED COUNTER: "+fileName;
						Log.write(entry);
						System.out.println("Succesfully deleted counter: "+f.getPath());
						return;
					}
				
				String entry = "COULD NOT DELETE COUNTER: "+counter;
				Log.write(entry);
				
				System.out.println("Could not delete counter: "+counter);
			}
		}, "DELETE A COUNTER");
	}
	
	private static void createNewAdjuster() {
		Handler.doOnInput(new Action() {
			public void run() throws Exception {
				String newAdjuster = Input.getLastInput();
				if(newAdjuster.equals(""))
					return;
				
				if(StreamSAKFileHandler.findFile(newAdjuster, Directory.MAIN) != null) {
					String entry = "CANNOT CREATE ADJUSTER WITH THAT NAME";
					Log.write(entry);
					return;
				}
				
				File f = new File(StreamSAKFileHandler.adjustersDirectoryPath+File.separator+newAdjuster+(newAdjuster.contains(".txt") ? "" : ".txt"));
				f.createNewFile();
				
				String entry = "CREATED NEW ADJUSTER: "+newAdjuster;
				Log.write(entry);
				
				StreamSAKFileHandler.addFile(f);
				CountersAdjustersPlugins.createAdjusterButton(f);
				
				System.out.println("Succesfully created new adjuster: "+f.getPath());
			}
		}, "CREATE A NEW ADJUSTER");
	}
	
	private static void deleteAdjuster() {
		Handler.doOnInput(new Action() {
			public void run() throws Exception {
				String adjuster = Input.getLastInput();
				if(adjuster.equals(""))
					return;
				
				for(File f : StreamSAKFileHandler.getFiles())
					if(StreamSAKFileHandler.getFileFormattedName(f).equalsIgnoreCase(adjuster) && f.getPath().toLowerCase().contains(Directory.ADJUSTERS.getValue())) {
						String fileName = f.getPath();
						fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1, fileName.lastIndexOf(".")).toUpperCase();
						
						CountersAdjustersPlugins.deleteAdjuster(fileName);
						StreamSAKFileHandler.removeFile(f);
						f.delete();
						
						String entry = "DELETED ADJUSTER: "+fileName;
						Log.write(entry);
						System.out.println("Succesfully deleted counter: "+fileName);
						return;
					}
				
				
				String entry = "COULD NOT DELETE ADJUSTER: "+adjuster;
				Log.write(entry);
					
				System.out.println("Could not delete adjuster: "+adjuster);
			}
		}, "DELETE AN ADJUSTER");
	}

}
