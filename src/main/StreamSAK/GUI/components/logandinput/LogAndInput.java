package main.StreamSAK.GUI.components.logandinput;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class LogAndInput extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public LogAndInput() {
		this.setLayout(new BorderLayout());
		this.setBackground(Color.DARK_GRAY);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		this.add(new Log(), BorderLayout.CENTER);
		this.add(new Input(), BorderLayout.SOUTH);
	}
	
}
