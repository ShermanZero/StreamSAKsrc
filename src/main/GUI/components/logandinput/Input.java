package main.GUI.components.logandinput;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import main.GUI.GUI;

public class Input extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static boolean noInput = true;
	public static String lastInput = "";
	
	public static JTextField textInput = new JTextField("");
	public static JTextField textInfo = new JTextField("");
	
	public Input() {
		this.setLayout(new GridLayout(0, 1));
		this.setBackground(Color.GRAY);
		
		textInfo.setBackground(Color.GRAY);
		textInfo.setForeground(Color.DARK_GRAY);
		textInfo.setEditable(false);
		textInfo.setFocusable(false);
		textInfo.setFont(GUI.defaultFont);
		textInfo.setBorder(new EmptyBorder(5, 10, 5, 10));
		textInfo.setHorizontalAlignment(JTextField.CENTER);
		this.add(textInfo);
		
		textInput.setBackground(Color.GRAY);
		textInput.setForeground(Color.DARK_GRAY);
		textInput.setCaretColor(Color.DARK_GRAY);
		textInput.setFont(GUI.defaultFont);
		textInput.setBorder(new CompoundBorder(new MatteBorder(2, 2, 2, 2, Color.DARK_GRAY), new EmptyBorder(5, 10, 5, 10)));
		textInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lastInput = textInput.getText().trim();
				noInput = false;
				
				disableInput();
			}
		});
		
		textInput.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE)
					disableInput();
			}
			@Override
			public void keyReleased(KeyEvent arg0) {}
			@Override
			public void keyTyped(KeyEvent arg0) {}
			
		});
		this.add(textInput);
		
		disableInput();
	}
	
	//return text inside textInput
	public static String getInputText() {
		return textInput.getText();
	}
	
	//returns the last inputted text from textInput
	public static String getLastInput() {
		return lastInput;
	}
	
	public static boolean hasNoInput() {
		return noInput;
	}
	
	public static void setNoInput() {
		noInput = true;
	}
	
	//set the text inside textInput
	public static void setInputText(String text) {
		textInput.setText(text);
	}
	
	//set the info above the textInput
	public static void setInputInfo(String info) {
		textInfo.setText(info);
	}
	
	//allows input
	public static void allowInput(String info) {
		textInput.setBackground(Color.LIGHT_GRAY);
		textInput.setEditable(true);
		textInput.setFocusable(true);
		textInput.requestFocus();
		textInput.selectAll();
		
		textInfo.setText(info);
	}
	
	//prevents input
	public static void disableInput() {
		textInput.setText("");
		textInput.setFocusable(false);
		textInput.setEditable(false);
		textInput.setBackground(Color.GRAY);
		
		textInfo.setText("");
	}
	

}
