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
public class LastAction extends NavAction implements Serializable {

	private ModuleController mc;

	public LastAction(ModuleController mc) {
		this.mc = mc;
		setText(Messages.getString("LastAction.Last_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("LastAction.Jump_to_last._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/last.gif")); //$NON-NLS-1$
	}

	public void run() {
		mc.goToLastStep();
	}

}
