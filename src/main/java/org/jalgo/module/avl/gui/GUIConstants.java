/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer science. It is written in Java and platform independent. j-Algo is developed with the help of Dresden University of Technology.
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

/* Created on 19.05.2005 */
package org.jalgo.module.avl.gui;

import java.awt.Color;
import java.awt.Font;

import org.jalgo.module.avl.Constants;

/**
 * The interface <code>Constants</code> is a collection of several constant values,
 * used in the GUI implementation of the AVL module.
 * 
 * @author Alexander Claus
 */
public interface GUIConstants
extends Constants {

	/*------------------------display modes--------------------------*/
	/** Indicates, that display is tuned for working on a pc screen. */
	public static final int PC_MODE = 0;
	/** Indicates, that display is tuned for working on a beamer. */
	public static final int BEAMER_MODE = 1;

	/*------------------------validity states-------------------------*/
	/** Indicates a valid input value. */
	public final static int VALID_INPUT = 0;
	/** Indicates that input is no integer. */
	public final static int NO_INTEGER = 1;
	/** Indicates that input value is out of the acceptable range. */
	public final static int NOT_IN_RANGE = 2;
	/** Indicates that input is empty. */
	public final static int INPUT_EMPTY = 3;

	/*------------------------------colors----------------------------*/
	/** The background color in standard layout. */
	public final static Color STANDARD_BACKGROUND = new Color(212, 208, 200);
	/** The background color in the welcome screen. */
	public final static Color WELCOME_SCREEN_BACKGROUND = new Color(51, 102, 153);
	/** The font color in the welcome screen. */
	public final static Color WELCOME_SCREEN_FOREGROUND = new Color(254, 215, 0);

	/*------------------------------fonts-----------------------------*/
	/** The font used in the welcome screen. */
	public static final Font WELCOME_SCREEN_FONT =
		new Font("SansSerif", Font.PLAIN, 16);
	
	/*------------------------visualization modes---------------------*/
	/** Indicates that automatical visualisation animation is selected. */
	public static final int AUTOMATICAL = 0;
	/** Indicates that stepwise visualisation is selected. */
	public static final int STEPWISE = 1;
	/** Indicates that no visualisation is selected. */
	public static final int NO_VISUALIZATION = 2;

	/*--------------------------message types-------------------------*/
	/** Indicates that displayed message is reset. */
	public static final int NO_MESSAGE = 0;
	/** Indicates a message to be an error message. */
	public static final int ERROR_MESSAGE = 1;
	/** Indicates a message to be an information message. */
	public static final int INFORMATION_MESSAGE = 2;
}