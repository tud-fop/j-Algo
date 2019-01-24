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

package org.jalgo.module.ebnf.model.syndia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jalgo.main.util.Messages;

/**
 * The class <code>Concatenation</code> administrates a list of syntax diagram
 * elements, it consists of. All syntax diagram elements except a concatenation
 * can be added and removed. <code>Concatenation</code> has only 2 elements if
 * the syntax diagram is in strict mode.
 * 
 * @author Michael Thiele
 */
public class Concatenation extends SynDiaElem {

	private static final long serialVersionUID = 1L;

	private List<SynDiaElem> synDiaElem;

	private int line; // number of the current concatenation

	private static int lines = 1; // to have unique line numbers

	private boolean filledWithNullElems;

	/**
	 * Constructs a <code>Concatenation</code> by saving its parent element
	 * and initializing the list of syntax diagram elements.
	 * 
	 * @param parent
	 *            parent of the element
	 * @param mySyntaxDiagram
	 *            the syntax diagram this element belongs to
	 */
	public Concatenation(SynDiaElem parent, SyntaxDiagram mySyntaxDiagram) {
		super(parent, mySyntaxDiagram);
		synDiaElem = new LinkedList<SynDiaElem>();
		line = lines++; // line is a unique number
		// automatically insert a NullElem, so that other SynDiaElems can be
		// inserted
		synDiaElem.add(0, new NullElem(this, line, 0, mySyntaxDiagram));
		filledWithNullElems = true;
	}

	/**
	 * @return number of elements in the concatenation
	 */
	public int getNumberOfElems() {
		return synDiaElem.size();
	}

	/**
	 * Returns an element the concatenation consists of.
	 * 
	 * @param index
	 *            index of the syntax diagram element
	 * @return the syntax diagram element at position <code>index</code>
	 * @throws IndexOutOfBoundsException
	 */
	public SynDiaElem getSynDiaElem(int index) throws IndexOutOfBoundsException {
		return synDiaElem.get(index);
	}

	/**
	 * Inserts a syntax diagram element at the end of the concatenation. No new
	 * element is created!
	 * 
	 * @param s
	 *            the <code>SynDiaElem</code> to add
	 */
	private void insert(SynDiaElem s) {
		synDiaElem.add(s);
	}

	/**
	 * @param index
	 *            index in the synDiaElem list
	 * @param s
	 *            the <code>SynDiaElem</code> to include
	 * @throws NoNullElemException
	 *             Is thrown if index does not point at a NullElem.
	 */
	private void addSynDiaElem(int index, SynDiaElem s)
			throws NoNullElemException {
		if (!(synDiaElem.get(index) instanceof NullElem)) {
			throw new NoNullElemException(Messages.getString("ebnf",
					"SynDiaModel.NoNullElemException")
					+ ". index= " + index);
		}
		// the old NullElem is NOT shifted to the right
		synDiaElem.add(index + 1, s);
		// all NullElems to the right have to get new index numbers
		for (int i = index + 3; i < synDiaElem.size(); i = i + 2) {
			// setIndex(i + 1) because at the end a new NullElem will be
			// added left of this NullElem
			((NullElem) synDiaElem.get(i)).setIndex(i + 1);
		}
		// a new NullElem right of the SynDiaElem
		synDiaElem.add(index + 2, new NullElem(this, line, index + 2,
				getMySyntaxDiagram()));
		getMySyntaxDiagram().getMySynDiaSystem().changed();
	}

	/**
	 * Adds a terminal symbol from the <code>undoElemStack</code> to the
	 * concatenation.
	 * 
	 * @param index
	 *            index in the synDiaElem list
	 * @throws NoNullElemException
	 *             Is thrown if index does not point at a NullElem.
	 */
	public void redoAddTerminal(int index) throws NoNullElemException {
		TerminalSymbol t = (TerminalSymbol) getMySyntaxDiagram()
				.getMySynDiaSystem().getUndoElemStack().pop();
		addSynDiaElem(index, t);
		t.getMySyntaxDiagram().getMySynDiaSystem().addTerminal(t.getLabel());
	}

	/**
	 * Adds a terminal symbol from the <code>deleteElemStack</code> to the
	 * concatenation.
	 * 
	 * @param index
	 *            index in the synDiaElem list
	 * @throws NoNullElemException
	 *             Is thrown if index does not point at a NullElem.
	 */
	public void undoDeleteTerminal(int index) throws NoNullElemException {
		TerminalSymbol t = (TerminalSymbol) getMySyntaxDiagram()
				.getMySynDiaSystem().getDeleteElemStack().pop();
		addSynDiaElem(index, t);
		t.getMySyntaxDiagram().getMySynDiaSystem().addTerminal(t.getLabel());
	}

