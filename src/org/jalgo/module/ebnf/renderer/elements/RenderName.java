/**
 * 
 */
package org.jalgo.module.ebnf.renderer.elements;

import java.awt.Color;
import java.awt.Font;
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
public class RenderName extends RenderElement {

	/**
	 * Reperesents the name of the syntax diagram
	 */
	private String name;
	private boolean startDiagram;
	private int fontStyle;

	/**
	 * Initializes the render base and sets the name of the syntax diagram
	 * 
	 * @param name
	 *            the name of the syntax diagram
	 * @param startDiagram
	 *            indicates whether this RenderBase belongs to a startDiagram
	 */
	public RenderName(String name, boolean startDiagram) {

		this.name = name;
		this.startDiagram = startDiagram;
		this.setBackground(new Color(150, 150, 50));
		fontStyle = Font.ITALIC;

	}

	/**
	 * Sets the style of the font.
	 * 
	 * @param fontStyle
	 *            the style of the font
	 */
	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
	}

	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));

		g2d.setFont(new Font(rv.font.getName(), fontStyle, rv.font.getSize()));
		if (startDiagram)
			g2d.setColor(HIGHLIGHT_COLOR);

		g2d.drawString(name, 0, (int) Math.round(0.9 * rv.radius));

	}

	public void update() {

	}

}
