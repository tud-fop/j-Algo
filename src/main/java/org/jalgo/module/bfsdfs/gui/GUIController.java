package org.jalgo.module.bfsdfs.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.gui.JAlgoWindow;
import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.ModuleConnector;
import org.jalgo.module.bfsdfs.algorithms.Algo;
import org.jalgo.module.bfsdfs.algorithms.BFS;
import org.jalgo.module.bfsdfs.algorithms.DFS;
import org.jalgo.module.bfsdfs.algorithms.stack.NodeStatus;
import org.jalgo.module.bfsdfs.algorithms.stack.StackObserver;
import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.graph.AlgoGraphObserver;
import org.jalgo.module.bfsdfs.graph.GraphObserver;
import org.jalgo.module.bfsdfs.gui.components.AlgoTab;
import org.jalgo.module.bfsdfs.gui.components.BFSTab;
import org.jalgo.module.bfsdfs.gui.components.DFSTab;
import org.jalgo.module.bfsdfs.gui.components.DesignTab;
import org.jalgo.module.bfsdfs.gui.components.TabContainer;
import org.jalgo.module.bfsdfs.gui.components.WelcomeScreen;
import org.jalgo.module.bfsdfs.gui.event.AnimationAction;
import org.jalgo.module.bfsdfs.gui.event.CancelAction;
import org.jalgo.module.bfsdfs.gui.event.DoubleEdgeAction;
import org.jalgo.module.bfsdfs.gui.event.EdgeAction;
import org.jalgo.module.bfsdfs.gui.event.EraserAction;
import org.jalgo.module.bfsdfs.gui.event.NodeAction;
import org.jalgo.module.bfsdfs.gui.event.PlayAction;
import org.jalgo.module.bfsdfs.gui.event.RedoAction;
import org.jalgo.module.bfsdfs.gui.event.ResetAction;
import org.jalgo.module.bfsdfs.gui.event.StepBackAction;
import org.jalgo.module.bfsdfs.gui.event.StepForwardAction;
import org.jalgo.module.bfsdfs.gui.event.UndoAction;
import org.jalgo.module.bfsdfs.gui.graphview.EditMode;
import org.jalgo.module.bfsdfs.gui.graphview.InteractiveGraphView;
import org.jalgo.module.bfsdfs.undo.UndoableObserver;

/**
 * The <code>GUIController</code> manages all visualization e.g. the
 * {@linkplain WelcomeScreen}, the three tabs {@linkplain DesignTab},
 * {@linkplain BFSTab} and {@linkplain DFSTab}, the toolbar and the
 * {@linkplain InteractiveGraphView} and sends actions to the current
 * {@linkplain Algo}, the {@linkplain GraphController} and the
 * {@linkplain ModuleConnector}.
 * It owns one algorithm at a time and can switch between them when a new
 * window is selected by the user.
 * @author Florian Dornbusch
 */
public class GUIController implements GUIConstants {
	
	// general references
	private ModuleConnector connector;
	private GraphController controller;
	private Algo algo = null;
	private BFS bfs;
	private DFS dfs;
	private WelcomeScreen welcomeScreen;
	private TabContainer tabContainer;
	private DesignTab designTab;
	private BFSTab bfsTab;
	private DFSTab dfsTab;
	private InteractiveGraphView graphView;
	private EditMode graphViewEditMode = EditMode.PUT_NODE;
	
	// actions
	private AbstractAction animationToggleAction;
	private AbstractAction resetAction;
	private AbstractAction stepBackAction;
	private AbstractAction stepForwardAction;
	private AbstractAction playAction;
	private AbstractAction cancelAction;
	private AbstractAction undoAction;
	private AbstractAction redoAction;
	private AbstractAction nodeAction;
	private AbstractAction edgeAction;
	private AbstractAction doubleEdgeAction;
	private AbstractAction eraserAction;
	
	// components
	private JPanel contentPane;
	private JComponent rootPane;
	
	// all toolbar items that are further used
	private JToggleButton nodeButton;
	private JToggleButton edgeButton;
	private JToggleButton doubleEdgeButton;
	private JToggleButton eraserButton;
	private JLabel animationLabel;
	private JCheckBox animationBox;
	
	// saved states for enabling the tool bar buttons 
	private boolean design = true;
	private boolean designUndo = false;
	private boolean designRedo = false;
	private boolean bfsReset = false;
	private boolean bfsStepBack = false;
	private boolean bfsStepForward = true;
	private boolean bfsPlay = true;
	private boolean bfsCancel = false;
	private boolean dfsReset = false;
	private boolean dfsStepBack = false;
	private boolean dfsStepForward = true;
	private boolean dfsPlay = true;
	private boolean dfsCancel = false;
	
	/** The saved center position of the viewport in all three tabs. */
	private Point graphViewPosition;
	
	/** The used animation for playing the algorithm. */
	private PlayAnimation bfsAnimation, dfsAnimation;
	
