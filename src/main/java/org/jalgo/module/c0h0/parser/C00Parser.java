package org.jalgo.module.c0h0.parser;

import java.io.IOException;
import java.io.StringReader;

import org.jalgo.module.c0h0.models.ast.*;

/**
 * the c00 parser
 *
 */
public class C00Parser{
	private Program program;
	private String errorText = "";

	/**
	 * returns the program
	 * 
	 * @return the program
	 */
	public Program getProgram() {
		return program;
	}

	/**
	 * returns the error text
	 * 
	 * @return the error text
	 */
	public String getErrorText() {
		return errorText;
	}

	/**
	 * initiates the parsing
	 * 
	 * @param text the text to parse
	 * @return if parsing was successful
	 */
	public boolean parse(String text) {
		boolean errorFlag = false;
		
		GeneratedC00Parser parser = new GeneratedC00Parser();
		C0Scanner scanner = new C0Scanner(new StringReader(text));
		
		try {
			program = (Program) parser.parse(scanner);
		}catch (beaver.Parser.Exception e) {
			errorFlag = true;
		}catch (IOException e) {
			errorFlag = true;
		}
		if(!errorFlag && !parser.getErrorEvents().hasErrors()) {
			return true;
		}
		// TODO mehr Fehlerbehandlung?
		errorText = parser.getErrorEvents().getErrorText();
		return false;
	}

}
