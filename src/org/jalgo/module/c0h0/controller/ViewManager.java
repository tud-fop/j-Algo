package org.jalgo.module.c0h0.controller;

import java.awt.Image;

import org.jalgo.module.c0h0.gui.ModuleContainer;
import org.jalgo.module.c0h0.views.*;

/**
 * manages the views
 * 
 * @author Hendrik Sollich, Peter Schwede
 */
public class ViewManager implements InterfaceConstants {

	private EditView editView;
	private HelpView helpView;
	private C0View c0View;
	private FlowChartView fcView;
	public H0View h0View; // TODO dirty durchreichen - move performer
	private LogView logView;
	private TransView transView;
	private WelcomeView welcomeView;

	private ModuleContainer container;

	private State editHelp, welcome, c0Flow, c0H0, flowH0;

	private State state;
	private View focusedView;
	private boolean beamerMode = false;

	/**
	 * @param controller
	 * @param container
	 */
	public ViewManager(Controller controller, ModuleContainer container) {
		this.container = container;

		editView = new EditView(controller, this);
		helpView = new HelpView(controller, this);
		c0View = new C0View(controller, this);
		h0View = new H0View(controller, this);
		fcView = new FlowChartView(controller, this);
		logView = new LogView(controller, this);
		welcomeView = new WelcomeView(controller, this);
		transView = new TransView(controller, this);
		transView.registerToView(h0View); // foo-foo-foo-foo-foo, need CORRECT
		// observer in viewManager
		
		editHelp = new State(editView, helpView, logView, ViewState.EDIT);
		welcome = new State(editView, helpView, logView, ViewState.WELCOME);
		c0Flow = new State(c0View, fcView, transView, ViewState.C0FLOW);
		c0H0 = new State(c0View, h0View, transView, ViewState.C0H0);
		flowH0 = new State(fcView, h0View, transView, ViewState.FLOWH0);

		// First View to show is a Single WelcomeView
		container.display(welcomeView);

		// First State is editHelp
		state = welcome;

		// FocusedView is:
		focusedView = helpView;
	}

	/**
	 * Sets the views according to a short, describing String
	 * 
	 * @param newState
	 *            One out of "c0", "fc" or other
	 */
	public void setState(ViewState newState) {
		switch (newState) {
		case C0FLOW:
		case RUN:
			state = c0Flow;
			TerminalView.println("c0Flow");
			break;
		case FLOWH0:
			state = flowH0;
			TerminalView.println("flowH0");
			break;
		case C0H0:
			state = c0H0;
			TerminalView.println("c0H0");
			break;
		case EDIT:
		default:
			state = editHelp;
		}
		state.update();
		setViews();
	}

	/**
	 * returns the current state of the containers
	 * 
	 * @return state
	 */
	public ViewState getState() {
		return state.getName();
	}

	/**
	 * @return ModuleContainer
	 */
	public ModuleContainer getContainer() {
		return this.container;
	}

	/**
	 * calls update method on all Views of the current state
	 */
	public void updateViews() {
		state.update();

		// redraw container
		container.updateUI();
	}

	/**
	 * Renders all Views
	 */
	public void renderAllViews() {
		editView.render();
		helpView.render();
		c0View.render();
		fcView.render();
		h0View.render();
		logView.render();
		transView.render();
		welcomeView.render();
	}

	/**
	 * calls render method on all Views of the current state
	 */
	public void renderViews() {
		state.render();

		// redraw container
		container.updateUI();
	}

	/**
	 * sets the views into the containers
	 */
	public void setViews() {
		container.leftPane.removeAll();
		container.leftPane.add(state.getLeftView());

		container.rightPane.removeAll();
		container.rightPane.add(state.getRightView());

		container.bottomPane.removeAll();
		container.bottomPane.add(state.getBottomView());

		// make width of containers equal 
		container.getLeftright().setDividerLocation(0.5);
		container.display();

	}

	/**
	 * sets the beamer mode
	 * 
	 * @param beamerMode
	 */
	public void setBeamerMode(boolean beamerMode) {
		this.beamerMode = beamerMode;
		updateViews();
	}

	/**
	 * returns boolean if beamer mode is set
	 * 
	 * @return beamerMode
	 */
	public boolean isBeamerMode() {
		return beamerMode;
	}

	/**
	 * sets a view focused
	 * 
	 * @param fv
	 *            the focused view
	 */
	public void setFocusedView(View fv) {
		focusedView = fv;
		focusedView.requestFocusInWindow();
	}

	/**
	 * returns the focused view
	 * 
	 * @return focusedView
	 */
	public View getFocusedView() {
		return focusedView;
	}

	/**
	 * returns the c0 code from the editor
	 * 
	 * @return c0 code
	 */
	public String getC0Code() {
		/**    *            *     *               *
		 *  *  Greetings from White Rabbit!            *
		 *       *      *    *               */
		if (editView.getC0Code().contains("wörmhoulruhding")) {
			container.display(new Easter());
		}
		return editView.getC0Code();
	}

	/**
	 * gives an error message to the log view
	 * 
	 * @param error
	 */
	public void updateErrorInformation(String error) {
		logView.setErrorText(error);
	}

	/**
	 * set caret into the edit view
	 * 
	 * @param line
	 *            number
	 */
	public void focusCaret(int line) {
		editView.requestFocus();
		editView.setCaret(line);
	}

	/**
	 * Returns an image of the flowchart
	 * 
	 * @return Image
	 */
	public Image getFlowChartImage() {
		return fcView.getGraph();
	}

	/**
	 * Returns the currently set background color
	 * 
	 * @param active
	 * @return String(Hex)
	 */
	public String backgroundColor(boolean active) {
		if (active)
			return "#ffffff";
		return "#dddddd";
	}

}
