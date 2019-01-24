/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.jalgo.module.ebnf.model.ebnf;

import java.io.Serializable;

import org.jalgo.main.util.Messages;

/**
 * This is the interface to the EBNF definnition.
 * @author johannes mey
 */
public class Definition extends java.util.Observable implements Serializable{
	
	// -------------------------------------------------------------------------
	// private attributes ------------------------------------------------------
	// -------------------------------------------------------------------------
	
	/**
	 * This is needed for restoring a saved definition
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the list of terminal symbols in the definition. it must not contain null
	 * elements and no two elements must have the same name.
	 */
	private java.util.List<ETerminalSymbol> terminals 
								= new java.util.ArrayList<ETerminalSymbol>();

	/**
	 * the list of variable symbols. it must not contain null elements and no
	 * two elements must have the same name.
	 */
	private java.util.List<EVariable> variables 
								= new java.util.ArrayList<EVariable>();

	/**
	 * the list of rules. it must not contain null elements and there is only
	 * one rule per variable symbol allowed.
	 * 
	 */
	private java.util.List<Rule> rules = new java.util.ArrayList<Rule>();

	/**
	 * the startVariable must be an element of the variables list. It can be be
	 * null only when there are no variables in the definition.
	 */
	private EVariable startVariable;
	
	// -------------------------------------------------------------------------
	// public methods ----------------------------------------------------------
	// -------------------------------------------------------------------------
	// 1. definition property methods ------------------------------------------
	// -------------------------------------------------------------------------
	
	/**
	 * tests if all rules of the definition are strict according to the
	 * definition of ebnf terms, i.e. if all alternatives are binary
	 * 
	 * @return true if all alternatives are binary, otherwise false
	 */
	public boolean isStrict() {

		// if a rule is not strict, the definition is not strict and false is
		// returned
		for (Rule rule : rules)
			if(!rule.isStrict()) return false;

		// otherwise true is returned
		return true;
	}

	/**
	 * Checks if the symbol is valid, i.e. its name is not the beginning of
	 * another name and it is not an element of a symbols list yet.
	 * 
	 * @param symbol
	 *            the symbol to test
	 * @return if the symbol is valid
	 */
	private boolean isValidSymbolName(String symbol) {
		
		// return false if any name of a symbol starts with the name of the
		// new symbol
		for (ESymbol listSymbol : terminals) 
			if ((listSymbol.getName().startsWith(symbol)) || (symbol.startsWith(listSymbol.getName())))
				return false;
		
			
	
		for (ESymbol listSymbol : variables)
			if ((listSymbol.getName().startsWith(symbol)) || (symbol.startsWith(listSymbol.getName())))
				return false;
		
		// otherwise return true
		return true;

	}

	/**
	 * Checks if every variable has a corresponding rule
	 * 
	 * @return true if the definition is complete
	 */
	public boolean isComplete() {
		for(EVariable var:variables) {
			if (!rules.contains(getRule(var))) return false;
		}
		return true;
	}
	
	// -------------------------------------------------------------------------
	// public methods ----------------------------------------------------------
	// -------------------------------------------------------------------------
	// 2. getters --------------------------------------------------------------
	// -------------------------------------------------------------------------
	
	/**
	 * Returns a strict Definition according to the definition of EBNF terms
	 * which fulfills the following conventions:
	 * <ul>
	 * <li> all Alternatives are binary </li>
	 * </ul>
	 * 
	 * @return a strict Definition equivalent to the given Definition
	 * @throws DefinitionFormatException 
	 */
	public Definition getStrict() throws DefinitionFormatException {
		
		// this should NEVER throw an Exception, so we should consider to ignore the fact that it can!
		// but that is not too important...
		
		Definition strictDefinition = new Definition();
		
		// add the existing terminal symbols.
		for(ETerminalSymbol terminal:terminals)
			strictDefinition.addTerminal(terminal);
		
		// add the existing variable symbols
		for(EVariable variable:variables)
			strictDefinition.addVariable(variable);
		
		// add the new, strict rules
		for(Rule rule:rules) {
			strictDefinition.addRule(rule.getStrict());
		}
		
		// Set the new start variable
		strictDefinition.setStartVariable(this.startVariable);
		
		return strictDefinition;
	}

