/*
 * Created on 20.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.main.gui.widgets.Splitter;
import org.jalgo.main.gui.widgets.StyledTextViewForm;

/**
 * @author Michael Pradel
 */
public class NormalViewEbnfGui extends Gui {

	private StyledTextViewForm viewForm; 
	private StyledText ebnfText;
	
	public NormalViewEbnfGui(Composite parent) {
		super(parent);

		Splitter sash = new Splitter(parent, SWT.HORIZONTAL);
		viewForm = new StyledTextViewForm(sash, SWT.BORDER);
		//viewForm.setImage(new Image(parent.getDisplay(), "./pix/new.gif"));
	}
	
	/**
	 * @return
	 */
	public StyledText getEbnfText() {
		return ebnfText;
	}
	
	/**
	 * @param text
	 */
	public void setEbnfText(StyledText text) {
		ebnfText = text;
		viewForm.setContent(ebnfText);
	}

	/**
	 * @return
	 */
	public StyledTextViewForm getViewForm() {
		return viewForm;
	}

}
