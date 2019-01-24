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

import java.util.List;

import org.jalgo.main.util.Messages;

/**
 * Represents an EBNF symbol. The name must not be an empty String, null or a
 * String consisting of blanks only
 * 
 * @author Johannes Mey
 */
abstract public class ESymbol extends Term {

	/**
	 * This is the name of the symbol.
	 */
	private String name;	
	
// -----------------------------------------------------------------------------
// ESymbol methods & constructors
// -----------------------------------------------------------------------------
	

	/**
	 * Creates a new symbol with the given name.
	 * 
	 * @param name
	 *            the name of the new Symbol
	 * @throws DefinitionFormatException
	 *             if the name is invalid
	 */
	public ESymbol(String name) throws DefinitionFormatException {
		this.setName(name);
	}

	/**
	 * Renames the symbol.
	 * 
	 * @param name
	 *            the new name
	 * @throws DefinitionFormatException
	 *             if the new name is invalid
	 */
	public void setName(String name) throws DefinitionFormatException {
		
		if (name == null) {
			throw new DefinitionFormatException("No name for ESymbol given!");
		}
		else if (name.equals("") || name.trim().equals("")) {
			throw new DefinitionFormatException
					(Messages.getString("ebnf", "Ebnf.Error.Error") + " '" + name + "' " + Messages.getString("ebnf", "Ebnf.Error.InvalidSymbolName"));
		}
		
		this.name = name;
	}

	/**
	 * @return the name of the symbol
	 */
	public String getName() {
		return name;
	}

// -----------------------------------------------------------------------------
// methods inherited from Term
// -----------------------------------------------------------------------------
	
	/**
	 * Checks if the symbol is strict according to the definition of a strict
	 * term. this is always true!
	 * 
	 * @see org.jalgo.module.ebnf.model.ebnf.Term#isStrict()
	 * @return this method always returns true
	 */
	@Override
	public boolean isStrict() {
		return true;
	}

	/*
	 * @see Term#getTerms()
	 */
	@Override
	public List<Term> getTerms() {
		return new java.util.ArrayList<Term>();
	}

	/*
	 * @see Term#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/*
	 * @see Term#getStrict()
	 */
	@Override
	public Term getStrict() {
		return this;
	}

	/*
	 * @see Term#contains(ESymbol)
	 */
	@Override
	public boolean contains(Term term) {
		return this.equals(term);
	}

}
