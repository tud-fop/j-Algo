package org.jalgo.module.dijkstra.gui.event;

import java.awt.Point;

import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.DeleteEdgeAction;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.gui.components.GraphDisplay;
import org.jalgo.module.dijkstra.model.Edge;
import org.jalgo.module.dijkstra.util.DefaultExceptionHandler;

/**
 * This class represents the state, which collaborates with the mode
 * {@link org.jalgo.module.dijkstra.gui.Controller#MODE_DELETE_EDGE}.
 * 
 * @author Alexander Claus
 */
public class StateRemoveEdge
extends GraphDisplayState {

	private Edge selectedEdge;

	public StateRemoveEdge(GraphDisplay display, Controller controller) {
		super(display, controller);
	}

	@Override
	public void mousePressed(Point p) {
		Edge clickedEdge = display.findEdge(p);
		if (clickedEdge != null) {
			clickedEdge.setActive(true);
			clickedEdge.getVisual().update();
		}
	}

	@Override
	public void mouseReleased(Point p) {
		// sense of the following? -- alexander
		Edge clickedEdge = display.findEdge(p);
		if (clickedEdge != null) {
			// here was the weight itself deactivated! - alexander
			clickedEdge.setActive(false);
			clickedEdge.getVisual().update();
		}
	}

	@Override
	public void mouseClicked(Point p) {
		try {
			Edge clickedEdge = display.findEdge(p);
			if (clickedEdge != null)
				new DeleteEdgeAction(controller, clickedEdge);
		}
		catch (ActionException ex) {
			new DefaultExceptionHandler(ex);
		}
	}

	@Override
	public void mouseMoved(Point p) {
		Edge currentEdge = display.findEdge(p);
		if (currentEdge != selectedEdge &&
			selectedEdge!= null) {
			selectedEdge.setActive(false);
			selectedEdge.setHighlighted(false);
			selectedEdge.getVisual().update();
		}
		selectedEdge = currentEdge;
		if (selectedEdge != null) {
			selectedEdge.setHighlighted(true);
			selectedEdge.getVisual().update();
		}			
	}

	@Override
	public void mouseDragged(Point p) {
		Edge currentEdge = display.findEdge(p);
		if (currentEdge != selectedEdge &&
			selectedEdge!= null) {
			selectedEdge.setActive(false);
			selectedEdge.setHighlighted(false);
			selectedEdge.getVisual().update();
		}
		selectedEdge = currentEdge;
		if (selectedEdge != null) {
			selectedEdge.setHighlighted(true);
			selectedEdge.getVisual().update();
		}			
	}
}