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
 * <p>
 * Represents the alternative in an EBNF term (a|b). According to the definition
 * an alternative is binary. Since the alternative is commonly used in a not
 * binary way, the model also allows the alternative of more than two terms.
 * </p>
 * 
 */
public class EAlternative extends org.jalgo.module.ebnf.model.ebnf.Term {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * <p>
	 * Stores the terms of the alternative. It has to be ensured that this list
	 * <b>always</b> contains at least two elements.
	 * </p>
	 * 
	 */
	private java.util.List<Term> terms;

	/**
	 * <p>
	 * Creates an Aternative with at least two terms. It only adds elements that
	 * are not null.
	 * </p>
	 * 
	 * @param terms
	 *            is the list of terms to be in the alternative
	 * @throws DefinitionFormatException
	 *             if there are less than two terms in the given list that are
	 *             not null.
	 */
	public EAlternative(java.util.List<Term> terms)
			throws DefinitionFormatException {
		
		if (terms == null) throw new DefinitionFormatException();
		this.terms = new java.util.ArrayList<Term>();

		if(terms == null)
			throw new DefinitionFormatException("List of terms is null");
		
		// add all terms that are not null
		for (Term term : terms) {
			if (term != null)
				this.terms.add(term);
		}

		// check if alternative has alt least two terms, otherwise throw
		// exception
		if (this.terms.size() < 2) {
			this.terms = null;
			throw new DefinitionFormatException();
		}

	}

	/**
	 * <p>
	 * Adds the term to the end of the list of alternatives if it is not null
	 * or is not the EAlternative itself.
	 * </p>
	 * 
	 * 
	 * @param term
	 *            is the term to be added
	 * @throws DefinitionFormatException 
	 */
	public void addTerm(Term term) throws DefinitionFormatException {
		if (term == null  || term.contains(this))
			throw new DefinitionFormatException();
		else {
			terms.add(term);
		}
	}

	/**
	 * @return a copy of the list of alternatives
	 */
	@Override
	public java.util.List<Term> getTerms() {
		return new java.util.ArrayList<Term>(terms);
	}

	/**
	 * <p>
	 * Creates a binary Alternative. If at least one of the arguments is null an
	 * Exception is thrown.
	 * </p>
	 * 
	 * 
	 * @param term1
	 *            is the first alternative
	 * @param term2
	 *            is the second alternative
	 * @throws DefinitionFormatException
	 * @throws DefinitionFormatException
	 */
	public EAlternative(Term term1, Term term2)
			throws DefinitionFormatException {
		if (term1 == null || term2 == null)
			throw new DefinitionFormatException("could not add null term");

		terms = new java.util.ArrayList<Term>();
		terms.add(term1);
		terms.add(term2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.ebnf.model.ebnf.Term#contains(org.jalgo.module.ebnf.model.ebnf.ESymbol)
	 */
	@Override
	public boolean contains(Term term) {
		// if the term is the requested term return true
		if (this.equals(term)) return true;
		
		// otherwise check all the contained terms
		for (Term subterm : terms) {
			if(subterm.contains(term)) return true;
		}
		
		return false;
	}


	/**
	 * Tests if the alternative and all alternatives in the contained term
	 * contain exactly two terms
	 * 
	 * @return true if all alternatives contain two terms, otherwise false
	 */
	public boolean isStrict() {
		if (terms.size() != 2)
			return false;

		boolean isStrict = true;
		for (Term term : terms)
			isStrict = isStrict && term.isStrict();

		return isStrict;
	}

	/**
	 * This is the only implementation of getStrict that actually does something
	 * useful: it transforms the list of alternative terms into binary
	 * alternatives.
	 * @throws DefinitionFormatException if something goes worng with the 
	 * creation of the strict term
	 * 
	 * @see org.jalgo.module.ebnf.model.ebnf.Term#getStrict()
	 */
	@Override
	public Term getStrict() throws DefinitionFormatException {
		// this is the alternative that will be returned
		Term newAlternative = null;
		
		// this is the list of the new alternative
		java.util.List<Term> newTerms = new java.util.ArrayList<Term>();
		

		// if the alternative already is binary, only modify the contained terms
		if (terms.size() == 2) {

			// add the two terms to the new list of the concatenation
			newTerms.add(terms.get(0).getStrict());
			newTerms.add(terms.get(1).getStrict());

		}
		// if the list contains more than two alternatives...
		else {
			// add the fist term of the list to the alternative
			newTerms.add(terms.get(0).getStrict());
			
			// create a new alternative for the rest of the terms.
			Term restAlternative = null;
			
			// the list for the new rest-alternative
			java.util.List<Term> restTerms = new java.util.ArrayList<Term>();
			
			// fill the list
			for (int i=1; i<terms.size();i++) {
				restTerms.add(terms.get(i).getStrict());
			}
			
			// Construct the rest-alternative
			restAlternative = new EAlternative(restTerms);
			
			// now, add the restAlternative as the second term of the new
			// alternative
			newTerms.add(restAlternative.getStrict());

		}

		// now the list for the two alternatives is properly filled.
		// now we can create the new Alternative with the two new terms

		newAlternative = new EAlternative(newTerms);

		return newAlternative;
	}

	/**
	 * @see org.jalgo.module.ebnf.model.ebnf.Term#toString()
	 */
	@Override
	public String toString() {

		String returnString = "(";
		for (int i = 0; i < terms.size(); i++) {
			
			returnString += terms.get(i).toString();
			
			if (i == (terms.size() - 1))
				returnString += ")";
			else
				returnString += "|";
		}
		return returnString;
	}

}
