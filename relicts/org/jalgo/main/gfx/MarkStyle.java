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
 
package org.jalgo.main.gfx;

import org.eclipse.swt.graphics.Color;

/**
 * This class is a container for attributes which represents the look
 * of a mark in a TextCanvas.
 * 
 * @author Cornelius Hald
 */
public class MarkStyle {

	private Color forground;
	private Color backgound;
	private int fontstyle;

	/**
	 * Creates a MarkStyle Object, which holds informations of the visual
	 * representation of Marks in a TextCanvas.<p>
	 * 
	 * Possible fontsyles are:
	 * <li> SWT.NORMAL
	 * <li> SWT.BOLD
	 * <li> SWT.ITALIC
	 * 
	 * @param forground		The Color of the text forground
	 * @param background	The Color of the text background
	 * @param fontstyle		The fontstyle. May be NORMAL, BOLD or ITALIC
	 */
	public MarkStyle(Color forground, Color background, int fontstyle) {
		this.forground = forground;
		this.backgound = background;
		this.fontstyle = fontstyle;
	}

	public Color getBgColor() {
		return backgound;
	}

	public Color getFgColor() {
		return forground;
	}

	public int getFontstyle() {
		return fontstyle;
	}
}