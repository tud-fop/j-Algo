/*
 * Created on Apr 23, 2004
 */
 
package org.jalgo.module.synDiaEBNF.gui.actions;

import java.io.Serializable;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.module.synDiaEBNF.ModuleController;

/**
 * @author Cornelius Hald
 * @author Michael Pradel
 */
public class RightAction extends NavAction implements Serializable {

	private ModuleController mc;

	public RightAction(ModuleController mc) {
		this.mc = mc;
		setText(Messages.getString("RightAction.Next_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("RightAction.Perform_next_step._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/right.gif")); //$NON-NLS-1$
	}

	public void run() {
		mc.performNextStep();
	}

}
