package org.jalgo.main.gui;

import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.JalgoMain;

/**
 * Class <code>JAlgoGUIConnector</code> represents the counterpart to the
 * <code>AbstractModuleConnector</code> interface. It contains methods to use
 * from module instances or several non-GUI parts of the program. So it provides
 * a kind of encapsulation and helps to split strictly the main program from the
 * modules.<br>
 * This class is realized in the Singleton design pattern, because of easy
 * management and the reason, that there is only one GUI, to connect with...<br>
 * This class contains methods for displaying several message boxes, for
 * managing the save-button-enable-status, and for setting the status message.
 * Also this class contains access methods for the GUI components of the opened
 * module instances, such as the main GUI component, the menu or the toolbar.
 * 
 * @author Alexander Claus
 */
public class JAlgoGUIConnector {

	/** The singleton instance of <code>JAlgoGUIConnector</code> */
	private static JAlgoGUIConnector instance;

	/**
	 * Constructs an object of <code>JAlgoGUIConnector</code>. This constructor
	 * is declared as private to avoid access from outside this class. This
	 * mechanism is part of the Singleton design pattern.
	 */
	private JAlgoGUIConnector() {
	// nothing to initialize
	}

	/**
	 * Retrieves the singleton instance of this class.
	 * 
	 * @return the singleton instance of <code>JAlgoGUIConnector</code>
	 */
	public static JAlgoGUIConnector getInstance() {
		if (instance == null) instance = new JAlgoGUIConnector();
		return instance;
	}

	/**
	 * This method has to be called, if the save status of a module instance
	 * changes. It guarantees, that the enabled status of the save buttons are
	 * controlled in the right way and so get the well known semantics of "save"
	 * and "save as".<br>
	 * A module, which save status changes, has to call this method with the
	 * current instance of its <code>AbstractModuleConnector</code> as
	 * argument. If the current active module instance is not the relating
	 * module instance, this method has no effect.
	 * 
	 * @param moduleInstance the instance of the
	 *            <code>AbstractModuleConnector</code> of the relating module
	 */
	public void saveStatusChanged(AbstractModuleConnector moduleInstance) {
		if (JalgoMain.getInstance().getCurrentInstance() != moduleInstance) return;
		JalgoWindow.getInstance().updateSaveButtonEnableStatus();
		JalgoWindow.getInstance().updateTitle();
	}

	/**
	 * Opens an error message with the given message string.
	 * 
	 * @param msg the message string
	 * 
	 * @see JalgoWindow#showErrorMessage(String)
	 */
	public void showErrorMessage(String msg) {
		JalgoWindow.getInstance().showErrorMessage(msg);
	}

	/**
	 * Opens a warning message with the given message string.
	 * 
	 * @param msg the message string
	 * 
	 * @see JalgoWindow#showWarningMessage(String)
	 */
	public void showWarningMessage(String msg) {
		JalgoWindow.getInstance().showWarningMessage(msg);
	}

	/**
	 * Opens an info message box with the given message string.
	 * 
	 * @param msg the message string
	 * 
	 * @see JalgoWindow#showInfoMessage(String)
	 */
	public void showInfoMessage(String msg) {
		JalgoWindow.getInstance().showInfoMessage(msg);
	}

	/**
	 * Opens a confirm dialog with the given question string. The type of option
	 * buttons can be selected using the second argument. It can be one of the
	 * following constants defined in {@link org.jalgo.main.gui.DialogConstants}:
	 * <ul>
	 * <li><code>YES_NO_OPTION</code></li>
	 * <li><code>YES_NO_CANCEL_OPTION</code></li>
	 * <li><code>OK_CANCEL_OPTION</code></li>
	 * </ul>
	 * The return value indicates, which button the user pressed. It can be one
	 * of the constants defined in {@link org.jalgo.main.gui.DialogConstants}:
	 * <ul>
	 * <li><code>OK_OPTION</code></li>
	 * <li><code>YES_OPTION</code></li>
	 * <li><code>NO_OPTION</code></li>
	 * <li><code>CANCEL_OPTION</code></li>
	 * </ul>
	 * 
	 * @param question the question string
	 * @param optionType the type of the option buttons to be displayed
	 * 
	 * @return an integer designating the option selected by the user
	 * 
	 * @see JalgoWindow#showConfirmDialog(String, int)
	 */
	public int showConfirmDialog(String question, int optionType) {
		return JalgoWindow.getInstance().showConfirmDialog(question, optionType);
	}

