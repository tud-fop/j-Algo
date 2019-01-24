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
 * <p>An abstract class representing an EBNF term.</p>
 * 
 * @author Johannes Mey
 */

public abstract class Term implements Serializable{

	/**
	 * Checks if, when the term is an alternative, it contains exactly two
	 * terms.
	 * 
	 * @return true
	 */
	public abstract boolean isStrict();
	
	
	/**
	 * This method is used to create a copy of the current ebnf definition with
	 * only binary alternatives.
	 * 
	 * @return a term with only binary alternatives.
	 * @throws DefinitionFormatException 
	 */
	public abstract Term getStrict() throws DefinitionFormatException;
	
	/**
	 * Checks if the term <code>term</code> is contained in the term
	 * 
	 * @param term the term to be searched for
	 * @return true if the term contains the term as a sub-term or is the term, 
	 * false if it does not contain the term.
	 */
	public abstract boolean contains(Term term);
	
	
	/**
	 * Returns the contained subterms.
	 * 
	 * @return the contained subterms
	 */
	public abstract java.util.List<Term> getTerms();
	
	
	/**
	 * This is used in the <code>addRule</code> function of the Definition to 
	 * insure that all symbols in a rule are included in the definition
	 * @return a list of all symbols in the rule.
	 */
	java.util.List<EVariable> getSymbols () {
		java.util.List<EVariable> variablesList 
										= new java.util.ArrayList<EVariable>();
		for(Term subterm:getTerms())
			variablesList.addAll(subterm.getSymbols());
		
		return variablesList;
		
	}
	
/**
 * <p>This method is for testing purposes only.
 * It should output the term, but there is no distinction
 * between meta symbols and other symbols (i.e. no ^).</p>
 * 
 * 
 * @return a String representation of the EBNF-term
 */
    public abstract String toString();
 }
