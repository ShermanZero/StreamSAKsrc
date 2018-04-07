package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import main.GUI.GUI;
import main.GUI.GUI_log;

public class Counter extends GUI {
	
	private String counterFileName;
	private File counterFile;
	
	private String linkedFileName;
	private File linkedFile;
	
	private boolean update = false;
	
	public Counter(File file) {
		this.counterFile = file;

		String fileName = file.getName();
		if(fileName.contains("--")) {
			counterFileName = fileName.substring(0, fileName.indexOf("--")).toUpperCase();
			
			linkedFile = new File(Handler.adjusterFilePath+"\\"+fileName.substring(fileName.indexOf("--")+2, fileName.lastIndexOf(".")));
			linkedFileName = linkedFile.getName().toUpperCase();
		} else {
			counterFileName = fileName.substring(0, fileName.lastIndexOf(".")).toUpperCase();
		}
	}
	
	public JPanel generate() {
		JPanel main = new JPanel(new GridLayout());
		main.setBackground(Color.DARK_GRAY);
		main.setBorder(null);
		
		main.add(generateMainPanel());
		main.add(generateAddSubtractLinkPanel());		
		return main;
	}
	
	private JPanel generateMainPanel() {
		JPanel main = new JPanel(new BorderLayout());
		main.setBackground(Color.DARK_GRAY);
		main.setBorder(null);
		
		JButton b = new JButton(counterFileName);
		b.setBackground(Color.DARK_GRAY);
		b.setForeground(new Color(224, 217, 172));
		addHighlighting(b);
		b.setBorder(new EmptyBorder(0, 10, 0, 10));
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Handler.displayCounter(Handler.getFileName(counterFile));
			}
		});
		
		b.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				if(SwingUtilities.isRightMouseButton(evt))
					Handler.resetSpecific(Handler.getFileName(counterFile), "counters");
			}
		});
		
		main.add(b, BorderLayout.CENTER);	
		return main;
	}

	private JPanel generateAddSubtractLinkPanel() {
		JPanel addSubtractLink = new JPanel(new GridLayout());
		addSubtractLink.setBackground(Color.DARK_GRAY);
		
		JButton link = generateLink(), down = generateDown(), up = generateUp();
		JButton [] buttons = {up, down, link};
		
		JPanel addSubtract = new JPanel(new GridLayout());
		addSubtract.setBackground(Color.DARK_GRAY);
		
		for(JButton b : buttons) {
			b.setFont(GUI.font);
			b.setBackground(Color.DARK_GRAY);
			addHighlighting(b);
			b.setBorder(new EmptyBorder(0, 5, 0, 5));
		}
		
		addSubtract.add(up);
		addSubtract.add(down);
		addSubtractLink.add(addSubtract);
		addSubtractLink.add(link);
		
		return addSubtractLink;
	}
	
	private JButton generateUp() {
		JButton up = new JButton("+");
		up.setForeground(Color.LIGHT_GRAY);
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				Handler.incrementCounter(Handler.getFileName(counterFile), true);
				
				if(l.adjusterCallEnabled())
					l.adjust(linkedFileName);
			}
		});
		
		return up;
	}
	
	private JButton generateDown() {
		JButton down = new JButton("-");
		down.setForeground(Color.LIGHT_GRAY);
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				Handler.incrementCounter(Handler.getFileName(counterFile), false);
				
				if(l.adjusterCallEnabled())
					l.adjust(linkedFileName);
			}
		});
		
		return down;
	}
	
	private JButton generateLink() {
		JButton link = new JButton(linkedFile == null ? "--" : linkedFileName);
		link.setForeground(new Color(172, 209, 224));
		
		link.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				Handler.doOnInput(new Action() {
					@Override
					public void run() throws Exception {
						if(linkedFileName != null)
							deleteLinkedFile();
						
						String link = GUI_log.input.toLowerCase();
						if(link.equals(""))
							return;
						
						setLinkedFile(link);
						
						restart();
					}
				}, "SET LINKED ADJUSTER");
			}
		});
		
		link.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent evt) {
				if(SwingUtilities.isRightMouseButton(evt)) {
					deleteLinkedFile();
					
					restart();
				}
			}
		});
		
		return link;
	}
	
	private void setLinkedFile(String link) {	
		String filePath = counterFile.getAbsolutePath();
		String newPath = filePath.substring(0, filePath.lastIndexOf("."))+"--"+link+".txt";
		
		File newFile = new File(newPath);
		counterFile.renameTo(newFile);
		
		needToUpdate();
	}
	
	private void deleteLinkedFile() {	
		String filePath = counterFile.getAbsolutePath();
		String newPath = filePath.substring(0, filePath.indexOf("--"))+".txt";
		
		File newFile = new File(newPath);
		counterFile.renameTo(newFile);
		
		needToUpdate();
	}
	
	private void needToUpdate() {
		update = true;
	}
	
	public boolean getUpdate() {
		return update;
	}
	
	private void addHighlighting(JButton b) {
		b.setBorder(null);
		b.setBackground(Color.DARK_GRAY);
		b.setFocusable(false);
		
		b.addMouseListener(new MouseAdapter() {
			Color def = b.getForeground();
			
			public void mouseEntered(MouseEvent evt) {
				b.setBackground(Color.WHITE);
				b.setForeground(Color.DARK_GRAY);
			}

			public void mouseExited(MouseEvent evt) {
				b.setBackground(null);
				b.setForeground(def);
			}
		});
	}

}
