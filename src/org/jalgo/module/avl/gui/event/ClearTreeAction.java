/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
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

/* Created on 06.06.2005 */
package org.jalgo.module.avl.gui.event;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.gui.GUIController;

/**
 * The class <code>ClearTreeAction</code> defines an <code>Action</code> object,
 * which can be added to SWT toolbars and menus. Performing this action asks the
 * user for discarding his changes and if "OK" is selected, the tree is cleared.
 * 
 * @author Alexander Claus
 */
public class ClearTreeAction
extends Action {

	private final GUIController gui;
	private final Composite parent;
	private final SearchTree tree;

	/**
	 * Constructs a <code>ClearTreeAction</code> object with the given references.
	 * 
	 * @param gui the <code>GUIController</code> instance of the AVL module
	 * @param parent the parent component for correct location of the dialog
	 * @param tree the <code>SearchTree</code> instance to be the target of operation
	 */
	public ClearTreeAction(GUIController gui, Composite parent, SearchTree tree) {
		this.gui = gui;
		this.parent = parent;
		this.tree = tree;
		setText("Baum löschen");
		setToolTipText("Löscht den gesamten Baum");
		setImageDescriptor(ImageDescriptor.createFromURL(
			getClass().getResource("/avl_pix/clear.gif")));
	}

	/**
	 * Performs the action.
	 */
	public void run() {
		switch (new MessageDialog(parent.getShell(), "Achtung", null,
			"Der gesamte Baum wird gelöscht.", MessageDialog.QUESTION,
			new String[] {"OK", "Abbrechen"}, 0).open()) {
		case 0:
			tree.clear();
			gui.setToolbarButtonsDisabled();
			gui.setAVLMode(true, true);
			gui.installStandardLayout();
			break;
		case 1:
			return;
		}
	}
}