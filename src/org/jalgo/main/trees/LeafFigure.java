/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A {@link Figure}that shows a leaf of a tree.
 * 
 * @author Michael Pradel
 *  
 */
public class LeafFigure extends Figure {

	private final int spacing = 10;

	private textFigure rect;

	private Label label;

	private Label outerLabel;

	private class textFigure extends RoundedRectangle {
		public Label label;
		
		public textFigure(String t) {
			label = new Label(t);
			FlowLayout textLayout = new FlowLayout();
			textLayout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
			setLayoutManager(textLayout);
			add(label, new Rectangle(-1,-1,-1,-1));
		}

		public Dimension getPreferredSize(int wHint, int hHint) {
			Dimension pref = super.getPreferredSize(wHint, hHint);
			pref.width += spacing;
			return pref;
		}
	}
	
	public LeafFigure() {
		this("NIL");
	}
	
	public LeafFigure(String text) {
		super();
		outerLabel = new Label();

		rect = new textFigure(text);
		
		// TODO: remove after testing
		outerLabel.setText("xyz");

		FlowLayout layout = new FlowLayout();
		setLayoutManager(layout);

		add(outerLabel, new Rectangle(0, 0, -1, -1));
		add(rect, new Rectangle(0, 0, -1, -1));

	}

	public void setText(String text) {
		rect.label.setText(text);
	}
	
	public String getText() {
		return rect.label.getText();
	}
	
	public void setOuterText(String text) {
		outerLabel.setText(text);
	}
	
	public String getOuterText() {
		return outerLabel.getText();
	}
	
	public Figure getInnerFigure() {
		return rect;
	}
}