package main.java.StreamSAK.misc;

import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JButton;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyBinds implements NativeKeyListener {

	private static JButton currentButton;
	private static ActionListener currentAction;
	
	private static boolean waitForKeybind;
	private static int lastKeyPressed;
	private static String lastKeyPressedString;
	
	private final static int escape = 1;
	
	private static Dictionary<String, ActionListener> keybinds;
	
	public KeyBinds() {
		keybinds = new Hashtable<String, ActionListener>();
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()) + e.getKeyCode());
		
		lastKeyPressed = e.getKeyCode();
		lastKeyPressedString = NativeKeyEvent.getKeyText(e.getKeyCode());
		
		if(waitForKeybind) {
			if(lastKeyPressed == escape) {
				keybinds.remove(currentButton.getText());
				currentButton.setText("--");
			} else {
				keybinds.put(lastKeyPressedString, currentAction);
				currentButton.setText(lastKeyPressedString);
			}
		} else {
			ActionListener action = keybinds.get(lastKeyPressedString);
			if(action != null)
				action.actionPerformed(null);
		}
		
		waitForKeybind = false;
	}
	
	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {}
	
	public static void bindKey(JButton button, ActionListener action) {
		currentButton = button;
		currentAction = action;
		
		waitForKeybind = true;
	}
}