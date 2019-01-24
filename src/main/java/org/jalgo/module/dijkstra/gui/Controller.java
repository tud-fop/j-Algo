/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
 * @author Frank Staudinger, Julian Stecklina, Martin Winter, Hannes Strass,
 *         Steven Voigt
 * 
 * 
 */
package org.jalgo.module.dijkstra.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Timer;

import org.jalgo.main.InternalErrorException;
import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.module.dijkstra.ModuleConnector;
import org.jalgo.module.dijkstra.actions.Action;
import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.ActionStack;
import org.jalgo.module.dijkstra.actions.ShowAlgorithmPageAction;
import org.jalgo.module.dijkstra.actions.ShowEditPageAction;
import org.jalgo.module.dijkstra.gui.components.GraphDisplay;
import org.jalgo.module.dijkstra.model.DijkstraAlgorithm;
import org.jalgo.module.dijkstra.model.Edge;
import org.jalgo.module.dijkstra.model.Graph;
import org.jalgo.module.dijkstra.model.Node;
import org.jalgo.module.dijkstra.model.State;

/**
 * The Controller is the central point of the Dijkstra's MVC-architecture. It
 * controls all operations and notifys it's observers about changes in the
 * model.
 * 
 * @author Alexander Claus, Frank Staudinger, Julian Stecklina, Martin Winter,
 *         Hannes Strass, Steven Voigt
 */
