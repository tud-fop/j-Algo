/*
 * Created on 25.05.2004
 */
 
package org.jalgo.main.gfx;
import org.eclipse.draw2d.geometry.Point;

/**
 * Calculates a linear animation path.
 * 
 * @author Hauke Menges
 */
public class LinearAnimationConcept implements IAnimationConcept {
	

	/**
	 * Calculates a linear animation path.
	 * 
	 * @param startPoint	Animation start-point
	 * @param endPoint		Animation end-point
	 * @param position		Current position (from 0.0 to 1.0)
	 * @return				The new position
	 */
	public Point calculate(Point startPoint, Point endPoint, double position){
		return new Point();
	}

}
