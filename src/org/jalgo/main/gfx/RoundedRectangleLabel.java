/*
 * Created on May 17, 2004
 */
 
package org.jalgo.main.gfx;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;


/**
 * This class represents a graphical rounded rectangle with 2 Labels.<p>
 * One Label is in the middel (mainLabel) and one Label (indexLabel)
 * is at the top-right corner.
 * 
 * @author Cornelius Hald
 */
public class RoundedRectangleLabel extends RoundedRectangle {
	
	private Label mainLabel;
	private Label indexLabel;

	/**
	 * Contructs new GfxEllipseLabel
	 */
	public RoundedRectangleLabel() {
		super();
		
		//setLayoutManager(new ToolbarLayout());
		
		mainLabel = new Label();
		add(mainLabel);
		indexLabel = new Label();
		add(indexLabel);
		
		
		setOpaque(true);
	}
	
	/**
	 * @return The mainLabel, which is at the center of this Figure
	 */
	public Label getLabel() {
		return mainLabel;
	}
	
	public void setIndexVisible(boolean bool) {
		indexLabel.setVisible(bool);
	}
	
	/**
	 * @return The indexLabel, which is at the top-right on this Figure
	 */
	public Label getIndexLabel() {
		return indexLabel;
	}
	
	/**
	 * Sets text on the label
	 * FIXME: Textposition is wrong
	 * @param text
	 */
	public void setText(String text) {
		mainLabel.setText(text);
		setSize(mainLabel.getPreferredSize().width, getSize().height);
		mainLabel.setSize(mainLabel.getPreferredSize());
	}
	
	/**
	 * @return The text from the mainLabel
	 */
	public String getText() {
		return mainLabel.getText();
	}
	
	public void setTextBackground(Color color) {
		mainLabel.setBackgroundColor(color);
	}
	
	/**
	 * @param bool Enable or disable a line under the mainLabel
	 */
	public void setTextUnderline(boolean bool) {
	}
	
	public void setFont(Font font) {
		mainLabel.setFont(font);
	}
	
	/**
	 * Set text on the indexLabel
	 * @param text
	 */
	public void setIndexText(String text) {
		indexLabel.setText(text);
	}
}
