package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.Admin;

/**
 * Exception is thrown when an undefined function has been called.
 *
 */
public class EPointerTargetException extends EExecutionException {
	private static final String LanguageNode = "Exception.EPointerTargetException";
	private static final String MessageStart = LanguageNode + ".Message_Start";
	private static final String MessageEnd = LanguageNode + ".Message_End";

	public EPointerTargetException(String name) {
		super(Admin.getLanguageString(MessageStart) + name
			+ Admin.getLanguageString(MessageEnd));
	}
}