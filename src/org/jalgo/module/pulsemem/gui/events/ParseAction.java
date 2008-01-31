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

/* Created on 06.05.2007 */
package org.jalgo.module.pulsemem.gui.events;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.pulsemem.Controller;
import org.jalgo.module.pulsemem.gui.GUIConstants;
import org.jalgo.module.pulsemem.gui.GUIController;

/**
 * ParseAction.java
 * <p>
 * Will parse the C-Code.
 * <p>
 *
 * @version $Revision: 1.11 $
 * @author Martin Brylski - TU Dresden, SWTP 2007
 */
public class ParseAction extends AbstractAction {

    private GUIController gui;

    private Controller controller;

    /**
     * @param gui
     * @param controller
     */
    public ParseAction(GUIController gui, Controller controller) {
        this.gui = gui;
        this.controller = controller;
        putValue(NAME, Messages.getString(
                "pulsemem", "ParseAction.startParsingShort")); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, Messages.getString(
                "pulsemem", "ParseAction.startParsingLong")); //$NON-NLS-1$
        putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL("pulsemem", //$NON-NLS-1$
                "PStart"))); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
    	//return if SourceCode is empty
    	if (gui.getSourceCode().equals(""))
    		return;
    	gui.toConsole("Interpretiere... Bitte warten!");
    	gui.switchParseStopEnabled(GUIConstants.PARSE_ENABLED);
    	//return if interpretAndBeautify fails
        if (!controller.interpretAndBeautify())
            return;
        gui.toConsole("Interpretation erfolgreich.");

    }

}