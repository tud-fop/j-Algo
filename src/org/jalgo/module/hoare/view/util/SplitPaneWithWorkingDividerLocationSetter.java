package org.jalgo.module.hoare.view.util;

import java.awt.Graphics;

import javax.swing.JSplitPane;

//The method setDividerLocation(double) doesn't work as expected, if the component isn't painted yet
//so this changed class is nesseccary.
/**
 * <code>JSplitPane</code> that allows changing the divider position during the initialization
 * @author unknown
 */

public class SplitPaneWithWorkingDividerLocationSetter extends JSplitPane {
	
	private static final long serialVersionUID = -2815748405860579477L;
	
	private boolean isPainted = false;
	private boolean hasProportionalLocation = false;
	private boolean hasDividerLocation = false;
	private double proportionalLocation;
	private int dividerLocation;
	
	/**
	 * Returns a new JSplitPane with the specified orientation and with the specified components that does not do continuous redrawing.
	 * @param orientation an int specifying the horizontal or vertical orientation
	 * @param leftOrTop a boolean, true for the components to redraw continuously as the divider changes position, false to wait until the divider position stops changing to redraw
	 * @param rightOrBottom the Component that will appear on the left of a horizontally-split pane, or at the top of a vertically-split pane.
	 */
	public SplitPaneWithWorkingDividerLocationSetter(int orientation, java.awt.Component leftOrTop, java.awt.Component rightOrBottom) {
		super(orientation, leftOrTop, rightOrBottom);
	}
	
	/**
	 * Sets the divider location as a percentage of the JSplitPane's size.
	 * @param proportionalLocation a double-precision floating point value that specifies a percentage, from zero (top/left) to 1.0 (bottom/right)
	 */
    public void setDividerLocation(double proportionalLocation) {
        if (!isPainted) {       
            hasProportionalLocation = true;
            this.proportionalLocation = proportionalLocation;
        }
        else
            super.setDividerLocation(proportionalLocation);
    }
    
    /**
     * Sets the location of the divider. This is passed off to the look and feel implementation.
     * @param dividerLocation an int specifying a UI-specific value (typically a pixel count)
     */
    public void setDividerLocation(int dividerLocation) {
        if (!isPainted) {       
        	hasDividerLocation = true;
            this.dividerLocation = dividerLocation;
        }
        else
            super.setDividerLocation(dividerLocation);
    }
    
    /**
     * Paints this component.<br>
     * <br>
     * This method is called when the contents of the component should be painted; such as when the component is first being shown or is damaged and in need of repair.<br>
     * The clip rectangle in the <code>Graphics</code> parameter is set to the area which needs to be painted. Subclasses of <code>Component</code> that override this method need not call <code>super.paint(g)</code>.<br>
     * <br>
     * For performance reasons, <code>Component</code>s with zero width or height aren't considered to need painting when they are first shown, and also aren't considered to need repair.
     * 
     * @param g the graphics context to use for painting
     */
    public void paint(Graphics g) {
        if (!isPainted) {       
            if (hasProportionalLocation)
                super.setDividerLocation(proportionalLocation);
            if (hasDividerLocation)
                super.setDividerLocation(dividerLocation);
            isPainted = true;
        }
        super.paint(g);
    }
    
}
