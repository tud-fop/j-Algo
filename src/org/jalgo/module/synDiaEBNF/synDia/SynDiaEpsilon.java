/*
 * Created on 17.06.2004
 */
  
package org.jalgo.module.synDiaEBNF.synDia;

import java.io.Serializable;

import org.jalgo.module.synDiaEBNF.gfx.EmptyFigure;

/**
 * @author Babett Schaliz
 */
public class SynDiaEpsilon extends SynDiaElement implements Serializable {

	private EmptyFigure epsilon; // the graphical figure

	public SynDiaEpsilon(){
		epsilon = new EmptyFigure();
	}
	
	public SynDiaEpsilon(EmptyFigure epsilon){
		this.epsilon = epsilon;
	}
	
	public EmptyFigure getFigure(){
		return epsilon;
	} 
	
	public void setFigure(EmptyFigure epsilon){
		this.epsilon=epsilon;
	}
}
