package org.jalgo.main.gui.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import org.jalgo.main.gui.JAlgoGUIConnector;

/**
 * This class represents a simple mouse listener, whose only function is to
 * update the text of the j-Algo status line. For this, a component, whose
 * description text should be displayed in status line, has only to add the
 * singleton instance of <code>StatusLineUpdater</code> as a mouse listener.
 * 
 * @author Alexander Claus
 */
public class StatusLineUpdater
extends MouseAdapter {

	private static StatusLineUpdater instance;

	private StatusLineUpdater() {
		// no initialization neccessary
	}

	public static StatusLineUpdater getInstance() {
		if (instance == null) instance = new StatusLineUpdater();
		return instance;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		JAlgoGUIConnector.getInstance().setStatusMessage(
			((JComponent)e.getSource()).getToolTipText());
	}

	@Override
	public void mouseExited(MouseEvent e) {
		JAlgoGUIConnector.getInstance().setStatusMessage(null);
	}
}