	/** The current frame state if not in beamer mode. */
	private int frameState = Frame.NORMAL;
	
	/**
	 * Constructor. Initializes the inner class observers, the
	 * <code>contentPane</code>, the {@linkplain WelcomeScreen}, the
	 * {@linkplain TabContainer}, the animations and the
	 * {@linkplain InteractiveGraphView}.
	 * Installs the tool bar and creates shortcuts.
	 * @author Florian Dornbusch
	 */
	public GUIController(ModuleConnector connector, BFS bfs, DFS dfs,
			GraphController controller) {
		this.connector = connector;
		this.bfs = bfs;
		this.dfs = dfs;
		this.controller = controller;
		
		// install the main panel
		rootPane =
			JAlgoGUIConnector.getInstance().getModuleComponent(connector);
		rootPane.setLayout(new BorderLayout());
		contentPane = new JPanel();
		rootPane.add(contentPane, BorderLayout.CENTER);
		
		// install the toolbar
		installToolbar();
		
		// create the tab container
		tabContainer = new TabContainer(this, bfs, dfs, this.controller);
		designTab = tabContainer.getDesignTab();
		bfsTab = tabContainer.getBFSTab();
		dfsTab = tabContainer.getDFSTab();
		graphView = designTab.getGraphView();
		
		// create the animations
		bfsAnimation = new PlayAnimation(bfsTab.getNodeStackView(), bfs);
		dfsAnimation = new PlayAnimation(dfsTab.getNodeStackView(), dfs);
		
		// internal classes that are used to observe other parts of the program
		new MyBFSObserver();
		new MyDFSObserver();
		new MyGraphObserver();
	
		welcomeScreen = new WelcomeScreen(this);
		
		// create the shortcuts
		createShortcuts();
	}
	
	
	/**
	 * Loads the graph from the specified file.
	 * @param filename full name of the file containing the graph
	 * @author Thomas G&ouml;rres
	 */
	public void loadGraph(String filename) throws IOException {
		ModuleConnector.loadFromFile(filename);
	}
	
	
	/**
	 * Loads the graph from the last loaded file.
	 * @author Thomas G&ouml;rres
	 */
	public void loadLastGraph() throws IOException {
		ModuleConnector.loadFromLastFile();
	}
	
	
	/**
	 * Returns the used {@linkplain ResetAction}.
	 * @author Florian Dornbusch
	 */
	public AbstractAction getResetAction() {
		return resetAction;
	}
	
	
	/**
	 * Returns the used {@linkplain StepBackAction}.
	 * @author Florian Dornbusch
	 */
	public AbstractAction getStepBackAction() {
		return stepBackAction;
	}
	
	
	/**
	 * Returns the used {@linkplain StepForwardAction}.
	 * @author Florian Dornbusch
	 */
	public AbstractAction getStepForwardAction() {
		return stepForwardAction;
	}
	
	
	/**
	 * Returns the used {@linkplain PlayAction}.
	 * @author Florian Dornbusch
	 */
	public AbstractAction getPlayAction() {
		return playAction;
	}
	
	
	/**
	 * Returns the used {@linkplain CancelAction}.
	 * @author Florian Dornbusch
	 */
	public AbstractAction getCancelAction() {
		return cancelAction;
	}
	
	
	/**
	 * Returns the used {@linkplain UndoAction}.
	 * @author Florian Dornbusch
	 */
	public AbstractAction getUndoAction() {
		return undoAction;
	}
	
	
	/**
	 * Returns the used {@linkplain RedoAction}.
	 * @author Florian Dornbusch
	 */
	public AbstractAction getRedoAction() {
		return redoAction;
	}
	
	
	/**
	 * Returns the used {@linkplain NodeAction}.
	 * @author Florian Dornbusch
	 */
	public AbstractAction getNodeAction() {
		return nodeAction;
	}
	
	
	/**
	 * Returns the used {@linkplain EdgeAction}.
	 * @author Florian Dornbusch
	 */
	public AbstractAction getEdgeAction() {
		return edgeAction;
	}
	
	
	/**
	 * Returns the used {@linkplain DoubleEdgeAction}.
	 * @author Florian Dornbusch
	 */
	public AbstractAction getDoubleEdgeAction() {
		return doubleEdgeAction;
	}
	
	
	/**
	 * Returns the used {@linkplain EraserAction}.
	 * @author Florian Dornbusch
	 */
	public AbstractAction getEraserAction() {
		return eraserAction;
	}
	
	
	/**
	 * Shows the {@linkplain WelcomeScreen} and changes the
	 * {@linkplain SaveStatus} to <code>NOTHING_TO_SAVE</code>.
	 * @author Florian Dornbusch
	 */
	public void installWelcomeScreen() {
		contentPane.removeAll();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(welcomeScreen, BorderLayout.CENTER);
		welcomeScreen.setVisible(true);		
		connector.setSaveStatus(SaveStatus.NOTHING_TO_SAVE);
	}
	
	
	/**
	 * Hides the {@linkplain WelcomeScreen} and switches to design mode.
	 * @author Florian Dornbusch
	 */
	public void installStandardLayout() {
		contentPane.add(tabContainer, BorderLayout.CENTER);
		welcomeScreen.setVisible(false);
		graphView.addStartNode();
		animationToggleAction.setEnabled(true);
		animationLabel.setEnabled(true);
		switchToDesign();
		setGraphView(EditMode.PUT_NODE);
	}
	
	
	/**
	 * Shows the design buttons and hides the algorithm buttons. If the boolean
	 * values of the design buttons are false, at least one algorithm is
	 * currently running. If so, shows a window where the user can reset them.
	 * @author Florian Dornbusch
	 */
	public void switchToDesign() {
		// stop possibly running animations
		stopBFSAnimation();
		stopDFSAnimation();
		
		// show the design buttons
		undoAction.setEnabled(designUndo && design);
		redoAction.setEnabled(designRedo && design);
		nodeAction.setEnabled(design);
		edgeAction.setEnabled(design);
		doubleEdgeAction.setEnabled(design);
		eraserAction.setEnabled(design);
		
		// hide the algorithm buttons
		resetAction.setEnabled(false);
		stepBackAction.setEnabled(false);
		stepForwardAction.setEnabled(false);
		playAction.setEnabled(false);
		cancelAction.setEnabled(false);
		
		// activate the saved edit mode for the graph view
		graphView.setEditMode(graphViewEditMode);
		
		// set animations to Design
		animationBox.setActionCommand("Design");
		toggleAnimation();
			
		// if the design buttons are disabled set the GraphView to algorithm
		if(!design) {
			setGraphView(EditMode.ALGORITHM);
			
			// show a warning window that an algorithm is running
			Object[] options = {
				Messages.getString("bfsdfs", "GUIController.warning_reset"),
				Messages.getString("bfsdfs", "GUIController.warning_abort"),
            };
			int n = JOptionPane.showOptionDialog(
					rootPane,
					Messages.getString("bfsdfs", "GUIController.warning_desc")
					+ System.getProperty("line.separator")
					+ System.getProperty("line.separator"),
					Messages.getString("bfsdfs", "GUIController.warning"),
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[1]);
			
			// if the user choice is to reset the algorithm
			if(n == 0) {
				// reset both algorithms
				Algo temp = algo;
				algo = bfs;
				resetAlgo();
				algo = dfs;
				resetAlgo();
				algo = temp;
				switchToDesign();
			}		
		}
	}
	
	
	/**
	 * Changes the {@linkplain Algo} to {@linkplain DFS}, hides the design
	 * buttons and shows the algorithm buttons.
	 * @author Florian Dornbusch
	 */
	public void switchToDFS() {
		// toggle algo to BFS
		algo = dfs;
		
		// stop possibly running DFS animation
		stopBFSAnimation();
		
		// disable all design buttons
		undoAction.setEnabled(false);
		redoAction.setEnabled(false);
		nodeAction.setEnabled(false);
		edgeAction.setEnabled(false);
		doubleEdgeAction.setEnabled(false);
		eraserAction.setEnabled(false);
		
		// enable the algorithm buttons according to their boolean values
		resetAction.setEnabled(dfsReset);
		stepBackAction.setEnabled(dfsStepBack);
		stepForwardAction.setEnabled(dfsStepForward
				&& !dfsTab.startNodeChooserEmpty());
		playAction.setEnabled(dfsPlay
				&& !bfsTab.startNodeChooserEmpty());
		cancelAction.setEnabled(dfsCancel);
		
		// set animations to DFS
		animationBox.setActionCommand("DFS");
		toggleAnimation();
	}
	
	
	/**
	 * Changes the {@linkplain Algo} to {@linkplain BFS}, hides the design
	 * buttons and shows the algorithm buttons.
	 * @author Florian Dornbusch
	 */
	public void switchToBFS() {
		// toggle algo to BFS
		algo = bfs;
		
		// stop possibly running DFS animation
		stopDFSAnimation();
		
		// disable all design buttons
		undoAction.setEnabled(false);
		redoAction.setEnabled(false);
		nodeAction.setEnabled(false);
		edgeAction.setEnabled(false);
		doubleEdgeAction.setEnabled(false);
		eraserAction.setEnabled(false);
		
		// enable the algorithm buttons according to their boolean values
		resetAction.setEnabled(bfsReset);
		stepBackAction.setEnabled(bfsStepBack);
		stepForwardAction.setEnabled(bfsStepForward
				&& !bfsTab.startNodeChooserEmpty());
		playAction.setEnabled(bfsPlay
				&& !bfsTab.startNodeChooserEmpty());
		cancelAction.setEnabled(bfsCancel);
		
		// set animations to BFS
		animationBox.setActionCommand("BFS");
		toggleAnimation();
	}
		
	
	/**
	 * This method is called, if the user sets a new start node with the use
	 * of the start node chooser in an {@linkplain AlgoTab}.<br>
	 * The current algorithm is informed about the new start node.<br>
	 * If the algorithm is not yet chosen, which means the user has not yet
	 * changed the tabs, both algorithms are informed of the new start node. 
	 * @param id the identifier of the node
	 * @author Florian Dornbusch
	 */
	public void setStartNode(int id) {
		if(algo == null) {
			bfs.setStartNode(id);
			dfs.setStartNode(id);
		}
		else {
			algo.setStartNode(id);
		}
	}
	
	
	/**
	 * Sends the successor order to the current algorithm.
	 * @param list the successor order chosen in {@link SuccessorChooser}.
	 * @author Florian Dornbusch
	 */
	public void setSuccessorOrder(List<Integer> list) {
		if(list == null || list.isEmpty()) return;
		if(algo == null) {
			bfs.setSuccessorOrder(list);
			dfs.setSuccessorOrder(list);
		}
		else {
			algo.setSuccessorOrder(list);
		}
	}
	
	
	/**
	 * Resets the current algorithm.
	 * @author Florian Dornbusch
	 */
	public void resetAlgo() {
		AlgoTab tab;
		if(algo instanceof BFS) tab = bfsTab;
		else tab = dfsTab;
		tab.getNodeStackView().stopAnimation();
		algo.undoAll();
		tab.getNodeStackView().clearStack();
	}
	
	
	/**
	 * Undoes the last step in the algorithm.
	 * @author Florian Dornbusch
	 */
	public void previousAlgoStep() {
		AlgoTab tab;
		if(algo instanceof BFS) tab = bfsTab;
		else tab = dfsTab;
		tab.getNodeStackView().stopAnimation();
		algo.undo();
		tab.getNodeStackView().startAnimation();
		
		stepForwardAction.setEnabled(true);
		playAction.setEnabled(true);
		if(algo instanceof BFS) {
			bfsStepForward = true;
			bfsPlay = true;
		}
		else {
			dfsStepForward = true;
			dfsPlay = true;
		}
	}
	
	
	/**
	 * Executes the next step in the algorithm. If the check box, which
	 * determines if the algorithm is deterministic or not, is enabled,
	 * a random permutation of the possible successors is sent before
	 * the step is executed.
	 * @author Florian Dornbusch
	 */
	public void nextAlgoStep() {
		AlgoTab tab;
		if(algo instanceof BFS) tab = bfsTab;
		else tab = dfsTab;
		
		tab.getNodeStackView().stopAnimation();
		algo.step();
		tab.getNodeStackView().startAnimation();
		
		resetAction.setEnabled(true);
		stepBackAction.setEnabled(true);
		if(algo instanceof BFS) {
			bfsReset = true;
			bfsStepBack = true;
		}
		else {
			dfsReset = true;
			dfsStepBack = true;
		}
	}
	
	
	/**
	 * Plays the current algorithm.
	 * @author Florian Dornbusch
	 */
	public void playAlgo() {
		if(algo instanceof BFS) {
			bfsAnimation.start();
			bfsReset = false;
			bfsStepBack = false;
			bfsStepForward = false;
			bfsPlay = false;
			bfsCancel = true;
		}
		else {
			dfsAnimation.start();
			dfsReset = false;
			dfsStepBack = false;
			dfsStepForward = false;
			dfsPlay = false;
			dfsCancel = true;
		}
		resetAction.setEnabled(false);
		stepBackAction.setEnabled(false);
		stepForwardAction.setEnabled(false);
		playAction.setEnabled(false);
		cancelAction.setEnabled(true);
	}
	
	
	/**
	 * Cancels the current animation.
	 * @author Florian Dornbusch
	 */
	public void cancel() {
		if(algo instanceof BFS) stopBFSAnimation();
		else stopDFSAnimation();
	}
	
	
	/**
	 * Undoes the last step in the graph (e.g. adding a node).
	 * @author Florian Dornbusch
	 */
	public void undo() {
		controller.undo();
	}
	
	
	/**
	 * Redoes the last step in the graph (e.g. adding a node).
	 * @author Florian Dornbusch
	 */
	public void redo() {
		controller.redo();
	}
	
	
	/**
	 * Sets the {@linkplain InteractiveGraphView} to the according mode,
	 * saves the mode and toggles the appropriate buttons.
	 * @author Florian Dornbusch
	 */
	public void setGraphView(EditMode mode) {
		graphViewEditMode = mode;
		
		nodeButton.setSelected(mode == EditMode.PUT_NODE);
		edgeButton.setSelected(mode == EditMode.START_EDGE);
		doubleEdgeButton.setSelected(mode == EditMode.START_DOUBLE_EDGE);
		eraserButton.setSelected(mode == EditMode.ERASE);
		designTab.switchTo(mode);
		
		if(mode == EditMode.ALGORITHM) {
			graphViewEditMode = graphView.getEditMode();
		}
		
		graphView.setEditMode(mode);
	}
	
	
	/**
	 * Returns the saved position of the <code>Viewport</code> used in the
	 * <code>JScrollPane</code> in all three tabs.
	 * @author Florian Dornbusch
	 */
	public Point getGraphViewPosition() {
		return graphViewPosition;
	}
	
	
	/**
	 * Saves the position of the <code>Viewport</code> used in the
	 * <code>JScrollPane</code> in all three tabs.
	 * @author Florian Dornbusch
	 */
	public void setGraphViewPosition(Point p) {
		graphViewPosition = p;
	}
	
	
	/**
	 * Adds a {@linkplain StackObserver} to {@linkplain BFS}.
	 * @author Florian Dornbusch
	 */
	public void addBFSStackObserver(StackObserver observer) {
		bfs.addStackObserver(observer);
	}
	
	
	/**
	 * Removes a {@linkplain StackObserver} from {@linkplain BFS}.
	 * @author Florian Dornbusch
	 */
	public void removeBFSStackObserver(StackObserver observer) {
		bfs.removeStackObserver(observer);
	}
	
	
	/**
	 * Adds a {@linkplain StackObserver} to {@linkplain DFS}.
	 * @author Florian Dornbusch
	 */
	public void addDFSStackObserver(StackObserver observer) {
		dfs.addStackObserver(observer);
	}
	
	
	/**
	 * Removes a {@linkplain StackObserver} from {@linkplain DFS}.
	 * @author Florian Dornbusch
	 */
	public void removeDFSStackObserver(StackObserver observer) {
		dfs.removeStackObserver(observer);
	}
	
	
	/**
	 * Adds an {@linkplain UndoableObserver} to {@linkplain BFS}.
	 * @author Florian Dornbusch
	 */
	public void addBFSUndoableObserver(UndoableObserver observer) {
		bfs.addUndoableObserver(observer);
	}
	
	
	/**
	 * Removes an {@linkplain UndoableObserver} from {@linkplain BFS}.
	 * @author Florian Dornbusch
	 */
	public void removeBFSUndoableObserver(UndoableObserver observer) {
		bfs.removeUndoableObserver(observer);
	}
	
	
	/**
	 * Adds an {@linkplain UndoableObserver} to {@linkplain DFS}.
	 * @author Florian Dornbusch
	 */
	public void addDFSUndoableObserver(UndoableObserver observer) {
		dfs.addUndoableObserver(observer);
	}
	
	
	/**
	 * Removes an {@linkplain UndoableObserver} from {@linkplain DFS}.
	 * @author Florian Dornbusch
	 */
	public void removeDFSUndoableObserver(UndoableObserver observer) {
		dfs.removeUndoableObserver(observer);
	}
	
	
	/**
	 * Adds an {@linkplain AlgoGraphObserver} to {@linkplain BFS}.
	 * @author Florian Dornbusch
	 */
	public void addBFSTreeObserver(AlgoGraphObserver observer) {
		bfs.addTreeObserver(observer);
	}
	
	
	/**
	 * Removes an {@linkplain AlgoGraphObserver} from {@linkplain BFS}.
	 * @author Florian Dornbusch
	 */
	public void removeBFSTreeObserver(AlgoGraphObserver observer) {
		bfs.removeTreeObserver(observer);
	}
	
	
	/**
	 * Adds an {@linkplain AlgoGraphObserver} to {@linkplain DFS}.
	 * @author Florian Dornbusch
	 */
	public void addDFSTreeObserver(AlgoGraphObserver observer) {
		dfs.addTreeObserver(observer);
	}
	
	
	/**
	 * Removes an {@linkplain AlgoGraphObserver} from {@linkplain DFS}.
	 * @author Florian Dornbusch
	 */
	public void removeDFSTreeObserver(AlgoGraphObserver observer) {
		dfs.removeTreeObserver(observer);
	}
	
	
	/**
	 * Adds a {@linkplain GraphObserver} to the {@linkplain GraphController}.
	 * @author Florian Dornbusch
	 */
	public void addGraphObserver(GraphObserver observer) {
		controller.addGraphObserver(observer);
	}
	
	
	/**
	 * Removes a {@linkplain GraphObserver} from the
	 * {@linkplain GraphController}.
	 * @author Florian Dornbusch
	 */
	public void removeGraphObserver(GraphObserver observer) {
		controller.addGraphObserver(observer);
	}
	
	
	/**
	 * Adds an {@linkplain UndoableController} to the
	 * {@linkplain GraphController}.
	 * @author Florian Dornbusch
	 */
	public void addGraphUndoableObserver(UndoableObserver observer) {
		controller.addUndoableObserver(observer);
	}
	
	
	/**
	 * Removes an {@linkplain UndoableController} from the
	 * {@linkplain GraphController}.
	 * @author Florian Dornbusch
	 */
	public void removeGraphUndoableObserver(UndoableObserver observer) {
		controller.removeUndoableObserver(observer);
	}
	
	
	/**
	 * This method changes the content of all major components depending on
	 * {@linkplain ComponentUtility#BEAMER_MODE}.
	 * @author Florian Dornbusch
	 */
	public void toggleBeamerMode() {
		// maximize / minimize window depending on beamer mode
		Frame[] frames = JAlgoWindow.getFrames();
		// get the JAlgoWindow
		for(Frame f:frames) {
			if(f instanceof JAlgoWindow) {
				if(ComponentUtility.BEAMER_MODE) {
					// save state and then maximize
					frameState = f.getExtendedState();
					f.setExtendedState(Frame.MAXIMIZED_BOTH);
				}
				else {
					// load state
					if(f.getExtendedState() == Frame.MAXIMIZED_BOTH)f.setExtendedState(frameState);
				}
			}
		}
		
		if (tabContainer != null)
			tabContainer.toggleBeamerMode();
		
		if (bfsTab != null) {
			bfsTab.toggleBeamerMode();
			bfsTab.getNodeStackView().toggleBeamerMode();
		}
		
		if (dfsTab != null) {
			dfsTab.toggleBeamerMode();
			dfsTab.getNodeStackView().toggleBeamerMode();
		}
	}

	
	/**
	 * Enables or disables the main animation for the three tabs
	 * {@linkplain BFSTab}, {@linkplain DFSTab} and {@linkplain DesignTab}.<br>
	 * If the checkbox "Animation" is selected, the animations for the
	 * three tabs are set according to their boolean values. <br>
	 * Otherwise all animations are disabled.
	 * @author Ephraim Zimmer
	 */
	public void toggleAnimation() {
		if (!animationBox.isSelected()){
			graphView.setAnimationsEnabled(
					animationBox.getActionCommand().equals("Design"));
			bfsTab.getTreeView().setAnimationsEnabled(
					animationBox.getActionCommand().equals("BFS"));
			dfsTab.getTreeView().setAnimationsEnabled(
					animationBox.getActionCommand().equals("DFS"));
			
			bfsTab.getNodeStackView().setAnimationsEnabled(true);
			dfsTab.getNodeStackView().setAnimationsEnabled(true);
		} else {
			graphView.setAnimationsEnabled(false);
			bfsTab.getTreeView().setAnimationsEnabled(false);
			dfsTab.getTreeView().setAnimationsEnabled(false);
			
			bfsTab.getNodeStackView().setAnimationsEnabled(false);
			dfsTab.getNodeStackView().setAnimationsEnabled(false);
		}
	}
	
	
	/**
	 * Inner class to observe the graph.
	 * @author Florian Dornbusch
	 */
	private class MyGraphObserver implements UndoableObserver {
		