	/**
	 * Adds a terminal symbol to the concatenation.
	 * 
	 * @param index
	 *            index in the synDiaElem list
	 * @param label
	 *            label of the terminal symbol
	 * @throws NoNullElemException
	 *             Is thrown if index does not point at a NullElem.
	 */
	public void addTerminalSymbol(int index, String label)
			throws NoNullElemException {
		TerminalSymbol t = new TerminalSymbol(this, label, getMySyntaxDiagram());
		addSynDiaElem(index, t);
		t.getMySyntaxDiagram().getMySynDiaSystem().addTerminal(t.getLabel());
	}

	/**
	 * Adds a variable from the <code>undoElemStack</code> to the
	 * concatenation.<br>
	 * <b>IMPORTANT</b>: If this concatenation is part of a syntax diagram
	 * system, you have to call SynDiaSystem.addVariable(retValueOfThisFunction)
	 * after calling this function!
	 * 
	 * @param index
	 *            index in the synDiaElem list
	 * @return the variable
	 * @throws NoNullElemException
	 *             Is thrown if index does not point at a NullElem.
	 */
	public Variable redoAddVariable(int index) throws NoNullElemException {
		Variable v = (Variable) getMySyntaxDiagram().getMySynDiaSystem()
				.getUndoElemStack().pop();
		addSynDiaElem(index, v);
		return v;
	}

	/**
	 * Adds a variable from the <code>deleteElemStack</code> to the
	 * concatenation.<br>
	 * <b>IMPORTANT</b>: If this concatenation is part of a syntax diagram
	 * system, you have to call SynDiaSystem.addVariable(retValueOfThisFunction)
	 * after calling this function!
	 * 
	 * @param index
	 *            index in the synDiaElem list
	 * @return the variable
	 * @throws NoNullElemException
	 *             Is thrown if index does not point at a NullElem.
	 */
	public Variable undoDeleteVariable(int index) throws NoNullElemException {
		Variable v = (Variable) getMySyntaxDiagram().getMySynDiaSystem()
				.getDeleteElemStack().pop();
		addSynDiaElem(index, v);
		return v;
	}

	/**
	 * Adds a variable to the concatenation.<br>
	 * <b>IMPORTANT</b>: If this concatenation is part of a syntax diagram
	 * system, you have to call SynDiaSystem.addVariable(retValueOfThisFunction)
	 * after calling this function!
	 * 
	 * @param index
	 *            index in the synDiaElem list
	 * @param label
	 *            label of the variable
	 * @return the new variable
	 * @throws NoNullElemException
	 *             Is thrown if index does not point at a NullElem.
	 */
	public Variable addVariable(int index, String label)
			throws NoNullElemException {
		Variable v = new Variable(this, label, getMySyntaxDiagram());
		addSynDiaElem(index, v);
		return v;
	}

	private List<SynDiaElem> addBranch(Branch b, int begin, int end)
			throws NoNullElemException {
		// end = begin + 2*n
		if (begin > end || (begin != end && (end - begin) % 2 != 0)) {
			throw new IllegalArgumentException(Messages.getString("ebnf",
					"SynDiaModel.BeginEndException: " + begin + ", " + end));
		}
		if (!(synDiaElem.get(begin) instanceof NullElem)
				|| !(synDiaElem.get(end) instanceof NullElem)) {
			throw new NoNullElemException(Messages.getString("ebnf",
					"SynDiaModel.NoNullElemException"));
		}
		List<SynDiaElem> retList = new ArrayList<SynDiaElem>();
		// if there is something to copy
		if (begin != end) {
			// start with begin+1, because Branch already has NullElem
			int _begin = begin + 1;
			for (int i = _begin; i <= end; i++) {
				// set new line and index for NullElems
				if (synDiaElem.get(_begin) instanceof NullElem) {
					((NullElem) synDiaElem.get(_begin))
							.setLine(b.getLeft().line);
					// i - begin, because there is already a NullElem in
					// this concatenation
					((NullElem) synDiaElem.get(_begin)).setIndex(i - begin);
				}
				// the left side of the branch has to be new parent
				synDiaElem.get(_begin).setParent(b.getLeft());
				b.getLeft().insert(synDiaElem.get(_begin));
				retList.add(synDiaElem.get(_begin));
				// delete copied elements
				synDiaElem.remove(_begin);
			}
		}
		// now add branch
		addSynDiaElem(begin, b);
		return retList;
	}

	/**
	 * Adds a branch from the <code>undoElemStack</code> to the concatenation.
	 * 
	 * @param begin
	 *            begin index in the synDiaElem list
	 * @param end
	 *            end index in the synDiaElem list
	 * @return list with all contained syntax diagram elements
	 * @throws NoNullElemException
	 *             Is thrown if begin does not point at a NullElem.
	 */
	public List<SynDiaElem> redoAddBranch(int begin, int end)
			throws NoNullElemException {
		Branch b = (Branch) getMySyntaxDiagram().getMySynDiaSystem()
				.getUndoElemStack().pop();
		return addBranch(b, begin, end);
	}

