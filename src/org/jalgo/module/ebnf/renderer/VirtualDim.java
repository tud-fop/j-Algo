/**
 * 
 */
package org.jalgo.module.ebnf.renderer;

/**
 * Is a class, which contains the real dimension used to define an element to
 * render and a virtualHeight needed to get the rekursive full height of a
 * branch
 * 
 * @author Andre
 */
public class VirtualDim {

	/**
	 * Represents the real width of a drawn element
	 */
	public int width;

	/**
	 * Represents the real height of a drawn element
	 */
	public int height;

	/**
	 * Reperesents the height including all children
	 */
	public int virtualHeight;

	/**
	 * Initializes the elements and sets them to 0
	 */
	public VirtualDim() {

		this.width = 0;
		this.height = 0;
		this.virtualHeight = 0;

	}

	/** Initializes the elements by getting the values
	 * @param width the width
	 * @param height the height
	 * @param virtualHeight the virtualHeight.
	 */
	public VirtualDim(int width, int height, int virtualHeight) {

		this.width = width;
		this.height = height;
		this.virtualHeight = virtualHeight;

	}
}
