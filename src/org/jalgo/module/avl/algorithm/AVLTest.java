/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jalgo.module.avl.algorithm;
import org.jalgo.module.avl.datastructure.*;
import java.util.*;

/**
 * @author Ulrike Fischer
 * 
 * The class <code>AVLTest</code> checks if the current search tree has the 
 * AVL characteristic.
 */

public class AVLTest extends MacroCommand {
	
	private SearchTree tree;
	private boolean result=true;
	private Set<Node> nodes;
	
	/**
	 * @param st the searchtree
	 */
	
	public AVLTest(SearchTree st) {
		tree = st;
		name = "AVL-Test";
		nodes = new HashSet<Node>(); 
	}
	
	/**
	 * returns true, if the tree is an AVLTree
	 */
	
	public void perform() {
		results.clear();
		if (currentPosition==0) {
			SearchTree.calculateBalances((AVLNode) tree.getRoot());
			test((AVLNode) tree.getRoot());
			results.add(0,"AVL-Test");
			results.add(1,"");
			results.add(2,result);
			currentPosition++;
		}
		else {
			for (Node n:nodes) {
				n.setVisualizationStatus(Visualizable.NORMAL);
			}
			results.add(0,"");
			results.add(1,"");
		}
		currentPosition++;
	}
	
	private void test(AVLNode n) {
		if (n==null) return;
		if (n.getBalance()>=2 || n.getBalance()<=-2) {
			n.setVisualizationStatus(Visualizable.FOCUSED&Visualizable.LINE_NORMAL | Visualizable.BALANCE);
			nodes.add(n);
			result=false;
		}
		test((AVLNode) n.getLeftChild());
		test((AVLNode) n.getRightChild());
	}

	/**
	 * This method has no effect.
	 */
	public void abort() {}
	
	/**
	 * This method has no effect.
	 */
	public void undo() {}
	
	/**
	 * This method has no effect.
	 */
	public void performBlockStep() {}
	
	/**
	 * This method has no effect.
	 */
	public void undoBlockStep() {}
}
