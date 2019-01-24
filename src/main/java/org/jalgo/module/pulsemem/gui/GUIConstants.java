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

/* Created on 05.05.2007 */
package org.jalgo.module.pulsemem.gui;

import java.awt.Color;
import java.awt.Font;

/**
 * GUIConstants.java
 * <p>
 * The interface <code>Constants</code> is a collection of several constant
 * values, used in the GUI implementation of the PulsMem module.
 * <p>
 *
 * @version $Revision: 1.6 $
 * @author Martin Brylski - TU Dresden, SWTP 2007
 */
public interface GUIConstants {

    /*------------------------------colors----------------------------*/
    /** The background color in the welcome screen. */
    public final static Color WELCOME_SCREEN_BACKGROUND = new Color(51, 102,
            153);

    /** The font color in the welcome screen. */
    public final static Color WELCOME_SCREEN_FOREGROUND = new Color(254, 215, 0);

    /** The font color for disabled textares */
    public final static Color DISABLED_TEXTAREA = new Color(128, 128, 128);

    /** The font color for disabled textares */
    public final static Color DISABLED_TEXTAREA_FONT = new Color(0, 0, 0);

    /** The line highlighted color */
    public final static Color LineHighlightColor = Color.YELLOW;

    /** The caret selected color */
    public final static Color CARET_SELECT_COLOR = new Color(68, 123, 205);

    /*------------------------------fonts-----------------------------*/
    /** The font used in the welcome screen. */
    public static final Font WELCOME_SCREEN_FONT = new Font("SansSerif",
            Font.PLAIN, 16);

    /** The Beamerfont */
    public static final Font BEAMERFONT = new Font("Courier", Font.BOLD, 16);

    /** The Standardfont */
    public static final Font STANDARDFONT = new Font("Courier", Font.PLAIN,
            12);

    /*------------------------------Others-----------------------------*/
    /** Determine wether to enable/disable parsing/stopping items. */
    public static final int PARSE_ENABLED = 0;

    public static final int PARSE_DISABLED = 1;

}
