/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
 * Created on 17.04.2004
 */

package org.jalgo.module.synDiaEBNF.ebnf;

import java.util.Set;

import org.jalgo.main.util.Sets;
import org.jalgo.main.util.Stack;

/**
 * an object of this class is able to parse an EBNF term (the right side of an
 * EBNF rule)
 * 
 * @author Stephan Creutz
 */
public class EbnfTermParser extends Parser implements IEbnfTokenConstants {

	private Stack<StackElement> parseStack;

	private EbnfDefinition definition;

	private String variable;

	private Set terminals, variables;

	/*
	 * SYMBOL becomes a SYMBOL in a parsetree, thus its the same as the constant
	 * defined in IEbnfTokenConstants
	 */
	private final Integer ALTERNATIVE = new Integer(102);

	private final Integer OPTION = new Integer(103);

	private final Integer PRECEDENCE = new Integer(104);

	private final Integer REPETITION = new Integer(105);

	private final Integer CONCATENATION = new Integer(106);

	/**
	 * Contructor for the EBNF-Parser object and takes the following parameters
	 * for an EBNF rule system
	 * 
	 * @param term
	 *                   a <code>String</code> containing an EBNF term
	 * @param definition
	 *                   an EBNF <code>Definition</code> object
	 * @param variable
	 *                   the current variable
	 * @param variables
	 *                   Set of Strings representing the EBNF SynVariables
	 * @param terminals
	 *                   Set of String representing the EBNF Terminals
	 */
	public EbnfTermParser(String term, EbnfDefinition definition, String variable, Set<String> variables,
			Set<String> terminals) {
		tokenizer = new EbnfTokenizer(term, Sets.union(variables, terminals));
		parseStack = new Stack<StackElement>();
		this.definition = definition;
		this.variable = variable;
		this.terminals = terminals;
		this.variables = variables;
	}

	/**
	 * analyse an EBNF term
	 * 
	 * @return an <code>EbnfElement</code> object
	 */
	public EbnfElement analyse() throws EbnfParseException {
		tokenizer.reset();
		if (!tokenizer.hasNextToken()) {
			throw new EbnfParseException(errorMsg(Messages.getString("EbnfTermParser.EbnfParseException_1_1"))); //$NON-NLS-1$
		}
		begin(0);
		tokenizer.hasNextToken();
		if (tokenizer.hasNextToken()) {
			throw new EbnfParseException(errorMsg(Messages.getString("EbnfTermParser.EbnfParseException_2_2") //$NON-NLS-1$
					+ tokenizer.getNextToken().getTokenValue()));
		}
		EbnfElement result = buildEbnfTree(0);
		if (result == null) {
			throw new EbnfParseException(errorMsg(Messages.getString("EbnfTermParser.EbnfParseException_3_3"))); //$NON-NLS-1$
		}
		return result;
	}

