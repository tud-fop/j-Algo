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
 * symbolize terminalsymbols in the internal Layer
 */
public class EbnfTerminal extends EbnfElement implements Serializable {

	private String label;

	public EbnfTerminal() {
		label = new String(""); //$NON-NLS-1$
	}

	public EbnfTerminal(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean equals(Object o) {
		if (o instanceof EbnfTerminal) {
			return ((EbnfTerminal) o).getLabel().equals(label);
		}
		return label.equals(o);
	}

	public int render(Shell shell, List styleList, int pos) {
		styleList.add(new StyleRange(pos, label.length(), null, null));
		return pos + label.length();
	}

	public String toString() {
		return label;
	}
}