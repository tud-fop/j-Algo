/*
 * Created on 12.05.2004
 */
 
package org.jalgo.module.synDiaEBNF.startWizard;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.wizard.Wizard;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.main.gui.actions.OpenAction;
import org.jalgo.module.synDiaEBNF.ModuleController;

/**
 * @author Michael Pradel
 */
public class StartWizard extends Wizard {

	private ApplicationWindow appWin;
	private ModuleController mc;
	
	public FirstChoice firstChoice;

	public StartWizard(ModuleController mc, ApplicationWindow appWin) {
		super();
		this.mc = mc;
		this.appWin = appWin;
	}

	public void addPages() {
		firstChoice = new FirstChoice("firstChoice"); //$NON-NLS-1$
		addPage(firstChoice);
	}

	public boolean performFinish() {

		switch (firstChoice.getSelected()) {
			case FirstChoice.LOADFILE :
				OpenAction oa = new OpenAction((JalgoWindow) appWin);
				oa.run(true);  // 'true' tells open action to use current instance of this module
				return true;
				
			case FirstChoice.PUTINSYNDIA :
				mc.setMode(3);
				return true;

			case FirstChoice.PUTINEBNF :
				mc.setMode(4);
				return true;

			default :
				return true;
		}
	}
	
	public boolean performCancel() {
		mc.setMode(2);
		return true;
	}
}
