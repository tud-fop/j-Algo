package org.jalgo.module.unifikation.parser;

import java.util.List;

import org.jalgo.module.unifikation.algo.model.ProblemSet;

public interface ISetParser {
	/**
	 * Parses a String (with or w/o HTML)
	 * @param input String to parse
	 * @return true if input could be parsed
	 */
	public boolean parse(final String input);
	
	/**
	 * @return Error classes for occurred errors
	 */
	public List<ParserError> getErrors();
	
	/**
	 * @return ProblemSet as parsed from the string; null=error occurred
	 */
	public ProblemSet getResult();
}
