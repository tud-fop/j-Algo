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

	public static ImageFigure getRightArrows() {
		if (instance == null)
			instance = new Arrows();
		ImageFigure ra = new ImageFigure();
		ra.setImage(new Image(null, "pix/trees/right_arrows.png"));
		return ra;
	}
	
	public static ImageFigure getLeftArrows() {
		if (instance == null)
			instance = new Arrows();
		ImageFigure la = new ImageFigure();
		la.setImage(new Image(null, "pix/trees/left_arrows.png"));
		return la;
	}

}