package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.*;

/**
 * Diese Exception wird geworfen, wenn ein Pointer nicht initialisiert wurde.
 * @author Frank Herrlich
 *
 */
public class ENullPointer extends EExecutionException {

	private static final String LanguageNode = "Exception.ENullPointer";
	private static final String MessageStart = LanguageNode + ".Message_Start";
	private static final String MessageEnd = LanguageNode + ".Message_End";

	public ENullPointer(String name)
	{
		super(Admin.getLanguageString(MessageStart) + name
			+ Admin.getLanguageString(MessageEnd));
	}
}
