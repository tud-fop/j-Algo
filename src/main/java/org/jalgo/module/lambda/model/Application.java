/**
 *	Class Application that represent a application in the labdaterme
 *	This Class is Composit of the Term Composit 
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

public class Application extends Term {

	private Term lterm;

	private Term rterm;

	public Application(Term lterm, Term rterm) {
		this.rterm = rterm;
		this.lterm = lterm;
	}

	@Override
	/**
	 * Getter for TermType
	 *  return termtype of application
	 */
	public ETermType getTermType() {

		return ETermType.APPLICATION;
	}

	@Override
	/**
	 * This Method return all subterms of the current term include the current term
	 * @return List of all Children
	 */
	public List<Term> getAllChildren() {
		List<Term> children = new LinkedList<Term>();
		children.add(this);
		//children.add(lterm);
		children.addAll(lterm.getAllChildren());
		//children.add(rterm);
		children.addAll(rterm.getAllChildren());
		return children;
	}

	@Override
	/**
	 * Getter for subterms
	 * @param position l = linker Teilterm r = rechter Teilterm
	 * 
	 * @return returns term at position
	 * 
	 * @throws thrwos NoSuchElementException() if the first char of position is
	 * not l or r
	 */
	public Term getSubTerm(String position) {
		if (position != null) {
			if (!position.equals("")) {
				if (position.startsWith("l")) {
					return lterm.getSubTerm(position.substring(1));
				} else if (position.startsWith("r")) {
					return rterm.getSubTerm(position.substring(1));
				} else {
					throw new NoSuchElementException(toString());
				}
			} else {
				return this;
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	/**
	 * 
	 * @return String represents a lambaterm without format
	 */
	public String toString() {
		return "(" + lterm.toString() + rterm.toString() + ")";
	}

	@Override
	/** 
	 * @param pos Positionstring l = linker Teilterm r = rechter Teilterm
	 * 
	 * @param showBrackets vollgeklammert = true
	 */
	public List<FormatString> toFormatString(String pos, boolean showBrackets) {
		LinkedList<FormatString> fliste = new LinkedList<FormatString>();
		Set<Format> format = new HashSet<Format>();
		if (showBrackets) {
			fliste.add(new FormatString("(", format, pos));
			fliste.addAll(lterm.toFormatString(pos + "l", showBrackets));
			fliste.addAll(rterm.toFormatString(pos + "r", showBrackets));
			fliste.add(new FormatString(")", format, pos));
		} else {
			if(lterm.getTermType() == ETermType.ABSTRACTION){
				fliste.add(new FormatString("(", format, pos));
			}
			fliste.addAll(lterm.toFormatString(pos + "l", showBrackets));
			if(lterm.getTermType() == ETermType.ABSTRACTION){
				fliste.add(new FormatString(")", format, pos));
			}
			if(rterm.getTermType() == ETermType.ABSTRACTION){
				fliste.add(new FormatString("(", format, pos));
			}
			fliste.addAll(rterm.toFormatString(pos + "r", showBrackets));
			if(rterm.getTermType() == ETermType.ABSTRACTION){
				fliste.add(new FormatString(")", format, pos));
			}
		}
		return fliste;
	}

	@Override
	/**
	 * 
	 * @param boundVars Menge der gebundnen variablen
	 */
	public void recalculateBindingIDs(Set<Atom> boundvars) {
		lterm.recalculateBindingIDs(boundvars);
		rterm.recalculateBindingIDs(boundvars);
	}

	@Override
	/**
	 * 
	 * @param pos Positionstring l = linker Teilterm r = rechter Teilterm
	 * 
	 * @param newTerm new subterm for position
	 */
	public boolean replaceChild(String pos, Term newterm) {
		if (pos != null) {
			if (pos.equals("l")) {
				lterm = newterm;
				//lterm.setParent(this);
				return true;
			} else if (pos.equals("r")) {
				rterm = newterm;
				//rterm.setParent(this);
				return true;
			} else if (pos.startsWith("r")) {
				return rterm.replaceChild(pos.substring(1), newterm);
			} else if (pos.startsWith("l")) {
				return lterm.replaceChild(pos.substring(1), newterm);
			} else {
				throw new NoSuchElementException();
			}
		} else {
			throw new IllegalArgumentException();
		}

	}

	@Override
	/*
	 * *
	 * 
	 * @return returns left and right part of abstraction
	 */
	public List<Term> getChildren() {
		List<Term> children = new LinkedList<Term>();
		children.add(lterm);
		children.add(rterm);
		return children;
	}

	@Override
	/**
	 * 
	 * @param bindingID position der bindenen abstraktion
	 * 
	 * @param newName name for variable at position
	 */
	public boolean alphaConvert(String bindingID, String newName) {
		boolean l = lterm.alphaConvert(bindingID, newName);
		boolean r = rterm.alphaConvert(bindingID, newName);
		return (l || r); //TODO return (l && r); ???
	}

	@Override
	/**
	 * 
	 * @param bindingID position der bindenen abstraktion
	 * 
	 * @param newTerm new subterm for position
	 */
	public Term replaceVarsByBindingID(String bindingID, Term t) {
		Term newLTerm = lterm.replaceVarsByBindingID(bindingID, t);
		Term newRTerm = rterm.replaceVarsByBindingID(bindingID, t);
		//newLTerm.setParent(this);
		//newRTerm.setParent(this);
		Term newTerm = new Application(newLTerm, newRTerm);
		//newTerm.setParent(this.parent); //TODO abs wird wegreduziert
		return newTerm;
	}

	/**
	 * @ return returns cloned term
	 */
	public Term clone() {
		Application a = new Application(lterm.clone(), rterm.clone());
		//a.setParent(parent);
		return a;
	}

	@Override
	/**
	 * 
	 * @return Set<Atom> Menge der gebundnen variablen
	 */
	public Set<Atom> getBoundVars() {
		Set<Atom> set = new HashSet<Atom>();
		set.addAll(lterm.getBoundVars());
		set.addAll(rterm.getBoundVars());
		return set;
	}
	
	public List<Atom> getBoundVarOccurences() {
		List<Atom> l = new LinkedList<Atom>();
		l.addAll(lterm.getBoundVarOccurences());
		l.addAll(rterm.getBoundVarOccurences());
		return l;
	}

	@Override
	/**
	 * 
	 * @param boundVars set of bound Variables
	 * 
	 * @return returns set of free Variables
	 */
	public Set<Atom> getFreeVars(Set<Atom> boundVars) {
		Set<Atom> set = new HashSet<Atom>();
		set.addAll(lterm.getFreeVars(boundVars));
		set.addAll(rterm.getFreeVars(boundVars));
		return set;
	}
	
	public List<Atom> getFreeVarOccurences() {
		List<Atom> l = new LinkedList<Atom>();
		l.addAll(lterm.getFreeVarOccurences());
		l.addAll(rterm.getFreeVarOccurences());
		return l;
	}
	
	@Override
	public boolean equalsStructure(Term t) {
		if(t.getTermType() != ETermType.APPLICATION)
			return false;
		else {
			return	lterm.equalsStructure(t.getSubTerm("l")) &&
					rterm.equalsStructure(t.getSubTerm("r"));
		}
	}
}
