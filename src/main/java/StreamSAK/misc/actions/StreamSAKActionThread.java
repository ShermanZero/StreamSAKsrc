package main.java.StreamSAK.misc.actions;

import main.java.StreamSAK.GUI.components.logandinput.Input;

public class StreamSAKActionThread extends Thread {

	private boolean stopThread = false;
	private StreamSAKAction a;
	
	public StreamSAKActionThread(StreamSAKAction a) {
		this.a = a;
	}
	
	@Override
	public void run() {
		while(Input.hasNoInput() && !stopThread)
			try { Thread.sleep(100); } catch (Exception e) {}
		
		if(!stopThread)
			try { a.run(); } catch (Exception e) { e.printStackTrace(); }
	}
	
	public void end() {
		stopThread = true;
	}
	
}
