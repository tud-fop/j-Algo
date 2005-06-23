/*
 * Created on 31.05.2005
 *
 */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.model.Position;
import org.jalgo.module.dijkstraModule.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This class provides the functionality for creating a new Node.
 * @author Hannes Strass, Martin Winter
 *
 */
public class NewNodeAction extends GraphAction
{
	private Position position;
	/*
	 * Changed to Position position; because of this:
	 * Place a new node in the top right corner of the drawing rectangle
	 * Undo the last NewNodeAction 
	 * compact the jAlgo-Window
	 * Redo the last NewNodeAction
	 * -> the Node will be invisible
	 * 
	private Point m_screenCoordinates;
	private Rectangle m_Bounds;
	*/
	
	/** Creates a NewNodeAction, which creates a new Node
	 * 
	 * @param ctrl the Controller
	 * @param screenCoordinates Point on screen where user wants new Node to appear
	 * @param bounds bounds of the canvas where Nodes can be drawn
	 * @throws Exception 
	 */
	public NewNodeAction(Controller ctrl, Point screenCoordinates, Rectangle bounds) throws Exception
	{
		super(ctrl);
		/*
		this.m_screenCoordinates = screenCoordinates;
		this.m_Bounds = bounds;
		*/
		position = new Position(screenCoordinates, bounds);
		this.registerAndDo(true);
	}
	
	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstraModule.actions.Action#doAction()
	 */
	public boolean doAction() throws Exception
	{
		
		if (getController().getGraph().getNextNodeIndex() == 10) return false;
		getController().getGraph().setAllChangedFlagsFalse();
		//Position position = new Position(m_screenCoordinates, m_Bounds);
		getController().getGraph().addNode(new Node(getController().getGraph().getNextNodeIndex(), position));
		getController().setGraph(getController().getGraph());

		return true;
	}

}
