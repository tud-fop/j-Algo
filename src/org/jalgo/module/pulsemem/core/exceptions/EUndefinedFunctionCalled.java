package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.Admin;

/**
 * Exception is thrown when an undefined function has been called.
 *
 */
public class EUndefinedFunctionCalled extends EExecutionException {
	private static final String LanguageNode = "Exception.EUndefinedFunctionCalled";
	private static final String MessageStart = LanguageNode + ".Message_Start";

	public EUndefinedFunctionCalled(int line) {
		super(Admin.getLanguageString(MessageStart) + line);
	}
}
