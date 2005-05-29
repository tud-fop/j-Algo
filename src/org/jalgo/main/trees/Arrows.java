/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import org.eclipse.draw2d.ImageFigure;

/**
 * A {@link Figure} that shows two arrows. 
 * @author Michael Pradel
 *  
 */
public class Arrows extends ImageFigure {

	public int direction;

	/**
	 * Set the direction of the arrows.
	 * @param dir left or right rotation, see also {@link ITreeConstants}
	 */
	public void setDirection(int dir) {
		direction = dir;
	}
}