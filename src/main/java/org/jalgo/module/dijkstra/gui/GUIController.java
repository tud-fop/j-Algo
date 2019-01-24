package org.jalgo.module.dijkstra.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.main.gui.event.StatusLineUpdater;
import org.jalgo.main.util.Messages;
import org.jalgo.module.dijkstra.ModuleConnector;
import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.GotoStepAction;
import org.jalgo.module.dijkstra.actions.ShowAlgorithmPageAction;
import org.jalgo.module.dijkstra.actions.ShowEditPageAction;
import org.jalgo.module.dijkstra.gui.components.AlgorithmCalculationTablePanel;
import org.jalgo.module.dijkstra.gui.components.AlgorithmModeToolPanel;
import org.jalgo.module.dijkstra.gui.components.AlgorithmResultTablePanel;
import org.jalgo.module.dijkstra.gui.components.EdgeListPanel;
import org.jalgo.module.dijkstra.gui.components.EditModeToolPanel;
import org.jalgo.module.dijkstra.gui.components.GraphPanel;
import org.jalgo.module.dijkstra.gui.components.MatrixPanel;
import org.jalgo.module.dijkstra.gui.components.NodeListPanel;
import org.jalgo.module.dijkstra.gui.event.ToolbarActionHandler;
import org.jalgo.module.dijkstra.util.DefaultExceptionHandler;

/**
 * The class <code>GUIController</code> is the main part of the gui of the
 * dijkstra module. It is responsible for creating the several gui parts and for
 * updating their states.
 * 
 * @author Alexander Claus
 */
