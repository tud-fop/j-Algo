/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/**
 * @author Frank Staudinger, Julian Stecklina, Martin Winter, Hannes Strass,
 *         Steven Voigt
 * 
 *  
 */
package org.jalgo.module.dijkstraModule.gui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.jalgo.main.InternalErrorException;
import org.jalgo.main.Jalgo;
import org.jalgo.main.JalgoMain;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.main.gui.actions.SaveAction;
import org.jalgo.main.gui.actions.SaveAsAction;
import org.jalgo.module.dijkstraModule.actions.Action;
import org.jalgo.module.dijkstraModule.actions.ActionStack;
import org.jalgo.module.dijkstraModule.actions.ShowAlgorithmPageAction;
import org.jalgo.module.dijkstraModule.actions.ShowEditPageAction;
import org.jalgo.module.dijkstraModule.model.DijkstraAlgorithm;
import org.jalgo.module.dijkstraModule.model.Graph;
import org.jalgo.module.dijkstraModule.model.Node;
import org.jalgo.module.dijkstraModule.model.State;
import org.jalgo.module.dijkstraModule.util.StatusbarText;

/**
 * @author Frank Staudinger, Julian Stecklina, Martin Winter, Hannes Strass,
 *         Steven Voigt
 * 
 * The Controller is the central point of the Dijkstra's MVC-architecture.
 * It controls all operations and notifys it's observers about changes in the model. 
 */

public class Controller extends Observable {

	private class _Observable extends Observable {

		/**
		 *  
		 */
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

	protected class _DisposeListener implements DisposeListener {
		Controller m_ctrl;

		public _DisposeListener(Controller ctrl) {
			m_ctrl = ctrl;
		}

		public void widgetDisposed(DisposeEvent e) {
			m_ctrl.stopAnimation();

			if (m_ctrl.isSaved() == false) {
				MessageBox msg = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_QUESTION
						| SWT.YES | SWT.NO);
				msg.setMessage("M\u00F6chten Sie Ihre \u00C4nderungen im Modul \"Dijkstra\" speichern?");
				if (msg.open() == SWT.YES) {
					JalgoMain main = Jalgo.getJalgoMain();
					main.saveFile();
				}
			}
		}
	}

	private class _AlgoAnimator implements Runnable {
		private Controller m_ctrl;

