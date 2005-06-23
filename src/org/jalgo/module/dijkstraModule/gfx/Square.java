/*
 * Created on 10.05.2005
 */

package org.jalgo.module.dijkstraModule.gfx;

import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A rectangle with equal horizontal and vertical dimensions, defined by a "radius", 
 * and positioned through a center point instead of bounds.
 * <br><br><i>Note:</i> The square's width and height are (2 * radius) + 1 pixels,
 * so the center always lies on a pixel and not between two pixels.
 * @author Martin Winter
 */
public class Square 
extends RectangleFigure {
	
	private Point squareCenter;
	private int squareRadius;
	
	/**
	 * Creates a square with the given center and radius.
	 * @param center the square's center in pixel coordinates
	 * @param radius the square's radius in pixels
	 */
	public Square(Point center, int radius) {
		squareCenter = center;
		squareRadius = radius;
		updateBounds();
	}

	/**
	 * Sets the square's center.
	 * @param center the square's center in pixel coordinates
	 */
	public void setCenter(Point center) {
		squareCenter = center;
		updateBounds();
	}
	
	/**
	 * Returns the square's center.
	 * @return the square's center in pixel coordinates
	 */
	public Point getCenter() {
		return squareCenter;
	}
	
	/**
	 * Sets the square's radius.
	 * @param radius the square's radius in pixels
	 */
	public void setRadius(int radius) {
		squareRadius = radius;
		updateBounds();
	}
	
	/**
	 * Gets the square's radius.
	 * @return the square's radius in pixels
	 */
	public int getRadius() {
		return squareRadius;
	}
	
	/**
	 * Updates the square's bounds according to center and radius.
	 */
	private void updateBounds() {
		Point topLeft = new Point(squareCenter.getTranslated(-squareRadius, -squareRadius));
		Point bottomRight = new Point(squareCenter.getTranslated(squareRadius, squareRadius));
		Rectangle bounds = new Rectangle(topLeft, bottomRight);
		setBounds(bounds);
	}
}
