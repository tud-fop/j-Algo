/*
 * Created on May 29, 2004
 */
 
package org.jalgo.main.gui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Cornelius Hald
 * @author Christopher Friedrich
 */
public class StackViewForm extends CustomViewForm {

	private StackCanvas stackCanvas;

	public StackViewForm(Composite parent, int style) {

		super(parent, style);

		stackCanvas = new StackCanvas(this, SWT.FLAT);
		setContent(stackCanvas);
	}

	public StackCanvas getStackCanvas() {
		return stackCanvas;
	}
}
