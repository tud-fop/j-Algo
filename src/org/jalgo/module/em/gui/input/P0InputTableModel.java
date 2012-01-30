package org.jalgo.module.em.gui.input;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import org.jalgo.module.em.data.StartParameters;

/**
 * Stores the Content of the Table in a List<List<String>> and provides some
 * InputValidation Functionality.
 * 
 * @author Kilian Gebhardt
 */
public class P0InputTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private List<List<String>> data;

	/**
	 * Initializes the TableModel with the X-Column and one blank p0 Column.
	 * 
	 * @param startParameters
	 *            <code>StartParamters</code> object that has to be filled with
	 *            data
	 * @param objectIndex
	 *            the Index of the single Experiment
	 */
	public P0InputTableModel(final StartParameters startParameters,
			final int objectIndex) {
		data = new ArrayList<List<String>>();
		List<String> X = new ArrayList<String>();
		for (int objectSide = 1; objectSide <= startParameters.getExperiment()
				.get(objectIndex); objectSide++) {
			X.add(startParameters.singleEventToString(new Point(objectSide, objectIndex)));
		}
		data.add(X);

		// tries to find valid existing P0 input
		if ((startParameters.getP0EMState() != null)
				&& (startParameters.getP0EMState().getEMData() != null)
				&& (startParameters.getP0EMState().getEMData().get(0)
						.getPForSingleEvent().containsKey(new Point(1,
						objectIndex)))
				&& (startParameters.getP0EMState().getEMData().get(0)
						.getPForSingleEvent().containsKey(new Point(
						startParameters.getExperiment().get(objectIndex),
						objectIndex)))
				&& !(startParameters.getP0EMState().getEMData().get(0)
						.getPForSingleEvent().containsKey(new Point(
						startParameters.getExperiment().get(objectIndex) + 1,
						objectIndex)))) {
			for (int p0 = 0; p0 < startParameters.getP0EMState().getEMData()
					.size(); p0++) {
				List<String> x2 = new ArrayList<String>();
				for (int objectSide = 1; objectSide <= startParameters
						.getExperiment().get(objectIndex); objectSide++) {
					if (startParameters.getP0EMState().getEMData().get(p0)
							.getPForSingleEvent()
							.get(new Point(objectSide, objectIndex)) > 0) {
						x2.add(startParameters.getP0EMState().getEMData()
								.get(p0).getPForSingleEvent()
								.get(new Point(objectSide, objectIndex))
								.toString());
					} else {
						x2.add("");
					}
				}
				data.add(x2);
			}
		}

		if (data.size() < 2) {
			addColumn();
		}
	}

	@Override
	public final int getColumnCount() {
		return data.size();
	}

	@Override
	public final int getRowCount() {
		return data.get(0).size();
	}

	@Override
	public final Object getValueAt(final int row, final int column) {
		if (column < getColumnCount()) {
			if (row < data.get(column).size()) {
				return data.get(column).get(row);
			}
		}
		return null;
	}

	/**
	 * Adds a blank Column.
	 */
	public final void addColumn() {
		List<String> p = new ArrayList<String>();
		for (int row = 0; row < data.get(0).size(); row++) {
			p.add("");
		}
		data.add(p);
	}

	/**
	 * Removes a column.
	 * 
	 * @param column
	 *            the Index of the column
	 */
	public final void removeColumn(int column) {
		if ((getColumnCount() > column) && (column > 0)) {
			data.remove(column);
		}
	}

	/**
	 * creates the Name of the Column for the Table Header.
	 * 
	 * @return the Name "p0" + the Number of the p0
	 */
	@Override
	public final String getColumnName(final int columnIndex) {
		if (columnIndex == 0) {
			return "<html>X</html>";
		} else {
			return "<html>p<sub>0</sub> " + ((Integer) columnIndex).toString() + "</html>";
		}
	}

	/**
	 * 
	 */
	@Override
	public final boolean isCellEditable(final int rowIndex,
			final int columnIndex) {
		if (columnIndex == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Updates Table, if a Double Value was inserted, and the 0 < value < 1.0
	 * Otherwise the Cell stays in EditMode
	 */
	@Override
	public final void setValueAt(final Object value, final int rowIndex,
			final int columnIndex) {
		data.get(columnIndex).set(rowIndex, (String) value);
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	/**
	 * Checks all Cells, if Double Values were inserted and checks if the sum of
	 * each Row is 1.0.
	 * 
	 * @return the Index of the first column, that misses these requirements or
	 *         '-1' if everything is OK
	 */
	public final int checkColumns() {
		for (int i = 1; i < data.size(); i++) {
			List<String> p0 = data.get(i);
			double p0sum = 0;
			for (String px : p0) {
				if (parseInputString(px) < 0) {
					return i;
				} else {
					p0sum += parseInputString(px);
				}
			}

			if (Math.abs(p0sum - 1.0) > 0.001) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @param columnIndex
	 *            the index of the column whose values are to be added up
	 * @return the added up values of the specified column
	 */
	public final double getColumnSum(final int columnIndex) {
		double columnSum = 0.0;
		for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++) {
			if (parseInputString((String) getValueAt(rowIndex, columnIndex)) != -1) {
				columnSum += parseInputString((String) getValueAt(rowIndex,
						columnIndex));
			}
		}
		return columnSum;
	}

	/**
	 * Converts a String that represents a FlotingPointNumber in the Range of
	 * 0..1 in one of the following ways to double: "0,1323", "0.1323", "1/12".
	 * WhiteSpace is ignored.
	 * 
	 * @param string
	 *            String that should be parsed
	 * @return if input is correct, the corresponding double Value; otherwise
	 *         -1.0
	 */
	public static double parseInputString(final String string) {
		StringBuffer input = new StringBuffer(string);
		try {
			int index = 0;
			// Remove WhiteSpace
			while (index < input.length()) {
				if (input.charAt(index) == ' ') {
					input.deleteCharAt(index);
				} else {
					index++;
				}
			}
			index = 0;
			// Replaces ',' with '.'
			while (index < input.length()) {
				if (input.charAt(index) == ',') {
					input.setCharAt(index, '.');
				} else {
					index++;
				}
			}

			// Splits in Case of '/'
			if (input.toString().split("/").length == 2) {
				double numerator = Double.parseDouble(input.toString().split(
						"/")[0]);
				double denominator = Double.parseDouble(input.toString().split(
						"/")[1]);
				if ((numerator / denominator > 1)
						|| (numerator / denominator < 0)) {
					return -1.0;
				}
				return numerator / denominator;
			} else {
				double value = Double.parseDouble(input.toString());
				if (value <= 0 || value > 1) {
					return -1;
				}
				return value;
			}
			// If the Standard <code>Double.parseDouble()</code> won't work,
			// the Input is invalid.
		} catch (Exception e) {
			return -1.0;
		}
	}

}
