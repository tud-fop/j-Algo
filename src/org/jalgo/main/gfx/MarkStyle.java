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

	/**
	 * @return
	 */
	public Color getBgColor() {
		return backgound;
	}

	/**
	 * @return
	 */
	public Color getFgColor() {
		return forground;
	}

	/**
	 * @return
	 */
	public int getFontstyle() {
		return fontstyle;
	}
}
