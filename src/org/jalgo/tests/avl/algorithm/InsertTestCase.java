package org.jalgo.tests.avl.algorithm;

import java.util.Arrays;

import junit.framework.TestCase;
import org.jalgo.module.avl.algorithm.CommandFactory;
import org.jalgo.module.avl.algorithm.MacroCommand;
import org.jalgo.module.avl.datastructure.*;



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
		
		// alles rückgängig
		while (command.hasPrevious()) {
			command.undo();
		}
		
		tree.printToConsole();
		
		// wieder alles ausführen
		while (command.hasNext()) command.perform();
		
		tree.printToConsole();

		assertTrue("fehlerhaft eingefügt", tree.equals(new SearchTree(node)));
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
