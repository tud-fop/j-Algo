/*
 * Created on 10.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.ebnf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jalgo.main.util.Sets;

/**
 * @author Stephan Creutz
 */
public class EbnfParser extends Parser {
	private String ebnfName, variables, alphabet, startVariable;
	private HashMap rules;
	private EbnfDefinition definition;
	private Set metaSymbols;

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

		metaSymbols = new HashSet();
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
	 * @throws Exception if something goes wrong
	 */
	public EbnfDefinition analyse() throws EbnfParseException {
		InputParser variableSetParser = new InputParser(variables);
		InputParser terminalSetParser = new InputParser(alphabet);
		Set variableSet = null;
		Set terminalSet = null;
		variableSet = variableSetParser.analyse();
		try {
			terminalSet = terminalSetParser.analyse();
		} catch (Exception e) {
			throw new EbnfParseException(e.toString());
		}
		if (!Sets.disjoint(variableSet, terminalSet)) {
			throw new EbnfParseException(Messages.getString("EbnfParser.EbnfParseException_1_9")); //$NON-NLS-1$
		}
		if (!Sets.disjoint(variableSet, metaSymbols)) {
			throw new EbnfParseException(Messages.getString("EbnfParser.EbnfParseException_2_10")); //$NON-NLS-1$
		}
		if (!Sets.disjoint(terminalSet, metaSymbols)) {
			throw new EbnfParseException(Messages.getString("EbnfParser.EbnfParseException_3_11")); //$NON-NLS-1$
		}
		if (startVariable.equals("")) { //$NON-NLS-1$
			throw new EbnfParseException(Messages.getString("EbnfParser.EbnfParseException_4_12")); //$NON-NLS-1$
		}
		if (rules.keySet().contains("")) { //$NON-NLS-1$
			throw new EbnfParseException(Messages.getString("EbnfParser.EbnfParseException_5_13")); //$NON-NLS-1$
		}
		if (!variableSet.equals(rules.keySet())) {
			throw new EbnfParseException(Messages.getString("EbnfParser.EbnfParseException_6_14")); //$NON-NLS-1$
		}
		if (!variableSet.contains(startVariable)) {
			throw new EbnfParseException(Messages.getString("EbnfParser.EbnfParseException_7_15")); //$NON-NLS-1$
		}

		for (Iterator it = variableSet.iterator(); it.hasNext();) {
			String key = (String) it.next();
			EbnfSynVariable syn = new EbnfSynVariable(key);
			definition.addVariable(syn);
			if (key.equals(startVariable)) {
				definition.setStartVariable(syn);
			}
		}

		for (Iterator it = terminalSet.iterator(); it.hasNext();) {
			definition.addTerminal(new EbnfTerminal((String) it.next()));
		}

		for (Iterator it = definition.getVariables().iterator();
			it.hasNext();
			) {
			EbnfSynVariable syn = (EbnfSynVariable) it.next();
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
	/**
	 * @return
	 */
	public String getAlphabet() {
		return alphabet;
	}

	/**
	 * @return
	 */
	public EbnfDefinition getDefinition() {
		return definition;
	}

	/**
	 * @return
	 */
	public String getEbnfName() {
		return ebnfName;
	}

	/**
	 * @return
	 */
	public HashMap getRules() {
		return rules;
	}

	/**
	 * @return
	 */
	public String getStartVariable() {
		return startVariable;
	}

	/**
	 * @return
	 */
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
