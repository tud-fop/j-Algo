/**
 *	Class abstraction that represent a abstraction in the labdaterme
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

public class Abstraction extends Term {

	private Term term;

	private Atom var;

	public Abstraction(Atom var, Term term) {
		this.term = term;
		this.var = var;
	}

	/**
	 * This Method return all subterms of the current term include the current term
	 * @return List of all Children
	 */
	public List<Term> getAllChildren() {
		List<Term> children = new LinkedList<Term>();
		children.add(this);
		//children.addAll(var.getAllChildren());
		children.addAll(term.getAllChildren());
		return children;
	}

	@Override
	/**
	 * Getter for TermType
	 *  return termtype of abstraction
	 */
	public ETermType getTermType() {
		return ETermType.ABSTRACTION;
	}

	@Override
	/**
	 * Getter for subterms
	 * 
	 * @param position l = linker Teilterm r = rechter Teilterm
	 * 
	 * @return term at position
	 */
	public Term getSubTerm(String position) {
		if (position != null) {
			if (!position.equals("")) {
				if (position.startsWith("a")) {
					position = position.substring(1);
					return term.getSubTerm(position);
				} else {
					throw new NoSuchElementException();
				}
			} else {
				return this;
			}
		} else {
			throw new IllegalArgumentException(" @ "+ position);
		}
	}

	@Override
	/*
	 * *
	 * 
	 * @return String represents a lambaterm without format
	 */
	public String toString() {
		return "(\u03bb" + var.toString() + "." + term.toString() + ")";
	}

	@Override
	/*
	 * *
	 * 
	 * @param position l = linker Teilterm r = rechter Teilterm *@param
	 * showBrackets vollgeklammert = true
	 * 
	 * @return list of subterms with format
	 */
	public List<FormatString> toFormatString(String pos, boolean showBrackets) {
		
		this.position = pos;
		
		LinkedList<FormatString> fliste = new LinkedList<FormatString>();
		Set<Format> format = new HashSet<Format>();
		if (showBrackets) {
			fliste.add(new FormatString("(", format, pos));
			fliste.add(new FormatString("\u03bb" + var.toString() + ".",
					format, pos));
			fliste.addAll(term.toFormatString(pos + "a", showBrackets));
			fliste.add(new FormatString(")", format, pos));
		} else {
			if ((parent == null)
					|| (parent.getTermType() != ETermType.ABSTRACTION)) {
				fliste.add(new FormatString("\u03bb", format, pos));
			}
			fliste.add(new FormatString(var.toString(), format, pos));
			if (term.getTermType() != ETermType.ABSTRACTION) {
				fliste.add(new FormatString(".", format, pos));
			}
			fliste.addAll(term.toFormatString(pos + "a", showBrackets));
		}
		return fliste;
	}

	@Override
	/*
	 * *
	 * 
	 * @param boundvars Set of Variables bound by Abstraction
	 */
	public void recalculateBindingIDs(Set<Atom> boundvars) {
		var.setBindingID(getPosition());
		Set<Atom> newBoundvars = new HashSet<Atom>();
		newBoundvars.add(var);
		newBoundvars.addAll(boundvars);
		term.recalculateBindingIDs(newBoundvars);
	}

	@Override
	/*
	 * *
	 * 
	 * @param pos Positionstring l = linker Teilterm r = rechter Teilterm
	 * 
	 * @param newTerm new subterm for position
	 */
	public boolean replaceChild(String pos, Term newterm) {
		if (pos != null) {
			if (pos.equals("a")) {
				term = newterm;
				//term.setParent(this);
				return true;
			} else if (pos.equals("v")) {
				var = (Atom) newterm;
				//var.setParent(this);
				return true;
			} else if (pos.startsWith("a")) {
				return term.replaceChild(pos.substring(1), newterm);
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
		children.add(var);
		children.add(term);
		return children;
	}

	@Override
	/*
	 * *
	 * 
	 * @param bindingID position der bindenen abstraktion
	 * 
	 * @param newName name for variable at position
	 * 
	 * @return true= alphaconversion solved
	 */
	public boolean alphaConvert(String bindingID, String newName) {
		boolean t = term.alphaConvert(bindingID, newName);
		boolean v = var.alphaConvert(bindingID, newName);
		return (t || v);
	}

	@Override
	/*
	 * *
	 * 
	 * @param bindingID position der bindenen abstraktion
	 * 
	 * @param newTerm new subterm for position
	 * 
	 * @return further Term at position
	 */
	public Term replaceVarsByBindingID(String bindingID, Term t) {
		if (bindingID.equals(getPosition()))
			throw new RuntimeException(
					"Bound var about was about to be replaced!");
		
		Term newTerm = new Abstraction((Atom) var.clone(), term.replaceVarsByBindingID(bindingID, t));
		//newTerm.setParent(this.parent);
		
		return newTerm;
	}

	/**
	 * @ return returns cloned term
	 */
	public Term clone() {
		Abstraction a = new Abstraction((Atom)var.clone(), term.clone());
		//a.setParent(parent);
		return a;
	}

	@Override
	/*
	 * *
	 * 
	 * @ return returns set of bound Variables
	 */
	public Set<Atom> getBoundVars() {
		Set<Atom> set = new HashSet<Atom>();
		set.add(var);
		set.addAll(term.getBoundVars());
		return set;
	}
	
	public List<Atom> getBoundVarOccurences() {
		List<Atom> l = new LinkedList<Atom>();
		l.addAll(term.getBoundVarOccurences());
		return l;
	}
	
	public List<Atom> getFreeVarOccurences() {
		List<Atom> l = new LinkedList<Atom>();
		l.addAll(term.getFreeVarOccurences());
		return l;
	}

	@Override
	/**
	 * @param boundVars set of bound Variables
	 * 
	 * @return returns set of free Variables
	 */
	public Set<Atom> getFreeVars(Set<Atom> boundVars) {
		Set<Atom> set = new HashSet<Atom>();
		set.add(var);
		set.addAll(boundVars);
		return term.getFreeVars(set);
	}

	@Override
	public boolean equalsStructure(Term t) {
		if(t.getTermType() != ETermType.ABSTRACTION)
			return false;
		else
			return 	(term.equalsStructure(t.getSubTerm("a")));
	}	
}
