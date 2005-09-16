/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and platform
 * independant. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/*
 * Created on 13.04.2004
 */

package org.jalgo.module.synDiaEBNF;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.main.AbstractModuleConnector;

/**
 * @author Michael Pradel
 * @author Benjamin Scholz
 * @author Marco Zimmerling
 */
public class ModuleConnector
extends AbstractModuleConnector {

	private ModuleController controller;

	/**
	 * @see AbstractModuleConnector
	 */
	public ModuleConnector(Composite comp, SubMenuManager menu,
		SubToolBarManager tb) {
		super(comp, menu, tb);
		controller = new ModuleController(this, comp, menu, tb);
	}

	/**
	 * @see AbstractModuleConnector#run()
	 */
	public void run() {
		controller.run();
	}

	/**
	 * @see AbstractModuleConnector#setDataFromFile(ByteArrayInputStream)
	 */
	public void setDataFromFile(ByteArrayInputStream data) {
		controller.setSerializedData(data);
	}

	/**
	 * @see AbstractModuleConnector#getDataForFile()
	 */
	public ByteArrayOutputStream getDataForFile() {
		return controller.getSerializedData();
	}

	/**
	 * This method is not used in this module.
	 */
	public void print() {
	// printing is currently not used
	}

	/**
	 * Returns the <code>ModuleController</code>, which is controlling this
	 * module.
	 * 
	 * @return the <code>ModuleController</code>
	 * @see ModuleController
	 */
	public ModuleController getController() {
		return controller;
	}
}