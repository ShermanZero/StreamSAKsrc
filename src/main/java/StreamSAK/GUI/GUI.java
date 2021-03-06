package main.java.StreamSAK.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import main.java.StreamSAK.StreamSAK;
import main.java.StreamSAK.GUI.components.Options;
import main.java.StreamSAK.GUI.components.countersadjustersplugins.Adjuster;
import main.java.StreamSAK.GUI.components.countersadjustersplugins.CountersAdjustersPlugins;
import main.java.StreamSAK.GUI.components.countersadjustersplugins.Plugin;
import main.java.StreamSAK.GUI.components.logandinput.LogAndInput;
import main.java.StreamSAK.GUI.components.misc.CustomButton;
import main.java.StreamSAK.GUI.components.misc.CustomMenu;
import main.java.StreamSAK.GUI.components.misc.CustomMenuBar;
import main.java.StreamSAK.GUI.components.misc.CustomVerticalScrollBarUI;
import main.java.StreamSAK.misc.StreamSAKFileHandler;

public class GUI {
	
	public static Font defaultFont = new Font("Tahoma", Font.PLAIN, 12);
	public static Color defaultRedColor = new Color(255, 75, 75);
	public static Color defaultColor = Color.LIGHT_GRAY;
	
	private static JFrame window;

	private static int WIDTH = 700, HEIGHT = 300;
	
	public static void generate() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				window = new JFrame("StreamSAK");
				
				Dimension d = new Dimension(WIDTH, HEIGHT);
				window.setMinimumSize(new Dimension(WIDTH, 275));
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setPreferredSize(d);
				window.setResizable(true);
				
				JPanel main = new JPanel(new BorderLayout());
				main.add(new Options(), BorderLayout.WEST);
				main.add(new LogAndInput(), BorderLayout.CENTER);
				main.add(new CountersAdjustersPlugins(), BorderLayout.EAST);
				main.setBorder(new MatteBorder(0, 1, 1, 1, Color.LIGHT_GRAY));
				
				window.setJMenuBar(generateMenuBar());
				
				window.add(main);
				window.setContentPane(main);
				
				window.pack();
				window.setLocationRelativeTo(null);
				window.setVisible(true);
				window.requestFocus();
			}
		});
	}
	
	public static int getHeight() {
		return HEIGHT;
	}
	
	public static int getWidth() {
		return WIDTH;
	}
	
	public static void setHeight(int height) {
		if(height < 275)
			height = 275;
		
		HEIGHT = height;
		resetWindowSize();
	}
	
	public static void setWidth(int width) {
		if(width < 500)
			width = 500;
		
		WIDTH = width;
		resetWindowSize();
	}
	
	public static void generateNotification(JFrame window, String header, String message, JButton[] buttons) {
		window.setPreferredSize(new Dimension(400, 200));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setAlwaysOnTop(true);
		window.setUndecorated(true);
		
		JPanel main = new JPanel(new BorderLayout());
		main.setBackground(Color.DARK_GRAY);
		main.setBorder(new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		
		JLabel headerLabel = new JLabel(header);
		headerLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerLabel.setBackground(Color.DARK_GRAY);
		headerLabel.setFont(GUI.defaultFont);
		headerLabel.setForeground(Adjuster.adjusterForegroundColor);
		main.add(headerLabel, BorderLayout.NORTH);
		
		JTextArea messageTextArea = new JTextArea(message);
		messageTextArea.setBackground(Color.DARK_GRAY);
		messageTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));
		messageTextArea.setLineWrap(true);
		messageTextArea.setWrapStyleWord(true);
		messageTextArea.setForeground(GUI.defaultColor);
		messageTextArea.setEditable(false);
		messageTextArea.setFocusable(false);
		messageTextArea.setFont(GUI.defaultFont);
		
		JScrollPane scrollPane = new JScrollPane(messageTextArea);
		scrollPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		scrollPane.setBackground(Color.DARK_GRAY);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUI(new CustomVerticalScrollBarUI(Color.DARK_GRAY, Color.WHITE, Color.LIGHT_GRAY));
		
		main.add(scrollPane, BorderLayout.CENTER);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
		
		for(int i = 0; i < buttons.length-1; i++) {
			JButton b = buttons[i];
			b.setBorder(new MatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
		}
		
		for(JButton b : buttons)
			buttonPanel.add(b, gbc);
		
		main.add(buttonPanel, BorderLayout.SOUTH);
		
		window.add(main);
		window.setContentPane(main);
		
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
	private static JMenuBar generateMenuBar() {
		boolean hasPlugins = CountersAdjustersPlugins.getPlugins().size() > 0;
		
		JMenuBar mb = new CustomMenuBar(Color.DARK_GRAY);
		mb.setBorder(new CompoundBorder(new MatteBorder(1, 1, 0, 1, Color.LIGHT_GRAY), new MatteBorder(0, 0, 1, 0, Color.GRAY)));
		
		JButton version = new CustomButton(StreamSAK.getCurrentVersion());
		version.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { StreamSAK.checkForNewVersion(true); }
		});
		
		JButton build = new CustomButton(StreamSAK.getPluginLibraryBuild());
		build.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { StreamSAK.downloadLibraryBuild(); }
		});
		
		JMenu plugins = null;
		if(hasPlugins) {
			plugins = new CustomMenu("Plugins", Plugin.pluginForegroundColor);
			
			for(Plugin p : CountersAdjustersPlugins.getPlugins()) {
				JButton pluginButton = new CustomButton(p.getName()+" "+(String)p.getPlugin().getValue("plugin-data", "version"), Plugin.pluginForegroundColor);
				
				pluginButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) { p.getPlugin().doOnSettingsSelect(); }
				});
				
				plugins.add(pluginButton);
			}
		}
		
		JButton dev = new CustomButton("Support the Developer", Adjuster.adjusterForegroundColor);
		dev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { StreamSAKFileHandler.openURL("https://www.twitch.tv/shermanzero"); }
		});
		
		mb.add(version);
		mb.add(build);
		mb.add(Box.createHorizontalGlue());
		
		if(hasPlugins)
			mb.add(plugins);

		mb.add(dev);
		
		return mb;
	}
	
	private static void resetWindowSize() {
		window.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		window.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		
		window.revalidate();
		window.pack();
	}

	public static class FrameDragListener extends MouseAdapter {

        private final JFrame frame;
        private Point mouseDownCompCoords = null;

        public FrameDragListener(JFrame frame) {
            this.frame = frame;
        }

        public void mouseReleased(MouseEvent e) {
            mouseDownCompCoords = null;
        }

        public void mousePressed(MouseEvent e) {
            mouseDownCompCoords = e.getPoint();
        }

        public void mouseDragged(MouseEvent e) {
            Point currCoords = e.getLocationOnScreen();
            frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
        }
    }
	
}