	/**
	 * Adds a branch from the <code>undoElemStack</code> to the concatenation.
	 * 
	 * @param begin
	 *            begin index in the synDiaElem list
	 * @param end
	 *            end index in the synDiaElem list
	 * @throws NoNullElemException
	 *             Is thrown if begin does not point at a NullElem.
	 */
	public void undoDeleteBranch(int begin, int end) throws NoNullElemException {
		Branch b = (Branch) getMySyntaxDiagram().getMySynDiaSystem()
				.getDeleteElemStack().pop();
		addBranch(b, begin, end);
		// search for elements on the right side, that have wrong line and index
		// numbers
		Concatenation rightPath = b.getRight();
		for (int i = 0; i < rightPath.getNumberOfElems(); i++) {
			rightPath.getSynDiaElem(i).setParent(rightPath);
			if (rightPath.getSynDiaElem(i) instanceof NullElem) {
				((NullElem) rightPath.getSynDiaElem(i))
						.setLine(b.getRight().line);
				((NullElem) rightPath.getSynDiaElem(i)).setIndex(i);
			}
		}
	}

	/**
	 * Adds a branch to the concatenation.
	 * 
	 * @param begin
	 *            begin index in the synDiaElem list
	 * @param end
	 *            end index in the synDiaElem list
	 * @return list with all contained syntax diagram elements
	 * @throws NoNullElemException
	 *             Is thrown if begin does not point at a NullElem.
	 * @throws IllegalArgumentException
	 *             Is thrown if begin > end.
	 */
	public List<SynDiaElem> addBranch(int begin, int end)
			throws NoNullElemException, IllegalArgumentException {
		Branch b = new Branch(this, getMySyntaxDiagram());
		return addBranch(b, begin, end);
	}

	private List<SynDiaElem> addRepetition(Repetition r, int begin, int end)
			throws NoNullElemException, IllegalArgumentException {
		// end = begin + 2*n
		if (begin > end || (begin != end && (end - begin) % 2 != 0)) {
			throw new IllegalArgumentException(Messages.getString("ebnf",
					"SynDiaModel.BeginEndException: " + begin + ", " + end));
		}
		if (!(synDiaElem.get(begin) instanceof NullElem)
				|| !(synDiaElem.get(end) instanceof NullElem)) {
			throw new NoNullElemException(Messages.getString("ebnf",
					"SynDiaModel.NoNullElemException"));
		}
		List<SynDiaElem> retList = new ArrayList<SynDiaElem>();
		// if there is something to copy
		if (begin != end) {
			// start with begin+1, because repetition already has NullElem
			int _begin = begin + 1;
			for (int i = _begin; i <= end; i++) {
				// the left side of the repetition has to be new parent
				synDiaElem.get(_begin).setParent(r.getLeft());
				r.getLeft().synDiaElem.add(0, (synDiaElem.get(_begin)));
				retList.add(synDiaElem.get(_begin));
				// delete copied elements
				synDiaElem.remove(_begin);
			}
			// set new line and index for NullElems
			Concatenation leftPath = r.getLeft();
			for (int i = 0; i < leftPath.getNumberOfElems(); i++) {
				if (leftPath.getSynDiaElem(i) instanceof NullElem) {
					((NullElem) leftPath.getSynDiaElem(i))
							.setLine(leftPath.line);
					((NullElem) leftPath.getSynDiaElem(i)).setIndex(i);
				}
			}
		}
		addSynDiaElem(begin, r);
		return retList;
	}

	/**
	 * Adds a new repetition to the concatenation.
	 * 
	 * @param begin
	 *            begin index in the synDiaElem list
	 * @param end
	 *            end index in the synDiaElem list
	 * @return list with all contained syntax diagram elements
	 * @throws NoNullElemException
	 *             Is thrown if begin does not point at a NullElem.
	 * @throws IllegalArgumentException
	 *             Is thrown if begin > end.
	 */
	public List<SynDiaElem> addRepetition(int begin, int end)
			throws IllegalArgumentException, NoNullElemException {
		Repetition r = new Repetition(this, getMySyntaxDiagram());
		return addRepetition(r, begin, end);
	}

	/**
	 * Adds a repetition from the <code>undoElemStack</code> to the
	 * concatenation.
	 * 
	 * @param begin
	 *            begin index in the synDiaElem list
	 * @param end
	 *            end index in the synDiaElem list
	 * @return list with all contained syntax diagram elements
	 * @throws NoNullElemException
	 *             Is thrown if begin does not point at a NullElem.
	 * @throws IllegalArgumentException
	 *             Is thrown if begin > end.
	 */
	public List<SynDiaElem> redoAddRepetition(int begin, int end)
			throws IllegalArgumentException, NoNullElemException {
		Repetition r = (Repetition) getMySyntaxDiagram().getMySynDiaSystem()
				.getUndoElemStack().pop();
		return addRepetition(r, begin, end);
	}

