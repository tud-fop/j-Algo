/*
 * Created on May 31, 2004
 */
 
package org.jalgo.main.gui.widgets;

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jalgo.main.util.Stack;

/**
 * @author Cornelius Hald
 * @author Christopher Friedrich
 */
public class StackCanvas extends Composite {

	private Label label;

	private Stack stack;

	private Text textField;

	private void redrawStack() {
		
		textField.setText(""); //$NON-NLS-1$
		String str = new String();
		
		LinkedList l = stack.getContent();
		Iterator si = stack.getContent().iterator();
		while (si.hasNext()) {
			str = (String) si.next() + "\n" + str; //$NON-NLS-1$
		}
		
		textField.setText(str);
	}

	public StackCanvas(Composite parent, int style) {
		
		super(parent, style);
		
		setLayout(new FillLayout());
		textField = new Text(this, SWT.MULTI);
		stack = new Stack();
	}

	public void push(String element) {
		stack.push(element);
		redrawStack();
	}

	public String pop() {
		String obj = (String) stack.pop();
		redrawStack();
		return obj;
	}

	public void reset() {
		stack.clear();
	}


	public Stack getStack() {
		return stack;
	}


	public void setStack(Stack stack) {
		this.stack = stack;
	}

}
