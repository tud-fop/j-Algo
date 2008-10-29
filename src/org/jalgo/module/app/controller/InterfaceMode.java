package org.jalgo.module.app.controller;

/**
 * Provides the current state of the module. First, a Semiring is selected (mode
 * SEMIRING_EDITING), then the Graph is edited (GRAPH_EDITING), and then the
 * Aho-Algorithm is run and visualized (ALGORITHM_DISPLAY). The user can switch
 * between the last two modes.
 */
public enum InterfaceMode {
	/**
	 * Selection of a semiring.
	 */
	SEMIRING_EDITING,
	/**
	 * Editing of the graph.
	 */
	GRAPH_EDITING,
	/**
	 * Running of the aho algorithm.
	 */
	ALGORITHM_DISPLAY
}
