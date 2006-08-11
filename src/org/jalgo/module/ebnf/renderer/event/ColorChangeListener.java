/**
 * 
 */
package org.jalgo.module.ebnf.renderer.event;


import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.jalgo.module.ebnf.renderer.elements.RenderElement;

/**
 * @author Andre Viergutz
 *
 */
public class ColorChangeListener extends MouseAdapter {
	
	
	public void mouseEntered(MouseEvent e) {
		
		((RenderElement) e.getComponent()).setColor(new Color(240,240,200));
		
	}
    
	public void mouseExited(MouseEvent e) {
		
		((RenderElement) e.getComponent()).setColor(RenderElement.STANDARD_FILL_COLOR);
		
	}
   
		
		
}
