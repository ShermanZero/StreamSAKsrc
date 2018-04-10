package main.StreamSAKPluginLibrary;

public interface StreamSAKPlugin {

	public static final String StreamSAKPluginLibrary_BUILD = "0.1.1";
	
	/**
	 * Returns the name of the plug-in as a String.
	 * @return String - plug-in name
	 */
	public String getName();
	
	/**
	 * Returns the version of the plug-in as a String.
	 * @return String - plug-in version
	 */
	public String getVersion();
	
	/**
	 * Responsible for the action(s) the plug-in takes when selected.
	 */
	public void doOnPress();
	

}
