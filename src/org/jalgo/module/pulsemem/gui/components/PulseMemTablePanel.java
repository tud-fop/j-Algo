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

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.jalgo.module.pulsemem.Admin;
import org.jalgo.module.pulsemem.core.exceptions.*;
import org.jalgo.main.util.Messages;
import org.jalgo.module.pulsemem.core.*;
import org.jalgo.module.pulsemem.gui.*;

/**
 * PulseMemTablePanel.java
 *
 * @version $Revision: 1.46 $
 * @author Karsten Diepelt
 */
public class PulseMemTablePanel extends JPanel {

	private static final long serialVersionUID = 4932273813663983244L;

	private PulseMemTable pmt;

	private List<PulsMemLine> pml;

	private InlineBreakpoint lineOfBreakpoint;

	private GUIController gui;

	public PulseMemTablePanel() {
		super(new BorderLayout());
		lineOfBreakpoint = new InlineBreakpoint();
		pmt = new PulseMemTable();
		this.add(pmt, BorderLayout.CENTER);
		this.repaint();
	}

	/**
	 * Gets and formates the name of the variable.
	 * @param line The line which is handled at the moment
	 * @param j Index of the variable.
	 * @return The formatted name of the variable.
	 */
	private String getFormattedVariableName(PulsMemLine line, int j) {
		String name;
		boolean isVisibleVar = line.getVisibleStack().indexOf(line.getStack().get(j)) != -1;
		 
		name = (isVisibleVar ? "<b>" : "");
		name += line.getStack().get(j).getName();
		name += (isVisibleVar ? "</b>" : "");
		return name;
	}
	
	/**
	 * Returns the PulseMemTable
	 *
	 * @return
	 */
	public PulseMemTable getPulseMemTable() {
		return pmt;
	}

	public void setTableData(List<PulsMemLine> pmlList) {
		this.pml = pmlList;
		updateTable();
	}

	/**
	 * Sets the line number, up to which the PulsMem is shown
	 * Used for loading.
	 *
	 * @param line
	 */
	public void setVisibleLines(int line) {
		this.pmt.setVisibleLine(line);
	}

	/**
	 * shows an empty PulseMemTable
	 *
	 */
	public void showEmptyTable() {
		pmt.resetTable();
		this.pml = null;
	}

	/**
	 * This function updates the whole table data. It analyzes the list of
	 * PulseMemLine and the InlineBreakpoint object to re-create all table data.
	 * (The PulseMemTable itself just display data, this panel selects the data
	 * for the table to display.)
	 */
	public void updateTable() {
		if (this.pml != null) {
			List<PulsMemLine> pmlVis = getVisiblePML(this.pml);
			int numberOfRows = pmlVis.size();
			int numberOfColumns = 0;
			int numberOfGlobalVars = 0;
			boolean addDummy = false;
			// find the longest line:
			for (int i = 0; i < numberOfRows; i++) {
				numberOfColumns = Math.max(pmlVis.get(i).getStack().size(),
						numberOfColumns);
			}
			// find out the number of global variables,
			for (int i = 0; i < numberOfRows; i++) {
				PulsMemLine line = pmlVis.get(i);
				numberOfGlobalVars = Math.max(checkNumberOfGlobalVars(line),
						numberOfGlobalVars);
			}
			// find out if adding a dummy is necessary
			addDummy = (numberOfColumns == numberOfGlobalVars);

			numberOfColumns += 2;
			String columns[];
			if (addDummy) {
				columns = new String[numberOfColumns + 1];
				columns[numberOfColumns] = " - ";
			} else {
				columns = new String[numberOfColumns];
			}

			columns[0] = Messages.getString(
					"pulsemem", "PulseMemTablePanel.LabelLine"); //$NON-NLS-1$
			columns[1] = Messages.getString(
					"pulsemem", "PulseMemTablePanel.Stack"); //$NON-NLS-1$
			for (int i = 2; i < numberOfColumns; i++) {
				columns[i] = Integer.toString(i - 1);
			}
			CellObject data[][];
			if (addDummy) {
				data = new CellObject[numberOfRows + 1][numberOfColumns + 1];
				data[numberOfRows][numberOfColumns] = new CellObject(" - ");
			} else {
				data = new CellObject[numberOfRows + 1][numberOfColumns];
			}

			// get data for each row

			for (int i = 0; i < numberOfRows; i++) {
				try {
					PulsMemLine line = pmlVis.get(i);
					numberOfGlobalVars = Math.max(
							checkNumberOfGlobalVars(line), numberOfGlobalVars);
					// find out if line has a label
					if (line.isLabel()) {
						data[i][0] = new CellObject(Messages.getString(
								"pulsemem", "PulseMemTable.Line")
								+ " " + Integer.toString(line.getCodeLine()),
								Messages.getString("pulsemem",
										"PulseMemTable.Label")
										+ " "
										+ Integer.toString(line.getLabel()));
					} else {
						data[i][0] = new CellObject(Messages.getString(
								"pulsemem", "PulseMemTable.Line")
								+ " " + Integer.toString(line.getCodeLine()));
					}
					// reverse stack string
					String rm = "] ";
					int size = line.getRuecksprungMarken().size();
					for (int j = 0; j < size; j++) {
						if (j == 0) {
							rm = Integer.toString(line.getRuecksprungMarken()
									.get(j))
									+ rm;
						} else {
							rm = Integer.toString(line.getRuecksprungMarken()
									.get(j))
									+ "," + rm;
						}
					}
					rm = "[" + rm;
					data[i][1] = new CellObject(rm, SwingConstants.RIGHT);
					// get data for variable of the line
					for (int j = 0; j < line.getStack().size(); j++) {
						String name;
						String value = "?"; //$NON-NLS-1$

						if (line.getStack().get(j).getName() == null) {
							name = "?"; //$NON-NLS-1$
						} else {
							if (line.getStack().get(j) instanceof Zeiger) {
								Zeiger tmp = (Zeiger) line.getStack().get(j);

								name = "<i>" + tmp.getName() + "</i>";
								// name = tmp.toString();
								try {
									value = "#"
											+ Integer
													.toString(line
															.getStack()
															.indexOf(
																	tmp
																			.getTarget()) + 1);
								} catch (EMemoryError eme) {

								}
							} else {
								
								name = getFormattedVariableName(line, j);
									
								try {
									if (line.getStack().get(j).getValue() != null) {
										value = line.getStack().get(j).getValue().toString();
									}
								} catch (Exception e) {

								}

							}
						}

						data[i][j + 2] = new CellObject("<center>" + name,
								value + "</center>");

					}

					if (addDummy) {
						data[i][numberOfColumns] = new CellObject(" - ");
					}
				} catch (OutOfMemoryError t) {
					System.gc();
					data = null;
					this.pml = null;
					this.updateTable();
					System.gc();
					System.out.println("OutOfMemoryError");
					gui.switchParseStopEnabled(GUIConstants.PARSE_DISABLED);
					gui.showErrorMessage(Admin.getLanguageString("gui.OutOfMemory"));
					return;

				}
			}
			for (int i = 0; i < numberOfColumns; i++) {
				data[numberOfRows][i] = new CellObject("-----");
			}
			pmt.setData(columns, data, numberOfGlobalVars);
		}
	}

