/* Created on 05.06.2005 */
package org.jalgo.module.avl.gui;

import java.awt.Color;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jalgo.module.avl.gui.graphics.GraphicsConstants;

/**
 * @author Alexander Claus
 */
public class Settings
implements GraphicsConstants, GUIConstants {

	/**
	 * The display mode indicates if the user is working on a pc display or
	 * on a beamer. This integer can be one of the constants
	 * <code>PC_MODE</code>, <code>BEAMER_MODE</code> defined in
	 * <code>GUIConstants</code>.
	 */
	private static int DISPLAY_MODE = PC_MODE;
	public static int getDisplayMode() {return DISPLAY_MODE;}
	public static void setDisplayMode(int mode) {DISPLAY_MODE = mode;}
	
	/** The delay time of steps in animation in milliseconds. */
	private static long STEP_DELAY = 500; 
	public static long getStepDelay() {return STEP_DELAY;}
	public static void setStepDelay(long delay) {STEP_DELAY = delay;}

	/** The normal font style for log and documentation pane. */
	public static SimpleAttributeSet[] NORMAL_STYLE;

	/** The highlighted font style for log pane. */
	public static SimpleAttributeSet[] HIGHLIGHTED_STYLE;

	/** The highlighted font style for documentation pane. */
	public static SimpleAttributeSet[] DOCU_HIGHLIGHTED_STYLE;

	static {
		NORMAL_STYLE = new SimpleAttributeSet[] {
			new SimpleAttributeSet(), new SimpleAttributeSet()};
		StyleConstants.setFontFamily(NORMAL_STYLE[PC_MODE], "SansSerif");
		StyleConstants.setFontSize(NORMAL_STYLE[PC_MODE], 11);

		StyleConstants.setFontFamily(NORMAL_STYLE[BEAMER_MODE], "SansSerif");
		StyleConstants.setFontSize(NORMAL_STYLE[BEAMER_MODE], 15);

		HIGHLIGHTED_STYLE = new SimpleAttributeSet[] {
				new SimpleAttributeSet(), new SimpleAttributeSet()};
		StyleConstants.setFontFamily(HIGHLIGHTED_STYLE[PC_MODE], "SansSerif");
		StyleConstants.setFontSize(HIGHLIGHTED_STYLE[PC_MODE], 11);
		StyleConstants.setForeground(HIGHLIGHTED_STYLE[PC_MODE], Color.RED);
		
		StyleConstants.setFontFamily(HIGHLIGHTED_STYLE[BEAMER_MODE], "SansSerif");
		StyleConstants.setFontSize(HIGHLIGHTED_STYLE[BEAMER_MODE], 15);
		StyleConstants.setForeground(HIGHLIGHTED_STYLE[BEAMER_MODE],
			Color.RED.darker());

		DOCU_HIGHLIGHTED_STYLE = new SimpleAttributeSet[] {
				new SimpleAttributeSet(), new SimpleAttributeSet()};
		StyleConstants.setFontFamily(DOCU_HIGHLIGHTED_STYLE[PC_MODE], "SansSerif");
		StyleConstants.setFontSize(DOCU_HIGHLIGHTED_STYLE[PC_MODE], 11);
		StyleConstants.setForeground(DOCU_HIGHLIGHTED_STYLE[PC_MODE], Color.RED);
		
		StyleConstants.setFontFamily(DOCU_HIGHLIGHTED_STYLE[BEAMER_MODE],
			"SansSerif");
		StyleConstants.setFontSize(DOCU_HIGHLIGHTED_STYLE[BEAMER_MODE], 11);
		StyleConstants.setForeground(DOCU_HIGHLIGHTED_STYLE[BEAMER_MODE],
			Color.RED.darker());
	}
}