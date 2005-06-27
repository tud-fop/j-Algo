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
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.graph.Node;

/**
 * Abstract class to represent a component of a tree, independant of its
 * specific properties (following the "Composite" design pattern).
 * 
 * @author Michael Pradel
 *  
 */
public abstract class TreeComponent {

	protected int weight = 0;

	protected TreeComponent parent;

	protected Edge edgeToParent;

	private org.eclipse.draw2d.graph.Node node;
	
	public TreeComponent() {
		node = new Node(this);
	}

	public org.eclipse.draw2d.graph.Node getNode() {
		return node;
	}

	/**
	 * Get the weight of this component. A node, leaf or subtree with a higher
	 * weight will appear on the right of one with a lower weigth.
	 * 
	 * @return The weight of this component, see also {@link ITreeConstants}.
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * Set the weight of this component. A node, leaf or subtree with a higher
	 * weight will appear on the right of one with a lower weigth.
	 * 
	 * @param weight
	 *            The new weight of this component, see also
	 *            {@link ITreeConstants}.
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	public abstract boolean getVisibility();

	public abstract void setVisibility(boolean visible);

	public boolean isTree() {
		return false;
	}

	public TreeComponent getParent() {
		return parent;
	}

	public Edge getEdgeToParent() {
		return edgeToParent;
	}

	public abstract Figure getInnerFigure();
	
	protected void setParent(TreeComponent p) {
		parent = p;
	}

	protected void setEdgeToParent(Edge e) {
		edgeToParent = e;
	}

}