/*
 * Created on 03.05.2004
 */
 
package org.jalgo.module.synDiaEBNF.synDia;

import java.io.Serializable;

import org.jalgo.module.synDiaEBNF.ebnf.EbnfElement;
import org.jalgo.module.synDiaEBNF.gfx.ToTransFigure;

/**
 * element of a syntactical diagramm, which is only required in the 
 * TransAlgorithm it's a rounded rectangle holding the first ebnfElem of a 
 * still-to-trans ebnf-term
 * 
 * @author Michael Pradel
 * @author Benjamin Scholz
 * @author Marco Zimmerling
 */
public class SynDiaToTrans extends SynDiaElement implements Serializable {

	private EbnfElement stillToTrans;
	private ToTransFigure gfx;

	public SynDiaToTrans() {
	}

	public SynDiaToTrans(EbnfElement stillToTrans) {
		this.stillToTrans = stillToTrans;
	}

	/**
	 * 
	 * @return something like "trans(abc)"
	 */
	public String getLabel() {
		return stillToTrans.toString();
	}

	/**
	 * @return
	 */
	public ToTransFigure getGfx() {
		return gfx;
	}

	/**
	 * @param figure
	 */
	public void setGfx(ToTransFigure figure) {
		gfx = figure;
	}
	
	public EbnfElement getEbnf() {
		return stillToTrans;
	}
}
