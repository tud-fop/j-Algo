package org.jalgo.module.ebnf.renderer.wordalgorithm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

import org.jalgo.module.ebnf.renderer.elements.RenderElement;

/**
 * This class reperesents the end of an <code>SyntaxDiagram</code>.
 * 
 * Use to make the end clickable by an own <code>MouseListener</code>.
 * 
 * @author Claas Wilke
 */
@SuppressWarnings("serial")
public class RenderEnd extends RenderElement {

	/**
	 * Represents the base line of the exit of syntax diagram
	 */

	private int radius;

	protected Stroke stroke = new BasicStroke();

	/**
	 * Initializes the render end.
	 */
	public RenderEnd(int radius) {
		this.radius = radius;

		this.stroke = STANDARD_STROKE;
	}

	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));

		g2d.setColor(this.color);
		g2d.setStroke(stroke);
		g2d.drawLine(0, 3 * radius, this.getWidth(), 3 * radius);

	}

	public void update() {

	}

	/**
	 * Sets the Element highlighted
	 * 
	 * @param highlight
	 *            If true the elem should be highlighted, else the highlightinf
	 *            should be resetted.
	 * @param hColor
	 *            The <code>Color</code> the Element should be highlighted or
	 *            dehighlighted with.
	 */
	public void setHighlighted(boolean highlight, Color hColor) {

		color = hColor;
		if (highlight) {
			stroke = HIGHLIGHT_STROKE;
		} else {
			stroke = STANDARD_STROKE;
		}

		repaint();
	}

}
