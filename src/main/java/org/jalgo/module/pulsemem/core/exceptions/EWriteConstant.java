package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.*;

/**
 * Diese Exception wird geworfen, wenn auf einen nicht sichtbaren Speicherbereichzugegriffen wurde.
 * @author Joachim Protze
 *
 */
public class EWriteConstant extends EExecutionException {

	private static final String LanguageNode = "Exception.EWriteConstant";
	private static final String MessageStart = LanguageNode + ".Message_Start";
	private static final String MessageEnd = LanguageNode + ".Message_End";

	public EWriteConstant(String name)
	{
		super(Admin.getLanguageString(MessageStart) + name
			+ Admin.getLanguageString(MessageEnd));
	}
}
