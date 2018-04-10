package main.src.misc.actions;

import main.src.GUI.components.logandinput.Input;

public class ActionThread extends Thread {

	private boolean stopThread = false;
	private Action a;
	
	public ActionThread(Action a) {
		this.a = a;
	}
	
	@Override
	public void run() {
		while(Input.hasNoInput() && !stopThread)
			try { Thread.sleep(100); } catch (Exception e) {}
		
		if(!stopThread)
			try { a.run(); } catch (Exception e) { e.printStackTrace(); }
		
		Input.setNoInput();
	}
	
	public void end() {
		stopThread = true;
	}
	
}
