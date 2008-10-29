package org.jalgo.module.app.view.graph;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

/**
 * The graphical representation of an arbitrary element in the user interface.
 * This class is the superclass of NodeElement and EdgeElement.
 */
public abstract class GraphElement extends JComponent implements KeyListener, MouseListener,
		MouseMotionListener {

	protected GraphComponent component;
	protected HighlightState highlightState;

	public abstract void paint(Graphics2D g);

	public abstract boolean contains(Point2D point);

	public HighlightState getHighlightState() {
		return highlightState;
	}

	public void setHighlightState(HighlightState s) {
		highlightState = s;
	}

	public GraphComponent getComponent() {
		return component;
	}

	public void setComponent(GraphComponent component) {
		this.component = component;
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		component.setFocussedElement(this);
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}
}
