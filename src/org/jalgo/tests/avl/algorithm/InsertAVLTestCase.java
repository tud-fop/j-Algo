package org.jalgo.module.avl.algorithm;

import org.jalgo.module.avl.datastructure.AVLNode;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.datastructure.WorkNode;

import junit.framework.TestCase;

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
