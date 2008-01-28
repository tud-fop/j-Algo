package org.jalgo.module.heapsort.renderer;

import java.awt.Container;
import java.awt.Rectangle;


/**
 * <p> This interface corresponds to a primary (=output) surface
 * of a renderer. Canvas entities are created wrt. to this surface
 * (because they could be requiring the same hardware as the
 * primary surface). Standard operations are there, as well:
 * Clearing the primary surface, rendering canvas entities,
 * and rendering canvas entities which lie in a certain area.</p>
 * 
 * <p>This interface is preliminary and subject to change!</p>
 *  
 * @author mbue
 *
 */
public interface Renderer {
	
	/**
	 * initialize renderer
	 * 
	 * @param cc
	 */
	void init(Container cc);
	
	/**
	 * finalise renderer
	 *
	 */
	void dispose();
	
	// -- factory stuff
	CanvasEntityFactory createFactory();
	
	// -- rendering stuff
	/**
	 * Returns the rectangle describing the visible area.
	 * 
	 * @return
	 */
	Rectangle getVisible();
	
	/**
	 * Returns whether the image was lost since the
	 * last time it was rendered.
	 * 
	 * @return
	 */
	boolean validate();
	
	/**
	 * <p>Render those canvas entities from the tree given by
	 * <code>root</code> which lie in the rectangle <code>r</code>.
	 * The tree is treated as being immutable. The user is responsible
	 * for clearing dirty regions.</p>
	 * 
	 * <p>Implementors might find CanvasEntity.foldVisible useful.</p>
	 * 
	 * @param root
	 * @param r
	 */
	void renderVisible(CanvasEntity root, Rectangle r);

	/**
	 * Update part of the image on screen
	 * specified by the rectangle <code>r</code>.
	 * If the argument is <code>null</code>,
	 * update everything.
	 * 
	 * @param r
	 * @return <code>true</code>
	 */
	public boolean show(Rectangle r);
}
