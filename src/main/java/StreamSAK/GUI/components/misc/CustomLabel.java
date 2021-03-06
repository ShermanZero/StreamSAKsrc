package main.java.StreamSAK.GUI.components.misc;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import main.java.StreamSAK.GUI.GUI;

public class CustomLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	private static int topBottomMargin = 6;
	private static int leftRightMargin = 10;
	
	public CustomLabel(String text) {
		super(text);
	
		setFont(GUI.defaultFont);
		setOpaque(true);
		setFocusable(false);
		setForeground(GUI.defaultColor);
		setBackground(Color.DARK_GRAY);
		setBorder(new EmptyBorder(topBottomMargin, leftRightMargin, topBottomMargin, leftRightMargin));
	}
	

}
