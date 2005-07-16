/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
 * Created on 23.05.2005
 *
 */
package org.jalgo.module.dijkstraModule.actions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.model.Graph;

/**
 * @author Frank
 *
 * Klasse zum Speichern/Laden eines Graphen im/aus dem Dateisystem
 * 
 */
public class FileAction extends GraphAction {

	protected boolean m_bSave;

	protected Graph m_newGraph;

	public FileAction(Controller ctrl, boolean bSave) throws ActionException {
		super(ctrl);
		m_bSave = bSave;
		this.registerAndDo(m_bSave == false);
	}

	public boolean doAction() throws ActionException {
		if (m_bSave == true) {
			save(getController().getGraph());
			return false;
		} else if (open())
			getController().setGraph(m_newGraph);
		return true;
	}

	public boolean undoAction() throws ActionException {
		if (m_bSave == false)
			getController().setGraph(m_oldGraph);
		return (m_bSave == false);
	}

	/*
	 * Author: Steven Voigt
	 * 
	 * Speichert Graph im FS 
	 * 
	 * */
	protected boolean save(Graph g) throws ActionException {
		Display disp = Display.getCurrent();
		Shell shell = new Shell(disp);

		FileDialog dialog = new FileDialog(shell, SWT.SAVE);

		final String[] FILTER = { "Graph Objekt (*.graph)", "Alle Dateien (*.*)" };

		dialog.setFilterNames(FILTER);
		dialog.setFileName("*.gra");
		String file = dialog.open();
		if (file != null) {
			ObjectOutputStream out = null;
			try {
				out = new ObjectOutputStream(new FileOutputStream(file));
				out.writeObject(g);
				out.flush();
				return true;
			} catch (IOException ioe) {
				throw new ActionException(ioe.getMessage());
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException ioex) {
						throw new ActionException(ioex.getMessage());
					}
				}
			}
		}
		return false;
	}

	/*
	 * Author: Steven Voigt
	 * 
	 * Lädt Graph aus Datei im FS 
	 * 
	 * */
	protected boolean open() throws ActionException {
		Display disp = Display.getCurrent();
		Shell shell = new Shell(disp);
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		final String[] FILTER = { "Graph Objekt (*.graph)", "Alle Dateien (*.*)" };

		dialog.setFilterNames(FILTER);

		dialog.setFileName("*.gra");
		//dialog.setText("(Einkaufs)Laden");
		final String[] Ext = { "*.gra" };
		dialog.setFilterNames(Ext);

		String file = dialog.open();
		m_newGraph = null;
		if (file != null) {
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				m_newGraph = (Graph) in.readObject();
				return true;
			} catch (IOException ioe) {
				throw new ActionException(ioe.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new ActionException(cnfe.getMessage());
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException ioex) {
						ioex.printStackTrace();
					}
				}
			}

		}
		return false;
	}
}
