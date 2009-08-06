/**
 *	Class TermHandler
 *	
 * @author Joshua Peschke
 * @author Nico Braunisch  
 * @version 1.0
 */
package org.jalgo.module.lambda.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.jalgo.module.lambda.ShortcutHandler;
import org.jalgo.module.lambda.view.EMessageType;

public class TermHandler extends Observable implements ITermHandler {

	private Term root;
	private String lastOperationOutput;

	/**
	 * 
	 * @param t
	 *            rootterm
	 * @param o
	 *            obsever for termevent
	 */
	public TermHandler(Term t, Observer o) {
		this.root = t;
		this.addObserver(o);
		// root.recalculateParents();
		root.recalculateBindingIDs();
		setChanged();
		notifyObservers(EMessageType.OUTPUTSTRING_CHANGED);
	}

	/**
	 * Do autostep
	 * @param position of autostep
	 * @return Kind of operation
	 */
	public EStepKind doLowLevelAutoStep(String position) {
		String betaPos = getPossibleBetaReduction(position);
		if (betaPos != null) {
			if (betaReduce(betaPos) == EAvailability.AVAILABLE)
				return EStepKind.BETA;
			else
				throw new RuntimeException("Automatic Beta Reduction failed...");
		}
		//alpha
		Term alpha = getNeededAlphaConversion(position);
		String bindingIDtoConvert = "";
		if (alpha != null) {
			Term abs = alpha.getSubTerm("la");
			Term rterm = alpha.getSubTerm("r");
			Set<Atom> absBoundVars = abs.getBoundVars();
			Set<Atom> rFreeVars = rterm.getFreeVars();
			for (Atom a : absBoundVars) {
				if (rFreeVars.contains(a)) {
					bindingIDtoConvert = a.getBindingID();
					break;
				}
			}
			if (!bindingIDtoConvert.equals("")) {
				Iterator<String> it;
				it = getAllUnusedVars().iterator();
				if (it.hasNext()) {
					if (alphaConvert(bindingIDtoConvert, it.next()))
						return EStepKind.ALPHA;
				} else
					throw new RuntimeException("No unused vars available!");
			}
			throw new RuntimeException("Automatic Alpha Conversion failed...");
		}
		return EStepKind.NONE;
	}
	
	public EStepKind doHighLevelAutoStep(String position) {
		if(simplifyAShortcut(position)) {
			return EStepKind.SHORTCUT;
		}
		else return EStepKind.NONE;
	}
	
