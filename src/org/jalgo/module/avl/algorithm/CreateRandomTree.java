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
 * 
 * Created on 22.04.2005
 */
package org.jalgo.module.avl.algorithm;

import java.util.*;

import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.*;
import org.jalgo.module.avl.datastructure.*;

/**
 * @author Matthias Schmidt
 * 
 * The class <code>CreateRandomTree</code> creates a random binary tree with a
 * given number of notes. There is the posibility to create an avl tree as well.
 */
public class CreateRandomTree
extends MacroCommand
implements Constants {

	int finalNodeNumber;
	Random rand;
	List<Integer> keyList;
	private SearchTree tree;
	private WorkNode wn;
	private boolean avl;

	/**
	 * Constructs the <code>CreateRandomTree</code> instance for the current
	 * tree generation. <br>
	 * Initializes the name of the working algorithm which is dependent on the
	 * avl mode. Initializes the command list with the first
	 * <code>Insert or InsertAVL</code> object.
	 * 
	 * @param nodes number of nodes the <code>SearchTree</code> will have
	 * @param st <code>SearchTree</code> which ought to be builded.
	 * @param wn <code>WorkNode</code> which is always use by the
	 *            <code>MacroCommand</code>s
	 */
	@SuppressWarnings("unchecked")
	public CreateRandomTree(int nodes, SearchTree st, WorkNode wn, boolean avl) {
		super();
		if (avl) name = Messages.getString("avl", "Alg_name.Create_AVL_tree"); //$NON-NLS-1$ //$NON-NLS-2$
		else name = Messages.getString("avl", "Alg_name.Create_tree"); //$NON-NLS-1$ //$NON-NLS-2$
		tree = st;
		this.wn = wn;
		this.avl = avl;
		if (nodes > 0) {
			rand = new Random();
			finalNodeNumber = nodes;
			keyList = new ArrayList<Integer>();

			int firstKey = rand.nextInt(MAX_KEY) + MIN_KEY;
			changeWorkNode(firstKey);
			if (avl) commands.add(0, CommandFactory.createInsertAVL(wn, tree));
			else commands.add(0, CommandFactory.createInsert(wn, tree));
			commands.add(1, CommandFactory.createNoOperation());
			keyList.add(0, firstKey);

			results.add(0, getName()
				+ Messages.getString("avl", "CreateRandomTree.With") + //$NON-NLS-1$ //$NON-NLS-2$
				finalNodeNumber
				+ Messages.getString("avl", "CreateRandomTree.Nodes")); //$NON-NLS-1$ //$NON-NLS-2$
			results.add(1, "1"); //$NON-NLS-1$
			results.add(2, WORKING);
		}
		else {
			results.add(0, Messages.getString(
				"avl", "CreateRandomTree.Tree_finished")); //$NON-NLS-1$ //$NON-NLS-2$
			results.add(1, " "); //$NON-NLS-1$
			results.add(2, DONE);

		}
	}

	// Changes the work node.
	private void changeWorkNode(int key) {
		wn.setKey(key);
		wn.setNextToMe(tree.getRoot());
		wn.setVisualizationStatus(Visualizable.NORMAL);
	}

	// Stores the log description and the section from a command.
	@SuppressWarnings("unchecked")
	private void storeLogAndSectionFrom(Command c) {
		results.clear();
		results.add(c.getResult(0));
		results.add(c.getResult(1));
		results.add(c.getResult(2));
	}

	/**
	 * Calculates one step. <br>
	 * The result list gets WORKING at the index 2 if the final number of
	 * <code>Node</code>s is not reached, DONE if the <code>SearchTree</code>
	 * is completed
	 */
	@SuppressWarnings("unchecked")
	public void perform() {
		Command c = commands.get(currentPosition);
		if (c instanceof NoOperation) {
			// Test: Ready?
			if (keyList.size() >= finalNodeNumber) {
				if (avl) results.set(0, Messages.getString(
					"avl", "CreateRandomTree.AVL_finished")); //$NON-NLS-1$ //$NON-NLS-2$
				else results.set(0, Messages.getString(
					"avl", "CreateRandomTree.Tree_finished")); //$NON-NLS-1$ //$NON-NLS-2$
				currentPosition++;
				return;
			}
			int newkey = 0;
			while (keyList.contains(newkey = rand.nextInt(MAX_KEY) + MIN_KEY)) {
				/* search new key*/ }
			keyList.add(newkey);
			changeWorkNode(newkey);
			if (avl) commands.set(0, CommandFactory.createInsertAVL(wn, tree));
			else commands.set(0, CommandFactory.createInsert(wn, tree));
			currentPosition = 0;
			storeLogAndSectionFrom(c);
		}
		else {
			MacroCommand m = (MacroCommand)c;
			if (m.hasNext()) {
				m.perform();
				storeLogAndSectionFrom(m);
				if (!m.hasNext()) currentPosition++;
			}
		}
	}

	/**
	 * Calculates one block step. <br>
	 * The result list gets WORKING at the index 2 if the final number of
	 * <code>Node</code>s is not reached, DONE if the <code>SearchTree</code>
	 * is completed
	 */
	@SuppressWarnings("unchecked")
	public void performBlockStep() {
		Command c = commands.get(currentPosition);
		if (c instanceof NoOperation) {
			if (keyList.size() >= finalNodeNumber) {
				if (avl) results.set(0, Messages.getString(
					"avl", "CreateRandomTree.AVL_finished")); //$NON-NLS-1$ //$NON-NLS-2$
				else results.set(0, Messages.getString(
					"avl", "CreateRandomTree.Tree_finished")); //$NON-NLS-1$ //$NON-NLS-2$

				currentPosition++;
				return;
			}
			int newkey = 0;
			while (keyList.contains(newkey = rand.nextInt(MAX_KEY) + MIN_KEY)) {
			/* search new key*/ }
			keyList.add(newkey);
			changeWorkNode(newkey);
			if (avl) commands.set(0, CommandFactory.createInsertAVL(wn, tree));
			else commands.set(0, CommandFactory.createInsert(wn, tree));
			currentPosition = 0;
		}
		else {
			MacroCommand m = (MacroCommand)c;
			while (m.hasNext()) {
				m.perform();
				storeLogAndSectionFrom(m);
				if (!m.hasNext()) currentPosition++;
			}
			if (keyList.size() >= finalNodeNumber) {
				currentPosition = 2;
				return;
			}
		}
	}

	/**
	 * Calculates one undo.
	 */
	public void undo() {
		if (!hasPrevious()) return;

		MacroCommand mc = (MacroCommand)commands.get(0);
		mc.undo();
		storeLogAndSectionFrom(mc);
		currentPosition = 0;
	}

	/**
	 * Calculates one block undo.
	 */
	public void undoBlockStep() {
		if (!hasPrevious()) return;

		currentPosition = 0;
		MacroCommand mc = (MacroCommand)commands.get(0);
		while (mc.hasPrevious())
			mc.undo();
		storeLogAndSectionFrom(mc);
	}

	/**
	 * Terminates the tree generation. After that, the tree has it's current
	 * number of nodes.
	 */
	@SuppressWarnings("unchecked")
	public void abort() {
		if (!this.hasPrevious()) return;
		Command com = commands.get(currentPosition);
		if (com instanceof NoOperation) {
			currentPosition++;
		}
		if (currentPosition < commands.size()) {
			if (avl) {
				InsertAVL ins = (InsertAVL)commands.get(currentPosition);
				while (ins.hasPrevious())
					ins.undo();
			}
			else {
				Insert ins = (Insert)commands.get(currentPosition);
				while (ins.hasPrevious())
					ins.undo();
			}
		}
		commands = commands.subList(0, currentPosition);
		keyList = keyList.subList(0, currentPosition / 2);
		finalNodeNumber = keyList.size();
		results.clear();
		results.add(Messages.getString(
			"avl", "CreateRandomTree.Tree_finished")); //$NON-NLS-1$ //$NON-NLS-2$
		results.add(" "); //$NON-NLS-1$
		results.add(DONE);
	}
}