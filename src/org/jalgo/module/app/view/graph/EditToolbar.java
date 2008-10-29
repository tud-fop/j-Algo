package org.jalgo.module.app.view.graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.text.JTextComponent;

import org.jalgo.main.util.Messages;
import org.jalgo.module.app.controller.GraphActionListener;
import org.jalgo.module.app.controller.GraphController;
import org.jalgo.module.app.controller.GraphObserver;
import org.jalgo.module.app.controller.GraphActionListener.EditMode;
import org.jalgo.module.app.core.graph.Edge;
import org.jalgo.module.app.core.graph.Graph;
import org.jalgo.module.app.core.graph.Node;

public class EditToolbar extends JPanel implements GraphObserver {

	private static final long serialVersionUID = -8480050495295128178L;

	private GraphActionListener graphActionListener;
	private GraphController graphCtrl;
	
	private ButtonGroup modeGroup;
	private JToggleButton nodeEdit, edgeEdit;
	private JButton addButton, deleteButton, modifyButton, undoButton,
			redoButton;
	private JPanel spacer_mode, spacer_undo, spacer_char;
	
	private List<JButton> specialCharButtons;
	
	private boolean enabled;
	
	@SuppressWarnings("unused")
	private String[] specialChars;

	public class JSpecialCharButton extends JButton {

		private static final long serialVersionUID = 4161178148628990677L;
		
	}
	
	public EditToolbar(GraphController graphCtrl) {
		this.graphCtrl = graphCtrl;
		graphCtrl.addGraphObserver(this);

		specialCharButtons = new ArrayList<JButton>();
		
		nodeEdit = new JToggleButton();
		edgeEdit = new JToggleButton();
		addButton = new JButton();
		deleteButton = new JButton();
		modifyButton = new JButton();
		undoButton = new JButton();
		redoButton = new JButton();
		
		// Setup Buttons
		nodeEdit.setIcon(new ImageIcon(Messages.getResourceURL("app", //$NON-NLS-1$
				"icon_nodeEdit"))); //$NON-NLS-1$
		edgeEdit.setIcon(new ImageIcon(Messages.getResourceURL("app", //$NON-NLS-1$
				"icon_edgeEdit"))); //$NON-NLS-1$
		addButton.setIcon(new ImageIcon(Messages.getResourceURL("app", //$NON-NLS-1$
				"icon_addEdit"))); //$NON-NLS-1$
		deleteButton.setIcon(new ImageIcon(Messages.getResourceURL("app", //$NON-NLS-1$
				"icon_deleteEdit"))); //$NON-NLS-1$
		modifyButton.setIcon(new ImageIcon(Messages.getResourceURL("app", //$NON-NLS-1$
				"icon_modifyEdit"))); //$NON-NLS-1$
		undoButton.setIcon(new ImageIcon(Messages.getResourceURL("app", //$NON-NLS-1$
				"icon_undoEdit"))); //$NON-NLS-1$
		redoButton.setIcon(new ImageIcon(Messages.getResourceURL("app", //$NON-NLS-1$
				"icon_redoEdit"))); //$NON-NLS-1$

		nodeEdit.setToolTipText(Messages.getString("app","EditToolbar.nodeEdit")); //$NON-NLS-1$ //$NON-NLS-2$
		edgeEdit.setToolTipText(Messages.getString("app","EditToolbar.edgeEdit")); //$NON-NLS-1$ //$NON-NLS-2$
		addButton.setToolTipText(Messages.getString("app","EditToolbar.addButton")); //$NON-NLS-1$ //$NON-NLS-2$
		deleteButton.setToolTipText(Messages.getString("app","EditToolbar.deleteButton")); //$NON-NLS-1$ //$NON-NLS-2$
		modifyButton.setToolTipText(Messages.getString("app","EditToolbar.modifyButton")); //$NON-NLS-1$ //$NON-NLS-2$
		undoButton.setToolTipText(Messages.getString("app","EditToolbar.undoButton")); //$NON-NLS-1$ //$NON-NLS-2$
		redoButton.setToolTipText(Messages.getString("app","EditToolbar.redoButton")); //$NON-NLS-1$ //$NON-NLS-2$
		
		undoButton.setName("undoButton"); //$NON-NLS-1$
		redoButton.setName("redoButton"); //$NON-NLS-1$

		nodeEdit.addActionListener(new nodeModeListener());
		edgeEdit.addActionListener(new edgeModeListener());
		addButton.addActionListener(new addButtonListener());
		deleteButton.addActionListener(new deleteButtonListener());
		modifyButton.addActionListener(new modifyButtonListener());
		undoButton.addActionListener(new undoButtonListener());
		redoButton.addActionListener(new redoButtonListener());

		// Setup Button Group
		modeGroup = new ButtonGroup();
		modeGroup.add(nodeEdit);
		modeGroup.add(edgeEdit);
		nodeEdit.setSelected(true);

		spacer_mode = new JPanel();
		spacer_mode.setSize(60, 0);

		spacer_undo = new JPanel();
		spacer_undo.setSize(60, 0);

		// Setup Tool Bar
		this.add(nodeEdit);
		this.add(edgeEdit);
		this.add(spacer_mode);
		this.add(addButton);
		this.add(deleteButton);
		this.add(modifyButton);
		this.add(spacer_undo);
		this.add(undoButton);
		this.add(redoButton);
		// this.setBorder(border);
		
		modifyButton.setVisible(false);

	}

