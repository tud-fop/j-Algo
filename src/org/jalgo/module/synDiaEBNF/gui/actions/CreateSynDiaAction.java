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
public class CreateSynDiaAction extends Action implements Serializable {

	private Composite parent;
	private ModuleController mc;

	public CreateSynDiaAction(ModuleController mc, Composite parent) {
		this.parent = parent;
		this.mc = mc;
		setText(Messages.getString("CreateSynDiaAction.Syntaxdiagramme_erstellen_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("CreateSynDiaAction.Erstellen_eines_Syntaxdiagramm-Systems_mit_Hilfe_der_Maus._2")); //$NON-NLS-1$
		//		TODO: find or design nice buttons
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/createSynDia.gif")); //$NON-NLS-1$
	}

	public void run() {
		if (mc.getMode() != 2) {
			if (!(MessageDialog
				.openQuestion(
					parent.getShell(),
					Messages.getString("CreateSynDiaAction.Really_create_new_syntactical_diagram__4"), //$NON-NLS-1$
					Messages.getString("CreateSynDiaAction.Creating_a_new_syntactical_diagram_will_destroy_all_you_have_done_with_this_module_up_to_now_!_5")))) { //$NON-NLS-1$
				return;
			}
		}
		mc.setMode(3);
	}

}
