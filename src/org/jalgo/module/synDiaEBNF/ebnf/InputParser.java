/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

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
			Token t = tokenizer.getNextToken();
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
			Token t = tokenizer.getNextToken();
			if (t.getTokenName().equals(COMMA)) {
				state = concState;
			} else {
				state = errorState;
			}
		}
	}

	private class ConcState extends InputState {
		public void check() {
			Token t = tokenizer.getNextToken();
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
			tokenizer.getNextToken();
			state = errorState;
		}
	}
}
