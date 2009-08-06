/**
 *	Abstract class term
 *	This class is a component of the term composit 
 * @author Joshua Peschke
 * @author Nico Braunisch  
 * @version 1.0
 */
package org.jalgo.module.lambda.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public abstract class Term implements Iterable<Term> {

	protected Term parent;
	protected String position;

	/**
	 * @param position
	 *            l = linker Teilterm r = rechter Teilterm a=abstraction
	 * @return term at position
	 */
	public abstract Term getSubTerm(String position);

	/**
	 * This method calculates the free variables.
	 * 
	 * @return returns set of free Variables
	 */
	public Set<Atom> getFreeVars() {
		return getFreeVars(new HashSet<Atom>());
	}

	/**
	 * This method calculates the free variables.
	 * 
	 * @param boundVars
	 *            Menge der gebundnen variablen
	 * @return returns set of free Variables
	 */
	public abstract Set<Atom> getFreeVars(Set<Atom> boundVars);

	/**
	 * This method calculates the free variables. 
	 * @ return returns set of bound Variables
	 */
	public abstract Set<Atom> getBoundVars();

	/**
	 * This method replace Variables
	 * 
	 * @param bindingID
	 *            position der bindenen abstraktion
	 * @param newTerm
	 *            new subterm for position
	 */
	public abstract Term replaceVarsByBindingID(String bindingID, Term t);

	/**
	 * This method realise the Alpha Convertion
	 * 
	 * @param bindingID
	 *            position der bindenen abstraktion
	 * @param newName
	 *            name for variable at position
	 */
	public abstract boolean alphaConvert(String bindingID, String newName);

	/**
	 * Returns a string that represent the lambdaterm
	 * 
	 * @return String represents a lambaterm without format
	 */
	public abstract String toString();

	/**
	 * Returns a complex object that represent the lambdaterm with format
	 * 
	 * @param pos
	 *            Positionstring l = linker Teilterm r = rechter Teilterm
	 * @param showBrackets
	 *            vollgeklammert = true
	 */
	public abstract List<FormatString> toFormatString(String pos,
			boolean showBreckets);

	/**
	 * Getter for parent
	 * 
	 * @return parent of curent term
	 */
	public Term getParent() {
		return parent;
	}

	/**
	 * Setter for Parent
	 * 
	 * @param parent
	 *            parent of current term
	 */
	public void setParent(Term parent) {
		this.parent = parent;
	}

	/**
	 * Check equality of 2 terms
	 * 
	 * @return true if object o is same kind of term like current term
	 * @param o
	 *            should be term
	 */
	public boolean equals(Object o) {
		return (o.toString().equals(this.toString()));
	}

	/**
	 * Getter for Termtype
	 * 
	 * @return termtype of current term
	 */
	public abstract ETermType getTermType();

	/**
	 * This Method return all subterms of the current term include the current
	 * term
	 * 
	 * @return List of all Children
	 */
	public abstract List<Term> getAllChildren();

	/**
	 * This Method return all subterms of the current term include the current
	 * term
	 * 
	 * @return returns direct subterm ob current term
	 */
	public abstract List<Term> getChildren();

	/**
	 * Recalculate binding id for Atoms The bindi id is the position of the
	 * Abstraction that bind the atom
	 * 
	 * @param boundvars
	 *            Set of Variables bound by Abstraction
	 */
	public abstract void recalculateBindingIDs(Set<Atom> boundvars);

	/**
	 * Recalculate binding id for Atoms The bindi id is the position of the
	 * Abstraction that bind the atom
	 * 
	 * @param boundvars
	 *            Set of Variables bound by Abstraction
	 */
	public void recalculateBindingIDs() {
		recalculateBindingIDs(new HashSet<Atom>());
	}

	/**
	 * Relpace subterm at position
	 * 
	 * @param pos
	 *            Positionstring l = linker Teilterm r = rechter Teilterm
	 * @param newTerm
	 *            new subterm for position
	 */
	public abstract boolean replaceChild(String pos, Term newterm);

	/**
	 * Calculate the position of the subterm
	 * 
	 * @return returns absolut position of current term from root
	 */
	public String getPosition() {
		String pos = "";
		Term parent = getParent();
		Term current = this;
		while (parent != null) {
			ETermType tType = parent.getTermType();
			switch (tType) {
			case ABSTRACTION:
				pos = "a" + pos;
				break;
			case APPLICATION:
				if (current == parent.getSubTerm("l")) // TODO geht das mit '=='
														// ???
					pos = "l" + pos;
				else
					pos = "r" + pos;
				break;
			case ATOM:
				throw new RuntimeException("Atoms cant have childresn");
			case SHORTCUT:
				throw new RuntimeException("Shortcuts cant have children");
			}
			current = parent;
			parent = parent.getParent();
		}
		return pos;
	}

	/**
	 * Reculculate all parents Top-Down
	 * 
	 * @param p
	 *            Subterm
	 */
	public void recalculateParentsRecursive(Term p) {
		this.parent = p;

		for (Term c : this.getChildren()) {
			c.recalculateParentsRecursive(this);
		}
	}

	/**
	 * Clone the subterm @ return returns cloned term
	 */
	public abstract Term clone();

	/**
	 * This method check the syntactic equality of 2 terms
	 * 
	 * @param t
	 * @return return true if the t has the same semantic as the current term
	 */
	public abstract boolean equalsStructure(Term t);

	/**
	 * Iterates over the term
	 * 
	 * @return returns TermIterator
	 */
	public TermIterator iterator() {
		return new TermIterator(this);
	}
	
	public List<Atom> getVarOccurences(Atom var) {
		TermIterator termIt = this.iterator();
		List<Atom> list = new LinkedList<Atom>();
		while(termIt.hasNext()) {
			Term t = termIt.next();
			if(t.equals(var)) {
				list.add((Atom)t);
			}
		}
		return list;
	}
	
	public abstract List<Atom> getBoundVarOccurences();
	
	public abstract List<Atom> getFreeVarOccurences();
}
