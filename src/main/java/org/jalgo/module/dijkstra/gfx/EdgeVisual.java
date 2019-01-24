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
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import org.jalgo.module.dijkstra.gui.components.GraphDisplay;
import org.jalgo.module.dijkstra.model.Edge;
import org.jalgo.module.dijkstra.model.GraphElement;

/**
 * Visual representation of an edge connecting two nodes. It has an
 * {@link EdgeWeightVisual} at its center which displays the edge's weight.
 * 
 * @author Martin Winter
 */
public class EdgeVisual
extends Visual {

	private NodeVisual source;
	private NodeVisual target;
	private Edge modelEdge;
	private EdgeWeightVisual weightVisual;

	private transient Color lineColor;
	private transient Stroke lineStroke;
	private transient boolean arrowVisible;
	private transient boolean bigArrow;

	private static final Stroke boldLineStroke = new BasicStroke(7);
	private static final Stroke thinLineStroke = new BasicStroke(3);
	private static final GeneralPath arrow = new GeneralPath();
	static {
		arrow.moveTo(0, 0);
		arrow.lineTo(-22, -11);
		arrow.lineTo(-22, 11);
		arrow.closePath();
	}

	/**
	 * Creates a new edge visual, which is backed by the given model edge.
	 * 
	 * @param modelEdge the model edge this edge visual represents
	 */
	public EdgeVisual(Edge modelEdge) {
		source = (NodeVisual)modelEdge.getStartNode().getVisual();
		target = (NodeVisual)modelEdge.getEndNode().getVisual();
		this.modelEdge = modelEdge;

		weightVisual = new EdgeWeightVisual(modelEdge);

		updateLocation(GraphDisplay.getScreenSize());
		update(); // Initialize appearance.
	}

	public EdgeWeightVisual getWeightVisual() {
		return weightVisual;
	}

	public void setControllerMode(int controllerMode) {
		super.setControllerMode(controllerMode);
		weightVisual.setControllerMode(controllerMode);
	}

	/* Methods inherited from Visual. */

	/**
	 * Update appearance according to flags. Call this method after modifying
	 * any flags. The flags are also passed on the the edge weight visual.
	 */
	public void update() {
		weightVisual.update();
		if (isInEditingMode()) {
			// Flag with highest priority comes first.
			if (modelEdge.isActive()) {
				lineColor = Visual.RED;
				lineStroke = boldLineStroke;
				arrowVisible = false;
			}
			else if (modelEdge.isHighlighted()) {
				lineColor = Visual.BLACK;
				lineStroke = thinLineStroke;
				arrowVisible = false;
			}
			else if (modelEdge.isChanged()) {
				lineColor = Visual.RED;
				lineStroke = thinLineStroke;
				arrowVisible = false;
			}
			else {
				lineColor = Visual.BLACK;
				lineStroke = thinLineStroke;
				arrowVisible = false;
			}
		}
		else {
			if (modelEdge.isReversed()) {
				//TODO: assert, that update is never called twice in alg.mode
				NodeVisual tmp = source;
				source = target;
				target = tmp;
			}
			if (modelEdge.isChosen()) {
				lineColor = Visual.GREEN;
				lineStroke = boldLineStroke;
				arrowVisible = true;
				bigArrow = true;
			}
			else if (modelEdge.isBorder()) {
				if (modelEdge.isActive()) {
					if (modelEdge.isConflict()) {
						lineColor = Visual.ORANGE;
						lineStroke = boldLineStroke;
						arrowVisible = true;
						bigArrow = true;
					}
					else {
						lineColor = Visual.RED;
						lineStroke = boldLineStroke;
						arrowVisible = true;
						bigArrow = true;
					}
				}
				else {
					lineColor = Visual.ORANGE;
					lineStroke = thinLineStroke;
					arrowVisible = true;
					bigArrow = false;
				}
			}
			else {
				lineColor = Visual.GRAY;
				lineStroke = thinLineStroke;
				arrowVisible = false;
			}
		}
	}

	/**
	 * Retrieves a <code>Shape</code> object, which represents the arrow for the
	 * backing edge. For correct orientation the angle <code>theta</code> is
	 * passed here.
	 * 
	 * @param theta the angle of the edge
	 * @return the arrow of this edge as <code>Shape</code>
	 */
	private Shape getArrow(double theta) {
		AffineTransform transform = new AffineTransform();
		transform.translate(target.getCenter().getX(), target.getCenter().getY());
		transform.rotate(theta);
		transform.translate(-NodeVisual.NODE_RADIUS, 0);
		if (!bigArrow) transform.scale(0.6, 0.6);
		return arrow.createTransformedShape(transform);
	}

	@Override
	public void draw(Graphics2D g, Dimension screenSize) {
		double deltaX = target.getCenter().getX() - source.getCenter().getX();
		double deltaY = target.getCenter().getY() - source.getCenter().getY();
		double theta  = Math.atan(deltaY/deltaX);
		if (deltaX < 0.0) theta = theta+Math.PI; 

		g.setColor(lineColor);
		g.setStroke(lineStroke);
		if (arrowVisible) {
			// draw line shorter to avoid intersection with arrowhead
			g.drawLine(
				source.getCenter().x,
				source.getCenter().y,
				(int)(target.getCenter().x - Math.cos(theta)*
					(NodeVisual.NODE_RADIUS+12)),
				(int)(target.getCenter().y - Math.sin(theta)*
					(NodeVisual.NODE_RADIUS+12)));
			weightVisual.draw(g, screenSize);
			g.fill(getArrow(theta));
		}
		else {
			g.drawLine(source.getCenter().x, source.getCenter().y,
				target.getCenter().x, target.getCenter().y);
			weightVisual.draw(g, screenSize);
		}
	}

	@Override
	public void updateModel(GraphElement edge) {
		this.source = (NodeVisual)((Edge)edge).getStartNode().getVisual();
		this.target = (NodeVisual)((Edge)edge).getEndNode().getVisual();
		weightVisual.updateModel(edge);
	}

	@Override
	public boolean hit(Dimension screenSize, Point p) {
		return weightVisual.hit(screenSize, p);
	}

	@Override
	public void updateLocation(Dimension screenSize) {
		source.updateLocation(screenSize);
		target.updateLocation(screenSize);
		weightVisual.updateLocation(screenSize);
	}
}