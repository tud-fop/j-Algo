/*
 * Created on 29.04.2004
 */
 
package org.jalgo.module.synDiaEBNF.synDia;

import java.io.Serializable;

import org.jalgo.module.synDiaEBNF.gfx.VariableFigure;

/**
 * @author Babett Schaliz 
 */
public class SynDiaVariableBack
	extends SynDiaElement
	implements Serializable {

	private SynDiaVariable original;
	private SynDiaInitial parentInitial=null;


	public SynDiaVariableBack(SynDiaVariable original, SynDiaInitial parentInitial) {
			this.original=original;
			this.parentInitial=parentInitial;
	}

	public String getLabel() {
		return original.getGfx().getLabel();
	}

	public SynDiaInitial getStartElem() {
		return original.getStartElem();
	}

	public void markObjekt() {
		original.markObject();
	}
	
		public void remarkObject(boolean bool){
		original.remarkObject(bool);
	}
	
	
	public VariableFigure getGfx() {
		return original.getGfx();
	}

	public SynDiaInitial getParentInitial() {
		return parentInitial;
	}

	public void setParentInitial(SynDiaInitial figure) {
		parentInitial = figure;
	}

	public SynDiaVariable getOriginal() {
		return original;
	}
	public void setOriginal(SynDiaVariable variable) {
		original = variable;
	}

}