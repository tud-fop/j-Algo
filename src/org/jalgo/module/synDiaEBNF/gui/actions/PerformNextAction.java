/*
 * Created on 02.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gui.actions;

import java.io.Serializable;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.module.synDiaEBNF.ModuleController;

/**
 * @author Cornelius Hald
 * @author Michael Pradel
 */
public class PerformNextAction extends Action implements Serializable {

	private ModuleController mc; 
	
	public PerformNextAction(ModuleController mc) {
		this.mc = mc;
		setText(Messages.getString("PerformNextAction.Next_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("PerformNextAction.Perform_next_step._2")); //$NON-NLS-1$
		// TODO: find or design nice buttons
		setImageDescriptor(
					ImageDescriptor.createFromFile(null, "pix/performNext.gif")); //$NON-NLS-1$
		setId(Messages.getString("PerformNextAction.performNext_4")); //$NON-NLS-1$
	}

	public void run() {
		mc.performNextStep();
	}
}
