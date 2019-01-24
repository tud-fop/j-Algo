/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
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
 * Created on Aug 15, 2004 $Id: ModuleConnector.java,v 1.3 2005/07/13 23:01:10
 * styjdt Exp $
 */
package org.jalgo.module.dijkstra;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.module.dijkstra.gui.Controller;


/**
 * @see AbstractModuleConnector
 * 
 * @author Julian Stecklina
 */
public class ModuleConnector
extends AbstractModuleConnector {

	private Controller controller;

	public void init() {
		// initialization is taken here in run()
		JAlgoGUIConnector.getInstance().getModuleMenu(this).setEnabled(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.AbstractModuleConnector#run()
	 */
	public void run() {
		controller = new Controller(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.AbstractModuleConnector#setDataFromFile(java.io.ByteArrayInputStream)
	 */
	public void setDataFromFile(ByteArrayInputStream data) {
		controller.setSerializedData(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.AbstractModuleConnector#getDataForFile()
	 */
	public ByteArrayOutputStream getDataForFile() {
		return controller.getSerializedData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.AbstractModuleConnector#print()
	 */
	public void print() {
	// printing is currently not supported
	}
}