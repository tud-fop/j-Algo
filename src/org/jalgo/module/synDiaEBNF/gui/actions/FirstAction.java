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
public class FirstAction extends NavAction implements Serializable {

	private ModuleController mc;

	public FirstAction(ModuleController mc) {
		this.mc = mc;
		setText(Messages.getString("FirstAction.First_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("FirstAction.Jump_to_first._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/first.gif")); //$NON-NLS-1$
	}

	public void run() {
		mc.goToFirstStep();
	}

}
