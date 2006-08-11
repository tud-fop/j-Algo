package org.jalgo.module.ebnf.renderer.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;

/**
 * This class represents a drawn branch of a syntax diagram
 * 
 * @author Andre
 */
@SuppressWarnings("serial")
public class RenderSplit extends RenderElement {

	// TODO [Andre] Man kann auch über affine Transformationen mit nur einem
	// Arc2D aukommen

	protected Arc2D.Double arc_left_top;

	protected Arc2D.Double arc_right_top;

	protected Arc2D.Double arc_left_bottom;

	protected Arc2D.Double arc_right_bottom;

	protected Line2D.Double line_top;

	protected Line2D.Double line_bottom;

	protected Line2D.Double line_left;

	protected Line2D.Double line_right;

	protected Color topColor = new Color(0, 0, 0);

	protected Color bottomColor = new Color(0, 0, 0);

	protected Stroke topStroke = new BasicStroke();

	protected Stroke bottomStroke = new BasicStroke();

	/**
	 * Initializes the elements
	 */
	public RenderSplit() {

		this.setEnabled(true);
		arc_left_top = new Arc2D.Double();
		arc_right_top = new Arc2D.Double();
		arc_left_bottom = new Arc2D.Double();
		arc_right_bottom = new Arc2D.Double();

		line_top = new Line2D.Double();
		line_bottom = new Line2D.Double();
		line_left = new Line2D.Double();
		line_right = new Line2D.Double();

	}

	/**
	 * Sets the given color to the top area of a branch or repetition and
	 * changes the stroke to thin or fat
	 * 
	 * @param highlight true, if you want to highlight this part
	 * @param hColor the highlight color
	 */
	public void setTopHighlight(boolean highlight, Color hColor) {

		if (highlight) {
			topColor = hColor;
			topStroke = HIGHLIGHT_STROKE;
		} else {
			topColor = hColor;
			topStroke = STANDARD_STROKE;
		}
		this.repaint();

	}

	/**
	 * Sets the given color to the bottom area of a branch or repetition and
	 * changes the stroke to thin or fat
	 * 
	 * @param highlight true, if you want to highlight this part
	 * @param hColor the highlight color
	 */
	public void setBottomHighlight(boolean highlight, Color hColor) {

		if (highlight) {
			bottomColor = hColor;
			bottomStroke = HIGHLIGHT_STROKE;
		} else {
			bottomColor = hColor;
			bottomStroke = STANDARD_STROKE;
		}
		this.repaint();

	}

	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));

		// top
		g2d.setColor(topColor);
		g2d.setStroke(topStroke);
		g2d.draw(line_top);

		// bottom
		g2d.setColor(bottomColor);
		g2d.setStroke(bottomStroke);

		g2d.draw(arc_left_top);
		g2d.draw(arc_left_bottom);
		g2d.draw(arc_right_top);
		g2d.draw(arc_right_bottom);

		g2d.draw(line_bottom);
		g2d.draw(line_left);
		g2d.draw(line_right);

	}

	public void update() {

		int radius = rv.radius;

		arc_left_top.setArc(-radius, radius, 2 * radius, 2 * radius, 0, 90,
				Arc2D.OPEN);

		arc_left_bottom.setArc(radius, this.getHeight() - 3 * radius,
				2 * radius, 2 * radius, 180, 90, Arc2D.OPEN);

		arc_right_top.setArc(this.getWidth() - radius, radius, 2 * radius,
				2 * radius, 92, 90, Arc2D.OPEN);
		arc_right_bottom.setArc(this.getWidth() - 3 * radius, this.getHeight()
				- 3 * radius, 2 * radius, 2 * radius, 270, 90, Arc2D.OPEN);
		line_top.setLine(2, radius, this.getWidth() - 2, radius);
		line_bottom.setLine(2 * radius, this.getHeight() - radius, this
				.getWidth()
				- 2 * radius, this.getHeight() - radius);
		line_left.setLine(radius, 2 * radius, radius, this.getHeight() - 2
				* radius);
		line_right.setLine(this.getWidth() - radius, 2 * radius, this
				.getWidth()
				- radius, this.getHeight() - 2 * radius);

	}

}
