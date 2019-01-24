package org.jalgo.module.app.core;

import java.util.ArrayList;
import java.util.List;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Operation;
import org.jalgo.module.app.core.graph.Edge;
import org.jalgo.module.app.core.graph.Graph;
import org.jalgo.module.app.core.step.AtomicStep;
import org.jalgo.module.app.core.step.GroupStep;
import org.jalgo.module.app.core.step.RootStep;

/**
 * The <code>Calculation</code> class is responsible for calculating the
 * <code>Step</code>s and the Matrices for a given <code>Graph</code> and
 * its <code>SemiRing</code>.
 * 
 */
public class Calculation {

	private SemiRing semiring;
	private RootStep rootStep;
	private Graph graph;

	/**
	 * Instantiates a new Calculation.
	 */
	public Calculation() {
	}

	/**
	 * Gets the graph used in this calculation.
	 * 
	 * @return the graph.
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * Sets the <code>Graph</code> for the calculation and resets the
	 * <code>rootStep</code>.
	 * 
	 * @param graph
	 *            the given Graph for the calculation
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
		this.rootStep = null;
	}

	/**
	 * Gets the Semiring used in this calculation.
	 * 
	 * @return the semiring.
	 */
	public SemiRing getSemiring() {
		return semiring;
	}

	/**
	 * Sets the <code>SemiRing</code>, resets the <code>rootStep</code> and
	 * tries to convert the Graph weights, if possible.
	 * 
	 * @param semiring
	 *            the given SemiRing
	 */
	public void setSemiring(SemiRing semiring) {
		this.semiring = semiring;
		this.rootStep = null;

		// try {
		// convertGraphWeights();
		// } catch (InstantiationException e) {
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// e.printStackTrace();
		// }
	}

	/*
	 * We don't need this Method, since we don't convert a graph from one
	 * semiring to another.
	 */

	// /**
	// * Converts the weights of the given graph, if possible (the weights must
	// be
	// * a subclass of <code>NumericDataType</code>).
	// *
	// * @throws InstantiationException
	// * @throws IllegalAccessException
	// */
	// private void convertGraphWeights() throws InstantiationException,
	// IllegalAccessException {
	// Class<? extends DataType> type;
	// boolean numeric;
	//
	// if (graph == null)
	// return;
	//
	// type = semiring.getType();
	// numeric = ((Object) type instanceof NumericDataType);
	//
	// for (Edge e : graph.getEdges()) {
	// if (numeric && (e.getWeight() instanceof NumericDataType)) {
	//
	// NumericDataType numValue = (NumericDataType) type.newInstance();
	// numValue.setFromNumeric((NumericDataType) e.getWeight());
	// e.setWeight(numValue);
	// } else {
	// e.setWeight(type.newInstance());
	// }
	// }
	// }
	public RootStep calculateRootStep() {
		calculate();

		return rootStep;
	}

	/**
	 * Gets the Root Step used in this calculation.
	 * 
	 * @return the root step.
	 */
	public RootStep getRootStep() {
		if (rootStep == null)
			calculateRootStep();

		return rootStep;
	}

	/**
	 * Calculates the adjacency matrix from the graph edges and nodes
	 * 
	 * @return the adjacency matrix.
	 */
	public Matrix calculateInitialMatrix() {
		int size;
		Matrix matrix;
		DataType neutral, zero;
		Operation plus, dot;

		if (semiring == null || graph == null)
			return null;

		plus = semiring.getPlusOperation();
		dot = semiring.getDotOperation();

		zero = dot.getNeutralElement();
		neutral = plus.getNeutralElement();

		size = graph.getNodes().size();
		matrix = new Matrix(size);

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (i == j) {
					matrix.setValueAt(i, j, zero);
					matrix.setNodesAt(i, j, new ArrayList<Integer>());
				} else
					matrix.setValueAt(i, j, neutral);
			}
		}

		for (Edge e : graph.getEdges()) {
			int i, j;

			i = e.getBegin().getId();
			j = e.getEnd().getId();

			matrix.setValueAt(i, j, e.getWeight());
			matrix.setNodesAt(i, j, new ArrayList<Integer>());
		}

		return matrix;
	}

	/**
	 * Calculates the Matrices, the <code>AtomicStep</code>s and the
	 * <code>GroupStep</code>s for the given <code>Graph</code> and
	 * <code>SemiRing</code>.
	 */
	private void calculate() {
		Operation plus, dot;
		Matrix matrix, oldMatrix;
		int size;

		if (semiring == null || graph == null)
			return;

		// Fetch some calculation parameters
		size = graph.getNodes().size();

		plus = semiring.getPlusOperation();
		dot = semiring.getDotOperation();

		// Init start matrix
		matrix = calculateInitialMatrix();

		// Init root step
		rootStep = new RootStep();
		oldMatrix = matrix.clone();
		rootStep.setBeforeMatrix(oldMatrix);

		// Calculate
		for (int k = 0; k < size; k++) {
			GroupStep nodeStep;
			nodeStep = new GroupStep();

			for (int u = 0; u < size; u++) {
				for (int v = 0; v < size; v++) {
					AtomicStep cellStep;
					List<DataType> ops;
					List<Integer> nodes;
					DataType orig, val;
					boolean changed;

					// Calculate the new value
					val = plus.star(oldMatrix.getValueAt(k, k), dot);
					
					ops = new ArrayList<DataType>();
					ops.add(oldMatrix.getValueAt(u, k));
					ops.add(val);
					ops.add(oldMatrix.getValueAt(k, v));
					val = dot.op(ops);

					orig = oldMatrix.getValueAt(u, v);
					val = plus.op(orig, val);
					changed = !val.equals(orig);

					// Calculate the node list
					nodes = new ArrayList<Integer>();
					if (oldMatrix.getNodesAt(u, k) != null)
						nodes.addAll(oldMatrix.getNodesAt(u, k));
					nodes.add(k);
					if (oldMatrix.getNodesAt(k, v) != null)
						nodes.addAll(oldMatrix.getNodesAt(k, v));

					// Update the matrix
					if (changed) {
						matrix.setValueAt(u, v, val);
						matrix.setNodesAt(u, v, nodes);
					}

					// Create the step item
					cellStep = new AtomicStep();
					cellStep.setKUV(k, u, v);
					cellStep.setValue(val);
					cellStep.setNodes(nodes);
					cellStep.setChanged(changed);

					// Add the step
					nodeStep.addStep(cellStep);
				}
			}

			oldMatrix = matrix.clone();
			nodeStep.setAfterMatrix(oldMatrix);
			rootStep.addStep(nodeStep);
		}

		rootStep.setAfterMatrix(oldMatrix);
	}

}
