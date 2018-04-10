package main.StreamSAKPluginLibrary.types;

import main.StreamSAKPluginLibrary.StreamSAKPlugin;
import main.StreamSAKPluginLibrary.components.StreamSAKInput;
import main.StreamSAKPluginLibrary.components.StreamSAKLogEntry;

/**
 * 
 * The AdvancedPlugin interface gives the user more functionality compared to its
 * {@link SimplePlugin} counterpart. The interface allows the user to take advantage of StreamSAK's
 * input, as well as its logger.
 * 
 * @author <a href="https://www.twitch.tv/shermanzero">ShermanZero</a>
 * @see #setInput(String)
 * @see #requiresInput()
 * @see #requiresLogEntry()
 * @see #getLogEntry()
 * @see #getInfo()
 *
 */
public abstract class StreamSAKAdvancedPlugin extends StreamSAKSimplePlugin implements StreamSAKPlugin {

	/**
	 * The input from StreamSAK to the plug-in.
	 */
	private StreamSAKInput input;
	
	/**
	 * The log entry.
	 */
	private StreamSAKLogEntry logEntry;
	
	/**
	 * The default constructor sets the name and version of the plug-in.
	 * 
	 * @param name - plug-in name
	 * @param version - plug-in version
	 */
	public StreamSAKAdvancedPlugin(String name, String version) {
		super(name, version);
		
		input = new StreamSAKInput();
		logEntry = new StreamSAKLogEntry();
	}
	
	/**
	 * Returns the input object.
	 * @return StreamSAKInput - input
	 */
	public StreamSAKInput getInput() { return input; }
	
	/**
	 * Returns the log entry object.
	 * @return StreamSAKLogEntry - logEntry
	 */
	public StreamSAKLogEntry getLogEntry() { return logEntry; }
	
	
}
