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

/*
 * Created on 06.05.2005
 */

package org.jalgo.module.dijkstraModule.gfx;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.graphics.Device;
import org.jalgo.module.dijkstraModule.model.Edge;

/**
 * Visual representation of an edge connecting two nodes.
 * It has an {@link EdgeWeightVisual} at its center which displays the edge's weight.
 * <br><br><i>Note:</i> What is actually drawn is two connections. Draw2D draws a connection 
 * all the way to the end points even when it has an arrowhead as its decoration. 
 * This causes thicker lines to overlap the tip of the arrowhead. To solve this problem, 
 * we need two connections -- one for the arrow (no line), and another for the line itself, 
 * which is not drawn all the way to the end. (Actually, one end is covered by an invisible 
 * circle; see NodeVisual for more info.)
 * @author Martin Winter
 */
public class EdgeVisual 
extends Visual {

	private PolylineConnection arrowConnection;
	private PolylineConnection lineConnection;	
	private EdgeWeightVisual weightVisual;
	
	/**
	 * Creates a new edge visual, connecting source and target.
	 * @param device the current device (needed for creating colors)
	 * @param parent the graph parent that the edge visual should appear on
	 * @param source the source node visual
	 * @param target the target node visual
	 * @param modelEdge the model edge this edge visual represents
	 */
	public EdgeVisual(Device device, GraphParent parent, NodeVisual source, NodeVisual target, Edge modelEdge) {
		super(parent);
		
		setFlags(modelEdge.getFlags());
		
		NodeVisual realSource = source;
		NodeVisual realTarget = target;
		
		// If we got a reversed edge, draw it thus
		if (modelEdge.isReversed()) {
		    realSource = target;
		    realTarget = source;
		}
		
		arrowConnection = new PolylineConnection();
		lineConnection = new PolylineConnection();
		setSource(realSource);
		setTarget(realTarget);
		
		weightVisual = new EdgeWeightVisual(device, parent, this, modelEdge);
		
		update();	// Initialize appearance.
	}
	
	/**
	 * Sets the source node visual.
	 * @param source the source node visual
	 */
	public void setSource(NodeVisual source) {
		arrowConnection.setSourceAnchor(source.getInnerAnchor());
		lineConnection.setSourceAnchor(source.getInnerAnchor());
	}

	/**
	 * Sets the target node visual.
	 * @param target the target node visual
	 */
	public void setTarget(NodeVisual target) {
		arrowConnection.setTargetAnchor(target.getInnerAnchor());
		lineConnection.setTargetAnchor(target.getOuterAnchor());
	}
	
	/**
	 * Returns the one of the two connections (see class description above)
	 * that the edge weight visual of this edge visual attaches itself to.
	 * @return a connection
	 */
	public Connection getConnectionForEdgeWeightVisual() {
		return arrowConnection;
	}
	
	/**
	 * Returns the edge's weight as a number.
	 * @return the edge's weight
	 */
	public int getWeight() {
		return weightVisual.getWeight();
	}
	

	/* Methods inherited from Visual. */
	
	/**
	 * Adds the actual drawing elements to the graph parent.
	 * These are the line and arrow connections (see class description above).
	 * @param parent the graph parent that the drawing elements should appear on
	 */
	public void addToParent(GraphParent parent) {
		parent.add(lineConnection);
		parent.add(arrowConnection);
	}

	/**
	 * Update appearance according to flags.
	 * Call this method after modifying any flags.
	 * The flags are also passed on the the edge weight visual.
	 */
	public void update() {
		int flags = getFlags();		
		
		weightVisual.setFlags(flags);
		weightVisual.update();

		if (isInEditingMode()) {
			// Flag with highest priority comes first.
			if (isActive()) {
				arrowConnection.setForegroundColor(parent.redColor);
				arrowConnection.setLineWidth(7);
				arrowConnection.setTargetDecoration(null);
				lineConnection.setLineWidth(0);
			}
			else if (isHighlighted()) {
				arrowConnection.setForegroundColor(parent.blackColor);
				arrowConnection.setLineWidth(3);
				arrowConnection.setTargetDecoration(null);
				lineConnection.setLineWidth(0);
			}
			else if (isChanged()) {
				arrowConnection.setForegroundColor(parent.redColor);
				arrowConnection.setLineWidth(3);
				arrowConnection.setTargetDecoration(null);
				lineConnection.setLineWidth(0);
			}
			else {
				arrowConnection.setForegroundColor(parent.blackColor);
				arrowConnection.setLineWidth(3);
				arrowConnection.setTargetDecoration(null);
				lineConnection.setLineWidth(0);
			}
		}
		else {
			if (isChosen()) {
				arrowConnection.setForegroundColor(parent.greenColor);
				arrowConnection.setLineWidth(0);
				PolygonDecoration arrowHead = new PolygonDecoration();
				arrowHead.setScale(20.0, 10.0);
				arrowConnection.setTargetDecoration(arrowHead);
				lineConnection.setForegroundColor(parent.greenColor);
				lineConnection.setLineWidth(7);
			}
			else if (isBorder()) {
				if (isActive()) {
					if (isConflict()) {
						arrowConnection.setForegroundColor(parent.orangeColor);
						arrowConnection.setLineWidth(0);
						PolygonDecoration arrowHead = new PolygonDecoration();
						arrowHead.setScale(20.0, 10.0);
						arrowConnection.setTargetDecoration(arrowHead);
						lineConnection.setForegroundColor(parent.orangeColor);
						lineConnection.setLineWidth(7);
					} else {
						arrowConnection.setForegroundColor(parent.redColor);
						arrowConnection.setLineWidth(0);
						PolygonDecoration arrowHead = new PolygonDecoration();
						arrowHead.setScale(20.0, 10.0);
						arrowConnection.setTargetDecoration(arrowHead);
						lineConnection.setForegroundColor(parent.redColor);
						lineConnection.setLineWidth(7);						
					}
				} else {
					arrowConnection.setForegroundColor(parent.orangeColor);
					arrowConnection.setLineWidth(0);
					PolygonDecoration arrowHead = new PolygonDecoration();
					arrowHead.setScale(10.0, 5.0);
					arrowConnection.setTargetDecoration(arrowHead);
					lineConnection.setForegroundColor(parent.orangeColor);
					lineConnection.setLineWidth(3);
				}
			}
			else {
				arrowConnection.setForegroundColor(parent.grayColor);
				arrowConnection.setLineWidth(3);
				arrowConnection.setTargetDecoration(null);
				lineConnection.setLineWidth(0);
			}
		}
		
		// Explicitly perform drawing update.
		performUpdate();
	}
	
	/**
	 * Forces the update manager to immediately refresh the display.
	 */
	public void performUpdate() {
		arrowConnection.getUpdateManager().performUpdate();
		lineConnection.getUpdateManager().performUpdate();
	}
}
