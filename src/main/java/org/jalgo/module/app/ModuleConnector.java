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
package org.jalgo.module.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.app.controller.MainController;
import org.jalgo.module.app.core.SemiRing;
import org.jalgo.module.app.core.graph.Graph;

/**
 * Provides the connection to j-Algo.
 */
public class ModuleConnector extends AbstractModuleConnector {

	private MainController mainController;

	@Override
	public void init() {
		mainController = new MainController(this);
	}

	@Override
	public void run() {
	}

	@Override
	public boolean close() {
		mainController.close();
		return true;
	}
	
	@Override
	public void setDataFromFile(ByteArrayInputStream data) {
		Graph graph = null;
		SemiRing semiring = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(data);
			graph = (Graph) ois.readObject();
			semiring = (SemiRing) ois.readObject();
			ois.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), Messages.getString(
					"app", "ModuleConnector.IOError"), Messages.getString(
					"app", "ModuleConnector.Error"), JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(new JFrame(), Messages.getString(
					"app", "ModuleConnector.ClassError"), Messages.getString(
					"app", "ModuleConnector.Error"), JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		mainController.load(graph, semiring);
	}

	@Override
	public ByteArrayOutputStream getDataForFile() {
		Graph graph = mainController.getGraphController().getGraph();
		SemiRing semiring = mainController.getSemiringController()
				.getSemiRing();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(out);
			oos.writeObject(graph);
			oos.writeObject(semiring);
			oos.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), Messages.getString(
					"app", "ModuleConnector.SavingError"), Messages.getString(
					"app", "ModuleConnector.Error"), JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return out;
	}

	@Override
	public void print() {
	}
}