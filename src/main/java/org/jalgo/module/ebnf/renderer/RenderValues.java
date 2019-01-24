/**
 * 
 */
package org.jalgo.module.ebnf.renderer;

import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.JPanel;

import org.jalgo.module.ebnf.gui.EbnfFont;
import org.jalgo.module.ebnf.gui.FontNotInitializedException;
import org.jalgo.module.ebnf.gui.syndia.display.DiagramSize;

/**
 * This class holds all preferences for rendering.
 * 
 * @author Andre Viergutz
 */
public class RenderValues {

	/**
	 * Represents a terminal symbol
	 */
	public static int TERMINAL = 0;

	/**
	 * Represents a variable
	 */
	public static int VARIABLE = 1;

	/**
	 * Represents a concatenation
	 */
	public static int CONCATENATION = 2;

	/**
	 * Represents a branch
	 */
	public static int BRANCH = 3;
	
	/**
	 * Represents a branch
	 */
	public static int BRANCHNOSTAIRS = 4;
	

	/**
	 * Represents a repetition
	 */
	public static int REPETITION = 5;

	/**
	 * Represents a null element
	 */
	public static int NULLELEM = 6;

	/**
	 * Represents a null element
	 */
	public static int TRANSTERM = 7;

	/**
	 * Represents a ghost object for creating the FontMetrics obect
	 */
	private static JPanel PANEL = new JPanel();

	/**
	 * Represents the radius of the rounded rectangle and the arc angle of the
	 * branch and repetition
	 */
	public int radius = 15;

	/**
	 * Represents the space between two syntax diagram elements
	 */
	public int space = 15;
	
	/**
	 * Represents whether to render with or without stairs at multiple branches
	 */
	public boolean withStairs = true;
	

	/**
	 * Represents the font of the symbols
	 */
	public Font font;
	/**
	 * Represent the font metrics for calculating the string width in pixel
	 */
	public FontMetrics fm; 

	/**
	 *  Sets the size of the font saved in <code>DiagramSize</code>
	 */
	public RenderValues() {
		
		this.setFontSize(DiagramSize.getFontSize());
		
	}
	
	
	/**
	 * Sets the size of the actual Font
	 * 
	 * @param size The size
	 */
	public void setFontSize(int size) {

		try {
			this.font = EbnfFont.getFont().deriveFont(Font.PLAIN, size);
		} catch (FontNotInitializedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fm = PANEL.getFontMetrics(font);
		radius = (int) Math.round(0.6 * fm.getHeight());
		space = radius;
	}

	/**
	 * Calculates the width of the given String object in pixel
	 * 
	 * @param s The String object to calculate
	 * @return the width of the String object in pixel
	 */
	public int getWidthFromString(String s) {

		return fm.stringWidth(s);

	}
	
	/**
	 * Calculates the width of the given String object in pixel
	 * 
	 * @param s The String object to calculate
	 * @param f The Font which size is to calculate
	 * @return the width of the String object in pixel
	 */
	public int getWidthFromString(String s, Font f) {
		
		FontMetrics fontmetrics = PANEL.getFontMetrics(f);
				
		return fontmetrics.stringWidth(s);

	}

}
