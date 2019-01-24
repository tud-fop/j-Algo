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

package org.jalgo.module.avl.algorithm;

import org.jalgo.module.avl.datastructure.*;

/**
 * @author Matthias Schmidt, Ulrike Fischer, Jean Christoph Jung
 * 
 * The class <code>CommandFactory</code> has the function to create new
 * algorithm objects and to give them initial parameters.
 */

public class CommandFactory {

	/**
	 * Creates a <code>CompareKey</code> object.
	 * 
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @return <code>CompareKey</code> the new instance.
	 */
	public static CompareKey createCompareKey(WorkNode wn) {
		return new CompareKey(wn);
	}

	/**
	 * Creates a <code>CreateNode</code> object.
	 * 
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @param tree the datastructure for the calculations.
	 * @return <code>CreateNode</code> the new instance
	 */
	public static CreateNode createCreateNode(WorkNode wn, SearchTree tree) {
		return new CreateNode(wn, tree);
	}

	/**
	 * Creates a <code>Search</code> object.
	 * 
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @return <code>Search</code> the new instance
	 */
	public static Search createSearch(WorkNode wn) {
		return new Search(wn);
	}

	/**
	 * Creates an <code>Insert</code> object.
	 * 
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @param tree the datastructure for the calculations.
	 * @return <code>Insert</code> the new instance
	 */
	public static Insert createInsert(WorkNode wn, SearchTree tree) {
		return new Insert(wn, tree);
	}

	/**
	 * Creates an <code>InsertAVL</code> object.
	 * 
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @param tree the datastructure for the calculations.
	 * @return <code>InsertAVL</code> the new instance
	 */
	public static InsertAVL createInsertAVL(WorkNode wn, SearchTree tree) {
		return new InsertAVL(wn, tree);
	}

	/**
	 * Creates a <code>CalcBalance</code> object.
	 * 
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @return <code>CalcBalance</code> the new instance
	 */
	public static CalcBalance createCalcBalance(WorkNode wn) {
		return new CalcBalance(wn);
	}

	/**
	 * Creates an <code>UpdateBalance</code> object.
	 * 
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @return <code>UpdateBalance</code> the new instance
	 */
	public static UpdateBalance createUpdateBalance(WorkNode wn) {
		return new UpdateBalance(wn);
	}

	/**
	 * Creates a <code>RotateLeft</code> object.
	 * 
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @param tree the datastructure for the calculations.
	 * @return <code>RotateLeft</code> the new instance
	 */
	public static RotateLeft createRotateLeft(WorkNode wn, SearchTree tree) {
		return new RotateLeft(tree, wn);
	}

	/**
	 * Creates a <code>RotateRight</code> object.
	 * 
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @param tree the datastructure for the calculations.
	 * @return <code>RotateRight</code> the new instance
	 */
	public static RotateRight createRotateRight(WorkNode wn, SearchTree tree) {
		return new RotateRight(tree, wn);
	}

	/**
	 * Creates a <code>CreateRandomTree</code> object.
	 * 
	 * @param number the final tree has this number of nodes.
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @param tree the datastructure for the calculations.
	 * @param avl if <code>true</code> the final tree has the avl character.
	 * @return <code>CreateRandomTree</code> the new instance
	 */
	public static CreateRandomTree createCreateRandomTree(int number,
		SearchTree tree, WorkNode wn, boolean avl) {
		return new CreateRandomTree(number, tree, wn, avl);
	}

	/**
	 * Creates a <code>CalcHeight</code> object.
	 * 
	 * @param n the height for this <code>Node</code> in a tree is calculated.
	 * @return <code>CalcHeight</code> the new instance.
	 */
	public static CalcHeight createCalcHeight(Node n) {
		return new CalcHeight(n);
	}

	/**
	 * Creates an <code>AVLTest<code> object.
	 * @param tree the datastructure for the calculations.
	 * @return <code>AVLTest<code> the new instance.
	 */
	public static AVLTest createAVLTest(SearchTree tree) {
		return new AVLTest(tree);
	}

	/**
	 * Creates a <code>NoOperation</code> object. The NoOperation needs no
	 * parameters.
	 * 
	 * @return <code>NoOperation</code> the new instance.
	 */
	public static NoOperation createNoOperation() {
		return new NoOperation();
	}

	/**
	 * Creates a <code>RemoveNode<code> object.
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @param tree the datastructure for the calculations.
	 * @param nodeToRemove this <code>Node</code> will be removed from the tree.
	 * @return <code>RemoveNode<code> the new instance.
	 */
	public static RemoveNode createRemoveNode(WorkNode wn, SearchTree tree,
		Node nodeToRemove) {
		return new RemoveNode(wn, tree, nodeToRemove);

	}

	/**
	 * Creates a <code>Remove<code> object.
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @param tree the datastructure for the calculations.
	 * @return <code>Remove<code> the new instance.
	 */
	public static Remove createRemove(WorkNode wn, SearchTree tree) {
		return new Remove(wn, tree);
	}

	/**
	 * Creates a <code>RemoveAVL<code> object.
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @param tree the datastructure for the calculations.
	 * @return <code>RemoveAVL<code> the new instance.
	 */
	public static RemoveAVL createRemoveAvl(WorkNode wn, SearchTree tree) {
		return new RemoveAVL(wn, tree);
	}

	/**
	 * Creates a <code>FindSuccessor<code> object.
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @return <code>FindSuccessor<code> the new instance.
	 */
	public static FindSuccessor createFindSuccessor(WorkNode wn) {
		return new FindSuccessor(wn);
	}

	/**
	 * Creates a <code>FindSuccessorStep<code> object.
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @return <code>FindSuccessorStep<code> the new instance.
	 */
	public static FindSuccessorStep createFindSuccessorStep(WorkNode wn) {
		return new FindSuccessorStep(wn);
	}

	/**
	 * Creates a <code>FindSuccessorStart<code> object.
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @return <code>FindSuccessorStart<code> the new instance.
	 */
	public static FindSuccessorStart createFindSuccessorStart(WorkNode wn) {
		return new FindSuccessorStart(wn);
	}

	/**
	 * Creates a <code>SearchAlg<code> object.
	 * @param wn the <code>WorkNode</code> for the calculations.
	 * @return <code>Command</code> the new instance.
	 */
	public static Command createSearchAlg(WorkNode wn) {
		return new SearchAlg(wn);
	}
}