	/**
	 * Adds a repetition from the <code>deleteElemStack</code> to the
	 * concatenation.
	 * 
	 * @param begin
	 *            begin index in the synDiaElem list
	 * @param end
	 *            end index in the synDiaElem list
	 * @throws NoNullElemException
	 *             Is thrown if begin does not point at a NullElem.
	 * @throws IllegalArgumentException
	 *             Is thrown if begin > end.
	 */
	public void undoDeleteRepetition(int begin, int end)
			throws NoNullElemException {
		Repetition r = (Repetition) getMySyntaxDiagram().getMySynDiaSystem()
				.getDeleteElemStack().pop();
		addRepetition(r, begin, end);
		// search for elements on the right side, that have wrong line and index
		// numbers
		Concatenation rightPath = r.getRight();
		for (int i = 0; i < rightPath.getNumberOfElems(); i++) {
			rightPath.getSynDiaElem(i).setParent(rightPath);
			if (rightPath.getSynDiaElem(i) instanceof NullElem) {
				((NullElem) rightPath.getSynDiaElem(i))
						.setLine(r.getRight().line);
				((NullElem) rightPath.getSynDiaElem(i)).setIndex(i);
			}
		}
	}

	/**
	 * Adds a manual word wrap to the concatenation.
	 * 
	 * @param index
	 *            index in the synDiaElem list
	 * @throws NoNullElemException
	 *             Is thrown if index does not point at a NullElem.
	 */
	public void addWordWrap(int index) throws NoNullElemException {
		WordWrap w = new WordWrap(this, getMySyntaxDiagram());
		addSynDiaElem(index, w);
	}

	/**
	 * Fills the <code>Concatenation</code> with <code>NullElems</code>.<br>
	 * The function is assumed that either there are no NullElems or all
	 * NullElems in the concatenation.
	 * 
	 * @return <code>true</code> if <code>NullElem</code>s were added,
	 *         <code>false</code> if nothing had to be added
	 */
	public boolean fillWithNullElems() {
		if (filledWithNullElems == true) {
			return false;
		}
		// test, if there are already NullElems in the concatenation
		if (synDiaElem.size() == 0) {
			synDiaElem
					.add(0, new NullElem(this, line, 0, getMySyntaxDiagram()));
			filledWithNullElems = true;
			return true;
		}
		boolean retVal = false;
		// list with positions, where a NullElem has to be added
		List<Integer> fillList = new LinkedList<Integer>();
		SynDiaElem currentElem;
		for (int i = 0; i < synDiaElem.size(); i++) {
			currentElem = synDiaElem.get(i);
			// Branches and Repetitions have own concatenations --> fill them!
			if (currentElem instanceof Branch) {
				retVal = ((Branch) currentElem).fillWithNullElems();
			} else if (currentElem instanceof Repetition) {
				retVal = ((Repetition) currentElem).fillWithNullElems();
			}
			// all elements that are no NullElem will get a NullElem in front
			if (!(currentElem instanceof NullElem)) {
				retVal = true;
				fillList.add(i * 2); // fill every 2nd gap
			}
		}
		// now fill with NullElems ...
		for (int i = 0; i < fillList.size(); i++) {
			synDiaElem.add(fillList.get(i), new NullElem(this, line, fillList
					.get(i), getMySyntaxDiagram()));
		}
		// ... don't forget the last element
		synDiaElem.add(new NullElem(this, line, synDiaElem.size(),
				getMySyntaxDiagram()));
		filledWithNullElems = true;
		// update the view
		getMySyntaxDiagram().getMySynDiaSystem().changed();
		return retVal;
	}

	/**
	 * Removes all <code>NullElem</code>s in the concatenation.
	 * 
	 * @return <code>true</code> if <code>NullElem</code>s were removed,
	 *         <code>false</code> if nothing had to be removed
	 */
	public boolean removeNullElems() {
		if (filledWithNullElems == false) {
			return false;
		}
		boolean retVal = false;
		List<SynDiaElem> removeList = new LinkedList<SynDiaElem>();
		for (SynDiaElem e : synDiaElem) {
			if (e instanceof Branch) {
				retVal = ((Branch) e).removeNullElems();
			} else {
				if (e instanceof Repetition) {
					retVal = ((Repetition) e).removeNullElems();
				} else {
					// if it is a NullElem add the element to the removeList
					if (e instanceof NullElem) {
						retVal = true;
						removeList.add(e);
					}
				}
			}
		}
		// remove all elements, that are in removeList
		synDiaElem.removeAll(removeList);
		filledWithNullElems = false;
		// update the view
		getMySyntaxDiagram().getMySynDiaSystem().changed();
		return retVal;
	}

