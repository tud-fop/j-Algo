package org.jalgo.module.ebnf.gui.syndia;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import org.jalgo.module.ebnf.renderer.elements.RenderNullElem;

/**
 * MouseListener that changes color of NullElems to red if mouse enters it.
 * 
 * @author Michael Thiele
 * 
 */
public class ChangeColorToRedListener extends MouseInputAdapter {

	/**
	 * If mouse enters, change color of NullElem to red.
	 */
	public void mouseEntered(MouseEvent e) {
		((RenderNullElem) e.getComponent())
				.setColor(RenderNullElem.HIGHLIGHT_COLOR);
	}

	/**
	 * If mouse exits, change color back to standard color.
	 */
	public void mouseExited(MouseEvent e) {
		((RenderNullElem) e.getComponent())
				.setColor(RenderNullElem.STANDARD_COLOR_GREY);
	}
}
