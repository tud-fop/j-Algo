package org.jalgo.module.avl.algorithm;
import org.jalgo.module.avl.datastructure.*;

/**
 * @author Ulrike Fischer
 * 
 * The class <code>CompareKey</code> compares two keys with eacher other.
 */

public class CompareKey extends Command {
	
	private WorkNode wn;
	
	/**
	 * @param wn reference to the position in the tree, holds the new key
	 */
	
	public CompareKey(WorkNode wn) {
		super();
		this.wn = wn;
	}
	
	/**
	 * Gets the WorkNode and compares its key with the key of the <code>nextToMe
	 * </code> node, returns 0 if the keys are the same, -1 if the key of the
	 * WorkNode is smaller and 1 otherwise.
	 */
	
	public void perform() {
		
		Integer worknodekey = wn.getKey();
		Integer nexttomekey = wn.getNextToMe().getKey();
		
		results.add(worknodekey.compareTo(nexttomekey));
	}
	
	/**
	 * method is empty, undo not necessary
	 */
	
	public void undo() {

	}
}
