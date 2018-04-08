package main.GUI.components.countersandadjusters;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import main.GUI.components.misc.CustomButton;
import main.GUI.logandinput.Input;
import main.GUI.logandinput.Log;
import main.actions.Action;
import main.misc.FileHandler;
import main.misc.Handler;

public class Adjuster {
	public static Color adjusterForegroundColor = new Color(172, 209, 224);
	
	private String value;
	private final File adjusterFile;
	
	public Adjuster(File f) {
		adjusterFile = f;
		value = FileHandler.getFileData(adjusterFile);
	}
	
	public JButton[] generate() {
		JButton[] buttons = {generateButton()};
		return buttons;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getName() {
		return FileHandler.getFileFormattedName(adjusterFile).toUpperCase();
	}
	
	public void resetAdjuster() {
		String entry = FileHandler.getFileFormattedName(adjusterFile).toUpperCase()+" RESET";
		Log.write(entry);
		
		value = "";
		write();
	}
	
	public void changeAdjuster() {
		Input.setInputText(FileHandler.getFileData(adjusterFile));
		
		Handler.doOnInput(new Action() {
			public void run() throws Exception {
				String newValue = Input.getLastInput();
				if(newValue.equals(""))
					return;
				
				String entry = getName()+": "+value+" -> "+newValue;
				Log.write(entry);
				
				value = newValue;
				write();
			}
		}, "ADJUST "+getName());
	}
	
	private JButton generateButton() {
		JButton b = new CustomButton(getName(), adjusterForegroundColor);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { changeAdjuster(); }
		});
		
		b.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) { if(SwingUtilities.isRightMouseButton(evt)) resetAdjuster(); }
		});
		
		return b;
	}
	
	private void write() {
		FileHandler.writeToFile(adjusterFile, value);
	}
	

}
