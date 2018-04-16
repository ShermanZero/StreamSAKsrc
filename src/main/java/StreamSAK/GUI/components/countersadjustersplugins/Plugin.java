package main.java.StreamSAK.GUI.components.countersadjustersplugins;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;

import com.shermanzero.StreamSAKPlugin;
import com.shermanzero.types.StreamSAKAdvancedPlugin;
import com.shermanzero.types.StreamSAKSimplePlugin;

import main.java.StreamSAK.GUI.GUI;
import main.java.StreamSAK.GUI.components.logandinput.Input;
import main.java.StreamSAK.GUI.components.logandinput.Log;
import main.java.StreamSAK.GUI.components.misc.CustomButton;
import main.java.StreamSAK.misc.StreamSAKFileHandler;
import main.java.StreamSAK.misc.StreamSAKFileHandler.Directory;
import main.java.StreamSAK.misc.StreamSAKHandler;
import main.java.StreamSAK.misc.actions.StreamSAKAction;

public class Plugin {

	public static Color pluginForegroundColor = new Color(138, 219, 190);
	
	private StreamSAKPlugin plugin;
	
	public Plugin(StreamSAKPlugin plugin) {
		this.plugin = plugin;
		
		plugin.doOnApplicationLoad();
	}
	
	public JButton[] generate() {
		JButton[] components;
		
		if(plugin instanceof StreamSAKAdvancedPlugin && ((StreamSAKAdvancedPlugin)plugin).linkAllowed()) {
			components = new JButton[] { generateLink(), generateButton() };
		} else {
			components = new JButton[] { generateButton() };
		}
		
		return components;
	}
	
	public String getName() {
		return plugin.getName();
	}
	
	public StreamSAKPlugin getPlugin() {
		return plugin;
	}
	
	public void callPlugin() {
		if(plugin instanceof StreamSAKAdvancedPlugin) {
			StreamSAKAdvancedPlugin ap = (StreamSAKAdvancedPlugin)plugin;
			
			if(ap.linkAllowed()) {
				String fileName = StreamSAKHandler.getLink(ap.getName());
				if(fileName != null) {
					File f = StreamSAKFileHandler.findFile(fileName, Directory.ADJUSTERS);
					Input.setInputText(StreamSAKFileHandler.getFileData(f));
				}
			}
			
			//if the plug-in requires input
			if(ap.getInputter().getRequired()) {
				//if the plug-in auto-enters
				if(ap.getInputter().getAutoEnter()) {
					Input.enterInput();
					ap.getInputter().setData(Input.getLastInput());
					
					plugin.doOnSelect();
					
					if(ap.getLogEntrier().getRequired())
						Log.write(ap.getLogEntrier().getEntry());
				//if the plug-in requires manual input
				} else {
					StreamSAKHandler.doOnInput(new StreamSAKAction() { public void run() throws Exception { 
						ap.getInputter().setData(Input.getLastInput());

						plugin.doOnSelect();
						
						if(ap.getLogEntrier().getRequired())
							Log.write(ap.getLogEntrier().getEntry());
					} }, ap.getInputter().getMessage());
				}
			//if the plug-in does not require input
			} else {
				plugin.doOnSelect();

				if(ap.getLogEntrier().getRequired())
					Log.write(ap.getLogEntrier().getEntry());
			}
		} else if (plugin instanceof StreamSAKSimplePlugin) {
			plugin.doOnSelect();
		}
	}
	
	private JButton generateLink() {
		StreamSAKAdvancedPlugin p = (StreamSAKAdvancedPlugin)plugin;
		
		String link = StreamSAKHandler.getLink(p.getName());
		Color startingColor = GUI.defaultColor;
		
		if(link != null) {
			if(StreamSAKFileHandler.findFile(link, Directory.ADJUSTERS) != null)
				startingColor = Adjuster.adjusterForegroundColor;
		}
		
		JButton linkButton = new CustomButton(link == null ? "--" : link, startingColor);
		linkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				linkButton.setText("--");
				
				if(StreamSAKHandler.getLink(p.getName()) != null) {
					StreamSAKHandler.removeLink(p.getName());
					((CustomButton)linkButton).setDefaultForeground(GUI.defaultColor);
				} else {
					StreamSAKHandler.doOnInput(new StreamSAKAction() {
						@Override
						public void run() throws Exception {
							String str = Input.getLastInput();
							if(str.equals(""))
								return;
							
							File f = StreamSAKFileHandler.findFile(str, Directory.ADJUSTERS);
							
							if(f != null) {
								String fileName = StreamSAKFileHandler.getFileFormattedName(f);
								
								if(StreamSAKHandler.setLink(p.getName(), fileName)) {
									linkButton.setText(fileName);
									((CustomButton)linkButton).setDefaultForeground(Adjuster.adjusterForegroundColor);
								}
							}
						}
					}, "Set Linked Call");
				}
			}
		});
		
		return linkButton;
	}
	
	private JButton generateButton() {
		JButton b = new CustomButton(plugin.getName(), pluginForegroundColor);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { callPlugin(); }
		});
		
		return b;
	}

}