public class GUIController
implements Observer {

	private final Controller controller;

	private JToolBar toolbar;
	private JButton undoButton;
	private JButton redoButton;
	private ToolbarActionHandler toolbarActionHandler;

	private JComponent rootPane;
	private GraphPanel graphPane;
	private EditModeToolPanel editModeToolPane;
	private JPanel editModePane;
	private JPanel editModeLeftPane;
	private JPanel algModePane;
	private JPanel algModeLeftPane;
	private JButton startAlgorithmButton;
	private JButton restartAlgorithmButton;
	private JButton editGraphButton;
	private JLabel statusLabel;
	
	public GUIController(ModuleConnector connector, Controller controller) {
		this.controller = controller;
		// get the relevant components from the main program
		rootPane = JAlgoGUIConnector.getInstance().getModuleComponent(connector);
		toolbar = JAlgoGUIConnector.getInstance().getModuleToolbar(connector);

		controller.addObserver(this);
	}

	/**
	 * Constructs the whole gui, namely the toolbar, the graph display panel and
	 * the composed panels for editing and algorithm mode.<br>
	 * Switches the layout initially to the editing mode view.
	 */
	public void createGUI() {
		createToolbar();
		graphPane = new GraphPanel(controller);
		createEditModePanel();
		createAlgorithmModePanel();
		rootPane.setLayout(new BorderLayout());
		showEditModePanel();
	}

	private void createToolbar() {
		toolbarActionHandler = new ToolbarActionHandler(controller);
		undoButton = new JToolbarButton(
			new ImageIcon(Messages.getResourceURL("main", "Icon.Undo")), //$NON-NLS-1$ //$NON-NLS-2$
			Messages.getString("dijkstra", "Undo_tooltip"), //$NON-NLS-1$ //$NON-NLS-2$
			"undo"); //$NON-NLS-1$
		undoButton.setEnabled(false);
		undoButton.addActionListener(toolbarActionHandler);
		undoButton.addMouseListener(StatusLineUpdater.getInstance());
		toolbar.add(undoButton);
		redoButton = new JToolbarButton(
			new ImageIcon(Messages.getResourceURL("main", "Icon.Redo")), //$NON-NLS-1$ //$NON-NLS-2$
			Messages.getString("dijkstra", "Redo_tooltip"), //$NON-NLS-1$ //$NON-NLS-2$
			"redo"); //$NON-NLS-1$
		redoButton.setEnabled(false);
		redoButton.addActionListener(toolbarActionHandler);
		redoButton.addMouseListener(StatusLineUpdater.getInstance());
		toolbar.add(redoButton);
	}

	private void createEditModePanel() {
		editModePane = new JPanel(new BorderLayout());
		JPanel contentPane = new JPanel(new GridLayout(1,2));
		editModeLeftPane = new JPanel(new BorderLayout());
		editModeToolPane = new EditModeToolPanel(controller);
		editModeLeftPane.add(editModeToolPane, BorderLayout.NORTH);
		contentPane.add(editModeLeftPane);

		JPanel rightPane = new JPanel(new BorderLayout());
		JPanel innerRightPane = new JPanel();
		innerRightPane.setLayout(new BoxLayout(innerRightPane, BoxLayout.PAGE_AXIS));
		NodeListPanel nodeListPane = new NodeListPanel(controller);
		innerRightPane.add(nodeListPane);
		EdgeListPanel edgeListPane = new EdgeListPanel(controller);
		innerRightPane.add(edgeListPane);
		MatrixPanel matrixPane = new MatrixPanel(controller);
		innerRightPane.add(matrixPane);
		rightPane.add(innerRightPane, BorderLayout.CENTER);

		JPanel southPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
		startAlgorithmButton = new JButton(Messages.getString("dijkstra", //$NON-NLS-1$
			"StatusbarComposite.Start_algorithm")); //$NON-NLS-1$
		startAlgorithmButton.setToolTipText(Messages.getString("dijkstra", //$NON-NLS-1$
			"StatusbarComposite.Start_algorithm_tooltip")); //$NON-NLS-1$
		startAlgorithmButton.addActionListener(new ActionListener() {
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent e) {
				try {
					new ShowAlgorithmPageAction(controller);
				}
				catch (ActionException ex) {
					new DefaultExceptionHandler(ex);
				}
			}
		});
		southPane.add(startAlgorithmButton);
		rightPane.add(southPane, BorderLayout.SOUTH);
		contentPane.add(rightPane);
		editModePane.add(contentPane, BorderLayout.CENTER);
	}

	private void createAlgorithmModePanel() {
		algModePane = new JPanel(new BorderLayout());
		JPanel contentPane = new JPanel(new GridLayout(1, 2));
		algModeLeftPane = new JPanel(new BorderLayout());
		AlgorithmModeToolPanel toolPane = new AlgorithmModeToolPanel(controller);
		algModeLeftPane.add(toolPane, BorderLayout.NORTH);
		contentPane.add(algModeLeftPane);

		JPanel rightPane = new JPanel(new BorderLayout());
		JPanel innerRightPane = new JPanel(new BorderLayout());
		JPanel tablePane = new JPanel(new GridLayout(2, 1));
		AlgorithmCalculationTablePanel calcTablePane =
			new AlgorithmCalculationTablePanel(controller);
		tablePane.add(calcTablePane);
		AlgorithmResultTablePanel resultTablePane =
			new AlgorithmResultTablePanel(controller);
		tablePane.add(resultTablePane);
		innerRightPane.add(tablePane, BorderLayout.CENTER);
		statusLabel = new JLabel();
		statusLabel.setMaximumSize(new Dimension(1000, 60));
		statusLabel.setMinimumSize(new Dimension(100, 60));
		statusLabel.setPreferredSize(
			new Dimension(statusLabel.getPreferredSize().width, 60));
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusLabel.setVerticalAlignment(SwingConstants.TOP);
		statusLabel.setBorder(new EmptyBorder(2, 2, 2, 2));
		innerRightPane.add(statusLabel, BorderLayout.SOUTH);
		rightPane.add(innerRightPane, BorderLayout.CENTER);

		JPanel southPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
		restartAlgorithmButton = new JButton(Messages.getString("dijkstra", //$NON-NLS-1$
			"StatusbarComposite.Restart_algorithm")); //$NON-NLS-1$
		restartAlgorithmButton.addActionListener(new ActionListener() {
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent e) {
				try {
					new GotoStepAction(controller, 0, true);
				}
				catch (ActionException exc) {
					new DefaultExceptionHandler(exc);
				}
			}
		});
		southPane.add(restartAlgorithmButton);
		editGraphButton = new JButton(Messages.getString("dijkstra", //$NON-NLS-1$
			"StatusbarComposite.Edit_graph")); //$NON-NLS-1$
		editGraphButton.setToolTipText(Messages.getString("dijkstra", //$NON-NLS-1$
			"StatusbarComposite.Edit_graph_tooltip")); //$NON-NLS-1$
		editGraphButton.addActionListener(new ActionListener() {
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent e) {
				try {
					new ShowEditPageAction(controller);
				}
				catch (ActionException ex) {
					new DefaultExceptionHandler(ex);
				}
			}
		});
		southPane.add(editGraphButton);
		rightPane.add(southPane, BorderLayout.SOUTH);
		contentPane.add(rightPane);
		algModePane.add(contentPane, BorderLayout.CENTER);
	}

	/**
	 * Updates the enabled state of the redo- and undo-button.
	 */
	public void updateToolBar() {
		undoButton.setEnabled(controller.hasUndoAction());
		redoButton.setEnabled(controller.hasRedoAction());
	}

	/**
	 * Switches the layout to the editing mode view.
	 */
	public void showEditModePanel() {
		editModeToolPane.reset();
		graphPane.setEditMode(true);
		editModeLeftPane.add(graphPane, BorderLayout.CENTER);
		switchLayoutTo(editModePane);
	}

	/**
	 * Switch the layout to the algorithm mode view.
	 */
	public void showAlgorithmModePanel() {
		graphPane.setEditMode(false);
		algModeLeftPane.add(graphPane, BorderLayout.CENTER);
		switchLayoutTo(algModePane);
	}

	private void switchLayoutTo(JPanel pane) {
		rootPane.removeAll();
		rootPane.add(pane, BorderLayout.CENTER);
		rootPane.updateUI();
	}

	/**
	 * Sets the given text to the result label in algorithm mode. The text can
	 * be in HTML format to support styles.
	 * 
	 * @param text the text to be displayed
	 */
	public void setStatusbarText(String text) {
		statusLabel.setText(text);
	}

	/**
	 * Updates the states of the edit-graph- and the restart-algorithm-button.
	 */
	public void update(Observable o, Object arg) {
		if (controller.getAnimationMillis() > 0 &&
			controller.getEditingMode() == Controller.MODE_ALGORITHM) {
			editGraphButton.setEnabled(false);
			restartAlgorithmButton.setEnabled(false);
		}
        else {
			editGraphButton.setEnabled(true);
			restartAlgorithmButton.setEnabled(true);
        }
	}
}