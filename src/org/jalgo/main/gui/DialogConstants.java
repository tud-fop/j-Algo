package org.jalgo.main.gui;

import org.jalgo.main.util.Messages;

/**
 * The class DialogConstants contains some constant values used in dialog boxes.
 * 
 * @author Alexander
 */
public class DialogConstants {

	/** Return value from class method if YES is chosen. */
	public static final int YES_OPTION = 0;
	/** Return value from class method if NO is chosen. */
	public static final int NO_OPTION = 1;
	/** Return value from class method if CANCEL is chosen. */
	public static final int CANCEL_OPTION = 2;
	/** Return value from class method if OK is chosen. */
	public static final int OK_OPTION = 0;

	/** Option type for confirm dialogs. */
	public static final int YES_NO_OPTION = 1;
	/** Option type for confirm dialogs. */
	public static final int YES_NO_CANCEL_OPTION = 2;
	/** Option type for confirm dialogs. */
	public static final int OK_CANCEL_OPTION = 3;
	
	/**
	 * Retrieves an array of Strings containing the option strings for a dialog
	 * box. What strings are in the array is specified by the
	 * <code>option</code> parameter.
	 * 
	 * @param optionType one of the following constants:
	 * <ul>
	 * <li><code>YES_NO_OPTION</code></li>
	 * <li><code>YES_NO_CANCEL_OPTION</code></li>
	 * <li><code>OK_CANCEL_OPTION</code></li>
	 * </ul>
	 * 
	 * @return a String array containing the requested option strings
	 */
	public static String[] getOptionStrings(int optionType) {
		switch (optionType) {
			case YES_NO_OPTION:
				return new String[] {
					Messages.getString("main", "DialogConstants.Yes"),
					Messages.getString("main", "DialogConstants.No")};
			case YES_NO_CANCEL_OPTION:
				return new String[] {
					Messages.getString("main", "DialogConstants.Yes"),
					Messages.getString("main", "DialogConstants.No"),
					Messages.getString("main", "DialogConstants.Cancel")};
			case OK_CANCEL_OPTION:
				return new String[] {
					Messages.getString("main", "DialogConstants.Ok"),
					Messages.getString("main", "DialogConstants.Cancel")};
			default:
				return null;
		}
	}
}