/*
 * Created on May 29, 2004
 */
 
package org.jalgo.main.gui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.main.gui.TextCanvas;

/**
 * @author Cornelius Hald
 * @author Christopher Friedrich
 */
public class TextViewForm extends CustomViewForm {

	private TextCanvas textCanvas;
	
	public TextViewForm (Composite parent, int style) {
		super(parent, style);
		textCanvas = new TextCanvas(this, SWT.FLAT);
		setContent(textCanvas);
	}
	
	public TextCanvas getTextCanvas() {
		return textCanvas;
	}
}
