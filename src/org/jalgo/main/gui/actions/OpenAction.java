/*
 * Created on Mar 23, 2004
 */
 
package org.jalgo.main.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.jalgo.main.gui.JalgoWindow;

/**
 * This is the action responible for the "File open" request.
 * 
 * @author Cornelius Hald
 */
public class OpenAction extends Action {

	private JalgoWindow win;
	private String currentDir;

	public OpenAction(JalgoWindow win) {

		this.win = win;
		setText(Messages.getString("OpenAction.Open_file..._1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("OpenAction.Open_a_file_2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/open.gif")); //$NON-NLS-1$
	}

	/**
	 * The method is called if the user presses the "File open" button.
	 * 
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		run(false);
	}

	/**
	 * The method is called if the user presses the "File open" button.
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run(boolean useCurrentModuleInstance) {
		FileDialog fileChooser = new FileDialog(win.getShell(), SWT.OPEN);

		fileChooser.setText(Messages.getString("OpenAction.Open_file_4")); //$NON-NLS-1$
		fileChooser.setFilterPath(currentDir);
		fileChooser.setFilterExtensions(new String[] { "*.jalgo", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
		fileChooser.setFilterNames(
			new String[] { Messages.getString("OpenAction.jAlgo_files_(*.jalgo)_7"), Messages.getString("OpenAction.All_files_8") }); //$NON-NLS-1$ //$NON-NLS-2$

		String filename = fileChooser.open();
		if (filename != null) {
			win.openFile(filename, useCurrentModuleInstance);
		}
	}
}
