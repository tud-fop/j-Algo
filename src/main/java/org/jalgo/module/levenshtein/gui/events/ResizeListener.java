package org.jalgo.module.levenshtein.gui.events;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import org.jalgo.module.levenshtein.gui.components.ResizeComponent;

public class ResizeListener implements ComponentListener {

	private ResizeComponent component;
	
	public ResizeListener(ResizeComponent component) {
		this.component = component;
	}
	
	public void componentResized(ComponentEvent e) {
		component.onResized();
	}

	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

}
