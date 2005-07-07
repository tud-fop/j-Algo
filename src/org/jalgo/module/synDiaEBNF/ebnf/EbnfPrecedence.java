/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
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

	private static final long serialVersionUID = 8287444742704734873L;

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

	public int render(Shell shell, List<StyleRange> styleList, int pos) {
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