/**
 * 
 */
package org.jalgo.module.ebnf.renderer.elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

/**
 * This class represents a drawn variable of a syntax diagram
 * 
 * @author Andre Viergutz
 */
@SuppressWarnings("serial")
public class RenderVariable extends RenderElement {

	/**
	 * Represents the name of the variable
	 */
	public String label;

	/**
	 * Represents some shape
	 */
	protected Rectangle2D.Double rect;

	/**
	 * Initializes the element
	 * 
	 * @param label
	 *            the name of the variable
	 */
	public RenderVariable(String label) {

		this.label = label;
		// this.setEnabled(true);

		rect = new Rectangle2D.Double();

	}

	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));

		g2d.setColor(Color.BLACK);
		rect.x += 3;
		rect.y += 3;
		g2d.setComposite(makeComposite(0.4F));
		g2d.fill(rect);

		rect.x -= 3;
		rect.y -= 3;
		g2d.setComposite(makeComposite(1F));
		g2d.setColor(color);
		g2d.fill(rect);

		g2d.setColor(Color.BLACK);
		g2d.setFont(rv.font);
		int x;
		if (label.length() > 1)
			x = (int) Math.round(0.6 * rv.radius);
		else
			x = (int) Math.round(0.56 * rv.radius);
		g2d.drawString(label, x, (int) Math.floor(1.275 * rv.radius));

		g2d.draw(rect);

	}

	public void update() {

		rect.setRect(1, 1, this.getWidth() - 5, this.getHeight() - 5);
	}

}
