package org.jalgo.module.lambda;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jalgo.module.lambda.controller.LambdaException;
import org.jalgo.module.lambda.controller.ParseUnit;
import org.jalgo.module.lambda.model.Abstraction;
import org.jalgo.module.lambda.model.Application;
import org.jalgo.module.lambda.model.Atom;
import org.jalgo.module.lambda.model.ETermType;
import org.jalgo.module.lambda.model.Shortcut;
import org.jalgo.module.lambda.model.Term;

/**
 * Manages Shortcuts like <add>. Therefore it stores a String and Term
 * representation of every defined Shortcut. Serves to create Shortcut objects.
 * 
 * 
 */
public class ShortcutHandler {

	private LinkedList<Shortcut> shortcuts;

	private static ShortcutHandler instance;

	/**
	 * Private constructor, an object can only be created through getInstance().
	 * See "Singleton Design Pattern"
	 */
	private ShortcutHandler() {
		// shortcutList = new TreeMap<String, Term>();
		shortcuts = new LinkedList<Shortcut>();
	}

	public boolean prepareShortcutList() {
		try {
			addShortcut("identity", "(\u03BBy.y)", true);
			addShortcut("true", "(\u03BBx.(\u03BBy.x))", true);
			// addShortcut("false", "(\u03BBx.(\u03BBy.y))"); == <0>
			addShortcut("0", "(\u03BBx.(\u03BBy.y))", true);
			addShortcut("1", "(\u03BBx.(\u03BBy.(xy)))", true);
			addShortcut("2", "(\u03BBx.(\u03BBy.(x(xy))))", true);
			addShortcut("3", "(\u03BBx.(\u03BBy.(x(x(xy)))))", true);
			addShortcut("4", "(\u03BBx.(\u03BBy.(x(x(x(xy))))))", true);
			addShortcut("5", "(\u03BBx.(\u03BBy.(x(x(x(x(xy)))))))", true);
			addShortcut("6", "(\u03BBx.(\u03BBy.(x(x(x(x(x(xy))))))))", true);
			addShortcut("7", "(\u03BBx.(\u03BBy.(x(x(x(x(x(x(xy)))))))))", true);
			addShortcut("8", "(\u03BBx.(\u03BBy.(x(x(x(x(x(x(x(xy))))))))))", true);
			addShortcut("9", "(\u03BBx.(\u03BBy.(x(x(x(x(x(x(x(x(xy)))))))))))", true);
			addShortcut("ite", "(\u03BBx.(\u03BBy.(\u03BBz.((xy)z))))", true);
			addShortcut("succ", "(\u03BBz.(\u03BBx.(\u03BBy.(x((zx)y)))))", true);
			addShortcut("iszero", lambda("(Lk.((k(<true><0>))<true>))"), true);
			addShortcut("pred",	lambda("(Lk.(((k(Lp.(Lu.((u(<succ>(p<true>)))(p<true>)))))(Lu.((u<0>)<0>)))<0>))"), true);
			addShortcut("Y", lambda("(Lz.((Lu.(z(uu)))(Lu.(z(uu)))))"), true);
			addShortcut("add", lambda("(<Y>(Lz.(Lx.(Ly.(((<ite>(<iszero>x))y)(<succ>((z(<pred>x))y)))))))"), true);
			addShortcut("mult",	lambda("(<Y>(Lz.(Lx.(Ly.(((<ite>(<iszero>x))<0>)((<add>y)((z(<pred>x))y)))))))"), true);
			addShortcut("fac", lambda("(<Y>(Lz.(Lx.(((<ite>(<iszero>x))<1>)((<mult>x)(z(<pred>x)))))))"), true);

		} catch (LambdaException e) {
			return false;
		}
		return true;
	}

	public String lambda(String s) {
		return s.replaceAll("L", "\u03BB");
	}

	public Term toLambdaNumber(int i) {
		if(i == 0) {
			return createShortcutObject("<0>");
		}
		Atom x = new Atom("x");
		Atom y = new Atom("y");
		Term t1 = new Application(x, y);
		x.setParent(t1);
		y.setParent(t1);
		Term t0;
		while (i > 1) {
			x = new Atom("x");
			t0 = t1;
			t1 = new Application(x, t0);
			x.setParent(t1);
			t0.setParent(t1);
			i--;
		}
		x = new Atom("x");
		y = new Atom("y");
		Term t2 = new Abstraction(y, t1);
		y.setParent(t2);
		t1.setParent(t2);
		Term t3 = new Abstraction(x, t2);
		x.setParent(t3);
		t2.setParent(t3);
		t3.recalculateBindingIDs();
		return t3;
	}

