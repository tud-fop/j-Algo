/*
 * Created on 10.12.2004
 */
package org.jalgo.tests.chars;

import org.eclipse.swt.widgets.Display;

/**
 * @author Michael Pradel
 */
public class CharTest1 {

	public static void main(String[] args) {
		CharTestWindow1 win = new CharTestWindow1();
		win.setBlockOnOpen(true);
		win.open();
		
		Display.getCurrent().dispose();
	}
}
