/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

/**
 * A leaf component of a tree. Leaves cannot have any children.
 * @author Michael Pradel
 *
 */
public class Leaf extends TreeComponent {

	private LeafFigure figure;
	
	public LeafFigure getFigure() {
		return figure;
	}
}