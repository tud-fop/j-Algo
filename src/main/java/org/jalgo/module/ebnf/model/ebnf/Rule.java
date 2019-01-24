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

/**
 * Represents a rule of an EBNF definition, a rule always contains a valid name
 * (the variable on the left side) and a valid term (right side)
 */
public class Rule implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The name of a rule is the name of the variable of the left side
	 */
	private EVariable name;

	/**
	 * The term is the right side of a rule
	 */
	private Term term;

	public Rule(EVariable name, Term term) throws DefinitionFormatException {
		if (name == null || term == null)
			throw new DefinitionFormatException(
					"could not add variable or term because it was null");
		this.name = name;
		this.term = term;
	}

	/**
	 * Checks, if every Alternative in the Term has exactly two sub-terms
	 * 
	 * @return true if every alternative has two sub-terms
	 */
	public boolean isStrict() {
		return term.isStrict();
	}

	/**
	 * 
	 * @return a version of the rule where the term contains binary alternatives
	 *         only
	 * @throws DefinitionFormatException
	 *             if for some reason the strict version could not be created
	 */
	public Rule getStrict() throws DefinitionFormatException {
		Rule strictRule = new Rule(name, term.getStrict());

		return strictRule;
	}

	/**
	 * @see org.jalgo.module.ebnf.model.ebnf.Term#toString()
	 */
	@Override
	public String toString() {

		return name.toString() + " ::= " + term.toString();
	}

	/**
	 * Returns the name of the Rule, i.e. the name of the Variable on the left
	 * side
	 * 
	 * @return
	 */
	public EVariable getName() {
		return name;
	}

	/**
	 * Sets the name of the rule.
	 * 
	 * @param name
	 *            must not be null
	 * @throws DefinitionFormatException if the name is a null object
	 */
	public void setName(EVariable name) throws DefinitionFormatException {
		if (name == null)
			throw new DefinitionFormatException();
		this.name = name;
	}

	/**
	 * @return the left side of the rule
	 */
	public Term getTerm() {
		return term;
	}

	/**
	 * Sets the term of the rule to <code>term</code>
	 * @param term
	 *            The term to set.
	 * @throws DefinitionFormatException
	 */
	public void setTerm(Term term) throws DefinitionFormatException {

		if (term != null)
			this.term = term;
		else
			throw new DefinitionFormatException();
	}

	/**
	 * Checks if the rule contains a certain term
	 * @param term to be searched for
	 * @return true if the rule contains the term
	 */
	public boolean contains (Term term) {
		if (term.equals(name)) return true;
		else return this.term.contains(term);
	}
	
}
