package org.jalgo.module.ebnf.gui.syndia;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import org.jalgo.module.ebnf.renderer.elements.RenderElement;

/**
 * MouseListener that highlights terminal symbols and variables.
 * 
 * @author Michael Thiele
 * 
 */
public class ChangeFillColorToListener extends MouseInputAdapter {

	/**
	 * Changes the color to blue if mouse enters this element.
	 */
	public void mouseEntered(MouseEvent e) {
		((RenderElement) e.getComponent())
				.setColor(RenderElement.HIGHLIGHT_BLUE);
	}

	/**
	 * Changes the color back to standard fill color if mouse exits this
	 * element.
	 */
	public void mouseExited(MouseEvent e) {
		((RenderElement) e.getComponent())
				.setColor(RenderElement.STANDARD_FILL_COLOR);
	}

}
