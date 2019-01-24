package org.jalgo.module.em.gui.output.table;

import java.awt.Point;
import java.util.ArrayList;
import java.util.IllegalFormatPrecisionException;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.jalgo.module.em.control.LogState;

public class TableModelSE extends AbstractTableModel{
	/**
	 * Stores the Content of the Table in an List<List<String>> 
	 * @author Meinhardt Branig
	 */
	private static final long serialVersionUID = 1L;
	private List<List<String>> data;
	private int index = 0;
	private LogState log;
	private int objectIndex;
	
	/**
	 * initializes the table
	 * @param log
	 * @param objectIndex
	 */
	public TableModelSE(LogState log, int objectIndex){
		this.log = log;
		this.objectIndex = objectIndex;
		data = new ArrayList<List<String>>();
		List<String> X = new ArrayList<String>();
		
		for (int objectSide = 1; objectSide <= log.getExperiment()
				.get(objectIndex); objectSide++) {
			X.add(log.singleEventToString(new Point(objectSide, objectIndex)));
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
	 * 
	 * @param columnIndex
	 * @return name of the column
	 */
	@Override
	public String getColumnName(int columnIndex){
		if (columnIndex >= 0 && columnIndex < this.getColumnCount()){
			if (columnIndex==0){
				return log.getObjectNames()[objectIndex];
			}
			else {
				return "<html> p<sub>" + (index + columnIndex - 1) + "</sub></html>";
			}
		}
		
		else return null;
	}
	
	/**
	 * @return returns always false (cell is not editable)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex){
		return false;
	}
	
	/**
	 * set value of a cell and updates table
	 * @param value
	 * @param rowIndex
	 * @param columnIndex
	 */
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex){
//		try { 
//            double px = Double.parseDouble(((String) value));
//             
//                    data.get(columnIndex).set(rowIndex, (String) value);
//                    fireTableCellUpdated( rowIndex, columnIndex );  
//		}
//		catch (NumberFormatException e) {
//		}
		try { 
			double px = Double.parseDouble(((String) value));
			 
				data.get(columnIndex).set(rowIndex, String.format("%.30f", px));
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
