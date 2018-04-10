package main.StreamSAK.GUI.components.misc;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import main.StreamSAK.GUI.GUI;

public class CustomButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	private static int topBottomMargin = 7;
	private static int leftRightMargin = 10;
	
	private static int componentHeight;

	private Color originalForeground;
	private Color originalBackground;
	
	public CustomButton(String text) {
		super(text);
		setDefaultForeground(GUI.defaultColor);
		setDefaultBackground(Color.DARK_GRAY);
		init(true);
	}
	
	public CustomButton(String text, Color foreground) {
		super(text);
		setDefaultForeground(foreground);
		setDefaultBackground(Color.DARK_GRAY);
		init(true);
	}
	
	public CustomButton(String text, boolean highlightEnabled) {
		super(text);
		init(highlightEnabled);
	}
	
	public static int getComponentHeight() {
		return componentHeight;
	}
	
	public void setDefaultForeground(Color color) {
		originalForeground = color;
		setForeground(originalForeground);
	}
	
	public void setDefaultBackground(Color color) {
		originalBackground = color;
		setBackground(originalBackground);
	}
	
	private void init(boolean highlightEnabled) {
		setFont(GUI.defaultFont);
		setOpaque(true);
		setFocusable(false);
		setBackground(Color.DARK_GRAY);
		setBorder(new EmptyBorder(topBottomMargin, leftRightMargin, topBottomMargin, leftRightMargin));
		
		if(highlightEnabled) {
			addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					setForeground(Color.DARK_GRAY);
					setBackground(Color.WHITE);
				}
	
				public void mouseExited(MouseEvent evt) {
					setForeground(originalForeground);
					setBackground(originalBackground);
				}
			});
		}

		componentHeight = 29;
	}
	
}