	/**
	 * Removes all elements except the first <code>NullElem</code> of the
	 * concatenation.
	 * 
	 */
	public void removeAllElements() {
		synDiaElem.subList(1, synDiaElem.size()).clear();
	}

	/**
	 * Removes a syntax diagram element from the concatenation.
	 * 
	 * @param index
	 *            index of the element to remove
	 * @param undoElemStack
	 *            <code>true</code> if the element has to be put on the
	 *            undoElemStack
	 * @param deleteStack
	 *            <code>true</code> if the element has to be put on the
	 *            deleteStack
	 * @return the <code>NullElem</code> before
	 */
	private NullElem removeSynDiaElem(int index, boolean undoElemStack,
			boolean deleteStack) {
		if (undoElemStack) {
			getMySyntaxDiagram().getMySynDiaSystem().getUndoElemStack().push(
					synDiaElem.get(index));
		}
		if (deleteStack) {
			getMySyntaxDiagram().getMySynDiaSystem().getDeleteElemStack().push(
					synDiaElem.get(index));
		}
		// save the NullElem BEFORE the deleted elem (for re-inserts)
		NullElem retNullElem = (NullElem) synDiaElem.get(index - 1);
		// push deleted element on the synDiaElemStack
		synDiaElem.remove(index);
		// the next element is a NullElem - delete it too
		synDiaElem.remove(index);
		// now update all indices of the next NullElems in the concatenation
		// index + 1 --> next element surely is no NullElem
		for (int i = index + 1; i < synDiaElem.size(); i++) {
			if (synDiaElem.get(i) instanceof NullElem) {
				((NullElem) synDiaElem.get(i)).setIndex(i);
			}
		}
		getMySyntaxDiagram().getMySynDiaSystem().changed();
		return retNullElem;
	}

	/**
	 * Removes a <code>SynDiaElem</code> of the concatenation by an given
	 * <code>SynDiaElem</code>
	 * 
	 * @param s
	 *            the <code>SynDiaElem</code> to remove
	 * @return the <code>NullElem</code> before
	 * @throws ElementNotFoundException
	 *             Is thrown if the element is not part of the concatenation
	 */
	private NullElem removeSynDiaElem(SynDiaElem s, boolean undoElemStack,
			boolean deleteStack) throws ElementNotFoundException {
		for (int i = 0; i < synDiaElem.size(); i++) {
			if (synDiaElem.get(i) == s) {
				return removeSynDiaElem(i, undoElemStack, deleteStack);
			}
		}
		throw new ElementNotFoundException(Messages.getString("ebnf",
				"SynDiaModel.ElementNotFoundException"));
	}

	/**
	 * Removes a syntax diagram element that is neither pushed on the <code>
	 * undoElemStack</code>
	 * nor <code>deleteElemStack</code>.
	 * 
	 * @param sde
	 *            the sde to delete
	 * @throws ElementNotFoundException
	 *             is thrown if the given element is no part of the
	 *             concatenation.
	 */
	public void removeSynDiaElemCompletely(SynDiaElem sde)
			throws ElementNotFoundException {
		removeSynDiaElem(sde, false, false);
	}

	/**
	 * Removes a terminal symbol from the concatenation and pushes it on the
	 * <code>deleteElemStack</code>.
	 * 
	 * @param t
	 *            a reference to the terminal symbol to remove
	 * @return the <code>NullElem</code> before
	 * @throws ElementNotFoundException
	 *             Is thrown if the terminal symbol is not part of the
	 *             concatenation
	 */
	public NullElem deleteTerminal(TerminalSymbol t)
			throws ElementNotFoundException {
		t.getMySyntaxDiagram().getMySynDiaSystem().removeTerminal(t.getLabel());
		return removeSynDiaElem(t, false, true);

	}

	/**
	 * Removes a terminal symbol from the concatenation.
	 * 
	 * @param t
	 *            a reference to the terminal symbol to remove
	 * @return the <code>NullElem</code> before
	 * @throws ElementNotFoundException
	 *             Is thrown if the terminal symbol is not part of the
	 *             concatenation
	 */
	public NullElem removeTerminalSymbol(TerminalSymbol t)
			throws ElementNotFoundException {
		t.getMySyntaxDiagram().getMySynDiaSystem().removeTerminal(t.getLabel());
		return removeSynDiaElem(t, true, false);
	}

	/**
	 * Removes a terminal symbol from the concatenation.
	 * 
	 * @param index
	 *            index of the terminal symbol to remove
	 * @return the <code>NullElem</code> before
	 * @throws ElementNotFoundException
	 *             Is thrown if element at index is not of type
	 *             <code>TerminalSymbol</code>
	 */
	public NullElem removeTerminalSymbol(int index)
			throws ElementNotFoundException {
		if (!(synDiaElem.get(index) instanceof TerminalSymbol)) {
			throw new ElementNotFoundException(Messages.getString("ebnf",
					"SynDiaModel.ElementNotFoundException"));
		}
		TerminalSymbol t = (TerminalSymbol) (synDiaElem.get(index));
		t.getMySyntaxDiagram().getMySynDiaSystem().removeTerminal(t.getLabel());
		return removeSynDiaElem(index, true, false);
	}

