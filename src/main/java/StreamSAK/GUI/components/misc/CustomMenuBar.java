package main.java.StreamSAK.GUI.components.misc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JMenuBar;

public class CustomMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	private Color bgColor;
	
	public CustomMenuBar(Color bgColor) {
		super();
		
		this.bgColor = bgColor;
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

}
