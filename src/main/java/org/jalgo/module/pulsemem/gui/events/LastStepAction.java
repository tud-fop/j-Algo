/**
 *
 */

/* Created on 13.06.2007 */
package org.jalgo.module.pulsemem.gui.events;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.util.Messages;
import org.jalgo.module.pulsemem.Controller;
import org.jalgo.module.pulsemem.gui.GUIController;

/**
 * LastStepAction.java
 * <p>
 * Will go to last step.
 * <p>
 *
 * @version $Revision: 1.3 $
 * @author Martin Brylski - TU Dresden, SWTP 2007
 */
public class LastStepAction extends AbstractAction {

    private GUIController gui;

    private Controller controller;

    /**
     * @param gui
     * @param controller
     */
    public LastStepAction(GUIController gui, Controller controller) {
        this.gui = gui;
        this.controller = controller;
        putValue(NAME, Messages.getString(
                "pulsemem", "LastStepAction.lastStepShort")); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, Messages.getString(
                "pulsemem", "LastStepAction.lastStepLong")); //$NON-NLS-1$
        putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL("pulsemem", //$NON-NLS-1$
                "StepForw"))); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
    	controller.getConnector()
    		.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
        gui.showLastStep();
    }

}
