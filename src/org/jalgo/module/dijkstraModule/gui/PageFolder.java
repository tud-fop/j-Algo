/*
 * Created on 12.05.2005
 * $Id: PageFolder.java,v 1.1 2005/06/23 10:08:26 jalgosequoia Exp $
 */
package org.jalgo.module.dijkstraModule.gui;

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
