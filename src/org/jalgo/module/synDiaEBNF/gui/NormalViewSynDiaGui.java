/*
 * Created on 24.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gui;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.main.gui.widgets.GraphViewForm;
import org.jalgo.main.gui.widgets.Splitter;
import org.jalgo.main.gui.widgets.StyledTextViewForm;

/**
 * @author Michael Pradel
 */
public class NormalViewSynDiaGui extends Gui {

	Composite parent;
	StyledTextViewForm tupleForm;
	GraphViewForm synDiaForm;

	public NormalViewSynDiaGui(Composite parent) {
		super(parent);

		Splitter sash = new Splitter(parent, SWT.VERTICAL);
		tupleForm = new StyledTextViewForm(sash, SWT.BORDER);
		synDiaForm = new GraphViewForm(sash, SWT.BORDER);
	}

	public Composite getTupleForm() {
		return tupleForm;
	}

	public void setTuple(StyledText tuple) {
		tupleForm.setContent(tuple);
	}

	public void setSynDia(Figure synDia) {
		synDiaForm.setPanel(synDia);
	}

}
