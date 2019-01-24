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
 * Created on 06.05.2005
 */

package org.jalgo.module.dijkstra.gfx;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import org.jalgo.module.dijkstra.gui.components.GraphDisplay;
import org.jalgo.module.dijkstra.model.GraphElement;
import org.jalgo.module.dijkstra.model.Node;

/**
 * Visual representation of a node. It can be connected with other node visuals
 * using {@link EdgeVisual} objects. <br>
 * <br>
 * <i>Note:</i> What is actually drawn is two circles (an inner and an outer
 * circle), and a label. The inner circle is visible, whereas the outer circle
 * acts as a spacer for the line connection of edge visuals (see the class
 * description there for more info).
 * 
 * @author Martin Winter, Hannes Strass
 */
public class NodeVisual
extends Visual {

	private Point center;
	private Node modelNode;

	private Color fillingColor;
	private Color contourColor;
	private Color labelColor;

	private static final int NODE_DIAMETER = 32;
	static final int NODE_RADIUS = NODE_DIAMETER / 2;
	private static final Stroke contourStroke = new BasicStroke(3);
	private static final java.awt.Font labelFont =
		new java.awt.Font("Verdana", java.awt.Font.BOLD, 20);

	/**
	 * Creates a new node visual.
	 * 
	 * @param modelNode the model node that this node visual represents
	 */
	public NodeVisual(Node modelNode) {
		this.modelNode = modelNode;

		center = modelNode.getPosition().getScreenPoint(
			GraphDisplay.getScreenSize());

		update(); // Initialize appearance.
	}

	/**
	 * Sets the center of the node visual in pixel coordinates.
	 * 
	 * @param center the center of the node visual in pixel coordinates
	 */
	public void setCenter(Point center) {
		this.center = center;
	}

	/**
	 * Returns the center of the node visual in pixel coordinates.
	 * 
	 * @return the center of the node visual in pixel coordinates
	 */
	public Point getCenter() {
		return center;
	}

	/* Methods inherited from Visual. */

	/**
	 * Update appearance according to flags. Call this method after modifying
	 * any flags.
	 */
	public void update() {
		if (isInEditingMode()) {
			// Flag with highest priority comes first.
			if (modelNode.isActive()) {
				fillingColor = Visual.RED;
				contourColor = Visual.RED;
				labelColor = Visual.WHITE;
			}
			else if (modelNode.isHighlighted()) {
				fillingColor = Visual.BLACK;
				contourColor = Visual.BLACK;
				labelColor = Visual.WHITE;
			}
			else if (modelNode.isChanged()) {
				fillingColor = Visual.WHITE;
				contourColor = Visual.RED;
				labelColor = Visual.RED;
			}
			else {
				fillingColor = Visual.WHITE;
				contourColor = Visual.BLACK;
				labelColor = Visual.BLACK;
			}
		}
		else {
			if (modelNode.isStart()) {
				if (modelNode.isActive()) {
					fillingColor = Visual.BLUE;
					contourColor = Visual.BLUE;
					labelColor = Visual.WHITE;
				}
				else {
					fillingColor = Visual.WHITE;
					contourColor = Visual.BLUE;
					labelColor = Visual.BLUE;
				}
			}
			else if (modelNode.isChosen()) {
				if (modelNode.isActive()) {
					fillingColor = Visual.GREEN;
					contourColor = Visual.GREEN;
					labelColor = Visual.WHITE;
				}
				else {
					fillingColor = Visual.WHITE;
					contourColor = Visual.GREEN;
					labelColor = Visual.GREEN;
				}
			}
			else if (modelNode.isBorder()) {
				if (modelNode.isActive()) {
					fillingColor = Visual.RED;
					contourColor = Visual.RED;
					labelColor = Visual.WHITE;
				}
				else {
					fillingColor = Visual.WHITE;
					contourColor = Visual.ORANGE;
					labelColor = Visual.ORANGE;
				}
			}
			else {
				fillingColor = Visual.WHITE;
				contourColor = Visual.GRAY;
				labelColor = Visual.GRAY;
			}
		}

		// Explicitly perform drawing update.
//		performUpdate();
	}

	public void updateModel(GraphElement node) {
		this.modelNode = (Node)node;
	}

	@Override
	public void draw(Graphics2D g, Dimension screenSize) {
		g.setColor(fillingColor);
		g.fillOval(
			getCenter().x-NODE_RADIUS,
			getCenter().y-NODE_RADIUS,
			NODE_DIAMETER, NODE_DIAMETER);

		g.setColor(contourColor);
		g.setStroke(contourStroke);
		g.drawOval(
			getCenter().x-NODE_RADIUS,
			getCenter().y-NODE_RADIUS,
			NODE_DIAMETER, NODE_DIAMETER);

		g.setColor(labelColor);
		g.setFont(labelFont);
		g.drawString(modelNode.getLabel(), getCenter().x-7, getCenter().y+8);
	}

	@Override
	public boolean hit(Dimension screenSize, Point p) {
		return
			modelNode.getPosition().getScreenPoint(screenSize).distance(p) < 20;
	}

	@Override
	public void updateLocation(Dimension screenSize) {
		setCenter(modelNode.getPosition().getScreenPoint(screenSize));
	}
}