package main;

import javax.swing.JOptionPane;

import main.SimplePlugin;

public class SimplePluginTest implements SimplePlugin {

	@Override
	public String getName() {
		return "SimplePlugin";
	}

	@Override
	public void doOnPress() {
		JOptionPane.showMessageDialog(null, "Hello from the SimplePlugin", "SimplePlugin", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public String getVersion() {
		return "v1.0";
	}

}
