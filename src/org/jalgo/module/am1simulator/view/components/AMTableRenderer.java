/**
 * AM1 Simulator - simulating am1 code in an abstract machine based on the
 * definitions of the lectures 'Programmierung' at TU Dresden.
 * Copyright (C) 2010 Max Leuthäuser
 * Contact: s7060241@mail.zih.tu-dresden.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jalgo.module.am1simulator.view.components;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Custom table renderer to show rows in different colors.
 * 
 * @author Max Leuth&auml;user
 */
class AMTableRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	/**
	 * Standard grey for our am1 table
	 */
	public static final Color TABLE_ODD = new Color(237, 237, 237);
	/**
	 * Standard white for our am1 table
	 */
	public static final Color TABLE_EVEN = new Color(255, 255, 255);
	private final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component cell;
		cell = DEFAULT_RENDERER.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		cell.setForeground(Color.BLACK);

		if (!isSelected) {
			if (row % 2 == 0) {
				cell.setBackground(TABLE_EVEN);
			} else {
				cell.setBackground(TABLE_ODD);
			}
		} else {
			if (row % 2 == 0) {
				cell.setBackground(Color.YELLOW);
			} else {
				cell.setBackground(Color.ORANGE);
			}
		}

		if (row == table.getRowCount() - 1) {
			cell.setBackground(Color.ORANGE);
		}

		if (column == 0) {
			DEFAULT_RENDERER.setHorizontalAlignment(SwingConstants.CENTER);
			return cell;
		} else {
			DEFAULT_RENDERER.setHorizontalAlignment(SwingConstants.LEFT);
		}
		if (column == 1) {
			DEFAULT_RENDERER.setHorizontalAlignment(SwingConstants.RIGHT);
			return cell;
		} else {
			DEFAULT_RENDERER.setHorizontalAlignment(SwingConstants.LEFT);
		}
		return cell;
	}
}
