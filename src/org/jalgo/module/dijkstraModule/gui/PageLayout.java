/* $Id: PageLayout.java,v 1.1 2005/06/23 10:08:26 jalgosequoia Exp $
 * Created on 26.05.2005
 *
 */
package org.jalgo.module.dijkstraModule.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/** A layout that stretches all its children to the maximum possible size. The children are lying on top of each other.
 * @author Julian Stecklina
 */
public class PageLayout extends Layout {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Layout#computeSize(org.eclipse.swt.widgets.Composite, int, int, boolean)
	 */
	protected Point computeSize(Composite composite, int wHint, int hHint,
			boolean flushCache) {
		Control [] children = composite.getChildren ();
		int count = children.length;
		int maxWidth = 0, maxHeight = 0;
		for (int i=0; i<count; i++) {
			Control child = children [i];
			Point size = child.computeSize (SWT.DEFAULT, SWT.DEFAULT, flushCache);
			maxWidth = Math.max (maxWidth, size.x);
			maxHeight = Math.max (maxHeight, size.y);
		}
		
		
		return new Point(maxWidth,maxHeight);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Layout#layout(org.eclipse.swt.widgets.Composite, boolean)
	 */
	protected void layout(Composite composite, boolean flushCache) {
		Rectangle rect = composite.getClientArea ();
		Control [] children = composite.getChildren ();
		for (int i = 0; i<children.length; i++)
			children[i].setBounds(rect);
	}

}
