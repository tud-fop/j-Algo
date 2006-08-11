/**
 * 
 */
package org.jalgo.module.ebnf.renderer.elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;

/**
 * This class reperesents the element in the syntax diagram editor showing if
 * the user is possible add a new element
 * 
 * @author Andre Viergutz
 */
@SuppressWarnings("serial")
public class RenderNullElem extends RenderElement {

	/**
	 * Some shape to represent this element
	 */
	private RoundRectangle2D.Double kreis;
	
	private float composite;

	/**
	 * grey, used when mouse outside null element
	 */
	public final static Color STANDARD_COLOR_GREY = new Color(130, 130, 110);
	
	/**
	 * green, used when mouse over null element
	 */
	public final static Color HIGHLIGHT_GREEN = new Color(0, 150, 0);
	
	/**
	 * Inititalizes the element
	 */
	public RenderNullElem() {

		this.setEnabled(true);
		
		composite = 0.2f;

		this.setColor(STANDARD_COLOR_GREY);
		kreis = new RoundRectangle2D.Double();

	}

	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));

		
		g2d.setComposite(makeComposite(composite));
		g2d.setColor(color);
		
		AffineTransform at = new AffineTransform();
		at.rotate(0.25 * Math.PI, 0.5 * getWidth(), 0.5 * getHeight());
		g2d.transform(at);
		g2d.fill(kreis);

	}

	public void update() {
		
		int width = (int) Math.round(getWidth()/2*Math.sqrt(2));
		int height = (int) Math.round(getHeight()/2*Math.sqrt(2));
		kreis.setRoundRect(
				(getWidth()-width)/2, 
				(getHeight()-height)/2, 
				width, 
				height, 
				0.1 * getHeight(),
				0.1 * getHeight());

	}
	
	public void setColor(Color color) {
		this.color = color;
		if (color.equals(STANDARD_COLOR_GREY)) {
			composite = 0.2f;
		} else {
			composite = 0.4f;
		}
		this.repaint();
	}

}
