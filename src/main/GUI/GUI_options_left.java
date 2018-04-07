package main.GUI;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import main.Action;
import main.Handler;

public class GUI_options_left extends GUI {
	
	public ArrayList<JButton> buttons = new ArrayList<JButton>();
	
	public GUI_options_left() {}
	
	public JPanel generate() {
		JPanel main = null;
		
		JButton createCounter = new JButton("CREATE NEW [COUNTER]");
		createCounter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try { createNewCounter(); } catch (Exception e) { e.printStackTrace(); }
			}
		});
		buttons.add(createCounter);
		
		JButton resetCounters = new JButton("RESET ALL [COUNTERS]");
		resetCounters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { Handler.resetCounters(); }
		});
		buttons.add(resetCounters);
		
		JButton deleteCounter = new JButton("DELETE [COUNTER]");
		deleteCounter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try { deleteCounter(); } catch (Exception e) { e.printStackTrace(); }
			}
		});
		buttons.add(deleteCounter);
		
		JButton createAdjuster = new JButton("CREATE NEW {ADJUSTER}");
		createAdjuster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try { createNewAdjuster(); } catch (Exception e) { e.printStackTrace(); }
			}
		});
		buttons.add(createAdjuster);
		
		JButton resetAdjusters = new JButton("RESET ALL {ADJUSTERS}");
		resetAdjusters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { Handler.resetAdjusters(); }
		});
		buttons.add(resetAdjusters);		
		
		JButton deleteAdjuster = new JButton("DELETE {ADJUSTER}");
		deleteAdjuster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try { deleteAdjuster(); } catch (Exception e) { e.printStackTrace(); }
			}
		});
		buttons.add(deleteAdjuster);
		
		main = new JPanel(new GridLayout(buttons.size(), 0));
		
		for(JButton b : buttons) {
			b.setFocusable(false);
			b.setBorder(null);
			
			if(b.getText().toLowerCase().contains("{") && b.getText().toLowerCase().contains("}"))
				b.setForeground(new Color(172, 209, 224));
			else if(b.getText().toLowerCase().contains("[") && b.getText().toLowerCase().contains("]"))
				b.setForeground(new Color(224, 217, 172));
			else
				b.setForeground(new Color(255, 100, 100));
			
			if(b.getText().toLowerCase().contains("delete"))
				b.setForeground(new Color(255, 100, 100));
			
			b.setBackground(Color.DARK_GRAY);
			b.setFont(GUI.font);
			
			GUI.addHighlightingToButton(b);
			
			b.setBorder(new EmptyBorder(0, 15, 0, 15));
			
			main.add(b);
		}
		
		
		main.setBackground(Color.DARK_GRAY);
		main.setBorder(new CompoundBorder(new MatteBorder(2, 2, 2, 2, Color.GRAY), new EmptyBorder(5, 5, 5, 5)));
		
		return main;
	}
	
	private void createNewCounter() {
		Handler.doOnInput(new Action() {
			public void run() throws Exception {
				String newCounter = GUI_log.input.toLowerCase();
				if(newCounter.equals(""))
					return;
				
				File f = new File(Handler.counterFilesPath+File.separator+newCounter+(newCounter.contains(".txt") ? "" : ".txt"));
				f.createNewFile();
				
				FileWriter fw = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("0");
				bw.flush();
				bw.close();
				
				String entry = "CREATED NEW COUNTER ["+newCounter+"]";
				l.write(entry);
				
				System.out.println("Succesfully created new counter: "+f.getAbsolutePath());
				restart();
			}
		}, "CREATE A NEW COUNTER");
	}
	
	private void deleteCounter() {
		Handler.doOnInput(new Action() {
			public void run() throws Exception {
				String counter = GUI_log.input.toLowerCase();
				if(counter.equals(""))
					return;
				
				for(File f : Handler.files)
					if(f.getPath().toLowerCase().contains(counter) && f.getPath().toLowerCase().contains("counters")) {
						String fileName = f.getPath();
						fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1, fileName.lastIndexOf(".")).toUpperCase();
						
						f.delete();
						String entry = "DELETED COUNTER {"+fileName+"}";
						l.write(entry);
						
						System.out.println("Succesfully deleted counter: "+fileName);
						restart();
						return;
					}
				
				String entry = "COULD NOT DELETE COUNTER {"+counter+"}";
				l.write(entry);
				
				System.out.println("Could not delete counter: "+counter);
			}
		}, "DELETE A COUNTER");
	}
	
	private void createNewAdjuster() {
		Handler.doOnInput(new Action() {
			public void run() throws Exception {
				String newAdjuster = GUI_log.input.toLowerCase();
				if(newAdjuster.equals(""))
					return;
				
				File f = new File(Handler.adjusterFilePath+File.separator+newAdjuster+(newAdjuster.contains(".txt") ? "" : ".txt"));
				f.createNewFile();
				
				String entry = "CREATED NEW ADJUSTER {"+newAdjuster+"}";
				l.write(entry);
				
				System.out.println("Succesfully created new adjuster: "+f.getAbsolutePath());
				restart();
			}
		}, "CREATE A NEW ADJUSTER");
	}
	
	private void deleteAdjuster() {
		Handler.doOnInput(new Action() {
			public void run() throws Exception {
				String adjuster = GUI_log.input.toLowerCase();
				if(adjuster.equals(""))
					return;
				
				for(File f : Handler.files)
					if(f.getPath().toLowerCase().contains(adjuster) && f.getPath().toLowerCase().contains("adjuster")) {
						String fileName = f.getPath();
						fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1, fileName.lastIndexOf(".")).toUpperCase();
						
						f.delete();
						String entry = "DELETED ADJUSTER {"+fileName+"}";
						l.write(entry);
						
						System.out.println("Succesfully deleted counter: "+fileName);
						restart();
						return;
					}
				
				
				String entry = "COULD NOT DELETE ADJUSTER {"+adjuster+"}";
				l.write(entry);
					
				System.out.println("Could not delete adjuster: "+adjuster);
			}
		}, "DELETE AN ADJUSTER");
	}

}
