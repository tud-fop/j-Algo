/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
 *
 * Copyright (C) 2004 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
	private StyledTextViewForm tupleForm;
	private GraphViewForm synDiaForm;

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
