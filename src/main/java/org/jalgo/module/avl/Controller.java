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

/*
 * Created on 26.04.2005
 * 
 */
package org.jalgo.module.avl;

import java.util.LinkedList;
import java.util.Queue;

import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.algorithm.AVLTest;
import org.jalgo.module.avl.algorithm.CommandFactory;
import org.jalgo.module.avl.algorithm.CreateRandomTree;
import org.jalgo.module.avl.algorithm.Insert;
import org.jalgo.module.avl.algorithm.InsertAVL;
import org.jalgo.module.avl.algorithm.MacroCommand;
import org.jalgo.module.avl.algorithm.Remove;
import org.jalgo.module.avl.algorithm.RemoveAVL;
import org.jalgo.module.avl.datastructure.Node;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.datastructure.Visualizable;
import org.jalgo.module.avl.datastructure.WorkNode;

/**
 * @author Matthias Schmidt
 * 
 * The class <code>Controller</code> is an important connection between the
 * GUI an the working algorithms. <br>
 * It provides several functions like: to start a new algorithm, to check if the
 * working algorithm is ready and to get the results of the active algorithm.
 */
public class Controller
implements Constants {

	private SearchTree tree;
	private boolean avlMode = false;
	private MacroCommand currentCommand = null;
	private WorkNode workingNode = null;
	private Queue<String> logDescriptions;

	/**
	 * Constructs the <code>Controller</code> instance for the current AVL
	 * module instance.<br>
	 * Initializes the <code>WorkingNode</code> for the algorithms. <br>
	 * Initializes the list of logdescriptions shown in the logbook at the GUI.
	 * 
	 * @param st the datastructure for the algorithm
	 */
	public Controller(SearchTree st) {
		tree = st;
		workingNode = new WorkNode(0, null);
		workingNode.setVisualizationStatus(Visualizable.INVISIBLE);

		logDescriptions = new LinkedList<String>();
	}

	/**
	 * Sets the AVL mode
	 * 
	 * @param avl <code>true</code> to enable the AVL mode, <code>false</code>
	 *            to disable it.
	 */
	public void setAVLMode(boolean avl) {
		avlMode = avl;
	}

	/**
	 * Retrieves the status of the AVL mode
	 * 
	 * @return <code>true</code> if the AVL mode is enabled
	 */
	public boolean isAVLMode() {
		return avlMode;
	}

	/**
	 * @return The section of the current command. <br>
	 *         If there is no current command "noCommand" is returned.
	 */
	public String getSection() {
		if (currentCommand == null || !currentCommand.hasNext()) return null;
		return (String)currentCommand.getResults().get(1);
	}

	/**
	 * Retrieves the Node with which the algorithms work.
	 * 
	 * @return The WorkNode of the active algorithm.
	 */
	public WorkNode getWorkNode() {
		return workingNode;
	}

	/**
	 * Starts a new Search algorithm.
	 * 
	 * @param key the key of the <code>Node</code> that is searched in the
	 *            tree
	 */
	public void startSearch(int key) {
		workingNode = new WorkNode(key, tree.getRoot());
		currentCommand = CommandFactory.createSearch(workingNode);
		putLogDescription((String)currentCommand.getResult(0));
	}

	/**
	 * Starts a new Insert algorithm. <br>
	 * If the AVL mode is enabled the insertation keeps the avl character.
	 * 
	 * @param key the key of the <code>Node</code> that shall be inserted.
	 */
	public void startInsert(int key) {
		workingNode = new WorkNode(key, tree.getRoot());
		if (avlMode) currentCommand = CommandFactory.createInsertAVL(
			workingNode, tree);
		else currentCommand = CommandFactory.createInsert(workingNode, tree);
		putLogDescription((String)currentCommand.getResult(0));
	}

	/**
	 * Starts a new Remove algorithm. If the AVL mode is enabled the removal
	 * keeps the avl character.
	 * 
	 * @param key the key of the <code>Node</code> the shall be removed.
	 */
	public void startRemove(int key) {
		workingNode = new WorkNode(key, tree.getRoot());
		if (avlMode) currentCommand = CommandFactory.createRemoveAvl(
			workingNode, tree);
		else currentCommand = CommandFactory.createRemove(workingNode, tree);
		putLogDescription((String)currentCommand.getResult(0));
	}

	/**
	 * Starts an algorithm that creates a random tree. <br>
	 * If the AVL mode is enabled the new tree has the avl character.
	 * 
	 * @param nodeNumber number of <code>Node</code>s for the random tree
	 */
	public void createRandomTree(int nodeNumber) {
		workingNode = new WorkNode(0, tree.getRoot());
		workingNode.setVisualizationStatus(Visualizable.INVISIBLE);
		currentCommand = CommandFactory.createCreateRandomTree(nodeNumber,
			tree, workingNode, avlMode);
		putLogDescription((String)currentCommand.getResult(0));
	}

	/**
	 * Starts an AVLTest algorithm and performs it once.
	 */
	public void startAVLTest() {
		currentCommand = CommandFactory.createAVLTest(tree);
		currentCommand.perform();
		putLogDescription((String)currentCommand.getResult(0));
	}

	/**
	 * Checks the status of the current command.
	 * 
	 * @return <code>false</code> if the current command has no next step or
	 *         if there is no current command.
	 */
	public boolean algorithmHasNextStep() {
		boolean next = false;
		if (currentCommand != null) next = currentCommand.hasNext();
		return next;
	}

	/**
	 * Checks the status of the current command.
	 * 
	 * @return <code>false</code> if the current command has no previous step
	 *         or if there is no current command.
	 */
	public boolean algorithmHasPreviousStep() {
		boolean previous = false;
		if (currentCommand != null) previous = currentCommand.hasPrevious();
		return previous;
	}

	/**
	 * Tries to perform one step forward in the active algorithm.
	 * 
	 * @throws NoActionException if there is no active algorithm or if it's
	 *             impossible to calculate a new step in the active algorithm
	 */
	public void perform()
	throws NoActionException {
		try {
			currentCommand.perform();
			// section = (String) currentCommand.getResults().get(0);
			putLogDescription((String)currentCommand.getResult(0));
		}
		catch (NullPointerException e) {
			throw new NoActionException(Messages.getString(
				"avl", "Controller.No_alg_exception")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (IndexOutOfBoundsException e) {
			throw new NoActionException(Messages.getString(
				"avl", "Controller.No_next_exception")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Tries to perform one undo-Step in the active algorithm.
	 * 
	 * @throws NoActionException if there is no active algorithm or if it's
	 *             impossible to calculate a previous step in the active
	 *             algorithm
	 */
	public void undo()
	throws NoActionException {
		try {
			currentCommand.hasNext();
		}
		catch (NullPointerException e) {
			throw new NoActionException(Messages.getString(
				"avl", "Controller.No_alg_exception")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		try {
			currentCommand.undo();
			// section = (String) currentCommand.getResults().get(0);
			putLogDescription((String)currentCommand.getResult(0));
		}
		catch (NullPointerException e) {
			throw new NoActionException(Messages.getString(
				"avl", "Controller.No_prev_exception")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Tries to perform one BIG step in the active algorithm.
	 * 
	 * @throws NoActionException if there is no active algorithm or if it's
	 *             impossible to calculate a new step in the active algorithm
	 */
	public void performBlockStep()
	throws NoActionException {
		try {
			currentCommand.performBlockStep();
			// section = (String) currentCommand.getResults().get(0);
			putLogDescription((String)currentCommand.getResult(0));
		}
		catch (NullPointerException e) {
			throw new NoActionException(Messages.getString(
				"avl", "Controller.No_alg_exception")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (IndexOutOfBoundsException e) {
			throw new NoActionException(Messages.getString(
				"avl", "Controller.No_next_exception")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Tries to perform one BIG step backwards in the active algorithm.
	 * 
	 * @throws NoActionException if there is no active algorithm or if it's
	 *             impossible to calculate a previous step in the active
	 *             algorithm
	 */
	public void undoBlockStep()
	throws NoActionException {
		try {
			currentCommand.hasNext();
		}
		catch (NullPointerException e) {
			throw new NoActionException(Messages.getString(
				"avl", "Controller.No_alg_exception")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		try {
			currentCommand.undoBlockStep();
			// section = (String) currentCommand.getResults().get(0);
			putLogDescription((String)currentCommand.getResult(0));
		}
		catch (NullPointerException e) {
			throw new NoActionException(Messages.getString(
				"avl", "Controller.No_prev_exception")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Terminates the active algorithm with the last stable status.
	 * 
	 * @throws NoActionException if something goes wrong with the Termination.
	 */
	public void abort()
	throws NoActionException {
		if (currentCommand == null) throw new NoActionException(
			Messages.getString(
				"avl", "Controller.No_alg_exception")); //$NON-NLS-1$ //$NON-NLS-2$

		if (currentCommand.hasNext()) putLogDescription(getAlgoName()
			+ Messages.getString("avl", "Controller.aborted")); //$NON-NLS-1$ //$NON-NLS-2$
		currentCommand.abort();

		if (tree.getRoot() != null) tree.getRoot().setVisualizationStatus(
			Visualizable.NORMAL);
		workingNode.setVisualizationStatus(Visualizable.INVISIBLE);
		Node next;
		if ((next = workingNode.getNextToMe()) != null)
			next.setVisualizationStatus(Visualizable.NORMAL);
		currentCommand = null;
	}

	/**
	 * Terminates the active algorithm by getting it to the end.
	 * 
	 * @throws NoActionException if something goes wrong with the Termination.
	 */
	public void finish()
	throws NoActionException {
		if (currentCommand == null) throw new NoActionException(
			Messages.getString("avl", "Controller.No_alg_exception")); //$NON-NLS-1$ //$NON-NLS-2$

		currentCommand.finish();
		putLogDescription(getAlgoName() + Messages.getString(
			"avl", "Controller.finished")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Returns the Result of the <code>AVL-Test</code>
	 * 
	 * @return <code>false</code> as <code>Boolean</code> if the tree has no
	 *         avl character. <br>
	 *         <code>null</code> is returned if the active algorithm is no
	 *         <code>AVLTest</code>.
	 */
	public Boolean getAVLTestResult() {
		if (currentCommand instanceof AVLTest)
			return (Boolean)currentCommand.getResult(2);
		return null;
	}

	/**
	 * Inserts the given <code>String</code> in the queue of log descriptions.
	 * These descriptions can be results of algorithms or events caused by the
	 * GUI.
	 * 
	 * @param logDesc a log description of the last action
	 */
	public void putLogDescription(String logDesc) {
		logDescriptions.offer(logDesc);
	}

	/**
	 * Retrieves the log description of the last action as <code>String</code>.
	 * If this method is called, the string is removed from the description
	 * queue. This guarantees that no action is mentioned more than once in log.
	 * 
	 * @return the log description of the last action
	 */
	public String getLogDescription() {
		return logDescriptions.poll();
	}

	/**
	 * Retrieves all accumulated log descriptions in entrance order.
	 * 
	 * @return a <code>Queue</code> of log descriptions.
	 */
	public Queue<String> getLogDescriptions() {
		Queue<String> retVal = new LinkedList<String>(logDescriptions);
		logDescriptions.clear();
		return retVal;
	}

	/**
	 * Returns a <code>String</code> which is shown in a short Message at the
	 * end of an algorithm.
	 * 
	 * @return the result of the active algorithm. <br>
	 *         0 is returned if there is no working algorithm or an
	 *         <code>AVLTest</code> is working.
	 */
	public String getResult() {
		if (currentCommand != null && !(currentCommand instanceof AVLTest)) {
			int result = (Integer)currentCommand.getResult(2);
			switch (result) {
				case FOUND:
					if (currentCommand instanceof Insert
						|| currentCommand instanceof InsertAVL)
						return Messages.getString(
							"avl", "Controller.Key_exists"); //$NON-NLS-1$ //$NON-NLS-2$
					return Messages.getString("avl", "Key_found"); //$NON-NLS-1$ //$NON-NLS-2$
				case DONE:
					if (currentCommand instanceof Insert
						|| currentCommand instanceof InsertAVL)
						return Messages.getString(
							"avl", "Node_inserted"); //$NON-NLS-1$ //$NON-NLS-2$
					if (currentCommand instanceof CreateRandomTree)
						return getAlgoName() + Messages.getString(
							"avl", "Controller.Successful"); //$NON-NLS-1$ //$NON-NLS-2$
					if (currentCommand instanceof Remove
						|| currentCommand instanceof RemoveAVL)
						return Messages.getString(
							"avl", "Node_removed"); //$NON-NLS-1$ //$NON-NLS-2$
				case NOTFOUND:
					return Messages.getString(
						"avl", "Controller.Key_not_found"); //$NON-NLS-1$ //$NON-NLS-2$
				default:
					return Messages.getString(
						"avl", "Controller.Log_exception"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return Messages.getString("avl", "Controller.No_alg_exception"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Returns a <code>String</code> better known as the algorithm name.
	 * 
	 * @return the name of the current active <code>MacroCommand</code>
	 */
	public String getAlgoName() {
		if (currentCommand != null) return currentCommand.getName();
		return "nocommand"; //$NON-NLS-1$
	}
}