		public MyGraphObserver() {
			addGraphUndoableObserver(this);
		}
		
		
		/**
		 * If undo is enabled in the graph, the appropriate button
		 * is shown and the save status is updated.
		 * @author Florian Dornbusch
		 */
		public void onUndoEnabled() {
			undoAction.setEnabled(true);
			designUndo = true;
			connector.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
		}
		
		
		/**
		 * If undo is disabled in the graph, the appropriate button
		 * is hid and the save status is updated.
		 * @author Florian Dornbusch
		 */
		public void onUndoDisabled() {
			undoAction.setEnabled(false);
			designUndo = false;
			connector.setSaveStatus(SaveStatus.NOTHING_TO_SAVE);
		}
		
		
		/**
		 * If redo is enabled in the graph, the appropriate button
		 * is shown.
		 * @author Florian Dornbusch
		 */
		public void onRedoEnabled() {
			redoAction.setEnabled(true);
			designRedo = true;
		}
		
		
		/**
		 * If redo is disabled in the graph, the appropriate button is hid.
		 * @author Florian Dornbusch
		 */
		public void onRedoDisabled() {
			redoAction.setEnabled(false);
			designRedo = false;
		}
	}
	
	/**
	 * Inner class to observe the BFS algorithm.
	 * @author Florian Dornbusch
	 */
	private class MyBFSObserver implements UndoableObserver, StackObserver {
		
