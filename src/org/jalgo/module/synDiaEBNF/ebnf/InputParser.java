/*
 * Created on 09.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.ebnf;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Stephan Creutz
 */
public class InputParser extends Parser {
	private final Integer COMMA = new Integer(1);
	private final Integer SYMBOL = new Integer(3);
	private final BeginState beginState = new BeginState();
	private final ConcBeginState concBeginState = new ConcBeginState();
	private final ConcState concState = new ConcState();
	private final ErrorState errorState = new ErrorState();
	private InputState state = beginState;
	private Set set = new HashSet();

	public InputParser(String str) {
		tokenizer = new InputTokenizer(str);
	}

	public Set analyse() throws EbnfParseException {
		if (!tokenizer.hasNextToken()) {
			throw new EbnfParseException(Messages.getString("InputParser.Input_empty_1")); //$NON-NLS-1$
		}
		while (tokenizer.hasNextToken()) {
			state.check();
		}
		if (state == errorState) {
			throw new EbnfParseException(Messages.getString("InputParser.Invalid_input_2")); //$NON-NLS-1$
		}
		return set;
	}

	/*
	 * the following code implements the State-Design-Pattern
	 * @author Stephan
	 * 10.06.2004 17:31:55
	 */
	private abstract class InputState {
		public abstract void check();
	}

	private class BeginState extends InputState {
		public void check() {
			Token t = (Token) tokenizer.getNextToken();
			if (t.getTokenName().equals(SYMBOL)) {
				set.add(t.getTokenValue());
				state = concBeginState;
			} else {
				state = errorState;
			}
		}
	}

	private class ConcBeginState extends InputState {
		public void check() {
			Token t = (Token) tokenizer.getNextToken();
			if (t.getTokenName().equals(COMMA)) {
				state = concState;
			} else {
				state = errorState;
			}
		}
	}

	private class ConcState extends InputState {
		public void check() {
			Token t = (Token) tokenizer.getNextToken();
			if (t.getTokenName().equals(COMMA)) {
				state = concState;
			} else if (t.getTokenName().equals(SYMBOL)) {
				set.add(t.getTokenValue());
				state = concBeginState;
			} else {
				state = errorState;
			}
		}
	}

	private class ErrorState extends InputState {
		public void check(){
			Token t = (Token) tokenizer.getNextToken();
			state = errorState;
		}
	}
}