	public int matchLambdaNumber(Term t) {
		Term matchTerm = t.clone(); // clone so the bindingIDs of t stay intact
		matchTerm.recalculateBindingIDs();
		if (matchTerm.getTermType() == ETermType.ABSTRACTION) {
			matchTerm = matchTerm.getSubTerm("a");
			if (matchTerm.getTermType() == ETermType.ABSTRACTION) {
				matchTerm = matchTerm.getSubTerm("a");
				int i = 0;
				while (matchTerm.getTermType() == ETermType.APPLICATION
						&& matchTerm.getSubTerm("l").getTermType() == ETermType.ATOM) {
					Atom x = (Atom) matchTerm.getSubTerm("l");
					if (!x.toString().equals("x"))
						return -1;
					else {
						matchTerm = matchTerm.getSubTerm("r");
						i++;
					}
				}
				if (matchTerm.getTermType() == ETermType.ATOM
						&& ((Atom) matchTerm).toString().equals("y"))
					return i;
			}
		}
		return -1;
	}


	/**
	 * Get the (only) ShortcutHandler instance
	 * 
	 * @return the ShortcutHandler instance
	 */
	public static ShortcutHandler getInstance() {
		if (instance == null) {
			instance = new ShortcutHandler();
			instance.prepareShortcutList();
		}
		return instance;
	}

	/**
	 * Add a shortcut definition to shortcutList
	 * 
	 * @param representation
	 *            String representation of the Shortcut
	 * @param term
	 *            Term representation of the Shortcut
	 * @return true if the definition has been added
	 * @throws LambdaException
	 */
	public boolean addShortcut(String representation, String term, boolean predefined)
			throws LambdaException {
		ParseUnit parser = new ParseUnit();
		Term t = null;
		int pos = 0;
		if (representation.equals(""))
			throw new LambdaException("Leere Abkürzung");
		if (term.equals(""))
			throw new LambdaException("Leerer Lambdaterm");
		if (!representation.startsWith("<"))
			representation = "<" + representation;
		if (!representation.endsWith(">"))
			representation = representation + ">";
		t = parser.parseString(term);
		for (Shortcut sc : shortcuts) {
			if (t.toString().length() < sc.getRepresentedTerm().toString()
					.length()) {
				pos++;
			}
			if (sc.getRepresentation().equals(representation)) {
				throw new LambdaException("Abkürzung schon vergeben");
			}
			if (sc.getRepresentedTerm().toString().equals(t.toString())) {
				throw new LambdaException(
						"Lambdaterm ist schon als Abkürzung vorhanden");
			}
			if (sc.getRepresentedTerm().equalsStructure(t)) {
				throw new LambdaException(
						"Lambdaterm ist schon als Abkürzung vorhanden");
			}
		}
		t.recalculateBindingIDs();
		shortcuts.add(pos, new Shortcut(representation, t, predefined));		
		return true;
	}

	/**
	 * Remove a Shortcut from the list
	 * 
	 * @param representation
	 *            String representation of the shortcut
	 * @return true if the shortcut has been removed
	 */
	public boolean removeShortcut(String representation) {
		// Term altTerm;
		// altTerm = shortcutList.remove(representation);
		for (Shortcut sc : shortcuts) {
			if (sc.getRepresentation().equals(representation)) {
				return shortcuts.remove(sc);
			}
		}
		// if (altTerm.toString() == representation)
		// return true;
		// else
		return false;
	}
	
	/**
	 * Remove all non predefined Shortcuts from the list
	 * 
	 * @return true if the shortcuts have been removed
	 */
	public void clearShortcuts() {
		shortcuts.clear();
		prepareShortcutList();
	}

	/**
	 * Get a Set of all shortcut String representations
	 * 
	 * @return Set of all shortcut String representations
	 */
	public Set<String> getShortcutList() {
		Set<String> list = new TreeSet<String>();
		for (Shortcut sc : shortcuts) {
			list.add(sc.getRepresentation());
		}
		return list;
	}

	public List<Shortcut> getAllAvailabilShortCuts() {
		return shortcuts;
	}

