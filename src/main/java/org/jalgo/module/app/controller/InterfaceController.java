package org.jalgo.module.app.controller;

import java.awt.*;

import javax.swing.*;

import org.jalgo.module.app.view.graph.ModeSwitchComponent;

/**
 * Initializes the superior panels for the allocation of the user interface. The
 * <code>InterfaceController</code> coordinates the main actions of the user
 * interface.
 */
public class InterfaceController {

	// Connection to the MainController to communicate with the core and the
	// specific controllers.
	private MainController mainCtrl;

	// Panels and Components, which the InterfaceController initializes.
	private JToolBar toolBar;
	private JMenu menu;
	private JPanel graphPanel, graphToolPanel;
	private JPanel formalGraphPanel, algorithmPanel, algorithmToolPanel;
	private JPanel semiringPanel;
	private ModeSwitchComponent modePanel;

	// Superior panels, which divide the User Interface.
	private JComponent rootPanel;
	private JPanel headPanel;
	private JPanel workPanel;
	private JPanel leftPanel, rightPanel;
	private BorderLayout leftLayout, rightLayout;
	private BorderLayout beamerLayout;
	private BorderLayout layout;
	private boolean isBeamerLayout;

	// Setting by this Controller the current display state.
	private InterfaceMode currentDisplay;

	/**
	 * Instantiates the jAlgo Module with a "basic interface": a menu, the
	 * toolbar and a rootpane for all other components)
	 * 
	 * @param menu
	 *            the menu entry for this module.
	 * @param toolbar
	 *            the toolbar on the button that can be used in this module.
	 * 
	 * @param rootPane
	 *            the pane to put all components in.
	 * @param mainCtrl
	 *            connection to the <code>MainController</code.>
	 */
	public InterfaceController(JMenu menu, 
							   JToolBar toolbar, 
							   JComponent rootPane, 
							   MainController mainCtrl
							  ) 
	{
		this.mainCtrl = mainCtrl;
		this.rootPanel = rootPane;
		this.menu = menu;
		this.toolBar = toolbar;

		isBeamerLayout = false;

		initLayout();
	}

