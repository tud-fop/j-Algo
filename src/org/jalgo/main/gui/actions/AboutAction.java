package org.jalgo.main.gui.actions;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.MessageBox;
import org.jalgo.main.gui.JalgoWindow;

/*
 * Created on Mar 23, 2004
 */

/**
 * @author Christopher Friedrich
 */
public class AboutAction extends Action {

	private JalgoWindow win;

	public AboutAction(JalgoWindow win) {
		this.win = win;

		setText(Messages.getString("AboutAction.About_jAlgo_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("AboutAction.Get_infos_about_jAlgo._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/about.gif")); //$NON-NLS-1$
	}

	public void run() {

		MessageBox dia = new MessageBox(win.getShell());

		dia.setText(Messages.getString("AboutAction.About_jAlgo_4")); //$NON-NLS-1$
		dia
			.setMessage(
				Messages.getString("AboutAction.jAlgo_Name_5") //$NON-NLS-1$
				+ "\n" //$NON-NLS-1$
				+ Messages.getString("AboutAction.jAlgo_Copyright_7") //$NON-NLS-1$
				+ Messages.getString("AboutAction.jAlgo_Visit_URL_8") //$NON-NLS-1$
				+ "\n" //$NON-NLS-1$
				+ Messages.getString("AboutAction.Author__n_10") //$NON-NLS-1$
				+ Messages.getString("AboutAction.jAlgo_Authors_11") //$NON-NLS-1$
				+ "\n" //$NON-NLS-1$
				+ Messages.getString("AboutAction.License__n_13") //$NON-NLS-1$
				+ Messages.getString("AboutAction.GNU_General_Public_License_14") //$NON-NLS-1$
		/*
		+ "This program is free software; you can redistribute it and/or modify\n"
		+ "it under the terms of the GNU General Public License as published by\n"
		+ "the Free Software Foundation; either version 2 of the License, or\n"
		+ "(at your option) any later version.\n"
		+ "\n"
		+ "This program is distributed in the hope that it will be useful,\n"
		+ "but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
		+ "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"
		+ "GNU General Public License for more details."
		*/
		);
		dia.open();
	}

}
