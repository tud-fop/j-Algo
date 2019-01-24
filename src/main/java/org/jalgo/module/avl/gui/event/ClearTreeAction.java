/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
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

/* Created on 06.06.2005 */
package org.jalgo.module.avl.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.gui.DialogConstants;
import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.gui.GUIController;

import org.jalgo.main.gui.JAlgoGUIConnector;

/**
 * The class <code>ClearTreeAction</code> defines an <code>Action</code>
 * object, which can be added to SWT toolbars and menus. Performing this action
 * asks the user for discarding his changes and if "OK" is selected, the tree is
 * cleared.
 * 
 * @author Alexander Claus
 */
public class ClearTreeAction
extends AbstractAction {

	private final GUIController gui;
	private final SearchTree tree;

	/**
	 * Constructs a <code>ClearTreeAction</code> object with the given
	 * references.
	 * 
	 * @param gui the <code>GUIController</code> instance of the AVL module
	 * @param tree the <code>SearchTree</code> instance to be the target of
	 *            operation
	 */
	public ClearTreeAction(GUIController gui, SearchTree tree) {
		this.gui = gui;
		this.tree = tree;
		putValue(NAME, Messages.getString("avl", "Clear_tree")); //$NON-NLS-1$ //$NON-NLS-2$
		putValue(SHORT_DESCRIPTION, Messages.getString("avl", "Clear_tree_tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		putValue(SMALL_ICON, new ImageIcon(
			Messages.getResourceURL("main", "Icon.Clear"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Performs the action.
	 */
	public void actionPerformed(ActionEvent e) {
		switch (JAlgoGUIConnector.getInstance().showConfirmDialog(
			Messages.getString("avl", "Clear_tree_warning"), //$NON-NLS-1$ //$NON-NLS-2$
			DialogConstants.OK_CANCEL_OPTION)) {
			case DialogConstants.OK_OPTION:
				tree.clear();
				gui.setToolbarButtonsDisabled();
				gui.setAVLMode(true, true);
				gui.installStandardLayout();
				break;
			case DialogConstants.CANCEL_OPTION:
				return;
		}
	}
}