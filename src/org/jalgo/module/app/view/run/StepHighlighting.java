package org.jalgo.module.app.view.run;

import org.jalgo.module.app.core.step.AtomicStep;
import org.jalgo.module.app.core.step.GroupStep;
import org.jalgo.module.app.core.step.RootStep;

/**
 * An interface used for notifying the matrices and the formula (in the
 * algorithm panel) upon changing the <code>Step<code>. 
 *
 */
public interface StepHighlighting {
	
	/**
	 * Notifies components about entering the algorithm view
	 * 
	 *  @param rootStep
	 *  		the root step of the calculation process
	 */
	public void enterHighlightMode(RootStep rootStep);

	/**
	 * Notifies components about leaving the view 
	 */
	public void leaveHighlightMode();
	
	/**
	 * Notifies components (step observers) upon the first step.
	 * 
	 * @param step
	 *            the current <code>GroupStep</code>.
	 */
	public void highlightFirstStep(RootStep step);
	
	/**
	 * Notifies components (step observers) upon the last step.
	 * 
	 * @param step
	 *            the current <code>GroupStep</code>.
	 */
	public void highlightLastStep(RootStep step);	
	
	/**
	 * Notifies components (step observers) upon group step change.
	 * 
	 * @param step
	 *            the current <code>GroupStep</code>.
	 *        isForward
	 *        	  the next step was selected
	 */
	public void highlightGroupStep(GroupStep step, boolean isForward);

	/**
	 * Notifies components (step observers) upon atomic step change.
	 * 
	 * @param step
	 *            the current <code>AtomicStep</code>.
	 *        isForward
	 *        	  the next step was selected
	 */
	public void highlightAtomicStep(AtomicStep step, boolean isForward);
}
