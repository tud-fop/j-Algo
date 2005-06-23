/* Created on 12.06.2005 */
package org.jalgo.module.avl.gui.graphics;

import java.awt.Color;
import java.awt.Font;

/**
 * The class <code>GraphicsConstants</code> is a collection of global constants
 * for visualization in AVL module. It contains changeable and not changeable
 * constants. Changeable constants are realized as an array, where the first entry
 * defines the value of the constant in pc mode, and the second constant defines
 * the value of the constant in beamer mode. To get the currently set value,
 * you get the index from the <code>Settings</code> class.
 * 
 * @author Alexander Claus
 */
public interface GraphicsConstants {

	/*                        changeable constants                    */

	/*--------------------------graphical layout----------------------*/
	/** The distance between two nodes along the x axis. */
	public static final int[] X_DIST_NODES = new int[] {35, 46};
	/** The distance between two nodes along the y axis. */
	public static final int[] Y_DIST_NODES = new int[] {60, 75};
	/** The diameter of a node. */
	public static final int[] NODE_DIAMETER = new int[] {34, 45};
	/** The distance between focused node and working node along x axis. */
	public static final int[] X_DIST_WN = new int[] {40, 51};

	/*------------------------------fonts-----------------------------*/	
	/** The font used for the key in a node. */
	public static final Font[] KEY_FONT = new Font[] {
		new Font("SansSerif", Font.BOLD, 18),
		new Font("SansSerif", Font.BOLD, 23)};
	/** The font used for the balance of a node. */
	public static final Font[] BALANCE_FONT = new Font[] {
		new Font("SansSerif", Font.BOLD, 13),
		new Font("SansSerif", Font.BOLD, 15)};
	/** The font used for the balance of a node when it is highlighted. */
	public static final Font[] BALANCE_FONT_RED = new Font[] {
		new Font("SansSerif", Font.BOLD, 18),
		new Font("SansSerif", Font.BOLD, 20)};
	/** The font used for the caption of the rotation-arrow. */
	public static final Font[] ROTATION_ARROW_FONT = new Font[] {
		new Font("SansSerif", Font.BOLD, 18),
		new Font("SansSerif", Font.BOLD, 23)};
	
	/*------------------------------colors----------------------------*/
	/** The color the worknode is filled with. */
	public static final Color[] COLOR_FOCUSED_WNODE = new Color[]{
		Color.RED, Color.RED.darker()};
	
	/*--------------------------paint area margins--------------------*/
	/** The top margin of the paint area. */
	public static final int[] MARGIN_TOP = new int[] {50, 60};
	/** The bottom margin of the paint area. */
	public static final int[] MARGIN_BOTTOM = new int[] {50, 60};
	/** The left margin of the paint area. */
	public static final int[] MARGIN_LEFT = new int[] {70, 80};
	/** The right margin of the paint area. */
	public static final int[] MARGIN_RIGHT = new int[] {70, 80};

	/*                    not changeable constants                    */

	/*------------------------------colors----------------------------*/
	/** The color a focused node is filled with. */
	public static final Color COLOR_FOCUSED_NODE = new Color(50,200,0);
	/** The color a focused arrow is painted with. */
	public static final Color COLOR_FOCUSED_ARROW = new Color(50,180,0);
}