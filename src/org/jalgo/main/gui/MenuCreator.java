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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return null;
	}

}