	/**
	 * Sets the message at the status line of the main program to the given
	 * message string.
	 * 
	 * @param msg the message string
	 */
	public void setStatusMessage(String msg) {
		JalgoWindow.getInstance().setStatusMessage(msg);
	}

	/**
	 * Opens a filechooser for opening files. The file selected by the user can
	 * be opened automatically as j-Algo file. When selected this option the
	 * second parameter specifies, if the selected file should be opened in the
	 * current instance of the module or if a new module instance should be
	 * opened.<br>
	 * If the file should not be opened automatically, e.g. for using the file
	 * in other ways, the file name is returned as string.
	 * 
	 * @param openAsJAlgoFile <code>true</code>, if the file should be opened
	 *            as j-Algo file, <code>false</code> otherwise
	 * @param useCurrentModuleInstance <code>true</code>, if the file should
	 *            be opened in current module instance, <code>false</code>
	 *            otherwise
	 * 
	 * @return the file name of the selected file, if the first parameter is set
	 *         to <code>false</code>
	 * 
	 * @see JalgoWindow#showOpenDialog(boolean, boolean)
	 */
	public String showOpenDialog(boolean openAsJAlgoFile,
		boolean useCurrentModuleInstance) {
		return JalgoWindow.getInstance().showOpenDialog(openAsJAlgoFile, useCurrentModuleInstance);
	}

	/**
	 * Creates a new instance of the module with the given name. Returns
	 * <code>null</code>, if the given name does not match to any known
	 * module name.
	 * 
	 * @param moduleName the name of the module to be created
	 * 
	 * @return the <code>AbstractModuleConnector</code> instance of the
	 *         module, if it is created, <code>null</code> otherwise
	 * 
	 * @see JalgoMain#newInstanceByName(String)
	 */
	public AbstractModuleConnector newModuleInstanceByName(String moduleName) {
		return JalgoMain.getInstance().newInstanceByName(moduleName);
	}

	/**
	 * Retrieves the main GUI component of the module instance, having the given
	 * <code>AbstractModuleConnector</code>.
	 * 
	 * @param module the <code>AbstractModuleConnector</code> instance of module
	 * 
	 * @return the main GUI component of the module instance
	 */
	public Composite getModuleComponent(AbstractModuleConnector module) {
		return JalgoWindow.getInstance().getModuleComponent(module);
	}

	/**
	 * Sets the main GUI component of the module instance, having the given
	 * <code>AbstractModuleConnector</code>.
	 * 
	 * @param module the <code>AbstractModuleConnector</code> of the module
	 * @param comp the main GUI component of the module instance
	 */
	public void setModuleComponent(AbstractModuleConnector module,
		Composite comp) {
		JalgoWindow.getInstance().setModuleComponent(module, comp);
	}

	/**
	 * Retrieves the menu of the module instance, having the given
	 * <code>AbstractModuleConnector</code>.
	 * 
	 * @param module the <code>AbstractModuleConnector</code> instance of module
	 * 
	 * @return the menu of the module instance
	 */
	public SubMenuManager getModuleMenu(AbstractModuleConnector module) {
		return JalgoWindow.getInstance().getModuleMenu(module);
	}

	/**
	 * Sets the menu of the module instance, having the given
	 * <code>AbstractModuleConnector</code>.
	 * 
	 * @param module the <code>AbstractModuleConnector</code> of the module
	 * @param menu the menu of the module instance
	 */
	public void setModuleMenu(AbstractModuleConnector module,
		SubMenuManager menu) {
		JalgoWindow.getInstance().setModuleMenu(module, menu);
	}

	/**
	 * Retrieves the toolbar of the module instance, having the given
	 * <code>AbstractModuleConnector</code>.
	 * 
	 * @param module the <code>AbstractModuleConnector</code> instance of module
	 * 
	 * @return the toolbar of the module instance
	 */
	public SubToolBarManager getModuleToolbar(AbstractModuleConnector module) {
		return JalgoWindow.getInstance().getModuleToolbar(module);
	}

	/**
	 * Sets the toolbar of the module instance, having the given
	 * <code>AbstractModuleConnector</code>.
	 * 
	 * @param module the <code>AbstractModuleConnector</code> of the module
	 * @param toolbar the toolbar of the module instance
	 */
	public void setModuleToolbar(AbstractModuleConnector module,
		SubToolBarManager toolbar) {
		JalgoWindow.getInstance().setModuleToolbar(module, toolbar);
	}
}