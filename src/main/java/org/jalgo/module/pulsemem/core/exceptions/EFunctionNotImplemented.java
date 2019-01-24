package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.*;

/**
 * Diese Eception wird geworfen, wenn eine nicht implementierte, aber deklarierte
 * Funktion aufgerufen wird
 *
 */
public class EFunctionNotImplemented extends EExecutionException {
	private static final String LanguageNode = "Exception.EFunctionNotImplemented";
	private static final String MessageStart = LanguageNode + ".Message_Start";
	private static final String MessageEnd = LanguageNode + ".Message_End";

	public EFunctionNotImplemented(int line) {
		super(Admin.getLanguageString(MessageStart) + line
			+ Admin.getLanguageString(MessageEnd));
	}
}