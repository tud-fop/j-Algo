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

/**
 * This abstract class holds the functionality for EOption, ECompoundTerm, and
 * ERepetition.
 * 
 */
public abstract class ETermContainer extends Term {

	/**
	 * <p>
	 * The term inside the brackets
	 * </p>
	 * 
	 */
	protected Term term;

	/**
	 * @return Returns the term.
	 */
	public Term getTerm() {
		return term;
	}

	/**
	 * Sets the sub-term to <code>term</code>.
	 * @param term
	 *            The term to set.
	 * @throws DefinitionFormatException if the term is null or contains the
	 * container term itself
	 */
	public void setTerm(Term term) throws DefinitionFormatException {

		if (term == null || term.contains(this))
			throw new DefinitionFormatException();
		else
			this.term = term;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.ebnf.model.ebnf.Term#isStrict()
	 */
	@Override
	public boolean isStrict() {
		return term.isStrict();
	}

	/**
	 * @return a list including the sub-term as the only element
	 */
	@Override
	public java.util.List<Term> getTerms() {
		java.util.List<Term> returnList = new java.util.ArrayList<Term>();
		returnList.add(term);
		return returnList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.ebnf.model.ebnf.Term#contains(org.jalgo.module.ebnf.model.ebnf.ESymbol)
	 */
	@Override
	public boolean contains(Term term) {
		// if this is the term or the sub-term is the term return true
		if (this.equals(term) || this.term.equals(term))
			return true;
		// otherwise test if the sub-term contains the term
		return this.term.contains(term);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.ebnf.model.ebnf.Term#toString()
	 */
	@Override
	public String toString() {

		return "[" + term.toString() + "]";
	}

}
