/*
 * Created on 29.04.2004
 */
 
package org.jalgo.module.synDiaEBNF.ebnf;

import java.io.Serializable;
import java.util.List;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Babett Schaliz
 * @author Stephan Creutz
 * 
 * symbolize syntax variable in an EBNF-term 
 */
public class EbnfSynVariable extends EbnfElement implements Serializable {
	private String label;
	private EbnfElement startElem;

	public EbnfSynVariable() {
		label = new String(""); //$NON-NLS-1$
	}

	public EbnfSynVariable(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public EbnfElement getStartElem() {
		return startElem;
	}

	public void setStartElem(EbnfElement start) {
		startElem = start;
	}

	public boolean equals(Object o) {
		if (o instanceof EbnfSynVariable) {
			return ((EbnfSynVariable) o).getLabel().equals(label);
		}
		return super.equals(o);
	}
	
	public int render(Shell shell, List styleList, int pos) {
		styleList.add(new StyleRange(pos, label.length(), null, null));
		return pos + label.length();
	}
	
	public String toString() {
		return label;
	}
}
