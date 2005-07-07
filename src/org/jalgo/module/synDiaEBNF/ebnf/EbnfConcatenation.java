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

package org.jalgo.module.synDiaEBNF.ebnf;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Shell;

/**
 * symbolize EBNF-terms of the form x=listelem1 listelem2 .... where listelemX
 * are new terms
 * 
 * @author Babett Schaliz
 * @author Michael Pradel
 * @author Stephan Creutz
 */
public class EbnfConcatenation extends EbnfComposition implements Serializable {

	private static final long serialVersionUID = 3723908500641089028L;

	private LinkedList<EbnfElement> elements;

	public EbnfConcatenation() {
		elements = new LinkedList<EbnfElement>();
	}

	public EbnfConcatenation(LinkedList<EbnfElement> elements) {
		this.elements = elements;
	}

	public int getNumOfElements() {
		return elements.size();
	}

	public LinkedList getContent() {
		return elements;
	}

	public EbnfElement getContent(int num) {
		return elements.get(num);
	}

	public void setContent(int num, EbnfElement newElem) {
		elements.set(num, newElem);
	}

	public void setContent(LinkedList<EbnfElement> elements) {
		this.elements = elements;
	}

	public void addElem(EbnfElement elem) {
		elements.addLast(elem);
	}

	public void addElem(int num, EbnfElement elem) {
		elements.add(num, elem);
	}

	public boolean removeElem(EbnfElement elem) {
		return elements.remove(elem);
	}

	public int render(Shell shell, List<StyleRange> styleList, int pos) {
		for (Iterator it = elements.iterator(); it.hasNext();) {
			pos = ((EbnfElement) it.next()).render(shell, styleList, pos);
		}
		return pos;
	}

	public String toString() {
		StringBuffer strBuffer = new StringBuffer();
		for (Iterator it = elements.iterator(); it.hasNext();) {
			strBuffer.append(((EbnfElement) it.next()).toString());
		}
		return strBuffer.toString();
	}
}