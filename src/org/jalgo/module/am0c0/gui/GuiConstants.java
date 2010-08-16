/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2010 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
package org.jalgo.module.am0c0.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;

import org.jalgo.main.gui.JAlgoWindow;

/**
 * All constants for GUI elements are defined here.
 * 
 * @author Max Leuth&auml;user
 */
public class GuiConstants {
	/** The presentation font */
	public static final Font PRESENTATIONFONT = new Font("Courier", Font.BOLD,
			16);
	/** The standard font */
	public static final Font STANDARDFONT = new Font("Courier", Font.PLAIN, 14);

	public static final Font STANDARDFONT_SERIF = new Font(Font.SERIF, Font.PLAIN, 14);
	public static final Font PRESENTATIONFONT_SERIF = new Font(Font.SERIF, Font.PLAIN, 16);

	public static final String BACK_TO_EDITOR_ICON = "back_to_editor_Icon"; //$NON-NLS-1$
	public static final String CLEAR_ICON = "clear_Icon"; //$NON-NLS-1$
	public static final String INITIAL_CONFIG_ICON = "initial_config_Icon"; //$NON-NLS-1$
	public static final String LAST_ICON = "last_Icon"; //$NON-NLS-1$
	public static final String NEXT_ICON = "next_Icon"; //$NON-NLS-1$
	public static final String OPEN_ICON = "open_Icon"; //$NON-NLS-1$
	public static final String PREVIOUS_ICON = "previous_Icon"; //$NON-NLS-1$
	public static final String RUN_ICON = "run_Icon"; //$NON-NLS-1$
	public static final String SAVE_ICON = "save_Icon"; //$NON-NLS-1$

	/**
	 * Standard grey for our am0 table
	 */
	public static final Color TABLE_ODD = new Color(237, 237, 237);
	/**
	 * Standard white for our am0 table
	 */
	public static final Color TABLE_EVEN = new Color(255, 255, 255);

	/**
	 * This method returns the {@link JAlgoWindow} or throw a
	 * {@link RuntimeException} if it was unable to find it.
	 * 
	 * @return the {@link JAlgoWindow}
	 */
	public static JAlgoWindow getJAlgoWindow() {
		for (Frame f : Frame.getFrames())
			if (f instanceof JAlgoWindow)
				return (JAlgoWindow) f;

		throw new RuntimeException("JAlgoWindow not found"); //$NON-NLS-1$
	}
}
