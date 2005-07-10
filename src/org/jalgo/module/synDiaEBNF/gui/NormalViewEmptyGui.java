/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
 * Created on 20.06.2004
 */

package org.jalgo.module.synDiaEBNF.gui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.main.gui.actions.OpenAction;
import org.jalgo.module.synDiaEBNF.IModeConstants;
import org.jalgo.module.synDiaEBNF.ModuleController;

/**
 * @author Michael Pradel
 */
public class NormalViewEmptyGui extends Gui {

	private static final long serialVersionUID = 1481705887589094185L;

	public NormalViewEmptyGui(Composite parent, ModuleController mc) {
		super(parent);

		RowLayout outerLayout = new RowLayout(SWT.VERTICAL);
		outerLayout.spacing = 8;
		GridLayout innerLayout = new GridLayout();
		innerLayout.numColumns = 3;
		innerLayout.horizontalSpacing = 7;

		Composite main = new Composite(parent, SWT.FLAT);
		main.setLayout(outerLayout);

		Group group1 = new Group(main, SWT.SHADOW_ETCHED_IN);
		group1.setLayout(innerLayout);
		Group group2 = new Group(main, SWT.SHADOW_ETCHED_IN);
		group2.setLayout(innerLayout);
		Group group3 = new Group(main, SWT.SHADOW_ETCHED_IN);
		group3.setLayout(innerLayout);

		// How to do this nice??
		final ModuleController mc_final = mc;

		// Grid-Data for groups
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;

		/*
		 * First group (open file):
		 */
		group1.setText("EBNF laden");
		Label openFileImage = new Label(group1, SWT.CENTER);
		ImageDescriptor img1 = ImageDescriptor.createFromURL(
			getClass().getResource("/main_pix/open.gif"));
		openFileImage.setImage(img1.createImage());

		Label openFileText = new Label(group1, SWT.WRAP);
		openFileText.setText(Messages
				.getString("FirstChoice.Load_sample_EBNF_4")); //$NON-NLS-1$
		openFileText.setLayoutData(gridData);

		Button openFileButton = new Button(group1, SWT.CENTER);
		openFileButton.setText("Auswählen");
		openFileButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent event) {
			}

			public void widgetSelected(SelectionEvent event) {
				OpenAction oa = new OpenAction(
						(JalgoWindow) mc_final
								.getAppWin());
				/*
				 * 'true' tells open action to use current
				 * instance of this module
				 */
				oa.run(true);
			}
		});

		/*
		 * Second group (create EBNF):
		 */
		group2.setText("Neue EBNF");
		Label createEbnfImage = new Label(group2, SWT.CENTER);
		ImageDescriptor img2 = ImageDescriptor.createFromURL(
			getClass().getResource("/ebnf_pix/createEbnf.gif"));
		createEbnfImage.setImage(img2.createImage());

		Label createEbnfText = new Label(group2, SWT.WRAP);
		createEbnfText
				.setText(Messages
						.getString("FirstChoice.Enter_new_EBNF_definition._6") + "      "); //$NON-NLS-1$
		createEbnfText.setLayoutData(gridData);

		Button createEbnfButton = new Button(group2, SWT.CENTER);
		createEbnfButton.setText("Auswählen");
		createEbnfButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent event) {
			}

			public void widgetSelected(SelectionEvent event) {
				mc_final.setMode(IModeConstants.EBNF_INPUT);
			}
		});

		/*
		 * Third group (create SynDia):
		 */
		group3.setText("Syntax-Diagramm-System erstellen");
		Label createSynDiaImage = new Label(group3, SWT.CENTER);
		ImageDescriptor img3 = ImageDescriptor.createFromURL(
			getClass().getResource("/ebnf_pix/createSynDia.gif"));
		createSynDiaImage.setImage(img3.createImage());

		Label createSynDiaText = new Label(group3, SWT.WRAP);
		createSynDiaText
				.setText(Messages
						.getString("FirstChoice.Create_SynDia_using_mouse_8")); //$NON-NLS-1$
		createSynDiaText.setLayoutData(gridData);

		Button createSynDiaButton = new Button(group3, SWT.CENTER);
		createSynDiaButton.setText("Auswählen");
		createSynDiaButton
				.addSelectionListener(new SelectionListener() {

					public void widgetDefaultSelected(
							SelectionEvent event) {
					}

					public void widgetSelected(
							SelectionEvent event) {
						mc_final
								.setMode(IModeConstants.CREATE_SYNDIA);
					}
				});
		
		parent.redraw();
	}
}
