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
import main.java.StreamSAK.misc.StreamSAKHandler;
import main.java.StreamSAK.misc.actions.StreamSAKAction;

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
		JButton[] components = { generateButton(), generateChange(), generateDelete() };
		componentCount = components.length;
		return components;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getName() {
		return StreamSAKFileHandler.getFileFormattedName(adjusterFile);
	}
	
	public void resetAdjuster() {
		String entry = StreamSAKFileHandler.getFileFormattedName(adjusterFile)+":";
		Log.write(entry);
		
		value = "";
		write();
	}
	
	public void changeAdjuster() {
		Input.setInputText(StreamSAKFileHandler.getFileData(adjusterFile));
		
		StreamSAKHandler.doOnInput(new StreamSAKAction() {
			public void run() throws Exception {
				String newValue = Input.getLastInput();
				if(newValue.equals(""))
					return;
				
				value = newValue;
				String entry = getName()+": "+value;
				Log.write(entry);
				
				write();
			}
		}, "Adjust "+getName());
	}
	
	public void displayAdjuster() {
		String entry = getName()+": "+value;
		Log.write(entry);
	}
	
	private JButton generateButton() {
		JButton b = new CustomButton(getName(), adjusterForegroundColor, true, true);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { displayAdjuster(); }
		});
		
		b.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) { if(SwingUtilities.isRightMouseButton(evt)) resetAdjuster(); }
		});
		
		return b;
	}
	
	private JButton generateChange() {
		JButton b = new CustomButton("=", Color.LIGHT_GRAY);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { changeAdjuster(); }
		});
		
		return b;
	}
	
	private JButton generateDelete() {
		JButton delete = new CustomButton("x", GUI.defaultRedColor);
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String fileName = adjusterFile.getPath();
				fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1, fileName.lastIndexOf("."));
				
				CountersAdjustersPlugins.deleteAdjuster(fileName);
				StreamSAKFileHandler.removeFile(adjusterFile);
				adjusterFile.delete();
				
				String entry = "Deleted adjuster: "+fileName;
				Log.write(entry);
				return;
			}
		});
		
		return delete;
	}
	
	private void write() {
		StreamSAKFileHandler.writeToFile(adjusterFile, value);
	}

}
