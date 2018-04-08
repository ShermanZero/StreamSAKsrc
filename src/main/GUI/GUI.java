package main.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.StreamSAK;
import main.GUI.components.Options;
import main.GUI.components.countersandadjusters.CountersAndAdjusters;
import main.GUI.logandinput.LogAndInput;

public class GUI {
	
	public static Font defaultFont = new Font("Tahoma", Font.BOLD, 12);
	public static Color defaultRedColor = new Color(255, 75, 75);
	
	private static int WIDTH = 650, HEIGHT = 300;
	
	public static void generate() {
		JFrame window = new JFrame("StreamSAK © 2018 | "+StreamSAK.VERSION);
		
		Dimension d = new Dimension(WIDTH, HEIGHT);
		window.setMinimumSize(new Dimension(376, 200));
		window.setPreferredSize(d);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setAlwaysOnTop(true);
		
		JPanel main = new JPanel(new BorderLayout());
		main.add(new Options(), BorderLayout.WEST);
		main.add(new LogAndInput(), BorderLayout.CENTER);
		main.add(new CountersAndAdjusters(), BorderLayout.EAST);
		
		window.add(main);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.requestFocus();
	}
	
}
