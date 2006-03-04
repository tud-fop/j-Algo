package org.jalgo.module.dijkstra.gui.event;

import java.awt.Point;

import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.gui.components.GraphDisplay;

/**
 * This class defines an interface for states, which are used in graph display.
 * Subclasses collaborate with the modes in the <code>Controller</code>. Those
 * states handle mouse events on their individually way.<br>
 * This class is part of the state design pattern.
 * 
 * @author Alexander Claus
 */
public abstract class GraphDisplayState {

	protected GraphDisplay display;
	protected Controller controller;

	public GraphDisplayState(GraphDisplay display, Controller controller) {
		this.display = display;
		this.controller = controller;
	}

	public abstract void mousePressed(Point p);

	public abstract void mouseReleased(Point p);

	public abstract void mouseClicked(Point p);

	public abstract void mouseMoved(Point p);

	public abstract void mouseDragged(Point p);
}