package org.jalgo.module.avl.algorithm;

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
 * Testcase for RotateLeft-Command
 * creates two SearchTrees, one in the setUp-method and 
 * the other in testRotate. The first one must arise out
 * of the second, when the second is rotated left around 
 * his root
 * balances are not considered yet.
 */
public class RotateLeftTestCase extends TestCase {
	
	private int [] keys = {5, 3, 8, 7, 10, 6};
	private int [] keys_order = {8, 5, 10, 3, 7, 6};
	AVLNode n;
	SearchTree tree;
	
	public static void main(String[] args) {
		new RotateLeftTestCase("testRotate");
	}

	public RotateLeftTestCase(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		tree = new SearchTree();
		for (int i = 0; i<keys_order.length; i++) {
			WorkNode wn = new WorkNode(keys_order[i]);
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
		Command rotate = CommandFactory.createRotateLeft(wn,tree);
		rotate.perform();
		
		assertTrue("falsch rotiert", tree.equals(new SearchTree(n)));
		assertTrue("worknode falsch", wn.getNextToMe()==tree.getRoot());
		
		rotate.undo();
		assertTrue("worknode falsch", wn.getNextToMe()==tree.getRoot());
		rotate.perform();
		
		assertTrue("worknode falsch", wn.getNextToMe()==tree.getRoot());
		assertTrue("fehler beim rückgängig machen", tree.equals(new SearchTree(n)));
	}	
	protected void tearDown() throws Exception {
	}

}
