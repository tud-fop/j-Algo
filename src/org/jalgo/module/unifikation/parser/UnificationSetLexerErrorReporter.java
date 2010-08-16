package org.jalgo.module.unifikation.parser;

import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

/**
 * Extends generated lexer to show and save errors
 * @author Alex
 *
 */
public class UnificationSetLexerErrorReporter extends UnificationSetLexer {
	private List<RecognitionException> errors;

	public UnificationSetLexerErrorReporter() {
		errors=new LinkedList<RecognitionException>();
	}

	public UnificationSetLexerErrorReporter(CharStream input) {
		super(input);
		errors=new LinkedList<RecognitionException>();
	}

	public UnificationSetLexerErrorReporter(CharStream input,
			RecognizerSharedState state) {
		super(input, state);
		errors=new LinkedList<RecognitionException>();
	}

	@Override
	public void reportError(RecognitionException e) {
		super.reportError(e);
		errors.add(e);
		state.syntaxErrors++;
	}
	
	public List<RecognitionException> getErrors(){
		return errors;
	}
	
}
