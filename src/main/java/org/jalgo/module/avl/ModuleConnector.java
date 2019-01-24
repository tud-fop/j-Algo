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

/* Created on 12.04.2005 */
package org.jalgo.module.avl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.datastructure.Node;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.gui.GUIController;

/**
 * This class provides the bridge between the main program and the current
 * instance of the AVL module. It handles the references of several objects and
 * provides some methods used in the main program.
 * 
 * @author Alexander Claus
 */
public class ModuleConnector
extends AbstractModuleConnector {

	private SearchTree tree;
	private Controller controller;
	private GUIController gui;

	/**
	 * Initializes the <code>ModuleConnector</code> instance for the AVL module.
	 * Instances of the module specific <code>SearchTree</code>,
	 * <code>Controller</code> and <code>GUIController</code> are created
	 * here.
	 * 
	 * @see AbstractModuleConnector#init()
	 */
	public void init() {
		tree = new SearchTree();
		controller = new Controller(tree);
		gui = new GUIController(this, controller, tree);
	}

	/**
	 * The "program code" of the AVL module. Currently there is only the welcome
	 * screen displayed.
	 * 
	 * @see org.jalgo.main.AbstractModuleConnector#run()
	 */
	public void run() {
		gui.installWelcomeScreen();
	}

	/**
	 * This method is invoked, when module or program are intended to be closed.
	 * If there are no changes to save, it returns true, otherwise the user is
	 * asked for saving his work.
	 * 
	 * @return <code>true</code>, if module is ready to be closed,
	 *         <code>false</code> otherwise
	 */
	public boolean close() {
		// if some dialogs are open, close this first
		if (gui.isDialogOpen()) return false;

		// ensure, that tree is in consistent state
		if (controller.algorithmHasNextStep()) try {
			controller.abort();
			gui.algorithmAborted();
		}
		catch (NoActionException ex) {
			// no exception handling here, module is being closed
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.AbstractModuleConnector#setDataFromFile(java.io.ByteArrayInputStream)
	 */
	@SuppressWarnings("unchecked")
	public void setDataFromFile(ByteArrayInputStream data) {
		try {
			ObjectInputStream in = new ObjectInputStream(data);
			boolean avlMode = in.readBoolean();
			gui.setAVLMode(avlMode, avlMode);
			tree.importLevelOrder((List<Integer>)in.readObject());
			gui.installStandardLayout();
		}
		catch (IOException ex) {
			JAlgoGUIConnector.getInstance().showErrorMessage(Messages.getString(
				"avl", "ModuleConnector.No_valid_AVL_file")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (ClassNotFoundException ex) {
			JAlgoGUIConnector.getInstance().showErrorMessage(Messages.getString(
				"avl", "ModuleConnector.Loading_error") + //$NON-NLS-1$ //$NON-NLS-2$
				System.getProperty("line.separator") + //$NON-NLS-1$
				Messages.getString("avl", "ModuleConnector.File_damaged")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Here the data for serializing the <code>SearchTree</code> is formatted.
	 * First there is a boolean value serialized, indicating, if the tree is an
	 * AVL-tree. Secondly the integer keys of the <code>Node</code>s are
	 * serialized in a levelordered linked list.
	 * 
	 * @see org.jalgo.main.AbstractModuleConnector#getDataForFile()
	 */
	public ByteArrayOutputStream getDataForFile() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeBoolean(controller.isAVLMode());
			List<Node> nodes = tree.exportLevelOrder();
			List<Integer> keys = new LinkedList<Integer>();
			for (Node node : nodes)
				keys.add(node.getKey());
			objOut.writeObject(keys);
			objOut.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		return out;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.AbstractModuleConnector#print()
	 */
	public void print() {
	// printing is currently not supported
	}
}