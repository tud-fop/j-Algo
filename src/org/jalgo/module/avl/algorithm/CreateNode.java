package org.jalgo.module.avl.algorithm;

import org.jalgo.module.avl.Constants;
import org.jalgo.module.avl.datastructure.*;


/**
 * @author Jean Christoph Jung
 * This class provides the creation of a new node. 
 * The node is inserted into the searchtree at the place,
 * specified by the worknode and the parameterlist.
 */
public class CreateNode extends Command {
	private WorkNode wn;
	private SearchTree tree;
	
	/**
	 * @param w: reference to the position in the tree, holds the new key too.
	 * @param st: the searchtree the new node is inserted in. 
	 */
	public CreateNode(WorkNode w, SearchTree st) {
		super();
		wn = w;
		tree = st;
	}
	
	/**
	 * This method creates a new node and inserts it into the searchtree.
	 */
	@Override
	public void perform() {
		if (wn.getNextToMe()==null) {
			AVLNode node = new AVLNode(wn.getKey());
			tree.setRoot(node);
			wn.setNextToMe(node);
		}
		else {
			AVLNode parent = (AVLNode) wn.getNextToMe();
			AVLNode node = new AVLNode(wn.getKey(), parent);
			int child = ((Integer)parameters.get(0)).intValue();
			if (child==Constants.LEFT) {
				parent.setLeftChild(node);
			}
			else {
				parent.setRightChild(node);
			}
			wn.setNextToMe(node);
		}
	}
	
	
	/**
	 * This method deletes the node, which was created by the <code>perform</code>-method
	 * the behaviour is undefined, if perform has not been called yet;
	 * otherwise <code>undo</code> recovers the state before calling <code>perform</code>
	 */
	@Override
	public void undo() {
		if (wn.getNextToMe() == tree.getRoot()) {
			wn.setNextToMe(null);
			tree.setRoot(null);
			wn.setVisualizationStatus(Visualizable.NORMAL);
		}
		else {
			AVLNode parent = (AVLNode) wn.getNextToMe().getParent();
			wn.setNextToMe(parent);
			int child = ((Integer)parameters.get(0)).intValue();
			if (child==Constants.LEFT) {
				parent.setLeftChild(null);
				wn.setVisualizationStatus(Visualizable.NORMAL | Visualizable.LEFT_ARROW);
			}
			else {
				parent.setRightChild(null);
				wn.setVisualizationStatus(Visualizable.NORMAL | Visualizable.RIGHT_ARROW);
			}
		}
		
		Command ch = CommandFactory.createCalcHeight(tree.getRoot());
		ch.perform();
	}
	
}
