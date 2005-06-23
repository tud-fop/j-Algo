/* Created on 26.05.2005 */
package org.jalgo.module.avl.gui.event;

import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.program.Program;
import org.jalgo.main.util.BrowserLauncher;
import org.jalgo.module.avl.gui.GUIConstants;
import org.jalgo.module.avl.gui.GUIController;

/**
 * The class <code>HelpAction</code> defines an <code>Action</code> object, which
 * can be added to toolbars and menus. Performing this action opens the online help
 * file. The path to this file is defined in <code>GUIConstants</code>.<br>
 * This action comes with the standard key accelerator 'F1', although it doesn't work
 * properly. Calling help with 'F1' should be provided by the jAlgo main program.
 * 
 * @author Alexander Claus
 */
public class HelpAction
extends Action
implements GUIConstants {

	private GUIController gui;
	private String helpPath;
	private static final String fileSep = System.getProperty("file.separator");
	private static final String lineSep = System.getProperty("line.separator");
	
	/**
	 * Constructs a <code>HelpAction</code> object with the given reference.
	 * 
	 * @param gui the <code>GUIController</code> instance of the AVL module
	 */
	public HelpAction(GUIController gui) {
		this.gui = gui;

		setText("Hilfe zum AVL-Modul");
		setToolTipText("Öffnet die Hilfe zum AVL-Modul");
		setImageDescriptor(ImageDescriptor.createFromFile(null, "pix/help.gif"));
//TODO: enable this, when switching to plugin structure
//		setImageDescriptor(ImageDescriptor.createFromURL(
//			getClass().getResource("/pix/help.gif")));
		setAccelerator(SWT.F1);
		helpPath = System.getProperty("user.dir")+fileSep+HELP_FILE_NAME.
			replace(":",fileSep);
	}

	/**
	 * Performs the action.
	 */
	public void run() {
		try {BrowserLauncher.openURL(helpPath);}
		catch (IOException ex) {
			gui.showErrorMessage("Konnte Hilfedatei nicht öffnen!"+lineSep+
				"Bitte öffnen Sie die Hilfe manuell mit Ihrem Browser");			
		}
//		if (System.getProperty("os.name").toLowerCase().startsWith("windows")) try {
//			Runtime.getRuntime().exec(
//				"C:\\Programme\\Internet Explorer\\iexplore.exe "+helpPath);
//		}
//		catch (IOException ex) {
//			gui.showErrorMessage("Konnte Hilfedatei nicht öffnen!"+lineSep+
//				"Bitte öffnen Sie die Hilfe manuell mit Ihrem Browser");
//		}
	}
}