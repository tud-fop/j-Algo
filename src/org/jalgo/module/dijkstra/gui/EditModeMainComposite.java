/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
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
 * Created on 26.05.2005
 */

package org.jalgo.module.dijkstra.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.jalgo.module.dijkstra.gui.EdgeListComposite;
import org.jalgo.module.dijkstra.gui.EditModeToolsComposite;
import org.jalgo.module.dijkstra.gui.GraphComposite;
import org.jalgo.module.dijkstra.gui.MatrixComposite;
import org.jalgo.module.dijkstra.gui.NodeListComposite;

/**
 * @author Frank Staudinger
 *
 * The EditModeMainComposite provides the GUI in edit mode
 */
public class EditModeMainComposite extends ControllerComposite {

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.gui.ControllerComposite
	 */
	public EditModeMainComposite(Controller ctrl, Composite parent, int style) {
		super(ctrl, parent, style);

		Composite shell = this;

		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 5;
		layout.marginHeight = 7;
		layout.marginWidth = 7;
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = true;

		shell.setLayout(layout);

		Group group = new Group(shell, SWT.NONE);
		group.setLayout(new FillLayout());
		group.setText("Werkzeuge");

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING);

		group.setLayoutData(gridData);
		Composite cmp = new EditModeToolsComposite(getController(),
				group, SWT.NONE);

		group = new Group(shell, SWT.NONE);
		group.setLayout(new FillLayout());
		group.setText("Knotenliste");
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING));
		cmp = new NodeListComposite(getController(), group, SWT.NONE);

		group = new Group(shell, SWT.NONE);
		group.setLayout(new FillLayout());
		group.setText("Graph");

		cmp = new GraphComposite(getController(), group, SWT.NONE,
				Display.getCurrent(), true);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.verticalSpan = 2;
		group.setLayoutData(gridData);

		group = new Group(shell, SWT.NONE);
		group.setLayout(new FillLayout());
		group.setText("Kantenliste");

		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		cmp = new EdgeListComposite(getController(), group, SWT.NONE);

		group = new Group(shell, SWT.NONE);

		layout = new GridLayout();
		layout.verticalSpacing = 5;
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		layout.numColumns = 3;
		layout.makeColumnsEqualWidth = true;

		group.setLayout(layout);
		group.setLayout(new FillLayout());
		group.setText("Distanzmatrix");
		gridData = new GridData(GridData.FILL_BOTH);
		group.setLayoutData(gridData);

		cmp = new MatrixComposite(getController(), group, SWT.NONE);
		gridData = new GridData(GridData.FILL_BOTH);
		// FIXME: with the following commented out, swt 3.1.0 fucks up, find out why (Stephan)
		//cmp.setLayoutData(gridData);

		cmp = new StatusbarComposite(getController(), this, SWT.NONE,
				true);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		gridData.heightHint = 75;
		cmp.setLayoutData(gridData);

		getController().setModifiedFlag();
	}
}
