package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.*;

/**
 * Exception is thrown, when a PML is modified. (Error in Java-Implementation)
 */
public class EPMLException extends EExecutionException {

	private static final String LanguageNode = "Exception.EPMLException";
	private static final String MessageStart = LanguageNode + ".Message_Start";

	public EPMLException()
	{
		super(Admin.getLanguageString(MessageStart));
	}
}
