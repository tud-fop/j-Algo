package org.jalgo.module.hoare.view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jalgo.module.hoare.constants.TextStyle;
import org.jalgo.module.hoare.model.Variable;
import org.jalgo.module.hoare.model.VerificationFormula;
import org.jalgo.module.hoare.view.formula.FormulaViewer;

/**
 * This is the Panel which shows the Variables.
 * 
 * @author Antje
 *
 */
public class WSVar extends WSPart {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Color in which the pre asssertion of the selected verification formula is highlighted.
	 */
	protected static final Color HIGHLIGHT_COLOR_PRE = new Color(0, 128, 0);
	/**
	 * Color in which the post assertion of the selected verification formula is highlighted.
	 */
	protected static final Color HIGHLIGHT_COLOR_POST = new Color(128, 0, 0);
	/**
	 * Color in which the post assertion of the selected verification formula is highlighted.
	 */
	protected static final Color NO_HIGHLIGHT_COLOR = (new JLabel()).getForeground();
	
	/**
	 * The instance itself.
	 */
	private WSVar mirror = this;
	/**
	 * The gui controller.
	 */
	protected final View gui;
	/**
	 * Container for the <code>variableViews</code>.
	 */
	private final Container variablesView;
	/**
	 * Container for the <code>variableViews</code> showing the variables for the pre- and the postcondition of the selected node.
	 */
	private final Container selectedVariablesView;
	/**
	 * Container for the <code>selectedVariablesView</code>.
	 */
	private final Container selectedPanel;
	/**
	 * Map of the variable names to their <code>VariableView</code>.
	 */
	private Map<String,VariableView> variableViews = new HashMap<String,VariableView>();
	
