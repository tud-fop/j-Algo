package org.jalgo.module.dijkstra.gfx;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.jalgo.module.dijkstra.model.Node;

/**
 * The class <code>NewEdgeLine</code> represents a simple line, which is shown
 * during the user creates a new edge.
 * 
 * @author Alexander
 */
public class NewEdgeLine
extends Line2D.Double {

	private final Node sourceNode;

	public NewEdgeLine(Node sourceNode) {
		this.sourceNode = sourceNode;
		setP2(((NodeVisual)sourceNode.getVisual()).getCenter());
	}

	public Node getSourceNode() {
		return sourceNode;
	}

	@Override
	public Point2D getP1() {
		return ((NodeVisual)sourceNode.getVisual()).getCenter();
	}

	@Override
	public double getX1() {
		return getP1().getX();
	}

	@Override
	public double getY1() {
		return getP1().getY();
	}

	public void setP2(Point2D p2) {
		x2 = p2.getX();
		y2 = p2.getY();
	}
}