package main.GUI.components.logandinput;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import main.GUI.GUI;
import main.misc.FileHandler;

public class Log extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static JButton enableAdjusterCall = new JButton("AAC ENABLED");
	private static JList<String> log = new JList<String>();
	private static DefaultListModel<String> model = new DefaultListModel<String>();
	
	public Log() {
		this.setLayout(new BorderLayout());
		this.setBackground(Color.GRAY);
		this.setBorder(new MatteBorder(2, 2, 2, 2, Color.GRAY));
		
		this.add(generateTopPanel(), BorderLayout.NORTH);	
		this.add(generateLogPanel(), BorderLayout.CENTER);
	}
	
	public static void write(String entry) {
		entry = "["+(new SimpleDateFormat("HH:mm:ss").format(new Date()))+"]: "+entry;
		
		FileWriter fw;
		try {
			fw = new FileWriter(FileHandler.findFile("log"), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(entry+(entry.contains("\n") ? "" : "\n"));
			bw.flush();
			bw.close();
		} catch (IOException e) { e.printStackTrace(); }
		
		model.add(0, entry);
		log.setSelectedIndex(0);
	}
	
	public static void clear() {
		model.clear();
		FileHandler.writeToFile(FileHandler.findFile("log"), "");
	}
		
	public static boolean automaticAdjusterCallEnabled() {
		return enableAdjusterCall.getText().toLowerCase().contains("enabled");
	}
	
	private static JPanel generateLogPanel() {
		JPanel main = new JPanel(new BorderLayout());
		main.setBorder(null);
		main.setBackground(null);
		
		log.setBackground(Color.LIGHT_GRAY);
		log.setFont(new Font("Calibri", Font.BOLD, 12));
		log.setFocusable(false);
		log.setModel(model);	
		load();
		
		JScrollPane scroll = new JScrollPane(log);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.setBorder(new CompoundBorder(new MatteBorder(2, 2, 2, 2, Color.DARK_GRAY), new EmptyBorder(5, 5, 5, 5)));
		scroll.setBackground(Color.LIGHT_GRAY);
		
		main.add(scroll, BorderLayout.CENTER);
		return main;
	}

	private static JPanel generateTopPanel() {
		JPanel main = new JPanel(new GridLayout());
		main.setBackground(null);
		main.setBorder(new MatteBorder(2, 2, 0, 2, Color.DARK_GRAY));
		
		enableAdjusterCall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(enableAdjusterCall.getText().contains("ENABLED"))
					enableAdjusterCall.setText("AAC DISABLED");
				else
					enableAdjusterCall.setText("AAC ENABLED");
			}
		});
		enableAdjusterCall.setBackground(Color.LIGHT_GRAY);
		enableAdjusterCall.setBorder(new EmptyBorder(3, 3, 3, 3));
		enableAdjusterCall.setFont(GUI.defaultFont);
		enableAdjusterCall.setFocusable(false);
		main.add(enableAdjusterCall);
		
		JButton link = new JButton("SUPPORT THE DEV");
		link.setBackground(Color.LIGHT_GRAY);
		link.setForeground(new Color(97, 140, 203));
		link.setFocusable(false);
		link.setBorder(null);
		link.setFont(GUI.defaultFont.deriveFont(10f));
		link.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(URI.create("https://www.twitch.tv/shermanzero"));
				} catch (IOException e1) { e1.printStackTrace(); }
			}
		});
		main.add(link);
		
		return main;
	}
	
	private static void load() {
		FileReader fr;
		try {
			fr = new FileReader(FileHandler.findFile("log"));
			BufferedReader br = new BufferedReader(fr);
			
			String line;
			while ((line = br.readLine()) != null)
				model.add(0, line);
			
			br.close();
		} catch (IOException e) { e.printStackTrace(); }
		
		log.setSelectedIndex(0);
	}
	
}
