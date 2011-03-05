package org.jalgo.module.c0h0.views;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import org.jalgo.module.c0h0.controller.Controller;
import org.jalgo.module.c0h0.controller.ViewManager;

/**
 * displays error messages
 *
 */
public class LogView extends View {
	private static final long serialVersionUID = -6055576254061914463L;

	private Controller controller;
	private TextEditor editor;

	/**
	 * @param controller
	 * @param viewManager
	 */
	public LogView(Controller controller, ViewManager viewManager) {
		this.controller = controller;
		editor = new TextEditor();

		// Aufbau des Grundskeletts
		editor.setContentType("text/html");
		JScrollPane scrollPane = new JScrollPane(editor);
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		doLayout();
	}

	/**
	 * Renders the view.
	 * 
	 * @return success
	 */
	public boolean render() {
		controller.getC0CodeModel();
		return true;
	}

	/**
	 * sets the current error text
	 * @param error text
	 */
	public void setErrorText(String error) {
		editor.setText(error);
	}
}
