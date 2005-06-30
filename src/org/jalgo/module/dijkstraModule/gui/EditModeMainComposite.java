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
 * Created on 26.05.2005
 *
 
 */
package org.jalgo.module.dijkstraModule.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Group;
import org.jalgo.module.dijkstraModule.gui.EdgeListComposite;
import org.jalgo.module.dijkstraModule.gui.EditModeToolsComposite;
import org.jalgo.module.dijkstraModule.gui.GraphComposite;
import org.jalgo.module.dijkstraModule.gui.MatrixComposite;
import org.jalgo.module.dijkstraModule.gui.NodeListComposite;

/**
 * @author Frank Staudinger
 *
 * The EditModeMainComposite provides the GUI in edit mode
 */
public class EditModeMainComposite extends ControllerComposite {
	
	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstraModule.gui.ControllerComposite
	 */
	public EditModeMainComposite(Controller ctrl,Composite arg0, int arg1) {
		super(ctrl,arg0, arg1);
		
		
		Composite shell = this;
		
		GridLayout l = new GridLayout();
		l.verticalSpacing = 5;
		l.marginHeight = 7;
		l.marginWidth = 7;
		l.numColumns = 2;
		l.makeColumnsEqualWidth = true;
		
		shell.setLayout(l);
		
		Group gr = new Group(shell,SWT.NONE);
		gr.setLayout(new FillLayout());
		gr.setText("Werkzeuge");
		
		GridData g = getButtonGridData(GridData.FILL_HORIZONTAL|GridData.VERTICAL_ALIGN_BEGINNING);
		
		gr.setLayoutData(g);	    	
		Composite cmp = new EditModeToolsComposite(getController(),gr,SWT.NONE);
		
		gr = new Group(shell,SWT.NONE);
		gr.setLayout(new FillLayout());
		gr.setText("Knotenliste");
		gr.setLayoutData(getButtonGridData(GridData.FILL_HORIZONTAL|GridData.VERTICAL_ALIGN_BEGINNING));
		cmp = new NodeListComposite(getController(),gr,SWT.NONE);
		
		
		gr = new Group(shell,SWT.NONE);
		gr.setLayout(new FillLayout());
		gr.setText("Graph");
		
		cmp = new GraphComposite(getController(),gr,SWT.NONE,Display.getCurrent(),true);
		g = getButtonGridData(GridData.FILL_BOTH);
		g.verticalSpan = 2;
		gr.setLayoutData(g);
		
		gr = new Group(shell,SWT.NONE);
		gr.setLayout(new FillLayout());
		gr.setText("Kantenliste");
		
		gr.setLayoutData(getButtonGridData(GridData.FILL_BOTH));
		cmp = new EdgeListComposite(getController(),gr,SWT.NONE);		
		
		gr = new Group(shell,SWT.NONE);
		
		l = new GridLayout();
		l.verticalSpacing = 5;
		l.marginHeight = 5;
		l.marginWidth = 5;
		l.numColumns = 3;
		l.makeColumnsEqualWidth = true;
		
		gr.setLayout(l);
		gr.setLayout(new FillLayout());
		gr.setText("Distanzmatrix");	
		g = getButtonGridData(GridData.FILL_BOTH);
		gr.setLayoutData(g);
		
		
		//MatrixGUI matrix = new MatrixGUI(m_ctrl);			
		//cmp = new ControllerComposite(m_ctrl,gr,SWT.NONE);
		
		cmp = new MatrixComposite(getController(),gr,SWT.NONE);
		g = getButtonGridData(GridData.FILL_BOTH);
		cmp.setLayoutData(g);
		
		cmp = new StatusbarComposite(getController(),this,SWT.NONE,true);
		g = getButtonGridData(GridData.FILL_HORIZONTAL);
		g.horizontalSpan = 2;
		g.heightHint = 75;
		cmp.setLayoutData(g);
		
		getController().setModifiedFlag();
	}
	
	private GridData getButtonGridData(int i)
	{
		GridData g = new GridData(i);	
		return g;
	}	    	
}