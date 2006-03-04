package org.jalgo.module.dijkstra.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.RedoAction;
import org.jalgo.module.dijkstra.actions.UndoAction;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.util.DefaultExceptionHandler;

/**
 * This class is responsible for handling clicks on toolbar buttons, namely:
 * undo and redo.
 * 
 * @author Alexander Claus
 */
public class ToolbarActionHandler
implements ActionListener {

	private final Controller controller;

	public ToolbarActionHandler(Controller controller) {
		this.controller = controller;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("undo")) try {
			new UndoAction(controller);
		}
		catch (ActionException ex) {
			new DefaultExceptionHandler(ex);
		}
		else if (e.getActionCommand().equals("redo")) try {
			new RedoAction(controller);
		}
		catch (ActionException ex) {
			new DefaultExceptionHandler(ex);
		}
	}
}