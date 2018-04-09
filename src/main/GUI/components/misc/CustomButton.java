package main.GUI.components.misc;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import main.GUI.GUI;

public class CustomButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	private static int topBottomMargin = 7;
	private static int leftRightMargin = 10;
	
	private static int componentHeight;
	
	public CustomButton(String text) {
		super(text);
		setForeground(Color.LIGHT_GRAY);
		init(true);
	}
	
	public CustomButton(String text, Color foreground) {
		super(text);
		setForeground(foreground);
		init(true);
	}
	
	public CustomButton(String text, boolean highlightEnabled) {
		super(text);
		init(highlightEnabled);
	}
	
	public static int getComponentHeight() {
		return componentHeight;
	}
	
	private void init(boolean highlightEnabled) {
		setFont(GUI.defaultFont);
		setOpaque(true);
		setFocusable(false);
		setBackground(Color.DARK_GRAY);
		setBorder(new EmptyBorder(topBottomMargin, leftRightMargin, topBottomMargin, leftRightMargin));
		
		if(highlightEnabled) {
			addMouseListener(new MouseAdapter() {
				Color originalForeground = getForeground();
				Color originalBackground = getBackground();
				
				public void mouseEntered(MouseEvent evt) {
					setBackground(Color.WHITE);
					setForeground(Color.DARK_GRAY);
				}
	
				public void mouseExited(MouseEvent evt) {
					setBackground(originalBackground);
					setForeground(originalForeground);
				}
			});
		}

		componentHeight = 29;
	}
	
}
