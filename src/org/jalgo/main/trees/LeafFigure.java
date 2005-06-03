/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.swt.graphics.Color;

/**
 * A {@link Figure}that shows a leaf of a tree.
 * 
 * @author Michael Pradel
 *  
 */
public class LeafFigure extends RoundedRectangle {

	private Color backgroundColor;

	private Label label;

	private XYLayout layout;

	public LeafFigure() {
		this("NIL");
	}

	public LeafFigure(String text) {
		super();
		label = new Label(text);
		this.add(label);
		layout = new XYLayout();
		setLayoutManager(layout);
		setOpaque(true);
		System.out.println("Label width in constructor: "
				+ label.getPreferredSize().width);
		
		// HIER GEHTS WEITER!! (Größe von Figure automatisch an Label-Breite anpassen)
	}

	/*
	 * public Dimension getPreferredSize(int wHint, int hHint) {
	 * System.out.println("Call of getPreferredSize()"); Dimension pref =
	 * super.getPreferredSize(wHint, hHint); System.out.println("Rectangle's
	 * preferred width: " + pref.width); int childrenWidth = getInsets().left +
	 * getInsets().right; System.out.println("children's width without label: " +
	 * childrenWidth); childrenWidth += label.getPreferredSize().width;
	 * System.out.println("children's width with label: " + childrenWidth); if
	 * (pref.width < childrenWidth) pref.width = childrenWidth; return pref; }
	 */

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}