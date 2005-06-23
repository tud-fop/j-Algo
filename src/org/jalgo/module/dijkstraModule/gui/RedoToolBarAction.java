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
			ImageDescriptor.createFromFile(null, "pix/redo.gif"));
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
