/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

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
import org.jalgo.main.util.Messages;

/**
 * @author Michael Pradel
 * @author Christopher Friedrich
 */
public class BackTrackingAlgoGui extends Gui implements Serializable {

	private static final long serialVersionUID = -2967448408740673948L;
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
		synDiaViewForm.setText(Messages.getString("synDiaEBNF",
			"BackTrackingAlgoGui.SynDiaSystem_1")); //$NON-NLS-1$
		
		StackViewForm form2 = new StackViewForm(sash1, SWT.BORDER);
		form2.setText(Messages.getString("synDiaEBNF",
			"BackTrackingAlgoGui.Stack_2")); //$NON-NLS-1$
		stackCanvas = form2.getStackCanvas();
		
		TextViewForm form3 = new TextViewForm(sash2, SWT.BORDER);
		form3.setText(Messages.getString("synDiaEBNF",
			"BackTrackingAlgoGui.Algo_definition_3")); //$NON-NLS-1$
		algoText = form3.getTextCanvas();
		
		TextViewForm form4 = new TextViewForm(sash2, SWT.BORDER);
		form4.setText(Messages.getString("synDiaEBNF",
			"BackTrackingAlgoGui.Generated_word_4")); //$NON-NLS-1$
		word = form4.getTextCanvas();

		sash.setWeights(new int[] { 60, 40 });
		sash1.setWeights(new int[] { 80, 20 });
		sash2.setWeights(new int[] { 60, 40 });
	}

	public TextCanvas getAlgoText() {
		return algoText;
	}

	public Figure getFigure() {
		return figure;
	}

	public TextCanvas getWord() {
		return word;
	}

	public StackCanvas getStackCanvas() {
		return stackCanvas;
	}
	
	public void setFigure(Figure figure) {
		synDiaViewForm.setPanel(figure);
		this.figure = (Figure) synDiaViewForm.getPanel();
	}
}