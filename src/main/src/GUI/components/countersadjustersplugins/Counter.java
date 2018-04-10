package main.src.GUI.components.countersadjustersplugins;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import main.src.GUI.GUI;
import main.src.GUI.components.logandinput.Input;
import main.src.GUI.components.logandinput.Log;
import main.src.GUI.components.misc.CustomButton;
import main.src.misc.Handler;
import main.src.misc.StreamSAKFileHandler;
import main.src.misc.StreamSAKFileHandler.Directory;
import main.src.misc.actions.Action;

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
		return StreamSAKFileHandler.getFileFormattedName(counterFile).toUpperCase();
	}

	public JButton[] generate() {
		JButton[] components = { generateCounter(), generateUp(), generateDown(), generateLink() };
		componentCount = components.length;
		return components;
	}
	
	public void resetCounter() {
		String entry = StreamSAKFileHandler.getFileFormattedName(counterFile).toUpperCase()+" RESET";
		Log.write(entry);
		
		value = 0;
		write();
	}
	
	private JButton generateCounter() {
		JButton b = new CustomButton(getName(), counterForegroundColor);
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
		String link = Handler.getLink(counterFile);
		Color startingColor = GUI.defaultColor;
		Plugin p = null;
		if(link != null) {
			link = link.toUpperCase();
			
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
				
				if(Handler.getLink(counterFile) != null) {
					Handler.removeLink(counterFile);
					((CustomButton)linkButton).setDefaultForeground(GUI.defaultColor);
				} else {
					Handler.doOnInput(new Action() {
						@Override
						public void run() throws Exception {
							String str = Input.getLastInput();
							if(str.equals(""))
								return;
							
							Plugin p;
							if(Handler.setLink(counterFile, StreamSAKFileHandler.findFile(str, Directory.ADJUSTERS))) {
								linkButton.setText(str.toUpperCase());
								((CustomButton)linkButton).setDefaultForeground(Adjuster.adjusterForegroundColor);
							} else
							if((p = StreamSAKFileHandler.findPlugin(str)) != null && Handler.setLink(counterFile, str)) {
								linkButton.setText(p.getName());
								((CustomButton)linkButton).setDefaultForeground(Plugin.pluginForegroundColor);
							}
						}
					}, "SET LINKED CALL");
				}
			}
		});
		
		return linkButton;
	}
	
	private void displayCounter() {
		Log.write(getName()+": "+value);
	}
	
	private void incrementCounter() {
		value++;
		String entry = StreamSAKFileHandler.getFileFormattedName(counterFile).toUpperCase()+": "+value;
		Log.write(entry);
		write();
		callLink();
	}
	
	private void decrementCounter() {
		value--;
		String entry = StreamSAKFileHandler.getFileFormattedName(counterFile).toUpperCase()+": "+value;
		Log.write(entry);
		write();
		callLink();
	}
	
	private void callLink() {
		if(!Log.automaticCallEnabled())
			return;
		
		String link = Handler.getLink(counterFile);
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
