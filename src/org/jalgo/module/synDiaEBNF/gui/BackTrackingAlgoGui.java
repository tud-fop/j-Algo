/*
 * Created on 03.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gui;

import java.io.Serializable;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.SWT;
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
 */
public class BackTrackingAlgoGui extends Gui implements Serializable {

	private Figure figure;
	private GraphViewForm synDiaViewForm;
	private TextCanvas algoText; // verbal text of the algorithm form script
	private StackCanvas stackCanvas;
	private TextCanvas word;

	/**
	 * Creates a GUI for the backtracking algorithm.
	 * It should include the syntactical diagram, the verbal text of the algorithm
	 * (recognition of a word or generation of a word)
	 * and a stack.
	 *   
	 * @param parent
	 */
	public BackTrackingAlgoGui(Composite parent) {

		super(parent);
		
		Splitter sash = new Splitter(parent, SWT.VERTICAL);
		Splitter sash1 = new Splitter(sash, SWT.HORIZONTAL);
		Splitter sash2 = new Splitter(sash, SWT.HORIZONTAL);

		synDiaViewForm = new GraphViewForm(sash1, SWT.BORDER);
		synDiaViewForm.setText(Messages.getString("BackTrackingAlgoGui.SynDiaSystem_1")); //$NON-NLS-1$
		
		StackViewForm form2 = new StackViewForm(sash1, SWT.BORDER);
		form2.setText(Messages.getString("BackTrackingAlgoGui.Stack_2")); //$NON-NLS-1$
		stackCanvas = form2.getStackCanvas();
		
		TextViewForm form3 = new TextViewForm(sash2, SWT.BORDER);
		form3.setText(Messages.getString("BackTrackingAlgoGui.Algo_definition_3")); //$NON-NLS-1$
		algoText = form3.getTextCanvas();
		
		TextViewForm form4 = new TextViewForm(sash2, SWT.BORDER);
		form4.setText(Messages.getString("BackTrackingAlgoGui.Generated_word_4")); //$NON-NLS-1$
		word = form4.getTextCanvas();

		sash.setWeights(new int[] { 60, 40 });
		sash1.setWeights(new int[] { 80, 20 });
		sash2.setWeights(new int[] { 60, 40 });
	}

	/**
	 * @return
	 */
	public TextCanvas getAlgoText() {
		return algoText;
	}

	/**
	 * @return
	 */
	public Figure getFigure() {
		return figure;
	}

	/**
	 * @return
	 */
	public TextCanvas getWord() {
		return word;
	}

	/**
	 * @return
	 */
	public StackCanvas getStackCanvas() {
		return stackCanvas;
	}
	
	public void setFigure(Figure figure) {
		synDiaViewForm.setPanel(figure);
		this.figure = (Figure) synDiaViewForm.getPanel();
	}

}
