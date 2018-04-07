package main;

import main.GUI.GUI_log;

public class ActionThread extends Thread {

	private boolean stopThread = false;
	private Action a;
	
	public ActionThread(Action a) {
		this.a = a;
	}
	
	@Override
	public void run() {
		while(GUI_log.NO_INPUT && !stopThread)
			try { Thread.sleep(100); } catch (Exception e) {}
		
		if(!stopThread)
			try { a.run(); } catch (Exception e) { e.printStackTrace(); }
		
		GUI_log.NO_INPUT = true;
	}
	
	public void end() {
		stopThread = true;
	}
	
}
