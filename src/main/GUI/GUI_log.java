package main.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import main.Action;
import main.Handler;

public class GUI_log {
	
	private static String original;
	public static boolean NO_INPUT = true;
	public static String input = "";
	
	public static JTextField textInput = new JTextField("");
	public static JTextField textInfo = new JTextField("");
	
	private static JButton enableAdjusterCall = new JButton("AAC ENABLED");
	private static Font logFont = new Font("Calibri", Font.BOLD, 12);
	private static JList<String> log = new JList<String>();
	private static DefaultListModel<String> model = new DefaultListModel<String>();
	
	public JPanel generate() {
		JPanel main = new JPanel(new BorderLayout());
		
		log.setBackground(Color.LIGHT_GRAY);
		log.setFont(logFont);
		log.setFocusable(false);
		log.setModel(model);	
		load();
		
		JScrollPane scroll = new JScrollPane(log);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.setBorder(new CompoundBorder(new MatteBorder(2, 2, 2, 2, Color.DARK_GRAY), new EmptyBorder(5, 5, 5, 5)));
		scroll.setBackground(Color.LIGHT_GRAY);
		
		main.add(scroll, BorderLayout.CENTER);
		
		JPanel topPanel = new JPanel(new GridLayout());
		
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
		enableAdjusterCall.setFont(GUI.font);
		enableAdjusterCall.setFocusable(false);
		topPanel.add(enableAdjusterCall);
		
		JButton link = new JButton("SUPPORT THE DEV");
		link.setBackground(Color.LIGHT_GRAY);
		link.setForeground(new Color(97, 140, 203));
		link.setFocusable(false);
		link.setBorder(null);
		link.setFont(GUI.font.deriveFont(10f));
		link.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(URI.create("https://www.twitch.tv/shermanzero"));
				} catch (IOException e1) { e1.printStackTrace(); }
			}
		});
		topPanel.add(link);
		
		topPanel.setBorder(new MatteBorder(2, 2, 0, 2, Color.DARK_GRAY));
		main.add(topPanel, BorderLayout.NORTH);	
		
		JPanel inputPanel = new JPanel(new GridLayout(2, 0));
		
		textInfo.setBackground(Color.GRAY);
		textInfo.setForeground(Color.DARK_GRAY);
		textInfo.setEditable(false);
		textInfo.setFocusable(false);
		textInfo.setFont(GUI.font);
		textInfo.setBorder(new EmptyBorder(5, 10, 5, 10));
		textInfo.setHorizontalAlignment(JTextField.CENTER);
		inputPanel.add(textInfo);
		
		textInput.setBackground(Color.GRAY);
		textInput.setForeground(Color.DARK_GRAY);
		textInput.setCaretColor(Color.DARK_GRAY);
		textInput.setFont(GUI.font);
		textInput.setBorder(new CompoundBorder(new MatteBorder(2, 2, 2, 2, Color.DARK_GRAY), new EmptyBorder(5, 10, 5, 10)));
		textInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				input = textInput.getText().trim();
				NO_INPUT = false;
				
				Handler.disableInput();
			}
		});
		inputPanel.add(textInput);
		
		main.add(inputPanel, BorderLayout.SOUTH);
		main.setBackground(Color.GRAY);
		main.setBorder(new CompoundBorder(new MatteBorder(2, 2, 2, 2, Color.GRAY), new EmptyBorder(5, 5, 5, 5)));
		
		Handler.disableInput();
		
		return main;
	}

	private static void load() {
		FileReader fr;
		try {
			fr = new FileReader(Handler.findFile("log", ""));
			BufferedReader br = new BufferedReader(fr);
			
			String line;
			while ((line = br.readLine()) != null)
				model.add(0, line);
			
			br.close();
		} catch (IOException e) { e.printStackTrace(); }
		
		log.setSelectedIndex(0);
	}
	
	public void write(String entry) {
		entry = "["+(new SimpleDateFormat("HH:mm:ss").format(new Date()))+"]: "+entry;
		
		FileWriter fw;
		try {
			fw = new FileWriter(Handler.findFile("log", ""), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(entry+(entry.contains("\n") ? "" : "\n"));
			bw.flush();
			bw.close();
		} catch (IOException e) { e.printStackTrace(); }
		
		model.add(0, entry);
		log.setSelectedIndex(0);
	}
	
	public void clear() {
		model.clear();
	}
	
	public void adjust(String fileName) {
		Handler.adjustmentFile = fileName;
		
		File f = Handler.findFile(Handler.adjustmentFile, "adjusters");
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
			String line = br.readLine();
			
			textInput.setText(line);
			original = line;
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Handler.doOnInput(new Action() {
			@Override
			public void run() {
				File f = Handler.findFile(Handler.adjustmentFile, "adjusters");
				String newValue = null;
				FileWriter fw;
				try {
					fw = new FileWriter(f, false);
					BufferedWriter bw = new BufferedWriter(fw);
					
					newValue = input;
					
					bw.write(newValue);
					bw.flush();
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}		
				
				String entry = "{"+Handler.getFileName(f)+"}: "+original+" -> "+newValue;
				write(entry);
				
				System.out.println("Succesfully wrote: "+textInput.getText()+" to "+f.getAbsolutePath());
			}
		}, "ADJUST "+fileName);
	}
	
	public boolean adjusterCallEnabled() {
		return enableAdjusterCall.getText().toLowerCase().contains("enabled");
	}
	
}
