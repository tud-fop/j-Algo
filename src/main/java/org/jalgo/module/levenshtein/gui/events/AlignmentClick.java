package org.jalgo.module.levenshtein.gui.events;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.jalgo.module.levenshtein.gui.components.AlignPanel;

public class AlignmentClick implements MouseListener {

	private AlignPanel panel;
	
	public AlignmentClick(AlignPanel panel) {
		this.panel = panel;
	}
	
	public void mouseClicked(MouseEvent e) {
		panel.clicked();
	}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {
		panel.mouseEntered();
	}

	public void mouseExited(MouseEvent e) {
		panel.mouseExited();
	}

}
