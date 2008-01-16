package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.*;

/**
 * Diese Eception wird geworfen, wenn zwei Typen inkompatibel sind.
 *
 * @author Frank Herrlich
 *
 */
public class EIncompatibleTypes extends EExecutionException {
	private static final String LanguageNode = "Exception.EIncompatibleTypes";
	private static final String MessageStart = LanguageNode + ".Message_Start";
	private static final String MessageBetween = LanguageNode + ".Message_Between";
	private static final String MessageEnd = LanguageNode + ".Message_End";

	public EIncompatibleTypes(String typeName1, String typeName2, String varName) {
		super(Admin.getLanguageString(MessageStart) + varName
				+ Admin.getLanguageString(MessageBetween) + typeName1
				+ Admin.getLanguageString(MessageEnd) + typeName2);
	}
}
