/*
 * Created on 20.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.module.synDiaEBNF.ModuleController;

/**
 * @author Michael Pradel
 */
public class PerformAllAction extends Action {
	private ModuleController mc;

	public PerformAllAction(ModuleController mc) {
		this.mc = mc;
		setText(Messages.getString("PerformAllAction.Perform_All_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("PerformAllAction.Perform_trans-algorithm_automatically_to_the_end._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/transalgoend.gif")); //$NON-NLS-1$
		setId(Messages.getString("PerformAllAction.performAll_4")); //$NON-NLS-1$
	}

	public void run() {
		mc.performAll();
	}
}
