package main.StreamSAKPluginLibrary;

public interface StreamSAKPlugin {

	public static final String LIBRARY_BUILD = "0.1.1";
	
	/**
	 * Returns the name of the plug-in as a String.
	 * @return String - plug-in name
	 */
	public abstract String getName();
	
	/**
	 * Returns the version of the plug-in as a String.
	 * @return String - plug-in version
	 */
	public abstract String getVersion();
	
	/**
	 * Returns the build of this interface the plug-in is using.
	 * @return String - plug-in build
	 */
	public abstract String getLocalBuild();
	
	/**
	 * Responsible for the action(s) the plug-in takes when selected.
	 */
	public abstract void doOnPress();

}
