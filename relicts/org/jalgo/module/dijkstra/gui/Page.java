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

/*
 * Created on 12.05.2005
 * $Id$
 */
package org.jalgo.module.dijkstra.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * A page is a specialised Composite to be put on a {@link PageFolder}. 
 * 
 * @author Julian Stecklina
 */
public class Page extends Composite {

	/**
	 * This method is intended to create the contents of the Page and
	 * you probably want to override it.
	 */
	public void createContents() {
		// Do nothing per default
	}

	/**
	 * @return The parent as PageFolder.
	 */
	public PageFolder getPageFolder() {
		return (PageFolder) getParent();
	}

	/**
	 * Switch to the page after this in our PageFolder parent. 
	 */
	public void showNextPage() {
		PageFolder folder = getPageFolder();
		Page[] pages = folder.getPages();
		int pos = 0;

		// (show-page (or (first (member page pages)) (first pages)))
		for (int i = 0; i < pages.length; i++)
			if (pages[i] == this) {
				pos = (i + 1 == pages.length) ? 0 : i + 1;
			}

		folder.showPage(pages[pos]);
	}

	/** Creates a page with the given folder as parent.
	 * @param parent a page folder.
	 */
	Page(PageFolder parent) {
		super(parent, SWT.NONE);
		createContents();
	}
}
