package org.jalgo.module.unifikation.parser;

import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.TokenStream;

/**
 * Extends generated parser to show and save errors
 * @author Alex
 *
 */
public class UnificationSetParserErrorReporter extends UnificationSetParser {
	private List<RecognitionException> errors;

	public UnificationSetParserErrorReporter(TokenStream input) {
		super(input);
		errors=new LinkedList<RecognitionException>();
	}
	
    public UnificationSetParserErrorReporter(TokenStream input, RecognizerSharedState state) {
        super(input, state);
		errors=new LinkedList<RecognitionException>();
    }
	
	public void reportError(RecognitionException e) {
		// if we've already reported an error and have not matched a token
		// yet successfully, don't report any errors.
		if ( state.errorRecovery ) {
			//System.err.print("[SPURIOUS] ");
			return;
		}
		state.syntaxErrors++; // don't count spurious
		state.errorRecovery = true;

		errors.add(e);
	}

	public List<RecognitionException> getErrors() {
		return errors;
	}

}
