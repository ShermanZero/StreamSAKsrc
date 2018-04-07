package main.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Handler;
import main.StreamSAK;

public class GUI {
	
	public static Font font = new Font("Tahoma", Font.BOLD, 12);
	
	private static final String SUN_JAVA_COMMAND = "sun.java.command";
	private static int WIDTH = 700, HEIGHT;
	
	protected GUI_options_left ol;
	protected GUI_options o;
	protected GUI_log l;
	
	public void generate(GUI_options_left ol, GUI_options o, GUI_log l) {
		this.ol = ol;
		this.o = o;
		this.l = l;
		
		HEIGHT = 50+((Handler.files.size()+2)*25);
		
		JFrame window = new JFrame("StreamSAK "+StreamSAK.VERSION);
		
		Dimension d = new Dimension(WIDTH, HEIGHT);
		window.setMinimumSize(new Dimension(376, 250));
		window.setPreferredSize(d);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setAlwaysOnTop(true);
		
		JPanel main = new JPanel(new BorderLayout());
		main.add(ol.generate(), BorderLayout.WEST);
		main.add(o.generate(), BorderLayout.EAST);
		main.add(l.generate(), BorderLayout.CENTER);
		
		window.add(main);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.requestFocus();
	}

	public void restart() {
		StreamSAK.restart();
		
		/**
		try {
			restartApplication(null);
		} catch (Exception e) { e.printStackTrace(); }
		**/
	}
	
	public GUI_options_left getGUI_ol() {
		return ol;
	}
	
	public GUI_options getGUI_o() {
		return o;
	}
	
	public GUI_log getGUI_l() {
		return l;
	}
	
	public static void addHighlightingToButton(JButton b) {
		b.setBorder(null);
		b.setOpaque(true);
		b.setBackground(Color.DARK_GRAY);
		b.setFocusable(false);
		b.setFont(GUI.font);
		
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

	@SuppressWarnings("unused")
	private void restartApplication(Runnable runBeforeRestart) throws IOException {
		try {
			String java = System.getProperty("java.home") + "/bin/java";
			List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
			StringBuffer vmArgsOneLine = new StringBuffer();
			
			for(String arg : vmArguments) {
				if(!arg.contains("-agentlib")) {
					vmArgsOneLine.append(arg);
					vmArgsOneLine.append(" ");
				}
			}
	
			final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);

			String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");
			
			if(mainCommand[0].endsWith(".jar"))
				cmd.append("-jar " + new File(mainCommand[0]).getPath());
			else
				cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + mainCommand[0]);
			
			for(int i = 1; i < mainCommand.length; i++) {
				cmd.append(" ");
				cmd.append(mainCommand[i]);
			}
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try { Runtime.getRuntime().exec(cmd.toString()); } catch (IOException e) { e.printStackTrace(); }
				}
			});
			
			if(runBeforeRestart!= null) { runBeforeRestart.run(); }
			System.exit(0);
		} catch (Exception e) { throw new IOException("Error while trying to restart the application", e); }
	}
	
}
