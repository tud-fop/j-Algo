/*
 * Created on 03.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gui.actions;

import java.io.Serializable;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.module.synDiaEBNF.ModuleController;

/**
 * @author Michael Pradel
 */
public class WizardAction extends Action implements Serializable {

	private ModuleController mc;
	private Composite parent;

	public WizardAction(ModuleController mc, Composite parent) {
		this.mc = mc;
		this.parent = parent;
		setText(Messages.getString("WizardAction.Wizard_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("WizardAction.Starten_des_Wizards_2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/wizard.gif")); //$NON-NLS-1$
	}

	public void run() {
		if (MessageDialog
			.openQuestion(
				parent.getShell(),
				Messages.getString("WizardAction.Really_start_wizard__4"), //$NON-NLS-1$
				Messages.getString("WizardAction.Starting_the_wizard_will_destroy_all_you_have_done_with_this_module_up_to_now_!_5"))) { //$NON-NLS-1$
			mc.startWizard();
		} else {
			return;
		}
	}

}
