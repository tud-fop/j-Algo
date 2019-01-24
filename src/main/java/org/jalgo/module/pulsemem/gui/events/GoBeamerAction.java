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

/* Created on 07.05.2007 */
package org.jalgo.module.pulsemem.gui.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import org.jalgo.main.util.Messages;
import org.jalgo.module.pulsemem.Controller;
import org.jalgo.module.pulsemem.gui.GUIController;

/**
 * GoBeamer.java
 * <p>
 * Will go in BeamerMode. <br>
 * Therefor change font-size and so on.
 * <p>
 *
 * @version $Revision: 1.3 $
 * @author Martin Brylski - TU Dresden, SWTP 2007
 */
public class GoBeamerAction extends JCheckBoxMenuItem implements ActionListener {

    private GUIController gui;

    private Controller controller;

    private static GoBeamerAction instance;

    /**
     * @param gui
     * @param controller
     */
    private GoBeamerAction(GUIController gui, Controller controller) {
        super(
                Messages.getString("pulsemem", "GoBeamerAction.BeamerMode"), new ImageIcon(Messages.getResourceURL("main", "Icon.Beamer_mode")), false); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        addActionListener(this);
        this.gui = gui;
        this.controller = controller;
    }

    /**
     * Retrieves and, if necessary, initializes the singleton instance of
     * <code>BeamerModeAction</code>.
     *
     * @param gc
     *            the <code>GUIController</code>
     * @return the singleton instance
     */
    public static GoBeamerAction getInstance(GUIController gui,
            Controller controller) {
        if (instance == null) {
            instance = new GoBeamerAction(gui, controller);
        }
        return instance;
    }

    /**
     * Performs the action.
     */
    public void actionPerformed(ActionEvent e) {
        gui.goToBeamerMode(isSelected());
    }

}
