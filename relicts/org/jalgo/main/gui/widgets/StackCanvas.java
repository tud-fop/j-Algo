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
 * Created on May 31, 2004
 */
 
package org.jalgo.main.gui.widgets;

import java.util.Stack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @author Cornelius Hald
 * @author Christopher Friedrich
 */
/*
 * This is an adapter because Java don't support multiple inheritance
 */
public class StackCanvas extends Composite {
	private Stack<String> stack;
	private Text textField;

	private void redrawStack() {
		textField.setText(""); //$NON-NLS-1$
		StringBuffer strBuffer = new StringBuffer();
		
		for (String item : stack) {
			strBuffer.insert(0, item + "\n"); //$NON-NLS-1$
		}
		
		textField.setText(strBuffer.toString());
	}

	public StackCanvas(Composite parent, int style) {		
		super(parent, style);
		
		setLayout(new FillLayout());
		textField = new Text(this, SWT.MULTI);
		stack = new Stack<String>();
	}

	public void push(String element) {
		stack.push(element);
		redrawStack();
	}

	public String pop() {
		String obj = null;
		if (!stack.isEmpty()) {
			obj = stack.pop();
		}
		redrawStack();
		return obj;
	}

	public void reset() {
		stack.clear();
	}

	public Stack getStack() {
		return stack;
	}

	public void setStack(Stack<String> stack) {
		this.stack = stack;
	}
}