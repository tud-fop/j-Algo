package org.jalgo.module.pulsemem.core.exceptions;

/**
 * Eine Exception die geworfen wird, wenn ein Laufzeitproblem beim Ausführen des Codes auftritt.
 * @author Frank Herrlich
 *
 */
public abstract class EExecutionException extends RuntimeException {
	protected EExecutionException(String msg)
	{
		super(msg);
	}
}
