/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.swt.graphics.Image;

/**
 * A {@link Figure}that shows two arrows.
 * 
 * @author Michael Pradel
 *  
 */
public final class Arrows {

	private static Arrows instance = null;

	// Singleton design pattern
	private Arrows() {
	}

	public static ImageFigure getArrows(int direction) {
		if (instance == null)
			instance = new Arrows();
		ImageFigure a = new ImageFigure();
		if (direction == ITreeConstants.LEFT_ROT)
			a.setImage(new Image(null, "pix/trees/left_arrows.png"));
		else if (direction == ITreeConstants.RIGHT_ROT)
			a.setImage(new Image(null, "pix/trees/right_arrows.png"));
		return a;
	}
	
}