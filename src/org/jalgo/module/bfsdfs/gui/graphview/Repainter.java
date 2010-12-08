package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Helper class for delayed repainting of a specific component and rectangle.
 * @author Anselm Schmidt
 *
 */
public class Repainter implements ActionListener {
	/** The component that will be repainted. */
	Component component;
	
	/** The rectangle to repaint. */	
	Rectangle rect;
	
	/**
	 * Constructor.
	 * @param component Component that will be repainted.
	 * @param rect Rectangle that will be repainted.
	 * @author Anselm Schmidt
	 */
	public Repainter(Component component, Rectangle rect) {
		this.component = component;
		this.rect = rect;
	}
	
	/**
	 * Implemented interface method. Will be called when it's time to repaint.
	 * @param arg0 Not used.
	 * @author Anselm Schmidt
	 */
	public void actionPerformed(ActionEvent arg0) {
		component.repaint(rect.x, rect.y, rect.width, rect.height);
	}

}
