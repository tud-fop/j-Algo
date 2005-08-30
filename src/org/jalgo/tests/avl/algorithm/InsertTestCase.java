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

package org.jalgo.tests.avl.algorithm;

import junit.framework.TestCase;

import org.jalgo.module.avl.algorithm.CommandFactory;
import org.jalgo.module.avl.algorithm.MacroCommand;
import org.jalgo.module.avl.datastructure.AVLNode;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.datastructure.WorkNode;



public class InsertTestCase extends TestCase {

	AVLNode node;
	SearchTree tree;
	int [] keys = {5, 7, 1, 9, 4};
	
	public InsertTestCase(String s) {
		super(s);
	}
	
	public static void main(String[] args) {
		new InsertTestCase("testInsert");
	}

	protected void setUp() throws Exception {
		node = new AVLNode(keys[0]);
		node.setHeight(3);
		AVLNode sieben, eins, neun, vier;
		sieben = new AVLNode(7, node);
		sieben.setHeight(2);
		neun = new AVLNode(9, sieben);
		neun.setHeight(1);
		eins = new AVLNode(1, node);
		eins.setHeight(2);
		vier = new AVLNode(4, eins);
		vier.setHeight(1);
		sieben.setRightChild(neun);
		eins.setRightChild(vier);
		node.setLeftChild(eins);
		node.setRightChild(sieben);
	}

	public void testInsert() {
		int key;
		tree = new SearchTree();
		MacroCommand command = CommandFactory.createInsert(new WorkNode(0),tree);
		for (int i=0; i<keys.length; i++) {
			key = keys[i];
			WorkNode wn = new WorkNode(key);
			wn.setNextToMe(tree.getRoot());
			command = CommandFactory.createInsert(wn,tree);
		
			while (command.hasNext()) {
				command.perform();
			}
		}
		
		// alles r�ckg�ngig
		while (command.hasPrevious()) {
			command.undo();
		}
		
		tree.printToConsole();
		
		// wieder alles ausf�hren
		while (command.hasNext()) command.perform();
		
		tree.printToConsole();

		assertTrue("fehlerhaft eingef�gt", tree.equals(new SearchTree(node)));
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
