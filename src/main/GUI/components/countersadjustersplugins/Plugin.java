package main.GUI.components.countersadjustersplugins;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import main.AdvancedPlugin;
import main.SimplePlugin;
import main.StreamSAKPlugin;
import main.GUI.components.logandinput.Input;
import main.GUI.components.logandinput.Log;
import main.GUI.components.misc.CustomButton;
import main.actions.Action;
import main.misc.Handler;

public class Plugin {

	public static Color pluginForegroundColor = new Color(138, 219, 190);
	
	private StreamSAKPlugin plugin;
	
	public Plugin(StreamSAKPlugin plugin) {
		this.plugin = plugin;
	}
	
	public JButton[] generate() {
		JButton[] components = { generateButton() };
		return components;
	}
	
	public String getName() {
		return plugin.getName();
	}
	
	public StreamSAKPlugin getPlugin() {
		return plugin;
	}
	
	public void callPlugin() {
		if(plugin instanceof AdvancedPlugin) {
			AdvancedPlugin ap = (AdvancedPlugin)plugin;
			
			if(ap.requiresInput()) {
				Handler.doOnInput(new Action() {
					@Override
					public void run() throws Exception {
						ap.setInput(Input.getLastInput());
						ap.doOnPress();
						
						if(ap.requiresLogEntry())
							Log.write(ap.getLogEntry());
					}
				}, ap.getInfo());
			} else {
				ap.doOnPress();
				
				if(ap.requiresLogEntry())
					Log.write(ap.getLogEntry());
			}
		} else if (plugin instanceof SimplePlugin) {
			SimplePlugin sp = (SimplePlugin)plugin;
			sp.doOnPress();
		}
	}
	
	private JButton generateButton() {
		JButton b = new CustomButton(plugin.getName(), pluginForegroundColor);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { callPlugin(); }
		});
		
		return b;
	}

}
