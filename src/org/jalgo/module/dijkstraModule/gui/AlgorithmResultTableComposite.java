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
 * Created on 01.06.2005
 *
  */
package org.jalgo.module.dijkstraModule.gui;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jalgo.module.dijkstraModule.model.Node;
import org.jalgo.module.dijkstraModule.model.State;
import org.jalgo.module.dijkstraModule.util.DefaultExceptionHandler;

/**
 * @author Frank Staudinger
 *
 * This class represents the result table in the Algorithm-Mode.
 * It displays the shortest paths and so on.
 */
public class AlgorithmResultTableComposite extends ControllerComposite {
	protected class CalculationTableObserver implements Observer
	{
		Table m_tbl;
		CalculationTableObserver(Table tbl)
		{
			m_tbl = tbl;
		}
			/* (non-Javadoc)
		 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
		 */
		public void update(Observable o, Object arg)
		{
			try
			{
	
				Controller ctrl = (Controller)o;
				if(ctrl.getEditingMode() != Controller.MODE_ALGORITHM)
					return;
				m_tbl.removeAll();
				State dj = ctrl.getState(ctrl.getCurrentStep());
				if(dj != null)
				{
	
					if(dj.getBorderStates() == null)
						return;
					Iterator iter = dj.getGraph().getNodeList().iterator();
					
					dj.getBorderStates().iterator();
					while(iter.hasNext())
					{
					    Node node = (Node)iter.next();
					    if(node !=  dj.getGraph().getStartNode())
					    {
						    TableItem item = new TableItem(m_tbl,SWT.NONE);					    
							
							item.setText(0,node.getLabel());
							String strPath = node.getShortestPath();
							if(strPath.length() > 0)
							    item.setText(1,"("+node.getShortestPath()+","+node.getIndex()+")");
							if(node.getDistance() > 0)
							    item.setText(2,""+node.getDistance()); 
					    }

					}
				}						
			}
			catch(Exception exc)
			{
				new DefaultExceptionHandler(exc);
			}
	
		}
	}
	
	/**
	 * @param ctrl
	 * @param cmpParent
	 * @param nStyle
	 */
	public AlgorithmResultTableComposite(Controller ctrl, Composite cmpParent,
			int nStyle) {
		super(ctrl, cmpParent, nStyle);
		setLayout(new FillLayout());
		Table tbl = new Table(this,SWT.SINGLE|SWT.FULL_SELECTION|SWT.BORDER);
		tbl.setHeaderVisible(true);
		//tbl.setFont(BeamerFontFactory.getFont());
		
		TableColumn col = new TableColumn(tbl,SWT.LEFT);
		col.setText("Zielknoten");
		col.pack();
		col =  new TableColumn(tbl,SWT.LEFT);
		col.setText("kürzester Weg");
		col.pack();
		col =  new TableColumn(tbl,SWT.LEFT);
		col.setText("Weglänge");
		col.pack();	
		
		getController().addObserver(new CalculationTableObserver(tbl));
	}

}
