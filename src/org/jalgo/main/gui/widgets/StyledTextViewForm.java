/*
 * Created on May 29, 2004
 */
 
package org.jalgo.main.gui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Christopher Friedrich
 */
public class StyledTextViewForm extends CustomViewForm {

	private StyledText styledText;
	
	public StyledTextViewForm (Composite parent, int style) {
		super(parent, style);
		styledText = new StyledText(this, SWT.FLAT);
		setContent(styledText);
	}
	
	public StyledText getStyledText() {
		return styledText;
	}
}
