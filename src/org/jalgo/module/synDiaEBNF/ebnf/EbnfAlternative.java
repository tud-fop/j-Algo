/*
 * Created on 29.04.2004
 */
package org.jalgo.module.synDiaEBNF.ebnf;

import java.io.Serializable;
import java.util.List;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;

/**
 * symbolize EBNF-terms of the form x=(left|right) where left and right 
 * are new terms and (,|,) are metasymbols
 *  
 * @author Babett Schaliz
 * @author Stephan Creutz
 */
public class EbnfAlternative extends EbnfComposition implements Serializable {

	private EbnfElement left;
	private EbnfElement right;

	public EbnfAlternative() {
	}

	public EbnfAlternative(EbnfElement left, EbnfElement right) {
		this.right = right;
		this.left = left;
	}

	public EbnfElement getLeft() {
		return left;
	}

	public void setLeft(EbnfElement left) {
		this.left = left;
	}

	public void setRight(EbnfElement right) {
		this.right = right;
	}

	public EbnfElement getRight() {
		return right;
	}

	public int render(Shell shell, List styleList, int pos) {
		Color color = shell.getDisplay().getSystemColor(ALTERNATIVE_COLOR);
		styleList.add(new StyleRange(pos, 1, color, null));
		pos = left.render(shell, styleList, pos + 1);
		styleList.add(new StyleRange(pos, 1, color, null));
		pos = right.render(shell, styleList, pos + 1);
		styleList.add(new StyleRange(pos, 1, color, null));
		return pos + 1;
	}

	public String toString() {
		return "(" + left.toString() + "|" + right.toString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}