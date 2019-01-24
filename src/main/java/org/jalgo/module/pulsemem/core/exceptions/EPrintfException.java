package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.Admin;

/**
 * Diese Exception wird geworfen, wenn die Anzahl von Platzhaltern und Argumenten
 * bei einer Printf Anweisung nicht uebereinstimmt.
 *
 */
public class EPrintfException extends EExecutionException {
	private static final String LanguageNode = "Exception.EPrintfException";
	private static final String MessageStart = LanguageNode + ".Message_Start";

	public EPrintfException(int line) {
		super(Admin.getLanguageString(MessageStart) + line);
	}
}