		public _AlgoAnimator(Controller ctrl) {
			m_ctrl = ctrl;

		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				if (m_ctrl.m_iAniMillis > 0) {
					int nStep = m_ctrl.getCurrentStep();
					if (m_ctrl.hasNextStep(nStep) == true) {
						nStep = m_ctrl.getNextStepIndex();
						State state = m_ctrl.getState(nStep);
						if (state != null) {
							m_ctrl.setStatusbarText(state.getDescriptionEx());
							m_ctrl.setModifiedFlag();
						}
					} else {
						m_ctrl.stopAnimation();
						m_ctrl.setModifiedFlag();
						return;
					}
				}
				m_ctrl.m_algorithmPage.getDisplay().timerExec(m_ctrl.m_iAniMillis, this);
			} catch (SWTException e) {

			}
		}
	}

	protected int m_iAniMillis = -1;

	protected ActionStack m_actions;

	protected Graph m_curGraph;

	protected Composite m_composite;

	protected PageFolder m_folder;

	protected Page m_editingPage;

	protected Page m_algorithmPage;

	public static final int MODE_NO_TOOL_ACTIVE = 0; // When in editing mode but

	// with no visual tool
	// selected.

	public static final int MODE_ADD_MOVE_NODE = 1;

	public static final int MODE_DELETE_NODE = 2;

	public static final int MODE_ADD_WEIGH_EDGE = 4;

	public static final int MODE_DELETE_EDGE = 8;

	public static final int MODE_ALGORITHM = 16; // When in algorithm mode.

	protected static final int _MAX_MODE = MODE_ALGORITHM;

	protected static final int _MIN_MODE = MODE_NO_TOOL_ACTIVE;

	protected int m_iEditingMode = MODE_NO_TOOL_ACTIVE;

	protected _Observable m_StatusbarObservable;

	protected StatusbarText m_strStatusbarText;

	protected HashMap m_mapNodes2AlgoStatesArrayLists = new HashMap();

	protected int m_nCurStep = 0;

	private ApplicationWindow m_appWindow;

	private SubToolBarManager m_toolBarManager;

	private UndoToolBarAction m_undoToolBarAction;

	private RedoToolBarAction m_redoToolBarAction;

	private SaveAction m_saveToolBarAction;

	private SaveAsAction m_saveAsToolBarAction;

	private _AlgoAnimator m_animator;

	private boolean m_bSaved = true;

	public Controller(Composite parent, SubToolBarManager toolBarManager, ApplicationWindow appWindow) {
		super();
		m_actions = new ActionStack(0);
		m_curGraph = new Graph(new ArrayList(), new ArrayList());
		m_StatusbarObservable = new _Observable();

		m_composite = parent;

		this.m_toolBarManager = toolBarManager;
		this.m_appWindow = appWindow;

		// Create stuff here...
		createGUI();
		m_animator = new _AlgoAnimator(this);
	}

	/**
	 * @author Steven Voigt
	 * deserialize the data, given by the 
	 * @param data: must be a bytearrayinputstream with int for mode 
	 */
	private void deserialize(ByteArrayInputStream data) {
		try {
			ObjectInputStream serializedObjects = new ObjectInputStream(data);

			int newMode = serializedObjects.readInt();
			setGraph((Graph) serializedObjects.readObject());
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

		} catch (IOException IOExc) {
			java.lang.System.err.println(IOExc);
			throw new InternalErrorException(IOExc.getMessage());

		} catch (ClassNotFoundException cnfExc) {
			java.lang.System.err.println("Error while loading.");
			throw new InternalErrorException(cnfExc.getMessage());
		}
		try {
			if (this.getEditingMode() == MODE_ALGORITHM) {
				new ShowAlgorithmPageAction(this);

			} else {
				new ShowEditPageAction(this);
			}

			this.setModifiedFlag();
		} catch (Exception e) {

		}

	}

	/**
	 * @author Steven Voigt
	 * Get the graph to cast it in a ByteArrayOutputStream 
	 * @return the graph as an ByteArrayOutputStream with int for the actual mode
	 * 
	 */

	private ByteArrayOutputStream serialize() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream serializedObjects = new ObjectOutputStream(outputStream);

			serializedObjects.writeInt(getEditingMode());
			serializedObjects.writeObject(getGraph());

		} catch (IOException IOExc) {
			java.lang.System.err.println(IOExc);
			throw new InternalErrorException(IOExc.getMessage());
		}
		m_bSaved = true;
		return outputStream;
	}

	/**
	 * @author Steven Voigt
	 * Get the serialized data to store it in a file.
	 * @return the serialiazed data
	 */
	public ByteArrayOutputStream getSerializedData() {
		return serialize();
	}

	/**
	 *@author Steven Voigt
	 *Set data. that was loaded from file. This data will be deserialized to restore the saved state.
	 * @param data earlier serialized data
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

		m_iEditingMode = (((mode > _MAX_MODE) || (mode < _MIN_MODE)) ? MODE_NO_TOOL_ACTIVE : mode);
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
		try {
			m_nCurStep = 0;
			m_mapNodes2AlgoStatesArrayLists.clear();
			DijkstraAlgorithm DijkstraAlgorithm = new DijkstraAlgorithm(new ColorFactory(m_composite.getDisplay()),
					getGraph());
			Graph gr = getGraph();
			Iterator iterNodes = gr.getNodeList().iterator();
			Node prevStartNode = gr.getStartNode();
			while (iterNodes.hasNext()) {
				Node node = (Node) iterNodes.next();
				boolean bStart = node.getStart();
				DijkstraAlgorithm.generateStates(node);
				ArrayList arList = new ArrayList();

				int nStateCount = DijkstraAlgorithm.getStateCount();

				for (int i = 0; i < nStateCount; i++) {
					DijkstraAlgorithm.gotoState(i);
					arList.add(DijkstraAlgorithm.getCurrentState());
				}
				node.setStart(bStart);
				m_mapNodes2AlgoStatesArrayLists.put(new Integer(node.getIndex()), arList);
			}

			if (prevStartNode != null) {
				prevStartNode.setStart(true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param nStep	The zero-based # of the state
	 * @return Returns the state of the given number
	 */
	public State getState(int nStep) {
		Node node = getGraph().getStartNode();
		if (node == null)
			return null;
		ArrayList arList = (ArrayList) m_mapNodes2AlgoStatesArrayLists.get(new Integer(node.getIndex()));
		if (nStep >= arList.size())
			nStep = arList.size() - 1;
		else if (nStep < 0)
			nStep = 0;

		State state = (State) arList.get(nStep);
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
	 * 
	 * @return Returns the # of steps for the current start node
	 */
	public int getStepCount() {
		Node node = getGraph().getStartNode();
		if (node == null)
			return 0;
		ArrayList arList = (ArrayList) m_mapNodes2AlgoStatesArrayLists.get(new Integer(node.getIndex()));
		return ((arList != null) ? (arList.size()) : 0);
	}

	/**
	 * @param nStep
	 *            Zero-based index of the current step
	 * @return Returns true if we have a next step in the Dijkstra
	 */
	public boolean hasNextStep(int nStep) {
		Node node = getGraph().getStartNode();
		if (node == null)
			return false;
		ArrayList arList = (ArrayList) m_mapNodes2AlgoStatesArrayLists.get(new Integer(node.getIndex()));
		return ((arList != null) ? (nStep + 1) < arList.size() : false);
	}

	/**
	 * @param nStep
	 *            Zero-based index of the current step
	 * @return Returns true if we have a previous step in the Dijkstra
	 */
	public boolean hasPrevStep(int nStep) {
		Node node = getGraph().getStartNode();
		if (node == null)
			return false;
		ArrayList arList = (ArrayList) m_mapNodes2AlgoStatesArrayLists.get(new Integer(node.getIndex()));
		return ((arList != null) ? (nStep > 0) : false);
	}

	/**
	 * @param nStep
	 *            Zero-based index of the current step
	 * @return Returns true if we have a next macro step in the Dijkstra
	 */
	public boolean hasNextMacroStep(int nStep) {
		if (hasNextStep(nStep) == false)
			return false;
		Node node = getGraph().getStartNode();
		if (node == null)
			return false;
		ArrayList arList = (ArrayList) m_mapNodes2AlgoStatesArrayLists.get(new Integer(node.getIndex()));
		Iterator iter = arList.iterator();
		for (int i = 0; iter.hasNext(); i++) {
			State state = (State) iter.next();
			if (i > nStep && (state.isMacro() == true))
				return true;
		}
		return false;
	}

	/**
	 * @param nStep
	 *            Zero-based index of the current step
	 * @return Returns true if we have a previous macro step in the Dijkstra
	 */
	public boolean hasPrevMacroStep(int nStep) {
		if (hasPrevStep(nStep) == false)
			return false;
		Node node = getGraph().getStartNode();
		if (node == null)
			return false;
		ArrayList arList = (ArrayList) m_mapNodes2AlgoStatesArrayLists.get(new Integer(node.getIndex()));
		Iterator iter = arList.iterator();
		for (int i = 0; iter.hasNext(); i++) {
			State state = (State) iter.next();
			if (i < nStep && (state.isMacro() == true))
				return true;
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
		if (node == null)
			return 0;
		ArrayList arList = (ArrayList) m_mapNodes2AlgoStatesArrayLists.get(new Integer(node.getIndex()));

		for (int i = getCurrentStep() - 1; i >= 0; i--) {
			State state = (State) arList.get(i);
			if (state == null)
				break;
			else if (state.isMacro() == true)
				return i;
		}
		return 0;
	}

	/**
	 * @return Returns the # of the previous macro step in the Dijkstra
	 */
	public int getNextMacroStepIndex() {
		Node node = getGraph().getStartNode();
		if (node == null)
			return 0;
		ArrayList arList = (ArrayList) m_mapNodes2AlgoStatesArrayLists.get(new Integer(node.getIndex()));

		for (int i = getCurrentStep() + 1; i < arList.size(); i++) {
			State state = (State) arList.get(i);
			if (state == null)
				break;
			else if (state.isMacro() == true)
				return i;
		}
		return 0;
	}

	/**
	 * Show the part of the GUI where the user can manipulate Graphs.
	 *  
	 */
	public void showEditingPage() {
		m_folder.showPage(m_editingPage);
		this.setEditingMode(MODE_NO_TOOL_ACTIVE);
		this.setModifiedFlag();
	}

	/**
	 * Shows the part of the GUI where the user witnesses the progression of
	 * Dijkstra's Algorithm.
	 */
	public void showAlgorithmPage() {
		m_folder.showPage(m_algorithmPage);
		this.setEditingMode(MODE_ALGORITHM);

	}

	/**
	 * Creates the page folder and all the items on the pages.
	 */
	private void createGUI() {

		m_folder = new PageFolder(m_composite);

		m_editingPage = new Page(m_folder);
		m_algorithmPage = new Page(m_folder);

		m_editingPage.setLayout(new FillLayout());
		m_algorithmPage.setLayout(new FillLayout());
		m_folder.setLayout(new PageLayout());

		Composite edit = new EditModeMainComposite(this, m_editingPage, SWT.NONE);
		Composite algo = new AlgorithmModeMainComposite(this, m_algorithmPage, SWT.NONE);

		m_folder.showPage(m_editingPage);

		// This magic incantation is required so that the all Composites get
		// the right size from the start.
		m_folder.pack();
		m_folder.setSize(m_composite.getSize());

		//HS -- create toolbar
		m_saveToolBarAction = ((JalgoWindow) m_appWindow).getSaveAction();
		if (m_saveToolBarAction != null)
			m_saveToolBarAction.setEnabled(true);
		m_saveAsToolBarAction = ((JalgoWindow) m_appWindow).getSaveAsAction();
		if (m_saveAsToolBarAction != null)
			m_saveAsToolBarAction.setEnabled(true);
		m_undoToolBarAction = new UndoToolBarAction(this);
		m_undoToolBarAction.setEnabled(false);
		m_toolBarManager.add(m_undoToolBarAction);
		m_redoToolBarAction = new RedoToolBarAction(this);
		m_redoToolBarAction.setEnabled(false);
		m_toolBarManager.add(m_redoToolBarAction);
		m_toolBarManager.update(false);
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
	 * @return
	 */
	public Graph setGraph(Graph graph) {

		Graph oldGraph = (Graph) m_curGraph.clone();
		this.m_curGraph = graph;
		if (this.m_iEditingMode == Controller.MODE_ALGORITHM)
			computeAlgoStates();
		if (oldGraph.equals(graph) == false)
			m_bSaved = false;
		return oldGraph;
	}

	/**
	 * @author Frank Staudinger
	 * 
	 * It is intend to call this function from the contructor of 
	 * Your Action derived class 
	 * 
	 * @param act	The Action You want to be executed and registered in the action stack
	 * @param bRegister True if the Action should be registered in the action stack
	 * @return Returns the result of act.doAction()
	 * @throws Exception
	 */
	public boolean registerAndDoAction(Action act, boolean bRegister) throws Exception {

		if (bRegister == true)
			m_actions.add(act);

		if (act.doAction()) {
			setModifiedFlag();
			return true;
		}
		if (bRegister == true)
			m_actions.movePrevious();
		return false;
	}

	/**
	 * Undoes the last Action
	 * @return Returns the last actions doAction() function result
	 * @throws Exception
	 */
	public boolean undoAction() throws Exception {
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
	 * @return Returns the last actions doAction() function result
	 * @throws Exception
	 */
	public boolean redoAction() throws Exception {
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

		this.updateToolBar();
		super.setChanged();
		super.notifyObservers(this);
		super.clearChanged();

	}

	public void registerStatusbarObserver(Observer observer) {
		this.m_StatusbarObservable.addObserver(observer);
	}

	public StatusbarText getStatusbarText() {
		return m_strStatusbarText;
	}

	public void setStatusbarText(StatusbarText strText) {
		m_strStatusbarText = strText;
		this.m_StatusbarObservable.setChanged();
		this.m_StatusbarObservable.notifyObservers(this);
		this.m_StatusbarObservable.clearChanged();
	}

	private void updateToolBar() {
		if (m_undoToolBarAction != null)
			m_undoToolBarAction.setEnabled(hasUndoAction());
		if (m_redoToolBarAction != null)
			m_redoToolBarAction.setEnabled(hasRedoAction());
	}

	public void startAnimation(int iMilliseconds) {
		this.m_iAniMillis = iMilliseconds;
		m_animator.run();
	}

	public void stopAnimation() {
		if (getAnimationMillis() > 0) {
			try {
				this.m_iAniMillis = -1;
				m_algorithmPage.getDisplay().timerExec(this.m_iAniMillis, m_animator);
			} catch (SWTException e) {

			}
		}
	}

	/**
	 * @return Returns the m_iAniMillis.
	 */
	public int getAnimationMillis() {
		return m_iAniMillis;
	}

	/**
	 * @return Returns the m_bSaved.
	 */
	public boolean isSaved() {
		return m_bSaved;
	}

	public void registerDisposeListener(org.eclipse.swt.widgets.Widget widget) {
		widget.addDisposeListener(new _DisposeListener(this));
	}
}