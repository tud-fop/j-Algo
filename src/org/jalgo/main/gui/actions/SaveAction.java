/*
 * Created on Mar 23, 2004
 */
 
package org.jalgo.main.gui.actions;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.gui.JalgoWindow;

/**
 * @author Michael Pradel
 */
public class SaveAction extends Action {

	JalgoWindow win;

	public SaveAction(JalgoWindow win) {
		//this.mc = mc;
		this.win = win;
		setText(Messages.getString("SaveAction.Save_1")); //$NON-NLS-1$
		setId(Messages.getString("SaveAction.Save_2")); //$NON-NLS-1$
		setToolTipText(Messages.getString("SaveAction.Save_file._3")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/save.gif")); //$NON-NLS-1$
	}

	public void run() {
		win.saveFile();
	}

}
