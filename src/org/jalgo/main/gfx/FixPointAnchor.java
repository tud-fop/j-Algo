package org.jalgo.main.gfx;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * @author Anne Kersten
 */
public class FixPointAnchor extends AbstractConnectionAnchor {
	


	private int style;
	
	/**
	 * Constructs a new FixPointAnchor with the given owner figure at a fixed point.
	 * @param source  The owner Figure
	 * @param style   SWT.TOP, SWT.LEFT, SWT.RIGHT, SWT.BOTTOM <p>  Location of the anchor in the Figure.
	 */
	public FixPointAnchor(IFigure source, int style){
		super(source);
		this.style=style;
	}
	 
	 /**
	  *  Returns the Point where the FixPointAnchor is located.
	  * @param reference The reference point
	  * @return The anchor location  
	  */
	 
	public Point getLocation(Point reference) {
		Rectangle r = getOwner().getBounds().getCopy();
		getOwner().translateToAbsolute(r);
		int off = style;
		switch (off)
		{
			case(SWT.TOP):return r.getTop();
			case(SWT.RIGHT):return r.getRight();
			case(SWT.BOTTOM):return r.getBottom().translate(0,-1);
			case(SWT.LEFT): return r.getLeft();
			default:return r.getLeft(); 
		}
	}
}
