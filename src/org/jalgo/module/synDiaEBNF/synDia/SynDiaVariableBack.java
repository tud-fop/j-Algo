/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
 *
 * Copyright (C) 2004 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on 29.04.2004
 */

package org.jalgo.module.synDiaEBNF.synDia;

import java.io.Serializable;

import org.jalgo.module.synDiaEBNF.gfx.VariableFigure;

/**
 * Saves information for the <code>SynDiaVariable original</code> 
 * to jump back to <code>parentInitial</code> after the diagram it is
 * pointing to was executed.  
 * 
 * @author Babett Schalitz
 */
public class SynDiaVariableBack extends SynDiaElement implements Serializable {

	private SynDiaVariable original;
	private SynDiaInitial parentInitial = null;

	public SynDiaVariableBack(SynDiaVariable original,
			SynDiaInitial parentInitial) {
		this.original = original;
		this.parentInitial = parentInitial;
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

	public void unmarkObject(boolean bool) {
		original.unmarkObject(bool);
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