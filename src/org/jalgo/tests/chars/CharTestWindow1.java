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
 * Created on 10.12.2004
 */
package org.jalgo.tests.chars;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * @author Michael Pradel
 */
public class CharTestWindow1 extends ApplicationWindow {
	
	Composite parent;
	
	public CharTestWindow1 () {
		super(null);
	}
	
	protected Control createContents(Composite parent) {
		parent.getShell().setText("Character Test 1");
		parent.getShell().setSize(400,250);
		
		Label label = new Label(parent, SWT.VERTICAL);
		label.setText("Umlaute:\n" +
			"ä - ae\n" +
			"ö - oe\n" +			"ü - ue\n" +			"\n" +
			"griechische Buchstaben:\n" +			"\u03B5 - Epsilon\n" +			"\u03A3 - Sigma\n" +			"\u03B1\u2081 - Alpha unten 1\n" +			"\n" +			"Sonstiges:\n" +			"\u2208 - ist Element");

		return label;
	}
	
	/*
	 * -Djava.library.path=/home/michi/eclipse_fuer_sopra/eclipse/plugins/org.eclipse.swt.gtk_2.1.3/os/linux/x86
	 */

}
