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
public class RecognizeWordAction extends Action {
	private ModuleController mc;

	public RecognizeWordAction(ModuleController mc) {
		this.mc = mc;
		setText(Messages.getString("RecognizeWordAction.recognize_word_algorithm_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("RecognizeWordAction.Start_recognize_word_algorithm._2")); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromFile(null, "pix/recognizeWord.gif")); //$NON-NLS-1$
		//  TODO: add button-image
	}

	public void run() {
		mc.setMode(6); // start recognize word()
	}
}
