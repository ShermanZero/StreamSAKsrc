package main.GUI;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import main.Adjuster;
import main.Counter;
import main.Handler;

public class GUI_options {
	
	public GUI_options() {
		
	}
	
	public JPanel generate() {
		JPanel main = new JPanel(new GridLayout(0, 1));
		
		for(File f : Handler.files)
			if(f.getPath().contains("counters"))
				main.add(createCounterButtons(f));
		
		for(File f : Handler.files)
			 if(f.getPath().contains("adjusters"))
				main.add(createAdjusterButtons(f));
		
		JButton clear = new JButton("CLEAR LOG");
		clear.setForeground(new Color(255, 100, 100));
		addHighlighting(clear);
		
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { Handler.clearLog(); }
		});
		
		main.add(new JLabel());
		main.add(clear);
		
		main.setBackground(Color.DARK_GRAY);
		main.setBorder(new CompoundBorder(new MatteBorder(2, 2, 2, 2, Color.GRAY), new EmptyBorder(5, 5, 5, 5)));
		
		return main;
	}
	
	private JPanel createCounterButtons(File f) {
		Counter c = new Counter(f);
		return c.generate();
	}
	
	private JPanel createAdjusterButtons(File f) {
		Adjuster a = new Adjuster(f);
		return a.generate();
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
