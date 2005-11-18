/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and platform
 * independant. j-Algo is developed with the help of Dresden University of
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

/*
 * Created on 22.05.2005
 * 
 */
package org.jalgo.module.dijkstra.gui;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jalgo.main.util.Messages;
import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.ApplyEdgeListAction;
import org.jalgo.module.dijkstra.model.Edge;
import org.jalgo.module.dijkstra.model.Graph;
import org.jalgo.module.dijkstra.util.DefaultExceptionHandler;

/**
 * this class generate the matrix composite with the textfields and the events
 * of the textfields
 * 
 * @author Steven Voigt, Frank Staudinger
 */
public class MatrixComposite
extends ControllerComposite {

	protected class ApplyMatrixButtonSelectionAdapter
	extends SelectionAdapter {

		private Controller m_ctrl; // the controller of the main modul

		private MatrixComposite m_MatrixGUI; // the MatrixComposite that is

		// shown in the mainGUI

		ApplyMatrixButtonSelectionAdapter(MatrixComposite gui,
			Controller controller) {
			m_ctrl = controller;
			m_MatrixGUI = gui;
		}

		public void widgetSelected(SelectionEvent e) {
			try {
				new ApplyEdgeListAction(m_ctrl, m_MatrixGUI.getEdgeList());
			}
			catch (ActionException exc) {
				new DefaultExceptionHandler(exc);
			}
		}
	}

	protected class TextFocusAdapter
	extends FocusAdapter {

		protected Text[][] m_arTextfields;

		protected int r, s;

		protected boolean minuschanged = false;

		public TextFocusAdapter(Text[][] arTextfields, int r, int s) {
			m_arTextfields = arTextfields;
			this.r = r;
			this.s = s;
		}

		public void focusGained(FocusEvent e) {
			if (m_arTextfields[r][s].getText().equals("-")) { //$NON-NLS-1$

				minuschanged = true;
				m_arTextfields[r][s].setText(""); //$NON-NLS-1$
			}
			else minuschanged = false;
		}

		public void focusLost(FocusEvent e) {
			if (m_arTextfields[r][s].getText().equals("") && minuschanged) { //$NON-NLS-1$
				m_arTextfields[r][s].setText("-"); //$NON-NLS-1$
				m_arTextfields[s][r].setText("-"); //$NON-NLS-1$

			}
			if (m_arTextfields[r][s].getText().length() == 2) {
				m_arTextfields[s][r].setText(
					m_arTextfields[r][s].getText().replace('-', ' '));
				m_arTextfields[r][s].setText(
					m_arTextfields[r][s].getText().replace('-', ' '));
			}
			minuschanged = false;
		}
	}

	protected class MatrixObserver
	implements Observer {

		protected MatrixComposite m_cmp;

		public MatrixObserver(MatrixComposite cmp) {
			m_cmp = cmp;
		}

		public void update(Observable o, Object arg) {
			Controller ctrl = null;
			ctrl = (Controller)o;

			if (ctrl == null) return;
			Graph gr = ctrl.getGraph();
			m_cmp.setGraph(gr);
		}
	}

	protected Text[][] m_arTextfields = new Text[9][9];
	protected Label[] m_arRowLabels = new Label[9];
	protected Label[] m_arColLabels = new Label[9];
	protected Button[] m_arButtons = new Button[1]; // From L2R in GUI
	protected int m_nNodes = -1;

	/**
	 * @author Steven Voigt, Frank Staudinger
	 * @param arg0
	 * @param arg1
	 */
	public MatrixComposite(Controller ctrl, Composite arg0, int arg1) {
		super(ctrl, arg0, arg1);
		setLayout(new FillLayout());

		Composite compo = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 10;
		layout.makeColumnsEqualWidth = true;
		compo.setLayout(layout);
		compo.addMouseTrackListener(new StatusbarTextMouseTrackAdapter(
			getController(), Messages.getString(
				"dijkstra", "MatrixComposite.Tooltip"))); //$NON-NLS-1$ //$NON-NLS-2$

		for (int i = 0; i < layout.numColumns; i++) {
			Label label = new Label(compo, SWT.CENTER);
			if (i > 0) m_arColLabels[i - 1] = label;
			if (i != 0) label.setText("" + (i)); //$NON-NLS-1$
			label.setVisible(true);
			GridData g = new GridData(GridData.FILL_BOTH);
			label.setLayoutData(g);
		}

		for (int i = 0; i < layout.numColumns - 1; i++) {
			createOneRowEditFields(compo, i + 1);
		}
		Label label = new Label(compo, SWT.CENTER);
		label.setText(""); //$NON-NLS-1$
		label.setVisible(true);

		GridData g = new GridData(GridData.FILL_BOTH);
		label.setLayoutData(g);

		Button button = new Button(compo, SWT.PUSH);
		button.setText(Messages.getString("dijkstra", "MatrixComposite.Apply")); //$NON-NLS-1$ //$NON-NLS-2$
		button.addSelectionListener(new ApplyMatrixButtonSelectionAdapter(this,
			this.getController()));
		button.addMouseTrackListener(new StatusbarTextMouseTrackAdapter(
			getController(), Messages.getString(
				"dijkstra", "MatrixComposite.Apply_description"))); //$NON-NLS-1$ //$NON-NLS-2$
		g = new GridData(GridData.HORIZONTAL_ALIGN_END);
		g.horizontalSpan = 9;
		button.setLayoutData(g);
		m_arButtons[0] = button;

		// generate the eventlistener for the textfields of the matrix
		for (int k = 0; k < 9; k++) {
			for (int i = 0; i < 9; i++) {
				final int r = i;
				final int s = k;
				m_arTextfields[r][s].setTextLimit(2);
				m_arTextfields[r][s].setToolTipText("" + (s + 1) + "," + (r + 1)); //$NON-NLS-1$ //$NON-NLS-2$
				m_arTextfields[r][s].addFocusListener(new TextFocusAdapter(
					m_arTextfields, r, s));
				m_arTextfields[r][s].addMouseTrackListener(
					new StatusbarTextMouseTrackAdapter(getController(),
						Messages.getString("dijkstra", "MatrixComposite.Tooltip"))); //$NON-NLS-1$ //$NON-NLS-2$

				m_arTextfields[r][s].addKeyListener(new KeyAdapter() {

					public void keyPressed(KeyEvent e) {

						// On key press, accept digits 0 through 9, and control
						// keys (tab, arrows, delete ...).
						// Do not accept entry of digits if text field already
						// has two characters.
						int ascii = e.character;
						boolean isDigit = (ascii >= 48) && (ascii <= 57);
						boolean textFieldFull = false;// m_arTextfields[r][s].getText().length()
						// >= 2;
						boolean isControl = (ascii == 0) || (ascii == 8)
							|| (ascii == 127);
						e.doit = (isDigit && !textFieldFull) || isControl;
					}

					public void keyReleased(KeyEvent e) {

						// Copy changed text contents from the active field to
						// its symmetric counterpart.
						// fill empty weight of node with a line for unreachable
						int l;
						int f;
						int w;
						m_arTextfields[s][r].setText(m_arTextfields[r][s]
						.getText());

						boolean nottab = (!(e.keyCode == 9));
						boolean notbackspace = (!(e.keyCode == 8));
						boolean notcursorleft = (!(e.keyCode == 16777219));
						boolean notcursorright = (!(e.keyCode == 16777220));
						boolean notcursorup = (!(e.keyCode == 16777217));
						boolean notcursordown = (!(e.keyCode == 16777218));
						boolean notnull = (!(m_arTextfields[s][r].getText()
						.equals(""))); //$NON-NLS-1$
						notbackspace = true;
						if (nottab && notnull && notcursorleft && notcursorup
							&& notcursordown && notcursorright && notbackspace) {
							w = s;
							// System.out.println(""+e.keyCode);
							if (w < r) {
								w = r;
							}
							for (f = 0; f <= w; f++) {
								for (l = 0; l <= f; l++) {
									if (m_arTextfields[f][l].getText().equals(
										"")) { //$NON-NLS-1$
										m_arTextfields[l][f].setText("-"); //$NON-NLS-1$
										m_arTextfields[f][l].setText("-"); //$NON-NLS-1$
									}
								}
							}
						}
						else {
							if (nottab && notcursorleft && notcursorup
								&& notcursordown && notcursorright
								&& notbackspace) {
								m_arTextfields[s][r].setText("-"); //$NON-NLS-1$
								m_arTextfields[r][s].setText("-"); //$NON-NLS-1$
							}
						}
					}

				});
			}
		}

		this.getController().addObserver(new MatrixObserver(this));
	}

	/**
	 * generate the Labels that show the x and y index of the matrix
	 * @author Frank Staudinger, Steven Voigt
	 */
	protected void createOneRowEditFields(Composite compo, int nRowNumber) {
		for (int i = 0; i < 10; i++) {
			if (i == 0) {
				Label label = new Label(compo, SWT.CENTER);
				m_arRowLabels[nRowNumber - 1] = label;
				label.setText("" + nRowNumber); //$NON-NLS-1$
				label.setVisible(true);
				GridData g = new GridData(GridData.FILL_BOTH);
				label.setLayoutData(g);
			}
			else {
				Text text = new Text(compo, SWT.BORDER);
				m_arTextfields[i - 1][nRowNumber - 1] = text;
				if (nRowNumber == i) {
					text.setEditable(false);
					text.setText("0"); //$NON-NLS-1$
				}
				GridData g = new GridData(GridData.FILL_BOTH);
				text.setLayoutData(g);
			}
		}
	}

	/**
	 * @author Steven Voigt set all textfields of the matrix empty
	 */
	protected void clearGraph() {
		for (int l = 0; l < 9; l++) {
			for (int i = 0; i < 9; i++)
				m_arTextfields[i][l].setText(""); //$NON-NLS-1$
			m_arTextfields[l][l].setText("0"); //$NON-NLS-1$
		}
	}

	/**
	 * the setGraph method takes the graph from
	 * @param graph and set the entries for the matrix
	 * 
	 * @author Steven Voigt
	 */
	public void setGraph(Graph graph) {
		clearGraph();
		m_nNodes = graph.getNodeList().size() - 1;
		for (int i = 0; i < graph.getNodeList().size(); i++) {
			m_arRowLabels[i].setVisible(true);
			m_arColLabels[i].setVisible(true);
			for (int l = 0; l <= i; l++) {
				m_arTextfields[i][l].setVisible(true);
				m_arTextfields[i][l].setText("-"); //$NON-NLS-1$
				m_arTextfields[l][i].setVisible(true);
				m_arTextfields[l][i].setText("-"); //$NON-NLS-1$
				m_arTextfields[l][l].setVisible(true);
				m_arTextfields[l][l].setText("0"); //$NON-NLS-1$
			}

		}

		for (Edge edge : graph.getEdgeList()) {
			int i = edge.getStartNodeIndex() - 1;
			int l = edge.getEndNodeIndex() - 1;
			if ((i >= 0) && (i <= 8) && (l >= 0) && (l <= 8)) {
				m_arTextfields[i][l].setText("" + edge.getWeight()); //$NON-NLS-1$
				m_arTextfields[l][i].setText("" + edge.getWeight()); //$NON-NLS-1$
			}
		}
	}

	/**
	 * the getEdgeList() method returns the actual graph() as an EdgeList using
	 * entries of the matrix
	 * @return String with Syntax of EdgeList
	 * 
	 * @author Steven Voigt
	 */
	public String getEdgeList() {
		String edgeList = ""; //$NON-NLS-1$
		for (int m = 0; m < 9; m++) {
			if (nodeExist(m + 1)) {
				for (int n = 0; n < 9; n++) {
					boolean notminus = (!m_arTextfields[n][m].getText().equals(
						"-")); //$NON-NLS-1$
					boolean notnull = (!m_arTextfields[n][m].getText().equals(
						"")); //$NON-NLS-1$
					if (notminus && !(n == m) && notnull) {
						edgeList = edgeList
							+ "(" + (m + 1) + "," + m_arTextfields[n][m].getText() //$NON-NLS-1$ //$NON-NLS-2$
							+ "," + (n + 1) + "),"; //$NON-NLS-1$ //$NON-NLS-2$
					}
					if (!notminus) edgeList = edgeList
						+ "(" + (m + 1) + "," + "1," + (m + 1) + "),"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				}
			}
		}
		if (edgeList.endsWith(",")) //$NON-NLS-1$
		edgeList = edgeList.substring(0, edgeList.length() - 1);
		return edgeList;
	}

	/**
	 * check existing of a node if nodeexist
	 * @return boolean true else false
	 * 
	 * @author Steven Voigt
	 */
	protected boolean nodeExist(int nodeNr) {
		for (int i = 0; i < nodeNr; i++)
			if (m_arTextfields[i][nodeNr - 1].getText().equals("")) //$NON-NLS-1$
			return false;
		return true;
	}
}