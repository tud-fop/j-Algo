package org.jalgo.module.app.controller;

import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JToolBar;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.module.app.ModuleConnector;
import org.jalgo.module.app.core.AvailableSemiRings;
import org.jalgo.module.app.core.Calculation;
import org.jalgo.module.app.core.SemiRing;
import org.jalgo.module.app.core.dataType.formalLanguage.FormalLanguage;
import org.jalgo.module.app.core.dataType.rationalNumber.PositiveRationalNumber;
import org.jalgo.module.app.core.graph.Graph;
import org.jalgo.module.app.core.graph.Node;

/**
 * The MainController is the communicator between the specific controllers and
 * the InterfaceController and observes also the actions, which he gets from the
 * specific controllers and the InterfaceController, to send them to the core.
 * Also the core gives the Information to him, so that he can arrange the
 * transfer to the other controller. Furthermore the MainController initializes
 * all controller.
 */
public class MainController {

	private AlgorithmController algorithmCtrl;

	private GraphController graphCtrl;

	private SemiringController semiringCtrl;

	private InterfaceController interfaceCtrl;

	// Class in the core to get all information from the controller.
	private Calculation calculation;

	// Connector to JAlgo
	private ModuleConnector moduleConnector;

	/**
	 * Creates a main controller which holds all other controller.
	 * 
	 * @param module The module connector as the link to j-Algo.
	 */
	public MainController(ModuleConnector module) {
		moduleConnector = module;
		calculation = new Calculation();

		initControllers();
		interfaceCtrl.setSemiringLayout();

		// dummyInit(0);
	}

	// Initialize the Tool with hard coded graphs.
	@SuppressWarnings("unused")
	private void dummyInit(int id) {
		SemiRing ring = null;
		Graph graph = new Graph();
		Node node;

		switch (id) {
		case 0:
			ring = AvailableSemiRings
					.getSemiRing(AvailableSemiRings.SHORTEST_PATH_PROBLEM_ID);

			// Build Graph
			node = graph.newNode();
			node.setLocation(new Point2D.Float(300, 150));

			node = graph.newNode();
			node.setLocation(new Point2D.Float(50, 150));

			node = graph.newNode();
			node.setLocation(new Point2D.Float(175, 350));

			node = graph.newNode();
			node.setLocation(new Point2D.Float(175, 50));

			graph.addEdge(0, 2, new PositiveRationalNumber(5));
			graph.addEdge(2, 0, new PositiveRationalNumber(1));
			graph.addEdge(1, 0, new PositiveRationalNumber(8));
			graph.addEdge(1, 3, new PositiveRationalNumber(3));
			graph.addEdge(2, 1, new PositiveRationalNumber(2));
			graph.addEdge(3, 0, new PositiveRationalNumber(2));
			graph.addEdge(0, 1, new PositiveRationalNumber(8));
			break;

		case 1:
			ring = AvailableSemiRings
					.getSemiRing(AvailableSemiRings.PROCESS_PROBLEM_ID);

			// Build Graph
			node = graph.newNode();
			node.setLocation(new Point2D.Float(300, 150));

			node = graph.newNode();
			node.setLocation(new Point2D.Float(50, 150));

			node = graph.newNode();
			node.setLocation(new Point2D.Float(175, 250));

			node = graph.newNode();
			node.setLocation(new Point2D.Float(175, 50));

			graph.addEdge(0, 2, new FormalLanguage("a"));
			graph.addEdge(1, 0, new FormalLanguage("c"));
			graph.addEdge(1, 3, new FormalLanguage("a"));
			graph.addEdge(2, 1, new FormalLanguage("b"));
			graph.addEdge(3, 0, new FormalLanguage("b"));
			break;

		default:
			throw new IllegalArgumentException();
		}

		// Create calculation
		calculation.setSemiring(ring);
		calculation.setGraph(graph);

		// Notify
		interfaceCtrl.setEditLayout();
	}

