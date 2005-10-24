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

/**
 * @author Frank Staudinger
 * Created on 30.05.2005 10:10:30
 *
 */
package org.jalgo.module.dijkstra.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Group;

import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.gui.ControllerComposite;


/**
 * @author Frank Staudinger
 *
 * This class is the main composite in the algorothm-mode it compine the other
 * "basic" composites to a comfortable GUI 
 */
public class AlgorithmModeMainComposite extends ControllerComposite
{
	
	/**
	 * @param ctrl Controller object for this composite
	 * @param arg0 Parent composite
	 * @param arg1 Style for this composite
	 */
	public AlgorithmModeMainComposite(Controller ctrl, Composite arg0, int arg1)
	{
		super(ctrl, arg0, arg1);
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
		
		GridData g = new GridData(GridData.FILL_HORIZONTAL|GridData.VERTICAL_ALIGN_BEGINNING);
		g.horizontalSpan = 2;
		gr.setLayoutData(g);	    	
		Composite cmp = new AlgorithmModeToolsComposite(getController(),gr,SWT.NONE);
		
		
		gr = new Group(shell,SWT.SHADOW_NONE);
		gr.setLayout(new FillLayout());
		gr.setText("Graph");
		
		cmp = new AlgorithmGraphComposite(getController(),gr,SWT.BORDER,Display.getCurrent(),false);
		g = new GridData(GridData.FILL_BOTH);
		g.verticalSpan =2;
		gr.setLayoutData(g);	
		
		
		gr = new Group(shell,SWT.NONE);
		gr.setLayout(new FillLayout());
		gr.setText("Rechentableau");
		
		gr.setLayoutData(new GridData(GridData.FILL_BOTH));
		cmp = new AlgorithmCalculationTableComposite(getController(),gr,SWT.NONE);
		
		
		gr = new Group(shell,SWT.NONE);
		gr.setLayout(new FillLayout());
		gr.setText("Ergebnis");	
		g = new GridData(GridData.FILL_BOTH);
		gr.setLayoutData(g);
		
		cmp = new AlgorithmResultTableComposite(getController(),gr,SWT.NONE);
		
		cmp = new StatusbarComposite(getController(),this,SWT.NONE,false);
		g = new GridData(GridData.FILL_HORIZONTAL);
		g.horizontalSpan = 2;
		g.heightHint = 125;
		cmp.setLayoutData(g);
		
		getController().setModifiedFlag();
	}
	
}
