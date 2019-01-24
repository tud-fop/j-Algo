package org.jalgo.module.app.core.graph;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Represents a node which should be a part of a graph. The node has an ID which
 * have to be unambiguous in the graph. The node knows its name and location.
 */
public class Node implements Serializable {

	private static final long serialVersionUID = 8145719979029192129L;

	/**
	 * The unambiguous identification of the node.
	 */
	private int id;

	/**
	 * The position in the paint area on the screen.
	 */
	private SerialPoint2D location;

	/**
	 * Initialized the node with the given ID. The Label is set by default on
	 * the string representation of the ID.
	 * 
	 * @param id
	 *            The ID of the new node.
	 */
	public Node(int id) {
		this.id = id;
		this.location = new SerialPoint2D(0, 0);
	}

	/**
	 * Gets the ID of the node.
	 * 
	 * @return The ID of the node.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the node to a new ID.
	 * 
	 * @param id
	 *            the new ID.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the location of the node
	 * 
	 * @return the position of the node.
	 */
	public Point2D getLocation() {
		return location.getPoint2D();
	}

	/**
	 * Puts the node in a new location.
	 * 
	 * @param location
	 *            the new location.
	 */
	public void setLocation(Point2D location) {
		this.location = new SerialPoint2D(location);
	}

	/*
	 * This private class is needed, because Point2d.Float is not serializable.
	 * (Sun Developer Network - Bug ID: 4263142)
	 * 
	 */
	private class SerialPoint2D implements Serializable {
		private static final long serialVersionUID = -3941806130927961539L;
		double x, y;

		public SerialPoint2D(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public SerialPoint2D(Point2D point) {
			x = point.getX();
			y = point.getY();
		}

		public Point2D getPoint2D() {
			return new Point2D.Float((float) x, (float) y);
		}
	}
}
