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
