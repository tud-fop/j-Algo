package org.jalgo.module.ebnf.renderer.wordalgorithm;

import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

/**
 * This Element is the graphical representation of a ReturnAdress from a
 * <code>Variable</code> on the Stack during the Algorithm.
 * 
 * @author Claas Wilke
 * 
 */
@SuppressWarnings("serial")
public class StackAdress extends RenderElement {

	// The Name of the return Adress (an int).
	private String label;

	// Represents some shape
	protected Rectangle2D.Double rect;

	/**
	 * Initializes the element
	 * 
	 * @param label
	 *            The name of the <code>ReturnAdress</code>.
	 * @param aVar
	 *            The <code>Variable</code> that belongs to this
	 *            <code>ReturnAdress</code>.
	 */
	public StackAdress(String label) {

		this.label = label;

		rect = new Rectangle2D.Double();

	}

	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));

		// Shadow
		g2d.setColor(Color.BLACK);
		rect.x += 3;
		rect.y += 3;
		g2d.setComposite(makeComposite(0.4F));
		g2d.fill(rect);

		// Rectangle
		rect.x -= 3;
		rect.y -= 3;
		g2d.setComposite(makeComposite(1F));
		g2d.setColor(this.color);
		g2d.fill(rect);

		// Adress itself
		g2d.setColor(Color.BLACK);
		g2d
				.setFont(new Font(rv.font.getName(), Font.ITALIC, 17));
		g2d.drawString(label, (int) Math.round(0.6 * 15), (int) Math
				.floor(1.27 * 15));

		g2d.draw(rect);

	}

	public void update() {
		rect.setRect(0, 0, this.getWidth() - 4, this.getHeight() - 4);
	}

}