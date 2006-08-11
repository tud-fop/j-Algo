/**
 * 
 */
package org.jalgo.module.ebnf.renderer.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;

/**
 * This class represents a drawn repetition of a syntax diagram
 * 
 * @author Andre Viergutz
 */
@SuppressWarnings("serial")
public class RenderTiledRepetition extends RenderElement {

	// TODO [Andre] Man kann auch über affine Transformationen mit nur einem
	// Arc2D aukommen

	
	private Arc2D.Double arc_left_top;
	private Arc2D.Double arc_right_top;
	private Arc2D.Double arc_left_bottom;
	private Arc2D.Double arc_right_bottom;
	private Line2D.Double line_bottom;
	private Line2D.Double line_left;
	private Line2D.Double line_right;

	/**
	 * Initializes the elements
	 */
	public RenderTiledRepetition() {

		this.setEnabled(true);
		arc_left_top = new Arc2D.Double();
		arc_right_top = new Arc2D.Double();
		arc_left_bottom = new Arc2D.Double();
		arc_right_bottom = new Arc2D.Double();

		line_bottom = new Line2D.Double();
		line_left = new Line2D.Double();
		line_right = new Line2D.Double();

	}

	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));

		/*
		 * g2d.setColor(Color.BLACK); kreis.x += 3; kreis.y += 3;
		 * g2d.setComposite(makeComposite(0.4F)); g2d.fill(kreis); kreis.x -= 3;
		 * kreis.y -= 3; g2d.setComposite(makeComposite(1F));
		 */
		
		float dash[] = { 2.0f, 5.0f };
		g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 5.0f, dash, 0.0f));
		g2d.setColor(Color.GRAY);
				
		g2d.draw(arc_left_top);
		g2d.draw(arc_left_bottom);
		g2d.draw(line_left);

		g2d.draw(arc_right_top);
		g2d.draw(arc_right_bottom);
		g2d.draw(line_bottom);
		g2d.draw(line_right);

	}

	public void update() {

		int radius = rv.radius;

		arc_left_top.setArc(0, radius, 2 * radius, 2 * radius, 90, 90,
				Arc2D.OPEN);

		arc_left_bottom.setArc(0, this.getHeight() - 3 * radius, 2 * radius,
				2 * radius, 180, 90, Arc2D.OPEN);

		arc_right_top.setArc(this.getWidth() - 2 * radius, radius, 2 * radius,
				2 * radius, -1, 90, Arc2D.OPEN);
		arc_right_bottom.setArc(this.getWidth() - 2 * radius - 1, this
				.getHeight()
				- 3 * radius, 2 * radius, 2 * radius, 270, 90, Arc2D.OPEN);
		line_bottom.setLine(radius, this.getHeight() - radius, this.getWidth()
				- radius, this.getHeight() - radius);
		line_left.setLine(0, 2 * radius, 0, this.getHeight() - 2 * radius);
		line_right.setLine(this.getWidth() - 1, 2 * radius,
				this.getWidth() - 1, this.getHeight() - 2 * radius);

	}

}
