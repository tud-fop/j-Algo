package org.jalgo.module.app.controller;

import java.awt.BorderLayout;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import org.jalgo.module.app.core.SemiRing;
import org.jalgo.module.app.core.dataType.Operation;
import org.jalgo.module.app.core.dataType.Operation.Notation;
import org.jalgo.module.app.core.step.AtomicStep;
import org.jalgo.module.app.core.step.GroupStep;
import org.jalgo.module.app.core.step.RootStep;
import org.jalgo.module.app.core.step.Step;
import org.jalgo.module.app.view.run.ControlToolbar;
import org.jalgo.module.app.view.run.FormulaComponent;
import org.jalgo.module.app.view.run.FormulaWindow;
import org.jalgo.module.app.view.run.MatrixComponent;
import org.jalgo.module.app.view.run.StepHighlighting;

/**
 * Controls the two Panels, which are in the right panel of the User Interface.
 * The <code>AlgorithmController</code> initializes the three components,
 * which are involved in these panels.
 */
public class AlgorithmController {

	// Connection to the MainController to communicate with the Core and the
	// InterfaceController.
	private MainController mainCtrl;

	private AnimationTimerTask timerTask;
	private Timer animationTimer;

	// Components, which are controlled by this Controller.
	private ControlToolbar controlToolbar;
	private MatrixComponent matrixComponent;
	private FormulaComponent formulaComponent;
	private FormulaWindow formulaWindow;

	// to remember the currentStep
	private RootStep rootStep;
	private Step currentStep;

	private Set<StepHighlighting> highlightedViews;

	// The Panels, which are initialized by the InterfaceController.
	private JPanel algorithmPanel, algorithmToolPanel;

	/**
	 * Instantiates the two Panels, which are in the right panel of the User
	 * Interface (in <code>InterfaceMode.ALGORITM_MODE</code>). He also
	 * initializes the three components involved in the panels.
	 */
	public AlgorithmController(MainController mainController,
							   JPanel algorithmPanel, 
							   JPanel algorithmToolPanel
							  ) 
	{
		BorderLayout layout;

		mainCtrl = mainController;

		this.algorithmPanel = algorithmPanel;
		this.algorithmToolPanel = algorithmToolPanel;

		highlightedViews = new HashSet<StepHighlighting>();

		controlToolbar = new ControlToolbar(this);
		matrixComponent = new MatrixComponent(this);
		formulaComponent = new FormulaComponent(this);

		layout = new BorderLayout();
		algorithmPanel.setLayout(layout);
		algorithmPanel.add(matrixComponent, BorderLayout.CENTER);
		algorithmPanel.add(formulaComponent, BorderLayout.SOUTH);
		algorithmToolPanel.add(controlToolbar);
	}

	private void updateToolbarButtons(boolean hasPrev, boolean hasNext) {
		controlToolbar.setButtonState(
				ControlToolbar.ControlButtons.AnimateButton, hasNext);

		controlToolbar.setButtonState(
				ControlToolbar.ControlButtons.FastForwardButton, hasNext);
		controlToolbar.setButtonState(
				ControlToolbar.ControlButtons.ForwardButton, hasNext);
		controlToolbar.setButtonState(
				ControlToolbar.ControlButtons.BackwardButton, hasPrev);
		controlToolbar.setButtonState(
				ControlToolbar.ControlButtons.FastBackwardButton, hasPrev);

		if (!hasNext)
			pauseAnimation();
	}

	/**
	 * Changes the look of the components owned by this controller based upon
	 * the <code>InterfaceMode</code>. If interfaceMode is set to
	 * <code>InterfaceMode.ALGORITHM_DISPLAY</code>, the calculation will be
	 * started and the components will be made visible.
	 * 
	 * @param interfaceMode
	 *            the current mode of the Interface
	 */
	public void updateDisplay(InterfaceMode interfaceMode) {
		SemiRing semiring;
		Operation plusOperation, dotOperation;

		if (interfaceMode == InterfaceMode.ALGORITHM_DISPLAY) {
			rootStep = mainCtrl.getCalculation().calculateRootStep();

			currentStep = rootStep;

			semiring = mainCtrl.getCalculation().getSemiring();
			plusOperation = semiring.getPlusOperation();
			dotOperation = semiring.getDotOperation();

			matrixComponent.setVisible(true);
			formulaComponent.setOperators(plusOperation
					.getSymbolicRepresentation(),
					(plusOperation.getNotation() == Notation.INFIX),
					dotOperation.getSymbolicRepresentation(), (dotOperation
							.getNotation() == Notation.INFIX));
			notifyViewsEnterHighlightMode();
			notifyViewsFirstStep();
		} else {
			pauseAnimation();
			matrixComponent.setVisible(false);

			notifyViewsLeaveHighlightMode();
			setToolbarEnabled(false);
		}
	}

