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
 * Created on Jun 8, 2005
 */
 
package org.jalgo.module.dijkstraModule.gui;

import java.io.Serializable;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.util.DefaultExceptionHandler;
import org.jalgo.module.dijkstraModule.actions.RedoAction;

/**
 * @author Hannes Stra"s
 */
public class RedoToolBarAction extends Action implements Serializable
{

	private Controller controller;

	public RedoToolBarAction(Controller c)
	{
		this.controller = c;
		setText("Wiederherstellen");
		setToolTipText("Wiederherstellen");
		setImageDescriptor(
			ImageDescriptor.createFromURL(getClass().getResource("/dijkstra_pix/redo.gif")));
	}

	public void run()
	{
		try
		{
			new RedoAction(controller);
		}
		catch (Exception e)
		{
			new DefaultExceptionHandler(e);
		}
	}

}
