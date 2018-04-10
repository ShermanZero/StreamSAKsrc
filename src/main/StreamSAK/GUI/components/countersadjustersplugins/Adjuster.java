package main.StreamSAK.GUI.components.countersadjustersplugins;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import main.StreamSAK.GUI.components.logandinput.Input;
import main.StreamSAK.GUI.components.logandinput.Log;
import main.StreamSAK.GUI.components.misc.CustomButton;
import main.StreamSAK.misc.Handler;
import main.StreamSAK.misc.StreamSAKFileHandler;
import main.StreamSAK.misc.actions.Action;

public class Adjuster {
	public static Color adjusterForegroundColor = new Color(172, 209, 224);
	
	public static int componentCount;
	
	private String value;
	private final File adjusterFile;
	
	public Adjuster(File f) {
		adjusterFile = f;
		value = StreamSAKFileHandler.getFileData(adjusterFile);
	}
	
	public JButton[] generate() {
		JButton[] components = {generateButton(), generateChange()};
		componentCount = components.length;
		return components;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getName() {
		return StreamSAKFileHandler.getFileFormattedName(adjusterFile).toUpperCase();
	}
	
	public void resetAdjuster() {
		String entry = StreamSAKFileHandler.getFileFormattedName(adjusterFile).toUpperCase()+" RESET";
		Log.write(entry);
		
		value = "";
		write();
	}
	
	public void changeAdjuster() {
		Input.setInputText(StreamSAKFileHandler.getFileData(adjusterFile));
		
		Handler.doOnInput(new Action() {
			public void run() throws Exception {
				String newValue = Input.getLastInput();
				if(newValue.equals(""))
					return;
				
				value = newValue;
				String entry = getName()+": "+value;
				Log.write(entry);
				
				write();
			}
		}, "ADJUST "+getName());
	}
	
	public void displayAdjuster() {
		String entry = getName()+": "+value;
		Log.write(entry);
	}
	
	private JButton generateChange() {
		JButton b = new CustomButton("=", Color.LIGHT_GRAY);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { changeAdjuster(); }
		});
		
		return b;
	}
	
	private JButton generateButton() {
		JButton b = new CustomButton(getName(), adjusterForegroundColor);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { displayAdjuster(); }
		});
		
		b.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) { if(SwingUtilities.isRightMouseButton(evt)) resetAdjuster(); }
		});
		
		return b;
	}
	
	private void write() {
		StreamSAKFileHandler.writeToFile(adjusterFile, value);
	}

}
