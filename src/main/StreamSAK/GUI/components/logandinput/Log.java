package main.StreamSAK.GUI.components.logandinput;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

import main.StreamSAK.GUI.GUI;
import main.StreamSAK.GUI.components.misc.CustomButton;
import main.StreamSAK.misc.StreamSAKFileHandler;

public class Log extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static JButton clearLog = new CustomButton("Clear Log", GUI.defaultRedColor);
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
		System.out.println(entry);
		
		entry = "["+(new SimpleDateFormat("HH:mm:ss").format(new Date()))+"]: "+entry;
		
		FileWriter fw;
		try {
			fw = new FileWriter(StreamSAKFileHandler.findFile("log"), true);
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
		StreamSAKFileHandler.writeToFile(StreamSAKFileHandler.findFile("log"), "");
	}
		
	private static JPanel generateLogPanel() {
		JPanel main = new JPanel(new BorderLayout());
		main.setBackground(null);
		
		log.setBackground(Color.LIGHT_GRAY);
		log.setFont(new Font("Tahoma", Font.BOLD, 11));
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
		
		clearLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { Log.clear(); }
		});
		main.add(clearLog);
		
		return main;
	}
	
	private static void load() {
		FileReader fr;
		try {
			fr = new FileReader(StreamSAKFileHandler.findFile("log"));
			BufferedReader br = new BufferedReader(fr);
			
			String line;
			while ((line = br.readLine()) != null)
				model.add(0, line);
			
			br.close();
		} catch (IOException e) { e.printStackTrace(); }
		
		log.setSelectedIndex(0);
	}
	
}
