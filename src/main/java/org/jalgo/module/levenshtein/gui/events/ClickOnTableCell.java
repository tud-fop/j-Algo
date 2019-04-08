package org.jalgo.module.levenshtein.gui.events;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.jalgo.module.levenshtein.gui.components.TablePanel;

public class ClickOnTableCell implements MouseListener {

	private int j;
	private int i;
	private TablePanel panel;
	
	public ClickOnTableCell(int j, int i, TablePanel panel) {
		this.j = j;
		this.i = i;
		this.panel = panel;
	}
	
	public void mouseClicked(MouseEvent e) {
		panel.cellClicked(j, i);
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