	/**
	 * Creates a new instance of <code>WSVar</code>.
	 * @param gui the gui controller
	 */
	public WSVar(View gui){
		super("variables");
		this.gui = gui;
        
		setLayout(new BorderLayout());
		
		variablesView = new JPanel(null);
		
		selectedVariablesView = new JPanel(new GridLayout(2, 1));

		class SelectedVariablesPanel extends JPanel {
			
			private static final long serialVersionUID = -1444312757194614155L;
			
			public SelectedVariablesPanel() {
				super(new BorderLayout());
			}
			
			public void setFont(Font f) {
				super.setFont(f);
				if (selectedPanel!=null) {
					int width = 1;
					int height = 1;
					for (int i=0; i<getComponentCount(); i++) {
						getComponent(i).setFont(f);
						width = Math.max(width, (int)getComponent(i).getPreferredSize().getWidth());
					}
					if (getComponentCount()>0) {
						height = (int)getComponent(0).getPreferredSize().getHeight();
					}
					FormulaViewer dummy = new FormulaViewer(f);
					dummy.initSize();
					setPreferredSize(new Dimension(width, height+dummy.getHeight()*2));
					setMaximumSize(getPreferredSize());
				}
			}
			
		}
		
		selectedPanel = new SelectedVariablesPanel();
		JLabel selectedTitle = new JLabel(getMessage("selectedVariables"));
		selectedTitle.setMinimumSize(title.getMinimumSize());
		selectedPanel.add(selectedTitle, BorderLayout.NORTH);
		selectedPanel.add(selectedVariablesView, BorderLayout.SOUTH);
		
		add(title, BorderLayout.NORTH);
		add(new JScrollPane(variablesView), BorderLayout.CENTER);
		add(selectedPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Highlights the variables belonging to the selected cell.
	 * Is called when a new cell is celected.
	 * @param cell the cell that has been selected
	 */
	public void newCellSelected(MyCell cell) {
		initVariablesView();
		validate();
		repaint();
	}
	
	/**
	 * Unhighlights all variables.
	 * Is called when all cells have been unselected.
	 */
	public void cellsUnselected() {
		initVariablesView();
		validate();
		repaint();
	}
	
	/**
	 * Shows one variable.
	 * @author antje
	 *
	 */
	protected class VariableView extends JPanel {
		
		/**
		 * serial Id need to make JPanel serializable
		 */
		private static final long serialVersionUID = 8680750215447522047L;
		
		/**
		 * Name of the variable that is shown.
		 */
		protected String name;
		/**
		 * Shows the current value of the variable.
		 */
		protected Component content;
		/**
		 * Listener for showing the appropriate <code>FormulaEditor</code>.
		 */
		private FormulaEditorShower formulaEditorShower = new FormulaEditorShower();
		/**
		 * <code>true</code> if the variable is highlighted, otherwise <code>false</code>.
		 */
		protected boolean highlighted = false;
		
		/**
		 * Creates a new view for the specified variable
		 * @param variableName name of the variable to display
		 * @param content value that always shows the content of the variable
		 */
		public VariableView(String variableName, Component content) {
			this.content = content;
			setVariableName(variableName);
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			GridBagConstraints contentConstraints = new GridBagConstraints(1, 0, 1, 1, 0, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, c.insets, c.ipadx, c.ipady);
			add(content, contentConstraints);
			GridBagConstraints extraLabelConstraints = new GridBagConstraints(2, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, c.insets, c.ipadx, c.ipady);
			add(new JLabel(), extraLabelConstraints);
			content.addMouseListener(gui.new EditVariableListener(getVariableName()));
			
			setFont();
			
			if (content instanceof FormulaViewer) {
				((FormulaViewer)content).initSize();
				setPreferredSize(content.getPreferredSize());
				setSize(getPreferredSize());
			}
		}
		
		/**
		 * Returns the name of the variable the view is for.
		 * @return the name of the variable the view is for
		 */
		public String getVariableName() {
			return name;
		}
		
		/**
		 * Changes the name of the variable the view is for
		 * @param name new variable name
		 */
		protected void setVariableName(String name) {
			this.name = name;
			formulaEditorShower.setVariableName(this.name);
		}
		
		/**
		 * Returns the component that displays the value of the variable.
		 * @return the component that displays the value of the variable
		 */
		public Component getContent() {
			return content;
		}
		
		/**
		 * Changes the component that displays the value of the variable.
		 * @param content new component that displays the value of the variable
		 */
		public void setContent(Component content) {
			this.content = content;
		}
		
		/**
		 * MouseListener that shows a <code>FormulaEditor</code> for the variable on a click event.
		 * @author antje
		 *
		 */
		private class FormulaEditorShower extends MouseAdapter {
			private String variableName = "";
			/**
			 * Sets the name of the variable for the <code>FormulaEditor</code>.
			 * @param variableName
			 */
			public void setVariableName(String variableName) {
				this.variableName = variableName;
			}
			public void mouseClicked(MouseEvent e) {
				gui.showFormulaEditor(variableName);
			}
		}
		
		/**
		 * Highlights the <code>VariableView</code> as a pre assertion.
		 *
		 */
		public void highlightPre() {
			highlight(HIGHLIGHT_COLOR_PRE);
		}
		
		/**
		 * Highlights the <code>VariableView</code> as a post assertion.
		 *
		 */
		public void highlightPost() {
			highlight(HIGHLIGHT_COLOR_POST);
		}
		
		/**
		 * Highligths the <code>VariableView</code> with the specified color.
		 * @param color the highlight color
		 */
		public void highlight(Color color) {
			highlighted = true;
			setColor(color);
		}
		
		/**
		 * Changes the color of the <code>VariableView</code>.
		 * @param color the new color
		 */
		public void setColor (Color color) {
			if (content instanceof FormulaViewer) {
				((FormulaViewer)content).setColor(color);
			}
			else if (content instanceof JLabel) {
				((JLabel)content).setForeground(color);
			}
		}
		
		/**
		 * Unhighlights the <code>VariableView</code>.
		 *
		 */
		public void unhighlight() {
			if (highlighted) {
				setColor(getParent().getBackground());
			}
		}
		
		/**
		 * Sets the font to the global font of the gui.
		 *
		 */
		public void setFont() {
			Font f = gui.getMainFont();
			content.setFont(f);
		}
		
	}
	
	public void update(Observable o, Object arg) {
		initVariablesView();
		validate();
		repaint();
	}
	
	/**
	 * Removes all <code>VariableView</code>s.
	 *
	 */
	protected void clearVariablesView() {
		variablesView.removeAll();
		variableViews.clear();
	}
	
	/**
	 * Shows the current variables of the model.
	 * Highlights the variables of the selected cell.
	 */
	protected void initVariablesView() {
		//show all variables
		clearVariablesView();
		for (Variable v : gui.getModel().getVariables()) {
			initVariableView(v);
		}
		if (variableViews.size()>0) {
			variablesView.setPreferredSize(new Dimension((int)variablesView.getPreferredSize().getWidth(), variableViews.size()*variablesView.getComponent(0).getHeight()));
		}
		highlightSelectedVariables();
		setVariablesViewPreferredSize();
		initSelectedVariablesView();
	}
	
	/**
	 * Creates a new <code>VariableView</code> for the specified variable
	 * @param v variable for the <code>VariableView</code>
	 */
	protected void initVariableView(Variable v) {
		VariableView variableView = createVariableView(v);
		variableView.setBounds(0, variableViews.size()*(int)variableView.getPreferredSize().getHeight(), (int)variableView.getPreferredSize().getWidth(), (int)variableView.getPreferredSize().getHeight());
		variableViews.put(v.getName(), variableView);
		variablesView.add(variableView);
	}
	
	/**
	 * Creates a new <code>VariableView</code> for the specified variable.
	 * @param v the variable
	 * @return the <code>VariableView</code>
	 */
	protected VariableView createVariableView(Variable v) {
		 Component content = new FormulaViewer(v.getAssertion(TextStyle.EDITOR), mirror.getFont(), v.getName());
		return new VariableView(v.getName(), content);
	}
	
	/**
	 * Calculates and sets the preferred size of the <code>variableView</code>.
	 *
	 */
	protected void setVariablesViewPreferredSize() {
		if (variableViews.size()>0) {
			int width = 0;
			for (VariableView v : variableViews.values()) {
				width = Math.max(width, v.getWidth());
			}
			variablesView.setPreferredSize(new Dimension(width, variablesView.getComponent(0).getHeight()*variableViews.size()));
		}
	}
	
	/**
	 * Shows the the varibles belonging to the pre- and postcondition of the selected cell (if any) and highlights them.
	 */
	protected void initSelectedVariablesView() {
		if (gui.getWorkScreen().getGraph().getSelectedCell()!=null) {
			selectedVariablesView.removeAll();
			
			int id = gui.getWorkScreen().getGraph().getSelectedCell().getID();
			VerificationFormula vf = gui.getModel().getVerificationFormula(id);
			Variable[] variables = gui.getVariables(vf);
			if (variables[0]!=null) {
				VariableView selectedPreVariableView = createVariableView(variables[0]);
				selectedPreVariableView.highlightPre();
				selectedVariablesView.add(selectedPreVariableView);
			}
			if (variables[1]!=null) {
				VariableView selectedPostVariableView = createVariableView(variables[1]);
				selectedPostVariableView.highlightPost();
				selectedVariablesView.add(selectedPostVariableView);
			}
		}
		else {
			selectedVariablesView.removeAll();
		}
		selectedPanel.setFont(gui.getMainFont());
	}
	
	/**
	 * Unhighlights all variables.
	 * 
	 */
	protected void unhighlightAllVariables() {
		for (VariableView v : variableViews.values()) {
			v.unhighlight();
		}
	}
	
	/**
	 * Highlights the variables of the selected cell.
	 *
	 */
	protected void highlightSelectedVariables() {
		unhighlightAllVariables();
		MyCell selectedCell = gui.getWorkScreen().getGraph().getSelectedCell();
		if (selectedCell!=null) {
			VerificationFormula vf = gui.getModel().getVerificationFormula(selectedCell.getID());
			Variable[] variables = gui.getVariables(vf);
			if (!(variables[0]==null)) {
				variableViews.get(variables[0].getName()).highlightPre();
			}
			if (!(variables[1]==null)) {
				variableViews.get(variables[1].getName()).highlightPost();
			}
		}
	}
	
	/**
	 * Sets the font to the global font of the gui.
	 *
	 */
	public void updateFont() {
		title.setFont(gui.getMainFont().deriveFont(Font.BOLD));
		initVariablesView();
		validate();
		repaint();
	}
	
}