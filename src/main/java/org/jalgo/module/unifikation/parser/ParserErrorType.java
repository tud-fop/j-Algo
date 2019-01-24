package org.jalgo.module.unifikation.parser;

public enum ParserErrorType {
	/**
	 * body tag without ending tag
	 */
	InvalidBodyTag,
	/**
	 * invalid tag (not supported by parser)
	 */
	InvalidTag,
	/**
	 * Internal Error (wrong grammar...)
	 * should never happen
	 */
	InternalError,
	/**
	 * invalid token (char or sequence of chars)
	 */
	InvalidToken,
	/**
	 * missing token (char or sequence of chars) (e.g. missing M={})
	 */
	MissingToken,
	/**
	 * exception thrown by Lexer
	 */
	LexerError,
	/**
	 * exception thrown by parser
	 */
	ParserError,
	/**
	 * Invalid arity
	 */
	InvalidArity
	;
	@Override
	public String toString(){
		switch(this)
		{
		case ParserError:
			return "Fehler";
		case InvalidArity:
			return "Stelligkeitsfehler";
		default:
			return super.toString();
		}
	}
}
