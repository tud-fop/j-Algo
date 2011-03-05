package org.jalgo.module.c0h0.views;

import java.util.EventListener;

/**
 * interface to coordinate view updates
 *
 */
public interface teamViewListener extends EventListener {
	/**
	 * updates team
	 * 
	 * @param e
	 */
	void teamUpdate(teamViewUpdateEvent e);
}
