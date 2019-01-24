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
package org.jalgo.module.kmp.gui;

import java.awt.Color;
import java.awt.Font;

import org.jalgo.module.kmp.Constants;

/**
 * The interface <code>Constants</code> is a collection of several constant values,
 * used in the GUI implementation of the KMP module.
 * 
 * @author Danilo Lisske
 */
public interface GUIConstants
extends Constants {

	/*------------------------------colors----------------------------*/
	/** The background color in the welcome screen. */
	public final static Color WELCOME_SCREEN_BACKGROUND = new Color(51, 102, 153);
	/** The font color in the welcome screen. */
	public final static Color WELCOME_SCREEN_FOREGROUND = new Color(254, 215, 0);
	/** The color of the VglInd-pointer in the table visualisation phase one. */
	public final static Color VGLIND_POINTER_COLOR = Color.WHITE;
	/** The color of the PatPos-pointer in the table visualisation phase one. */
	public final static Color PATPOS_POINTER_COLOR = Color.BLACK;
	/** The color of the visualisation arrow over the table in phase one. */
	public final static Color ARROW_COLOR = Color.BLACK;
	/** The color of the view window in phase two. */
	public final static Color VIEW_WINDOW_COLOR = Color.MAGENTA;
	/** The color of the box border in phase one for highlight a cell. */
	public final static Color BOX_COLOR = Color.BLACK;
	/** The color of the table heads. */
	public final static Color HEAD_COLOR = Color.YELLOW;
	/** The color of the text in the "goon" button in phase one. */
	public static final Color MOVE_ON_TO_PHASE_TWO = Color.RED;
	/** The color of the highlighted searchtext. */
	public static final Color SEARCHTEXT_HIGHLIGHT = Color.YELLOW;
	/** The color of all true things. */
	public static final Color TRUE_COLOR = new Color(125, 255, 160);
	/** The color of all false things. */
	public static final Color FALSE_COLOR = new Color(255, 160, 160);
	/** The color for highlightning. */
	public static final Color HIGHLIGHT_COLOR = new Color(120, 200, 255);

	/*------------------------------fonts-----------------------------*/
	/** The font used in the welcome screen. */
	public static final Font WELCOME_SCREEN_FONT =
		new Font("SansSerif", Font.PLAIN, 16);
	/** The font used in the ShowPanels. Only monospaced fonts are supported. */
	public static final Font SHOW_PANEL_FONT =
		new Font( "Monospaced", Font.BOLD, 18 );
	/** The font for the code in the InfoTabbedPanel. */
	public static final Font CODE_FONT =
		new Font("SansSerif", Font.PLAIN, 11);
	/** The font for the searchtext in the InfoTabbedPanel. */
	public static final Font SEARCHTEXT_FONT =
		new Font("SansSerif", Font.PLAIN, 11);
	/** The font for the screens. */
	public static final Font SCREEN_FONT =
		new Font("SansSerif", Font.PLAIN, 12);
	/** The font for the screens. */
	public static final Font RESULT_FONT =
		new Font("SansSerif", Font.BOLD, 16);
	
	/*------------------------------ShowPanels-------------------------*/
	/** Indicates the horizontal position of the table in phase one.*/
	public static final int P1_X_OFFSET = 20;
	/** Indicates the vertical position of the table in phase one.*/
	public static final int P1_Y_OFFSET = 30;
	/** Indicates indirectly as a factor in compersisation to the letter width
	 *  the minimal cell width of the table in phase one.*/
	public static final float P1_MIN_CELL_WIDTH = 2;
	/** Indicates indirectly as a factor in compersisation to the letter width
	 *  the maximal cell width of the table in phase one.*/
	public static final float P1_MAX_CELL_WIDTH = 7;
	/** Indicates the number of (direct) shown cycles. */
	public static final int MAX_SHOW_CYCLES = 4;
	/** Indicates the horizontal position of the table in phase two. */
	public static final int P2_X_OFFSET = 20;
	/** Indicates the vertical position of the table in phase two. */
	public static final int P2_Y_OFFSET = 20;
	/** Indicates the minimal cell width of the  table in phase 2. */
	public static final int P2_MIN_CELL_WIDTH = 50;
	/** Indicates the maximal cell width of the  table in phase 2. */
	public static final int P2_MAX_CELL_WIDTH = 60;
	/** Indicates the maximum number of text cells befor and after the pattern in phase 2. */
	public static final int P2_PREFERED_CELL_OFFSET = 2;
	/** Indicates the minimal period time (in ms) of an animation partstep in phase 2. */
	public static final int MIN_PERIOD_TIME = 5;
	/** Indicates the maximal period time (in ms) of an animation partstep in phase 2. */
	public static final int MAX_PERIOD_TIME = 100;
	/** Indicates the length (in px) of the animation acceleration(s) in phase 2. */
	public static final int ACC_LENGTH = 20;
	
	/*------------------------------BeamerMode------------------------*/
	/** The scalefactor of the beamer mode. */
	public final static double SCALEFACTOR = 1.5;
	/** The heigth difference of the show panel with or without cycles. */
	public static final int CYCLE_TAB_SIZE = 56;
	/** The divider location of the beamer mode. */
	public static final int DIVIDER_LOCATION = 310;
}