package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.Admin;

/**
 * Diese Exception wird geworfen, wenn ein anderer Typ als erwartet gefunden
 * wurde. Streng genommen handelt es sich hierbei um einen syntaktischen Fehler.
 *
 * @author Frank Herrlich
 *
 */
public class ETypeExpected extends EExecutionException {
	private static final String LanguageNode = "Exception.ETypeExpected";
	private static final String MessageStart = LanguageNode + ".Message_Start";
	private static final String MessageBetween = LanguageNode + ".Message_Between";
	private static final String MessageEnd = LanguageNode + ".Message_End";

	public ETypeExpected(String typeExpectedName, String typeFoundName, int line) {
		super(Admin.getLanguageString(MessageStart) + typeExpectedName
				+ Admin.getLanguageString(MessageBetween) + typeFoundName
				+ Admin.getLanguageString(MessageEnd) +line);
	}
}