		public MyBFSObserver() {
			addBFSUndoableObserver(this);
			addBFSStackObserver(this);
		}

		
		/**
		 * If undo is disabled in the algorithm, the appropriate buttons
		 * are hid.
		 * @author Florian Dornbusch
		 */
		public void onUndoDisabled() {
			resetAction.setEnabled(false);
			stepBackAction.setEnabled(false);
			stepForwardAction.setEnabled(true);
			playAction.setEnabled(true);
			bfsReset = false;
			bfsStepBack = false;
			bfsStepForward = true;
			bfsPlay = true;
			
			design = !dfs.isUndoPossible();
		}

		
		public void onUndoEnabled() {
			design = false;
		}
		
		
		/** unused observer method */
		public void onRedoEnabled() {}
		
		
		/** unused observer method */
		public void onRedoDisabled() {}
		
		
		/**
		 * If the algorithm is finished, the appropriate buttons are hid.
		 * @author Florian Dornbusch
		 */
		public void onAllNodesFinished() {
			bfsAnimation.stop();
			bfsReset = true;
			bfsStepBack = true;
			bfsStepForward = false;
			bfsPlay = false;
			bfsCancel = false;
			resetAction.setEnabled(true);
			stepBackAction.setEnabled(true);
			stepForwardAction.setEnabled(false);
			playAction.setEnabled(false);
			cancelAction.setEnabled(false);
		}
		
		
		/** unused observer method */
		public void onStatusChanged(int node, NodeStatus newStatus) {}
		
		
		/** unused observer method */
		public void onFirstQueueAdded(int owner) {}
		
		
		/** unused observer method */
		public void onQueueAdded(int owner) {}
		
		
		/** unused observer method */
		public void onNodesAdded(List<Integer> nodes) {}
		
		
		/** unused observer method */
		public void onUntouchedReplaced(List<Integer> o, List<Integer> n) {}
		
		
		/** unused observer method */
		public void onTopQueueRemoved() {}
		
		
		/** unused observer method */
		public void onAllQueuesRemoved() {}
		
		
		/** unused observer method */
		public void onCurrentNodeChanged(int lastWaitingNode) {}
	}
	
	
	/**
	 * Inner class to observe the DFS algorithm.
	 * @author Florian Dornbusch
	 */
	private class MyDFSObserver implements UndoableObserver, StackObserver {
		
