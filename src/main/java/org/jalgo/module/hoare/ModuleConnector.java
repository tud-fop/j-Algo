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
package org.jalgo.module.hoare;

/**
*
* @author Tomas
* 
* Manipulated testModule.ModuleConnector
* 
*/


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.view.View;
import org.jalgo.module.hoare.model.Model;
import org.jalgo.module.hoare.control.Controller;


public class ModuleConnector
extends AbstractModuleConnector {

	View view;
	Model model;
	Controller controller;
	
	@Override
	public void init() {

		model = new Model();
		//view = new View(this);
		view = new View(this);
		controller = new Controller(view, model, this);
		
		model.addObserver(view);
		view.setModel(model);
		view.setConroller(controller);
	}

	@Override
	public void run() {
		view.installWelcomeScreen();
	}

	@Override
	public void setDataFromFile(ByteArrayInputStream data) {
		try {
			ObjectInputStream input = new ObjectInputStream(data);
			controller.load(input);
			input.close();
		} catch (IOException e) {
			view.setStatusText(Messages.getString("hoare", "out.loadError"));
			e.printStackTrace();
		} catch (ClassNotFoundException e){
			view.setStatusText(Messages.getString("hoare", "out.loadError"));
			e.printStackTrace();
		}
	}

	@Override
	public ByteArrayOutputStream getDataForFile() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream output = new ObjectOutputStream(baos);
			controller.save(output);
			output.close();
		} catch (IOException e) {
			view.setStatusText(Messages.getString("hoare", "out.saveError"));
			e.printStackTrace();
		}
		return baos;
	}

	@Override
	public void print() {
	// here is no action performed in test module
	}
}