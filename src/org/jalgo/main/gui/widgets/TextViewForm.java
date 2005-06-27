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
 * Created on May 29, 2004
 */
 
package org.jalgo.main.gui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.main.gui.TextCanvas;

/**
 * @author Cornelius Hald
 * @author Christopher Friedrich
 */
public class TextViewForm extends CustomViewForm {

	private TextCanvas textCanvas;
	
	public TextViewForm (Composite parent, int style) {
		super(parent, style);
		textCanvas = new TextCanvas(this, SWT.FLAT);
		setContent(textCanvas);
	}
	
	public TextCanvas getTextCanvas() {
		return textCanvas;
	}
}