	public List<Shortcut> getReverseList() {
		LinkedList<Shortcut> list = new LinkedList<Shortcut>(shortcuts);
		Collections.reverse(list);
		return list;
	}

	/**
	 * Check if the shortcut is predefined. The shortcut is 
	 * identified by its String representation
	 * 
	 * @param representation
	 *            the shortcuts String representation
	 * @return true if shortcut predefined
	 */
	public boolean isShortcutPredefined(String representation) {
		for (Shortcut sc : shortcuts) {
			if (sc.getRepresentation().equals(representation)) {
				return sc.isPredefined();
			}
		}
		return false;
	}

	/**
	 * Get the Term representation of shortcut identified by its String
	 * representation
	 * 
	 * @param representation
	 *            the shortcuts String representation
	 * @return the shortcuts Term representation
	 */
	public Term getShortcutTerm(String representation) {
		for (Shortcut sc : shortcuts) {
			if (sc.getRepresentation().equals(representation)) {
				return sc.getRepresentedTerm();
			}
		}
		// throw new IllegalArgumentException();
		// return shortcutList.get(representation);
		return null;
	}

	/**
	 * Get the String representation of shortcut if the shortcutList contains 
	 * a shortcut with the term representation
	 * 
	 * @param term
	 *            Term representation of the Shortcut
	 * @return string representation of shortcut
	 */
	public String getShortcutRepresentation(String term) {
		ParseUnit parser = new ParseUnit();
		Term t = null;
		try {
			t = parser.parseString(term);
		} catch (LambdaException e) {
			return null;
		}
		for (Shortcut sc : shortcuts) {
			if (sc.getRepresentedTerm().toString().equals(t.toString())) {
				return sc.getRepresentation();
			}
			if (sc.getRepresentedTerm().equalsStructure(t)) {
				return sc.getRepresentation();
			}
		}
		return null;
	}
	
	public Term getShortcutCloneByRepresentation(String representation) {
		for (Shortcut sc : shortcuts) {
			if (sc.getRepresentation().equals(representation)) {
				return sc.clone();
			}
		}
		representation=representation.replace(">", "");
		representation=representation.replace("<", "");
		try {
			// if(Integer.valueOf(representation).getClass().equals(Integer.class)){
			return new Shortcut("<"+representation+">", toLambdaNumber(Integer
					.parseInt(representation)), false);
			// }
		} catch (NumberFormatException e) {
			//System.out.println("fehler bei Zahl " + representation);
		}
		throw new IllegalArgumentException(representation + " is no Shortcut");
	}

	public Term getShorcutCloneByRepresentedTerm(Term SubTerm) {
		int zahl = matchLambdaNumber(SubTerm);
		if (zahl != -1) {
			return toLambdaNumber(zahl);
		}
		for (Shortcut sc : shortcuts) {
			if (sc.getRepresentedTerm().equalsStructure(SubTerm)) {
				return sc.clone();
			}
		}
		return SubTerm;
	}

	/**
	 * Creates a Shortcut object from a given String.
	 * 
	 * @param representation
	 *            String representation of the Shortcut to create.
	 * @return A Shortcut object that fits to the given representation or NULL
	 *         if there is no fitting entry in shortcutList.
	 */
	public Shortcut createShortcutObject(String representation) {
		return new Shortcut(representation, getShortcutTerm(representation), false);
	}

	/**
	 * Creates a Shortcut to a corrosponding int value
	 * 
	 * @param the int value that should be represented by the Shortcut
	 * @return the created Shortcut Object
	 */
	public Shortcut createNumberShortcut(int number) {
		return new Shortcut("<" + number + ">", toLambdaNumber(number), false);
	}
	
	/**
	 * check the subterm if the subterm is a shortcut.
	 * 
	 * @param subterm
	 *            that should match
	 * 
	 * @return retruns a shortcut that matchs or the full lambdaterm
	 */
	public Term matchShortcut(Term subTerm) {
		int zahl = matchLambdaNumber(subTerm);
		if (zahl > -1) {
			return new Shortcut("<" + zahl + ">", toLambdaNumber(zahl), false);
		}
		for (Shortcut sc : shortcuts) {
			if (sc.getRepresentedTerm().equalsStructure(subTerm)) {
				return sc.clone();
			}
		}
		return null;
	}

}