	/**
	 * Removes a variable from the concatenation and pushes it on the
	 * <code>deleteElemStack</code>.<br>
	 * <b>IMPORTANT</b>: If this concatenation is part of a syntax diagram
	 * system, you have to call SynDiaSystem.removeVariable(removedVariable)
	 * after calling this function!
	 * 
	 * @param v
	 *            reference to the variable to remove
	 * @return the <code>NullElem</code> before
	 * @throws ElementNotFoundException
	 *             Is thrown if the variable is not part of the concatenation
	 */
	public NullElem deleteVariable(Variable v) throws ElementNotFoundException {
		return removeSynDiaElem(v, false, true);
	}

	/**
	 * Removes a variable from the concatenation.<br>
	 * <b>IMPORTANT</b>: If this concatenation is part of a syntax diagram
	 * system, you have to call SynDiaSystem.removeVariable(removedVariable)
	 * after calling this function!
	 * 
	 * @param v
	 *            reference to the variable to remove
	 * @return the <code>NullElem</code> before
	 * @throws ElementNotFoundException
	 *             Is thrown if the variable is not part of the concatenation
	 */
	public NullElem removeVariable(Variable v) throws ElementNotFoundException {
		return removeSynDiaElem(v, true, false);
	}

	/**
	 * Removes a variable from the concatenation.<br>
	 * <b>IMPORTANT</b>: If this concatenation is part of a syntax diagram
	 * system, you have to call SynDiaSystem.removeVariable(removedVariable)
	 * after calling this function!
	 * 
	 * @param index
	 *            index of the variable to remove
	 * @return the <code>NullElem</code> before
	 * @throws ElementNotFoundException
	 *             Is thrown if element at index is not of type
	 *             <code>Variable</code>
	 */
	public NullElem removeVariable(int index) throws ElementNotFoundException {
		if (!(synDiaElem.get(index) instanceof Variable)) {
			throw new ElementNotFoundException(Messages.getString("ebnf",
					"SynDiaModel.ElementNotFoundException"));
		}
		return removeSynDiaElem(index, true, false);
	}

	private void copyPath(Concatenation path, int index) {
		for (int i = 1; i < path.getNumberOfElems(); i++) {
			SynDiaElem leftPathSde = path.getSynDiaElem(i);
			path.getSynDiaElem(i).setParent(this);
			// set new line and index for NullElems
			if (leftPathSde instanceof NullElem) {
				((NullElem) leftPathSde).setLine(line);
			}
			synDiaElem.add(index + i - 1, leftPathSde);
		}
		// if it is the left path, update all NullElems of the concatenation
		for (int i = 0; i < synDiaElem.size(); i++) {
			if (synDiaElem.get(i) instanceof NullElem) {
				((NullElem) synDiaElem.get(i)).setIndex(i);
			}
		}
	}

	/**
	 * Removes a repetition from the concatenation and pushes it on the
	 * <code>deleteElemStack</code>.
	 * 
	 * @param r
	 *            reference to the repetition to remove
	 * @param left
	 *            <code>true</code> if you want to preserve left path
	 * @param right
	 *            <code>false</code> if you want to preserve right path
	 * @return the <code>NullElem</code> before
	 * @throws ElementNotFoundException
	 *             Is thrown if the repetition is not part of the concatenation
	 * @throws IllegalArgumentException
	 *             Is thrown if <code>right</code> and <code>left</code> are
	 *             both true
	 */
	public NullElem deleteRepetition(Repetition r, boolean left, boolean right)
			throws IllegalArgumentException, ElementNotFoundException {
		NullElem retNullElem = removeRepetition(r, left, right, true);
		return retNullElem;
	}

	/**
	 * Removes a repetition from the concatenation.
	 * 
	 * @param r
	 *            reference to the repetition to remove
	 * @param left
	 *            <code>true</code> if you want to preserve left path
	 * @param right
	 *            <code>false</code> if you want to preserve right path
	 * @param delete
	 *            <code>true</code> if the element will be put on delete stack
	 * @return the <code>NullElem</code> before
	 * @throws ElementNotFoundException
	 *             Is thrown if the repetition is not part of the concatenation
	 * @throws IllegalArgumentException
	 *             Is thrown if <code>right</code> and <code>left</code> are
	 *             both true
	 */
	public NullElem removeRepetition(Repetition r, boolean left, boolean right,
			boolean delete) throws ElementNotFoundException,
			IllegalArgumentException {
		for (int i = 0; i < synDiaElem.size(); i++) {
			if (synDiaElem.get(i) == r) {
				return removeRepetition(i, left, right, delete);
			}
		}
		throw new ElementNotFoundException(Messages.getString("ebnf",
				"SynDiaModel.ElementNotFoundException"));
	}

