package tests;

import javax.swing.JOptionPane;

import main.plugins.AdvancedPlugin;

public class AdvancedPluginTest implements AdvancedPlugin {

	private String input = "";
	private String version = "v1.0";
	
	@Override
	public String getName() {
		return "AdvancedPlugin";
	}
	
	@Override
	public void doOnPress() {
		JOptionPane.showMessageDialog(null, "Hello from the AdvancedPlugin, you entered:\n"+input, "AdvancedPlugin", JOptionPane.INFORMATION_MESSAGE);
	}
	
	@Override
	public void setInput(String input) {
		this.input = input;
	}
	
	@Override
	public boolean requiresInput() {
		return true;
	}
	
	@Override
	public String getInfo() {
		return "Enter message";
	}
	
	@Override
	public String getLogEntry() {
		return "DISPLAYED: "+input;
	}

	@Override
	public String getVersion() {
		return version;
	}

}
