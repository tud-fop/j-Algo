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

import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.algorithm.Command;
import org.jalgo.module.avl.algorithm.CommandFactory;
import org.jalgo.module.avl.algorithm.MacroCommand;
import org.jalgo.module.avl.datastructure.AVLNode;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.datastructure.WorkNode;

import junit.framework.TestCase;


/**
 * @author Jean
 * 
 * Testcase for RotateRight-Command
 * creates two SearchTrees, one in the setUp-method and 
 * the other in testRotate. The first one must arise out
 * of the second, when the second is rotated right around 
 * his root
 *
 */
public class RotateRightTestCase extends TestCase {

	private int [] keys = {10, 8, 11, 7, 9, 6};
	private int [] keys_order = {8, 7, 10, 6, 9, 11};
	AVLNode n;
	SearchTree tree;
	
	public static void main(String[] args) {
		new RotateRightTestCase("testRotate");
	}

	public RotateRightTestCase(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		Messages.registerResourceBundle("avl", "org.jalgo.module.avl.de");
		WorkNode wn = null;
		tree = new SearchTree();
		for (int i = 0; i<keys_order.length; i++) {
			wn = new WorkNode(keys_order[i]);
			wn.setNextToMe(tree.getRoot());
			MacroCommand c = CommandFactory.createInsert(wn,tree);
			while (c.hasNext()) {
				c.perform();
			}
		}
		n = (AVLNode) tree.getRoot();
		tree.setRoot(null);
	}

	public void testRotate() {
		WorkNode wn = null;
		for (int i = 0; i<keys.length; i++) {
			wn = new WorkNode(keys[i]);
			wn.setNextToMe(tree.getRoot());
			MacroCommand c = CommandFactory.createInsert(wn,tree);
			while (c.hasNext()) {
				c.perform();
			}
		}
		
		AVLNode root = (AVLNode) tree.getRoot();
		wn.setNextToMe(root);
		Command rotate = CommandFactory.createRotateRight(wn,tree);
		rotate.perform();
		
		assertTrue("falsch rotiert", tree.equals(new SearchTree(n)));
		assertTrue("worknode falsch", wn.getNextToMe()==tree.getRoot());
		
		rotate.undo();
		assertTrue("worknode falsch", wn.getNextToMe()==tree.getRoot());
		rotate.perform();
		
		assertTrue("falsch r�ckg�ngig gemacht", tree.equals(new SearchTree(n)));
		assertTrue("worknode falsch", wn.getNextToMe()==tree.getRoot());
		
	}
	

}
