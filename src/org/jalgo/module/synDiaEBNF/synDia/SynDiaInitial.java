/*
 * Created on 22.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.synDia;

import org.jalgo.module.synDiaEBNF.gfx.InitialFigure;

/**
 * @author Babett Schaliz
 */
public class SynDiaInitial extends SynDiaElement {

	private SynDiaElement element;
	private InitialFigure gfx;
	
	public SynDiaInitial() {
		this(null, null);
	}
	
	public SynDiaInitial(InitialFigure gfx, SynDiaElement element) {
		this.gfx=gfx;
		this.element=element;
	}

	public SynDiaElement getInnerElem() {
		return element;
	}

	public void setInnerElem(SynDiaElement newElem) {
		element=newElem;
	}
	
	/**
	* @return
	*/
	public InitialFigure getGfx() {
		return gfx;
	}

	/**
	 * @param figure
	 */
	public void setGfx(InitialFigure figure) {
		gfx = figure;
	}


}
