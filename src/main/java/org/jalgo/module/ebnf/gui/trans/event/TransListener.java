/**
 * 
 */
package org.jalgo.module.ebnf.gui.trans.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.jalgo.module.ebnf.gui.trans.GUIController;
import org.jalgo.module.ebnf.model.ebnf.Term;
import org.jalgo.module.ebnf.renderer.elements.RenderTrans;

/**
 * @author Andre
 *
 */
public class TransListener extends MouseAdapter {
	
	private GUIController controller;
	private Term term;
	private boolean isExpShown;
	
	/** Initializes the controller
	 * @param controller the GuiController of the algorithm part
	 * @param t the <code>Term</code> to transform
	 */
	public TransListener(GUIController controller, Term t) {
		
		this.controller = controller;
		this.term = t;
		
	}
	
	
	public void mouseEntered(MouseEvent e) {
		isExpShown = false;
		if (controller.isExplanationShown()) {
			
			isExpShown = true;
			controller.setExplanationShow(false);
			
		}
		RenderTrans transelem = (RenderTrans) e.getComponent();
		transelem.showSeperated(true);
		//transelem.changeColor(false);
		
		controller.showExplanation(term);
				
	}
	public void mouseExited(MouseEvent e) {
		
		RenderTrans transelem = (RenderTrans) e.getComponent();
		transelem.showSeperated(false);
		//transelem.changeColor(true);
		
		controller.showExplanation(null);
		
		if (isExpShown)
			controller.setExplanationShow(true);
				
	}
	public void mouseReleased(MouseEvent e) {
		
		controller.performChosenStep((RenderTrans) e.getComponent());
		controller.showExplanation(null);
		if (isExpShown)
			controller.setExplanationShow(true);
		
	}
	
}
