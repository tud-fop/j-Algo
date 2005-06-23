/* Created on 05.05.2005 */
package org.jalgo.module.avl;

/**
 * The interface <code>Constants</code> is a collection of several constant values
 * used in the core implementation of the AVL module.
 * 
 * @author Ulrike Fischer, Alexander Claus
 */
public interface Constants {
	
	/*----------------------------valid key range----------------------------*/
	/** The minimum integer value a key can have. */
	public static final int MIN_KEY = 1;
	/** The maximum integer value a key can have. */
	public static final int MAX_KEY = 99;

	/*------------------------------return codes-----------------------------*/
	public static final int FOUND=1;
	public static final int WORKING=2;
	public static final int NOTFOUND=4;
	public static final int LEFT=8;
	public static final int RIGHT=16;
	public static final int DONE=32;
	public static final int ROTATE=64;
	public static final int ROOT=128;
	public static final int LASTUNDO=256;
	public static final int DOUBLEROTATE=512;
}