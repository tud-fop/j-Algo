package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.*;

/**
 * This exception is thrown if the user wants to abort the interpretation process.
 *
 */
public class EUserAbort extends EExecutionException {

	private static final String LanguageNode = "Exception.EUserAbort";
	private static final String Message = LanguageNode + ".Message";	
	
	public EUserAbort()
	{
		super(Admin.getLanguageString(Message));
	}
}
