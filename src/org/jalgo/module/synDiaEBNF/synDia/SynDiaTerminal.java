/*
 * Created on 29.04.2004
 */
 
package org.jalgo.module.synDiaEBNF.synDia;

import java.io.Serializable;

import org.eclipse.swt.graphics.Font;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaColors;
import org.jalgo.module.synDiaEBNF.gfx.TerminalFigure;

/**
 * @author Michael Pradel
 * @author Babett Schaliz
 */
public class SynDiaTerminal	extends SynDiaElement implements SynDiaColors, Serializable {

	private TerminalFigure ellipse;
	//Ellipse in with the SynDiaTerminal is shown on the Screen

	public SynDiaTerminal(String label, Font font) {
		this.ellipse = new TerminalFigure(label, font);
	}

	public SynDiaTerminal(String label) {
		this.ellipse = new TerminalFigure(label);
	}
	
	public SynDiaTerminal(TerminalFigure figure) {
			this.ellipse = figure;
		}


	public String getLabel() {
		return ellipse.getLabel();
	}
	public void markLastConnection(boolean marked) {
		ellipse.highlightIncomingConnection(marked);
	}
	
	public void markNextConnection(boolean marked) {
		ellipse.highlightExitingConnection(marked);
	}
	
	public void markObjekt(boolean marked) {
		 ellipse.highlight(marked);
	}
	
	public void markObject() {
	ellipse.setBackgroundColor(currentFigure);
	}
	
	public void remarkObject(boolean bool){
	ellipse.setBackgroundColor(null);
	markObjekt(bool);
	}
	
	/**
	 * @return
	 */
	public TerminalFigure getGfx() {
		return ellipse;
	}

	/**
	 * @param figure
	 */
	public void setGfx(TerminalFigure figure) {
		ellipse = figure;
	}

}