/*
 * Created on May 17, 2004
 */
 
package org.jalgo.main.gfx;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * This class represents a graphical ellipse with 2 Labels.<p>
 * One Label is in the middle (mainLabel) and one Label (indexLabel)
 * is at the top-right corner.
 * 
 * @author Malte Blumberg
 */
public class EllipseLabel extends Ellipse {

	private Label mainLabel;
	private Label indexLabel;
	private XYLayout layout;
	/**
	 * Contructs new GfxEllipseLabel
	 */
	public EllipseLabel() {

		layout = new XYLayout();
		setLayoutManager(layout);
		indexLabel = new Label();
		mainLabel = new Label();
		add(indexLabel, new Rectangle(0, 0, -1, -1));
		add(mainLabel, new Rectangle(0, 0, -1, -1));

		setOpaque(true);
	}

	public Dimension getPreferredSize(int arg0, int arg1) {
		return new Dimension(mainLabel.getPreferredSize().width + 20, 36);
	}

	/**
	 * @return The mainLabel, which is at the center of this Figure
	 */
	public Label getLabel() {
		return mainLabel;
	}

	/**
	 * @return The indexLabel, which is at the top-right on this Figure
	 */
	public Label getIndexLabel() {
		return indexLabel;
	}

	public void setIndexVisible(boolean bool) {
		indexLabel.setVisible(bool);
	}

	/**
	 * Sets text on the label
	 * @param text
	 */
	public void setText(String text) {
		mainLabel.setText(text);
		mainLabel.setTextAlignment(PositionConstants.CENTER);
		mainLabel.setLabelAlignment(PositionConstants.CENTER);

		readjustText();
	}

	private void readjustText() {
		Point textPoint =
			new Point(
				(getPreferredSize().width / 2)
					- (mainLabel.getPreferredSize().width / 2),
				(getPreferredSize().height) / 2
					- mainLabel.getPreferredSize().height / 2);
		layout.setConstraint(
			mainLabel,
			new Rectangle(
				textPoint.x,
				textPoint.y,
				mainLabel.getPreferredSize().width,
				mainLabel.getPreferredSize().height));
		textPoint =
			new Point(
				(getPreferredSize().width)
					- (indexLabel.getPreferredSize().width)
					- 3,
				3);
		layout.setConstraint(
			indexLabel,
			new Rectangle(
				textPoint.x,
				textPoint.y,
				indexLabel.getPreferredSize().width,
				indexLabel.getPreferredSize().height));

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
		if (bool) {
			Border labelBorder = new BottomLineBorder();
			mainLabel.setBorder(labelBorder);
		} else {
			mainLabel.setBorder(null);
		}
	}

	public void setFont(Font font) {
		mainLabel.setFont(font);
		indexLabel.setFont(font);
	}

	/**
	 * Set text on the indexLabel
	 * @param text
	 */
	public void setIndexText(String text) {
		indexLabel.setText(text);

		indexLabel.setTextAlignment(PositionConstants.RIGHT);
		indexLabel.setLabelAlignment(PositionConstants.RIGHT);

		readjustText();
	}
	
	public Label getMainLabel() {
		return mainLabel;
	}
	
	// TODO: Draw elliptical Background
}
