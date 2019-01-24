/**
 *	Class Shortcut that represent a shortcut of a complex labdaterme
 *	This class is a leaf of the term composit 
 * @author Joshua Peschke
 * @author Nico Braunisch  
 * @version 1.0
 */

package org.jalgo.module.lambda.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class Shortcut extends Term  {

	private String representation;
	private Term representedTerm;
	private boolean predefined;
	private int number;
	
	public Shortcut(String representation, Term repTerm, boolean predefined) {
		this.representation = representation;
		this.representedTerm = repTerm;
		repTerm.recalculateBindingIDs();
		this.predefined = predefined;
		String numberString = representation.replace(">", "");
		numberString = numberString.replace("<", "");
		try {
			number = Integer.valueOf(numberString).intValue();
		}
		catch (NumberFormatException e) {
			number = -1;
		}
	}

	public int getNumber() {
		return number;
	}
	
	/**
	 * @return returns lambdaterm of shortcut
	 */
	public Term getRepresentedTerm() {
		return representedTerm;
	}

	/**
	 * 
	 * @return returns symbol of shortcut
	 */
	public String getRepresentation() {
		return representation;
	}

	/**
	 * 
	 * @return predefined
	 */
	public boolean isPredefined() {
		return predefined;
	}

	@Override
	/*
	 * *
	 * 
	 * @return returns list of Children of the represented Lambdaterm
	 */
	public List<Term> getChildren() {
		return new LinkedList<Term>();
	}

	@Override
	/**
	 * 
	 * Getter for subterms
	 * @param position l = linker Teilterm r = rechter Teilterm a=abstraction
	 * 
	 * @return term at position
	 */
	public Term getSubTerm(String position) {
		if (position.equals("")){
			return this;
		}else {
			throw new NoSuchElementException();
		}
	}

	@Override
	/*
	 * *
	 * 
	 * @return retuns Termtype of reopresented Lambdaterm if shortcut is
	 * eliminated else return Termtype shortcut
	 */
	public ETermType getTermType() {

		return ETermType.SHORTCUT;

	}

	@Override
	/*
	 * *
	 * 
	 * @param position l = linker Teilterm r = rechter Teilterm
	 * 
	 * @param showBrackets vollgeklammert = true
	 * 
	 * @return returns list of subterms with format if shortcut is eliminated
	 * else retuns name of shortcut
	 */
	public List<FormatString> toFormatString(String pos, boolean showBreckets) {
		LinkedList<FormatString> fliste = new LinkedList<FormatString>();
		FormatString formatstring = new FormatString(representation,
				new HashSet<Format>(), pos);
		fliste.add(formatstring);
		return fliste;
	}

	@Override
	/*
	 * *
	 * 
	 * @return String represents a lambaterm without format if shortcut is
	 * eliminated else return name of shortcut
	 */
	public String toString() {
		return representation;
	}

	@Override
	/**
	 * This Method return all subterms of the current term include the current term
	 * @return List of all Children
	 */
	public List<Term> getAllChildren() {
		List<Term> children = new LinkedList<Term>();
		children.add(this);
		return children;
	}

	@Override
	/*
	 * *
	 * 
	 * @param boundvars Set of Variables bound by Abstraction
	 */
	public void recalculateBindingIDs(Set<Atom> boundVars) {

	}

	@Override
	/*
	 * *
	 * 
	 * @param pos Positionstring l = linker Teilterm r = rechter Teilterm a =
	 * position
	 * 
	 * @param newTerm new subterm for position
	 */
	public boolean replaceChild(String pos, Term newterm)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();

	}

	@Override
	/*
	 * *
	 * 
	 * @param bindingID position der bindenen abstraktion
	 * 
	 * @param newName name for variable at position
	 */
	public boolean alphaConvert(String bindingID, String newName) {
		return false;
	}

	@Override
	/*
	 * *
	 * 
	 * @param bindingID position der bindenen abstraktion
	 * 
	 * @param newTerm new subterm for position
	 */
	public Term replaceVarsByBindingID(String bindingID, Term t) {
		throw new UnsupportedOperationException();
	}

	@Override
	/*
	 * *
	 * 
	 * @ return returns cloned term
	 */
	public Term clone() {
		return new Shortcut(representation, representedTerm.clone(), predefined);
	}

	@Override
	/*
	 * *
	 * 
	 * @return set<Atom> Menge der gebundnen variablen
	 */
	public Set<Atom> getBoundVars() {
		return new HashSet<Atom>();
	}

	@Override
	/*
	 * *
	 * 
	 * @param boundVars set of bound Variables
	 * 
	 * @return returns set of free Variables
	 */
	public Set<Atom> getFreeVars(Set<Atom> boundVars) {
		return new HashSet<Atom>();
	}

	@Override
	public Set<Atom> getFreeVars() {
		return new HashSet<Atom>();
	}

	@Override
	public boolean equalsStructure(Term t) {
		if (t.getTermType() != ETermType.SHORTCUT)
			return false;
		else {
			Shortcut tsh = (Shortcut) t;
			return representedTerm.equalsStructure(tsh.getRepresentedTerm());
		}
	}

	@Override
	public List<Atom> getBoundVarOccurences() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public List<Atom> getFreeVarOccurences() {
		throw new UnsupportedOperationException();
	}
}
