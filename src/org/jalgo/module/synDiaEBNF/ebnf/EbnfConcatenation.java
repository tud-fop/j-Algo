/*
 * Created on 29.04.2004
 */
 
package org.jalgo.module.synDiaEBNF.ebnf;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;

/**
 * symbolize EBNF-terms of the form x=listelem1 listelem2 .... where listelemX 
 * are new terms 
 * 
 * @author Babett Schaliz
 * @author Michael Pradel
 * @author Stephan Creutz
 */
public class EbnfConcatenation
	extends EbnfComposition
	implements Serializable {

	private LinkedList elements;

	public EbnfConcatenation() {
		elements = new LinkedList();
	}

	public EbnfConcatenation(LinkedList elements) {
		this.elements = elements;
	}

	public int getNumOfElements() {
		return elements.size();
	}

	public LinkedList getContent() {
		return elements;
	}

	public EbnfElement getContent(int num) {
		if (elements.get(num) instanceof EbnfElement) {
			return (EbnfElement) elements.get(num);
		}
		return null;
	}

	public void setContent(int num, EbnfElement newElem) {
		elements.set(num, newElem);
	}

	public void setContent(LinkedList elements) {
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

	public int render(Shell shell, List styleList, int pos) {
		for (Iterator it = elements.iterator(); it.hasNext();) {
			pos = ((EbnfElement) it.next()).render(shell, styleList, pos);
		}
		return pos;
	}

	public String toString() {
		String str = ""; //$NON-NLS-1$
		for (Iterator it = elements.iterator(); it.hasNext();) {
			str += ((EbnfElement) it.next()).toString();
		}
		return str;
	}
}