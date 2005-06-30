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

package org.jalgo.module.avl.gui.event;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.module.avl.ModuleConnector;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.gui.GUIController;

/**
 * The class <code>WelcomeAction</code> defines an <code>Action</code> object, which
 * can be added to toolbars and menus. Performing this action asks the user for
 * discarding his changes, and if so, switches the layout of the current AVL module
 * instance to the welcome screen and clears the tree. If the user doesn't want to
 * discard his changes, a new instance of the AVL module is opened. The question
 * dialog can be cancelled too.
 * 
 * @author Alexander Claus
 */
public class WelcomeAction
extends Action {

	private JalgoWindow parent;
	private ModuleConnector connector;
	private GUIController gui;
	private SearchTree tree;

	/**
	 * Constructs a <code>WelcomeAction</code> object with the given references.
	 * 
	 * @param parent the current instance of <code>JalgoWindow</code>
	 * @param connector the <code>ModuleConnector</code> instance of the AVL module
	 * @param gui the <code>GUIController</code> instance of the AVL module
	 * @param tree the <code>SearchTree</code> instance of the AVL module
	 */
	public WelcomeAction(JalgoWindow parent, ModuleConnector connector,
			GUIController gui, SearchTree tree) {
	    this.parent = parent;
		this.connector = connector;
		this.gui = gui;
		this.tree = tree;
		setText("Willkommensbildschirm anzeigen");
		setToolTipText("�ffnet den Willkommensbildschirm");
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/avl/logo.gif"));
//		TODO: enable this, when switching to plugin structure
//		setImageDescriptor(ImageDescriptor.createFromURL(
//			getClass().getResource("/pix/avl/logo.gif")));
	}

	/**
	 * Performs the action.
	 */
	public void run() {
		switch (new MessageDialog(parent.getShell(), "Achtung", null,
				"M�chten Sie Ihre �nderungen verwerfen?", MessageDialog.QUESTION,
				new String[]{"Ja", "Nein", "Abbrechen"}, 1).open()) {
			case 0:
				tree.clear();
				gui.installWelcomeScreen();
				break;
			case 1:
				parent.getParent().newInstanceByName(
					connector.getModuleInfo().getName());
				break;
			case 2:
				return;
		}

//		if (MessageDialog.openConfirm(parent.getShell(),
//			"Achtung",
//			"Bei �ffnen des Willkommensbildschirms werden aktuelle �nderungen "+
//			"verworfen. Best�tigen sie dies mit \"Yes\" oder kehren Sie mit "+
//			"\"No\" zur�ck.")) {
//			tree.clear();
//			gui.installWelcomeScreen();
//		}
		//TODO: make possible to open a new module by selecting 'no'
		// -> ModuleConnector needs reference to JalgoMain or JalgoMain as Singleton
	}
}