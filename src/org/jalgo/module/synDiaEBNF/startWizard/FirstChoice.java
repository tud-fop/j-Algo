/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on 12.05.2004
 */
 
package org.jalgo.module.synDiaEBNF.startWizard;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Michael Pradel
 * @author Benjamin Scholz
 */
public class FirstChoice extends WizardPage {

	private Button loadFileButton;
	private Button putInEbnfButton;
	private Button putInSynDiaButton;

	public static final int LOADFILE = 1;
	public static final int PUTINEBNF = 2;
	public static final int PUTINSYNDIA = 3;

	public FirstChoice(String name) {
		super(name);
		setTitle(Messages.getString("FirstChoice.Module_title_1")); //$NON-NLS-1$
		setDescription(Messages.getString("FirstChoice.What_do_you_want_to_do_2")); //$NON-NLS-1$

	}

	public FirstChoice(String name, String title, ImageDescriptor image) {
		super(name, title, image);
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.FLAT);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		layout.verticalSpacing = 10;

		// load file		
		loadFileButton = new Button(container, SWT.RADIO);
		loadFileButton.setText(
			Messages.getString("FirstChoice.Load_EBNF_3") //$NON-NLS-1$
				+ Messages.getString("FirstChoice.Load_sample_EBNF_4")); //$NON-NLS-1$
		loadFileButton.setSelection(true);
		loadFileButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent event) {
			}

			public void widgetSelected(SelectionEvent event) {
				setPageComplete(true);
				getWizard().getContainer().updateButtons();
			}
		});

		// input of EBNF		
		putInEbnfButton = new Button(container, SWT.RADIO);
		putInEbnfButton.setText(
			Messages.getString("FirstChoice.Enter_EBNF_5") //$NON-NLS-1$
				+ Messages.getString("FirstChoice.Enter_new_EBNF_definition._6")); //$NON-NLS-1$
		putInEbnfButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent event) {
			}

			public void widgetSelected(SelectionEvent event) {
				setPageComplete(true);
				getWizard().getContainer().updateButtons();
			}

		});

		// input of SynDia		
		putInSynDiaButton = new Button(container, SWT.RADIO);
		putInSynDiaButton.setText(
			Messages.getString("FirstChoice.Create_SynDia_7") //$NON-NLS-1$
				+ Messages.getString("FirstChoice.Create_SynDia_using_mouse_8")); //$NON-NLS-1$
		putInSynDiaButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent event) {
			}

			public void widgetSelected(SelectionEvent event) {
				setPageComplete(true);
				getWizard().getContainer().updateButtons();
			}

		});

		setControl(container);
	}

	public boolean canFlipToNextPage() {
		return false;
	}

	public IWizardPage getNextPage() {
		return null;
	}

	public int getSelected() {
		if (loadFileButton.getSelection()) {
			return LOADFILE;

		} else if (putInEbnfButton.getSelection()) {
			return PUTINEBNF;

		} else if (putInSynDiaButton.getSelection()) {
			return PUTINSYNDIA;

		} else {
			return 0;
		}

	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
}