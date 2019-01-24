package org.jalgo.module.ebnf.controller.wordalgorithm;

import java.util.List;

import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;

/**
 * Method Parses an Word and checks if its only contains valid TerminalSymbols.
 * 
 * @author Claas Wilke
 * 
 */
public class WordParser {

	private WordAlgoModel myModel;

	/**
	 * Constructs a new WordParser
	 * 
	 * @param myModel
	 *            The <code>WordAlgoModel</code> which should be used to parse
	 *            Words.
	 */
	public WordParser(WordAlgoModel myModel) {

		this.myModel = myModel;

	}

	/**
	 * Parses a String and checks if its only contains TeminalSymbols which are
	 * in the parsers model.
	 * 
	 * @param word
	 *            The word which should be parsed.
	 * @return True im <code>word</code> only contains TeminalSymbols which
	 *         are in the parsers model.
	 */
	public boolean isWordValid(String symbols) {
		 List<String> terminalList = myModel.getSynDiaSystem().getLabelsOfTerminals();
		 boolean changed = true;
		 while (changed) {
		 changed = false;
		 for (String aTerminalName : terminalList) {
			 if (symbols.startsWith(aTerminalName)) {
				 symbols = symbols.substring(aTerminalName.length());
				 changed = true;
			 }
		 }
		 }
		 // If all symbols were removed, the String contains only valid
		 // TerminalSymbols
		 if (!symbols.equals("")) {
		 return false;
		 }
		return true;
	}

}
