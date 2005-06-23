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
 * <code> FindNextInSizeStep </code> is one step in the search for the key
 * that is next in size to the key that will be removed.
 * 
 * 
 * 
 */
public class FindSuccessorStep extends Command implements Constants {

	private WorkNode wn;
	
	/**
	 * @param wn: the worknode indicates the position in the tree 
	 */
	public FindSuccessorStep(WorkNode wn) {
		this.wn = wn;
		results.add(0, "");
		results.add(1, "absatz");
		results.add(2, WORKING);
	}

	/** 
	 * <code> perform </code> moves the worknode to the left child (if exists) of the current node next
	 * to the worknode, and changes the visualisations of both the old and new node next to
	 * the worknode
	 */
	@Override
	public void perform() {
		Node n = wn.getNextToMe();
		n.setVisualizationStatus(Visualizable.NORMAL);
		n = n.getLeftChild();
		n.setVisualizationStatus(Visualizable.FOCUSED | Visualizable.LINE_NORMAL);
		wn.setNextToMe(n);
		if (n.getLeftChild()!=null)
			results.set(2, WORKING);
		else 
			results.set(2, FOUND);
		results.set(0, "links gegangen");
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
		n.setVisualizationStatus(Visualizable.FOCUSED | Visualizable.LINE_NORMAL);
		wn.setNextToMe(n);
		results.set(2, WORKING);	
	}

}
