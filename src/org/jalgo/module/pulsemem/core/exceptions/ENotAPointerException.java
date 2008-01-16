package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.*;

/**
 * This exception is thrown when a non-pointer Variable is dereferenced.
 *
 */
public class ENotAPointerException extends EExecutionException {

	private static final String LanguageNode = "Exception.ENotAPointerException";
	private static final String MessageStart = LanguageNode + ".Message_Start";
	private static final String MessageEnd = LanguageNode + ".Message_End";

	public ENotAPointerException(String name)
	{
		super(Admin.getLanguageString(MessageStart) + name
			+ Admin.getLanguageString(MessageEnd));
	}
}
