/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2010 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
/**
 * 
 */
package org.jalgo.module.am0c0.core;

import org.jalgo.module.am0c0.gui.View;

/**
 * Abstract controller class which defines the basic functionality for:
 * <p>
 * <li>{@link Simulator}</li>
 * <li>{@link Editor}</li>
 * <li>{@link Transformator}</li>
 * </p>
 * <br />
 * 
 * @author Franz Gregor
 * @author Max Leuth&auml;user
 */
public abstract class Subcontroller {
	protected View view;
	protected Controller controller;

	/**
	 * @return the {@link View} which is used to handle user input und show
	 *         calculation results.
	 */
	public View getView() {
		return view;
	}

	/**
	 * Set the presentation mode for the {@link View} which is used by this
	 * {@link Subcontroller}.
	 * 
	 * @param presentationMode
	 *            <b>True</b> will activate the presentation mode which
	 *            increases font sizes, <b>false</b> will turn it off.
	 */
	public void setPresentationMode(boolean presentationMode) {
		view.setPresentationMode(presentationMode);
	}

	/**
	 * @return the {@link Controller}
	 */
	public Controller getController() {
		return controller;
	}
}