	/**
	 * @param name is the name of the Symbol as a String
	 * @return the Symbol if it exists, otherwise null
	 */
	public ETerminalSymbol getTerminalSymbol(String name) {
		for(ETerminalSymbol symbol:terminals)
			if(symbol.getName().equals(name)) return symbol;
		return null;
	}
	
	/**
	 * @param name is the name of the Symbol as a String
	 * @return the Symbol if it exists, otherwise null
	 */
	public EVariable getVariable(String name) {
		for(EVariable symbol:variables)
			if(symbol.getName().equals(name)) return symbol;
		return null;
	}
	
	@Override
	public String toString() {
		String returnString = "E = (V,\u03a3,";
		if (startVariable == null)
			returnString += "<null>,R)\nV = {";
		else
			returnString += startVariable.toString() + ",R)\nV = {";

		for (int i = 0; i < variables.size(); i++) {
			returnString += variables.get(i).toString();
			if (i != variables.size()-1)
				returnString += ",";
		}

		returnString += "}\n\u03a3 = {";

		for (int i = 0; i < terminals.size(); i++) {
			returnString += terminals.get(i).toString();
			if (i != terminals.size()-1)
				returnString += ",";
		}

		returnString += "}\n\nR:\n";

		for (Rule rule : rules) {
			returnString += rule.toString() + "\n";
		}

		return returnString;
	}
	
	/**
	 * @return a list containing all Terminals
	 */
	public java.util.List<ETerminalSymbol> getTerminals() {
		return new java.util.ArrayList<ETerminalSymbol>(terminals);
	}

	/**
	 * @return a list containing all Variables
	 */
	public java.util.List<EVariable> getVariables() {
		return new java.util.ArrayList<EVariable>(variables);
	}

	/**
	 * @return Returns the startVariable.
	 */
	public EVariable getStartVariable() {
		return startVariable;
	}
	
	/**
	 * @return returns the rules.
	 */
	public java.util.List<Rule> getRules() {
		return new java.util.ArrayList<Rule>(rules);
	}
	
	/**
	 * Returns the rule with the given name
	 * @param name the name of the rule, i.e. the left side variable
	 * @return the rule, if it exists, otherwise null
	 */
	public Rule getRule(EVariable name) {
		for(Rule rule:rules)
			if(rule.getName().equals(name)) return rule;
		return null;
	}
	
	// -------------------------------------------------------------------------
	// public methods ----------------------------------------------------------
	// -------------------------------------------------------------------------
	// 3. setters --------------------------------------------------------------
	// -------------------------------------------------------------------------
	// there should be a call of setChanged() and notifyOberserves()
	// in every method
	// -------------------------------------------------------------------------
	
	
	/**
	 * Sets the start variable of the definition to var if the set of variables
	 * contains var.
	 * 
	 * @param var
	 *            the new start variable
	 * @throws DefinitionFormatException if the variable is not contained in the definition
	 */
	public void setStartVariable(EVariable var) throws DefinitionFormatException {
		
		if (var == null) {
			startVariable = null;
		}
		else if (!variables.contains(var)) {
			throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.StartVarNotInDef"));
		}
		else {
			startVariable = var;
			
			// OBSERVER-PATTERN begin
			this.setChanged();
			this.notifyObservers();
			// OBSERVER-PATTERN end
		}
			
	}

	
	// add/remove functions
	
