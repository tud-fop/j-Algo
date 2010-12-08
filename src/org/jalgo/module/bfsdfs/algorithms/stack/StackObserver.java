package org.jalgo.module.bfsdfs.algorithms.stack;

public interface StackObserver extends QueueObserver {
	/**
	 * Invoked when a queue was added to the stack. Attention: in case of the very first queue,
	 * {@link #onFirstQueueAdded(int)} is invoked instead!
	 * @param owner index of the queue owner
	 */
	public void onQueueAdded(int owner);
	
	/**
	 * Invoked when the first queue is added to the empty stack. This will
	 * happen when the algorithm that uses the stack is started.
	 * <p>Note that {@link #onQueueAdded(int)} is not called seperately for the first queue.
	 * @param startNode index of the first queue's owner
	 */
	public void onFirstQueueAdded(int owner);
	
	/**
	 * Invoked when all nodes reached the finished-status. This will happen
	 * when the algorithm that uses the stack is finished.
	 */
	public void onAllNodesFinished();
	
	/**
	 * Invoked when the topmost queue was removed
	 */
	public void onTopQueueRemoved();
	
	/**
	 * Invoked when all queues where removed
	 */
	public void onAllQueuesRemoved();
	
	/**
	 * Invoked when the current node changed.
	 * <p>
	 * The current node is...
	 * <ul>
	 * <li>the first node, if there is only one node in the stack</li>
	 * <li>unchanged, if there are more than one but no waiting nodes in the stack</li>
	 * <li>the first waiting node of the topmost queue (which contains waiting nodes)</li>
	 * </ul>
	 * @param newCurrentNode index of the new current node
	 */
	public void onCurrentNodeChanged(int newCurrentNode);
}
