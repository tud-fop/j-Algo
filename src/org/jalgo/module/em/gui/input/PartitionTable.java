package org.jalgo.module.em.gui.input;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jalgo.module.em.data.Event;

/**
 * Table for customizing a partition. Offers a specified behavior for clicking
 * dragging. Disables events which are already yielded to a partition.
 * 
 * @author Tobias Nett
 * 
 */
public class PartitionTable extends JTable {

	private static final long serialVersionUID = 1559624150462345709L;

	private Set<Point> selected;
	private boolean dragging;

	/**
	 * Creates a new empty Table with no data.
	 */
	public PartitionTable() {
		initTable();
	}

	/**
	 * Creates a new table with the specified data.
	 * <p>
	 * Generates a <code>PartitionTableModel</code> out of the given data.
	 * 
	 * @param rowData
	 *            the row data, namely different events
	 * @param columnNames
	 *            the column names for this table
	 */
	public PartitionTable(Object[][] rowData, Object[] columnNames) {
		// TODO create PartitionTableModel from data
		initTable();
	}

	/**
	 * Creates a new table with the specified table model.
	 * <p>
	 * The model must be a <code>PartitionTableModel</code> which consists of
	 * the experiment's events.
	 * 
	 * @param model
	 *            TableModel containing the experiments events
	 */
	public PartitionTable(PartitionTableModel model) {
		super(model);
		initTable();
	}

	/**
	 * Returns a set of the selected events that are presented in the table
	 * cells. If no cells are selected, the returned set is empty.
	 * 
	 * @return set of selected events - empty set if no cell is selected
	 */
	public Set<Event> getSelecetedEvents() {
		TreeSet<Event> s = new TreeSet<Event>();
		for (Point p : selected) {
			s.add((Event) this.getModel().getValueAt(p.x, p.y));
		}
		return s;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		final Container viewport = getParent();
		return viewport.getWidth() > getMinimumSize().getWidth();
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		final Container viewport = getParent();
		return viewport.getHeight() > getMinimumSize().getHeight();
	}

	@Override
	public void clearSelection() {
		if (selected != null)
			selected.clear();
	}

	/**
	 * Returns true if the specified <code>Event</code> is not yielded to a
	 * partition. If the specified <code>Event</code> is already part of a
	 * partition, false is returned.
	 * 
	 * @param row
	 *            the row whose cell is queried
	 * @param column
	 *            the column whose cell is queried
	 * @return true if the cell's event is not yielded to a partition, false
	 *         else
	 */
	@Override
	public boolean isCellSelected(final int row, final int column) {
		Point p = new Point(row, column);
		if (((Event) this.getModel().getValueAt(row, column)).getYield() != null)
			return false;
		if (selected.contains(p))
			return true;
		if (dragging) {
			return super.isCellSelected(row, column);
		}
		return false;
	}

	/**
	 * Returns false for every cell. The user cannot edit the cells' values.
	 * 
	 * @param row
	 *            the row whose cell is queried
	 * @param column
	 *            the column whose cell is queried
	 * @return false - no cell is editable
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		// return !(parted.contains(new Point(row, column)));
		return false;
	}

	/**
	 * Initializes the table with a new <code>CellRenderer</code>.
	 */
	private void initTable() {
		selected = new HashSet<Point>();

		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {

				if (table.getModel().getValueAt(row, column) instanceof Event) {
					Event e = (Event) table.getModel().getValueAt(row, column);
					if (e.getYield() != null) {
						isSelected = false;
						hasFocus = false;
					}
				}
				Component c = super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, column);

				if (c instanceof JLabel) {
					((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
				}

				if (((Event) table.getModel().getValueAt(row, column))
						.getYield() == null) {
					c.setBackground(Color.WHITE);
					c.setForeground(Color.BLACK);
					if (isSelected)
						c.setBackground(new Color(0x40, 0xFF, 0x40));
				} else {
					c.setBackground(Color.LIGHT_GRAY);
					c.setForeground(Color.GRAY);
				}
				return c;
			}
		});

		MouseAdapter mouseHandler = new TableMouseAdapter();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);
		this.setRowSelectionAllowed(false);
		this.setRowSelectionAllowed(false);
		this.setCellSelectionEnabled(true);
	}

	private class TableMouseAdapter extends MouseAdapter {
		
		@Override
		public void mousePressed(final MouseEvent me) { 
			if (SwingUtilities.isLeftMouseButton(me)) {
				Point p = me.getPoint();
				int row = rowAtPoint(p);
				int col = columnAtPoint(p);
				Point cell = new Point(row, col);
				if (getValueAt(row, col) instanceof Event) {
					if (((Event) getValueAt(row, col)).getYield() == null) {
						if (selected.contains(cell)) {
							selected.remove(cell);
						} else {
							selected.add(cell);
						}
						((DefaultTableModel)getModel()).fireTableCellUpdated(row, col);
					}
				}
			} else if (SwingUtilities.isRightMouseButton(me)) {
				selected.clear();
				((PartitionTableModel) getModel()).fireTableDataChanged();
			}
		}

		@Override
		public void mouseDragged(final MouseEvent me) {
			if (!me.isControlDown()) { 
				//selected.clear();
				dragging = true;
				me.consume();
			}
		}

		@Override
		public void mouseReleased(final MouseEvent me) { 
			if (dragging) {				
				completeSelections();
				dragging = false;
			}
		}

		private void completeSelections() {
			int[] rows = getSelectedRows();
			int[] cols = getSelectedColumns();
			Set<Point> tmp = new HashSet<Point>();
			for (int row : rows) {
				for (int col : cols) {
					if (((Event) getValueAt(row, col)).getYield() == null) {
						tmp.add(new Point(row, col));
					}
				}
			}
			selected.addAll(tmp);			
		}
	}

	/**
	 * Searches for all events that are currently not partitioned (the event's
	 * yield is <code>null</code>). Returns an empty set if all events are
	 * partitioned.
	 * 
	 * @return set of all unpartitioned events, empty set if no elements were
	 *         found
	 */
	public Set<Event> getUnpartitionedEvents() {
		Set<Event> unpartitioned = new TreeSet<Event>();
		for (int r = 0; r < this.getRowCount(); r++) {
			for (int c = 0; c < this.getColumnCount(); c++) {
				Event e = (Event) this.getValueAt(r, c);
				if (e.getYield() == null) {
					unpartitioned.add(e);
				}
			}
		}
		return unpartitioned;
	}
}