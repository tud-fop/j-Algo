package org.jalgo.module.c0h0.parser;

/**
 * Set of constants that are used by the parser package.
 * 
 */
public final class ParserConstants {
	/**
	 * This class is not designed to be instantiated, therefore it's constructor
	 * is private.
	 */
	private ParserConstants() {
		throw new AssertionError();
	}

	/**
	 * Constant used as the value for end-of-file symbols (i.e. tokens).
	 */
	public static final String EOF = "end-of-file";

	/**
	 * Constant used as the value for end-of-line symbols (i.e. tokens).
	 */
	public static final String EOL = "end-of-line";
}
