package org.jalgo.module.ebnf.renderer.wordalgorithm;

import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * This Element is the graphical representation of a Base Line at the bottom of the Stack
 * 
 * @author Claas Wilke
 * 
 */
@SuppressWarnings("serial")
public class StackBase extends RenderElement {

	private int width;

	// Space between Line and shadow in pixels
	private static final int SHADOW_WIDTH = 2;
	
	private Color myColor = Color.BLACK;

	/**
	 * Initializes the element
	 * 
	 */
	public StackBase(int width) {

		this.width = width;
		// The ADRESS_SIZE part size of its Variable
		this.setSize(width, SHADOW_WIDTH + 1);

	}

	/**
	 * Used when the Adress should be drawn
	 */
	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));

		// Draw Base
		// Shadow
		g2d.setColor(Color.GRAY);
		// Horizontal Line
		g2d.drawLine(SHADOW_WIDTH, SHADOW_WIDTH, width, SHADOW_WIDTH);

		// Base Himself
		g2d.setColor(this.myColor);
		// Horizontal Line
		g2d.drawLine(0, 0, width - SHADOW_WIDTH, 0);
	}

	/**
	 * Called when the <code>ReturnAdress</code> is repainted.
	 */
	public void update() {
	}

	/**
	 * Sets the Color, which should be used to draw the stack baseline.
	 */
	public void setColor(Color myColor) {

		this.myColor = myColor;
		repaint();
		
	}
}