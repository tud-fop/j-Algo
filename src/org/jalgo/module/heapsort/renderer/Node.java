package org.jalgo.module.heapsort.renderer;

import java.awt.Point;

/**
 * <p>A node as used in Heapsort; this means: a circle with a
 * two-digit number in it.</p>
 * 
 * <p>The node can be drawn in several fashions, which are at the moment:
 * normal and highlighted.</p> 
 * 
 * @author mbue
 *
 */
public class Node extends CanvasEntity {
	private float highlight = 0.0f;
	private String label;
	private Point position = new Point();
	private double scale = 1.0;
	
	public Node(Point pos, String label) {
		bounds.setBounds(-20, -20, 40, 40);
		// use a trick:
		// if label == null, invalidation will only occur after setting
		setPosition(pos);
		// don't use setLabel:
		//   a) this.label is null (would crash)
		//   b) too early to invalidate
		this.label = label;
	}
	
	/**
	 * <p>This is called whenever the visual appearance
	 * parameters have changed and can thus be used by
	 * the descendant class (typically rendering-specific)
	 * to update its internal structures (textures etc.)</p>
	 * 
	 * <p>However, the renderer could as well detect changes
	 * on demand (lazily, so to speak), which would of
	 * course require caching the old values...</p>
	 */
	protected void update() {
		// "nothing is being done..." (Jeff Beck)
	}
	
	public float getHighlight() {
		return highlight;
	}
	
	public void setHighlight(float value) {
		if (value != highlight) {
			highlight = value;
			invalidate();
			update();
		}
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		if (!this.label.equals(label)) {
			this.label = label;
			invalidate();
			update();
		}
	}
	
	public Point getPosition() {
		return position;
	}

	// newly added method just for allowing efficient animations
	public void setPosition(double x, double y) {
		invalidate();
		this.position.setLocation(x, y);
		trans.setToTranslation(position.x, position.y);
		trans.scale(scale, scale);
		invalidate();
		//update(); -- nothing is changed concerning the appearance
	}
	
	public void setPosition(Point position) {
		if (!this.position.equals(position)) {
			this.position.setLocation(position);
			if (label != null)
				invalidate();
			trans.setToTranslation(position.x, position.y);
			trans.scale(scale, scale);
			invalidate();
			//update(); -- nothing is changed concerning the appearance
		}
	}
	
	public double getScale() {
		return scale;
	}
	
	public void setScale(double scale) {
		if (this.scale != scale) {
			this.scale = scale;
			invalidate();
			trans.setToTranslation(position.x, position.y);
			trans.scale(scale, scale);
			invalidate();
			update();
		}
	}
	
	public String toString() {
		return label;
	}
}
