package main.plugins;

public interface AdvancedPlugin extends StreamSAKPlugin {

	public void setInput(String input);
	
	public boolean requiresInput();
	
	public String getInfo();
	
	public String getLogEntry();
	
}
