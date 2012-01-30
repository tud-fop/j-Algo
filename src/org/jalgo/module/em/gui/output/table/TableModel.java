package org.jalgo.module.em.gui.output.table;

import java.util.ArrayList;
import java.util.IllegalFormatPrecisionException;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import org.jalgo.main.util.Messages;
import org.jalgo.module.em.data.Event;

public class TableModel extends AbstractTableModel{
	/**
	 * Stores the Content of the Table in an List<List<String>> 
	 * @author Meinhardt Branig
	 */
	private static final long serialVersionUID = 1L;
	private List<List<String>> data;
	private int index = 0;
	
	/**
	 * produces the first Column with Partitionnames
	 * @param eventSet
	 */
	public TableModel(Set<Event> eventSet){
		data = new ArrayList<List<String>>();
		List<String> X = new ArrayList<String>();
		for(Event aktEvent : eventSet){
			X.add(aktEvent.toString());
		}
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
	 * @return amount of rows
	 */
	@Override
	public int getRowCount() {
		return data.get(0).size();
	}
	
	/**
	 * set the index of the first column
	 * @param index
	 */
	public void setIndex(int index){
		this.index = index;
	}

	/**
	 * @param row
	 * @param column
	 * @return object in the chosen cell
	 */
	@Override
	public Object getValueAt(int row, int column) {
		if (column < getColumnCount()){
			if (row < data.get(column).size()){
				return data.get(column).get(row);
			}
		}
		return null;
	}
	
	/**
	 * adds a blank column
	 */
	public void addColumn(){
		List<String> p = new ArrayList<String>();
		for(int i=0; i<data.get(0).size(); i++){
			p.add(" ");
		}
		data.add(p);
	}
	
	/**
	 * removes a column
	 * @param column
	 */
	public void removeColumn(int column){
		if ((getColumnCount() > column) && (column>0)){
			data.remove(column);
		}
	}
	
	/**
	 * Returns the Name of the Column.
	 * @param columnIndex
	 * @return name of the column
	 */
	@Override
	public String getColumnName(int columnIndex){
		if (columnIndex >= 0 && columnIndex < this.getColumnCount()){
			if (columnIndex==0){
				return Messages.getString("em", "TableViewPanel.Event");
			} else if (columnIndex % 3 == 0) {
				return "<html>p<sub>" + ((index * 3 + columnIndex - 1) / 3) + "</sub></html>";
			} else if (columnIndex % 3 == 1) {
				return "<html>d<sub>" + ((index * 3 + columnIndex - 1) / 3) + "</sub></html>";
			} else
				return "<html>h<sub>" + ((index * 3 + columnIndex - 1) / 3) + "</sub></html>";
		} else
			return null;
	}
	
	/**
	 * @return returns always false (cell is not editable)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex){
		return false;
	}
	
	/**
	 * Set value of a cell and updates table.
	 * @param value
	 * @param rowIndex
	 * @param columnIndex
	 */
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex){
		try { 
			double px = Double.parseDouble(((String) value));
			 
				data.get(columnIndex).set(rowIndex, String.format("%.9f", px));
				fireTableCellUpdated( rowIndex, columnIndex );	
		}
		catch (IllegalFormatPrecisionException e) {
			data.get(columnIndex).set(rowIndex, (String) value);
			fireTableCellUpdated( rowIndex, columnIndex );
		}
		catch (NumberFormatException e) {
			data.get(columnIndex).set(rowIndex, (String) value);
			fireTableCellUpdated(rowIndex, columnIndex);
		}
	}
}
