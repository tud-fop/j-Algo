package org.jalgo.main.gui.actions;

import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.module.synDiaEBNF.ModuleController;
import org.jalgo.module.synDiaEBNF.startWizard.StartWizard;
import org.jalgo.module.synDiaEBNF.startWizard.StartWizardDialog;
import org.jalgo.module.testModule.ModuleConnector;

/**
 * @author Christopher Friedrich
 */
public class NewWizardAction extends Action {

	private JalgoWindow win;
	private Collection knownModules;

	private ModuleConnector modConn;

	public NewWizardAction(JalgoWindow win, Collection knownModules) {
		this.win = win;
		this.knownModules = knownModules;

		setText("New Wizard");
		setToolTipText("New Wizard.");
		setImageDescriptor(ImageDescriptor.createFromFile(null, "pix/new.gif"));

	}

	public void run() {

		StartWizard wizard = new StartWizard((ModuleController)win.getCurrentInstance(), win);
		StartWizardDialog wizardDialog =
			new StartWizardDialog(win.getShell(), wizard);
		wizardDialog.open();
		
		// Start Module
		win.getParent().newInstance("");
	}

}
