/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
 * Created on May 23, 2004
 */
 
package org.jalgo.main.gui;

import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * @author Cornelius Hald
 */
public class MenuCreator implements IMenuCreator {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuCreator#dispose()
	 */
	public void dispose() {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Control)
	 */
	public Menu getMenu(Control arg0) {

		Menu menu = new Menu(arg0);

		//MenuItem item1 = new MenuItem(menu, SWT.CASCADE);
		//item1.setText("EBNF Wizzard");
		//item1.setImage(new Image(null, "pix/jalgo-file.png"));

		MenuItem item2 = new MenuItem(menu, SWT.CASCADE);
		item2.setText(Messages.getString("MenuCreator.EBNF-Diagramm_1")); //$NON-NLS-1$
		item2.setImage(new Image(null, "pix/jalgo-file.png")); //$NON-NLS-1$
		
		MenuItem item3 = new MenuItem(menu, SWT.CASCADE);
		item3.setText(Messages.getString("MenuCreator.EBNF-Term_3")); //$NON-NLS-1$
		item3.setImage(new Image(null, "pix/jalgo-file.png")); //$NON-NLS-1$

		return menu;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Menu)
	 */
	public Menu getMenu(Menu arg0) {
		return null;
	}

}
