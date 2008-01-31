/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.jalgo.module.pulsemem.gui.components;

import javax.swing.*;
import javax.swing.table.*;

import org.jalgo.main.util.Messages;

import java.awt.*;
import org.jalgo.module.pulsemem.gui.*;

// import org.jalgo.module.pulsemem.core.*;

/**
 * @version $Revision: 1.31 $
 * @author Karsten Diepelt
 */
public class PulseMemTable extends JScrollPane {

	private static final long serialVersionUID = -3595289495202738856L;

	private JTable tabStatic;

	private JTable tabDynamic;

	private Font tableFont = GUIConstants.STANDARDFONT;

	private int numberOfGlobalVars;

	private int numberOfVisibleRows;

	private int verticalScrollValue;

	private int horizontalScrollValue;

	private boolean dataBeenAdded = false;

	private boolean beamerMode = false;

	String columns[] = {
			Messages.getString("pulsemem", "PulseMemTable.Stack"), Messages.getString("pulsemem", "PulseMemTable.Label"), " - " }; //$NON-NLS-1$ //$NON-NLS-2$

	CellObject data[][] = { {
			new CellObject("-----"), new CellObject("-----"), new CellObject(" - ") } }; //$NON-NLS-1$ //$NON-NLS-2$

	public PulseMemTable() {
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// System.out.println("pmt erstellt, initialisiere");
		numberOfGlobalVars = 1;
		numberOfVisibleRows = 0;
		init();
		this.repaint();
	}

	private void init() {

		tabStatic = new JTable(fixedModel);
		tabDynamic = new JTable(mainModel);

		tabStatic.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabDynamic.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		tabStatic.setDragEnabled(false);
		tabDynamic.setDragEnabled(false);

		tabStatic.getTableHeader().setReorderingAllowed(false);
		tabDynamic.getTableHeader().setReorderingAllowed(false);

		tabStatic.setFont(tableFont);
		tabDynamic.setFont(tableFont);

		findAndSetBestTableSize();

		tabStatic.setDefaultRenderer(Component.class, cellRenderer);
		tabDynamic.setDefaultRenderer(Component.class, cellRenderer);
		setCenterRendererForAllColumns(tabStatic, Color.white, Color.black);
		setCenterRendererForAllColumns(tabDynamic, Color.white, Color.gray);

		ListSelectionModel model = tabStatic.getSelectionModel();
		tabDynamic.setSelectionModel(model);
		this.setViewportView(tabDynamic);
		Dimension fixedSize = tabStatic.getPreferredSize();
		fixedSize.width += 5;
		JViewport view = new JViewport();
		view.setView(tabStatic);
		view.setPreferredSize(fixedSize);
		view.setMaximumSize(fixedSize);
		this.setCorner(JScrollPane.UPPER_LEFT_CORNER, tabStatic
				.getTableHeader());
		this.setRowHeaderView(view);

		// Dimension xxx = this.getViewport().getMaximumSize();
		// tabDynamic.setSize(xxx);
		// this.repaint();
	}

	public void setData(String[] columns, CellObject[][] data, int nogv) {

		if (dataBeenAdded) {
			this.saveVerticalScrollValue();
			if (this.numberOfVisibleRows >= data.length) {
				this.numberOfVisibleRows = data.length;
			}

			this.columns = columns;
			this.data = data;
			this.numberOfGlobalVars = nogv;
			init();
			this.restoreVerticalScrollValue();
			this.repaint();

		} else {
			this.dataBeenAdded = true;
			this.numberOfVisibleRows = 0;

			this.columns = columns;
			this.data = data;
			this.numberOfGlobalVars = nogv;
			if (beamerMode) {
				this.enableBeamer();
			} else {
				init();
			}
			this.getParent().repaint();
		}

	}

	public void setVisibleLine(int line) {
		this.numberOfVisibleRows = Math.min(line, data.length);
		this.saveHorizontalScrollValue();
		init();
		this.restoreHorizontalScrollValue();
		this.getVerticalScrollBar().setValue(
				this.getVerticalScrollBar().getMaximum());
	}

