package org.jalgo.module.levenshtein.gui.events;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class InputListener implements KeyListener {

	private JTextField inputField;
	
	public InputListener(JTextField inputField) {
		this.inputField = inputField;
	}
	
	public void keyTyped(KeyEvent e) {
		String text = inputField.getText();
		text = text.replaceAll("[^0-9]", "");
		inputField.setText(text);
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		String text = inputField.getText();
		text = text.replaceAll("[^0-9]", "");
		inputField.setText(text);
	}

}
