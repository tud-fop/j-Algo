/*
 * Created on 29.04.2004
 */
 
package org.jalgo.module.synDiaEBNF.synDia;

import java.io.Serializable;

import org.eclipse.swt.graphics.Font;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaColors;
import org.jalgo.module.synDiaEBNF.gfx.VariableFigure;

/**
 * @author Benjamin Scholz
 * @author Babett Schaliz
 * @author Michael Pradel
 * @author Marco Zimmerling
 */
public class SynDiaVariable
	extends SynDiaElement
	implements SynDiaColors, Serializable {

	//Rectangle in with the SynDiaVar is shown on the Screen
	private VariableFigure rectangle;

	private int backtrackingLabel;
	private static int maxBacktrackingLabel = 1;
	
	private SynDiaInitial startElem=null;
	private SynDiaVariableBack helpCopy=null; // for Backtracking is set there

	public SynDiaVariable() {
		backtrackingLabel = maxBacktrackingLabel++;
	}

	public SynDiaVariable(VariableFigure rectangle) {
		this.rectangle = rectangle;
		backtrackingLabel = maxBacktrackingLabel++;
	}

	public SynDiaVariable(String label) {
		this.rectangle = new VariableFigure(label);
		backtrackingLabel = maxBacktrackingLabel++;
		if (rectangle !=null) {rectangle.setIndexText(""+backtrackingLabel);} //$NON-NLS-1$
	}

	public SynDiaVariable(String label, SynDiaInitial startElem) {
		this.rectangle = new VariableFigure(label);
		this.startElem = startElem;
		backtrackingLabel = maxBacktrackingLabel++;
		if (rectangle !=null) {rectangle.setIndexText(""+backtrackingLabel);} 		 //$NON-NLS-1$
	}

	public SynDiaVariable(String label, SynDiaInitial startElem, Font font) {
		this.rectangle = new VariableFigure(label, font);
		this.startElem = startElem;
		backtrackingLabel = maxBacktrackingLabel++;
		if (rectangle !=null) {
			rectangle.setIndexVisible(false);
		}
	}

	public SynDiaVariable(String label, Font font) {
		this.rectangle = new VariableFigure(label, font);
		this.startElem = null;
		backtrackingLabel = maxBacktrackingLabel++;
		if (rectangle !=null) {
					rectangle.setIndexVisible(false);
		}
	}
		
	public String getLabel() {
		return rectangle.getLabel();
	}

	public int getBacktrackingLabel() {
		return backtrackingLabel;
	}

	public SynDiaInitial getStartElem() {
		return startElem;
	}

	public void setStartElem(SynDiaInitial startElem) {
		this.startElem = startElem;
	}

	public void markLastConnection(boolean marked) {
		rectangle.highlightIncomingConnection(marked);
	}

	public void markNextConnection(boolean marked) {
		rectangle.highlightExitingConnection(marked);
	}

	public void markObjekt(boolean marked) {
		rectangle.highlight(marked);
	}
	
	public void markObject() {
		rectangle.setBackgroundColor(currentFigure);
	}
	
	public void remarkObject(boolean bool){
		rectangle.setBackgroundColor(null);
		markObjekt(bool);
	}
	
	/**
	 * @return
	 */
	public VariableFigure getGfx() {
		return rectangle;
	}

	/**
	 * @param figure
	 */
	public void setGfx(VariableFigure figure) {
		rectangle = figure;
		if (figure !=null) {rectangle.setIndexText(""+backtrackingLabel);}  //$NON-NLS-1$
	}

	/**
	 * @return
	 */
	public SynDiaVariableBack getHelpCopy() {
		return helpCopy;
	}

	/**
	 * @param back
	 */
	public void setHelpCopy(SynDiaVariableBack back) {
		helpCopy = back;
	}

}