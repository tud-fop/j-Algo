/*
 * Created on 17.05.2004
 */
 
package org.jalgo.module.synDiaEBNF.synDia;

import java.io.Serializable;
import java.util.LinkedList;

import org.jalgo.module.synDiaEBNF.gfx.AlternativeFigure;

/**
 * @author Michael Pradel
 */
public class SynDiaAlternative
	extends SynDiaComposition
	implements Serializable {

	private LinkedList options;
	private AlternativeFigure gfx;

	public SynDiaAlternative() {
		options = new LinkedList();
	}
	
	public SynDiaAlternative(AlternativeFigure gfx, LinkedList options) {
		this.options=options;
		this.gfx=gfx;
	}

	public int getNumOfOptions() {
		return options.size();
	}

	public LinkedList getOptions() {
		return options;
	}

	public SynDiaElement getOption(int num) {
		if (options.get(num) instanceof SynDiaElement) {
			return (SynDiaElement) options.get(num);
		}
		return null;
	}

	public void setOption(int num, SynDiaElement newOption) {
		options.set(num, newOption);
	}

	public void setOptions(LinkedList options) {
		this.options = options;
	}

	public void addOption(SynDiaElement option) {
		options.addLast(option);
	}

	public boolean removeOption(SynDiaElement optionToRemove) {
		return options.remove(optionToRemove);
		
	}
	/**
	 * @return
	 */
	public AlternativeFigure getGfx() {
		return gfx;
	}

	/**
	 * @param figure
	 */
	public void setGfx(AlternativeFigure figure) {
		gfx = figure;
	}

}