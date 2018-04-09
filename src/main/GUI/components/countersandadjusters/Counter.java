package main.GUI.components.countersandadjusters;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import main.GUI.components.logandinput.Input;
import main.GUI.components.logandinput.Log;
import main.GUI.components.misc.CustomButton;
import main.actions.Action;
import main.misc.FileHandler;
import main.misc.Handler;
import main.misc.FileHandler.Directory;

public class Counter {
	public static Color counterForegroundColor = new Color(224, 217, 172);
	
	public static int componentCount;
	
	private final File counterFile;
	private int value;
	
	public Counter(File f) {
		counterFile = f;
		value = Integer.parseInt(FileHandler.getFileData(counterFile));
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return FileHandler.getFileFormattedName(counterFile).toUpperCase();
	}

	public JButton[] generate() {
		JButton[] components = {generateCounter(), generateUp(), generateDown(), generateLink()};
		componentCount = components.length;
		return components;
	}
	
	public void resetCounter() {
		String entry = FileHandler.getFileFormattedName(counterFile).toUpperCase()+" RESET";
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
		String link = Handler.getAdjusterLink(counterFile);
		
		JButton linkButton = new CustomButton(link == null ? "--" : link.toUpperCase(), Adjuster.adjusterForegroundColor);
		linkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				linkButton.setText("--");
				
				if(Handler.getAdjusterLink(counterFile) != null) {
					Handler.removeAdjusterLink(counterFile);
				} else {
					Handler.doOnInput(new Action() {
						@Override
						public void run() throws Exception {
							String str = Input.getLastInput();
							if(str.equals(""))
								return;
							
							if(Handler.setAdjusterLink(counterFile, FileHandler.findFile(str, Directory.ADJUSTERS)))
								linkButton.setText(str.toUpperCase());
						}
					}, "SET LINKED ADJUSTER");
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
		String entry = FileHandler.getFileFormattedName(counterFile).toUpperCase()+": "+value;
		Log.write(entry);
		write();
		callAdjuster();
	}
	
	private void decrementCounter() {
		value--;
		String entry = FileHandler.getFileFormattedName(counterFile).toUpperCase()+": "+value;
		Log.write(entry);
		write();
		callAdjuster();
	}
	
	private void callAdjuster() {
		if(!Log.automaticAdjusterCallEnabled())
			return;
		
		String adjusterLink = Handler.getAdjusterLink(counterFile);
		if(adjusterLink != null)
			for(Adjuster a : CountersAndAdjusters.getAdjusters())
				if(a.getName().equalsIgnoreCase(adjusterLink)) {
					a.changeAdjuster();
					break;
				}
	}
	
	private void write() {
		FileHandler.writeToFile(counterFile, value);
	}
	
}
