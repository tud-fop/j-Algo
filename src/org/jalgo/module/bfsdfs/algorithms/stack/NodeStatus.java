package org.jalgo.module.bfsdfs.algorithms.stack;

/**
 * Indicates the status of a node in an algorithm's <code>NodeStack</code>
 * @author Thomas G&ouml;rres
 *
 */
public enum NodeStatus {
	/**
	 * Indicates that a node is not yet known to the algorithm
	 */
	UNTOUCHED,
	
	/**
	 * Indicates that a node is used by the algorithm
	 */
	WAITING,
	
	/**
	 * Indicates that a node is no longer used by the algorithm
	 */
	FINISHED;
}
