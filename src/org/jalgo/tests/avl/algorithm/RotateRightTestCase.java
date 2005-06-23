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
		
		assertTrue("falsch rückgängig gemacht", tree.equals(new SearchTree(n)));
		assertTrue("worknode falsch", wn.getNextToMe()==tree.getRoot());
		
	}
	
	protected void tearDown() throws Exception {
	}

}
