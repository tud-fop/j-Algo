package org.jalgo.module.app.view.graph;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.graph.Node;

/**
 * This is for creating new Edges
 */
public class NewEdgeElement extends EdgeElement {

	private static final long serialVersionUID = 4184948572353232611L;
	private Node beginNode;
	private Node endNode;

	private Point2D endPoint;
	private DataType weight;

	public NewEdgeElement(Node node, Point2D point) {
		super(null);

		beginNode = node;
		endPoint = point;
	}

	/**
	 * @return the endPoint
	 */
	public Point2D getEndPoint() {
		return endPoint;
	}

	/**
	 * @param endPoint
	 *            the endPoint to set
	 */
	public void setEndPoint(Point2D endPoint) {
		this.endPoint = endPoint;
	}

	public void mouseDragged(MouseEvent event) {
		setEndPoint(event.getPoint());

		if (component == null)
			return;

		// Search for a matching end node
		endNode = null;

		for (NodeElement n : component.getNodeElements()) {
			if (n.nodeContains(event.getPoint())
					&& component.getGraphActionListener().getGraph().getEdge(
							beginNode, n.getNode()) == null
					&& n.getNode() != beginNode) {
				endNode = n.getNode();
				break;
			}
		}

		// Redraw
		component.update();
	}

	public void mouseReleased(MouseEvent event) {
		if (component == null)
			return;

		if (endNode == null)
			component.setFocussedElement(null);
		else {
			component.getGraphActionListener().addEdge(beginNode, endNode);
			component.setFocussedElement(null);
			// TODO Implement add edge
		}
	}

	protected float getEndNodeRadius() {
		if (endNode == null)
			return 0f;
		else
			return super.getEndNodeRadius();
	}

	protected Node getBeginNode() {
		return beginNode;
	}

	protected Node getEndNode() {
		return endNode;
	}

	protected Point2D getEndNodePoint() {
		if (endNode == null)
			return getEndPoint();
		else
			return component.getScaledLocation(endNode.getLocation());
	}

	protected String getEdgeWeight() {
		if (weight == null && component != null) {
			try {
				weight = component.getGraphActionListener().getDataType()
						.newInstance();
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}

		if (weight != null)
			return weight.toString();
		else
			return "";
	}
}
