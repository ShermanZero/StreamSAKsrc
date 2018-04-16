package main.java.StreamSAK.GUI.components.countersadjustersplugins;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import main.java.StreamSAK.GUI.GUI;
import main.java.StreamSAK.GUI.components.logandinput.Input;
import main.java.StreamSAK.GUI.components.logandinput.Log;
import main.java.StreamSAK.GUI.components.misc.CustomButton;
import main.java.StreamSAK.misc.StreamSAKFileHandler;
import main.java.StreamSAK.misc.StreamSAKFileHandler.Directory;
import main.java.StreamSAK.misc.StreamSAKHandler;
import main.java.StreamSAK.misc.actions.StreamSAKAction;

public class Counter {
	
	public static Color counterForegroundColor = new Color(224, 217, 172);
	
	public static int componentCount;
	
	private final File counterFile;
	private int value;
	
	public Counter(File f) {
		counterFile = f;
		value = Integer.parseInt(StreamSAKFileHandler.getFileData(counterFile));
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return StreamSAKFileHandler.getFileFormattedName(counterFile);
	}

	public JButton[] generate() {
		JButton[] components = { generateCounter(), generateUp(), generateDown(), generateLink(), generateDelete() };
		componentCount = components.length;
		return components;
	}
	
	public void resetCounter() {
		String entry = StreamSAKFileHandler.getFileFormattedName(counterFile)+": 0";
		Log.write(entry);
		
		value = 0;
		write();
	}
	
	private JButton generateCounter() {
		JButton b = new CustomButton(getName(), counterForegroundColor, true, true);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { displayCounter(); }
		});
		
		b.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				if(SwingUtilities.isRightMouseButton(evt))
					resetCounter();
			}
		});
		
		return b;
	}
	
	private JButton generateUp() {
		JButton up = new CustomButton("<html>&#9650</html>");
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { incrementCounter(); }
		});
		
		return up;
	}
	
	private JButton generateDown() {
		JButton down = new CustomButton("<html>&#9660</html>");
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { decrementCounter(); }
		});
		
		return down;
	}
	
	private JButton generateLink() {
		String link = StreamSAKHandler.getLink(StreamSAKFileHandler.getFileFormattedName(counterFile));
		Color startingColor = GUI.defaultColor;
		Plugin p = null;
		if(link != null) {
			if(StreamSAKFileHandler.findFile(link, Directory.ADJUSTERS) != null)
				startingColor = Adjuster.adjusterForegroundColor;
			else if ((p =StreamSAKFileHandler.findPlugin(link)) != null) {
				startingColor = Plugin.pluginForegroundColor;
				link = p.getName();
			}
		}
		
		JButton linkButton = new CustomButton(link == null ? "--" : link, startingColor);
		linkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				linkButton.setText("--");
				
				String fileName = StreamSAKFileHandler.getFileFormattedName(counterFile);
				
				if(StreamSAKHandler.getLink(fileName) != null) {
					StreamSAKHandler.removeLink(fileName);
					((CustomButton)linkButton).setDefaultForeground(GUI.defaultColor);
				} else {
					StreamSAKHandler.doOnInput(new StreamSAKAction() {
						@Override
						public void run() throws Exception {
							String str = Input.getLastInput();
							if(str.equals(""))
								return;
							
							Plugin p;
							File f = StreamSAKFileHandler.findFile(str, Directory.ADJUSTERS);
							
							if(StreamSAKHandler.setLink(fileName, StreamSAKFileHandler.getFileFormattedName(f))) {
								linkButton.setText(StreamSAKFileHandler.getFileFormattedName(f));
								((CustomButton)linkButton).setDefaultForeground(Adjuster.adjusterForegroundColor);
							} else
							if((p = StreamSAKFileHandler.findPlugin(str)) != null && StreamSAKHandler.setLink(fileName, str)) {
								linkButton.setText(p.getName());
								((CustomButton)linkButton).setDefaultForeground(Plugin.pluginForegroundColor);
							}
						}
					}, "Set Linked Call");
				}
			}
		});
		
		return linkButton;
	}
	
	private JButton generateDelete() {
		JButton delete = new CustomButton("x", GUI.defaultRedColor);
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String fileName = counterFile.getPath();
				fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1, fileName.lastIndexOf("."));
				
				CountersAdjustersPlugins.deleteCounter(fileName);
				StreamSAKFileHandler.removeFile(counterFile);
				counterFile.delete();
				
				String entry = "Deleted counter: "+fileName;
				Log.write(entry);
				return;
			}
		});
		
		return delete;
	}
	
	private void displayCounter() {
		Log.write(getName()+": "+value);
	}
	
	private void incrementCounter() {
		value++;
		String entry = StreamSAKFileHandler.getFileFormattedName(counterFile)+": "+value;
		Log.write(entry);
		write();
		callLink();
	}
	
	private void decrementCounter() {
		value--;
		String entry = StreamSAKFileHandler.getFileFormattedName(counterFile)+": "+value;
		Log.write(entry);
		write();
		callLink();
	}
	
	private void callLink() {
		String link = StreamSAKHandler.getLink(StreamSAKFileHandler.getFileFormattedName(counterFile));
		if(link != null) {
			for(Adjuster a : CountersAdjustersPlugins.getAdjusters())
				if(a.getName().equalsIgnoreCase(link)) {
					a.changeAdjuster();
					return;
				}
			
			for(Plugin p : CountersAdjustersPlugins.getPlugins())
				if(p.getName().equalsIgnoreCase(link)) {
					p.callPlugin();
					return;
				}
		}
	}
	
	private void write() {
		StreamSAKFileHandler.writeToFile(counterFile, value);
	}
	
}
