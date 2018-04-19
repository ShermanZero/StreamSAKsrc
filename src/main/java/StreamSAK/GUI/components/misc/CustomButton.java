package main.java.StreamSAK.GUI.components.misc;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import main.java.StreamSAK.GUI.GUI;

public class CustomButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	private static int topBottomMargin = 6;
	private static int leftRightMargin = 12;
	
	private static int componentHeight = 27;

	private Color originalForeground;
	private Color originalBackground;
	
	public CustomButton(String text) {
		super(text);
		setDefaultForeground(GUI.defaultColor);
		setDefaultBackground(Color.DARK_GRAY);
		init(true, false);
	}
	
	public CustomButton(String text, Color foreground) {
		super(text);
		setDefaultForeground(foreground);
		setDefaultBackground(Color.DARK_GRAY);
		init(true, false);
	}
	
	public CustomButton(String text, Color foreground, boolean highlightEnabled) {
		super(text);
		setDefaultForeground(foreground);
		setDefaultBackground(Color.DARK_GRAY);
		init(highlightEnabled, false);
	}
	
	public CustomButton(String text, Color foreground, boolean highlightEnabled, boolean rightClickEnabled) {
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
				boolean mouseIn = false;
				
				public void mouseEntered(MouseEvent evt) {
					if(!isEnabled())
						return;
					
					setForeground(Color.DARK_GRAY);
					setBackground(highlightColor);
					
					mouseIn = true;
				}
	
				public void mouseExited(MouseEvent evt) {
					if(!isEnabled())
						return;
					
					setForeground(originalForeground);
					setBackground(originalBackground);
					
					mouseIn = false;
				}
				
				public void mousePressed(MouseEvent evt) {
					if(!isEnabled())
						return;
					
					if(rightClickEnabled && SwingUtilities.isRightMouseButton(evt))
						setBackground(GUI.defaultRedColor);
				}
				
				public void mouseReleased(MouseEvent evt) {
					if(!isEnabled())
						return;
					
					if(mouseIn)
						setBackground(highlightColor);
					else
						setBackground(originalBackground);
				}
			});
		}
	}
	
}
