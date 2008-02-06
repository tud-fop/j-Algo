/*
 * j-Algo - a visualization tool for algorithm runs, especially useful for
 * students and lecturers of computer science. j-Algo is written in Java and
 * thus platform independent. Development is supported by Technische Universität
 * Dresden.
 *
 * Copyright (C) 2004-2008 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
package org.jalgo.module.heapsort;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.InternalErrorException;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.module.heapsort.anim.TimeEntity;
import org.jalgo.module.heapsort.anim.TimeRoot;
import org.jalgo.module.heapsort.model.Model;
import org.jalgo.module.heapsort.model.ModelListener;
import org.jalgo.module.heapsort.renderer.CanvasEntity;
import org.jalgo.module.heapsort.renderer.CanvasEntityFactory;
import org.jalgo.module.heapsort.renderer.Renderer;
import org.jalgo.module.heapsort.vis.Controller;
import org.jalgo.module.heapsort.vis.Visualisation;

/* MODULE TO-DO LIST (de todo un poco...)
 * 
 * TODO study mode animations
 */

/**
 * As demanded by j-Algo specification.
 * 
 * @author mbue
 */
public class ModuleConnector extends AbstractModuleConnector {
	
	private Model model;
	private GuiController gui;

	/* (non-Javadoc)
	 * @see org.jalgo.main.AbstractModuleConnector#getDataForFile()
	 */
	@Override
	public ByteArrayOutputStream getDataForFile() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			model.serialize(os);
		}
		catch (IOException e) {
			throw new InternalErrorException(e.getMessage());
		}
		return os;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.AbstractModuleConnector#init()
	 */
	@Override
	public void init() {
		Visualisation vis;
		Renderer renderer;
		CanvasEntityFactory f;
		CanvasEntity root;
		TimeEntity timeroot;
		Controller ctrl;
		
		// connect everything...
		model = new org.jalgo.module.heapsort.model.Heapsort();
		renderer = new org.jalgo.module.heapsort.renderer.RenderJava2D();
		f = renderer.createFactory();
		root = f.createRoot();
		vis = new org.jalgo.module.heapsort.vis.Heapsort(root, f);
		timeroot = new TimeRoot();
		ctrl = new Controller(model, vis, timeroot);
		gui = new GuiController(
				JAlgoGUIConnector.getInstance().getModuleComponent(this),
				JAlgoGUIConnector.getInstance().getModuleToolbar(this),
				JAlgoGUIConnector.getInstance().getModuleMenu(this),
				ctrl, renderer, root, timeroot);
		((Subject<ModelListener>)model).addListener(new ModelListener() {
			public void modelChanged() {
				setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.AbstractModuleConnector#print()
	 */
	@Override
	public void print() {
		// XXX ATM printing is not supported by j-Algo
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.AbstractModuleConnector#run()
	 */
	@Override
	public void run() {
		gui.run();
	}
	
	@Override
	public boolean close() {
		//gui.dispose(); XXX don't do this here
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.AbstractModuleConnector#setDataFromFile(java.io.ByteArrayInputStream)
	 */
	@Override
	public void setDataFromFile(ByteArrayInputStream data) {
		try {
			model.deserialize(data);
		}
		catch (IOException e) {
			throw new InternalErrorException(e.getMessage());
		}
	}

}
