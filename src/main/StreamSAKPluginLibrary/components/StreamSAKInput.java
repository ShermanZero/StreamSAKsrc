package main.StreamSAKPluginLibrary.components;

public class StreamSAKInput {

	/**
	 * The input data.
	 */
	private String data;
	
	/**
	 * The message to display above the input field.
	 */
	private String message;
	
	/**
	 * If input is required or not.
	 */
	private boolean required;
	
	/**
	 * Set the input data (called from StreamSAK).
	 * @param data - the user input
	 */
	public void setData(String data) { this.data = data; }
	
	/**
	 * Set the message to display above the input field.
	 * @param message - message above input field
	 */
	public void setMessage(String message) { this.message = message; }
	
	/**
	 * Set whether input is required.
	 * @param required - input required
	 */
	public void setRequired(boolean required) { this.required = required; }
	
	/**
	 * Returns the user-given input.
	 * @return String - the input given by the user
	 */
	public String getData() { return data; }
	
	/**
	 * Returns the message to display above the input field (called from StreamSAK).
	 * @return String - message above input field
	 */
	public String getMessage() { return message; }
	
	/**
	 * Returns whether or not the input is required (called from StreamSAK).
	 * @return boolean - input required
	 */
	public boolean getRequired() { return required; }
	
}
