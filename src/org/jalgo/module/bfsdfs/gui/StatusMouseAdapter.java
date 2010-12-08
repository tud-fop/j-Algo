package org.jalgo.module.bfsdfs.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;

/**
 * Utility class that extends {@linkplain MouseAdapter}. In this class, an
 * additional string can be stored. <br> The methods
 * {@link #mouseExited(MouseEvent)} and {@link #mouseEntered(MouseEvent)} are
 * preset to hide or show the status message in the status bar, respectively.
 * 
 * @param status : The stored string. Note that the string is not the string
 * of the status message, but the location where the actual string is stored
 * in the properties files.
 * @author Florian Dornbusch
 */
public class StatusMouseAdapter extends MouseAdapter {
	private String status;
	
	public StatusMouseAdapter(String status) {
		this.status = status;
	}
	public void mouseExited(MouseEvent e) {
		JAlgoGUIConnector.getInstance().setStatusMessage(null);
	}
	public void mouseEntered(MouseEvent e) {
		JAlgoGUIConnector.getInstance().setStatusMessage(
			Messages.getString("bfsdfs", status));
	}
}
