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
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/avl/clear.gif"));
//TODO: enable this, when switching to plugin structure
//		setImageDescriptor(ImageDescriptor.createFromURL(
//			getClass().getResource("/pix/avl/clear.gif")));
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