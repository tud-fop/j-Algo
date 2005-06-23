/*
 * Created on 08.05.2005
 *
 */
package org.jalgo.module.dijkstraModule.util;

/**
 * Defines an Exception which is thrown by the parsing classes when an parsing error occurs.
 * 
 * @author Hannes Stra"s
 *
 */
public class ParsingException extends Exception
{
	private String message;
	
	/** Creates a ParsingException containing the given message.
	 * @param message error message
	 */
	public ParsingException(String message)
	{
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage()
	{
		return message;
	}
}
