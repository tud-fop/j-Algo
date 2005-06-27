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
 * Created on 02.06.2005 17:58:30
 *
 *
 */
package org.jalgo.module.dijkstraModule.gui;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.module.dijkstraModule.gfx.GraphParent;
import org.jalgo.module.dijkstraModule.model.Graph;
import org.jalgo.module.dijkstraModule.model.Node;
import org.jalgo.module.dijkstraModule.model.State;


/**
 * @author Frank Staudinger
 *
 * This class represents the AlgorithmGraph in the Algorithm-Mode
 */
public class AlgorithmGraphComposite extends ControllerComposite {
 

	protected class GraphCompositeObserver implements Observer
	{
		private GraphParent m_graphParent;
		public GraphCompositeObserver(GraphParent g)
		{
			m_graphParent = g;
		}
		public void update(Observable o, Object arg)
		{
			Controller ctrl =  null;
			try
			{
				ctrl = (Controller)o;
			}
			catch(Exception e)
			{
				return;
			}
			
			if(ctrl == null)
				return;
			if(ctrl.getEditingMode() != Controller.MODE_ALGORITHM)
				return;
			State dj = ctrl.getState(ctrl.getCurrentStep());
			if(dj != null)
			{				
				Graph gr = dj.getGraph();
				Iterator nodeIter = gr.getNodeList().iterator();
				while(nodeIter.hasNext())
				{
				    Node node = (Node)nodeIter.next();
				}
				
				m_graphParent.setGraph(gr);			
				m_graphParent.update();							
			}
			else
			{
				Graph gr = ctrl.getGraph();
				
				
				m_graphParent.setGraph(gr);			
				m_graphParent.update();			
			}
		}
	};
	protected GraphParent m_graphParent;
	 /**
     * @param controller
     * @param cmpParent
     * @param nStyle
     */	
	public AlgorithmGraphComposite(Controller controller, Composite composite, int style, Device device, boolean bEditMode)
	{
		super(controller, composite, style);
		this.setLayout(new FillLayout());
		Canvas canvas = new Canvas(this, SWT.NONE);
		
		canvas.setBackground(ColorConstants.white);
		LightweightSystem lws = new LightweightSystem(canvas);	
		m_graphParent = new GraphParent(device, controller);
		
		m_graphParent.setBackgroundColor(ColorConstants.white);
		m_graphParent.setOpaque(true);
		lws.setContents(m_graphParent);
		controller.addObserver(new GraphCompositeObserver(m_graphParent));
		
	}

}
