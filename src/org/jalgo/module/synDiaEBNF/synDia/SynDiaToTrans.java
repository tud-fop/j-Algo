/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
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
