/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import org.eclipse.draw2d.Figure;

/**
 * A leaf component of a tree. Leaves cannot have any children.
 * @author Michael Pradel
 *
 */
public class Leaf extends TreeComponent {

	private LeafFigure figure;
	
	public Leaf() {
		this("NIL");
	}
	
	public Leaf(String text) {
		super();
		figure = new LeafFigure(text);
	}

	public void setText(String text) {
		figure.setText(text);
	}

	public String getText() {
		return figure.getText();
	}

	public void setOuterText(String text) {
		figure.setOuterText(text);
	}

	public String getOuterText() {
		return figure.getOuterText();
	}
	
	public LeafFigure getFigure() {
		return figure;
	}
	
	public void setVisibility(boolean v) {
		figure.setVisible(v);
	}
	
	public boolean getVisibility() {
		return figure.isVisible();
	}

	public Figure getInnerFigure() {
		return figure.getInnerFigure();
	}
	
}