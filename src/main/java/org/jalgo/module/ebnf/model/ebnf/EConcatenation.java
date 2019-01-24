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
 * Represents the concatenation in EBNF. The concatenation is <b>not</b>
 * binary, but contains at least two terms
 * </p>
 * 
 */
public class EConcatenation extends org.jalgo.module.ebnf.model.ebnf.Term {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * <p>
	 * the terms inside the concatenation
	 * </p>
	 */
	private java.util.List<Term> terms = new java.util.ArrayList<Term>();

	/**
	 * <p>
	 * Creates a Concatenation with at least two terms. It only adds elements
	 * that are not null.
	 * </p>
	 * 
	 * @param terms
	 *            is the list of terms to be in the alternative
	 * @throws DefinitionFormatException
	 *             if there are less than two terms in the given list that are
	 *             not null, if the list contains terms that are null am
	 *             exception will be thrown
	 */
	public EConcatenation(java.util.List<Term> terms)
			throws DefinitionFormatException {
		this.terms = new java.util.ArrayList<Term>();

		// add all terms that are not null and do not contain the concatenation
		// itself
		for (Term term : terms) {
			if (term == null)
				throw new DefinitionFormatException("could not add null term");
			else if(term.contains(this))
				throw new DefinitionFormatException
							("could not add term containing the parent term");
			else 
				this.terms.add(term);
		}

		// check if the concatenation has at least two terms, otherwise
		// throw exception
		if (this.terms.size() < 2) {
			this.terms = null;
			throw new DefinitionFormatException
							("not enough terms in concatenation!");
		}

	}

	/**
	 * <p>
	 * Creates a binary Concatenation. If at least one of the arguments is null an
	 * Exception is thrown.
	 * </p>
	 * 
	 * 
	 * @param term1
	 *            is the first term
	 * @param term2
	 *            is the second term
	 * @throws DefinitionFormatException
	 */
	public EConcatenation(Term term1, Term term2)
			throws DefinitionFormatException {
		if (term1 == null || term2 == null)
			throw new DefinitionFormatException("could not add null term");

		terms = new java.util.ArrayList<Term>();
		terms.add(term1);
		terms.add(term2);
	}
	
	
	/**
	 * @return a copy of the list of the terms
	 */
	@Override
	public java.util.List<Term> getTerms() {
		return new java.util.ArrayList<Term>(terms);
	}

	/**
	 * <p>
	 * Adds the term to the end of the list if it is not null
	 * or is not the EConcatenation itself.
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.ebnf.model.ebnf.Term#isStrict()
	 */
	@Override
	public boolean isStrict() {
		boolean isStrict = true;
		for (Term term : terms)
			isStrict = isStrict && term.isStrict();
		return isStrict;
	}

	
	/**
	 * This method returns the strict version of the EBNF term. Since the trans
	 * algorithm cannot process concatenations inside concatenations, all 
	 * concatenations inside a concatenation are merged with the the first
	 * concatenation. If a concat includes a compound term this term will be
	 * removed and its inner term added to the concatenation.
	 * 
	 * @return the strict version of the term
	 * 
	 * @see org.jalgo.module.ebnf.model.ebnf.Term#getStrict()
	 */
	@Override
	public Term getStrict() throws DefinitionFormatException {
		// the new concatenation
		Term newConcatenation;
		
		// the list of the new concatenation
		java.util.List<Term> newTerms = new java.util.ArrayList<Term>();
		
		// remove nested concatenations
		newTerms = getStrictRec(this.terms);
		
		// make terms in newTerms strict
		for(int i = 0; i < newTerms.size();i++) {
			newTerms.set(i,newTerms.get(i).getStrict());
		}

		// create the new concatenation with the new list
		newConcatenation = new EConcatenation(newTerms);

		return newConcatenation;
	}
	
	/**
	 * This private method is called by the getStrict method. It traverses the
	 * concatenations in the list and replaces every concatenation in the list
	 * with the included elements.
	 * 
	 * @param subTerms the list of terms of the original concatenation
	 * @return the new list of elements of the concatenation.
	 */
	private java.util.List<Term> getStrictRec(java.util.List<Term> subTerms){
		
		// create a new list for the subterms
		java.util.List<Term> retTerms = new java.util.ArrayList<Term>();
		
		// for every subTerm in the old list ...
		for (Term subTerm : subTerms) {
			// ... if it is a concatenation add all terms of this concatenation
			// to the new list
			if (subTerm instanceof EConcatenation) {
				retTerms.addAll(getStrictRec(subTerm.getTerms()));
			} 
			
			// ...if it is a compound term add all subterms of the first (and 
			// only) subterm of the compound term to the list.
			else if (subTerm instanceof ECompoundTerm) {
				retTerms.addAll(getStrictRec(subTerm.getTerms().get(0).getTerms()));
			}
			
			// ... if it is neither a compund term nor a concatenation, simply
			// add the term to the list.
			else {
				retTerms.add(subTerm);
			}
		}
		return retTerms;
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
	 * @see org.jalgo.module.ebnf.model.ebnf.Term#toString()
	 */
	@Override
	public String toString() {

		String returnString = "";
		for (Term term : terms) {
			returnString += term.toString();
		}
		return returnString;
	}
}
