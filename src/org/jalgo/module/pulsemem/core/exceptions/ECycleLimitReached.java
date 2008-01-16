package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.*;

/**
 * This exception is thrown if the interpretation cycle count reaches it's maximum.
 *
 */
public class ECycleLimitReached extends EExecutionException {

	private static final String LanguageNode = "Exception.ECycleLimitReached";
	private static final String MessageStart = LanguageNode + ".Message_Start";
	private static final String MessageEnd = LanguageNode + ".Message_End";

	public ECycleLimitReached(int maxCycles)
	{
		super(Admin.getLanguageString(MessageStart) + maxCycles
				+ Admin.getLanguageString(MessageEnd));
	}
}
