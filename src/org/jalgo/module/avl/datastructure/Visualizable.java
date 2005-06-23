/* Created on 03.05.2005 */
package org.jalgo.module.avl.datastructure;

/**
 * The interface <code>Visualizable</code> has to be implemented by data structures,
 * which are intended to be visualized. With setting some flags the programmer can
 * change the style of the data structure, or some decorations can be added.
 *  
 * @author Ulrike Fischer
 */
public interface Visualizable {

	/**
	 * Sets the visualization status of the underlying data structure to the given
	 * value. The different flags can be combined with bitwise OR'ing.
	 * 
	 * @param status the visualization flags
	 */
	public void setVisualizationStatus(int status);

	/**
	 * Retrieves the visualization status of the underlying data structure. With
	 * bitwise AND'ing the programmer can test, if the different flags are set.
	 * 
	 * @return the visualization flags of the data structure
	 */
	public int getVisualizationStatus();

	//the flags
	/** Indicates that the data structure is painted normally. */
	public static final int NORMAL=1;
	/** Indicates that the data structure is hided. */
	public static final int INVISIBLE=2;
	/** Indicates that the data structure has to be painted highlighted. */
	public static final int FOCUSED=4;
	/** Indicates that the balance to the node has to be displayed. */
	public static final int BALANCE=8;
	/** Indicates that an insertion arrow to the left has to be painted. */
	public static final int LEFT_ARROW=16;
	/** Indicates that an insertion arrow to the right has to be painted. */
	public static final int RIGHT_ARROW=32;
	/** Indicates that an rotation arrow to the left has to be painted. */
	public static final int ROTATE_LEFT_ARROW=64;
	/** Indicates that an rotation arrow to the right has to be painted. */
	public static final int ROTATE_RIGHT_ARROW=128;
	/** Indicates that the connection to this node has to be painted normally. */
	public static final int LINE_NORMAL=256;
}