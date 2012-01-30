package org.jalgo.module.em.gui.output.table;

import java.util.ArrayList;
import java.util.IllegalFormatPrecisionException;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Stores the Content of the Table in an List<List<String>>
 * 
 * @author Meinhardt Branig
 */
public class TableModelLike extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private List<String> data;
	private int index = 0;
	private String log = "";

	/**
	 * initializes the table
	 */
	public TableModelLike() {
		data = new ArrayList<String>();
		String X = "  ";
		data.add(X);
	}

	/**
	 * @return amount of columns
	 */
	@Override
	public int getColumnCount() {
		return data.size();
	}

	/**
	 * Returns the value for the cell at {@code row} and {@code column}.
	 * 
	 * @param row
	 *            the row whose value is to be queried
	 * @param column
	 *            the column whose value is to be queried
	 * @return object in the chosen cell
	 */
	@Override
	public Object getValueAt(int row, int column) {
		if (column < getColumnCount()) {
			return data.get(column);

		}
		return null;
	}

	/**
	 * adds a blank column
	 */
	public void addColumn() {
		String p = " ";
		data.add(p);
	}

	/**
	 * removes the column at the specified index {@code column}.
	 * 
	 * @param column
	 *            the index of the column that is to be removed
	 */
	public void removeColumn(int column) {
		if ((getColumnCount() > column) && (column > 0)) {
			data.remove(column);
		}
	}

	/**
	 * set the index of the first column
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * change between Likelihood and logarithmic Likelihood
	 * @param set
	 */
	public void setLog(boolean set) {
		if (set) {
			log = "log";
		} else {
			log = "";
		}
	}

	/**
	 * 
	 * @param columnIndex
	 * @return name of the column
	 */
	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex >= 0 && columnIndex < this.getColumnCount()) {
			if (columnIndex == 0) {
				return "<html>"+log + " L </html>";
			}
			// else return "L "+((Integer) index[columnIndex-1]).toString();
			// else return "L "+index[columnIndex];
			else
				return "<html>"+log + " L<sub>" + (index + columnIndex - 1) + "</sub></html>";
		}
		return null;
	}

	/**
	 * Returns false for every cell.
	 * 
	 * @return {@code false} - no cell editable
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	/**
	 * Set the value of a cell and updates the table.
	 * 
	 * @param value the new value
	 * @param rowIndex the row whose value is to be changed
	 * @param columnIndex the column whose value is to be changed
	 */
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		try {
			double px = Double.parseDouble(((String) value));
			data.set(columnIndex, String.format("%.30f", px));
			fireTableCellUpdated(rowIndex, columnIndex);
		}
		catch (IllegalFormatPrecisionException e) {
			data.set(columnIndex, (String) value);
			fireTableCellUpdated(rowIndex, columnIndex);
		}
		catch (NumberFormatException e) {
			data.set(columnIndex, (String) value);
			fireTableCellUpdated(rowIndex, columnIndex);
		}
	}

	/**
	 * Returns the number of rows. As the likelihood table is only one single row the return value is 1 always.
	 * 
	 * @return amount of rows, always 1 for {@code TableModelLike}
	 */
	@Override
	public int getRowCount() {
		return 1;
	}
}