	/**
	 * Removes a repetition from the concatenation.
	 * 
	 * @param index
	 *            index of the repetition to remove
	 * @param left
	 *            <code>true</code> if you want to preserve left path
	 * @param right
	 *            <code>false</code> if you want to preserve right path
	 * @param delete
	 *            <code>true</code> if the element will be put on delete stack
	 * @return the <code>NullElem</code> before
	 * @throws ElementNotFoundException
	 *             Is thrown if element at index is not of type
	 *             <code>Repetition</code>
	 * @throws IllegalArgumentException
	 *             Is thrown if <code>right</code> and <code>left</code> are
	 *             both true
	 */
	public NullElem removeRepetition(int index, boolean left, boolean right,
			boolean delete) throws ElementNotFoundException,
			IllegalArgumentException {
		if (!(synDiaElem.get(index) instanceof Repetition)) {
			throw new ElementNotFoundException(Messages.getString("ebnf",
					"SynDiaModel.ElementNotFoundException"));
		}
		if (left && right) {
			throw new IllegalArgumentException(Messages.getString("ebnf",
					"SynDiaModel.LeftAndRightPathException"));
		}
		// preserve upper path
		Repetition currentRep = (Repetition) synDiaElem.get(index);
		if (left) {
			// save old left path
			Concatenation leftPath = currentRep.getLeft();
			// remove repetition
			NullElem retNullElem = removeSynDiaElem(index, !delete, delete);
			copyPath(leftPath, index);
			this.getMySyntaxDiagram().getMySynDiaSystem().changed();
			currentRep.getLeft().removeAllElements();
			return retNullElem;
		} else {
			// preserve lower path --> switch elements
			if (right) {
				// save old right path
				Concatenation rightPath = currentRep.getRight();
				// remove repetition
				NullElem retNullElem = removeSynDiaElem(index, !delete, delete);
				// insert old right path in reverse order in the current
				// concatenation
				copyPath(rightPath, index);
				this.getMySyntaxDiagram().getMySynDiaSystem().changed();
				return retNullElem;
			} else {
				// if nothing has to be saved, simply remove repetition
				currentRep.getLeft().removeAllElements();
				currentRep.getRight().removeAllElements();
				return removeSynDiaElem(index, !delete, delete);
			}
		}
	}

	/**
	 * Removes a branch from the concatenation and pushes it on the
	 * <code>deleteElemStack</code>.
	 * 
	 * @param b
	 *            reference to the branch to remove
	 * @param left
	 *            <code>true</code> if you want to preserve left path
	 * @param right
	 *            <code>false</code> if you want to preserve right path
	 * @return the <code>NullElem</code> before
	 * @throws ElementNotFoundException
	 *             Is thrown if the branch is not part of the concatenation
	 * @throws IllegalArgumentException
	 *             Is thrown if <code>right</code> and <code>left</code> are
	 *             both true
	 */
	public NullElem deleteBranch(Branch b, boolean left, boolean right)
			throws IllegalArgumentException, ElementNotFoundException {
		NullElem retNullElem = removeBranch(b, left, right, true);
		return retNullElem;
	}

	/**
	 * Removes a branch from the concatenation.
	 * 
	 * @param b
	 *            reference to the branch to remove
	 * @param left
	 *            <code>true</code> if you want to preserve left path
	 * @param right
	 *            <code>false</code> if you want to preserve right path
	 * @param delete
	 *            <code>true</code> if the element will be put on delete stack
	 * @return the <code>NullElem</code> before
	 * @throws ElementNotFoundException
	 *             Is thrown if the branch is not part of the concatenation
	 * @throws IllegalArgumentException
	 *             Is thrown if <code>right</code> and <code>left</code> are
	 *             both true
	 */
	public NullElem removeBranch(Branch b, boolean left, boolean right,
			boolean delete) throws ElementNotFoundException,
			IllegalArgumentException {
		for (int i = 0; i < synDiaElem.size(); i++) {
			if (synDiaElem.get(i) == b) {
				return removeBranch(i, left, right, delete);
			}
		}
		throw new ElementNotFoundException(Messages.getString("ebnf",
				"SynDiaModel.ElementNotFoundException"));
	}

