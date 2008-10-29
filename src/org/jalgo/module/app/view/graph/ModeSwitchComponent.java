package org.jalgo.module.app.view.graph;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jalgo.main.util.Messages;
import org.jalgo.module.app.controller.GraphActionListener;
import org.jalgo.module.app.controller.GraphObserver;
import org.jalgo.module.app.controller.InterfaceMode;
import org.jalgo.module.app.controller.MainController;
import org.jalgo.module.app.core.graph.Edge;
import org.jalgo.module.app.core.graph.Graph;
import org.jalgo.module.app.core.graph.Node;

/**
 * Displays the two Buttons to toggle between Graph Editing and Algorithm Mode.
 * 
 */
public class ModeSwitchComponent extends JPanel implements GraphObserver {
	private static final long serialVersionUID = 3586672679853106515L;
	private ButtonGroup modeGroup;
	private JToggleButton editToggle, runToggle, beamerToggle;
	private MainController mainCtrl;
	private GraphActionListener graphActionListener;
	
	private JMenu menu;
	private JCheckBoxMenuItem beamerModeMenu;
	
	/**
	 * Initializes the toggler between the two modes (edit mode and algorithm mode).
	 * 
	 */
	public ModeSwitchComponent(MainController mainCtrl, JMenu menu) {

		this.mainCtrl = mainCtrl;

		// Setup control menu
		beamerModeMenu = new JCheckBoxMenuItem(Messages.getString("app", "ModeSwitchComponent.beamerToggle"),
											   new ImageIcon(Messages.getResourceURL("app","icon_beamerMode")),
											   false
											  );
		beamerModeMenu.addActionListener(new BeamerToggleListener());
		
		menu.add(beamerModeMenu);
		
		// Button Group: edit / run
		editToggle = new JToggleButton();
		runToggle = new JToggleButton();

		modeGroup = new ButtonGroup();
		modeGroup.add(editToggle);
		modeGroup.add(runToggle);
		editToggle.setSelected(true);

		editToggle.setIcon(new ImageIcon(Messages.getResourceURL("app","icon_editMode")));  //$NON-NLS-1$ //$NON-NLS-1$
		editToggle.setToolTipText(Messages.getString("app", "ModeSwitchComponent.editToggleToolTip")); //$NON-NLS-1$ //$NON-NLS-2$
		editToggle.setText(Messages.getString("app", "ModeSwitchComponent.editToggle")); //$NON-NLS-1$ //$NON-NLS-2$

		runToggle.setIcon(new ImageIcon(Messages.getResourceURL("app", "icon_runMode")));  //$NON-NLS-1$ //$NON-NLS-1$
		runToggle.setToolTipText(Messages.getString("app", "ModeSwitchComponent.runToggleToolTip")); //$NON-NLS-1$ //$NON-NLS-2$
		runToggle.setText(Messages.getString("app", "ModeSwitchComponent.runToggle")); //$NON-NLS-1$ //$NON-NLS-2$

		if (mainCtrl.getGraphController() == null
				|| mainCtrl.getGraphController().getGraph() == null
				|| mainCtrl.getGraphController().getGraph().getEdges()
						.isEmpty())
			runToggle.setEnabled(false);

		editToggle.addActionListener(new EditToggleListener());
		runToggle.addActionListener(new RunToggleListener());

		// Beamer Toggle
		beamerToggle = new JToggleButton();
		beamerToggle.setIcon(new ImageIcon(Messages.getResourceURL("app","icon_beamerMode")));
		beamerToggle.setToolTipText(Messages.getString("app", "ModeSwitchComponent.beamerToggle")); //$NON-NLS-1$
		beamerToggle.setText(Messages.getString("app", "ModeSwitchComponent.beamerToggle")); //$NON-NLS-1$

		beamerToggle.addActionListener(new BeamerToggleListener());
		
		// Layouting		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setAlignmentX(LEFT_ALIGNMENT);

		this.add(Box.createRigidArea(new Dimension(5,0)));
		this.add(editToggle);
		this.add(Box.createRigidArea(new Dimension(5,0)));
		this.add(runToggle);

		this.add(Box.createHorizontalGlue());
		this.add(beamerToggle);
		this.add(Box.createRigidArea(new Dimension(5,0)));
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
		editToggle.setEnabled(state);
		if (mainCtrl.getGraphController() == null
				|| mainCtrl.getGraphController().getGraph() == null
				|| mainCtrl.getGraphController().getGraph().getEdges()
						.isEmpty()) {
			runToggle.setEnabled(false);
		} else {
			runToggle.setEnabled(state);
		}
		beamerToggle.setEnabled(state);
	}
	
	/**
	 * Enables the modeButton from this Mode, where the user currently works.
	 * @param currentDisplayMode
	 * 				@see InterfaceMode 
	 * @param noGraphElement
	 * 				<code>true</code> sets runToggle disable
	 * 				<code>false</code> sets runToggle enable
	 */
	public void setCurrentModeButtonEnable(InterfaceMode currentDisplayMode, boolean noGraphElement){
					
		if(currentDisplayMode==InterfaceMode.GRAPH_EDITING){
			editToggle.setEnabled(false);
		}
		else if(currentDisplayMode==InterfaceMode.GRAPH_EDITING&&noGraphElement){
			runToggle.setEnabled(false);
		}
		else if(currentDisplayMode==InterfaceMode.GRAPH_EDITING&&!noGraphElement){
			runToggle.setEnabled(true);
		}
		else if(currentDisplayMode==InterfaceMode.ALGORITHM_DISPLAY){
			runToggle.setEnabled(false);
			editToggle.setEnabled(true);
		}
	}

	/**
	 * ActionListener for the ToggleButton to reach the Graph-Editing-Mode.
	 */
	class EditToggleListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			mainCtrl.getInterfaceController().setEditLayout();
		}

	}

	class BeamerToggleListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			AbstractButton source = (AbstractButton)event.getSource();
			
			if (source.isSelected()) {
				mainCtrl.getInterfaceController().setBeamerLayout(true);
				beamerModeMenu.setSelected(true);
				beamerToggle.setSelected(true);
			}
			else {
				mainCtrl.getInterfaceController().setBeamerLayout(false);
				beamerModeMenu.setSelected(false);
				beamerToggle.setSelected(false);
			}
		}

	}
	
	/**
	 * ActionListener for the ToggleButton to reach the Algorithm-Process-Mode.
	 */
	private class RunToggleListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
				mainCtrl.getInterfaceController().setAlgorithmLayout();
									
		}
	}
	
	public void edgeAdded(Graph graph, Edge edge) {
		runToggle.setEnabled(true);
	}

	public void edgeAltered(Graph graph, Edge edge) {

	}
	
	public void edgeRemoved(Graph graph, Edge edge) {
		if (mainCtrl.getGraphController().getGraph().getEdges().isEmpty())
			runToggle.setEnabled(false);
	}

	public void graphSelectionChanged() {
	}

	public void graphUpdated() {
	}

	public void nodeAdded(Graph graph, Node node) {
	}

	public void nodeAltered(Graph graph, Node node) {
	}

	public void nodeRemoved(Graph graph, Node node) {
	}

	public void setGraphActionListener(GraphActionListener listener) {
		graphActionListener = listener;
		graphActionListener.addGraphObserver(this);
	}

	public GraphActionListener getGraphActionListener() {
		return graphActionListener;
	}
}
