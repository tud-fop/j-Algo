/*
 * Created on 01.08.2004
 */
package org.jalgo.main.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.JalgoMain;

/**
 * @author michi
 */
public class NewModuleAction extends Action {
	
	// TODO: implement this class: user can choose from the list of known modules which one to open; 
	// information about the modules (description, logo, license, ...) should be available for the user 
	private JalgoMain main;
	
	public NewModuleAction(JalgoMain main) {
		this.main = main;
		setToolTipText(Messages.getString("ui.New"));
		setImageDescriptor(ImageDescriptor.createFromFile(null, "pix/new.gif"));
	}
	
	public void run() {
		// workaround as long as class isn't implented: open directly first module
		main.newInstance(0);
	}

}
