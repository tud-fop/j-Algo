package org.jalgo.module.em.gui.input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.table.DefaultTableModel;

import org.jalgo.module.em.data.Event;

/**
 * A TableModel that works on the experiments event set.
 * Sets the properties for cell being 'unselectable'.
 * 
 * @author Tobias Nett
 *
 */
public class PartitionTableModel extends DefaultTableModel {
	
	private static final long serialVersionUID = 1L;
	private List<List<Event>> colData;

	/**
	 * @param events set of events which should be displayed in the table.
	 */
	public PartitionTableModel(Set<Event> events) {
//		TreeMap<Integer, List<Event>> map = new TreeMap<Integer, List<Event>>();
//		for (Event event : events) {
//			int i = event.getTuple().firstElement() - 1;
//			if (!(map.containsKey(i))) {
//				map.put(i, new ArrayList<Event>());
//			}
//			map.get(i).add(event);
//		}
//		for (List<Event> l : map.values()) {
//			Collections.sort(l);
//		}
//		rowData = new ArrayList<List<Event>>(map.values());
		
		TreeMap<Integer, List<Event>> cols = new TreeMap<Integer, List<Event>>();
		for (Event event : events){
			int columnIndex = event.getTuple().firstElement() - 1;
			if (!(cols.containsKey(columnIndex))) {
				cols.put(columnIndex, new ArrayList<Event>());
			}
			cols.get(columnIndex).add(event);
		}
		for (List<Event> l : cols.values()) {
			Collections.sort(l);
		}
		colData = new ArrayList<List<Event>>(cols.values());
	}

	/**
	 * @return number of elements in a row, 0 if there are no events
	 */
	@Override
	public int getColumnCount() {
//		List<Event> firstRow = rowData.get(0);
//		if (firstRow != null)
//			return firstRow.size();
//		return 0;
		if (colData != null)
			return colData.size();
		return 0;
	}

	/**
	 * @return number of elements in one column
	 */
	@Override
	public int getRowCount() {
//		if (rowData != null)
//			return rowData.size();
//		return 0;
		List<Event> firstColumn;
		try {
			firstColumn = colData.get(0);
			if (firstColumn != null)
				return firstColumn.size();
		} catch (Exception e) {
		
		}
		return 0;
	}

	@Override
	public Object getValueAt(int row, int column) {
//		if (row < this.getRowCount() && column < this.getColumnCount() )
//			try {
//				return rowData.get(row).get(column);
//			} catch (ArrayIndexOutOfBoundsException e) {
//			}
//		return null;
		if (row < this.getRowCount() && column < this.getColumnCount() )
			try {
				return colData.get(column).get(row);
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		return null;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}
