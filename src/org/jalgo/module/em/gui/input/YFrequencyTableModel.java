package org.jalgo.module.em.gui.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.jalgo.main.util.Messages;
import org.jalgo.module.em.data.Partition;

/**
 * TableModel for the FrequencyPanel. The table data is created out of a set of
 * Partition (observations).
 * <p>
 * The model displays the name/mathematical representation in one column and the
 * frequency in another.
 * 
 * @author Tobias Nett
 */
public class YFrequencyTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 2539673847879777440L;

	private List<Partition> observations;
	private Vector<Vector<String>> data;

	/**
	 * Creates a new object of <code>YFrequencyTableModel</code> by calculating
	 * the table data out of the given set of partitions.
	 * <p>
	 * Consists of three columns, one for the name, the second for the
	 * mathematical representation and the last for the frequency.
	 * 
	 * @param observations
	 *            set of partitions to show
	 */
	public YFrequencyTableModel(Set<Partition> observations) {
		this.observations = new ArrayList<Partition>(observations);
		data = new Vector<Vector<String>>();

		for (Partition partition : observations) {
			Vector<String> v = new Vector<String>();
			v.add(partition.getName()); // name of the partition
			v.add(partition.getMathSet()); // mathematical representation of the
											// element set
			v.add(Double.toString(partition.getFrequency())); // partition's frequency
			data.add(v);
		}

	}

	/**
	 * Returns '3' on every call. This model consists of exactly three columns.
	 * 
	 * @return 3 static column count for this table model
	 */
	@Override
	public int getColumnCount() {
		return 3;
	}

	/**
	 * Returns the number of rows in this model which is the count of
	 * partitions.
	 * 
	 * @return number of partitions for this experiment
	 */
	@Override
	public int getRowCount() {
		if (observations != null)
			return observations.size();
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex < getRowCount() && columnIndex < getColumnCount()) {
			return data.get(rowIndex).get(columnIndex);
			
		}
		return null;
	}

	/**
	 * The name of specified column. The name is either 'Name', 'Observation' or
	 * 'Frequency'.
	 * 
	 * @param column
	 *            index of column
	 * @return name of the specified column
	 */
	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return Messages.getString("em",
					"YFrequencyTableModel.PartitionName");
		case 1:
			return Messages.getString("em",
					"YFrequencyTableModel.PartitionElements");
		case 2:
			return Messages.getString("em", "YFrequencyTableModel.Frequency");
		default:
			return null;
		}
	}

	/**
	 * Returns true if <code>columnIndex</code> equals 2 (index of the
	 * <i>frequency column</i>.
	 * 
	 * @param rowIndex
	 *            the row whose value is to be queried
	 * @param columnIndex
	 *            the column whose value is to be queried
	 * @return true if columnIndex is 2 (frequency column)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 2)
			return true;
		return false;
	}

	/**
	 * Sets the object value for the cell at <code>column</code> and
	 * <code>row</code>. <code>value</code> is the new value which is directly
	 * saved into the partition.
	 * <p>
	 * Because of <code>isCellEditable</code> the only editable cells are the
	 * frequency cells.
	 * 
	 * @param value
	 *            the new value
	 * @param rowIndex
	 *            the row whose value is to be changed
	 * @param columnIndex
	 *            he column whose value is to be changed
	 */
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		data.get(rowIndex).set(columnIndex, (String) value);
		// write change directly to the partition
		// observations.get(rowIndex).setFrequency(
		// Double.parseDouble((String) value));
		observations.get(rowIndex).setFrequency(
				InputStringParser.parseInputString((String) value, 0,
						Double.MAX_VALUE));
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	/**
	 * Returns the sum of the partitions' frequencies. This sum is always
	 * greater or equal 0.
	 * 
	 * @return sum of the partitions frequencies
	 */
	public double getFrequencySum() {
		double sum = 0;
		for (Partition p : observations) {
			sum += p.getFrequency();
		}
		return sum;
	}

}
