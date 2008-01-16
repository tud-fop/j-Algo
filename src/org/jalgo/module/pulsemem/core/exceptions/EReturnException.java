package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.Admin;

/**
 * Diese Exception wird geworfen, wenn eine Funktion einen Wert zurückliefert,
 * der nicht dem erwartetetn Typ entspricht.
 *
 */
public class EReturnException extends EExecutionException {
	private static final String LanguageNode = "Exception.EReturnException";
	private static final String MessageStart = LanguageNode + ".Message_Start";
	private static final String MessageBetween = LanguageNode + ".Message_Between";
	private static final String MessageEnd = LanguageNode + ".Message_End";

	public EReturnException(String t1, String t2, int line) {
		super(Admin.getLanguageString(MessageStart) + t1
				+ Admin.getLanguageString(MessageBetween) +t2
				+ Admin.getLanguageString(MessageEnd)+ line);
	}
}