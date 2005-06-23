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

	protected void setUp() throws Exception {
	}
	
	protected void tearDown() throws Exception {
	}
	
	
	public void testRandomTree() {
		tree = new SearchTree();
		WorkNode wn = new WorkNode(10);
		CreateRandomTree crt = CommandFactory.createCreateRandomTree(10, tree, wn,true);
		while (crt.hasNext()) crt.perform();
		
		tree.printToConsole();
	}

}
