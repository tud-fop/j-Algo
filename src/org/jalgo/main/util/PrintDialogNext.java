/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and platform
 * independant. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.jalgo.main.util;

import org.eclipse.draw2d.PrintFigureOperation;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jalgo.main.gui.DialogConstants;
import org.jalgo.main.gui.JAlgoGUIConnector;

/**
 * Used after a usual PrintDialog to decide about the scaling.
 * 
 * @author Anne Kersten
 */
public class PrintDialogNext
extends Dialog {

	private Combo select;

	private int selectionIndex;

	/**
	 * Constructs a new PrintDialogNext.
	 * 
	 * @param parent the parent-shell
	 */
	public PrintDialogNext(Shell parent) {
		super(parent);
	}

	/**
	 * Creates a Combo where you can chose the scaling.
	 * 
	 * @param parent the parent composite to contain the dialog area
	 * @return the parent composite holding a combo
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Label title = new Label(parent, SWT.LEFT);
		title.setText(Messages.getString(
			"main", "PrintDialogNext.Select_scaling_mode_1")); //$NON-NLS-1$
		parent.setLayout(new GridLayout());
		select.add(Messages.getString(
			"main", "PrintDialogNext.Scale_to_fit_on_one_page_2")); //$NON-NLS-1$
		select.add(Messages.getString(
			"main", "PrintDialogNext.Scale_only_width_3")); //$NON-NLS-1$
		select.add(Messages.getString(
			"main", "PrintDialogNext.Scale_only_height_4")); //$NON-NLS-1$
		select.add(Messages.getString("main", "PrintDialogNext.No_scaling_5")); //$NON-NLS-1$
		selectionIndex = 3;
		select.select(selectionIndex);
		select.addSelectionListener(new SelectionListener() {
			@SuppressWarnings("synthetic-access")
			public void widgetSelected(SelectionEvent e) {
				selectionIndex = select.getSelectionIndex();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
			/* this block is intended to be empty */
			}
		});
		return parent;
	}

	/**
	 * Opens the PrintDialogNext
	 * 
	 * @return PrintScaledFigureOperation.FIT_PAGE for "Scale to fit on one
	 *         page"
	 *         <p>
	 *         PrintScaledFigureOperation.FIT_WIDTH for "Scale only width"
	 *         <p>
	 *         PrintScaledFigureOperation.FIT_HEIGHT for "Scale only height"
	 *         <p>
	 *         PrintScaledFigureOperation.TILE for "No scaling"
	 *         <p>
	 *         -1 for Window.CANCEL
	 */
	public int open() {
		if (super.open() == Window.OK) switch (selectionIndex) {
			case 0:
				selectionIndex = PrintFigureOperation.FIT_PAGE;
				break;
			case 1:
				selectionIndex = PrintFigureOperation.FIT_WIDTH;
				break;
			case 2:
				selectionIndex = PrintFigureOperation.FIT_HEIGHT;
				break;
			case 4:
				if (JAlgoGUIConnector.getInstance().showConfirmDialog(
					Messages.getString("main", "PrintDialogNext.Layout_warning_7"),
					DialogConstants.YES_NO_OPTION) == DialogConstants.YES_OPTION)
				selectionIndex = PrintScaledFigureOperation.USE_LAYOUT; //$NON-NLS-1$ //$NON-NLS-2$
				else selectionIndex = -1;
				break;
			default:
				selectionIndex = PrintFigureOperation.TILE;
		}
		else selectionIndex = -1;

		return selectionIndex;
	}
}