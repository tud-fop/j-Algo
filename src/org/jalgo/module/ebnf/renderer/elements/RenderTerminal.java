/**
 * 
 */
package org.jalgo.module.ebnf.renderer.elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/**
 * This class represents a drawn terminal symbol of a syntax diagram
 * 
 * @author Andre Viergutz
 */
@SuppressWarnings("serial")
public class RenderTerminal extends RenderElement {

	/**
	 * Represents the name of the terminal symbol
	 */
	public String label;

	/**
	 * Represents the border of the symbol
	 */
	private RoundRectangle2D.Double kreis;

	/**
	 * Inititalizes the element by getting the name of the symbol
	 * 
	 * @param label the name of the terminal symbol
	 */
	public RenderTerminal(String label) {

		this.label = label;
		// this.setEnabled(true);

		kreis = new RoundRectangle2D.Double();

	}

	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));

		g2d.setColor(Color.BLACK);
		kreis.x += 3;
		kreis.y += 3;

		g2d.setComposite(makeComposite(0.4F));
		g2d.fill(kreis);

		kreis.x -= 3;
		kreis.y -= 3;

		g2d.setComposite(makeComposite(1F));

		g2d.setColor(color);
		g2d.fill(kreis);

		g2d.setColor(STANDARD_COLOR);
		g2d.setFont(rv.font);
		
		int x;
		if (label.length() > 1)
			x = (int) Math.round(0.6 * rv.radius);
		else
			x = (int) Math.round(0.66 * rv.radius);
		g2d.drawString(label, x, (int) Math
				.floor(1.27 * rv.radius));

		g2d.draw(kreis);

	}

	public void update() {

		kreis.setRoundRect(1, 1, getWidth() - 5, getHeight() - 5,
				getHeight() - 5, getHeight() - 5);

	}

}
