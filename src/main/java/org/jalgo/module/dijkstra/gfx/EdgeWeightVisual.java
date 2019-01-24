/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/*
 * Created on 10.05.2005
 */

package org.jalgo.module.dijkstra.gfx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

import org.jalgo.module.dijkstra.model.Edge;
import org.jalgo.module.dijkstra.model.GraphElement;

/**
 * Visual representation of an edge weight, displayed at the center of an
 * {@link EdgeVisual}. It allows the weight to be changed interactively with
 * the mouse. <br>
 * <br>
 * <i>Note:</i> An edge weight visual has three actual drawing components: a
 * label, a circle and a square. The label is always visible, but only one of
 * the other two is displayed at any point in time. The circle is used to
 * "punch a hole" in the edge visual under it, so the label can be easily read.
 * The square is used when the edge weight visual is highlighted or its weight
 * is interactively changed.
 * 
 * @author Martin Winter
 */
public class EdgeWeightVisual
extends Visual {

	private Edge modelEdge;
	private int weight;
	private Point initialLocation;
	private int initialWeight;
	private Point center;

	private transient Color boxColor;
	private transient Color labelColor;
	private transient boolean boxVisible;

	private static final int WEIGHT_DIAMETER = 30;
	private static final int WEIGHT_RADIUS = WEIGHT_DIAMETER / 2;
	private static final Font labelFont =
		new Font("Verdana", java.awt.Font.BOLD, 19);

	/**
	 * Creates a new edge weight visual on an edge visual.
	 * 
	 * @param modelEdge the model edge this edge weight visual represents
	 */
	public EdgeWeightVisual(Edge modelEdge) {
		this.modelEdge = modelEdge;
		this.weight = modelEdge.getWeight();

		update();
	}

	/**
	 * Sets the weight of the edge weight visual. It does <b>not</b> change the
	 * weight of the model edge. Values are automatically limited to the range 0
	 * ... 99.
	 * 
	 * @param weight the weight
	 */
	public void setWeight(int weight) {
		this.weight = Math.min(weight, 99);
		this.weight = Math.max(this.weight, 0);
	}

	/**
	 * Returns the weight of the edge weight visual. It does <b>not</b> fetch
	 * the the weight from the model edge.
	 * 
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	public void beginDragging(Point startPoint) {
		initialLocation = startPoint;
		initialWeight = getWeight();
	}

	public void updateWeightToDragging(Point currPoint) {
		setWeight(initialWeight - (currPoint.y-initialLocation.y)/5);
	}

	/* Methods inherited from Visual. */

	/**
	 * Update appearance according to flags. Call this method after modifying
	 * any flags.
	 */
	public void update() {
		if (isInEditingMode()) {
			// Flag with highest priority comes first.
			if (modelEdge.isActive()) {
				boxColor = Visual.RED;
				boxVisible = true;
				labelColor = Visual.WHITE;
			}
			else if (modelEdge.isHighlighted()) {
				boxColor = Visual.BLACK;
				boxVisible = true;
				labelColor = Visual.WHITE;
			}
			else if (modelEdge.isChanged()) {
				boxVisible = false;
				labelColor = Visual.RED;
			}
			else {
				boxVisible = false;
				labelColor = Visual.BLACK;
			}
		}
		else {
			if (modelEdge.isChosen()) {
				boxVisible = false;
				labelColor = Visual.GREEN;
			}
			else if (modelEdge.isBorder()) {
				if (modelEdge.isActive()) {
					if (modelEdge.isConflict()) {
						boxVisible = false;
						labelColor = Visual.ORANGE;
					}
					else {
						boxVisible = false;
						labelColor = Visual.RED;
					}
				}
				else {
					boxVisible = false;
					labelColor = Visual.ORANGE;
				}
			}
			else {
				boxVisible = false;
				labelColor = Visual.GRAY;
			}
		}
	}

	private void setCenter(Point screenPoint) {
		center = screenPoint;
	}

	private Point getCenter() {
		return center;
	}

	public void updateLocation(Dimension screenSize) {
		Point n1Point =
			((NodeVisual)modelEdge.getStartNode().getVisual()).getCenter();
		Point n2Point =
			((NodeVisual)modelEdge.getEndNode().getVisual()).getCenter();
		setCenter(new Point(
			(n1Point.x+n2Point.x)/2,
			(n1Point.y+n2Point.y)/2));
	}

	@Override
	public void draw(Graphics2D g, Dimension screenSize) {
		updateLocation(screenSize);
		if (boxVisible) {
			g.setColor(boxColor);
			g.fillRect(center.x-WEIGHT_RADIUS, center.y-WEIGHT_RADIUS,
				WEIGHT_DIAMETER, WEIGHT_DIAMETER);
		}
		else {
			g.setColor(Visual.WHITE);
			g.fillOval(center.x-WEIGHT_RADIUS, center.y-WEIGHT_RADIUS,
				WEIGHT_DIAMETER, WEIGHT_DIAMETER);
		}
		g.setColor(labelColor);
		g.setFont(labelFont);
		if (getWeight()<10) g.drawString(""+getWeight(), center.x-7, center.y+8);
		else g.drawString(""+getWeight(), center.x-14, center.y+8);
	}

	@Override
	public void updateModel(GraphElement modelElement) {
		this.modelEdge = (Edge)modelElement;
		weight = modelEdge.getWeight();
	}

	@Override
	public boolean hit(Dimension screenSize, Point p) {
		if (getCenter().distance(p) < 10) return true;
		return false;
	}
}