	public void setSpecialCharButtons(String[] specialChars) {
		this.specialChars = specialChars;		
		
		if (specialChars != null) {
			spacer_char = new JPanel();
			spacer_char.setSize(60, 0);		
			this.add(spacer_char);
			
			for (int i = 0; i < specialChars.length; i ++) {
				JButton spButton;
				SpecialCharButtonListener listener;
				
				spButton = new JSpecialCharButton();
				listener = new SpecialCharButtonListener();
				
				spButton.setText(specialChars[i]);
				spButton.setToolTipText(Messages.getString("app","EditToolbar.specialCharButton")+": "+specialChars[i]);
								
				spButton.addActionListener(listener);
				spButton.addFocusListener(listener);
				
				specialCharButtons.add(spButton);
				this.add(spButton);
			}
		}		
	}
	
	public void setEnabled(boolean state, boolean undo, boolean redo) {
		this.enabled = state;
		
		nodeEdit.setEnabled(state);
		edgeEdit.setEnabled(state);
		addButton.setEnabled(state);
		
		if (graphCtrl.getSelectedEdge() != null || graphCtrl.getSelectedNode() != null)
			deleteButton.setEnabled(state);
		else 
			deleteButton.setEnabled(false);
		modifyButton.setEnabled(state);
		undoButton.setEnabled(undo);
		redoButton.setEnabled(redo);
		if (specialCharButtons != null)
		for (JButton spButton : specialCharButtons) {
			if (graphCtrl.getSelectedEdge() != null)
				spButton.setEnabled(state);
			else
				spButton.setEnabled(false);
		}
	}

	public void updateButtons() {
		if (graphCtrl.getSelectedEdge() != null || graphCtrl.getSelectedNode() != null)
			deleteButton.setEnabled(enabled);
		else 
			deleteButton.setEnabled(false);
	}
	
	
	public void setUndoState() {
		undoButton.setEnabled(getGraphActionListener().getUndoManager()
				.isUndoable());
		redoButton.setEnabled(getGraphActionListener().getUndoManager()
				.isRedoable());
	}
	
	public void setGraphActionListener(GraphActionListener listener) {
		graphActionListener = listener;
	}

	public GraphActionListener getGraphActionListener() {
		return graphActionListener;
	}

	class nodeModeListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			modifyButton.setSelected(false);
			graphActionListener.setEditMode(EditMode.EDITING_NODES);
		}
	}

	class edgeModeListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			modifyButton.setSelected(false);
			graphActionListener.setEditMode(EditMode.EDITING_EDGES);
		}
	}

	class addButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (graphActionListener.getEditMode() == EditMode.EDITING_NODES)
				graphActionListener.addNode();
		}
	}

	class deleteButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (graphActionListener.getSelectedEdge() != null)
				graphActionListener.removeEdge(graphActionListener
						.getSelectedEdge());
			else if (graphActionListener.getSelectedNode() != null)
				graphActionListener.removeNode(graphActionListener
						.getSelectedNode());
		}
	}

	class modifyButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			graphActionListener.setEditMode(EditMode.EDITING_WEIGHT);
			modifyButton.setSelected(true);
		}
	}

	class undoButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			getGraphActionListener().getUndoManager().undo();
			undoButton.setEnabled(getGraphActionListener().getUndoManager()
					.isUndoable());
			redoButton.setEnabled(getGraphActionListener().getUndoManager()
					.isRedoable());
		}
	}
	
	class redoButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent arg0) {
			getGraphActionListener().getUndoManager().redo();
			undoButton.setEnabled(getGraphActionListener().getUndoManager()
					.isUndoable());
			redoButton.setEnabled(getGraphActionListener().getUndoManager()
					.isRedoable());
		}
	}
	
	class SpecialCharButtonListener implements ActionListener, FocusListener {
		private JTextComponent other;
		
		public void actionPerformed(ActionEvent arg0) {
			JButton button;
			char label;
			
			if ((other == null) && !(other instanceof JTextComponent))
				return;		
			
			button = (JButton)arg0.getSource();
			label = button.getText().charAt(0);

			other.dispatchEvent(new KeyEvent(other, KeyEvent.KEY_PRESSED, 0, 0,KeyEvent.VK_UNDEFINED, label));
			other.dispatchEvent(new KeyEvent(other, KeyEvent.KEY_RELEASED, 0, 0,KeyEvent.VK_UNDEFINED, label));
			other.dispatchEvent(new KeyEvent(other, KeyEvent.KEY_TYPED, 0, 0,KeyEvent.VK_UNDEFINED, label));
			
			other.requestFocus();
		}

		public void focusGained(FocusEvent event) {
			if (!(event.getOppositeComponent() instanceof JTextComponent))
				other = null;
			else
				other = (JTextComponent)event.getOppositeComponent();
		}

		public void focusLost(FocusEvent event) {
			other = null;
		}
		
	}
	
	public void edgeAdded(Graph graph, Edge edge) {
	}

	public void edgeAltered(Graph graph, Edge edge) {
	}

	public void edgeRemoved(Graph graph, Edge edge) {
	}

	public void graphSelectionChanged() {
		if (graphCtrl.getSelectedEdge() == null && graphCtrl.getSelectedNode() == null) {
			deleteButton.setEnabled(false);
		} else {
			deleteButton.setEnabled(true);
		}
		
		if (graphCtrl.getSelectedEdge() == null || graphCtrl.getGraphTextComponentFocusState()) {
			for (JButton spButton : specialCharButtons)
				spButton.setEnabled(false);
		} else {
			for (JButton spButton : specialCharButtons)
				spButton.setEnabled(true);
		}
	}

	public void graphUpdated() {
	}

	public void nodeAdded(Graph graph, Node node) {
	}

	public void nodeAltered(Graph graph, Node node) {
	}

	public void nodeRemoved(Graph graph, Node node) {
	}

}
