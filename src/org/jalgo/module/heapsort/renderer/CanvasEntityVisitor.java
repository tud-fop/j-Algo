/**
 * 
 */
package org.jalgo.module.heapsort.renderer;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;


//we will often call our visitor objects arnie, because Arnie is the one
//who visits people most effectively
public interface CanvasEntityVisitor {
	/**
	 * This method is called in the visiting process. The canvas entity
	 * to be visited is given by <code>e</code>, and <code>trans</code> is
	 * the affine transform yielding absolute coordinates wrt. to the root
	 * of the subtree on which the process is started. The third parameter,
	 * <code>clip</code>, determines a rectangle on which operations
	 * <em>have</em> to be limited. Often this will be <code>null</code>.
	 * 
	 * @param e
	 * @param trans
	 */
	void invoke(CanvasEntity e, AffineTransform trans, Rectangle clip);
}