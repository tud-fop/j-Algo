/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and platform
 * independant. j-Algo is developed with the help of Dresden University of
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

/* Created on 07.05.2007 */
package org.jalgo.module.pulsemem.gui.events;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.util.Messages;
import org.jalgo.module.pulsemem.Admin;
import org.jalgo.module.pulsemem.Controller;
import org.jalgo.module.pulsemem.gui.GUIController;

/**
 * NextBreakpointAction.java
 * <p>
 * Will show the next breakpoint.
 * <p>
 *
 * @version $Revision: 1.7 $
 * @author Martin Brylski - TU Dresden, SWTP 2007
 */
public class NextBreakpointAction extends AbstractAction {

    private GUIController gui;

    private Controller controller;

    /**
     * @param gui
     * @param controller
     */
    public NextBreakpointAction(GUIController gui, Controller controller) {
        this.gui = gui;
        this.controller = controller;
        putValue(NAME, Messages.getString(
                "pulsemem", "NextBreakpointAction.nextBreakpointShort")); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, Messages.getString(
                "pulsemem", "NextBreakpointAction.nextBreakpointLong")); //$NON-NLS-1$
        putValue(SMALL_ICON, new ImageIcon(Admin.getResourceURL("BreakForw"))); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
    	controller.getConnector()
    		.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
        gui.showNextLine();
    }

}
