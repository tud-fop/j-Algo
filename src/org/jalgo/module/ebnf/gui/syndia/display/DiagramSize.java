/**
 * 
 */
package org.jalgo.module.ebnf.gui.syndia.display;

/**
 * @author Andre Viergutz
 */
public class DiagramSize {

	private static int fontSize = 17;

	/**
	 * Sets the size of the Font.
	 * 
	 * @param fsize
	 */
	public static void setFontSize(int fsize) {

		fontSize = fsize;

	}

	/**
	 * This functions returns the set font size. 
	 *  
	 * @return an int representing the size of the Font
	 */
	public static int getFontSize() {

		return fontSize;

	}

}
