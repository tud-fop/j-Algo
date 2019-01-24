/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

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
	 * The display mode indicates if the user is working on a pc display or on a
	 * beamer. This integer can be one of the constants <code>PC_MODE</code>,
	 * <code>BEAMER_MODE</code> defined in <code>GUIConstants</code>.
	 */
	public static int getDisplayMode() {
		return org.jalgo.main.util.Settings.getBoolean("avl", "BeamerMode")
			? BEAMER_MODE : PC_MODE;
		}

	public static void setDisplayMode(int mode) {
		org.jalgo.main.util.Settings.setBoolean("avl", "BeamerMode",
			(mode == BEAMER_MODE));
	}

	/** The delay time of steps in animation in milliseconds. */
	private static long STEP_DELAY = 500;

	public static long getStepDelay() {
		return STEP_DELAY;
	}

	public static void setStepDelay(long delay) {
		STEP_DELAY = delay;
	}

	/** The normal font style for log and documentation pane. */
	public static SimpleAttributeSet[] NORMAL_STYLE;

	/** The highlighted font style for log pane. */
	public static SimpleAttributeSet[] HIGHLIGHTED_STYLE;

	/** The highlighted font style for documentation pane. */
	public static SimpleAttributeSet[] DOCU_HIGHLIGHTED_STYLE;

	static {
		NORMAL_STYLE = new SimpleAttributeSet[] {new SimpleAttributeSet(),
			new SimpleAttributeSet()};
		StyleConstants.setFontFamily(NORMAL_STYLE[PC_MODE],
			"SansSerif"); //$NON-NLS-1$
		StyleConstants.setFontSize(NORMAL_STYLE[PC_MODE], 11);

		StyleConstants.setFontFamily(NORMAL_STYLE[BEAMER_MODE],
			"SansSerif"); //$NON-NLS-1$
		StyleConstants.setFontSize(NORMAL_STYLE[BEAMER_MODE], 15);

		HIGHLIGHTED_STYLE = new SimpleAttributeSet[] {new SimpleAttributeSet(),
			new SimpleAttributeSet()};
		StyleConstants.setFontFamily(HIGHLIGHTED_STYLE[PC_MODE],
			"SansSerif"); //$NON-NLS-1$
		StyleConstants.setFontSize(HIGHLIGHTED_STYLE[PC_MODE], 11);
		StyleConstants.setForeground(HIGHLIGHTED_STYLE[PC_MODE], Color.RED);

		StyleConstants.setFontFamily(HIGHLIGHTED_STYLE[BEAMER_MODE],
			"SansSerif"); //$NON-NLS-1$
		StyleConstants.setFontSize(HIGHLIGHTED_STYLE[BEAMER_MODE], 15);
		StyleConstants.setForeground(HIGHLIGHTED_STYLE[BEAMER_MODE], Color.RED
		.darker());

		DOCU_HIGHLIGHTED_STYLE = new SimpleAttributeSet[] {
			new SimpleAttributeSet(), new SimpleAttributeSet()};
		StyleConstants.setFontFamily(DOCU_HIGHLIGHTED_STYLE[PC_MODE],
			"SansSerif"); //$NON-NLS-1$
		StyleConstants.setFontSize(DOCU_HIGHLIGHTED_STYLE[PC_MODE], 11);
		StyleConstants
		.setForeground(DOCU_HIGHLIGHTED_STYLE[PC_MODE], Color.RED);

		StyleConstants.setFontFamily(DOCU_HIGHLIGHTED_STYLE[BEAMER_MODE],
			"SansSerif"); //$NON-NLS-1$
		StyleConstants.setFontSize(DOCU_HIGHLIGHTED_STYLE[BEAMER_MODE], 11);
		StyleConstants.setForeground(DOCU_HIGHLIGHTED_STYLE[BEAMER_MODE],
			Color.RED.darker());
	}
}