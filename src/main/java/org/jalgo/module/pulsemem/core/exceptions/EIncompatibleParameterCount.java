package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.Admin;

/**
 * Diese Exception wird geworfen, wenn ein anderer Typ als erwartet gefunden
 * wurde. Streng genommen handelt es sich hierbei um einen syntaktischen Fehler.
 *
 * @author Frank Herrlich
 *
 */
public class EIncompatibleParameterCount extends EExecutionException {
	private static final String LanguageNode = "Exception.EIncompatibleParameterCount";
	private static final String MessageStart = LanguageNode + ".Message_Start";
	private static final String MessageBetween = LanguageNode + ".Message_Between";
	private static final String MessageEnd = LanguageNode + ".Message_End";

	public EIncompatibleParameterCount(String ExpectedCount, String FoundCount) {
		super(Admin.getLanguageString(MessageStart) + ExpectedCount
				+ Admin.getLanguageString(MessageBetween) + FoundCount
				+ Admin.getLanguageString(MessageEnd));
	}
}
