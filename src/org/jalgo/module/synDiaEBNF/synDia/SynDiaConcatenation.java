/*
 * Created on 17.05.2004
 */
 
package org.jalgo.module.synDiaEBNF.synDia;

import java.io.Serializable;
import java.util.LinkedList;

import org.jalgo.module.synDiaEBNF.gfx.ConcatenationFigure;

/**
 * @author Michael Pradel
 */
public class SynDiaConcatenation extends SynDiaComposition implements Serializable {

	private LinkedList elements;
	private ConcatenationFigure gfx;

	public SynDiaConcatenation() {
		elements = new LinkedList();
	}

	public SynDiaConcatenation(ConcatenationFigure gfx, LinkedList elements) {
		this.gfx=gfx;
		this.elements=elements;
	}

	public int getNumOfElements() {
		return elements.size();
	}

	public LinkedList getContent() {
		return elements;
	}

	public SynDiaElement getContent(int num) {
		if (elements.get(num) instanceof SynDiaElement) {
			return (SynDiaElement) elements.get(num);
		}
		return null;
	}

	public void setContent(int num, SynDiaElement newElem) {
		elements.set(num, newElem);
	}
	
	public void setContent(LinkedList elements) {
		this.elements = elements;
	}

	public void addElem(SynDiaElement elem) {
		elements.addLast(elem);
	}

	public boolean removeElem(SynDiaElement elem) {
		return elements.remove(elem);
	}

	/**
	 * @return
	 */
	public ConcatenationFigure getGfx() {
		return gfx;
	}

	/**
	 * @param figure
	 */
	public void setGfx(ConcatenationFigure figure) {
		gfx = figure;
	}

}