package main.StreamSAKPluginLibrary.components;

public class StreamSAKLogEntry {
	
	/**
	 * The log entry.
	 */
	private String entry;
	
	/**
	 * If an entry is required or not.
	 */
	private boolean required;
	
	/**
	 * Sets the log entry to display when the plug-in finishes executing.
	 * @param entry - log entry
	 */
	public void setEntry(String entry) { this.entry = entry; }
	
	/**
	 * Set whether a log entry is required.
	 * @param required - log entry required
	 */
	public void setRequired(boolean required) { this.required = required; }
	
	/**
	 * Returns the log entry to display (called from StreamSAK).
	 * @return String - log entry
	 */
	public String getEntry() { return entry; }
	
	/**
	 * Set whether the plug-in should display a log entry after completing.
	 * @return boolean - whether or not to display an entry
	 */
	public boolean getRequired() { return required; }

}