	/**
	 * Adds a new terminal symbol. if any symbol with the given name already
	 * exists false is returned and the element is not added.
	 * 
	 * @param symbol
	 *            is the symbol to be added
	 * @throws DefinitionFormatException 
	 */
	public void addTerminal(ETerminalSymbol symbol) throws DefinitionFormatException {

		if (isValidSymbolName(symbol.getName())) {
			if(terminals.add(symbol)) {
			// OBSERVER-PATTERN begin
			this.setChanged();
			this.notifyObservers();
			// OBSERVER-PATTERN end
		}
		else throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Undefined"));
		} else
			throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " '" + symbol.getName() + "' " + Messages.getString("ebnf", "Ebnf.Error.InvalidNameInDef"));
	}

	/**
	 * Adds a new variable symbol. if any symbol with the given name already
	 * exists false is returned and the element is not added.
	 * @param symbol is the symbol to be added
	 * @throws DefinitionFormatException 
	 */
	public void addVariable(EVariable symbol) throws DefinitionFormatException {

		if (isValidSymbolName(symbol.getName())) {
			if(variables.add(symbol)){
				// // Auto-Set-StartVar
				// if(getStartVariable()==null) setStartVariable(symbol);
				// else {
				// OBSERVER-PATTERN begin
				this.setChanged();
				this.notifyObservers();
				// OBSERVER-PATTERN end
				// }
			}
			else throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Undefined"));
			} else
				throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " '" + symbol.getName() + "' " + Messages.getString("ebnf", "Ebnf.Error.InvalidNameInDef"));
	}

	/**
	 * a terminal can only be removed if there are no rules containing it.
	 * @param symbol
	 *            the symbol to be removed
	 * @throws DefinitionFormatException 
	 */
	public void removeTerminal(ETerminalSymbol symbol) throws DefinitionFormatException {

		if (terminals.contains(symbol)) {
			for (Rule rule:rules) {
				if (rule.contains(symbol)) throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " '" + symbol.getName() + "' " + Messages.getString("ebnf", "Ebnf.Error.SymbolOccoursInRule"));
			}
			if(terminals.remove(symbol)){
			// OBSERVER-PATTERN begin
			this.setChanged();
			this.notifyObservers();
			// OBSERVER-PATTERN end
		}
			else throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Undefined"));
		}	
		else throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " '" + symbol.getName() + "' " + Messages.getString("ebnf", "Ebnf.Error.NotATerminalSymbol"));
	}
	
	/**
	 * a variable can only be removed if there are no rules containing it.
	 * 
	 * @param symbol
	 *            the symbol to be removed
	 * @throws DefinitionFormatException 
	 */
	public void removeVariable(EVariable symbol) throws DefinitionFormatException {

		if (variables.contains(symbol)) {
			for (Rule rule:rules) {
				if (rule.contains(symbol))
					throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " '" + symbol.getName() + "' " + Messages.getString("ebnf", "Ebnf.Error.SymbolOccoursInRule"));
			}
			if (this.getStartVariable()!=null && this.getStartVariable().equals(symbol)) {
				this.startVariable = null;
				// Auto-Set-StartVar
				// if (!variables.isEmpty()) setStartVariable(variables.get(0));
				
			}
			if(variables.remove(symbol)) {
			// OBSERVER-PATTERN begin
			this.setChanged();
			this.notifyObservers();
			// OBSERVER-PATTERN end
		}
		else throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Undefined"));
	}	
	else throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " '" + symbol.getName() + "' " + Messages.getString("ebnf", "Ebnf.Error.NotAVariable"));
}

	/**
	 * Adds a new rule to the end of the list of rules. The rule must not be
	 * and the name of the rule must be a variable that is an element of the
	 * variables list
	 * 
	 * @param rule
	 * @throws DefinitionFormatException 
	 */
	public void addRule(Rule rule) throws DefinitionFormatException {
		if(rule == null)
			throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.RuleIsNull"));
		else if (!variables.contains(rule.getName()))
			throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " '" + rule.getName().getName() + "' " + Messages.getString("ebnf", "Ebnf.Error.NotAVariable"));
		else {
			// check if there is no rule for the name of the new rule yet
			for(Rule oldrule:rules) {
				if (oldrule.getName().equals(rule.getName()))
					throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " " + Messages.getString("ebnf", "Ebnf.Error.RuleAlreadyExists") + " '" + rule.getName().getName() + "'!");
			}
			
			// check if all symbols in the rule are part of the definition
			for(ESymbol symbol:rule.getTerm().getSymbols()) {
				if(symbol.getClass().equals(EVariable.class)) {
					if (!variables.contains(symbol))
						throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " '" + symbol.getName() + " '" + rule.getName().getName() + Messages.getString("ebnf", "Ebnf.Error.NotASymbol"));
				} 
				else if(symbol.getClass().equals(ETerminalSymbol.class)) {
					if (!terminals.contains(symbol))
						throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " '" + symbol.getName() + " '" + rule.getName().getName() + Messages.getString("ebnf", "Ebnf.Error.NotASymbol"));
				} 
			}
			
			rules.add(rule);
			// OBSERVER-PATTERN begin
			this.setChanged();
			this.notifyObservers();
			// OBSERVER-PATTERN end
		}
		
	}

	/**
	 * removes the rule <code>rule</code> if it is part of the defintion.
	 * @param rule the rule to be removed
	 * @throws DefinitionFormatException 
	 */
	public void removeRule(Rule rule) throws DefinitionFormatException {
		if(rules.remove(rule)) {
			// OBSERVER-PATTERN begin
			this.setChanged();
			this.notifyObservers();
			// OBSERVER-PATTERN end
		}
		else throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Undefined"));
	}


	// replace/rename functions 

	/**
	 * Renames the symbol <code>symbol</code> to <code>newName</code> if this is
	 * possible.
	 * @param symbol the symbol to be renamed
	 * @param newName the new name
	 * @throws DefinitionFormatException 
	 */
	public void renameSymbol(ESymbol symbol, String newName) throws DefinitionFormatException {

			
		if(  !(isValidSymbolName(newName) || symbol.getName().startsWith(newName)|| newName.startsWith(symbol.getName())))
				throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " '" + newName + "' " + Messages.getString("ebnf", "Ebnf.Error.InvalidSymbolName"));
		else if(symbol instanceof EVariable) {
			if(!variables.contains(symbol))	
				throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " '" + newName + "' " + Messages.getString("ebnf", "Ebnf.Error.InvalidSymbolName"));
		} else if(symbol instanceof ETerminalSymbol) {
				if(!terminals.contains(symbol))	
					throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " '" + newName + "' " + Messages.getString("ebnf", "Ebnf.Error.InvalidSymbolName"));
		} 
		
		symbol.setName(newName);
		// OBSERVER-PATTERN begin
		this.setChanged();
		this.notifyObservers();
		// OBSERVER-PATTERN end
		
	}
	
	/**
	 * Replaces the Rule oldRule by newRule if oldRule is a valid rule of the
	 * definition
	 * 
	 * @param oldRule the rule to be replaced
	 * @param newRule the replacement rule
	 * @return true if a rule could be replaced
	 * @throws DefinitionFormatException 
	 */
	public void replaceRule(Rule oldRule, Rule newRule) throws DefinitionFormatException {
		
		// first of all check if there is a rule 'oldRule'
		if (!rules.contains(oldRule))
			throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.NotARuleReplace"));
		
		// then check if the new rule is not null
		else if(newRule == null)
			throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.ReplacementRuleIsNull"));
		
		// then check if the name of the rule is a variable from the definition
		else if (!variables.contains(newRule.getName()))
			throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " " + Messages.getString("ebnf", "Ebnf.Error.Replacement") + " '" +  newRule.getName().getName() + "' " + Messages.getString("ebnf", "Ebnf.Error.NotAVariable"));
		else {
			// check if all symbols in the rule are part of the definition
			for(ESymbol symbol:newRule.getTerm().getSymbols()) {
				if(symbol.getClass().equals(EVariable.class)) {
					if (!variables.contains(symbol))
						throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " " + Messages.getString("ebnf", "Ebnf.Error.Replacement") + " '" +  symbol.getName() + " '" + newRule.getName().getName() + Messages.getString("ebnf", "Ebnf.Error.NotASymbol"));
				} 
				else if(symbol.getClass().equals(ETerminalSymbol.class)) {
					if (!terminals.contains(symbol))
						throw new DefinitionFormatException(Messages.getString("ebnf", "Ebnf.Error.Error") + " " + Messages.getString("ebnf", "Ebnf.Error.Replacement") + " '" + symbol.getName() + " '" + newRule.getName().getName() + Messages.getString("ebnf", "Ebnf.Error.NotASymbol"));
				} 
			}
			
			rules.set(rules.indexOf(oldRule),newRule);

			// OBSERVER-PATTERN begin
			this.setChanged();
			this.notifyObservers();
			// OBSERVER-PATTERN end

		}
	}

	
	
}
