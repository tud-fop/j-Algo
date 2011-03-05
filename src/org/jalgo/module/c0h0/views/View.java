package org.jalgo.module.c0h0.views;

import java.util.EventListener;

import javax.swing.JPanel;

/**
 *  Abstract superclass of all views.
 * @author hendrik
 *
 */
public abstract class View extends JPanel implements EventListener {
	
	
	private static final long serialVersionUID = 9003570112807424330L;

	public View() {	
	}
	
	/**
	 * Renders the view.
	 * @return success
	 */
	public boolean render() {
		return true;
	}
	
	/**
	 * Updates the view.
	 * @return success
	 */
	public boolean update() {
		this.render();
		return true;
	}

	/**
	 * only for TeamViews (transView)
	 */
	public void teamUpdate() {
	}
}
