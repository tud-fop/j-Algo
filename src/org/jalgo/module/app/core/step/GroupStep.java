package org.jalgo.module.app.core.step;

import java.util.ArrayList;
import java.util.List;

import org.jalgo.module.app.core.Matrix;

/**
 * Represents a big step, where all elements in a matrix are processed once,
 * i.e. there are n^2 <code>AtomicStep</code>s between two
 * <code>GroupStep</code>s (where n is the number of rows/columns of the
 * matrix). A GroupStep holds all the <code>AtomicStep</code>s up to the next
 * <code>GroupStep</code>.
 * 
 */
public class GroupStep extends Step {

	protected List<Step> steps;
	private Matrix afterMatrix;

	public GroupStep() {
		steps = new ArrayList<Step>();
	}

	@Override
	public Matrix getAfterMatrix() {
		return afterMatrix;
	}

	/**
	 * Gets the matrix before the <code>child</code> <code>Step</code>.
	 * 
	 * @param child
	 *            The matrix to get the previous step to.
	 * @return the <code>Matrix</code> before the Step child.
	 */
	public Matrix getMatrixBeforeStep(Step child) {
		Matrix matrix;
		int index;

		index = steps.indexOf(child);
		if (index == 0)
			return parent.getMatrixBeforeStep(this);
		else {
			if (!(child instanceof AtomicStep))
				return getStepBeforeStep(child).getAfterMatrix();

			// Flat implementation, line above would work as well
			matrix = parent.getMatrixBeforeStep(this);
			matrix = matrix.clone();

			for (Step s : steps) {
				((AtomicStep) s).injectChange(matrix);

				if (s == child)
					break;
			}

			return matrix;
		}
	}

	/**
	 * Gets the <code>Step</code> before the
	 * <code>child</code> <code>Step</code>.
	 * 
	 * @param child
	 *            the step to get the previous step to.
	 * @return the step before the child step
	 */
	public Step getStepBeforeStep(Step child) {
		int index;

		index = steps.indexOf(child);
		if (index == 0) {
			if (parent != null)
				return parent.getStepBeforeStep(this);
			else
				return null;
		} else {
			return steps.get(index - 1);
		}
	}

	/**
	 * Gets the <code>Step</code> after the
	 * <code>child</code> <code>Step</code>.
	 * 
	 * @param child
	 *            the step to get the following step to.
	 * @return the step after the child step
	 */
	public Step getStepAfterStep(Step child) {
		int index;

		index = steps.indexOf(child);
		if (index == steps.size() - 1) {
			if (parent != null)
				return parent.getStepAfterStep(this);
			else
				return null;
		} else {
			return steps.get(index + 1);
		}
	}

	/**
	 * Gets a list of all the steps in this <code>GroupStep</code>.
	 * 
	 * @return a list of all the steps in this <code>GroupStep</code>.
	 */
	public List<Step> getSteps() {
		return steps;
	}

	/**
	 * Sets the steps in this <code>GroupStep</code>.
	 * 
	 * @param steps
	 */
	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

	/**
	 * Gets the step at the position index.
	 * 
	 * @param index
	 *            the position of the step
	 * @return the step at the position index
	 */
	public Step getStep(int index) {
		return steps.get(index);
	}

	/**
	 * Returns the numbers of steps in this <code>GroupStep</code>.
	 * 
	 * @return the numbers of steps in this <code>GroupStep</code>.
	 */
	public int getStepCount() {
		return steps.size();
	}

	/**
	 * Adds a step to this <code>GroupStep</code>.
	 * 
	 * @param step
	 *            the step to be added.
	 */
	public void addStep(Step step) {
		this.steps.add(step);
		step.setGroupStep(this);
	}

	public void setAfterMatrix(Matrix afterMatrix) {
		this.afterMatrix = afterMatrix;
	}
}