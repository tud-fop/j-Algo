package org.jalgo.module.app.core.step;

import org.jalgo.module.app.core.Matrix;

/**
 * Represents the very first step of the whole Calculation. The matrix before
 * this step is the adjacency matrix.
 * 
 */
public class RootStep extends GroupStep {

	private Matrix beforeMatrix;

	public Matrix getBeforeMatrix() {
		return beforeMatrix;
	}

	/**
	 * Sets the matrix at the begin of the calculation (adjacency matrix).
	 * 
	 * @param m
	 *            the adjacency matrix
	 */
	public void setBeforeMatrix(Matrix m) {
		beforeMatrix = m;
	}

	@Override
	public Matrix getMatrixBeforeStep(Step child) {
		if (steps.indexOf(child) == 0)
			return getBeforeMatrix();
		else
			return super.getMatrixBeforeStep(child);
	}

}