	/**
	 * Removes a branch from the concatenation.
	 * 
	 * @param index
	 *            index of the branch to remove
	 * @param left
	 *            <code>true</code> if you want to preserve left path
	 * @param right
	 *            <code>false</code> if you want to preserve right path
	 * @param delete
	 *            <code>true</code> if the element will be put on delete stack
	 * @return the <code>NullElem</code> before
	 * @throws ElementNotFoundException
	 *             Is thrown if element at index is not of type
	 *             <code>Branch</code>
	 * @throws IllegalArgumentException
	 *             Is thrown if <code>right</code> and <code>left</code> are
	 *             both true
	 */
	public NullElem removeBranch(int index, boolean left, boolean right,
			boolean delete) throws ElementNotFoundException,
			IllegalArgumentException {
		if (!(synDiaElem.get(index) instanceof Branch)) {
			throw new ElementNotFoundException(Messages.getString("ebnf",
					"SynDiaModel.ElementNotFoundException"));
		}
		if (left && right) {
			throw new IllegalArgumentException(Messages.getString("ebnf",
					"SynDiaModel.LeftAndRightPathException"));
		}
		// see @removeRepetition()
		Branch currentBranch = (Branch) synDiaElem.get(index);
		if (left) {
			Concatenation leftPath = currentBranch.getLeft();
			NullElem retNullElem = removeSynDiaElem(index, !delete, delete);
			copyPath(leftPath, index);
			this.getMySyntaxDiagram().getMySynDiaSystem().changed();
			currentBranch.getLeft().removeAllElements();
			return retNullElem;
		} else {
			if (right) {
				Concatenation rightPath = currentBranch.getRight();
				NullElem retNullElem = removeSynDiaElem(index, !delete, delete);
				copyPath(rightPath, index);
				this.getMySyntaxDiagram().getMySynDiaSystem().changed();
				return retNullElem;
			} else {
				// if nothing has to be saved, simply remove branch
				currentBranch.getRight().removeAllElements();
				currentBranch.getLeft().removeAllElements();
				return removeSynDiaElem(index, !delete, delete);
			}
		}
	}

	/**
	 * Removes a word wrap from the concatenation.
	 * 
	 * @param w
	 *            reference to the word wrap to remove
	 * @return the <code>NullElem</code> before
	 * @throws ElementNotFoundException
	 *             Is thrown if the word wrap is not part of the concatenation
	 */
	public NullElem removeWordWrap(WordWrap w) throws ElementNotFoundException {
		return removeSynDiaElem(w, true, false);
	}

	/**
	 * Removes a manual word wrap from the concatenation.
	 * 
	 * @param index
	 *            index of the word wrap to remove
	 * @return the <code>NullElem</code> before
	 * @throws ElementNotFoundException
	 *             Is thrown if element at index is not of type
	 *             <code>WordWrap</code>
	 */
	public NullElem removeWordWrap(int index) throws ElementNotFoundException {
		if (!(synDiaElem.get(index) instanceof WordWrap)) {
			throw new ElementNotFoundException(Messages.getString("ebnf",
					"SynDiaModel.ElementNotFoundException"));
		}
		return removeSynDiaElem(index, true, false);
	}

	/**
	 * This function deletes all elements in the concatenation!
	 * 
	 */
	public void deleteAllElements() {
		synDiaElem.clear();
	}

	/**
	 * Returns <code>true</code> if concatenation is empty (NullElems do not
	 * count!).
	 * 
	 * @return <code>true</code> if concatenation is empty (NullElems do not
	 *         count!)
	 */
	public boolean isEmpty() {
		if (filledWithNullElems) {
			if (synDiaElem.size() == 1)
				return true;
			return false;
		} else {
			return synDiaElem.isEmpty();
		}
	}

	public String toString() {
		String retStr = "< ";
		for (int i = 0; i < synDiaElem.size(); i++) {
			retStr += synDiaElem.get(i).toString();
			// last String does not need a comma
			if (i < synDiaElem.size() - 1) {
				retStr += ", ";
			}
		}
		return retStr + " > ";
	}

	/**
	 * For test cases: returns a String with lines and indices of NullElems.
	 * 
	 * @return String with NullElems
	 */
	public String printNullElems() {
		String retStr = "< ";
		for (int i = 0; i < synDiaElem.size(); i++) {
			if (i % 2 == 0) {
				retStr += ((NullElem) synDiaElem.get(i)).getLine();
				retStr += ", " + ((NullElem) synDiaElem.get(i)).getIndex();
			} else if (synDiaElem.get(i) instanceof Branch) {
				retStr += "[ "
						+ ((Branch) synDiaElem.get(i)).getLeft()
								.printNullElems();
				retStr += " : "
						+ ((Branch) synDiaElem.get(i)).getRight()
								.printNullElems() + " ] ";
			} else if (synDiaElem.get(i) instanceof Repetition) {
				retStr += "{ "
						+ ((Repetition) synDiaElem.get(i)).getLeft()
								.printNullElems();
				retStr += " : "
						+ ((Repetition) synDiaElem.get(i)).getRight()
								.printNullElems() + " } ";
			}
			// last String does not need a comma
			if (i < synDiaElem.size() - 1) {
				retStr += "; ";
			}
		}
		return retStr + " > ";
	}

}
