package org.jalgo.module.app.core;

import java.util.List;

import org.jalgo.module.app.core.dataType.DataType;

/**
 * Represents two n by n Matrices for a <code>Graph</code> with n
 * <code>Node</code>s. The first matrix represents the values (of
 * <code>DataType</code> calculated in <code>Calculation</code>, whereas
 * the second Matrix contains a <code>List</code> for every element, which
 * represents the path from one <code>Node</code> to another.
 */
public class Matrix {

	private int size;
	private DataType values[][];
	private List<Integer> viaNodes[][];

	/**
	 * Initiates a matrix of given size.
	 * 
	 * @param sz
	 *            The size of the Matrix.
	 */
	@SuppressWarnings(value = { "unchecked" })
	public Matrix(int sz) {
		size = sz;
		values = new DataType[sz][sz];
		viaNodes = new List[sz][sz]; // A bit hacky, misses a cast...
	}

	/**
	 * Initiates a Matrix by copying another one.
	 * 
	 * @param other
	 */
	@SuppressWarnings(value = { "unchecked" })
	public Matrix(Matrix other) {
		this(other.size);

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				values[x][y] = other.values[x][y].clone();
				viaNodes[x][y] = other.viaNodes[x][y];
			}
		}
	}

	public Matrix clone() {
		return new Matrix(this);
	}

	/**
	 * @return The size of the symmetrical <code>Matrix</code>.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * 
	 * @param x
	 *            the x-coordinate in the matrix.
	 * @param y
	 *            the y-coordinate in the matrix.
	 * @return the value (of <code>DataType</code>) of the given coordinates
	 *         in the <code>Matrix</code>.
	 */
	public DataType getValueAt(int x, int y) {
		return values[x][y];
	}

	/**
	 * 
	 * @param x
	 *            the x-coordinate in the matrix.
	 * @param y
	 *            the y-coordinate in the matrix.
	 * @param val
	 *            the value for the coordinate.
	 */
	public void setValueAt(int x, int y, DataType val) {
		values[x][y] = val;
	}

	/**
	 * 
	 * @param x
	 *            the x-coordinate in the matrix.
	 * @param y
	 *            the y-coordinate in the matrix.
	 * @return the <code>List</code> of the intermediate nodes between
	 *         <code>Node</code> x and <code>Node</code> y.
	 */
	public List<Integer> getNodesAt(int x, int y) {
		return viaNodes[x][y];
	}

	/**
	 * 
	 * @param x
	 *            the x-coordinate in the matrix.
	 * @param y
	 *            the y-coordinate in the matrix.
	 * @param nodes
	 *            the <code>List</code> of the intermediate nodes between
	 *            <code>Node</code> x and <code>Node</code> y.
	 */
	public void setNodesAt(int x, int y, List<Integer> nodes) {
		viaNodes[x][y] = nodes;
	}

	/**
	 * Prints the <code>Matrix</code> row-aligned.
	 */
	public String toString() {
		String str;
		int maxLength;

		// determine max. length of a matrix element
		maxLength = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (values[i][j].toString().length() > maxLength)
					maxLength = values[i][j].toString().length();
			}
		}

		str = "Matrix:{size = " + size + "}\n";

		for (int i = 0; i < size; i++) {
			str += "( ";
			for (int j = 0; j < size; j++) {
				for (int k = values[i][j].toString().length(); k < maxLength; k++)
					str += " ";
				str += values[i][j] + " ";
			}
			str += ")\n";
		}

		return str;
	}

	/**
	 * Compares two matrices based on their element values.
	 */
	public boolean equals(Object o) {
		Matrix other;

		if (!(o instanceof Matrix) || o == null)
			return false;

		other = (Matrix) o;

		if (size != other.size)
			return false;

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				if (!(values[x][y].equals(other.values[x][y]))) // ||
					// !viaNodes[x][y].equals(other.viaNodes[x][y]))
					return false;
			}
		}

		return true;
	}

}
