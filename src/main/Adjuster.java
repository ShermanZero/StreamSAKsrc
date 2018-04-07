package main;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import main.GUI.GUI;

public class Adjuster extends GUI {
	
	private String adjusterFileName;
	private File adjusterFile;
	
	public Adjuster(File file) {
		adjusterFile = file;
		
		String fileName = file.getName();
		adjusterFileName = fileName.substring(0, fileName.lastIndexOf(".")).toUpperCase();
	}
	
	public JPanel generate() {
		JPanel main = new JPanel(new GridLayout());
		main.setBackground(Color.DARK_GRAY);
		main.setBorder(null);
		
		JButton b = new JButton(adjusterFileName);
		b.setBackground(Color.DARK_GRAY);
		b.setForeground(new Color(172, 209, 224));
		addHighlighting(b);
		
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { l.adjust(Handler.getFileName(adjusterFile)); }
		});
		
		b.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				if(SwingUtilities.isRightMouseButton(evt))
					Handler.resetSpecific(Handler.getFileName(adjusterFile), "adjusters");
			}
		});
	
		main.add(b);
		return main;
	}
	
	private void addHighlighting(JButton b) {
		b.setBorder(null);
		b.setBackground(Color.DARK_GRAY);
		b.setFocusable(false);
		
		b.addMouseListener(new MouseAdapter() {
			Color def = b.getForeground();
			
			public void mouseEntered(MouseEvent evt) {
				b.setBackground(Color.WHITE);
				b.setForeground(Color.DARK_GRAY);
			}

			public void mouseExited(MouseEvent evt) {
				b.setBackground(null);
				b.setForeground(def);
			}
		});
	}

}
