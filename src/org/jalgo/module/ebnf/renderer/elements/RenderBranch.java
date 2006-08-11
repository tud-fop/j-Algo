/**
 * 
 */
package org.jalgo.module.ebnf.renderer.elements;

import java.awt.geom.Arc2D;

/**
 * This class represents a drawn branch of a syntax diagram
 * 
 * @author Andre
 */
@SuppressWarnings("serial")
public class RenderBranch extends RenderSplit {

	
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
		line_top.setLine(2, radius, this.getWidth()-2, radius);
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
