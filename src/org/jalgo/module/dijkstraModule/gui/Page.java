/*
 * Created on 12.05.2005
 * $Id: Page.java,v 1.1 2005/06/23 10:08:27 jalgosequoia Exp $
 */
package org.jalgo.module.dijkstraModule.gui;

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
		return (PageFolder)(getParent());
	}
	
	/**
	 * Switch to the page after this in our PageFolder parent. 
	 */
	public void showNextPage() {
		PageFolder folder = getPageFolder();
		Page[] pages = folder.getPages();
		int pos = 0;

		// (show-page (or (first (member page pages)) (first pages)))
		for (int i = 0;i<pages.length;i++)
			if (pages[i] == this) {
				pos = (i+1 == pages.length) ? 0 : i+1; 
			}

		folder.showPage(pages[pos]);
	}
	
	/** Creates a page with the given folder as parent.
	 * @param parent a page folder.
	 */
	Page(PageFolder parent) {
		super(parent,SWT.NONE);
		createContents();
	}
}
