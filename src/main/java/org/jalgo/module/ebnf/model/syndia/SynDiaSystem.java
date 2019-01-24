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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Stack;

import org.jalgo.main.util.Messages;

/**
 * A <code>SynDiaSystem</code> consists of many <code>SyntaxDiagram</code>s.
 * It administrates all <code>Variable</code>s and
 * <code>TerminalSymbol</code>s to know, if the system is consistent.<br>
 * The startDiagram is the label of the first element in the list of variables.
 * 
 * @author Michael Thiele
 */
public class SynDiaSystem extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;

	private String startDiagram;

	private List<String> variables;

	private List<String> terminals;

	/**
	 * List with all syntax diagrams.
	 */
	private List<SyntaxDiagram> syntaxDiagrams;

	/**
	 * The <code>deleteStack</code> is used to keep track, what elements were
	 * deleted through the "rubber". If an element is removed of the
	 * concatenation, it is pushed on this stack to possibly re-insert it again
	 * at the same position with the same attributes (e.g. line number and left
	 * and right side for branches and repetitions).
	 */
	private Stack<SynDiaElem> deleteElemStack = new Stack<SynDiaElem>();

	/**
	 * Elements are pushed on the <code>undoElemStack</code> if they were
	 * removed by clicking "undo".
	 */
	private Stack<SynDiaElem> undoElemStack = new Stack<SynDiaElem>();

	/**
	 * Syntax diagrams that were deleted come to this stack.
	 */
	private Stack<SyntaxDiagram> deleteSynDiaStack;

	/**
	 * Syntax diagrams that were removed (through "undo") are put on this stack.
	 */
	private Stack<SyntaxDiagram> undoSynDiaStack;

	/**
	 * Constructs a syntax diagram system without a start diagram.
	 */
	public SynDiaSystem() {
		variables = new LinkedList<String>();
		terminals = new LinkedList<String>();
		syntaxDiagrams = new LinkedList<SyntaxDiagram>();
		deleteSynDiaStack = new Stack<SyntaxDiagram>();
		undoSynDiaStack = new Stack<SyntaxDiagram>();
	}

	/**
	 * Constructs a syntax diagram system with a start diagram.
	 * 
	 * @param startDiagram
	 *            the start diagram
	 */
	public SynDiaSystem(String startDiagram) {
		this();
		this.addSyntaxDiagram(startDiagram);
		this.startDiagram = startDiagram;
	}

	/**
	 * @param startDiagram
	 *            the new start diagram
	 */
	public void setStartDiagram(String startDiagram) {
		this.startDiagram = startDiagram;
	}

	/**
	 * @return Returns the name of the start diagram.
	 */
	public String getStartDiagram() {
		return startDiagram;
	}

	/**
	 * Checks, if the syntax diagram is complete. That means, all variables are
	 * the start variable of a syntax diagram.
	 * 
	 * @return <code>true</code> if system is complete<br>
	 */
	public boolean isComplete() {
		boolean retVal;
		for (String var : variables) {
			retVal = false;
			for (int i = 0; i < syntaxDiagrams.size(); i++) {
				if (syntaxDiagrams.get(i).getName().equals(var)) {
					retVal = true;
					break;
				}
			}
			if (retVal == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the <code>deleteElemStack</code> that is used to re-insert
	 * syntax diagram elements that were deleted by the "rubber".
	 * 
	 * @return the <code>deletedElemStack</code>
	 */
	public Stack<SynDiaElem> getDeleteElemStack() {
		return deleteElemStack;
	}

	/**
	 * Returns the <code>undoElemStack</code> that is used to re-insert syntax
	 * diagram elements that were removed by "undo".
	 * 
	 * @return the <code>undoElemStack</code>
	 */
	public Stack<SynDiaElem> getUndoElemStack() {
		return undoElemStack;
	}

	/**
	 * @return Returns the size of the syntax diagram list.
	 */
	public int getNumberOfDiagrams() {
		return syntaxDiagrams.size();
	}

	/**
	 * Returns a list with all labels of variables in the syntax diagram system.
	 * 
	 * @return a list with all labels of variables in the syntax diagram system.
	 */
	public List<String> getLabelsOfVariables() {
		List<String> retList = new LinkedList<String>();
		for (SyntaxDiagram sd : syntaxDiagrams) {
			retList.add(sd.getName());
		}
		return retList;
	}

	/**
	 * Returns a list with all labels of terminal symbols in the syntax diagram
	 * system.
	 * 
	 * @return a list with all labels of terminal symbols in the syntax diagram
	 *         system.
	 */
	public List<String> getLabelsOfTerminals() {
		List<String> retList = new LinkedList<String>();
		for (String label : terminals) {
			if (!retList.contains(label)) {
				retList.add(label);
			}
		}
		return retList;
	}

	/**
	 * @param name
	 *            name of the syntax diagram
	 * @return the syntax diagram
	 * @throws ElementNotFoundException
	 *             Is thrown if no syntax diagram with the given name exists in
	 *             the syntax diagram system.
	 */
	public SyntaxDiagram getSyntaxDiagram(String name)
			throws ElementNotFoundException {
		for (SyntaxDiagram s : syntaxDiagrams) {
			if (s.getName().equals(name)) {
				return s;
			}
		}
		throw new ElementNotFoundException(Messages.getString("ebnf",
				"SynDiaModel.ElementNotFoundException")
				+ "; " + name);
	}

	private void addSyntaxDiagram(SyntaxDiagram s) {
		syntaxDiagrams.add(s);
		if (syntaxDiagrams.size() == 1) {
			startDiagram = s.getName();
		}
		setChanged();
	}
	
	/**
	 * Adds a syntax diagram to the syntax diagram system.
	 * 
	 * @param name
	 *            name of the new syntax diagram
	 */
	public void addSyntaxDiagram(String name) {
		SyntaxDiagram s = new SyntaxDiagram(name, this);
		addSyntaxDiagram(s);
	}
	
	/**
	 * Adds a syntax diagram that is popped from the undoSynDiaStack.
	 * 
	 */
	public void undoRemoveSyntaxDiagram() {
		SyntaxDiagram s = undoSynDiaStack.pop();
		addSyntaxDiagram(s);
	}
	
	/**
	 * Adds a syntax diagram that is popped from the deleteSynDiaStack.
	 *
	 */
	public void undoDeleteSyntaxDiagram() {
		SyntaxDiagram s = deleteSynDiaStack.pop();
		addSyntaxDiagram(s);
	}

	/**
	 * Insert a complete syntax diagram in the syntax diagram system.
	 * 
	 * @param s
	 *            the syntax diagram to insert
	 */
	public void insertSyntaxDiagram(SyntaxDiagram s) {
		syntaxDiagrams.add(s);
		setChanged();
	}

	/**
	 * Removes a syntax diagram by a given name.
	 * 
	 * @param name
	 *            The name of the syntax diagram to remove.
	 * @param delete
	 *            <code>true</code> if the diagram will be deleted
	 * @return <code>true</code> if the list contained the syntax diagram
	 * @throws ElementNotFoundException
	 *             Is thrown if no syntax diagram with the given name exists in
	 *             the syntax diagram system.
	 * @throws IllegalArgumentException
	 *             Is thrown if you try to delete the start diagram.
	 */
	public boolean removeSyntaxDiagram(String name, boolean delete)
			throws ElementNotFoundException, IllegalArgumentException {
		if (name.equals(startDiagram)) {
			throw new IllegalArgumentException(Messages.getString("ebnf",
					"SynDiaModel.DeleteStartDiagramException"));
		}
		for (SyntaxDiagram s : syntaxDiagrams) {
			if (s.getName().equals(name)) {
				if (delete)
					deleteSynDiaStack.push(s);
				else
					undoSynDiaStack.push(s);
				boolean retVal = syntaxDiagrams.remove(s);
				setChanged();
				return retVal;
			}
		}
		throw new ElementNotFoundException(Messages.getString("ebnf",
				"SynDiaModel.ElementNotFoundException"));
	}

	/**
	 * Removes an element of the variable list.
	 * 
	 * @param variable
	 *            the <code>Variable</code> to remove
	 * @return <code>true</code> if the list contained the variable
	 */
	public boolean removeVariable(String variable) {
		return variables.remove(variable);
	}

	/**
	 * Removes a terminal symbol of the terminal symbol list.
	 * 
	 * @param terminal
	 *            the terminal to remove
	 * @return <code>true</code> if the list contained the terminal symbol
	 */
	boolean removeTerminal(String terminal) {
		return terminals.remove(terminal);
	}

	/**
	 * Adds a variable.
	 * 
	 * @param variable
	 *            the variable to add
	 * @return <code>true</code> if the variable was added to the variable
	 *         list
	 */
	public boolean addVariable(String variable) {
		return variables.add(variable);
	}

	/**
	 * Returns the list with all variables in the syntax diagram system.
	 * 
	 * @return the list with all variables in the syntax diagram system
	 */
	public List<String> getVariables() {
		List<String> retList = new LinkedList<String>();
		for (String label : variables) {
			if (!retList.contains(label)) {
				retList.add(label);
			}
		}
		return retList;
	}

	/**
	 * Adds a terminal symbol.
	 * 
	 * @param terminal
	 *            the terminal symbol to add
	 * @return <code>true</code> if the terminal symbol was added to the
	 *         terminal symbol list
	 */
	boolean addTerminal(String terminal) {
		return terminals.add(terminal);
	}

	/**
	 * Fills the whole syntax diagram system with NullElems.
	 * 
	 * @return <code>true</code> if a new NullElem was added.
	 */
	public boolean fillWithNullElems() {
		boolean retVal = false;
		for (SyntaxDiagram s : syntaxDiagrams) {
			retVal |= s.fillWithNullElems();
		}
		changed();
		return retVal;
	}

	/**
	 * Removes all NullElems in the whole syntax diagram system.
	 * 
	 * @return <code>true</code> if a NullElem was removed.
	 */
	public boolean removeNullElems() {
		boolean retVal = false;
		for (SyntaxDiagram s : syntaxDiagrams) {
			retVal |= s.removeNullElems();
		}
		changed();
		return retVal;
	}

	/**
	 * This function is used by elements of the syntax diagram system to signal
	 * that something has been changed.
	 * 
	 */
	void changed() {
		setChanged();
	}

	public String toString() {
		String retString = "";
		for (SyntaxDiagram s : syntaxDiagrams) {
			retString += s.toString() + "\n";
		}
		return retString;
	}

	/**
	 * @return String with all NullElems
	 */
	public String printNullElems() {
		String retString = "";
		for (SyntaxDiagram s : syntaxDiagrams) {
			retString += s.printNullElems() + "\n";
		}
		return retString;
	}
}