	private String errorMsg(String msg) {
		return Messages.getString("EbnfTermParser.EbnfParseException_4_4") + variable + "\") " + msg; //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void begin(int level) throws EbnfParseException {
		Token token = null;
		Integer tName = new Integer(0);
		while (tokenizer.hasNextToken()) {
			token = tokenizer.lookahead(0);
			tName = token.getTokenName();
			if (tName.equals(SYMBOL)) {
				tokenizer.consume();
				parseStack.push(new StackElement(SYMBOL, token.getTokenValue(), level));
				concatenation(level);
				return;
			} else if (tName.equals(LEFT_CURLY_BRACKET)) {
				tokenizer.consume();
				begin(level + 1);
				concatenation(level + 1);
				if (!tokenizer.hasNextToken()) {
					throw new EbnfParseException(errorMsg(Messages
							.getString("EbnfTermParser.EbnfParseException_5_5"))); //$NON-NLS-1$
				}
				token = tokenizer.lookahead(0);
				tName = token.getTokenName();
				if (tName.equals(RIGHT_CURLY_BRACKET)) {
					tokenizer.consume();
					parseStack.push(new StackElement(REPETITION, token.getTokenValue(), level));
					concatenation(level);
					return;
				}
				throw new EbnfParseException(errorMsg(Messages
						.getString("EbnfTermParser.EbnfParseException_6_6") //$NON-NLS-1$
						+ token.getTokenValue()), token.getPosition());
			} else if (tName.equals(LEFT_SQUARED_BRACKET)) {
				tokenizer.consume();
				begin(level + 1);
				concatenation(level + 1);
				if (!tokenizer.hasNextToken()) {
					throw new EbnfParseException(errorMsg(Messages
							.getString("EbnfTermParser.EbnfParseException_7_7"))); //$NON-NLS-1$
				}
				token = tokenizer.lookahead(0);
				tName = token.getTokenName();
				if (tName.equals(RIGHT_SQUARED_BRACKET)) {
					tokenizer.consume();
					parseStack.push(new StackElement(OPTION, token.getTokenValue(), level));
					concatenation(level);
					return;
				}
				throw new EbnfParseException(errorMsg(Messages
						.getString("EbnfTermParser.EbnfParseException_8_8") + token.getTokenValue()), //$NON-NLS-1$
						token.getPosition());
			} else if (tName.equals(LEFT_BRACKET)) {
				tokenizer.consume();
				begin(level + 1);
				concatenation(level + 1);
				if (!tokenizer.hasNextToken()) {
					throw new EbnfParseException(errorMsg(Messages
							.getString("EbnfTermParser.EbnfParseException_9_9"))); //$NON-NLS-1$
				}
				token = tokenizer.lookahead(0);
				tName = token.getTokenName();
				if (tName.equals(PIPE)) {
					tokenizer.consume();
					parseStack.push(new StackElement(PIPE, token.getTokenValue(), level));
					begin(level + 1);
					concatenation(level + 1);
					if (!tokenizer.hasNextToken()) {
						throw new EbnfParseException(errorMsg(Messages
								.getString("EbnfTermParser.EbnfParseException_10_10"))); //$NON-NLS-1$
					}
					token = tokenizer.lookahead(0);
					tName = token.getTokenName();
					if (tName.equals(RIGHT_BRACKET)) {
						tokenizer.consume();
						parseStack.push(new StackElement(ALTERNATIVE, token.getTokenValue(), level));
						concatenation(level);
						return;
					}
					throw new EbnfParseException(errorMsg(Messages
							.getString("EbnfTermParser.EbnfParseException_11_11") //$NON-NLS-1$
							+ token.getTokenValue()), token.getPosition());
				} else if (tName.equals(RIGHT_BRACKET)) {
					tokenizer.consume();
					parseStack.push(new StackElement(PRECEDENCE, token.getTokenValue(), level));
					concatenation(level);
					return;
				} else {
					throw new EbnfParseException(errorMsg(Messages
							.getString("EbnfTermParser.EbnfParseException_12_12") //$NON-NLS-1$
							+ token.getTokenValue()), token.getPosition());
				}
			} else if (tName.equals(DOT) && !tokenizer.hasLookahead(1)) {
				tokenizer.consume();
			} else {
				throw new EbnfParseException(errorMsg(Messages
						.getString("EbnfTermParser.EbnfParseException_13_13") + token.getTokenValue())); //$NON-NLS-1$
			}
		}
	}

	private void concatenation(int level) throws EbnfParseException {
		Token token = null;
		Integer tName = new Integer(0);
		if (tokenizer.hasNextToken()) {
			token = tokenizer.lookahead(0);
			tName = token.getTokenName();
			if (tName.equals(SYMBOL)) {
				tokenizer.consume();
				parseStack.push(new StackElement(SYMBOL, token.getTokenValue(), level));
				parseStack.push(new StackElement(CONCATENATION, null, level));
				concatenation(level);
				return;
			} else if (tName.equals(LEFT_CURLY_BRACKET)) {
				tokenizer.consume();
				begin(level + 1);
				concatenation(level);
				if (!tokenizer.hasNextToken()) {
					throw new EbnfParseException(errorMsg(Messages
							.getString("EbnfTermParser.EbnfParseException_14_14"))); //$NON-NLS-1$
				}
				token = tokenizer.lookahead(0);
				tName = token.getTokenName();
				if (tName.equals(RIGHT_CURLY_BRACKET)) {
					tokenizer.consume();
					parseStack.push(new StackElement(REPETITION, token.getTokenValue(), level));
					parseStack.push(new StackElement(CONCATENATION, null, level));
					concatenation(level);
					return;
				}
				throw new EbnfParseException(errorMsg(Messages
						.getString("EbnfTermParser.EbnfParseException_15_15") //$NON-NLS-1$
						+ token.getTokenValue()), token.getPosition());
			} else if (tName.equals(LEFT_SQUARED_BRACKET)) {
				tokenizer.consume();
				begin(level + 1);
				concatenation(level);
				if (!tokenizer.hasNextToken()) {
					throw new EbnfParseException(errorMsg(Messages
							.getString("EbnfTermParser.EbnfParseException_16_16"))); //$NON-NLS-1$
				}
				token = tokenizer.lookahead(0);
				tName = token.getTokenName();
				if (tName.equals(RIGHT_SQUARED_BRACKET)) {
					tokenizer.consume();
					parseStack.push(new StackElement(OPTION, token.getTokenValue(), level));
					parseStack.push(new StackElement(CONCATENATION, null, level));
					concatenation(level);
					return;
				}
				throw new EbnfParseException(errorMsg(Messages
						.getString("EbnfTermParser.EbnfParseException_17_17") + token.getTokenValue()), //$NON-NLS-1$
						token.getPosition());
			} else if (tName.equals(LEFT_BRACKET)) {
				tokenizer.consume();
				begin(level + 1);
				concatenation(level);
				if (!tokenizer.hasNextToken()) {
					throw new EbnfParseException(errorMsg(Messages
							.getString("EbnfTermParser.EbnfParseException_18_18"))); //$NON-NLS-1$
				}
				token = tokenizer.lookahead(0);
				tName = token.getTokenName();
				if (tName.equals(PIPE)) {
					tokenizer.consume();
					parseStack.push(new StackElement(PIPE, token.getTokenValue(), level));
					begin(level + 1);
					concatenation(level);
					if (!tokenizer.hasNextToken()) {
						throw new EbnfParseException(errorMsg(Messages
								.getString("EbnfTermParser.EbnfParseException_19_19"))); //$NON-NLS-1$
					}
					token = tokenizer.lookahead(0);
					tName = token.getTokenName();
					if (tName.equals(RIGHT_BRACKET)) {
						tokenizer.consume();
						parseStack.push(new StackElement(ALTERNATIVE, token.getTokenValue(), level));
						parseStack.push(new StackElement(CONCATENATION, null, level));
						concatenation(level);
						return;
					}
					throw new EbnfParseException(errorMsg(Messages
							.getString("EbnfTermParser.EbnfParseException_20_20") //$NON-NLS-1$
							+ token.getTokenValue()), token.getPosition());
				} else if (tName.equals(RIGHT_BRACKET)) {
					tokenizer.consume();
					parseStack.push(new StackElement(PRECEDENCE, token.getTokenValue(), level));
					parseStack.push(new StackElement(CONCATENATION, null, level));
					concatenation(level);
					return;
				} else {
					throw new EbnfParseException(errorMsg(Messages
							.getString("EbnfTermParser.EbnfParseException_21_21") //$NON-NLS-1$
							+ token.getTokenValue()), token.getPosition());
				}
			} else if (tName.equals(RIGHT_CURLY_BRACKET) || tName.equals(RIGHT_SQUARED_BRACKET)
					|| tName.equals(RIGHT_BRACKET) || tName.equals(PIPE)) {
				return;
			}
		}
	}

