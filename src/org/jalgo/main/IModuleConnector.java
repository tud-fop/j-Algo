 /*
 * Created on May 18, 2004
 */
 
package org.jalgo.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.action.SubToolBarManager;

/**
 * @author Michael Pradel
 * @author Christopher Friedrich
 * @author Stephan Creutz
 */
public interface IModuleConnector {

	/**
	 * use this function after the user loaded a saved file for the module.
	 * @param 	data the loaded file consists of the module header, which was 
	 * 					added by the main program before saving (e.g. including 
	 * 					with which module the file is associated) and the data for the module; put the data in here
	 */
	public void setDataFromFile(ByteArrayInputStream data);

	/**
	 * use this function, when the user wants to save the state of the module.
	 * @return 		a stream with the data from the module, that has to 
	 * 					be stored in a file after the main program added the module header (e.g. including 
	 *						with which module the file is associated) to it
	 */
	public ByteArrayOutputStream getDataForFile();

	/**
	 * use this function, if the user clicked the print-button (or chose to print in any other way)
	 * the module will call a print dialog and manage the printing
	 */
	public void print();

	/**
	 * Get the Menu from the module
	 */
	public SubMenuManager getMenuManager();

	/**
	 * Get the ToolBar from the module
	 */
	public SubToolBarManager getToolBarManager();

	/**
	 * Get the StatusLine from the module
	 */
	public SubStatusLineManager getStatusLineManager();
	
	/**
	 * Get a class with all module information (name, description, version, ...)
	 */
	public IModuleInfo getModuleInfo();
	
}
