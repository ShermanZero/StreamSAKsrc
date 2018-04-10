package main.StreamSAKPlugins;

import javax.swing.JOptionPane;

import main.StreamSAKPluginLibrary.types.StreamSAKSimplePlugin;

public class SimplePluginExample extends StreamSAKSimplePlugin {

	public SimplePluginExample() {
		super("SimplePlugin", "v1.0");
	}

	@Override
	public void doOnPress() {
		JOptionPane.showMessageDialog(null, "Hello from the SimplePlugin", "SimplePlugin", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void main(String [] args) {
		new SimplePluginExample().display();
	}
	
	public void display() {
		System.out.println(getLocalBuild());
	}
	
}
