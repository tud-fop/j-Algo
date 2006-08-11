/**
 * 
 */
package org.jalgo.module.ebnf.renderer.elements;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;

/**
 * This class represents a drawn branch of a syntax diagram
 * 
 * @author Andre
 */
@SuppressWarnings("serial")
public class RenderNoStairBranch extends RenderSplit {

	

	/**
	 * Initializes the elements
	 */
	public RenderNoStairBranch() {

		this.setEnabled(true);
		
		arc_left_bottom = new Arc2D.Double();
		arc_right_bottom = new Arc2D.Double();

		line_top = new Line2D.Double();
		line_bottom = new Line2D.Double();
		line_left = new Line2D.Double();
		line_right = new Line2D.Double();

	}

	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));

		g2d.setColor(STANDARD_COLOR);
		g2d.setStroke(STANDARD_STROKE);
		g2d.draw(line_top);
		
		g2d.setColor(bottomColor);
		g2d.setStroke(bottomStroke);
		g2d.draw(arc_left_bottom);
		g2d.draw(arc_right_bottom);
		
		g2d.draw(line_bottom);
		g2d.draw(line_left);
		g2d.draw(line_right);

	}

	public void update() {

		int radius = rv.radius;

		arc_left_bottom.setArc(radius, this.getHeight() - 3 * radius,
				2 * radius, 2 * radius, 180, 90, Arc2D.OPEN);
		
		arc_right_bottom.setArc(this.getWidth() - 3 * radius, this.getHeight()
				- 3 * radius, 2 * radius, 2 * radius, 270, 90, Arc2D.OPEN);
		line_top.setLine(2 * radius, radius, this.getWidth() - 2 * radius, radius);
		line_bottom.setLine(2 * radius, this.getHeight() - radius, this
				.getWidth()
				- 2 * radius, this.getHeight() - radius);
		line_left.setLine(radius, 0, radius, this.getHeight() - 2
				* radius);
		line_right.setLine(this.getWidth() - radius, 0, this
				.getWidth()
				- radius, this.getHeight() - 2 * radius);

	}

}
