/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/* $Id: PageLayout.java,v 1.3 2005/10/03 10:34:11 stephancr Exp $
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

/**
 * A layout that stretches all its children to the maximum possible size. The
 * children are lying on top of each other.
 * @author Julian Stecklina
 */
public class PageLayout extends Layout {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Layout#computeSize(org.eclipse.swt.widgets.Composite, int, int, boolean)
	 */
	protected Point computeSize(Composite composite, int wHint, int hHint,
			boolean flushCache) {
		Control[] children = composite.getChildren();
		int maxWidth = 0, maxHeight = 0;
		for (int i = 0; i < children.length; i++) {
			Control child = children[i];
			Point size = child.computeSize(SWT.DEFAULT,
					SWT.DEFAULT, flushCache);
			maxWidth = Math.max(maxWidth, size.x);
			maxHeight = Math.max(maxHeight, size.y);
		}
		return new Point(maxWidth, maxHeight);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Layout#layout(org.eclipse.swt.widgets.Composite, boolean)
	 */
	protected void layout(Composite composite, boolean flushCache) {
		Rectangle rect = composite.getClientArea();
		Control[] children = composite.getChildren();
		for (int i = 0; i < children.length; i++)
			children[i].setBounds(rect);
	}
}
