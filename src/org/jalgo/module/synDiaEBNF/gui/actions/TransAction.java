/*
 * Created on 16.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.module.synDiaEBNF.ModuleController;

/**
 * @author Michael Pradel
 */
public class TransAction extends Action {
	private ModuleController mc;

	public TransAction(ModuleController mc) {
		this.mc = mc;
		setText(Messages.getString("TransAction.trans_algorithm_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("TransAction.Start_trans_algorithm._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/transalgo.gif")); //  TODO: add button-image //$NON-NLS-1$
	}

	public void run() {
		mc.setMode(5); // start trans()
	}

}
