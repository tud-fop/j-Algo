package org.jalgo.main.gui.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import org.jalgo.main.gui.JAlgoGUIConnector;


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