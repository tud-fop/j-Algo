/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2010 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
package org.jalgo.module.am0c0.model;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.gui.SimulationView;
import org.jalgo.module.am0c0.model.am0.MachineConfiguration;

/**
 * Simple paging table model which shows only a given number of lines at one
 * time.
 * 
 * @author Max Leuth&auml;user
 */
public class AM0PagingTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	protected int pageSize;
	protected int pageOffset;
	protected List<MachineConfiguration> data;
	private final String[] header = { Messages.getString("am0c0", "AM0PagingTableModel.0"), Messages.getString("am0c0", "AM0PagingTableModel.1"), Messages.getString("am0c0", "AM0PagingTableModel.2"), Messages.getString("am0c0", "AM0PagingTableModel.3"), Messages.getString("am0c0", "AM0PagingTableModel.4") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	private int rowCount = 0;
	private static final int STANDARD_PAGE_SIZE = 50;
	private SimulationView simView;

	public AM0PagingTableModel(SimulationView v) {
		data = new ArrayList<MachineConfiguration>();
		pageSize = STANDARD_PAGE_SIZE;
		simView = v;
	}

	/**
	 * Update the row count to fit to the currently shows values.
	 */
	private void updateRowCount() {
		if (data.isEmpty()) {
			rowCount = 0;
			return;
		}
		if (data.size() % pageSize == 0) {
			rowCount = pageSize;
			return;
		}
		rowCount = data.size() % pageSize;
	}

	/**
	 * Return values appropriate for the visible table part.
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return rowCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return header.length;
	}

	/**
	 * Work only on the visible part of the table.
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		int realRow = row + (pageOffset * pageSize);

		switch (col) {
		case 0:
			return data.get(realRow).getProgramCounter();
		case 1:
			return data.get(realRow).getStack();
		case 2:
			return data.get(realRow).getRam();
		case 3:
			return data.get(realRow).getInputStream();
		case 4:
			return data.get(realRow).getOutputStream();
		default:
			throw new IllegalArgumentException("Col has to be between 0 and 4!"); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int col) {
		return header[col];
	}

	/**
	 * Use this method to figure out which page you are on.
	 * 
	 * @return the number of the page which is currently shown.
	 */
	public int getPageOffset() {
		return pageOffset;
	}

	/**
	 * Add a new row containing the values of a {@link MachineConfiguration}.
	 * 
	 * @param m
	 */
	public void addRow(MachineConfiguration m) {
		data.add(m);
		fireTableDataChanged();
		updateRowCount();
		pageDown();
	}

	/**
	 * Show only the last page.
	 */
	public void showLastPage() {
		if (pageOffset < getPageCount()) {
			while (pageDown()) {
			}
			;
		}
	}

	/**
	 * Show only the fist page.
	 */
	public void showFirstPage() {
		while (pageUp()) {
		}
		;
	}

	/**
	 * Remove the last page and call {@link AM0PagingTableModel#pageUp()} if
	 * necessary.
	 */
	public void removeLastRow() {
		data.remove(data.size() - 1);
		updateRowCount();
		fireTableDataChanged();
		if (data.size() % pageSize == 0)
			pageUp();
	}

	/**
	 * Delete all entries.
	 */
	public void clear() {
		data.clear();
		pageOffset = 0;
		rowCount = 0;
		fireTableDataChanged();
		simView.getRangeLabel().setText(""); //$NON-NLS-1$
	}

	/**
	 * @return the number of pages needed.
	 */
	public int getPageCount() {
		return (int) Math.ceil((double) data.size() / (double) pageSize);
	}

	/**
	 * Use this method if you want to know how big the real table is.
	 * 
	 * @return the size of the underlying model.
	 */
	public int getRealRowCount() {
		return data.size();
	}

	/**
	 * @return the standard page size.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Set a new page size. Not needed here.
	 * 
	 * @param s
	 * @deprecated
	 */
	@Deprecated
	public void setPageSize(int s) {
		if (s == pageSize) {
			return;
		}
		int oldPageSize = pageSize;
		pageSize = s;
		pageOffset = (oldPageSize * pageOffset) / pageSize;
		fireTableDataChanged();
	}

	/**
	 * Update the page offset and fire a data changed (all rows).
	 * 
	 * @return <b>true</b> if a page down was successful, <b>false</b> otherwise
	 */
	public boolean pageDown() {
		if (pageOffset < getPageCount() - 1) {
			pageOffset++;
			rowCount = pageSize;
			if ((pageOffset == getPageCount() - 1)) {
				updateRowCount();
			}
			fireTableDataChanged();
			simView.getRangeLabel().setText(
					Messages.getString("am0c0", "AM0PagingTableModel.7") //$NON-NLS-1$
							+ (getPageOffset() == 0 ? 1
									: (getPageSize() * getPageOffset()))
							+ Messages.getString("am0c0", "AM0PagingTableModel.8") //$NON-NLS-1$
							+ (getPageSize() * (getPageOffset() + 1))
							+ Messages.getString("am0c0", "AM0PagingTableModel.9")); //$NON-NLS-1$
			return true;
		}
		return false;
	}

	/**
	 * Update the page offset and fire a data changed (all rows).
	 * 
	 * @return <b>true</b> if a page up was successful, <b>false</b> otherwise
	 */
	public boolean pageUp() {
		if (pageOffset > 0) {
			pageOffset--;
			rowCount = pageSize;
			fireTableDataChanged();
			simView.getRangeLabel().setText(
					Messages.getString("am0c0", "AM0PagingTableModel.10") //$NON-NLS-1$
							+ (getPageOffset() == 0 ? 1
									: (getPageSize() * getPageOffset()))
							+ Messages.getString("am0c0", "AM0PagingTableModel.11") //$NON-NLS-1$
							+ (getPageSize() * (getPageOffset() + 1))
							+ Messages.getString("am0c0", "AM0PagingTableModel.12")); //$NON-NLS-1$
			return true;
		}
		return false;
	}

	/**
	 * We provide our own version of a {@link JScrollPane} that includes the
	 * page up and page down buttons by default.
	 * 
	 * @param jt
	 *            {@link JTable}
	 * @return a {@link JScrollPane} with our page up/down buttons and the table
	 */
	public static JScrollPane createPagingScrollPaneForTable(JTable jt) {
		JScrollPane jsp = new JScrollPane(jt);
		TableModel tmodel = jt.getModel();

		if (!(tmodel instanceof AM0PagingTableModel)) {
			return jsp;
		}

		final AM0PagingTableModel model = (AM0PagingTableModel) tmodel;
		final JButton upButton = new JButton(new ArrowIcon(ArrowIcon.UP));
		final JButton downButton = new JButton(new ArrowIcon(ArrowIcon.DOWN));

		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				model.pageUp();
			}
		});

		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				model.pageDown();
			}
		});

		// Turn on the scrollbars - otherwise we won't get our corners.
		jsp
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		// Add in the corners (page up/down).
		jsp.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, upButton);
		jsp.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, downButton);

		return jsp;
	}
}