	public void resetTable() {
		this.dataBeenAdded = false;
		String columns[] = {
				Messages.getString("pulsemem", "PulseMemTable.Stack"), Messages.getString("pulsemem", "PulseMemTable.Label"), " - " }; //$NON-NLS-1$ //$NON-NLS-2$

		CellObject data[][] = { {
				new CellObject("-----"), new CellObject("-----"), new CellObject(" - ") } }; //$NON-NLS-1$ //$NON-NLS-2$

		this.columns = columns;
		this.data = data;
		this.numberOfVisibleRows = 0;
		this.numberOfGlobalVars = 1;
		init();
		this.repaint();
	}

	public void displayUntilNextRow() {
		if (numberOfVisibleRows < data.length) {
			numberOfVisibleRows++;
			this.saveHorizontalScrollValue();
			init();
			this.restoreHorizontalScrollValue();
			this.getVerticalScrollBar().setValue(
					this.getVerticalScrollBar().getMaximum());
		}
	}

	public void displayUntilPreviousRow() {
		if (numberOfVisibleRows > 0) {
			numberOfVisibleRows--;
			this.saveHorizontalScrollValue();
			init();
			this.restoreHorizontalScrollValue();
			this.getVerticalScrollBar().setValue(
					this.getVerticalScrollBar().getMaximum());
		}
	}

	public void displayUntilLastRow() {
		this.saveHorizontalScrollValue();
		this.saveVerticalScrollValue();
		numberOfVisibleRows = data.length;
		init();
		this.restoreVerticalScrollValue();
		this.restoreHorizontalScrollValue();
	}

	public void displayFirstRowOnly() {
		numberOfVisibleRows = 1;
		this.saveHorizontalScrollValue();
		init();
		this.restoreHorizontalScrollValue();
	}

	private void saveVerticalScrollValue() {
		this.verticalScrollValue = this.getVerticalScrollBar().getValue();
	}

	private void restoreVerticalScrollValue() {
		this.getVerticalScrollBar().setValue(this.verticalScrollValue);
	}

	private void saveHorizontalScrollValue() {
		this.horizontalScrollValue = this.getHorizontalScrollBar().getValue();
	}

	private void restoreHorizontalScrollValue() {
		this.getHorizontalScrollBar().setValue(this.horizontalScrollValue);
	}

	private void setCenterRendererForAllColumns(JTable tab, Color fg, Color bg) {
		TableCellRenderer rend = new CenterRenderer(fg, bg);
		int numberOfColumns = tab.getColumnCount();
		for (int i = 0; i < numberOfColumns; i++) {
			TableColumn tc = tab.getColumnModel().getColumn(i);
			tc.setHeaderRenderer(rend);
		}
	}

	private void findAndSetBestTableSize() {

		int rows = data.length;
		int cols = columns.length;
		int statCols = tabStatic.getColumnCount();
		int dynCols = tabDynamic.getColumnCount();

		int maxColumnWidth[] = new int[cols];
		maxColumnWidth[0] = 60;
		maxColumnWidth[1] = 60;
		for (int i = 2; i < cols; i++) {
			maxColumnWidth[i] = 10;
		}

		for (int i = 0; i < rows; i++) {

			if (beamerMode) {
				if (i == rows - 1) {
					tabStatic.setRowHeight(i, 20);
					tabDynamic.setRowHeight(i, 20);
				} else {
					tabStatic.setRowHeight(i, 45);
					tabDynamic.setRowHeight(i, 45);
				}
			} else {
				if (i == rows - 1) {
					tabStatic.setRowHeight(i, 15);
					tabDynamic.setRowHeight(i, 15);
				} else {
					tabStatic.setRowHeight(i, 30);
					tabDynamic.setRowHeight(i, 30);
				}
			}
			for (int j = 0; j < cols; j++) {
				if (data[i][j] != null) {
					maxColumnWidth[j] = Math.min(Math.max(data[i][j]
							.getPreferredSize().width, maxColumnWidth[j]), 100);
				}
			}
		}

		for (int i = 0; i < statCols; i++) {
			tabStatic.getColumnModel().getColumn(i).setPreferredWidth(
					maxColumnWidth[i] + 5);
		}

		for (int i = 0; i < dynCols; i++) {
			tabDynamic.getColumnModel().getColumn(i).setPreferredWidth(
					maxColumnWidth[i + statCols] + 5);
		}
	}

