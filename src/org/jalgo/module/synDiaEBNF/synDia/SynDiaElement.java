/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
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

import org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure;

/**
 * @author Michael Pradel
 * @author Stephan Creutz
 */
public abstract class SynDiaElement
	implements Serializable {

	private ReadingOrder readingOrder = ReadingOrder.LEFT_TO_RIGHT;

	public ReadingOrder getReadingOrder() {
		return readingOrder;
	}

	public void setReadingOrder(ReadingOrder readingOrder) {
		this.readingOrder = readingOrder;
	}

	public void changeReadingOrder() {
		if (readingOrder == ReadingOrder.LEFT_TO_RIGHT)
			readingOrder = ReadingOrder.RIGHT_TO_LEFT;
		else
			readingOrder = ReadingOrder.LEFT_TO_RIGHT;
	}

	/**
	 * Checks recursively the reading orders of all elements and sets them correctly.
	 * The reading order of an element indicates the direction (from left-to-right or from 
	 * right-to-left) you have to go through in a syntactical diagram.
	 * @param r The reading order of the element to start with.
	 */
	public void checkReadingOrder(ReadingOrder readingOrder) {
		this.setReadingOrder(readingOrder);
		if (this instanceof SynDiaComposition) {
			if (this instanceof SynDiaConcatenation) {
				//set all inner elements of concatenation to value of this element
				for (SynDiaElement item : ((SynDiaConcatenation) this).getContent())
					item.checkReadingOrder(readingOrder);
			} else if (this instanceof SynDiaAlternative) {
				//set all inner elements of alternative to value of this element
				for (SynDiaElement item : ((SynDiaAlternative) this).getOptions())
					item.checkReadingOrder(readingOrder);
			} else if (this instanceof SynDiaRepetition) {
				//set straightAheadElement to value of this element and repeatedElement to inverse
				ReadingOrder rInverse;
				if (readingOrder == ReadingOrder.LEFT_TO_RIGHT)
					rInverse = ReadingOrder.RIGHT_TO_LEFT;
				else
					rInverse = ReadingOrder.LEFT_TO_RIGHT;
				((SynDiaRepetition) this).getStraightAheadElem().checkReadingOrder(readingOrder);
				((SynDiaRepetition) this).getRepeatedElem().checkReadingOrder(rInverse);
			}
		}
	}

	public abstract SynDiaFigure getGfx();
	//public abstract void setGfx(SynDiaFigure figure);
}