	/**
	 * Generate shortcut
	 * @param position off posible shortcut
	 */
	public void makeAllShortcuts(String position) {
		TermIterator termIt;
		boolean solved = true;
		while (solved) {
			solved = false;
			termIt = getSubTerm(position).iterator();
			while (termIt.hasNext()) {
				String tpos = termIt.getCurrentPos();
				Term t = termIt.next();
				if (t.getTermType() != ETermType.SHORTCUT) {
					t = ShortcutHandler.getInstance().matchShortcut(t);
					if (t.getTermType() == ETermType.SHORTCUT) {
						if (replaceSubTerm(tpos, t)) {
							solved = true;
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * @return succes of alpha conversion
	 * @param position
	 *            position of Atom that should be converted
	 * @param newName
	 *            new name of Atom that should be converted
	 */
	public boolean alphaConvert(String position, String newName) {
		Term t = root.getSubTerm(position);
		if (t.getTermType() == ETermType.ABSTRACTION) {
			String bID = ((Atom) t.getChildren().get(0)).getBindingID();
			boolean success = t.alphaConvert(bID, newName);
			if (success) {
				setChanged();
				notifyObservers(EMessageType.OUTPUTSTRING_CHANGED);
			}
			return success;
		} else
			return false;
	}

	/**
	 * @return succes of beta conversion
	 * @param position
	 *            position of sub that should be converted
	 */
	public EAvailability betaReduce(String position) {
		EAvailability availability = checkPossibleBetaReduction(position);
		if (availability == EAvailability.AVAILABLE) {
			Term t = getSubTerm(position);
			Term lterm = t.getSubTerm("l");
			Term rterm = t.getSubTerm("r");
			String bindingID = position + "l";
			Term absTerm = lterm.getSubTerm("a");
			Term newTerm = absTerm.replaceVarsByBindingID(bindingID, rterm);
			if (replaceSubTerm(position, newTerm))
				return EAvailability.AVAILABLE;
			else
				return EAvailability.NOTAVAILABLE;
		} 
		return availability;
	}

	/**
	 * 
	 * @param t
	 *            term that should be reduced
	 * @return status of betreduction
	 */
	public EAvailability checkPossibleBetaReduction(Term t) {
		if (t.getTermType() == ETermType.APPLICATION) {
			Term lterm = t.getSubTerm("l");
			Term rterm = t.getSubTerm("r");
			if (lterm.getTermType() == ETermType.ABSTRACTION) {
				Term absTerm = lterm.getSubTerm("a");
				Set<Atom> absTermBoundVars = absTerm.getBoundVars();
				Set<Atom> rtermFreeVars = rterm.getFreeVars();
				for (Atom a : absTermBoundVars) {
					for (Atom b : rtermFreeVars) {
						if (a.equals(b))
							return EAvailability.ALPHANEEDED;
					}
				}
				return EAvailability.AVAILABLE;
			}
		}
		return EAvailability.NOTAVAILABLE;
	}

	/**
	 * 
	 * @param position
	 *            position of term that should be reduced
	 * @return status of betreduction
	 */
	public EAvailability checkPossibleBetaReduction(String position) {
		Term t = getSubTerm(position);
		return checkPossibleBetaReduction(t);
	}

	/**
	 * @param position
	 *            of shortcut should be eliminated
	 * @return returns true if shortcut would be eliminated
	 */
	public boolean eliminateShortcut(String position) {
		if (root.getSubTerm(position).getTermType() == ETermType.SHORTCUT) {
			Term newTerm = ((Shortcut) root.getSubTerm(position))
					.getRepresentedTerm().clone();
			return replaceSubTerm(position, newTerm);
			// return replaceSubTerm(position.substring(1), newTerm);
			// return root.getSubTerm(position).equals(newTerm);
		} else {
			return false;
		}
	}
	/**
	 * Generate shortcut
	 * @param position off posible shortcut
	 */
	public boolean matchShortcut(String position) {
		Term t = getSubTerm(position);

		t.recalculateParentsRecursive(null);
		t.recalculateBindingIDs();
		
		if (t.getTermType() != ETermType.SHORTCUT) {
			Term newTerm = ShortcutHandler.getInstance().matchShortcut(t)
					.clone();

			if (newTerm == null) {
				root.recalculateParentsRecursive(null);
				root.recalculateBindingIDs();
				return false;
			}

			root.recalculateParentsRecursive(null);
			root.recalculateBindingIDs();
			return replaceSubTerm(position, newTerm);
			// return replaceSubTerm(position.substring(1), newTerm);
			// return root.getSubTerm(position).equals(newTerm);
		} else {
			root.recalculateParentsRecursive(null);
			root.recalculateBindingIDs();
			return false;
		}
	}

	/**
	 * Dissolve all shortcuts at position
	 * eliminate all shortcut in the root term
	 */
	public void eliminateAllShortcuts(String position) {
		Iterator<Term> iter;
		boolean solved = true;
		while (solved) {
			solved = false;
			iter = getSubTerm(position).getAllChildren().iterator();
			while (iter.hasNext() && !solved) {
				solved = solved || eliminateShortcut(iter.next().getPosition());
			}
		}
	}
	
	/**
	 * This Method return all subterms of the current term include the current
	 * term
	 * 
	 * @return List of all Children
	 */
	public List<Term> getAllChildren() {
		List<Term> children = new LinkedList<Term>();
		children.add(root);
		children.addAll(root.getAllChildren());
		return children;
	}

	/**
	 * @return iterator of formatstring of subterms
	 * @param showBrackets
	 *            vollgeklammert = true
	 */
	public Iterator<FormatString> getFormatString(boolean showBrackets) {
		return root.toFormatString("", showBrackets).iterator();
	}

	/**
	 * @return im dont care
	 */
	public String getLastOperationOutput() {
		return lastOperationOutput;
	}

	/**
	 * Get all needed alphaconversions
	 * @return A set of position Strings that identify Applications where a beta
	 *         reduction would be possible after an alpha conversion was
	 *         performed.
	 */
	public Set<String> getNeededAlphaConversions(String position) {
		List<Term> allSubTerms = root.getSubTerm(position).getAllChildren();
		allSubTerms.add(root);
		Set<String> alphaNeededPositions = new HashSet<String>();
		for (Term t : allSubTerms) {
			if (checkPossibleBetaReduction(t) == EAvailability.ALPHANEEDED) {
				alphaNeededPositions.add(t.getPosition());
			}
		}
		return alphaNeededPositions;
	}

	/**
	 * Get one needed alphaconversions
	 * @return Applications where a beta reduction would be possible after an alpha conversion was
	 *         performed.
	 */
	public Term getNeededAlphaConversion(String position) {
		Iterator<Term> termIt = root.getSubTerm(position).iterator();
		Term t;
		while(termIt.hasNext()) {
			t = termIt.next();
			if(checkPossibleBetaReduction(t) == EAvailability.ALPHANEEDED) {
				return t;
			}
		}
		return null;
	}
	/**
	 * Get all posibel betareductions
	 * @return A set of position Strings that identify Applications where a beta
	 *         reduction is possible.
	 */
	public Set<String> getPossibleBetaReductions(String position) {
		List<Term> allSubTerms = getAllChildren(position);
		Set<String> betaPossiblePositions = new HashSet<String>();
		for (Term t : allSubTerms) {
			if (checkPossibleBetaReduction(t) == EAvailability.AVAILABLE) {
				betaPossiblePositions.add(t.getPosition());
			}
		}
		return betaPossiblePositions;
	}
	/**
	 * Get one posibel betareduction
	 * @return position Strings that identify Applications where a beta
	 *         reduction is possible.
	 */
	public String getPossibleBetaReduction(String position) {
		TermIterator termIt = (TermIterator) root.getSubTerm(position).iterator();
		String betaPos;
		while(termIt.hasNext()) {
			betaPos = termIt.nextPosition();
			if(checkPossibleBetaReduction(betaPos) == EAvailability.AVAILABLE) {
				return betaPos;
			}
		}
		return null;
	}

	/**
	 * Get subterm of the root term
	 * @param pos
	 *            Positionstring l = linker Teilterm r = rechter Teilterm
	 * @return subterm for position
	 */
	public Term getSubTerm(String position) {
		return root.getSubTerm(position);

	}

	/**
	 * @param position
	 *            Positionstring
	 * @param highligh
	 *            true if posible betareduction should be highlighted
	 */
	public void highlightPossibleBetaReductions(String position,
			boolean highligh) {
		// TODO Auto-generated method stub

	}

	/**
	 * MUSS NOCH PRIVAT WERDEN IN FINALER VERSION relace subterm at position
	 * 
	 * @param pos
	 *            Positionstring l = linker Teilterm r = rechter Teilterm
	 * @param newTerm
	 *            new subterm for position
	 */
	public boolean replaceSubTerm(String position, Term newterm) {
		if (position.equals("")) {
			root = newterm;
			// root.setParent(null);
			root.recalculateParentsRecursive(null);
			root.recalculateBindingIDs();
			setChanged();
			notifyObservers(EMessageType.OUTPUTSTRING_CHANGED);
			return true;
		} else {
			Term newT = newterm/* .clone() */;
			// Term parentTerm = getSubTerm(position).parent;
			// newT.setParent(parentTerm);
			if (root.replaceChild(position, newT)) {
				root.recalculateParentsRecursive(null);
				root.recalculateBindingIDs();
				setChanged();
				notifyObservers(EMessageType.OUTPUTSTRING_CHANGED);
				return true;
			} else {
				return false;
			}

		}
	}

	/**
	 * This method calculates the free variables.
	 * 
	 * @return returns set of free Variables
	 */
	public Set<Atom> getFreeVars(String position) {

		return getSubTerm(position).getFreeVars();
	}

	/**
	 * This method calculates the free variables. 
	 * @ return returns set of bound Variables
	 */
	public Set<Atom> getBoundVars(String position) {
		return getSubTerm(position).getBoundVars();
	}

	// /**
	// * @return returns set of free Variables at position
	// */
	// public Set<Atom> getFreeVars() {
	//
	// return root.getFreeVars();
	// }

	// /**
	// * @return returns set of free Variables at position
	// * @param position
	// * of subterm
	// */
	// public Set<Atom> getFreeVarsSubTerm(String position) {
	// return root.getSubTerm(position).getFreeVars(new HashSet<Atom>());
	// }

	// /**
	// * @return returns set of bound Variables
	// */
	// public Set<Atom> getBoundVars() {
	// return root.getBoundVars();
	// }

	// /**
	// * @return returns set of bound Variables
	// * @param position
	// * of subterm
	// */
	// public Set<Atom> getBoundVarsSubTerm(String position) {
	// return root.getSubTerm(position).getBoundVars();
	// }

	/**
	 * Returns root term without format
	 * @return String represents a lambaterm without format
	 */
	public String toString() {
		return root.toString();
	}

	/**
	 * Return all used variables 
	 * @return Set of all variables which will be used in the current term
	 */
	public Set<String> getAllUsedVars() {
		Set<String> usedvars = new HashSet<String>();
		Set<Atom> atoms = new HashSet<Atom>();
		atoms.addAll(root.getBoundVars());
		atoms.addAll(root.getFreeVars());
		for (Atom a : atoms) {
			usedvars.add(a.toString());
		}
		return usedvars;
	}

	/**
	 * Return all unused variables 
	 * @return Set of all variables which will be not used in the current term
	 */
	public Set<String> getAllUnusedVars() {
		Set<String> unusedvars = new HashSet<String>();
		//Auskommentierte zeilen für variablen x1...xn
		//int varIndex = -1;
		//while(unusedvars.isEmpty()) {
			for (int i = 107; i < 123; i++) {
				String varname = new Character((char) i).toString();
				/*if(varIndex >= 0) {
					varname += intToSubscriptNumber(varIndex);
				}*/
				unusedvars.add(varname);
			}
			unusedvars.removeAll(getAllUsedVars());
		//}
		return unusedvars;
	}
	
	/*
	//helper for getAllUnusedVars
	//creates subscript string out of an int
	private String intToSubscriptNumber(int varIndex) throws RuntimeException{
		String s = String.valueOf(varIndex);
		String subscript = "";
		for(int i = 0; i<s.length(); i++) {
			switch(s.charAt(i)) {
				case '0':
					subscript += "\u2080";
					break;
				case '1':
					subscript += "\u2081";
					break;
				case '2':
					subscript += "\u2082";
					break;
				case '3':
					subscript += "\u2083";
					break;
				case '4':
					subscript += "\u2084";
					break;
				case '5':
					subscript += "\u2085";
					break;
				case '6':
					subscript += "\u2086";
					break;
				case '7':
					subscript += "\u2087";
					break;
				case '8':
					subscript += "\u2088";
					break;
				case '9':
					subscript += "\u2089";
					break;
				default:
					throw new RuntimeException("String number contained non number character.");
			}
		}
		return subscript;
	}
	*/
	
	//@Override
	/**
	 * This Method return all subterms of the current term include the current
	 * term
	 * 
	 * @return List of all Children
	 */
	public List<Term> getAllChildren(String position) {
		List<Term> children = new LinkedList<Term>();
		children.add(getSubTerm(position));
		children.addAll(getSubTerm(position).getAllChildren());
		return children;
	}
	
	public boolean simplifyAShortcut(String position) {
		TermIterator termIt = getSubTerm(position).iterator();
		while(termIt.hasNext()) {
			String pos = termIt.getCurrentPos();
			Term t = termIt.next();
			if(t.getTermType() == ETermType.APPLICATION) {
				Term lterm = t.getSubTerm("l");
				Term rterm = t.getSubTerm("r");
				if(	lterm.getTermType() == ETermType.SHORTCUT &&
					rterm.getTermType() == ETermType.SHORTCUT) {
					if(lterm.toString().equals("<identity>")) {
						return replaceSubTerm(pos, lterm);
					}
					int number = ((Shortcut)rterm).getNumber();
					if(number >= 0) {
						if(lterm.toString().equals("<pred>")) {
							Term newTerm = ShortcutHandler.getInstance().createNumberShortcut(Math.max(0, number-1));
							return replaceSubTerm(pos, newTerm);
						}
						if(lterm.toString().equals("<succ>")) {
							Term newTerm = ShortcutHandler.getInstance().createNumberShortcut(Math.max(0, number+1));
							return replaceSubTerm(pos, newTerm);
						}
						if(lterm.toString().equals("<fac>")) {
							int fac = number;
							while(number > 1) {
								number = number-1;
								fac = fac*number;
							}
							Term newTerm = ShortcutHandler.getInstance().createNumberShortcut(fac);
							return replaceSubTerm(pos, newTerm);
						}
						if(lterm.toString().equals("<iszero>")) {
							Term newTerm;
							if(number == 0)
								newTerm = ShortcutHandler.getInstance().createShortcutObject("<true>");
							else
								newTerm = ShortcutHandler.getInstance().createNumberShortcut(0);
							return replaceSubTerm(pos, newTerm);
						}
					}
				}	
				if(	lterm.getTermType() == ETermType.APPLICATION &&
					rterm.getTermType() == ETermType.SHORTCUT) {
					Term llterm = lterm.getSubTerm("l");
					Term lrterm = lterm.getSubTerm("r");
					if(	llterm.getTermType() == ETermType.SHORTCUT &&
						lrterm.getTermType() == ETermType.SHORTCUT) {
						int number1 = ((Shortcut)lrterm).getNumber();
						int number2 = ((Shortcut)rterm).getNumber();
						if(number1 >= 0 && number2 >= 0) {
							if(llterm.toString().equals("<mult>")) {
								Term newTerm = ShortcutHandler.getInstance().createNumberShortcut(number1*number2);
								return replaceSubTerm(pos, newTerm);
							}
							else if(llterm.toString().equals("<add>")) {
								Term newTerm = ShortcutHandler.getInstance().createNumberShortcut(number1+number2);
								return replaceSubTerm(pos, newTerm);
							}
						}
					}
					if(	llterm.getTermType() == ETermType.APPLICATION &&
						lrterm.getTermType() == ETermType.SHORTCUT) {
						Term lllterm = llterm.getSubTerm("l");
						Term llrterm = llterm.getSubTerm("r");
						if(	lllterm.getTermType() == ETermType.SHORTCUT &&
							llrterm.getTermType() == ETermType.SHORTCUT) {
							if(lllterm.toString().equals("<ite>")) {
								if(	llrterm.toString().equals("<true>")||
									llrterm.toString().equals("<1>")) {
									return replaceSubTerm(pos, lrterm);
								}
								else if(llrterm.toString().equals("<false>")||
										llrterm.toString().equals("<0>")) {
									return replaceSubTerm(pos, rterm);
								}
							}
						}
					}
				}
			}
		}
		return false;
	}


}
