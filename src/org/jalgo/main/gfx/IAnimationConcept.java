/*
 * Created on 25.05.2004
 */
 
package org.jalgo.main.gfx;

import org.eclipse.draw2d.geometry.Point;

/**
 * An Interface for different animation path calculations. 
 * 
 * @author Hauke Menges
 */
public interface IAnimationConcept {
	
	/**
	 * Needed for calculating animation pathes.
	 * 
	 * @param startPoint	Animation start-point
	 * @param endPoint		Animation end-point
	 * @param position		Current position (from 0.0 to 1.0)
	 * @return				The new position
	 */
	public Point calculate(Point startPoint, Point endPoint, double percent);

}
