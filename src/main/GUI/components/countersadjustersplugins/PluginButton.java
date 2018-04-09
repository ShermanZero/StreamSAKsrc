package main.GUI.components.countersadjustersplugins;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import main.GUI.components.logandinput.Input;
import main.GUI.components.logandinput.Log;
import main.GUI.components.misc.CustomButton;
import main.actions.Action;
import main.misc.Handler;
import main.plugins.AdvancedPlugin;
import main.plugins.SimplePlugin;
import main.plugins.StreamSAKPlugin;

public class PluginButton {

	public static Color pluginForegroundColor = new Color(138, 219, 190);
	
	private StreamSAKPlugin plugin;
	
	public PluginButton(StreamSAKPlugin plugin) {
		this.plugin = plugin;
	}
	
	public JButton[] generate() {
		JButton[] components = {generateButton()};
		return components;
	}
	
	private JButton generateButton() {
		JButton b = new CustomButton(plugin.getName(), pluginForegroundColor);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(plugin instanceof AdvancedPlugin) {
					AdvancedPlugin ap = (AdvancedPlugin)plugin;
					
					if(ap.requiresInput()) {
						Handler.doOnInput(new Action() {
							@Override
							public void run() throws Exception {
								ap.setInput(Input.getLastInput());
								ap.doOnPress();
								Log.write(ap.getLogEntry());
							}
						}, ap.getInfo());
					}
				} else if (plugin instanceof SimplePlugin) {
					SimplePlugin sp = (SimplePlugin)plugin;
					
					sp.doOnPress();
				}
			}
		});
		
		return b;
	}

}
