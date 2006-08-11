package org.jalgo.module.ebnf.gui.syndia;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import org.jalgo.module.ebnf.renderer.elements.RenderNullElem;

/**
 * MouseListener that changes color of NullElems to green if mouse enters it.
 * 
 * @author Michael Thiele
 * 
 */
public class ChangeColorToGreenListener extends MouseInputAdapter {

	/**
	 * If mouse enters, change color of NullElem to green.
	 */
	public void mouseEntered(MouseEvent e) {
		((RenderNullElem) e.getComponent())
				.setColor(RenderNullElem.HIGHLIGHT_GREEN);
	}

	/**
	 * If mouse exits, change color back to standard color.
	 */
	public void mouseExited(MouseEvent e) {
		((RenderNullElem) e.getComponent())
				.setColor(RenderNullElem.STANDARD_COLOR_GREY);
	}
}
