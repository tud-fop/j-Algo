/*
 * Created on 11.06.2004
 */
package org.jalgo.module.synDiaEBNF.gui.actions;

import java.io.Serializable;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author Marco Zimmerling
 */
public class AlternativeAction extends Action implements Serializable {

	public AlternativeAction() {
		setText(Messages.getString("AlternativeAction.Alternative_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("AlternativeAction.Create_Alternative_2")); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromFile(null, "pix/Concat.png")); //$NON-NLS-1$
	}
	
	public void run() {
		
	}
}
