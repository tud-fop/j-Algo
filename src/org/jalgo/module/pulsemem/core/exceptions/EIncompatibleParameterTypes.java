package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.Admin;

/**
 * This exception is thrown when a function is called with the wrong
 * types of parameters.
 *
 */
public class EIncompatibleParameterTypes extends EExecutionException {
	private static final String LanguageNode = "Exception.EIncompatibleParameterTypes";
	private static final String MessageStart = LanguageNode + ".Message_Start";
	private static final String MessageBetween = LanguageNode + ".Message_Between";
	private static final String MessageEnd = LanguageNode + ".Message_End";

	public EIncompatibleParameterTypes(String ExpectedType, String FoundType, String name,int index) {
		super(Admin.getLanguageString(MessageStart) + name + ". " + (index+1)
				+ Admin.getLanguageString(MessageBetween) + ExpectedType
				+ Admin.getLanguageString(MessageEnd) + FoundType);
	}
}
