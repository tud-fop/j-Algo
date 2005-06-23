/* Created on 12.06.2005 */
package org.jalgo.module.avl.gui.event;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.module.avl.gui.DisplayModeChangeable;
import org.jalgo.module.avl.gui.Settings;

/**
 * The class <code>ToggleDisplayAction</code> defines a checkbox menuitem for
 * switching between beamer mode and pc mode.<br>
 * Here the Singleton design pattern is implemented, in order that this setting
 * takes global effect for all open instances of the AVL module.<br>
 * Components, which want to be notified, when display mode changes, have to
 * register as observers. So <code>ToggleDisplayAction</code> acts as Observable.
 * 
 * @author Alexander Claus
 */
public class ToggleDisplayModeAction
extends Action {

	//the singleton instance
	private static ToggleDisplayModeAction instance;

	//the list of observers
	private static List<DisplayModeChangeable> observers;

	/**
	 * Constructs the singleton instance of <code>ToggleDisplayModeAction</code>.
	 */
	private ToggleDisplayModeAction() {
		super("Beamermodus", Action.AS_CHECK_BOX);
		setImageDescriptor(
				ImageDescriptor.createFromFile(null, "pix/avl/beamer.gif"));
//		TODO: enable this, when switching to plugin structure
//		setImageDescriptor(ImageDescriptor.createFromURL(
//			getClass().getResource("/pix/avl/beamer.gif")));
		setChecked(Settings.getDisplayMode() == Settings.BEAMER_MODE);
		observers = new LinkedList<DisplayModeChangeable>();
	}

	/**
	 * Retrieves and, if necessary, initializes the singleton instance of
	 * <code>ToggleDisplayModeAction</code>.
	 * 
	 * @return the singleton instance
	 */
	public static ToggleDisplayModeAction getInstance() {
		if (instance == null) instance = new ToggleDisplayModeAction();
		return instance;
	}

	/**
	 * Adds the given components to the list of targets, which will be notified,
	 * when action is performed. These components acts as observers.
	 * 
	 * @param observer a component implementing <code>DisplayModeChangeable</code>
	 */
	public static void registerTarget(DisplayModeChangeable observer) {
		observers.add(observer);
	}

	/**
	 * Performs the action. The setting is changed globally and observers are
	 * notified.
	 */
	public void run() {
		Settings.setDisplayMode(isChecked()?Settings.BEAMER_MODE:Settings.PC_MODE);
		for (DisplayModeChangeable observer : observers) {
			observer.displayModeChanged();
		}
	}
}