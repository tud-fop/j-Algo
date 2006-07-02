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

public class InsertAVLTestCase extends TestCase {

	private SearchTree tree;
	private AVLNode n;
	int [] keys = {13,8,4,10,12};
	int [] orderedkeys = {8,4,12,10,13};
	public static void main(String[] args) {
		new InsertAVLTestCase("testInsertAVL");
	}

	public InsertAVLTestCase(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		tree = new SearchTree();
		WorkNode wn = null;
		for (int i = 0; i<orderedkeys.length; i++) {
			wn = new WorkNode(orderedkeys[i]);
			wn.setNextToMe(tree.getRoot());
			MacroCommand c = CommandFactory.createInsert(wn,tree);
			while (c.hasNext()) {
				c.perform();
			}
		}
		n = (AVLNode) tree.getRoot();
		tree.setRoot(null);
		System.out.println("setup beendet");
	}

	public void testInsertAVL() {
		int key;
		MacroCommand command = CommandFactory.createInsertAVL(new WorkNode(0),tree);
		for (int i=0; i<keys.length; i++) {
			key = keys[i];
			System.out.println(key);
			WorkNode wn = new WorkNode(key);
			wn.setNextToMe(tree.getRoot());
			command = CommandFactory.createInsertAVL(wn,tree);
		
			while (command.hasNext()) {
				command.perform();
			}
		}
		
		// alles rückgängig
/*		while (command.hasPrevious()) {
			command.undo();
		}
		
		tree.printToConsole();
		
		// wieder alles ausführen
		while (command.hasNext()) command.perform();
		
		tree.printToConsole();
	*/	
		assertTrue("fehlerhaft eingefügt", tree.equals(new SearchTree(n)));
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