/**
 * Class which represents the corner up/down buttons used in
 * {@link AM0PagingTableModel}.
 * 
 * @author Max Leuth&auml;user
 */
class ArrowIcon implements Icon {

	public static final int UP = 0;

	public static final int DOWN = 1;

	private int direction;

	private Polygon pagePolygon = new Polygon(new int[] { 2, 4, 4, 10, 10, 2 },
			new int[] { 4, 4, 2, 2, 12, 12 }, 6);

	private int[] arrowX = { 4, 9, 6 };

	private Polygon arrowUpPolygon = new Polygon(arrowX,
			new int[] { 10, 10, 4 }, 3);

	private Polygon arrowDownPolygon = new Polygon(arrowX,
			new int[] { 6, 6, 11 }, 3);

	public ArrowIcon(int which) {
		direction = which;
	}

	public int getIconWidth() {
		return 14;
	}

	public int getIconHeight() {
		return 14;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor(Color.black);
		pagePolygon.translate(x, y);
		g.drawPolygon(pagePolygon);
		pagePolygon.translate(-x, -y);
		if (direction == UP) {
			arrowUpPolygon.translate(x, y);
			g.fillPolygon(arrowUpPolygon);
			arrowUpPolygon.translate(-x, -y);
		} else {
			arrowDownPolygon.translate(x, y);
			g.fillPolygon(arrowDownPolygon);
			arrowDownPolygon.translate(-x, -y);
		}
	}
}
