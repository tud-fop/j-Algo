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
 * Created on 10.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.ebnf;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jalgo.main.util.Messages;

/**
 * @author Stephan Creutz
 */
public class EbnfParser extends Parser {
	private String ebnfName, variables, alphabet, startVariable;
	private HashMap rules;
	private EbnfDefinition definition;
	private Set<String> metaSymbols;

	/**
	 * Contructor for the EBNF-Parser object and takes the following
	 * parameters for an EBNF rule system
	 * 
	 * @param ebnfName the name
	 * @param vars the set of variables
	 * @param alphabet the set of terminals
	 * @param startvar the start variable
	 * @param rules the rules itsself
	 */
	public EbnfParser(
		String ebnfName,
		String vars,
		String alphabet,
		String startvar,
		HashMap rules) {
		this.ebnfName = ebnfName;
		this.variables = vars;
		this.alphabet = alphabet;
		this.startVariable = startvar;
		this.rules = rules;
		this.definition = new EbnfDefinition(ebnfName);

		metaSymbols = new HashSet<String>();
		metaSymbols.add("\\^("); //$NON-NLS-1$
		metaSymbols.add("\\^)"); //$NON-NLS-1$
		metaSymbols.add("\\^["); //$NON-NLS-1$
		metaSymbols.add("\\^]"); //$NON-NLS-1$
		metaSymbols.add("\\^{"); //$NON-NLS-1$
		metaSymbols.add("\\^}"); //$NON-NLS-1$
		metaSymbols.add("\\^|"); //$NON-NLS-1$
		metaSymbols.add("."); //$NON-NLS-1$
	}

	/**
	 * analyse an EBNF rule system
	 * 
	 * @return a EbnfDefinition object
	 * @throws EbnfParseException if something goes wrong
	 */
	public EbnfDefinition analyse() throws EbnfParseException {
		InputParser variableSetParser = new InputParser(variables);
		InputParser terminalSetParser = new InputParser(alphabet);
		Set<String> variableSet = null;
		Set<String> terminalSet = null;
		variableSet = variableSetParser.analyse();
		terminalSet = terminalSetParser.analyse();
		if (!Collections.disjoint(variableSet, terminalSet)) {
			throw new EbnfParseException(Messages.getString("synDiaEBNF",
				"EbnfParser.EbnfParseException_1_9")); //$NON-NLS-1$
		}
		if (!Collections.disjoint(variableSet, metaSymbols)) {
			throw new EbnfParseException(Messages.getString("synDiaEBNF",
				"EbnfParser.EbnfParseException_2_10")); //$NON-NLS-1$
		}
		if (!Collections.disjoint(terminalSet, metaSymbols)) {
			throw new EbnfParseException(Messages.getString("synDiaEBNF",
				"EbnfParser.EbnfParseException_3_11")); //$NON-NLS-1$
		}
		if (startVariable.equals("")) { //$NON-NLS-1$
			throw new EbnfParseException(Messages.getString("synDiaEBNF",
				"EbnfParser.EbnfParseException_4_12")); //$NON-NLS-1$
		}
		if (rules.keySet().contains("")) { //$NON-NLS-1$
			throw new EbnfParseException(Messages.getString("synDiaEBNF",
				"EbnfParser.EbnfParseException_5_13")); //$NON-NLS-1$
		}
		if (!variableSet.equals(rules.keySet())) {
			throw new EbnfParseException(Messages.getString("synDiaEBNF",
				"EbnfParser.EbnfParseException_6_14")); //$NON-NLS-1$
		}
		if (!variableSet.contains(startVariable)) {
			throw new EbnfParseException(Messages.getString("synDiaEBNF",
				"EbnfParser.EbnfParseException_7_15")); //$NON-NLS-1$
		}

		for (String key : variableSet) {
			EbnfSynVariable syn = new EbnfSynVariable(key);
			definition.addVariable(syn);
			if (key.equals(startVariable)) {
				definition.setStartVariable(syn);
			}
		}

		for (String key : terminalSet) {
			definition.addTerminal(new EbnfTerminal(key));
		}

		for (EbnfSynVariable syn : definition.getVariables()) {
			String key = syn.getLabel();
			EbnfTermParser termParser =
				new EbnfTermParser(
					(String) rules.get(key),
					definition,
					key,
					variableSet,
					terminalSet);
			syn.setStartElem(termParser.analyse());
		}
		return definition;
	}

	public String getAlphabet() {
		return alphabet;
	}

	public EbnfDefinition getDefinition() {
		return definition;
	}

	public String getEbnfName() {
		return ebnfName;
	}

	public HashMap getRules() {
		return rules;
	}

	public String getStartVariable() {
		return startVariable;
	}

	public String getVariables() {
		return variables;
	}

	/**
	 * @param string
	 */
	public void setAlphabet(String string) {
		alphabet = string;
	}

	/**
	 * @param definition
	 */
	public void setDefinition(EbnfDefinition definition) {
		this.definition = definition;
	}

	/**
	 * @param string
	 */
	public void setEbnfName(String string) {
		ebnfName = string;
	}

	/**
	 * @param map
	 */
	public void setRules(HashMap map) {
		rules = map;
	}

	/**
	 * @param string
	 */
	public void setStartVariable(String string) {
		startVariable = string;
	}

	/**
	 * @param string
	 */
	public void setVariables(String string) {
		variables = string;
	}
}
