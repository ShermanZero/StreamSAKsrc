package main.StreamSAKPluginLibrary.types;

import main.StreamSAKPluginLibrary.StreamSAKPlugin;

/**
 * 
 * The root plug-in requirements for a StreamSAK plug-in.  See the {@link SimplePlugin} interface
 * and {@link StreamSAKAdvancedPlugin} interface for developing.
 * 
 * @author <a href="https://www.twitch.tv/shermanzero">ShermanZero</a>
 */
public abstract class StreamSAKSimplePlugin implements StreamSAKPlugin {
	
	/**
	 * The plug-in name.
	 */
	private String name;
	
	/**
	 * The plug-in version.
	 */
	private String version;
	
	/**
	 * The default constructor sets the name and version of the plug-in.
	 * 
	 * @param name - plug-in name
	 * @param version - plug-in version
	 */
	public StreamSAKSimplePlugin(String name, String version) {
		this.name = name;
		this.version = version;
	}
	
	/**
	 * Returns the name of the plug-in as a String.
	 * @return String - plug-in name
	 */
	@Override
	public String getName() { return name; }
	
	/**
	 * Returns the version of the plug-in as a String.
	 * @return String - plug-in version
	 */
	@Override
	public String getVersion() { return version; }
	
	/**
	 * Returns the local build for this plug-in.
	 * @return String - local build
	 */
	@Override
	public String getLocalBuild() { return StreamSAKPlugin.LIBRARY_BUILD; }

	/**
	 * Responsible for the action(s) the plug-in takes when selected.
	 */
	@Override
	public abstract void doOnPress();
	
}
