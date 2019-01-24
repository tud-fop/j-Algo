package org.jalgo.module.app.core.step;

import org.jalgo.module.app.core.Matrix;

/**
 * Represents a Step in the calculation during the Aho-algorithm. A step can
 * access the matrix before the calculation step and after the calculation step.
 * It is also connected to its parent step. Therefore it is possible to navigate
 * between the steps.
 */
public abstract class Step {

	protected GroupStep parent;

	/**
	 * Gets the parent <code>GroupStep</code>.
	 * 
	 * @return parent <code>GroupStep</code>
	 */
	public GroupStep getGroupStep() {
		return parent;
	}

	/**
	 * Sets the parent <code>GroupStep</code>.
	 */
	public void setGroupStep(GroupStep aStep) {
		parent = aStep;
	}

	/**
	 * Returns the calculation matrix before performing the step.
	 * 
	 * @return the matrix before the step.
	 */
	public Matrix getBeforeMatrix() {
		if (parent != null)
			return parent.getMatrixBeforeStep(this);
		else
			return null;
	}

	/**
	 * Returns the calculation matrix after performing the step.
	 * 
	 * @return the matrix after the step.
	 */
	public abstract Matrix getAfterMatrix();
}
