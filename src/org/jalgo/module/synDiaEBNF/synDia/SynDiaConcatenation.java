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
 * Created on 17.05.2004
 */

package org.jalgo.module.synDiaEBNF.synDia;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.jalgo.module.synDiaEBNF.gfx.ConcatenationFigure;

/**
 * @author Michael Pradel
 */
public class SynDiaConcatenation extends SynDiaComposition implements
		Serializable {

	private static final long serialVersionUID = 5490760021030232097L;

	private List<SynDiaElement> elements;

	private ConcatenationFigure gfx;

	public SynDiaConcatenation() {
		elements = new LinkedList<SynDiaElement>();
	}

	public SynDiaConcatenation(ConcatenationFigure gfx,
			List<SynDiaElement> elements) {
		this.gfx = gfx;
		this.elements = elements;
	}
	
	public int getNumOfElements() {
		return elements.size();
	}

	public List<SynDiaElement> getContent() {
		//read from left-to-right? --> inverse list!
		if (this.getReadingOrder() == ReadingOrder.RIGHT_TO_LEFT)
			return inverse(elements);
		return elements;
	}

	public SynDiaElement getContent(int num) {
		//read from left-to-right? --> inverse list!
		if (this.getReadingOrder() == ReadingOrder.RIGHT_TO_LEFT) {
			List elementsInverted = inverse(elements);
			if (elementsInverted.get(num) instanceof SynDiaElement) {
				return (SynDiaElement) elementsInverted
						.get(num);
			}
		}
		return elements.get(num);
	}

	public void setContent(int num, SynDiaElement newElem) {
		elements.set(num, newElem);
	}

	public void setContent(List<SynDiaElement> elements) {
		this.elements = elements;
	}

	public void addElem(SynDiaElement elem) {
		elements.add(elem);
	}

	public boolean removeElem(SynDiaElement elem) {
		return elements.remove(elem);
	}

	public ConcatenationFigure getGfx() {
		return gfx;
	}

	public void setGfx(ConcatenationFigure figure) {
		gfx = figure;
	}

	/**
	 * Inverses a <code>LinkedList</code>.
	 * @param l The list to invert.
	 * @return The inverted list.
	 */
	private <T> List<T> inverse(List<T> list) {
		List<T> invertedList = new LinkedList<T>();
		for (T item : list) {
			invertedList.add(0, item);
		}
		return invertedList;
	}
}