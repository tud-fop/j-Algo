/*
 * Created on 12.05.2004
 */
 
package org.jalgo.module.synDiaEBNF.startWizard;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Michael Pradel
 */
public class StartWizardDialog extends WizardDialog {

	public StartWizardDialog(Shell arg0, IWizard arg1) {
		super(arg0, arg1);
	}
	
	protected void configureShell(Shell shell) {
	 	shell.setText(Messages.getString("StartWizardDialog.Assistent_for_creating_new_file_1")); //TODO shell.setText geht nicht..?. //$NON-NLS-1$
	}
}