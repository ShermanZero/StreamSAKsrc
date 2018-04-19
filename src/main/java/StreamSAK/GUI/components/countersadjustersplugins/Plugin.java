package main.java.StreamSAK.GUI.components.countersadjustersplugins;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;

import main.java.StreamSAK.GUI.GUI;
import main.java.StreamSAK.GUI.components.logandinput.Input;
import main.java.StreamSAK.GUI.components.logandinput.Log;
import main.java.StreamSAK.GUI.components.misc.CustomButton;
import main.java.StreamSAK.misc.StreamSAKFileHandler;
import main.java.StreamSAK.misc.StreamSAKFileHandler.Directory;
import main.java.StreamSAK.misc.StreamSAKHandler;
import main.java.StreamSAK.misc.actions.StreamSAKAction;
import main.java.src.StreamSAKPlugin;

public class Plugin {

	public static Color pluginForegroundColor = new Color(138, 219, 190);
	
	private StreamSAKPlugin plugin;
	
	public Plugin(StreamSAKPlugin plugin) {
		this.plugin = plugin;
		plugin.doOnApplicationLoad();
	}
	
	public JButton[] generate() {
		JButton[] components;
		
		if(plugin.hasValue("plugin-properties", "link-allowed"))
			components = new JButton[] { generateLink(), generateButton() };
		else
			components = new JButton[] { generateButton() };
		
		return components;
	}
	
	public String getName() { return (String)plugin.getValue("plugin-data", "name"); }
	
	public StreamSAKPlugin getPlugin() { return plugin; }
	
	public void callPlugin() {
		//if the plug-in uses a link
		if(plugin.hasValue("plugin-properties", "link-allowed")) {
			String fileName = StreamSAKHandler.getLink( getName() );
			if(fileName != null) {
				File f = StreamSAKFileHandler.findFile(fileName, Directory.ADJUSTERS);
				Input.setInputText(StreamSAKFileHandler.getFileData(f));
			}
		}
		
		//if the plug-in requires input
		if(plugin.hasMap("input")) {
			//if the plug-in auto-enters
			if(plugin.hasValue("input", "auto")) {
				Input.enterInput();
				setPluginInput();
				doOnSelect();
			//if the plug-in requires manual input
			} else {
				StreamSAKHandler.doOnInput(new StreamSAKAction() { public void run() throws Exception { 
					setPluginInput();
					doOnSelect();
				} }, (String)plugin.getValue("input", "title"));
			}
		//if the plug-in does not require input
		} else {
			doOnSelect();
		}
	}
	
	private void setPluginInput() {
		plugin.setValue("input", "data", Input.getLastInput());
	}
	
	private void doOnSelect() {
		plugin.doOnSelect();
		
		if(plugin.hasValue("plugin-properties", "log-entry"))
			Log.write((String)plugin.getValue("plugin-properties", "log-entry"));
	}
	
	private JButton generateLink() {
		String link = StreamSAKHandler.getLink( getName() );
		Color startingColor = GUI.defaultColor;
		
		if(link != null) {
			if(StreamSAKFileHandler.findFile(link, Directory.ADJUSTERS) != null)
				startingColor = Adjuster.adjusterForegroundColor;
		}
		
		JButton linkButton = new CustomButton(link == null ? "--" : link, startingColor);
		linkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				linkButton.setText("--");
				
				if(StreamSAKHandler.getLink( getName() ) != null) {
					StreamSAKHandler.removeLink( getName() );
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
								
								if(StreamSAKHandler.setLink(getName(), fileName)) {
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
		JButton b = new CustomButton(getName(), pluginForegroundColor);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { callPlugin(); }
		});
		
		if(plugin.hasValue("plugin-properties", "unselectable"))
			b.setEnabled(false);
		
		return b;
	}

}
