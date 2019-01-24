/**
 * 
 */
package org.jalgo.module.ebnf.renderer.elements;

import java.awt.geom.Arc2D;

/**
 * This class represents a drawn repetition of a syntax diagram
 * 
 * @author Andre Viergutz
 */
@SuppressWarnings("serial")
public class RenderRepetition extends RenderSplit {

	
	public void update() {

		int radius = rv.radius;

		arc_left_top.setArc(2, radius, 2 * radius, 2 * radius, 90, 90,
				Arc2D.OPEN);

		arc_left_bottom.setArc(2, this.getHeight() - 3 * radius, 2 * radius,
				2 * radius, 180, 90, Arc2D.OPEN);

		arc_right_top.setArc(this.getWidth() - 2 * radius - 2, radius, 2 * radius,
				2 * radius, 0, 90, Arc2D.OPEN);
		arc_right_bottom.setArc(this.getWidth() - 2 * radius - 2, this
				.getHeight()
				- 3 * radius, 2 * radius, 2 * radius, 270, 90, Arc2D.OPEN);
		line_top.setLine(radius, radius, this.getWidth()-radius, radius);
		line_bottom.setLine(radius, this.getHeight() - radius, this.getWidth()
				- radius, this.getHeight() - radius);
		line_left.setLine(2, 2 * radius, 2, this.getHeight() - 2 * radius);
		line_right.setLine(this.getWidth() - 2, 2 * radius,
				this.getWidth() - 2, this.getHeight() - 2 * radius);

	}

}
