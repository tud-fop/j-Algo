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
package org.jalgo.module.am0c0.gui;

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
				cell.setBackground(GuiConstants.TABLE_EVEN);
			} else {
				cell.setBackground(GuiConstants.TABLE_ODD);
			}
		} else {
			if (row % 2 == 0) {
				cell.setBackground(Color.YELLOW);
			} else {
				cell.setBackground(Color.ORANGE);
			}
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
			//cell.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		} else {
			DEFAULT_RENDERER.setHorizontalAlignment(SwingConstants.LEFT);
			//cell.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		}
		return cell;
	}
}
