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
public class CreateEbnfAction extends Action implements Serializable {

	private Composite parent;
	private ModuleController mc;

	public CreateEbnfAction(ModuleController mc, Composite parent) {
		this.mc = mc;
		this.parent = parent;
		setText(Messages.getString("CreateEbnfAction.EBNF_erstellen_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("CreateEbnfAction.Eingabe_von_EBNF_Termen._2")); //$NON-NLS-1$
		//		TODO: find or design nice buttons		
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/createEbnf.gif")); //$NON-NLS-1$
	}

	public void run() {
		if (mc.getMode() != 2) {
			if (!(MessageDialog
				.openQuestion(
					parent.getShell(),
					Messages.getString("CreateEbnfAction.Really_create_new_EBNF__4"), //$NON-NLS-1$
					Messages.getString("CreateEbnfAction.Creating_a_new_EBNF_will_destroy_all_you_have_done_with_this_module_up_to_now_!_5")))) { //$NON-NLS-1$
				return;
			}
		}
		mc.setMode(4);
	}
}