	/**
	 * Initializes the main panels and arranges them in the layout.
	 * 
	 */
	private void initLayout() {

		workPanel = new JPanel();

		leftPanel = new JPanel();
		graphPanel = new JPanel();
		graphToolPanel = new JPanel();

		rightPanel = new JPanel();
		algorithmPanel = new JPanel();
		algorithmToolPanel = new JPanel();
		formalGraphPanel = new JPanel();

		headPanel = new JPanel();
		semiringPanel = new JPanel();
		modePanel = new ModeSwitchComponent(mainCtrl, menu);

		// Arrange layouts
		beamerLayout = new BorderLayout();
		layout = new BorderLayout();
		beamerLayout.setHgap(7);
		rootPanel.setLayout(layout);

		rootPanel.add(workPanel, BorderLayout.CENTER);
		rootPanel.add(headPanel, BorderLayout.NORTH);

		arrangeWorkPanelLayout();

		leftLayout = new BorderLayout();
		leftPanel.setLayout(leftLayout);
		leftPanel.add(graphPanel, BorderLayout.CENTER);
		leftPanel.add(graphToolPanel, BorderLayout.SOUTH);

		rightLayout = new BorderLayout();
		rightPanel.setLayout(rightLayout);
		rightPanel.add(semiringPanel, BorderLayout.NORTH);
		rightPanel.add(algorithmPanel, BorderLayout.CENTER);
		rightPanel.add(algorithmToolPanel, BorderLayout.SOUTH);
		rightPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		semiringPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		layout = new BorderLayout();
		headPanel.setLayout(layout);
		headPanel.add(Box.createRigidArea(new Dimension(1, 5)),
				BorderLayout.NORTH);
		headPanel.add(modePanel, BorderLayout.CENTER);
		headPanel.add(Box.createRigidArea(new Dimension(1, 5)),
				BorderLayout.SOUTH);

		// Set Borders
		leftPanel.setBorder(BorderFactory.createMatteBorder(1, -1, 1, 1,
				Color.LIGHT_GRAY));
		rightPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, -1,
				Color.LIGHT_GRAY));

		semiringPanel.setBorder(BorderFactory.createMatteBorder(-1, -1, 1, -1,
				Color.LIGHT_GRAY));
		graphToolPanel.setBorder(BorderFactory.createMatteBorder(1, -1, -1, -1,
				Color.LIGHT_GRAY));
		algorithmToolPanel.setBorder(BorderFactory.createMatteBorder(1, -1, -1,
				-1, Color.LIGHT_GRAY));

		graphPanel.setLayout(new GridLayout());
	}

	/**
	 * Creates a new GridLayout and assigns it to the workPanel.
	 */
	public void arrangeWorkPanelLayout() {
		GridLayout workLayout;
		workLayout = new GridLayout(1, 2);
		workLayout.setHgap(7);
		workPanel.setLayout(workLayout);
		workPanel.add(leftPanel);
		workPanel.add(rightPanel);
	}

	/**
	 * Enables the <code>editToolbar</code>, so that it's invisible in the
	 * User Interface. The use of this method depends on the
	 * <code>currentDisplaymode</code>.
	 * 
	 * @param state
	 *            <code>true</code> if enabled, <code>false</code> if
	 *            disabled.
	 */
	public void setToolbarEnabled(boolean state) {
		modePanel.setToolbarEnabled(state);
	}

	/**
	 * Method to make all panels visible again.
	 */
	private void makePanelsVisible() {
		graphPanel.setVisible(true);
		formalGraphPanel.setVisible(true);
		algorithmPanel.setVisible(true);
		graphToolPanel.setVisible(true);
		algorithmToolPanel.setVisible(true);
		modePanel.setVisible(true);

		setToolbarEnabled(true);
	}

	/**
	 * Enables/disables all component of <code>root</code> recursively.
	 * 
	 * @param root
	 *            the <code>Container</code> in which all
	 *            <code>Component</code>s should be enabled/disabled.
	 * 
	 * @param enabled
	 *            <code>true</code> to enable all <code>Component</code>s,
	 *            <code>false</code> to disable them.
	 */
	private void setEnabledRecursively(Container root, boolean enabled) {
		root.setEnabled(enabled);
		for (Component c : root.getComponents()) {
			if (c instanceof Container)
				setEnabledRecursively((Container) c, enabled);
			else
				c.setEnabled(enabled);
		}
	}

	/**
	 * Sets SemiRingPanel visible and all other panel invisible. On this you can
	 * see Semiring selection.
	 */
	public void setSemiringLayout() {
		graphPanel.setVisible(false);
		formalGraphPanel.setVisible(false);
		algorithmPanel.setVisible(false);
		graphToolPanel.setVisible(false);
		algorithmToolPanel.setVisible(false);
		modePanel.setEnabled(false);

		currentDisplay = InterfaceMode.SEMIRING_EDITING;

		setToolbarEnabled(false);
		mainCtrl.displayModeChanged();
	}

	/**
	 * Sets the panels visible depending on the currentDisplayMode. Prepare the
	 * Graph-Editing-Mode.
	 */
	public void setEditLayout() {

		boolean undo;
		boolean redo;
		if (currentDisplay == InterfaceMode.ALGORITHM_DISPLAY) {
			rightPanel.remove(algorithmPanel);

		}

		makePanelsVisible();

		algorithmToolPanel.setEnabled(false);
		graphToolPanel.setEnabled(true);

		workPanel.remove(rightPanel);
		workPanel.remove(leftPanel);
		arrangeWorkPanelLayout();

		rightPanel.add(formalGraphPanel, BorderLayout.CENTER);
		rightPanel.repaint();
		rightPanel.revalidate();

		workPanel.repaint();
		workPanel.revalidate();

		currentDisplay = InterfaceMode.GRAPH_EDITING;

		setEnabledRecursively(algorithmToolPanel, false);
		// setEnabledRecursively(graphToolPanel, true);
		modePanel.setCurrentModeButtonEnable(currentDisplay, true);

		undo = mainCtrl.getGraphController().getUndoManager().isUndoable();
		redo = mainCtrl.getGraphController().getUndoManager().isRedoable();
		mainCtrl.getGraphController().setToolbarEnabled(true, undo, redo);

		mainCtrl.displayModeChanged();
	}

	/**
	 * Sets the panels visible depending on the currentDisplayMode. Prepare the
	 * Algorithm-Process-Mode.
	 */
	public void setAlgorithmLayout() {

		if (currentDisplay == InterfaceMode.GRAPH_EDITING) {
			rightPanel.remove(formalGraphPanel);
		}

		makePanelsVisible();
		algorithmToolPanel.setEnabled(true);
		graphToolPanel.setEnabled(false);

		rightPanel.add(algorithmPanel, BorderLayout.CENTER);
		rightPanel.repaint();
		rightPanel.revalidate();

		if (isBeamerLayout) {
			workPanel.remove(rightPanel);
			workPanel.remove(leftPanel);

			BorderLayout beamerLayout = new BorderLayout();

			workPanel.setLayout(beamerLayout);

			workPanel.add(leftPanel, BorderLayout.WEST);
			workPanel.add(rightPanel, BorderLayout.CENTER);
			beamerLayout.setVgap(10);

		}

		else {
			workPanel.remove(rightPanel);
			workPanel.remove(leftPanel);

			arrangeWorkPanelLayout();
		}

		workPanel.repaint();
		workPanel.revalidate();

		currentDisplay = InterfaceMode.ALGORITHM_DISPLAY;
		modePanel.setCurrentModeButtonEnable(currentDisplay, false);

		setEnabledRecursively(algorithmToolPanel, true);
		setEnabledRecursively(graphToolPanel, false);

		mainCtrl.displayModeChanged();

	}

	/**
	 * Changes the layout after the beamerLayout has been switched on or off.
	 */
	public void changeLayout() {
		if (isBeamerLayout) {
			mainCtrl.getGraphController().changeTextComponentFont();
		} else {
			mainCtrl.getGraphController().changeTextComponentFont();
		}

		if (currentDisplay == InterfaceMode.ALGORITHM_DISPLAY) {
			setAlgorithmLayout();
		} else if (currentDisplay == InterfaceMode.GRAPH_EDITING) {
			setEditLayout();
		} else if (currentDisplay == InterfaceMode.SEMIRING_EDITING) {
			setSemiringLayout();
		}
	}

	/**
	 * Gets the panel of the semiring.
	 * 
	 * @return The semiring panel.
	 */
	public JPanel getSemiringPanel() {
		return semiringPanel;
	}

	/**
	 * Gets the panel of the formular.
	 * 
	 * @return The formular panel.
	 */
	public JPanel getFormalGraphPanel() {
		return formalGraphPanel;
	}

	/**
	 * Gets the panel of the graph.
	 * 
	 * @return The graph panel.
	 */
	public JPanel getGraphPanel() {
		return graphPanel;
	}

	/**
	 * Gets the panel of the graph tools.
	 * 
	 * @return The graph tool panel.
	 */
	public JPanel getGraphToolPanel() {
		return graphToolPanel;
	}

	/**
	 * Gets the panel of the algorithm.
	 * 
	 * @return The algorithm panel. 
	 */
	public JPanel getAlgorithmPanel() {
		return algorithmPanel;
	}

	/**
	 * Gets the panel of the algorithm tools.
	 * 
	 * @return The algorithm tool panel.
	 */
	public JPanel getAlgorithmToolPanel() {
		return algorithmToolPanel;
	}

	/**
	 * Gets the current display mode. This can be SEMIRING_EDITING, GRAPH_EDITING or ALGORITHM_DISPLAY.
	 * 
	 * @return The display mode.
	 */
	public InterfaceMode getDisplayMode() {
		return currentDisplay;
	}

	/**
	 * Gets the component which displays the toggle button for switching between editing and algorithm mode.
	 * 
	 * @return The mode switch component.
	 */
	public ModeSwitchComponent getModeSwitchComponent() {
		return modePanel;
	}

	/**
	 * Returns the current beamerLayout state.
	 * 
	 * @return <code>true</code> if beamerLayout is enabled,
	 *         <code>false</code> otherwise.
	 */
	public boolean isBeamerLayout() {
		return isBeamerLayout;
	}

	/**
	 * Sets the beamerLayout state.
	 * 
	 * @param layout
	 *            if <code>true</code>, the beamerLayout will be enabled, if
	 *            <code>false</code>, it will be disabled.
	 */
	public void setBeamerLayout(boolean layout) {
		isBeamerLayout = layout;
		changeLayout();
		mainCtrl.getGraphController().setBeamerMode(isBeamerLayout);
		mainCtrl.getSemiringController().setBeamerMode(isBeamerLayout);
		mainCtrl.getAlgorithmController().setBeamerMode(isBeamerLayout);
	}

}