	/**
	 * Opens up the popUp formula window, which displays the formula in a bigger
	 * font.
	 */
	public void initFormulaWindow() {
		if (mainCtrl.getInterfaceController().getDisplayMode() != InterfaceMode.ALGORITHM_DISPLAY
				|| (formulaWindow != null && formulaWindow.isDisplayable())) {
			return;
		}
		SemiRing semiring;
		Operation plusOperation, dotOperation;

		semiring = mainCtrl.getCalculation().getSemiring();
		plusOperation = semiring.getPlusOperation();
		dotOperation = semiring.getDotOperation();

		int x, y;
		x = formulaComponent.getLocationOnScreen().x;
		y = formulaComponent.getLocationOnScreen().y
				+ formulaComponent.getHeight();

		formulaWindow = new FormulaWindow(this,
										  formulaComponent.getGraphicsConfiguration(),
										  new Point(x, y),
										  mainCtrl.getInterfaceController().isBeamerLayout());
		formulaWindow.setOperators(plusOperation.getSymbolicRepresentation(),
				(plusOperation.getNotation() == Notation.INFIX), dotOperation
						.getSymbolicRepresentation(), (dotOperation
						.getNotation() == Notation.INFIX));
		formulaWindow.setVisible(true);

		if (currentStep instanceof RootStep)
			formulaWindow.highlightGroupStep(
					(GroupStep) ((GroupStep) currentStep).getStep(0), false);
		else if (currentStep instanceof GroupStep)
			formulaWindow.highlightGroupStep((GroupStep) currentStep, false);
		else if (currentStep instanceof AtomicStep)
			formulaWindow.highlightAtomicStep((AtomicStep) currentStep, false);
	}

	/**
	 * Notification if formula window was closed.
	 */
	public void formulaWindowClosed() {
		removeStepHighlighting(formulaWindow);
		formulaWindow = null;
	}
	
	/**
	 * Closes the formula window.
	 */
	public void closeFormulaWindow() {
		
		if (formulaWindow != null)		
			formulaWindow.dispose();
		
		formulaWindowClosed();
	}
	
	
	/**
	 * Enables the controlToolbar so that it is visible in the User Interface.
	 * 
	 * @param state
	 *            <code>true</code> to enable toolbar, <code>false</code> to
	 *            disable it.
	 */
	public void setToolbarEnabled(boolean state) {
		controlToolbar.setEnabled(state);
	}

	/**
	 * Turns the beamer mode on or off.
	 * 
	 * @param beamerMode A boolean that says, whether the beamer mode should turn on or off.
	 */
	public void setBeamerMode(boolean beamerMode) {
		matrixComponent.updateBeamerMode(beamerMode);
		formulaComponent.updateBeamerMode(beamerMode);
		if (formulaWindow != null)
			formulaWindow.updateBeamerMode(beamerMode);
	}
	
	/**
	 * Adds a new highlighting element to the set of highlighting elements.
	 * 
	 * @param highlighting
	 */
	public void addStepHighlighting(StepHighlighting highlighting) {
		highlightedViews.add(highlighting);
	}
	
	/**
	 * Removes a new highlighting element to the set of highlighting elements.
	 * 
	 * @param highlighting
	 */
	public void removeStepHighlighting(StepHighlighting highlighting) {
		highlightedViews.remove(highlighting);
	}

	private void notifyViewsEnterHighlightMode() {
		for (StepHighlighting o : highlightedViews) {
			o.enterHighlightMode(rootStep);
		}

		updateToolbarButtons(false, true);
	}

	private void notifyViewsLeaveHighlightMode() {
		for (StepHighlighting o : highlightedViews) {
			o.leaveHighlightMode();
		}

		updateToolbarButtons(false, true);
	}

	private void notifyViewsFirstStep() {
		for (StepHighlighting o : highlightedViews) {
			o.highlightFirstStep((RootStep) currentStep);
		}

		updateToolbarButtons(false, true);
	}

	private void notifyViewsLastStep() {
		for (StepHighlighting o : highlightedViews) {
			o.highlightLastStep((RootStep) currentStep);
		}

		updateToolbarButtons(true, false);
	}

	private void notifyViewsAtomicStep(boolean isForward) {
		for (StepHighlighting o : highlightedViews) {
			o.highlightAtomicStep((AtomicStep) currentStep, isForward);
		}

		updateToolbarButtons(true, true);
	}

	private void notifyViewsGroupStep(boolean isForward) {
		for (StepHighlighting o : highlightedViews) {
			o.highlightGroupStep((GroupStep) currentStep, isForward);
		}

		updateToolbarButtons(true, true);
	}

