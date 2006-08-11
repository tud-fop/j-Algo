package org.jalgo.module.ebnf.gui.trans;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.ModuleConnector;
import org.jalgo.module.ebnf.controller.trans.TransController;
import org.jalgo.module.ebnf.gui.trans.event.PerformAction;
import org.jalgo.module.ebnf.gui.trans.event.PerformAllAction;
import org.jalgo.module.ebnf.gui.trans.event.ShowNextStepAction;
import org.jalgo.module.ebnf.gui.trans.event.UndoAction;
import org.jalgo.module.ebnf.gui.trans.event.UndoAllAction;
import org.jalgo.module.ebnf.model.ebnf.Term;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.trans.TransMap;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import org.jalgo.module.ebnf.renderer.elements.RenderTrans;

/**
 * @author Andre Viergutz
 */
public class GUIController {

	private TransController transcontroller;

	private ModuleConnector connector;

	private JPanel contentPane;

	private DrawPanel drawPanel;

	private ExplanationPanel explanationPanel;

	private ControlPanel controlPanel;

	private JScrollPane drawScrollPane;

	private JPanel workPanel;

	private JButton showNextStepButton;

	private AbstractAction showNextStepAction;

	private AbstractAction performAction;

	private AbstractAction performAllAction;

	private AbstractAction undoAction;

	private AbstractAction undoAllAction;

	private boolean explanationShown = false;

	/**
	 * This constructor initializes alle the GuiComponents
	 * 
	 * @param controller The TransController which created this
	 * @param connector The ModuleConnector from jAlgo
	 */

	public GUIController(TransController controller, ModuleConnector connector) {

		this.transcontroller = controller;
		this.connector = connector;
		this.contentPane = controller.getContentPane();
		this.contentPane.addComponentListener(new ComponentAdapter() {

			public void componentResized(ComponentEvent e) {

				drawScrollPane.setPreferredSize(new Dimension(Short.MAX_VALUE,
						contentPane.getHeight()
								- controlPanel.getPreferredSize().height
								- explanationPanel.getPreferredSize().height));

				explanationPanel.revalidate();

			}

		});

		// this.installToolbar();
		this.init();

	}

