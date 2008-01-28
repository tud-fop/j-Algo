/**
 * 
 */
package org.jalgo.module.heapsort.renderer;

import java.awt.Color;
import java.awt.Point;

/**
 * @author mbue
 *
 */
public class MarkingRect extends CanvasEntity {
	private Color color = Color.GRAY;
	private Point position = new Point();
	private float opacity = 1.0f;
	private int width;
	private int height;
	
	protected MarkingRect() {
		super();
	}

	/**
	 * @see Node#update()
	 *
	 */
	protected void update() {
	}	
	
	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		if (!this.position.equals(position)) {
			this.position.setLocation(position);
			invalidate();
			trans.setToTranslation(position.x, position.y);
			invalidate();
		}		
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		if (height != this.height) {
			this.height = height;
			invalidate();
			bounds.setBounds(0, 0, width, height);
			invalidate();
		}
		
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		if (width != this.width) {
			this.width = width;
			invalidate();
			bounds.setBounds(0, 0, width, height);
			invalidate();
		}
	}

	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
		invalidate();
		update();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		update();
	}

}
