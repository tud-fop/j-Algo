/**
 *	Class Atom that represent a atom in the labdaterme
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

public class Atom extends Term {

	private String name;
	private String bindingID;

	public Atom(String ident) {
		this.name = ident;
		bindingID = "0";
	}

	@Override
	/*
	 * * This Method return all subterms of the current term include the current
	 * term
	 * 
	 * @return List of all Children
	 */
	public List<Term> getAllChildren() {
		List<Term> children = new LinkedList<Term>();
		children.add(this);
		return children;
	}

	@Override
	/*
	 * * Getter for TermType return termtype of atom
	 */
	public ETermType getTermType() {
		return ETermType.ATOM;
	}

	/**
	 * Getter for subterms
	 * 
	 * @param position
	 *            should be "" or thrwos NoSuchElementException()
	 * @return returns term at position
	 * @throws thrwos
	 *             NoSuchElementException() if position not ""
	 */
	public Term getSubTerm(String position) {
		if (position.equals("")) {
			return this;
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	/*
	 * *
	 * 
	 * @return String represents a lambaterm without format
	 */
	public String toString() {
		return name;
	}

	@Override
	/*
	 * *
	 * 
	 * @param position l = linker Teilterm r = rechter Teilterm
	 * 
	 * @param showBrackets vollgeklammert = true
	 */
	public List<FormatString> toFormatString(String pos, boolean showBrackets) {
		
		this.position = pos;
		
		LinkedList<FormatString> fliste = new LinkedList<FormatString>();
		Set<Format> format = new HashSet<Format>();
		if (bindingID.equals("0"))
			format.add(Format.FREE_VAR);
		else {
			format.add(Format.BOUND_VAR);
		}
		FormatString formatstring = new FormatString(name, format, pos);
		fliste.add(formatstring);
		return fliste;
	}

	@Override
	/*
	 * *
	 * 
	 * @param boundvars Set of Variables bound by Abstraction
	 */
	public void recalculateBindingIDs(Set<Atom> boundvars) {
		for (Atom a : boundvars) {
			if (a.equals(this)) {
				setBindingID(a.getBindingID());
				return;
			}
		}
		bindingID = "0";
	}

	@Override
	/*
	 * *
	 * 
	 * @param position l = linker Teilterm r = rechter Teilterm
	 */
	public boolean replaceChild(String pos, Term newterm)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	/*
	 * *
	 * 
	 * @return new list because a atom has no child
	 */
	public List<Term> getChildren() {
		return new LinkedList<Term>();
	}

	/**
	 * 
	 * @return returns true wenn variable frei sonst false
	 */
	public boolean isFree() {
		return bindingID.equals("0");
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
		if (this.bindingID.equals(bindingID)) {
			name = newName;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @return position der bindenden abstraktion wenn variabele gebinen ist
	 *         sonst 0
	 */
	public String getBindingID() {
		return bindingID;
	}

	/**
	 * 
	 * @param bindingID
	 *            position der bindenden abstraktion
	 */
	public void setBindingID(String bindingID) {
		this.bindingID = bindingID;
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
		if (this.bindingID.equals(bindingID)) {
			Term newTerm = t.clone();
			newTerm.parent = this.parent;
			return newTerm;
		} else
			return this.clone();
	}

	@Override
	/*
	 * *
	 * 
	 * @ return returns cloned term
	 */
	public Term clone() {
		Atom a = new Atom(name);
		a.setBindingID(bindingID);
		// a.setParent(parent);
		return a;
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
	
	public List<Atom> getBoundVarOccurences() {
		List<Atom> l = new LinkedList<Atom>();
		
		if(!this.bindingID.equals("0"))
			l.add(this);
			
		return l;
	}
	
	public List<Atom> getFreeVarOccurences() {
		List<Atom> l = new LinkedList<Atom>();
		
		if(this.bindingID.equals("0"))
			l.add(this);
			
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
		if (!boundVars.contains(this)) {
			set.add(this);
		}
		return set;
	}

	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equalsStructure(Term t) {
		if (t.getTermType() != ETermType.ATOM)
			return false;
		else {
			Atom tatom = (Atom) t;
			return bindingID.equals(tatom.getBindingID());
		}
	}
}
