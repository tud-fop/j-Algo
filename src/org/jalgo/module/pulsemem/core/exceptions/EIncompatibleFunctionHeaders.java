package org.jalgo.module.pulsemem.core.exceptions;

import org.jalgo.module.pulsemem.Admin;
import c00.AST.FunctionHeading;

/**
 * Diese Exception wird geworfen, wenn eine Funktion implementiert wird, die vorher
 * mit einem anderen Header definiert wurde.
 *
 */
public class EIncompatibleFunctionHeaders extends EExecutionException {
	private static final String LanguageNode = "Exception.EIncompatibleFunctionHeaders";
	private static final String MessageStart = LanguageNode + ".Message_Start";
	private static final String MessageEnd = LanguageNode + ".Message_End";

	public EIncompatibleFunctionHeaders(FunctionHeading h) {
		super(Admin.getLanguageString(MessageStart) + h.startLine
				+ Admin.getLanguageString(MessageEnd));
	}
}
