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
 * Created on 29.04.2004
 */

package org.jalgo.module.synDiaEBNF.synDia;

import java.io.Serializable;

/**
 * @author Michael Pradel
 */
public abstract class SynDiaElement
	implements Serializable, IReadingOrderConstants {

	private int readingOrder = LEFT_TO_RIGHT;

	public int getReadingOrder() {
		return readingOrder;
	}

	public void setReadingOrder(int r) {
		readingOrder = r;
	}

	public void changeReadingOrder() {
		if (readingOrder == LEFT_TO_RIGHT)
			readingOrder = RIGHT_TO_LEFT;
		else
			readingOrder = LEFT_TO_RIGHT;
	}

	/**
	 * Checks recursively the reading orders of all elements and sets them correctly.
	 * The reading order of an element indicates the direction (from left-to-right or from 
	 * right-to-left) you have to go through in a syntactical diagram.
	 * @param r The reading order of the element to start with.
	 */
	public void checkReadingOrder(int r) {
		this.setReadingOrder(r);
		if (this instanceof SynDiaComposition) {
			if (this instanceof SynDiaConcatenation) {
				//set all inner elements of concatenation to value of this element
				for (int i = 0;
					i < (((SynDiaConcatenation) this).getContent()).size();i++) {
					((SynDiaConcatenation) this).getContent(i).checkReadingOrder(r);
				}
			} else if (this instanceof SynDiaAlternative) {
				//set all inner elements of alternative to value of this element
				for (int i = 0;
					i < (((SynDiaAlternative) this).getOptions()).size();i++) {
					((SynDiaAlternative) this).getOption(i).checkReadingOrder(r);
				}
			} else if (this instanceof SynDiaRepetition) {
				//set straightAheadElement to value of this element and repeatedElement to inverse
				int rInverse;
				if (r == LEFT_TO_RIGHT)
					rInverse = RIGHT_TO_LEFT;
				else
					rInverse = LEFT_TO_RIGHT;
				((SynDiaRepetition) this).getStraightAheadElem().checkReadingOrder(r);
				((SynDiaRepetition) this).getRepeatedElem().checkReadingOrder(rInverse);
			}
		}
	}

}