	/**
	 * Initializes all Controllers (InterfaceController, SemringController,
	 * GraphController, AlgorithmController) and sets j-Algo's toolbar and menu.
	 */
	private void initControllers() {
		JComponent rootPane;
		JToolBar toolbar;
		JMenu menu;

		rootPane = JAlgoGUIConnector.getInstance().getModuleComponent(moduleConnector);
		toolbar = JAlgoGUIConnector.getInstance().getModuleToolbar(moduleConnector);
		menu = JAlgoGUIConnector.getInstance().getModuleMenu(moduleConnector);

		interfaceCtrl = new InterfaceController(menu, toolbar, rootPane, this);

		semiringCtrl = new SemiringController(this, interfaceCtrl
				.getSemiringPanel());

		algorithmCtrl = new AlgorithmController(this, 
												interfaceCtrl.getAlgorithmPanel(), 
												interfaceCtrl.getAlgorithmToolPanel()
											   );

		graphCtrl = new GraphController(this, interfaceCtrl
				.getFormalGraphPanel(), interfaceCtrl.getGraphPanel(),
				interfaceCtrl.getGraphToolPanel());

	}

	/**
	 * Closes the APP module (used to remove the formula windows after closing) 
	 */
	public void close() {
		algorithmCtrl.closeFormulaWindow();
	}
	
	/**
	 * Creates a new Graph in the model, sets the Semiring and the graph for the
	 * calculation and notifies the <code>InterfaceController</code> to change
	 * the layout to the EditLayout.
	 * 
	 * @param semiring
	 *            the semiring needed to initialize the calculation.
	 */
	public void newGraph(SemiRing semiring) {
		Graph graph = new Graph();

		// Create calculation
		calculation.setSemiring(semiring);
		calculation.setGraph(graph);

		// Notify
		interfaceCtrl.setEditLayout();
	}

	/**
	 * Loads a saved graph including its semiring.
	 * 
	 * @param graph
	 * @param semiring
	 */
	public void load(Graph graph, SemiRing semiring) {
		calculation.setGraph(graph);
		calculation.setSemiring(semiring);

		if (semiring != null)
			interfaceCtrl.setEditLayout();
		else
			interfaceCtrl.setSemiringLayout();
	}

	/**
	 * Updates all three Controllers (and therefore their respective components)
	 * according to the new display mode.
	 */
	public void displayModeChanged() {
		semiringCtrl.updateDisplay(interfaceCtrl.getDisplayMode());
		graphCtrl.updateDisplay(interfaceCtrl.getDisplayMode());
		algorithmCtrl.updateDisplay(interfaceCtrl.getDisplayMode());
	}

	/**
	 * Gets the precalculated calculation of the aho algorithm.
	 * 
	 * @return The calculation.
	 */
	public Calculation getCalculation() {
		return calculation;
	}

	/**
	 * Sets the calculation.
	 * 
	 * @param calculation The new calculation.
	 */
	public void setCalculation(Calculation calculation) {
		this.calculation = calculation;
	}

	/**
	 * Gets the interface controller.
	 * 
	 * @return The interface controller.
	 */
	public InterfaceController getInterfaceController() {
		return interfaceCtrl;
	}

	/**
	 * Gets the graph controller.
	 * 
	 * @return The graph controller.
	 */
	public GraphController getGraphController() {
		return graphCtrl;
	}

	/**
	 * Gets the semiring controller.
	 * 
	 * @return The semiring controller.
	 */
	public SemiringController getSemiringController() {
		return semiringCtrl;
	}

	/**
	 * Gets the algorithm controller.
	 * 
	 * @return The algorithm controller.
	 */
	public AlgorithmController getAlgorithmController() {
		return algorithmCtrl;
	}

	/**
	 * Sets the save status to "nothing to save". All save buttons will be disabled. 
	 * This happens when there is nothing to save. 
	 */
	public void setNothingToSave() {
		moduleConnector.setSaveStatus(SaveStatus.NOTHING_TO_SAVE);
	}

	/**
	 * Sets the save status to "no changes". The save button will be disabled,
	 * but the "save as" button remains enabled.
	 * This state is set automatically with every save operation.
	 */
	public void setNoChanges() {
		moduleConnector.setSaveStatus(SaveStatus.NO_CHANGES);

	}

	/**
	 * Sets the save status to "changes to save". All save buttons will be enabled.
	 * This method have to called with every change that can be saved.
	 */
	public void setChangesToSave() {
		moduleConnector.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);

	}

}
