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
