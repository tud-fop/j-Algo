package org.jalgo.module.ebnf.renderer.wordalgorithm;

import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import org.jalgo.module.ebnf.renderer.elements.RenderVariable;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * This Element is the graphical representation of a ReturnAdress from a
 * <code>Variable</code>.
 * 
 * @author Claas Wilke
 * 
 */
@SuppressWarnings("serial")
public class ReturnAdress extends RenderElement {

	// Specifies the size of the ReturnAdress relative
	// to the size of its Variable
	public static final double ADRESS_SIZE = 1;

	// Shadow is deacitivated
	// // Space between Arrow and its shadow in half Pixels
	// // For example: Shadow_Width of 4 means a 2px Space between arrow and its
	// // shadow.
	// private static final int SHADOW_WIDTH = 4;

	// The Name of the return Adress (an int).
	public String label;

	// The Color of the Arrow (default is black).
	public Color myColor = Color.BLACK;

	/**
	 * Initializes the element
	 * 
	 * @param label
	 *            The name of the <code>ReturnAdress</code>.
	 * @param aVar
	 *            The <code>Variable</code> that belongs to this
	 *            <code>ReturnAdress</code>.
	 * @param fontSize
	 *            The size the font of the Number should have (default -1);
	 */
	public ReturnAdress(String label, RenderVariable aVar, int fontSize) {

		this.label = label;
		if (fontSize > 0) {
			this.rv.setFontSize(fontSize);
		}

		// The position at right top edge of the Variable
		this
				.setLocation((aVar.getX() + aVar.getWidth()) - 3,
						(aVar.getY() - (int) Math.round(aVar.getHeight()
								* ADRESS_SIZE)) + 3);

		// The ADRESS_SIZE part size of its Variable
		this.setSize((int) Math.round(aVar.getHeight() * ADRESS_SIZE * 2),
				(int) Math.round(aVar.getHeight() * ADRESS_SIZE));

	}

	/**
	 * Used when the Adress should be drawn
	 */
	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));

		// Shadow is deacitivated
		// // Draw Arrow
		// // Shadow
		// g2d.setColor(Color.GRAY);
		// // Diagonal Line
		// g2d.drawLine(0, this.getHeight(),
		// (int) Math.round(this.getHeight() / 2), (int) Math.round(this
		// .getWidth() / 2));
		// // Horizontal Line
		// g2d.drawLine((int) Math.round(this.getHeight() / 4), (int) Math
		// .round(this.getHeight() / 2), (int) Math
		// .round(this.getHeight() / 2), (int) Math
		// .round(this.getHeight() / 2));
		// // Vertical Line
		// g2d.drawLine((int) Math.round(this.getHeight() / 2), (int) Math
		// .round(this.getHeight() / 2), (int) Math
		// .round(this.getHeight() / 2), (int) Math
		// .round(this.getHeight() / 4 * 3));
		// // Draw Adress
		// g2d.setFont(new Font(rv.font.getName(), Font.ITALIC, (int) Math
		// .round(rv.font.getSize() * ADRESS_SIZE)));
		// g2d.drawString(label, (int) Math.round(this.getHeight() * 4 / 7
		// + SHADOW_WIDTH / 2), (int) Math
		// .floor((1.27 * rv.radius * ADRESS_SIZE) + SHADOW_WIDTH / 2));

		// Arrow Himself
		g2d.setColor(this.myColor);
		// Diagonal Line
		g2d.drawLine(0, this.getHeight(), (int) Math
				.round(this.getHeight() / 2), (int) Math
				.round(this.getHeight() / 2));
		// Horizontal Line
		g2d.drawLine((int) Math.round(this.getHeight() / 4), (int) Math
				.round(this.getHeight() / 2), (int) Math
				.round(this.getHeight() / 2), (int) Math
				.round(this.getHeight() / 2));
		// Vertical Line
		g2d.drawLine((int) Math.round(this.getHeight() / 2), (int) Math
				.round(this.getHeight() / 2), (int) Math
				.round(this.getHeight() / 2), (int) Math
				.round(this.getHeight() / 4 * 3));
		// Draw Adress
		g2d.setFont(new Font(rv.font.getName(), Font.ITALIC, (int) Math
				.round(rv.font.getSize() * ADRESS_SIZE)));
		g2d.drawString(label, (int) Math.round(this.getHeight() * 4 / 7),
				(int) Math.floor(1.5 * rv.radius * ADRESS_SIZE));
	}

	/**
	 * Called when the <code>ReturnAdress</code> is repainted.
	 */
	public void update() {
	}

	/**
	 * Sets the Color, which should be used to draw the Arrow.
	 */
	public void setColor(Color myColor) {

		this.myColor = myColor;
		repaint();

	}
}