	/**
	 * This method is called from the constructor to layout the GUI
	 */
	public void init() {

		// init

		workPanel = new JPanel();
		explanationPanel = new ExplanationPanel();
		drawPanel = new DrawPanel(transcontroller.getSynDiaSystem(),
				transcontroller.getTransMap(), this);
		drawScrollPane = new JScrollPane();
		controlPanel = new ControlPanel(this);

		// setting
		this.contentPane.setBackground(Color.WHITE);
		drawScrollPane.setViewportBorder(javax.swing.BorderFactory
				.createLoweredBevelBorder());
		drawScrollPane.setViewportView(drawPanel);
		drawScrollPane.setPreferredSize(new Dimension(Short.MAX_VALUE,
				contentPane.getHeight()
						- controlPanel.getPreferredSize().height - 170));
		drawScrollPane.setMaximumSize(new Dimension(Short.MAX_VALUE, 200));

		drawPanel.addComponentListener(new ComponentAdapter() {

			public void componentMoved(ComponentEvent e) {

				drawPanel.repaint();

			}

		});

		controlPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, controlPanel
				.getPreferredSize().height));

		workPanel.setLayout(new BoxLayout(workPanel, BoxLayout.Y_AXIS));
		workPanel.setBackground(Color.WHITE);
		workPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				Messages.getString("ebnf", "Border_Draw"),
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 0, 12)));
		workPanel.add(drawScrollPane);
		workPanel.add(explanationPanel);

		switchToAlgorithm();

	}

	/**
	 * Switches to the word algorithm
	 */
	public void switchToAlgorithm() {

		// layout
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));// layout);

		contentPane.add(controlPanel);
		contentPane.add(workPanel);

		contentPane.validate();
		contentPane.repaint();

		connector.setSaveStatus(SaveStatus.NOTHING_TO_SAVE);
		installToolbar();

	}

	/**
	 * Switches to the syntax diagram display
	 */
	public void switchToSynDiaDisplay() {

		
		transcontroller.switchToSynDiaDisplay();

	}

	/**
	 * Switches back to the ebnf-display
	 */
	public void setEbnfInputMode() {

		
		transcontroller.setEbnfInputMode();

	}

	/**
	 * Sets up the toolbar.
	 */
	private void installToolbar() {
		JToolBar toolBar = JAlgoGUIConnector.getInstance().getModuleToolbar(
				connector);

		toolBar.addSeparator();

		undoAllAction = new UndoAllAction(this);
		toolBar.add(createToolbarButton(undoAllAction));
		undoAction = new UndoAction(this);
		toolBar.add(createToolbarButton(undoAction));

		showNextStepAction = new ShowNextStepAction(this, transcontroller);
		showNextStepButton = createToolbarButton(showNextStepAction);
		
		toolBar.add(showNextStepButton);

		performAction = new PerformAction(this);
		toolBar.add(createToolbarButton(performAction));
		performAllAction = new PerformAllAction(this);
		toolBar.add(createToolbarButton(performAllAction));

		toolBar.validate();
		updateToolbar();

	}

	/**
	 * Creates a <code>JButton</code> object without border and text, which
	 * can be used in <code>JToolBar</code>s
	 * 
	 * @param a an Action associated to the created button
	 * @return a <code>JButton</code> instance with the given
	 *         <code>Action</code>
	 */
	public JButton createToolbarButton(Action a) {
		JToolbarButton button = new JToolbarButton((Icon) a
				.getValue(Action.SMALL_ICON), null, null);
		button.setAction(a);
		button.setText("");
		return button;
	}

	/**
	 * Asks the TransController, if there is at least one step to perform
	 * 
	 * @return true, if there are one or more steps to perform
	 */
	public boolean hasNextStep() {

		if (transcontroller.hasNextStep()) {
			this.updateToolbar();
			return true;
		} else
			return false;

	}

	/**
	 * Tells the TransController to perform the next step chosen by the user
	 * 
	 * @param re the RenderElement the user clicked on
	 * @return True, if it was possible to perform the chosen step
	 */
	public boolean performChosenStep(RenderElement re) {

		SynDiaElem se = this.getSynDiaElemFromRenderElem(re);

		if (transcontroller.performChosenStep(se)) {
			this.updateToolbar();
			return true;
		} else
			return false;
	}

	/**
	 * Tells the TransController to perform the next possible step.
	 * 
	 * @return True, if it was possible to perform a step.
	 */
	public boolean performNextStep() {

		if (transcontroller.performNextStep()) {
			this.updateToolbar();
			return true;
		} else
			return false;

	}

	/**
	 * Performs all possible steps.
	 * 
	 * @return True, if it was possible to perform a step.
	 */
	public boolean performAllSteps() {

		if (transcontroller.performAllSteps()) {
			this.updateToolbar();
			return true;
		} else
			return false;

	}

	/**
	 * Tells the TransController to undo the last executed step.
	 * 
	 * @return True
	 */
	public boolean undoStep() {

		if (transcontroller.undoStep()) {
			this.updateToolbar();
			return true;
		} else
			return false;
	}

	/**
	 * Tells the TransController to undo the last executed step.
	 * 
	 * @return True
	 */
	public boolean undoAllSteps() {

		transcontroller.undoAllSteps();
		this.updateToolbar();
		return true;
	}

	/**
	 * Looks for the SynDiaElem which is associated to the given RenderElemnt
	 * 
	 * @param re A RenderElement
	 * @return The associated SynDiaElem or null, if it was not found
	 */
	public SynDiaElem getSynDiaElemFromRenderElem(RenderElement re) {

		try {

			return drawPanel.getSdeFromRe(re);

		} catch (RenderElemNotFoundException e) {

		}

		return null;
	}

	/**
	 * Looks for the SynDiaElem which is associated to the given RenderElemnt
	 * 
	 * @param sde A SynDiaElem
	 * @return The associated SynDiaElem or null, if it was not found
	 */
	public RenderElement getRenderElemFromSynDiaElem(SynDiaElem sde) {

		try {

			return drawPanel.getReFromSde(sde);

		} catch (SynDiaElemNotFoundException e) {

		}

		return null;
	}

	/**
	 * Looks and returns the next trans-element to render
	 * 
	 * @return a RenderTrans
	 */
	public RenderTrans getNextRenderTrans() {

		SynDiaElem sde = transcontroller.getSdeFromNextStep();
		return (RenderTrans) getRenderElemFromSynDiaElem(sde);

	}

	/**
	 * Displays the explanation for the given Term
	 * 
	 * @param t a Term of the ebnf-<code>Definition</code>
	 */
	public void showExplanation(Term t) {

		explanationPanel.showExplanation(t, !this.hasNextStep());

	}

	/**
	 * Looks for and displays the next found explanation
	 */
	public void showNextExplanation() {

		Term t = transcontroller.getTermFromNextStep();
		explanationPanel.showExplanation(t, !this.hasNextStep());

	}

	/**
	 * Updates the visibilitiy of the toolbar buttons
	 */
	public void updateToolbar() {
		
		TransMap tm = this.transcontroller.getTransMap();
		undoAllAction.setEnabled(!tm.isEbnf());
		undoAction.setEnabled(!tm.isEbnf());
		showNextStepAction.setEnabled(!tm.isTransformed());
		
		if (explanationShown) {
			showNextStepButton.setIcon(new ImageIcon(Messages.getResourceURL(
					"main", "Icon.Advice_Selected")));
		} else {
			showNextStepButton.setIcon(new ImageIcon(Messages.getResourceURL(
					"main", "Icon.Advice")));
		}
		performAction.setEnabled(!tm.isTransformed());
		performAllAction.setEnabled(!tm.isTransformed());
		

	}

	/**
	 * Updates the DrawPanel (where the SyntaxDiagrams are located).
	 * 
	 * @param size The size of the Font associated to the SyntaxDiagram. Use
	 *            size 0 if you don't want to change the size
	 */
	public void updateDrawPanel(int size) {

		drawPanel.resizeSystem(size);
		this.validateComponents();

	}

	/**
	 * This method validates all necessary components after a resize
	 */
	public void validateComponents() {

		this.drawScrollPane.validate();

	}

	/**
	 * This method tells the guiController whether the DrawPanel should fit to
	 * size automatically or not
	 * 
	 * @param autosize True, if auto size shall be activated
	 */
	public void setAutoSize(boolean autosize) {

		drawPanel.setAutoSize(autosize);

	}

	/**
	 * @return Returns the controlPanel.
	 */
	public ControlPanel getControlPanel() {
		return controlPanel;
	}

	/**
	 * @return Returns the drawPanel.
	 */
	public DrawPanel getDrawPanel() {
		return drawPanel;
	}

	/**
	 * This method sets the GUI, so that the next explanation is shown
	 * automatically.
	 * 
	 * @param shown true, if the next explanation shall be shown automatically
	 */
	public void setExplanationShow(boolean shown) {

		if (this.hasNextStep()) {
			if (shown) {

				this.showNextExplanation();
				RenderTrans te = this.getNextRenderTrans();
				te.changeColor(true);
				te.showSeperated(true);

			} else {

				this.showExplanation(null);
				RenderTrans te = this.getNextRenderTrans();
				te.changeColor(false);
				te.showSeperated(false);

			}
		}

		this.explanationShown = shown;
	}

	/**
	 * This method tells the next explanation is shown automatically or not
	 * 
	 * @return true, if shown automatically
	 */
	public boolean isExplanationShown() {

		return this.explanationShown;
	}
}
