/*
 * Created on Apr 23, 2004
 */
 
package org.jalgo.main.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.jalgo.main.gui.JalgoWindow;

/**
 * @author Cornelius Hald
 */
public class SaveAsAction extends Action {
	
	private JalgoWindow win;
	
	public SaveAsAction(JalgoWindow win) {
		this.win = win;
		setText(Messages.getString("SaveAsAction.Save_As_1")); //$NON-NLS-1$
		setId(Messages.getString("SaveAsAction.Save_As_2")); //$NON-NLS-1$
		setToolTipText(Messages.getString("SaveAsAction.Save_As..._3")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/save_as.gif")); //$NON-NLS-1$
	}

	public void run() {
		FileDialog fileChooser = new FileDialog(win.getShell(), SWT.SAVE);
		fileChooser.setText(Messages.getString("SaveAsAction.Save_file_5")); //$NON-NLS-1$

		fileChooser.setFilterExtensions(new String[] {Messages.getString("SaveAsAction.*.jalgo_6")}); //$NON-NLS-1$
		String filename = fileChooser.open();
		if (filename != null) {
			win.saveFileAs(filename);
		}
	}

}
