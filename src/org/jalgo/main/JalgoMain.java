/*
 * Created on 13.04.2004
 */
 
package org.jalgo.main;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.main.gui.actions.SaveAsAction;
import org.jalgo.main.util.Storage;
import org.jalgo.module.synDiaEBNF.ModuleConnector;

/**
 * @author Christopher Friedrich
 */
public class JalgoMain {

	private JalgoWindow applicationWindow;

	private LinkedList knownModules;

	private HashMap openInstances;
	private IModuleConnector currentInstance;

	public JalgoMain() {

		knownModules = new LinkedList();
		scanForModules();

		openInstances = new HashMap();
	}

	public void createGUI() {
		applicationWindow =
			new JalgoWindow(this, getKnownModules(), currentInstance);

		applicationWindow.setBlockOnOpen(true);
		applicationWindow.open();

		Display.getCurrent().dispose();

	}

	public void scanForModules() {
		//TODO mainWin.updateInitialToolBarManager("&New").add()
		//knownModules.add("Testmodul");
	}

	public Collection getKnownModules() {
		return knownModules;
	}

	public IModuleConnector getCurrentInstance() {
		return currentInstance;
	}

	public void itemClosed(CTabItem cti) {

		//IModuleConnector mc = (IModuleConnector) openInstances.get(comp);
		openInstances.remove(cti);
		cti.dispose();

		if (openInstances.isEmpty()) {
			itemSelected(null);
		} else {
			itemSelected(applicationWindow.getCTabFolder().getSelection());
		}

	}

	/**
	 * Set currentInstance to corresponding CTabItem
	 * 
	 * @param cti 
	 */
	public void itemSelected(CTabItem cti) {

		//	Makes current Module-Tool/MenuBar invisible
		try {
			currentInstance.getMenuManager().setVisible(false);
			currentInstance.getToolBarManager().setVisible(false);
		} catch (NullPointerException e) {

		}

		// Set currentInstance to cti or to null
		if (cti == null) {
			currentInstance = null;
		} else {

			currentInstance = (IModuleConnector) openInstances.get(cti);
			try {
				String filename = new String();
				filename = currentInstance.getModuleInfo().getOpenFileName();
				if (filename == null)
					filename = Messages.getString("JalgoMain.none_1"); //$NON-NLS-1$
				cti.setText(
					filename
						+ " (" //$NON-NLS-1$
						+ currentInstance.getModuleInfo().getName()
						+ ")"); //$NON-NLS-1$
			} catch (NullPointerException e) {

			}
			applicationWindow.getCTabFolder().update();

			try {
				currentInstance.getMenuManager().setVisible(true);
				currentInstance.getToolBarManager().setVisible(true);
			} catch (NullPointerException e) {
				// TODO Handel Exception
			}

		}

		applicationWindow.getMenuBarManager().update(true);
		applicationWindow.getToolBarManager().update(true);
	}

	public IModuleConnector newInstance(String moduleType) {

		return newInstance(moduleType, true);
	}

	public IModuleConnector newInstance(
		String moduleType,
		boolean startWizard) {

		// Makes current Module-Tool/MenuBar invisible
		try {
			currentInstance.getMenuManager().setVisible(false);
			currentInstance.getToolBarManager().setVisible(false);
		} catch (NullPointerException e) {
		}

		// Requests a fresh CTabItem from the applicationWindow
		CTabItem cti =
			applicationWindow.requestNewCTabItem(
				Messages.getString("JalgoMain.none_(unknown)_4"), //$NON-NLS-1$
				new Image(
					applicationWindow.getShell().getDisplay(),
					"pix/jalgo-file.png")); //$NON-NLS-1$

		// Create a new instance of a module.
		currentInstance =
			new ModuleConnector(
				startWizard,
				applicationWindow,
				(Composite) cti.getControl(),
				new SubMenuManager(applicationWindow.getMenuBarManager()),
				new SubToolBarManager(applicationWindow.getToolBarManager()),
				new SubStatusLineManager(
					applicationWindow.getTheStatusLineManager()));

		cti.setText(Messages.getString("JalgoMain.none_(_6") + currentInstance.getModuleInfo().getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$

		/* not nice, but works ... change when cleaning up module loading  START */
		applicationWindow.setCurrentInstance(currentInstance);
		/* not nice, but works ... change when cleaning up module loading  END */

		// Set CTabItem selected
		applicationWindow.getCTabFolder().setSelection(
			applicationWindow.getCTabFolder().getItemCount() - 1);

		// Activate the modules Menu
		currentInstance.getMenuManager().setVisible(true);
		applicationWindow.getMenuBarManager().update(true);

		// Activate the modules ToolBar
		currentInstance.getToolBarManager().setVisible(true);
		applicationWindow.getToolBarManager().update(true);

		// Add module to running instances
		openInstances.put(cti, currentInstance);

		return currentInstance;

	}

	/**
	 * Takes content from module and stores it in currently used file
	 * 
	 */
	public boolean saveFile() {
		if (currentInstance.getModuleInfo().getOpenFileName() == null) {
			SaveAsAction a = new SaveAsAction(applicationWindow);
			a.run();
			return true;
		}
		return saveFileAs(currentInstance.getModuleInfo().getOpenFileName());
	}

	/**
	 * Takes content from module and stores it in file with given filename
	 * 
	 * @param filename
	 */
	public boolean saveFileAs(String filename) {
		return Storage.save(filename);
	}

	/**
	 * Opens file 
	 * 
	 * @param filename
	 */
	public boolean openFile(String filename) {

		return Storage.load(filename);
	}

	/**
	 * Opens file and gives content to Module
	 * 
	 * @param filename
	 */
	public boolean openFile(String filename, boolean useCurrentInstance) {

		if (useCurrentInstance) {
			return Storage.load(filename, currentInstance);
		} else {
			return Storage.load(filename);
		}
	}

}
