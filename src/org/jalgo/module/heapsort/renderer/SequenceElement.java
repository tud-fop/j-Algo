/**
 * 
 */
package org.jalgo.module.heapsort.renderer;

import java.awt.Point;

/**
 * @author mbue
 *
 */
public class SequenceElement extends CanvasEntity {
	private String label;
	private Point position = new Point();
	private float highlight = 0.0f;
	private double scale = 1.0;
	
	protected SequenceElement(Point pos, String label) {
		bounds.setBounds(-15, -15, 30, 30);
		// trick (see Node)
		setPosition(pos);
		// don't use setLabel:
		//   a) this.label is null (would crash)
		//   b) too early to invalidate
		this.label = label;
	}

	/**
	 * @see Node#update()
	 *
	 */
	protected void update() {
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
}