		public MyDFSObserver() {
			addDFSUndoableObserver(this);
			addDFSStackObserver(this);
		}
	
		
		/**
		 * If undo is disabled in the algorithm, the appropriate buttons
		 * are hid.
		 * @author Florian Dornbusch
		 */
		public void onUndoDisabled() {
			resetAction.setEnabled(false);
			stepBackAction.setEnabled(false);
			stepForwardAction.setEnabled(true);
			playAction.setEnabled(true);
			dfsReset = false;
			dfsStepBack = false;
			dfsStepForward = true;
			dfsPlay = true;
			
			design = !bfs.isUndoPossible();
		}

		
		public void onUndoEnabled() {
			design = false;
		}
		
		
		/** unused observer method */
		public void onRedoEnabled() {}
		
		
		/** unused observer method */
		public void onRedoDisabled() {}
		

		/** unused observer method */
		public void onAllNodesFinished() {}
		
		
		/** unused observer method */
		public void onStatusChanged(int node, NodeStatus newStatus) {}
		
		
		/** unused observer method */
		public void onFirstQueueAdded(int owner) {}
		
		
		/** unused observer method */
		public void onQueueAdded(int owner) {}
		
		
		/** unused observer method */
		public void onNodesAdded(List<Integer> nodes) {}
		
		
		/** unused observer method */
		public void onUntouchedReplaced(List<Integer> o, List<Integer> n) {}
		
		
		/** unused observer method */
		public void onTopQueueRemoved() {}
		

