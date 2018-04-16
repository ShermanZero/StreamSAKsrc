package main.java.StreamSAK.GUI.components.misc;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import main.java.StreamSAK.GUI.GUI;

public class CustomMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	private static int topBottomMargin = 6;
	private static int leftRightMargin = 12;
	
	private static int componentHeight = 27;

	private Color originalForeground;
	private Color originalBackground;
	
	public CustomMenu(String text) {
		super(text);
		setDefaultForeground(GUI.defaultColor);
		setDefaultBackground(Color.DARK_GRAY);
		init(true, false);
	}
	
	public CustomMenu(String text, Color foreground) {
		super(text);
		setDefaultForeground(foreground);
		setDefaultBackground(Color.DARK_GRAY);
		init(true, false);
	}
	
	public CustomMenu(String text, Color foreground, boolean highlightEnabled) {
		super(text);
		setDefaultForeground(foreground);
		setDefaultBackground(Color.DARK_GRAY);
		init(highlightEnabled, false);
	}
	
	public CustomMenu(String text, Color foreground, boolean highlightEnabled, boolean rightClickEnabled) {
		super(text);
		setDefaultForeground(foreground);
		setDefaultBackground(Color.DARK_GRAY);
		init(highlightEnabled, rightClickEnabled);
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
	
	private void init(boolean highlightEnabled, boolean rightClickEnabled) {
		setFont(GUI.defaultFont);
		setOpaque(true);
		setFocusable(false);
		setBackground(Color.DARK_GRAY);
		setBorder(new EmptyBorder(topBottomMargin, leftRightMargin, topBottomMargin, leftRightMargin));
		
		if(highlightEnabled) {
			Color highlightColor = Color.WHITE;
			
			addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent evt) {
					setForeground(Color.DARK_GRAY);
					setBackground(highlightColor);
				}
	
				public void mouseExited(MouseEvent evt) {
					setForeground(originalForeground);
					setBackground(originalBackground);
				}
				
				public void mousePressed(MouseEvent evt) {
					if(rightClickEnabled && SwingUtilities.isRightMouseButton(evt))
						setBackground(GUI.defaultRedColor);
				}
				
				public void mouseReleased(MouseEvent evt) {
					setBackground(highlightColor);
				}
			});
		}
	}
	
}