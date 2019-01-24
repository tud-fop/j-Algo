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
package org.jalgo.module.am0c0.core;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.module.am0c0.gui.EditorView;
import org.jalgo.module.am0c0.gui.SimulationView;
import org.jalgo.module.am0c0.gui.TransformationView;

/**
 * Main controller. Handles all components and supports switching between them.
 * 
 * @author Franz Gregor
 * @author Max Leuth&auml;user
 */
public class Controller {
	private JComponent rootPanel;
	@SuppressWarnings("unused")
	private JToolBar toolbarPanel;
	private JMenu menuPanel;
	private JPanel panelLayers;

	private Editor editor;
	private Simulator simulator;
	private Transformator transformator;

	private static final String EDITOR = "e";
	private static final String SIMULATION = "s";
	private static final String TRANSFORMATION = "t";

	public Controller(JComponent rootPanel, JToolBar toolbarPanel,
			JMenu menuPanel) {
		this.rootPanel = rootPanel;
		this.toolbarPanel = toolbarPanel;
		this.menuPanel = menuPanel;

		editor = new Editor(this);
		simulator = new Simulator(this);
		transformator = new Transformator(this);

		setViews();
		showEditor();
		createMenuBar();
	}

	/**
	 * Create the menu bar entries.
	 */
	private void createMenuBar() {
		menuPanel.add(new PresentationAction(this));
	}

	/**
	 * Add all views in a {@link CardLayout} to switch them easily.
	 */
	private void setViews() {
		rootPanel.setLayout(new BorderLayout());
		panelLayers = new JPanel(new CardLayout());
		panelLayers.add(editor.getView(), EDITOR);
		panelLayers.add(transformator.getView(), TRANSFORMATION);
		panelLayers.add(simulator.getView(), SIMULATION);
		rootPanel.add(panelLayers);
	}

	/**
	 * Switch to the {@link EditorView}.
	 */
	public void showEditor() {
		CardLayout cl = (CardLayout) (panelLayers.getLayout());
		cl.show(panelLayers, EDITOR);
	}

	/**
	 * Switch to the {@link SimulationView}.
	 */
	public void showSimulator() {
		CardLayout cl = (CardLayout) (panelLayers.getLayout());
		cl.show(panelLayers, SIMULATION);
	}

	/**
	 * Switch to the {@link TransformationView}.
	 */
	public void showTransformator() {
		CardLayout cl = (CardLayout) (panelLayers.getLayout());
		cl.show(panelLayers, TRANSFORMATION);
	}

	/**
	 * @return the {@link Editor}
	 */
	public Editor getEditor() {
		return editor;
	}

	/**
	 * @return the {@link Simulator}
	 */
	public Simulator getSimulator() {
		return simulator;
	}

	/**
	 * @return the {@link Transformator}
	 */
	public Transformator getTransformator() {
		return transformator;
	}

	/**
	 * Write a message to the j-Algo statusbar.
	 * 
	 * @param message
	 */
	public void writeOnStatusbar(String message) {
		JAlgoGUIConnector.getInstance().setStatusMessage(message);
	}

	/**
	 * Set all views in presentation mode or not.
	 * 
	 * @param presentation
	 */
	public void setPresentationMode(boolean presentation) {
		editor.setPresentationMode(presentation);
		simulator.setPresentationMode(presentation);
		transformator.setPresentationMode(presentation);
	}
}