		/**
		 * If the algorithm is finished, the appropriate buttons are hid.
		 * @author Florian Dornbusch
		 */
		public void onAllQueuesRemoved() {
			dfsAnimation.stop();
			dfsReset = true;
			dfsStepBack = true;
			dfsStepForward = false;
			dfsPlay = false;
			dfsCancel = false;
			resetAction.setEnabled(true);
			stepBackAction.setEnabled(true);
			stepForwardAction.setEnabled(false);
			playAction.setEnabled(false);
			cancelAction.setEnabled(false);}
		
		
		/** unused observer method */
		public void onCurrentNodeChanged(int lastWaitingNode) {}
	}
	
	
	/**
	 * Creates the buttons in the tool bar and saves all buttons, which are
	 * further used (either to toggle them or to set them as standard).
	 * @author Florian Dornbusch
	 */
	private void installToolbar() {
		JToolBar toolBar =
			JAlgoGUIConnector.getInstance().getModuleToolbar(connector);

		
		resetAction = new ResetAction(this);
		toolBar.add(ComponentUtility.createToolbarButton(resetAction));
		
		stepBackAction = new StepBackAction(this);
		toolBar.add(ComponentUtility.createToolbarButton(stepBackAction));
		
		stepForwardAction = new StepForwardAction(this);
		toolBar.add(ComponentUtility.
				createToolbarButton(stepForwardAction));
		
		playAction = new PlayAction(this);
		toolBar.add(ComponentUtility.createToolbarButton(playAction));
		
		cancelAction = new CancelAction(this);
		toolBar.add(ComponentUtility.createToolbarButton(cancelAction));
		
		toolBar.addSeparator();
		
		undoAction = new UndoAction(this);
		toolBar.add(ComponentUtility.createToolbarButton(undoAction));
		
		redoAction = new RedoAction(this);
		toolBar.add(ComponentUtility.createToolbarButton(redoAction));
		
		nodeAction = new NodeAction(this);
		nodeButton = ComponentUtility.createToolbarToggleButton(nodeAction);
		nodeButton.setFocusable(false);
		toolBar.add(nodeButton);
		
		edgeAction = new EdgeAction(this);
		edgeButton = ComponentUtility.createToolbarToggleButton(edgeAction);
		edgeButton.setFocusable(false);
		toolBar.add(edgeButton);
		
		doubleEdgeAction = new DoubleEdgeAction(this);
		doubleEdgeButton = ComponentUtility.
			createToolbarToggleButton(doubleEdgeAction);
		doubleEdgeButton.setFocusable(false);
		toolBar.add(doubleEdgeButton);
		
		eraserAction = new EraserAction(this);
		eraserButton = ComponentUtility.
			createToolbarToggleButton(eraserAction);
		eraserButton.setFocusable(false);
		toolBar.add(eraserButton);
		
		toolBar.addSeparator();
		
		animationToggleAction = new AnimationAction(this);
		animationLabel = new JLabel();
		animationLabel.setText(Messages.getString("bfsdfs",
				"animationLabel.text"));
		animationLabel.setEnabled(false);
		animationBox = new JCheckBox(animationToggleAction);
		animationBox.addMouseListener(new StatusMouseAdapter(
				"AnimationAction.tooltip"));
		toolBar.add(animationLabel);
		toolBar.add(animationBox);
	}
	
	
	/**
	 * Creates shortcuts for all important operations.
	 * @author Ephraim Zimmer
	 * @author Florian Dornbusch
	 */
	private void createShortcuts() {
		// y for undo
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
			put(KeyStroke.getKeyStroke(
					KeyEvent.VK_Y, 0), "undo");
		contentPane.getActionMap().put("undo", undoAction);
		
		// x for redo
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
			put(KeyStroke.getKeyStroke(
					KeyEvent.VK_X, 0),"redo");
		contentPane.getActionMap().put("redo", redoAction);
		
		// q for node
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
			put(KeyStroke.getKeyStroke(
					KeyEvent.VK_Q, 0), "node");
		contentPane.getActionMap().put("node", nodeAction);
		
		// w for edge
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
			put(KeyStroke.getKeyStroke(
					KeyEvent.VK_W, 0), "edge");
		contentPane.getActionMap().put("edge", edgeAction);
		
		// e for double edge
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
			put(KeyStroke.getKeyStroke(
					KeyEvent.VK_E, 0), "doubleEdge");
		contentPane.getActionMap().put("doubleEdge", doubleEdgeAction);	
		
		// r for eraser
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
			put(KeyStroke.getKeyStroke(
					KeyEvent.VK_R, 0), "eraser");
		contentPane.getActionMap().put("eraser", eraserAction);	
		
		// a for reset
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
			put(KeyStroke.getKeyStroke(
					KeyEvent.VK_A, 0), "reset");
		contentPane.getActionMap().put("reset", resetAction);
		
		// s for step back
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
			put(KeyStroke.getKeyStroke(
					KeyEvent.VK_S, 0), "stepBack");
		contentPane.getActionMap().put("stepBack", stepBackAction);
		
		// d for step forward
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
			put(KeyStroke.getKeyStroke(
					KeyEvent.VK_D, 0), "stepForward");
		contentPane.getActionMap().put("stepForward", stepForwardAction);
		
		// f for play
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
			put(KeyStroke.getKeyStroke(
					KeyEvent.VK_F, 0), "play");
		contentPane.getActionMap().put("play", playAction);
		
		// c for cancel
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
			put(KeyStroke.getKeyStroke(
					KeyEvent.VK_C, 0), "cancel");
		contentPane.getActionMap().put("cancel", cancelAction);
		
		// b for beamer
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
		put(KeyStroke.getKeyStroke(
				KeyEvent.VK_B, 0), "beamer");
		contentPane.getActionMap().put("beamer",
				tabContainer.getBeamerAction());
	}
	
	
	/**
	 * Stops the current BFS Animation if it is running.
	 * @author Florian Dornbusch
	 */
	private void stopBFSAnimation() {
		if(bfsAnimation.isRunning()) {
			bfsAnimation.stop();
			bfsReset = true;
			bfsStepBack = true;
			bfsStepForward = true;
			bfsPlay = true;
			bfsCancel = false;
			resetAction.setEnabled(true);
			stepBackAction.setEnabled(true);
			stepForwardAction.setEnabled(true);
			playAction.setEnabled(true);
			cancelAction.setEnabled(false);
		}	
	}
	
	
	/**
	 * Stops the current DFS Animation if it is running.
	 * @author Florian Dornbusch
	 */
	private void stopDFSAnimation() {
		if(dfsAnimation.isRunning()) {
			dfsAnimation.stop();
			dfsReset = true;
			dfsStepBack = true;
			dfsStepForward = true;
			dfsPlay = true;
			dfsCancel = false;
			resetAction.setEnabled(true);
			stepBackAction.setEnabled(true);
			stepForwardAction.setEnabled(true);
			playAction.setEnabled(true);
			cancelAction.setEnabled(false);
		}
	}
}