/*
 * Created on 02.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gui;

import java.io.Serializable;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.main.gui.TextCanvas;
import org.jalgo.main.gui.widgets.GraphViewForm;
import org.jalgo.main.gui.widgets.Splitter;
import org.jalgo.main.gui.widgets.StackCanvas;
import org.jalgo.main.gui.widgets.StackViewForm;
import org.jalgo.main.gui.widgets.TextViewForm;

/**
 * @author Michael Pradel
 * @author Christopher Friedrich
 *
 */
public class TransAlgorithmGui extends Gui  implements Serializable{
	
	private IFigure figure;  // mixture of ebnf-terms and syndia
	private TextCanvas textCanvas;  // verbal algorithm form script
	private StackCanvas stackCanvas;
	
	public TransAlgorithmGui(Composite parent) {

		super(parent);
		
		Splitter sash = new Splitter(parent, SWT.VERTICAL);
		Splitter sash1 = new Splitter(sash, SWT.HORIZONTAL);

		GraphViewForm form1 = new GraphViewForm(sash1, SWT.BORDER);
		form1.setText(Messages.getString("TransAlgorithmGui.translation_1")); //$NON-NLS-1$
		form1.setImage(new Image(parent.getDisplay(), "./pix/new.gif")); //$NON-NLS-1$
		figure = form1.getPanel();
		
		StackViewForm form2 = new StackViewForm(sash1, SWT.BORDER);
		form2.setText(Messages.getString("TransAlgorithmGui.Rest_variables_3")); //$NON-NLS-1$
		stackCanvas = form2.getStackCanvas();
		
		TextViewForm form3 = new TextViewForm(sash, SWT.BORDER);
		form3.setText(Messages.getString("TransAlgorithmGui.Algo_definition_4")); //$NON-NLS-1$
		form3.setImage(new Image(parent.getDisplay(), "./pix/new.gif")); //$NON-NLS-1$
		textCanvas = form3.getTextCanvas();

		sash.setWeights(new int[] { 60, 40 });
		sash1.setWeights(new int[] { 80, 20 });
	}
	
	public IFigure getFigure() {
		return figure;
	}
	
	public TextCanvas getText() {
		return textCanvas;
	}
	
	public StackCanvas getStackCanvas() {
		return stackCanvas;
	}
	
}
