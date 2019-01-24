package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.*;

/**
 * Diese Exception wird geworfen, wenn auf einen nicht sichtbaren Speicherbereichzugegriffen wurde.
 * @author Joachim Protze
 *
 */
public class EMemoryError extends EExecutionException {

	private static final String LanguageNode = "Exception.EMemoryError";
	private static final String MessageStart = LanguageNode + ".Message_Start";

	public EMemoryError(String name)
	{
		super(Admin.getLanguageString(MessageStart) + name);
	}
}