	/**
	 * Sets the InlineBreakpoint object for the table.
	 *
	 * @param lob
	 */
	public void setBreakpoints(InlineBreakpoint lob) {
		this.lineOfBreakpoint = lob;
		updateTable();
	}

	/**
	 * This function checks the number global variables in a PulsMemLine
	 *
	 * @param line
	 * @return
	 */
	private int checkNumberOfGlobalVars(PulsMemLine line) {
		int anz = 0;
		for (int i = 0; i < line.getStack().size(); i++) {
			if (line.getStack().get(i).isGlobal()) {
				anz++;
			}
		}
		return anz;
	}

	/*
	 * private String getPointerString(Object o) { String out = ""; if (o
	 * instanceof Zeiger) { Zeiger z = (Zeiger) o; out = out + " => " +
	 * z.getName() + "(" + Integer.toString(z.getVisibilityLevel() + 1) + ")" +
	 * getPointerString(z.getValue()); } else { if (o instanceof Variable) {
	 * Variable v = (Variable) o; out = out + " => " + v.getName() + "(" +
	 * Integer.toString(v.getVisibilityLevel() + 1) + ")"; } }
	 *
	 * return out; }
	 */

	/**
	 * Returns the list of visible PulseMemLines, depending on the breakpoints
	 * set by the user and the labels in the source code.
	 *
	 * @param allPML
	 * @return
	 */
	public List<PulsMemLine> getVisiblePML(List<PulsMemLine> allPML) {
		List<PulsMemLine> visPML = new ArrayList<PulsMemLine>();
		if (allPML!=null) {
			for (int i = 0; i < allPML.size(); i++) {
				if (this.lineOfBreakpoint.containsLine(allPML.get(i).getCodeLine())) {
					visPML.add(allPML.get(i));
				}
			}
		}

		return visPML;
	}

	/**
	 * @return the pml
	 */
	public List<PulsMemLine> getVisPml() {
		return getVisiblePML(this.pml);
	}

	/**
	 * sets the beamer mode
	 *
	 * @param beamer
	 * 			-true: enable beamer mode
	 */
	public void setBeamerMode(boolean beamer) {
		if (beamer) {
			pmt.enableBeamer();
		} else {
			pmt.disableBeamer();
		}
	}

	public void setGUI (GUIController gui) {
		this.gui = gui;
	}
}
