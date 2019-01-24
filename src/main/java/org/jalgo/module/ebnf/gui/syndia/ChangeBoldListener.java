package org.jalgo.module.ebnf.gui.syndia;

import java.awt.Font;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import org.jalgo.module.ebnf.renderer.elements.RenderName;

/**
 * MouseListener for <code>RenderName</code> in edit mode.
 * 
 * @author Michael Thiele
 *
 */
public class ChangeBoldListener extends MouseInputAdapter {

	/**
	 * If mouse enters <code>RenderName</code> change fontStyle to BOLD+ITALIC.
	 */
	public void mouseEntered(MouseEvent e) {
		((RenderName)e.getComponent()).setFontStyle(Font.BOLD | Font.ITALIC);
		((RenderName)e.getComponent()).repaint();
	}
	
	/**
	 * If mouse exits <code>RenderName</code> change fontStyle back to ITALIC.
	 */
	public void mouseExited(MouseEvent e) {
		((RenderName)e.getComponent()).setFontStyle(Font.ITALIC);
		((RenderName)e.getComponent()).repaint();
	}
	
}
