/*
 * Created on 28.06.2004
 */
package org.jalgo.module.synDiaEBNF.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.module.synDiaEBNF.ModuleController;

/**
 * @author Michael Pradel
 */
public class AbortAlgoAction extends Action {
	
	private ModuleController mc;

	public AbortAlgoAction(ModuleController mc) {
		this.mc = mc;
		setText(Messages.getString("AbortAlgoAction.Abort_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("AbortAlgoAction.Abort_the_algorithm._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/stop.gif")); //$NON-NLS-1$
	}

	public void run() {
		mc.abortAlgo();
	}
}
