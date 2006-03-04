/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
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
import org.eclipse.swt.widgets.Control;

/**
 * This class lets you switch between different pages. It is like a tabbed composite just without tabs.
 * 
 * @author Julian Stecklina 
 */
public class PageFolder extends Composite {

	
	/** Checks whether the page belongs to this composite.
	 *  
	 * @param page The page we are checking
	 */
	protected void assertValidPage(Page page) {
		int i;
		boolean found = false;
		Control[] children = getChildren();
		
		for (i=0;i<children.length;i++)
			if (children[i] == page) {
				found = true;
				break;
			}
			
		if (!found)
			throw new IllegalArgumentException();
	}
	
	/**  
	 * @return The array of pages.
	 */
	public Page[] getPages() {
		Control[] children = getChildren();
		Page[] pages = new Page[children.length];
		
		// Java really sux. Why do I have to write this?!
		for (int i = 0; i < pages.length; i++)
			pages[i] = (Page)children[i];
		
		return pages;
	}
	
	/**
	 * Makes the given page the visible one.
	 * 
	 * @param page
	 */
	public void showPage(Page page) {
		Control[] pages = getChildren();
		
		assertValidPage(page);
		for (int i = 0; i<pages.length; i++)
			pages[i].setVisible(pages[i]==page);
	}
	
	/** Creates a new page folder with the given parent.
	 * @param parent
	 */
	PageFolder(Composite parent) {
		super(parent,SWT.NONE);
	}
}
