/**
 * 
 */
package org.jalgo.module.ebnf.renderer.elements;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * This class reperesents the base elements of a syntax diagram, like the base
 * line and the name
 * 
 * @author Andre Viergutz
 */
@SuppressWarnings("serial")
public class RenderBase extends RenderElement {

	/**
	 * Initializes the render base and sets the name of the syntax diagram
	 */
	public RenderBase() {

		
	}

	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));
	
		g2d.setColor(STANDARD_COLOR);
		g2d.drawLine(0, 3 * rv.radius, this.getWidth(), 3 * rv.radius);

	}

	public void update() {

	}

}
