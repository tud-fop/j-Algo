/*
 * Created on 16.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.module.synDiaEBNF.ModuleController;

/**
 * @author Michael Pradel
 */
public class GenerateWordAction extends Action {
	private ModuleController mc;

		public GenerateWordAction(ModuleController mc) {
			this.mc = mc;
			setText(Messages.getString("GenerateWordAction.generate_word_algorithm_1")); //$NON-NLS-1$
			setToolTipText(Messages.getString("GenerateWordAction.Start_generate_word_algorithm._2")); //$NON-NLS-1$
			setImageDescriptor(
				ImageDescriptor.createFromFile(null, "pix/generateWord.gif")); //$NON-NLS-1$
		}

		public void run() {
			mc.setMode(7); // start generateWord-algorithm
		}
}
