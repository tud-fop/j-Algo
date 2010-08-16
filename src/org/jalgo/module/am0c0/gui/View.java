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
package org.jalgo.module.am0c0.gui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jalgo.module.am0c0.gui.EditorView;
import org.jalgo.module.am0c0.gui.SimulationView;
import org.jalgo.module.am0c0.gui.TransformationView;

/**
 * Simple view class. All views implementing this:
 * 
 * <p>
 * <li>{@link EditorView}</li>
 * <li>{@link SimulationView}</li>
 * <li>{@link TransformationView}</li>
 * </p>
 * <br>
 * 
 * @author Max Leuth&auml;user
 */
public abstract class View extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Set the presentation mode which sets bigger fonts.
	 * 
	 * @param presentationMode
	 */
	abstract public void setPresentationMode(boolean presentationMode);

	/**
	 * Create all compontents.
	 */
	abstract protected void initComponents();

	/**
	 * Add a ButtonHandler to all important components. Implement this handler
	 * by yourself.
	 */
	abstract protected void attachButtonHandler();

	/**
	 * Set all attributes to the components of this view.
	 */
	abstract protected void initComponentAttributs();

	/**
	 * @param c
	 *            {@link JComponent} to embed in the {@link JScrollPane}.
	 * @return a new {@link JScrollPane} with the {@link JComponent} <b>c</b>
	 *         embedded.
	 */
	protected JScrollPane getScrollPane(JComponent c) {
		JScrollPane scrollPane = new JScrollPane(c);
		scrollPane.setMinimumSize(new Dimension(100, 100));
		scrollPane.setMaximumSize(new Dimension(200, 200));
		return scrollPane;
	}
}