	public void enableBeamer() {
		this.saveHorizontalScrollValue();
		this.saveVerticalScrollValue();
		beamerMode = true;
		this.tableFont = GUIConstants.BEAMERFONT;
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < columns.length; j++) {
				if (data[i][j] != null) {
					data[i][j].setFont(tableFont);
					// data[i][j].updateUI();
				}
			}
		}
		init();
		this.restoreVerticalScrollValue();
		this.restoreHorizontalScrollValue();
	}

	public void disableBeamer() {
		this.saveHorizontalScrollValue();
		this.saveVerticalScrollValue();
		beamerMode = false;
		this.tableFont = GUIConstants.STANDARDFONT;
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < columns.length; j++) {
				if (data[i][j] != null) {
					data[i][j].setFont(tableFont);
					// data[i][j].updateUI();
				}
			}
		}
		init();
		this.restoreVerticalScrollValue();
		this.restoreHorizontalScrollValue();
	}

	/**
	 * Table Model for the fixed table
	 */

	TableModel fixedModel = new AbstractTableModel() {

		private static final long serialVersionUID = -5438194825238398059L;

		public int getColumnCount() {
			return numberOfGlobalVars + 2;
		}

		public String getColumnName(int col) {
			return columns[col];
		}

		public int getRowCount() {
			return numberOfVisibleRows;
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		public Class getColumnClass(int columnIndex) {
			return CellObject.class;
		}
	};

	/**
	 * Table Model for the non-fixed table
	 */

	TableModel mainModel = new AbstractTableModel() {

		private static final long serialVersionUID = 5243145608765652138L;

		public int getColumnCount() {
			return columns.length - (numberOfGlobalVars + 2);
		}

		public String getColumnName(int col) {
			return columns[col + numberOfGlobalVars + 2];
		}

		public int getRowCount() {
			return numberOfVisibleRows;
		}

		public Object getValueAt(int row, int col) {
			return data[row][col + numberOfGlobalVars + 2];
		}

		@SuppressWarnings("unchecked")
		public Class getColumnClass(int columnIndex) {
			return CellObject.class;
		}

	};

	/**
	 * This custom TableCellRenderer is responsible for displaying the
	 * CellObjects
	 */
	TableCellRenderer cellRenderer = new TableCellRenderer() {

		public final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component cell;
			if (value instanceof CellObject) {
				cell = (CellObject) value;
			} else {
				cell = DEFAULT_RENDERER.getTableCellRendererComponent(table,
						value, isSelected, hasFocus, row, column);
			}
			if (!isSelected) {
				if (row % 2 == 0) {
					cell.setBackground(Color.WHITE);
				} else {
					cell.setBackground(Color.LIGHT_GRAY);
				}
			} else {
				if (row % 2 == 0) {
					cell.setBackground(Color.YELLOW);
				} else {
					cell.setBackground(Color.ORANGE);
				}
			}
			// cell.setFont(tableFont);

			return cell;
		}

	};

	/**
	 * center renderer used for columns
	 */

	class CenterRenderer implements TableCellRenderer {

		Color fg;

		Color bg;

		public CenterRenderer(Color fg, Color bg) {
			this.fg = fg;
			this.bg = bg;
		}

		public final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			DEFAULT_RENDERER.setVerticalAlignment(SwingConstants.CENTER);
			DEFAULT_RENDERER.setHorizontalAlignment(SwingConstants.CENTER);
			Component cell = DEFAULT_RENDERER.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);
			cell.setBackground(bg);
			cell.setForeground(fg);
			return cell;
		}

	}

	/**
	 * @return the numberOfVisibleRows
	 */
	public int getNumberOfVisibleRows() {
		return numberOfVisibleRows;
	};

}
