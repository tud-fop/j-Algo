package org.jalgo.module.app.core.step;

import java.util.List;

import org.jalgo.module.app.core.Matrix;
import org.jalgo.module.app.core.dataType.DataType;

/**
 * Represents a single, atomic step, where only one element in the matrix is
 * processed (the element (u,v)).
 * 
 */
public class AtomicStep extends Step {

	private int u;
	private int v;
	private int k;
	private DataType value;
	private boolean changed;
	private List<Integer> nodes;

	@Override
	public Matrix getAfterMatrix() {
		Matrix matrix;

		matrix = getBeforeMatrix();
		if (changed) {
			matrix = matrix.clone();
			injectChange(matrix);
		}

		return matrix;
	}

	/**
	 * @return the u
	 */
	public int getU() {
		return u;
	}

	/**
	 * @return the v
	 */
	public int getV() {
		return v;
	}

	/**
	 * @return the k
	 */
	public int getK() {
		return k;
	}

	/**
	 * Sets the parameters needed for the step.
	 * 
	 * @param k
	 *            the step k to set
	 * @param u
	 *            the u-coordinate to set
	 * @param v
	 *            the v-coordinate to set
	 */
	public void setKUV(int k, int u, int v) {
		this.k = k;

		this.u = u;
		this.v = v;
	}

	/**
	 * @return the value
	 */
	public DataType getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(DataType value) {
		this.value = value;
	}

	/**
	 * @return the nodes
	 */
	public List<Integer> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(List<Integer> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Changes the given matrix by inserting the changed cell.
	 * 
	 * @param matrix
	 *            the matrix to modify
	 */
	public void injectChange(Matrix matrix) {
		if (changed) {
			matrix.setValueAt(u, v, value);
			matrix.setNodesAt(u, v, nodes);
		}
	}

	/**
	 * @return the changed
	 */
	public boolean isChanged() {
		return changed;
	}

	/**
	 * @param changed
	 *            the changed to set
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public String toString() {
		String str;

		str = "Step:{k/u/v = " + k + "/" + u + "/" + v + "; changed = "
				+ changed + "; value = " + value + "; nodes = " + nodes + "}\n";
		str += getAfterMatrix();

		return str;
	}
}