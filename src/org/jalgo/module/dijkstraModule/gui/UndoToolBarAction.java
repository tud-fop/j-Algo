/*
 * Created on Jun 8, 2005
 */
 
package org.jalgo.module.dijkstraModule.gui;

import java.io.Serializable;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.util.DefaultExceptionHandler;
import org.jalgo.module.dijkstraModule.actions.UndoAction;

/**
 * @author Hannes Straß
 */
public class UndoToolBarAction extends Action implements Serializable
{

	private Controller controller;

	public UndoToolBarAction(Controller c)
	{
		this.controller = c;
		setText("Rückgängig");
		setToolTipText("Macht die letzte Änderung rückgängig.");
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/undo.gif"));
	}

	public void run()
	{
		try
		{
			new UndoAction(controller);
		}
		catch (Exception e)
		{
			new DefaultExceptionHandler(e);
		}
	}

}
