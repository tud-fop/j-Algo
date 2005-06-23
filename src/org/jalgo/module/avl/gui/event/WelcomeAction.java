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
		setToolTipText("Öffnet den Willkommensbildschirm");
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
				"Möchten Sie Ihre Änderungen verwerfen?", MessageDialog.QUESTION,
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
//			"Bei Öffnen des Willkommensbildschirms werden aktuelle Änderungen "+
//			"verworfen. Bestätigen sie dies mit \"Yes\" oder kehren Sie mit "+
//			"\"No\" zurück.")) {
//			tree.clear();
//			gui.installWelcomeScreen();
//		}
		//TODO: make possible to open a new module by selecting 'no'
		// -> ModuleConnector needs reference to JalgoMain or JalgoMain as Singleton
	}
}