package org.jalgo.module.c0h0.views;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JScrollPane;

import org.jalgo.module.c0h0.controller.Controller;
import org.jalgo.module.c0h0.controller.InterfaceConstants;
import org.jalgo.module.c0h0.controller.ViewManager;

/**
 * View that provides a textEditor for entering C0 code
 * 
 * @author hendrik
 * 
 */
public class EditView extends View {
	private static final long serialVersionUID = -3224880580661414522L;
	private TextEditor textEditor;
	private Font font;
	private Controller controller;
	private ViewManager viewManager;

	public EditView(Controller con, ViewManager viewManager) {
		controller = con;
		this.viewManager = viewManager;
		textEditor = new TextEditor();
		JScrollPane scrollPane = new JScrollPane(textEditor);
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		doLayout();
		textEditor.setContentType("text/c");
	}

	/**
	 * Facade for texteditors setDot()
	 * 
	 * @param line
	 */
	public void setCaret(int line) {
		textEditor.getCaret().setDot(line);
		textEditor.requestFocus();
	}

	/**
	 * returns the c0Code from textpane
	 * @return the c0Code
	 */
	public String getC0Code() {
		return textEditor.getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.c0h0.views.View#update()
	 */
	public boolean update() {
		textEditor.setText(controller.getC0CodeModel().getCodeForm());
		if (viewManager.isBeamerMode())
			font = new Font("Courier New", Font.PLAIN, InterfaceConstants.BEAMERMODE_FONTSIZE);
		else font = new Font("Courier New", Font.PLAIN, InterfaceConstants.NORMAL_FONTSIZE);
		textEditor.setFont(font);
		return true;
		// textEditor.setText(controller.getC0Code().highlightEditorText());

	}
}