public class Controller
extends Observable {

	private class _Observable
	extends Observable {

		public _Observable() {
			super();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Observable#clearChanged()
		 */
		protected synchronized void clearChanged() {
			super.clearChanged();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Observable#setChanged()
		 */
		protected synchronized void setChanged() {
			super.setChanged();
		}
	}

	private class _AlgoAnimator
	implements ActionListener {

		private Controller m_ctrl;
		private Timer timer;

		public _AlgoAnimator(Controller ctrl) {
			m_ctrl = ctrl;
			timer = new Timer(ctrl.m_iAniMillis, this);
		}

		public void start() {
			timer.setDelay(m_ctrl.m_iAniMillis);
			timer.start();
		}

		public void stop() {
			timer.stop();
		}

		public void actionPerformed(ActionEvent e) {
			if (m_ctrl.m_iAniMillis > 0) {
				int nStep = m_ctrl.getCurrentStep();
				if (m_ctrl.hasNextStep(nStep) == true) {
					nStep = m_ctrl.getNextStepIndex();
					State state = m_ctrl.getState(nStep);
					if (state != null) {
						m_ctrl.setStatusbarText(state.getDescriptionEx());
						m_ctrl.setModifiedFlag();
					}
				}
				else {
					m_ctrl.stopAnimation();
					m_ctrl.setModifiedFlag();
					return;
				}
			}
		}
	}

	protected int m_iAniMillis = -1;
	protected ActionStack m_actions;
	protected Graph m_curGraph;

	// When in editing mode but with no visual tool selected
	public static final int MODE_NO_TOOL_ACTIVE = 0;
	public static final int MODE_ADD_MOVE_NODE = 1;
	public static final int MODE_DELETE_NODE = 2;
	public static final int MODE_ADD_WEIGH_EDGE = 4;
	public static final int MODE_DELETE_EDGE = 8;
	public static final int MODE_ALGORITHM = 16; // When in algorithm mode.

	protected static final int _MAX_MODE = MODE_ALGORITHM;
	protected static final int _MIN_MODE = MODE_NO_TOOL_ACTIVE;

	protected int m_iEditingMode = MODE_NO_TOOL_ACTIVE;

	protected _Observable m_StatusbarObservable;
	protected HashMap<Integer, List<State>> m_mapNodes2AlgoStatesArrayLists =
		new HashMap<Integer, List<State>>();

	protected int m_nCurStep = 0;

	private final ModuleConnector connector;
	private _AlgoAnimator m_animator;
	private GUIController gui;

	public Controller(ModuleConnector connector) {
		super();
		this.connector = connector;
		m_actions = new ActionStack(0);
		m_curGraph = new Graph(new ArrayList<Node>(), new ArrayList<Edge>());
		m_StatusbarObservable = new _Observable();

		// Create stuff here...
		gui = new GUIController(connector, this);
		gui.createGUI();
		m_animator = new _AlgoAnimator(this);
	}

	/**
	 * deserialize the data, given by the
	 * 
	 * @param data must be a bytearrayinputstream with int for mode
	 * @author Steven Voigt
	 */
	private void deserialize(ByteArrayInputStream data) {
		try {
			ObjectInputStream serializedObjects = new ObjectInputStream(data);

			int newMode = serializedObjects.readInt();
			setGraph((Graph)serializedObjects.readObject());
			switch (newMode) {
				case MODE_ADD_MOVE_NODE:
					setEditingMode(MODE_ADD_MOVE_NODE);
					break;

				case MODE_ADD_WEIGH_EDGE:
					setEditingMode(MODE_ADD_WEIGH_EDGE);
					break;

				case MODE_DELETE_NODE:
					setEditingMode(MODE_DELETE_NODE);
					break;

				case MODE_ALGORITHM:
					setEditingMode(MODE_ALGORITHM);
					break;

				case MODE_NO_TOOL_ACTIVE:
					setEditingMode(MODE_NO_TOOL_ACTIVE);
					break;

				default:
					setEditingMode(MODE_NO_TOOL_ACTIVE);
			}

		}
		catch (IOException IOExc) {
			java.lang.System.err.println(IOExc);
			throw new InternalErrorException(IOExc.getMessage());

		}
		catch (ClassNotFoundException cnfExc) {
			java.lang.System.err.println("Error while loading.");
			throw new InternalErrorException(cnfExc.getMessage());
		}
		try {
			if (this.getEditingMode() == MODE_ALGORITHM) {
				new ShowAlgorithmPageAction(this);

			}
			else {
				new ShowEditPageAction(this);
			}

			this.setModifiedFlag();
		}
		catch (ActionException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Get the graph to cast it in a ByteArrayOutputStream
	 * 
	 * @return the graph as an ByteArrayOutputStream with int for the actual
	 *         mode
	 * @author Steven Voigt
	 */
	private ByteArrayOutputStream serialize() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream serializedObjects = new ObjectOutputStream(
				outputStream);

			serializedObjects.writeInt(getEditingMode());
			serializedObjects.writeObject(getGraph());
			serializedObjects.close();
		}
		catch (IOException IOExc) {
			java.lang.System.err.println(IOExc);
			throw new InternalErrorException(IOExc.getMessage());
		}
		return outputStream;
	}

	/**
	 * Get the serialized data to store it in a file.
	 * 
	 * @return the serialiazed data
	 * @author Steven Voigt
	 */
	public ByteArrayOutputStream getSerializedData() {
		return serialize();
	}

	/**
	 * Set data. that was loaded from file. This data will be deserialized to
	 * restore the saved state.
	 * 
	 * @param data earlier serialized data
	 * @author Steven Voigt
	 */
	public void setSerializedData(ByteArrayInputStream data) {
		deserialize(data);
	}

	/**
	 * (MW) Call setModifiedFlag() after calling this method, except when
	 * calling from an action. When setModifiedFlag() was called from within
	 * this method, updates occurred multiple times because
	 * registerAndDoAction() already calls it.
	 * 
	 * @param mode The new mode (_MIN_MODE < mode < _MAX_MODE)
	 */
	public void setEditingMode(int mode) {
		m_iEditingMode = (((mode > _MAX_MODE) || (mode < _MIN_MODE)) ?
			MODE_NO_TOOL_ACTIVE : mode);
		if (mode == MODE_ALGORITHM) {
			computeAlgoStates();
		}
	}

	/**
	 * @return Returns the m_iEditingMode field
	 */
	public int getEditingMode() {
		return m_iEditingMode;
	}

	/**
	 * @author Frank Staudinger
	 * 
	 * This function recomputes the Dijkstra for the current graph
	 */
	protected void computeAlgoStates() {
		m_nCurStep = 0;
		m_mapNodes2AlgoStatesArrayLists.clear();
		DijkstraAlgorithm DijkstraAlgorithm = new DijkstraAlgorithm(getGraph());
		Graph graph = getGraph();
		Node prevStartNode = graph.getStartNode();
		for (Node node : graph.getNodeList()) {
			boolean bStart = node.isStart();
			DijkstraAlgorithm.generateStates(node);
			ArrayList<State> arList = new ArrayList<State>();

			int nStateCount = DijkstraAlgorithm.getStateCount();

			for (int i = 0; i < nStateCount; i++) {
				DijkstraAlgorithm.gotoState(i);
				arList.add(DijkstraAlgorithm.getCurrentState());
			}
			node.setStart(bStart);
			m_mapNodes2AlgoStatesArrayLists.put(new Integer(node.getIndex()),
				arList);
		}

		if (prevStartNode != null) {
			prevStartNode.setStart(true);
		}
	}

	/**
	 * @param nStep The zero-based # of the state
	 * @return Returns the state of the given number
	 */
	public State getState(int nStep) {
		Node node = getGraph().getStartNode();
		if (node == null) return null;
		List arList = m_mapNodes2AlgoStatesArrayLists.get(node.getIndex());
		if (nStep >= arList.size()) nStep = arList.size() - 1;
		else if (nStep < 0) nStep = 0;

		State state = (State)arList.get(nStep);
		m_nCurStep = nStep;
		return state;
	}

	/**
	 * @return Returns the next state or null of the Dijkstra
	 */
	public State getNextState() {
		m_nCurStep++;
		return getState(m_nCurStep);
	}

	/**
	 * @return Returns the # of the current step
	 */
	public int getCurrentStep() {
		return m_nCurStep;
	}

	/**
	 * @return Returns the # of steps for the current start node
	 */
	public int getStepCount() {
		Node node = getGraph().getStartNode();
		if (node == null) return 0;
		List arList = m_mapNodes2AlgoStatesArrayLists.get(node.getIndex());
		return ((arList != null) ? (arList.size()) : 0);
	}

	/**
	 * @param nStep Zero-based index of the current step
	 * @return Returns true if we have a next step in the Dijkstra
	 */
	public boolean hasNextStep(int nStep) {
		Node node = getGraph().getStartNode();
		if (node == null) return false;
		List arList = m_mapNodes2AlgoStatesArrayLists.get(node.getIndex());
		return ((arList != null) ? (nStep + 1) < arList.size() : false);
	}

	/**
	 * @param nStep Zero-based index of the current step
	 * @return Returns true if we have a previous step in the Dijkstra
	 */
	public boolean hasPrevStep(int nStep) {
		Node node = getGraph().getStartNode();
		if (node == null) return false;
		List arList = m_mapNodes2AlgoStatesArrayLists.get(node.getIndex());
		return ((arList != null) ? (nStep > 0) : false);
	}

	/**
	 * @param nStep Zero-based index of the current step
	 * @return Returns true if we have a next macro step in the Dijkstra
	 */
	public boolean hasNextMacroStep(int nStep) {
		if (hasNextStep(nStep) == false) return false;
		Node node = getGraph().getStartNode();
		if (node == null) return false;
		List arList = m_mapNodes2AlgoStatesArrayLists.get(node.getIndex());
		Iterator iter = arList.iterator();
		for (int i = 0; iter.hasNext(); i++) {
			State state = (State)iter.next();
			if (i > nStep && (state.isMacro() == true)) return true;
		}
		return false;
	}

	/**
	 * @param nStep Zero-based index of the current step
	 * @return Returns true if we have a previous macro step in the Dijkstra
	 */
	public boolean hasPrevMacroStep(int nStep) {
		if (hasPrevStep(nStep) == false) return false;
		Node node = getGraph().getStartNode();
		if (node == null) return false;
		List arList = m_mapNodes2AlgoStatesArrayLists.get(node.getIndex());
		Iterator iter = arList.iterator();
		for (int i = 0; iter.hasNext(); i++) {
			State state = (State)iter.next();
			if (i < nStep && (state.isMacro() == true)) return true;
		}
		return false;
	}

	/**
	 * @return Returns the # of the previous step in the Dijkstra
	 */
	public int getPrevStepIndex() {
		return this.getCurrentStep() - 1;
	}

	/**
	 * @return Returns the # of the next step in the Dijkstra
	 */
	public int getNextStepIndex() {
		return this.getCurrentStep() + 1;
	}

	/**
	 * @return Returns the # of the previous macro step in the Dijkstra
	 */
	public int getPrevMacroStepIndex() {
		Node node = getGraph().getStartNode();
		if (node == null) return 0;
		List arList = m_mapNodes2AlgoStatesArrayLists.get(node.getIndex());

		for (int i = getCurrentStep() - 1; i >= 0; i--) {
			State state = (State)arList.get(i);
			if (state == null) break;
			else if (state.isMacro() == true) return i;
		}
		return 0;
	}

	/**
	 * @return Returns the # of the previous macro step in the Dijkstra
	 */
	public int getNextMacroStepIndex() {
		Node node = getGraph().getStartNode();
		if (node == null) return 0;
		List arList = m_mapNodes2AlgoStatesArrayLists.get(node.getIndex());

		for (int i = getCurrentStep() + 1; i < arList.size(); i++) {
			State state = (State)arList.get(i);
			if (state == null) break;
			else if (state.isMacro() == true) return i;
		}
		return 0;
	}

	/**
	 * Show the part of the GUI where the user can manipulate Graphs.
	 */
	public void showEditingPage() {
		this.setEditingMode(MODE_NO_TOOL_ACTIVE);
		resetGraphVisuals();
		gui.showEditModePanel();
		this.setModifiedFlag();
	}

	/**
	 * Shows the part of the GUI where the user witnesses the progression of
	 * Dijkstra's Algorithm.
	 */
	public void showAlgorithmPage() {
		gui.showAlgorithmModePanel();
		this.setEditingMode(MODE_ALGORITHM);
	}

	/**
	 * @return Returns the m_curGraph field
	 */
	public Graph getGraph() {
		return m_curGraph;
	}

	/**
	 * (MW) Call setModifiedFlag() after calling this method, except when
	 * calling from an action. When setModifiedFlag() was called from within
	 * this method, updates occurred multiple times because
	 * registerAndDoAction() already calls it.
	 * 
	 * @param graph
	 * @return the old graph
	 */
	public Graph setGraph(Graph graph) {

		Graph oldGraph = (Graph)m_curGraph.clone();
		this.m_curGraph = graph;
		// i don't know how this works in detail, but because of the permanent
		// copying of the graph the mapping between some nodes and its visuals
		// is crashed, here a workaround - Alexander
		for (Node node : graph.getNodeList()) {
			node.getVisual().updateModel(node);
			node.getVisual().updateLocation(GraphDisplay.getScreenSize());
			node.setActive(false);
			node.setHighlighted(false);
			node.getVisual().update();
		}
		for (Edge edge : graph.getEdgeList()) {
			edge.getVisual().updateModel(edge);
			edge.getVisual().updateLocation(GraphDisplay.getScreenSize());
			edge.setActive(false);
			edge.setHighlighted(false);
			edge.getVisual().update();
		}

		if (this.m_iEditingMode == Controller.MODE_ALGORITHM) computeAlgoStates();
		if (!oldGraph.equals(graph))
			connector.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
		return oldGraph;
	}

	/**
	 * Updates the visual components of the graph elements.
	 */
	private void resetGraphVisuals() {
		for (Node node : m_curGraph.getNodeList()) {
			node.getVisual().setControllerMode(getEditingMode());
			node.getVisual().update();
		}
		for (Edge edge : m_curGraph.getEdgeList()) {
			edge.getVisual().setControllerMode(getEditingMode());
			edge.getVisual().update();
		}
	}

	/**
	 * @author Frank Staudinger
	 * 
	 * It is intend to call this function from the contructor of Your Action
	 * derived class
	 * 
	 * @param act The Action You want to be executed and registered in the
	 *            action stack
	 * @param bRegister True if the Action should be registered in the action
	 *            stack
	 * @return Returns the result of act.doAction()
	 * @throws ActionException
	 */
	public boolean registerAndDoAction(Action act, boolean bRegister)
	throws ActionException {
		if (bRegister == true) m_actions.add(act);

		if (act.doAction()) {
			setModifiedFlag();
			return true;
		}
		if (bRegister == true) m_actions.movePrevious();
		return false;
	}

	/**
	 * Undoes the last Action
	 * 
	 * @return Returns the last actions doAction() function result
	 * @throws ActionException
	 */
	public boolean undoAction()
	throws ActionException {
		Action pAction = m_actions.movePrevious();
		return ((pAction == null) ? false : pAction.undoAction());
	}

	/**
	 * @return Returns true if the action stack is not empty
	 */
	public boolean hasUndoAction() {
		return m_actions.canMovePrevious();
	}

	/**
	 * Redoes the last Action
	 * 
	 * @return Returns the last actions doAction() function result
	 * @throws ActionException
	 */
	public boolean redoAction()
	throws ActionException {
		Action pAction = m_actions.moveNext();
		return ((pAction == null) ? false : pAction.doAction());
	}

	/**
	 * @return Returns true if there is an action to redo
	 */
	public boolean hasRedoAction() {
		return m_actions.canMoveNext();
	}

	/**
	 * Call this function to inform the Controller's observers about changes
	 */
	public void setModifiedFlag() {
		gui.updateToolBar();
		super.setChanged();
		super.notifyObservers(this);
		super.clearChanged();
	}

	public void registerStatusbarObserver(Observer observer) {
		this.m_StatusbarObservable.addObserver(observer);
	}

	public void setStatusbarText(String strText) {
		gui.setStatusbarText(strText);
		this.m_StatusbarObservable.setChanged();
		this.m_StatusbarObservable.notifyObservers(this);
		this.m_StatusbarObservable.clearChanged();
	}

	public void startAnimation(int iMilliseconds) {
		this.m_iAniMillis = iMilliseconds;
		m_animator.start();
	}

	public void stopAnimation() {
		m_iAniMillis = -1;
		m_animator.stop();
	}

	/**
	 * @return Returns the m_iAniMillis.
	 */
	public int getAnimationMillis() {
		return m_iAniMillis;
	}
}