	private EbnfElement buildEbnfTree(int level) throws EbnfParseException {
		while (!parseStack.isEmpty()) {
			StackElement elem = parseStack.peak();
			if (elem.getType().equals(CONCATENATION) && elem.getLevel() == level) {
				parseStack.pop();
				EbnfConcatenation c = new EbnfConcatenation();
				while (!parseStack.isEmpty() && parseStack.peak().getLevel() == level) {
					if (parseStack.peak().getType().equals(CONCATENATION)) {
						parseStack.pop();
						continue;
					}
					c.addElem(0, buildEbnfTree(level));
				}
				return c;
			} else if (elem.getType().equals(ALTERNATIVE) && elem.getLevel() == level) {
				parseStack.pop();
				EbnfAlternative e = new EbnfAlternative();
				e.setRight(buildEbnfTree(level + 1));
				parseStack.pop(); // pop PIPE parsesymbol
				e.setLeft(buildEbnfTree(level + 1));
				return e;
			} else if (elem.getType().equals(OPTION) && elem.getLevel() == level) {
				parseStack.pop();
				return new EbnfOption(buildEbnfTree(level + 1));
			} else if (elem.getType().equals(PRECEDENCE) && elem.getLevel() == level) {
				parseStack.pop();
				return new EbnfPrecedence(buildEbnfTree(level + 1));
			} else if (elem.getType().equals(REPETITION) && elem.getLevel() == level) {
				parseStack.pop();
				return new EbnfRepetition(buildEbnfTree(level + 1));
			} else if (elem.getType().equals(SYMBOL) && elem.getLevel() == level) {
				parseStack.pop();
				if (terminals.contains(elem.getValue())) {
					return Sets.search(definition.getAlphabet(), new EbnfTerminal(elem.getValue()));
				} else if (variables.contains(elem.getValue())) {
					return Sets.search(definition.getVariables(), new EbnfSynVariable(elem.getValue()));
				} else {
					throw new EbnfParseException(Messages
							.getString("EbnfTermParser.EbnfParseException_22_22") //$NON-NLS-1$
							+ elem.getValue() + "\")"); //$NON-NLS-1$
				}
			} else {
				return null;
			}
		}
		return null;
	}

	private class StackElement {
		private Integer type;

		private String value;

		private int level;

		public StackElement(Integer type, String value, int level) {
			this.type = type;
			this.value = value;
			this.level = level;
		}

		public Integer getType() {
			return this.type;
		}

		public String getValue() {
			return this.value;
		}

		public int getLevel() {
			return level;
		}
	}

}