	/**
	 * Gets the event "next Step" of the Components, which are controlled by
	 * this Controller, and send it up to the MainController, which can transfer
	 * it to the Core.
	 */
	public void nextStep() {
		Step nextStep;

		// Start
		if (currentStep == rootStep) {
			currentStep = ((GroupStep) rootStep.getStep(0)).getStep(0);
			notifyViewsAtomicStep(true);

			return;
		}

		// Entering group step
		if (currentStep.getGroupStep() == rootStep) {
			currentStep = ((GroupStep) currentStep).getStep(0);
			notifyViewsAtomicStep(true);

			return;
		}

		nextStep = currentStep.getGroupStep().getStepAfterStep(currentStep);

		// Reached the end
		if (nextStep == null) {
			currentStep = rootStep;
			notifyViewsLastStep();
			return;
		}

		// Changing to the next group step
		if (nextStep instanceof GroupStep) {
			currentStep = nextStep;
			notifyViewsGroupStep(true);
			return;
		}

		// Changing atomic step
		currentStep = nextStep;
		notifyViewsAtomicStep(true);
	}

	/**
	 * Gets the event "previous Step" of the Components, which are controlled by
	 * this Controller, and send it up to the MainController, which can transfer
	 * it to the Core.
	 */
	public void previousStep() {
		Step prevStep;

		// End
		if (currentStep == rootStep) {
			int rootStepCount;
			int lastCount;
			GroupStep lastGroupStep;

			rootStepCount = rootStep.getStepCount();
			lastGroupStep = ((GroupStep) rootStep.getStep(rootStepCount - 1));
			lastCount = lastGroupStep.getStepCount();

			currentStep = lastGroupStep.getStep(lastCount - 1);
			notifyViewsAtomicStep(false);

			return;
		}

		// Entering (previous) group step
		if (currentStep.getGroupStep() == rootStep) {
			GroupStep currentGroupStep;

			currentGroupStep = (GroupStep) currentStep;
			currentGroupStep = (GroupStep) currentStep.getGroupStep()
					.getStepBeforeStep(currentGroupStep);

			// Reached the start
			if (currentGroupStep == null) {
				currentStep = rootStep;
				notifyViewsFirstStep();
				return;
			}

			currentStep = currentGroupStep.getStep(currentGroupStep
					.getStepCount() - 1);
			notifyViewsAtomicStep(false);

			return;
		}

		prevStep = currentStep.getGroupStep().getStepBeforeStep(currentStep);

		// Reached the start
		if (prevStep == null) {
			currentStep = rootStep;
			notifyViewsFirstStep();
			return;
		}

		// Changing to the previous group step
		if (prevStep instanceof GroupStep) {
			currentStep = currentStep.getGroupStep();
			notifyViewsGroupStep(false);
			return;
		}

		// Changing atomic step
		currentStep = prevStep;
		notifyViewsAtomicStep(false);
	}

	/**
	 * Gets the event "next GroupStep" from the Components, which are controlled
	 * by this Controller, and send it up to the MainController, which can
	 * transfer it to the Core.
	 */
	public void nextGroupStep() {
		// At the beginning
		if (currentStep == rootStep) {
			currentStep = rootStep.getStep(0);
			notifyViewsGroupStep(true);
			return;
		}

		// Inside a step, just select the parent
		if (currentStep.getGroupStep() != rootStep) {
			currentStep = currentStep.getGroupStep();
		}

		currentStep = currentStep.getGroupStep().getStepAfterStep(currentStep);

		// Reached the end
		if (currentStep == null) {
			currentStep = rootStep;
			notifyViewsLastStep();
			return;
		}

		notifyViewsGroupStep(true);
	}

	/**
	 * Gets the event "previous GroupStep" of the Components, which are
	 * controlled by this Controller, and send it up to the MainController,
	 * which can transfer it to the Core.
	 */
	public void previousGroupStep() {
		// At the end
		if (currentStep == rootStep) {
			currentStep = rootStep.getStep(rootStep.getStepCount() - 1);
			notifyViewsGroupStep(false);
			return;
		}

		// Inside a step, just select the parent
		if (currentStep.getGroupStep() != rootStep) {
			currentStep = currentStep.getGroupStep();
			notifyViewsGroupStep(false);
			return;
		}

		currentStep = currentStep.getGroupStep().getStepBeforeStep(currentStep);

		// Reached the start
		if (currentStep == null) {
			currentStep = rootStep;
			notifyViewsFirstStep();
			return;
		}

		notifyViewsGroupStep(false);
	}

	/**
	 * Starts the animation, which goes through the Atomic Steps. Only the timer
	 * and the scheduler are set here.
	 */
	public void startAnimation() {
		animationTimer = new Timer();
		timerTask = new AnimationTimerTask();

		animationTimer.schedule(timerTask, 0, 1000);

		controlToolbar.toggleAnimationState(false);
	}

	/**
	 * Pauses the animation. The timer is cancelled.
	 */
	public void pauseAnimation() {
		if (animationTimer == null)
			return;

		animationTimer.cancel();
		animationTimer.purge();

		controlToolbar.toggleAnimationState(true);
	}

	private class AnimationTimerTask extends TimerTask {

		@Override
		public void run() {
			nextStep();
		}

	}

}
