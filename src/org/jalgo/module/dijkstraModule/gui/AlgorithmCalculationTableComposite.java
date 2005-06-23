/**
 * @author Frank Staudinger
 * 
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
import org.jalgo.module.dijkstraModule.model.BorderState;
import org.jalgo.module.dijkstraModule.model.Edge;
import org.jalgo.module.dijkstraModule.model.Node;
import org.jalgo.module.dijkstraModule.model.State;
import org.jalgo.module.dijkstraModule.util.DefaultExceptionHandler;


/**
 * @author Frank Staudinger
 *
 * This class represents the AlgorithmCalculationTable in the Algorithm-Mode
 */
public class AlgorithmCalculationTableComposite extends ControllerComposite {

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
					
					Iterator iter = dj.getBorderStates().iterator();
					while(iter.hasNext())
					{
						BorderState bdstate = (BorderState)iter.next();
						TableItem item = new TableItem(m_tbl,SWT.NONE);
						
						// Write chosen edge. As we are given a node, try to get the
						// edge from the graph.
						Node chosen = bdstate.getChosen();
						Node realChosen = dj.getGraph().findNode(chosen.getIndex());
						Node pred = realChosen.getPredecessor();
						
						if (pred != null) {
						    Edge chosenEdge = dj.getGraph().findEdge(realChosen, pred);						        
						    item.setText(0, ""+chosenEdge.getAlgoText(true, bdstate));
						} else {
						    int index = chosen.getIndex();
						    item.setText(0, "("+index+",0,"+index+")");
						}
						Iterator edgeIter = bdstate.getBorder().iterator();
						String strText = "";
						while(edgeIter.hasNext())
						{
							Edge ed = (Edge)edgeIter.next();
							if(strText.length() > 0)
								strText+=", ";
							strText += ed.getAlgoText(true, bdstate);
						}
						item.setText(1,strText);
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
	 * @param ctrl Controller for this Composite
	 * @param cmpParent Parent Composite
	 * @param nStyle Style for this composite 
	 */
	public AlgorithmCalculationTableComposite(Controller ctrl,
			Composite cmpParent, int nStyle)
	{
		super(ctrl, cmpParent, nStyle);
		setLayout(new FillLayout());
		Table tbl = new Table(this,SWT.SINGLE|SWT.FULL_SELECTION|SWT.BORDER);
		tbl.setHeaderVisible(true);
		TableColumn col = new TableColumn(tbl,SWT.LEFT);
		col.setText("Gewählt");
		//col.setWidth(50);
		col.pack();
		col =  new TableColumn(tbl,SWT.LEFT);
		col.setText("Randknoten");
		//col.setWidth(250);	
		col.pack();
		getController().addObserver(new CalculationTableObserver(tbl));
		//tbl.setFont(BeamerFontFactory.getFont());
	}

}
