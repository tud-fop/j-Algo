/**
 * 
 */
package org.jalgo.module.heapsort.renderer;

import java.awt.Point;

/**
 * @author mbue
 *
 */
public class Edge extends CanvasEntity {
	protected Point from;
	protected Point to;
	protected float opacity = 1.0f;

	protected Edge(Point from, Point to) {
		this.from = from;
		this.to = to;
		updateBounds();
	}
	
	private void updateBounds() {
		invalidate();
		if (to.x < from.x)
			bounds.setBounds(to.x, from.y, from.x-to.x, to.y-from.y);
		else
			bounds.setBounds(from.x, from.y, to.x-from.x, to.y-from.y);
		update();
	}

	/**
	 * @see Node#update()
	 */
	protected void update() {
	}
	
	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
		invalidate();
		update();
	}	
}
