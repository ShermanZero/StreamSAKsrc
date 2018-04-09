package main.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.StreamSAK;
import main.GUI.components.Options;
import main.GUI.components.countersadjustersplugins.CountersAdjustersPlugins;
import main.GUI.components.logandinput.LogAndInput;

public class GUI {
	
	public static Font defaultFont = new Font("Tahoma", Font.PLAIN, 12);
	public static Color defaultRedColor = new Color(255, 75, 75);
	
	private static int WIDTH = 650, HEIGHT = 300;
	
	private static JFrame window;
	
	public static void generate() {
		window = new JFrame("StreamSAK "+StreamSAK.VERSION);
		
		Dimension d = new Dimension(WIDTH, HEIGHT);
		window.setMinimumSize(new Dimension(376, 200));
		window.setPreferredSize(d);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel main = new JPanel(new BorderLayout());
		main.add(new Options(), BorderLayout.WEST);
		main.add(new LogAndInput(), BorderLayout.CENTER);
		main.add(new CountersAdjustersPlugins(), BorderLayout.EAST);
		
		window.add(main);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.requestFocus();
	}
	
	public static int getHeight() {
		return HEIGHT;
	}
	
	public static int getWidth() {
		return WIDTH;
	}
	
	public static void setHeight(int height) {
		HEIGHT = height;
		resetWindowSize();
	}
	
	public static void setWidth(int width) {
		WIDTH = width;
		resetWindowSize();
	}
	
	private static void resetWindowSize() {
		window.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		window.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		window.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		window.revalidate();
		window.pack();
	}

}
