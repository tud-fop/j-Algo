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
 * @author Babett Schaliz
 * @author Stephan Creutz
 * 
 * symbolize EBNF-terms of the form x=(content) where content 
 * are new terms and (,) are metasymbols 
 */
public class EbnfPrecedence extends EbnfComposition implements Serializable {

	private EbnfElement content;

	public EbnfPrecedence() {
	}

	public EbnfPrecedence(EbnfElement content) {
		this.content = content;
	}

	public EbnfElement getContent() {
		return content;
	}

	public void setContent(EbnfElement content) {
		this.content = content;
	}

	public int render(Shell shell, List styleList, int pos) {
		Color color = shell.getDisplay().getSystemColor(PRECEDENCE_COLOR);
		styleList.add(new StyleRange(pos, 1, color, null));
		pos = content.render(shell, styleList, pos + 1);
		styleList.add(new StyleRange(pos, 1, color, null));
		return pos + 1;
	}

	public String toString() {
		return "(" + content.toString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}