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
import org.jalgo.module.avl.algorithm.CreateRandomTree;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.datastructure.WorkNode;

public class RandomTreeTestCase extends TestCase {

	SearchTree tree;
	public static void main(String[] args) {
		new RandomTreeTestCase("testRandomTree");
	}

	public RandomTreeTestCase(String arg0) {
		super(arg0);
	}

	public void testRandomTree() {
		tree = new SearchTree();
		WorkNode wn = new WorkNode(10);
		CreateRandomTree crt = CommandFactory.createCreateRandomTree(10, tree, wn,true);
		while (crt.hasNext()) crt.perform();
		
		tree.printToConsole();
	}

}
