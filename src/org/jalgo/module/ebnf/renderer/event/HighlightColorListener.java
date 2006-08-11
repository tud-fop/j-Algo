/**
 * 
 */
package org.jalgo.module.ebnf.renderer.event;


import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import org.jalgo.module.ebnf.renderer.elements.RenderElement;

/**
 * @author Andre Viergutz
 *
 */
public class HighlightColorListener implements MouseListener {
	
	public void mouseClicked(MouseEvent e) {}
    
	public void mouseEntered(MouseEvent e) {
		
		((RenderElement) e.getComponent()).changeColor(true);
		
	}
    
	public void mouseExited(MouseEvent e) {
		
		((RenderElement) e.getComponent()).changeColor(false);
		
	}
   
	public void mousePressed(MouseEvent e) {}
   
	public void mouseReleased(MouseEvent e) {}
	
		
}
