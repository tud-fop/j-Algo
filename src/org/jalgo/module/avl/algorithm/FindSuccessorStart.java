/**
 * 
 */
package org.jalgo.module.avl.algorithm;

import org.jalgo.module.avl.Constants;
import org.jalgo.module.avl.datastructure.Node;
import org.jalgo.module.avl.datastructure.Visualizable;
import org.jalgo.module.avl.datastructure.WorkNode;

/**
 * @author Jean Christoph Jung
 * 
 * <code> FindNextInSizeStart </code> is the first step in searching the key
 * that is next in size to the key that will be removed.
 * 
 */
public class FindSuccessorStart extends Command implements Constants {

	private WorkNode wn;
	
	/**
	 * @param wn: the worknode indicates the position in the tree, where the search is started 
	 */
	public FindSuccessorStart(WorkNode wn) {
		this.wn = wn;
		results.add(0, "");
		results.add(1, "absatz");
		results.add(2, WORKING);
	}

	/**
	 * <code> perform </code> moves the worknode to the right child of the current node next
	 * to the worknode, and changes the visualisations of both the old and new node next to
	 * the worknode
	 * 
	 */
	@Override
	public void perform() {
		Node n = wn.getNextToMe();
		n = n.getRightChild();
		wn.setNextToMe(n);
		wn.setVisualizationStatus(Visualizable.INVISIBLE);
		n.setVisualizationStatus(Visualizable.FOCUSED | Visualizable.LINE_NORMAL);
		if (wn.getNextToMe().getLeftChild()==null) 
			results.set(2, FOUND);
		results.set(0, "einmal rechts gegangen");
	}

	/**
	 * <code> undo </code> moves the worknode to the parent of the current node next
	 * to the worknode, and changes the visualisations of both the old and new node next to
	 * the worknode
	 */
	@Override
	public void undo() {
		Node n = wn.getNextToMe();
		n.setVisualizationStatus(Visualizable.NORMAL);
		n = n.getParent();
		wn.setNextToMe(n);
		results.set(2, WORKING);
	}
}
