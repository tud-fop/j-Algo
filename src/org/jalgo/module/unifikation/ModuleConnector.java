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
package org.jalgo.module.unifikation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JToolBar;

import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.gui.JAlgoGUIConnector;

public class ModuleConnector
extends AbstractModuleConnector {
	private Application application=null;

	@Override
	public void init() {
		application=new Application();
	}
	
	@Override
	public void run() {
		//Get Panel
		JComponent contentPane =
			JAlgoGUIConnector.getInstance().getModuleComponent(this);
		
		JToolBar toolbar =
			JAlgoGUIConnector.getInstance().getModuleToolbar(this);
				
		JMenu menu = 
			JAlgoGUIConnector.getInstance().getModuleMenu(this);
		application.startWelcomescreen(contentPane,toolbar,menu,this);
		
	}

	@Override
	public void setDataFromFile(ByteArrayInputStream data) {
		application.loadFile(data);
    }

	@Override
	public ByteArrayOutputStream getDataForFile() {
		return application.saveFile();
	}

	@Override
	public void print() {
	}
}