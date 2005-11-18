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
 * Created on 15.05.2005
 * 
 */
package org.jalgo.module.dijkstra.gui;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.ApplyEdgeListAction;
import org.jalgo.module.dijkstra.model.Graph;
import org.jalgo.module.dijkstra.util.DefaultExceptionHandler;

/**
 * @author Frank Staudinger The EdgeListComposite provides a textfield an a
 *         button to edit the edgelist for the graph
 */
public class EdgeListComposite
extends ControllerComposite {

	protected class ApplyEdgeListSelectionAdapter
	extends SelectionAdapter {

		private Controller m_Ctrl;
		private Text m_textEdgeList;

		ApplyEdgeListSelectionAdapter(Controller ctrl, Text t) {
			super();
			m_Ctrl = ctrl;
			this.m_textEdgeList = t;
		}

		public void widgetSelected(SelectionEvent e) {
			String strText = this.m_textEdgeList.getText();
			try {
				new ApplyEdgeListAction(m_Ctrl, strText);
			}
			catch (ActionException exc) {
				new DefaultExceptionHandler(exc);
				this.m_textEdgeList.setText(strText);
			}
		}
	}

	protected class EdgeListObserver
	implements Observer {

		private Text m_textEdgeList;

		public EdgeListObserver(Text t) {
			m_textEdgeList = t;
		}

		public void update(Observable o, Object arg) {
			Controller ctrl = (Controller)o;
			if (ctrl == null) return;
			Graph gr = ctrl.getGraph();
			m_textEdgeList.setText(gr.getEdgeListText());
		}
	}

	private Text m_t;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.module.dijkstra.gui.ControllerComposite
	 */
	public EdgeListComposite(Controller ctrl, Composite cmpParent, int nStyle) {
		super(ctrl, cmpParent, nStyle);
		GridLayout l = new GridLayout();
		l.verticalSpacing = 5;
		l.marginHeight = 5;
		l.marginWidth = 5;
		l.numColumns = 2;

		super.setLayout(l);

		m_t = new Text(this, SWT.BORDER | SWT.MULTI | SWT.WRAP);

		m_t.setLayoutData(new GridData(GridData.FILL_BOTH));
		m_t.addMouseTrackListener(new StatusbarTextMouseTrackAdapter(
			getController(), Messages.getString(
				"dijkstra", "EdgeListComposite.Tooltip"))); //$NON-NLS-1$ //$NON-NLS-2$
		m_t.addKeyListener(new KeyAdapter() {
			private int oldKey1;
			private int oldKey2;

			public void keyPressed(KeyEvent e) {
				if ((e.keyCode == (115)) && (oldKey1 == SWT.ALT)
					&& (oldKey2 == SWT.CTRL)
					&& (m_t.getText().equals("showcredits"))) //$NON-NLS-1$
				JAlgoGUIConnector.getInstance().showInfoMessage(
					"jAlgo Dijkstra Module\n\nDeveloped by:\n\nFrank Staudinger\nJulian Stecklina\nMartin Winter\nHannes Straß\nSteven Voigt\n\n(C) 2005."); //$NON-NLS-1$
				oldKey2 = oldKey1;
				oldKey1 = e.keyCode;
			}
		});

		Button b = new Button(this, SWT.CENTER);
		b.setText(Messages.getString("dijkstra", "EdgeListComposite.Apply")); //$NON-NLS-1$ //$NON-NLS-2$
		b.addSelectionListener(new ApplyEdgeListSelectionAdapter(
			this.getController(), m_t));
		b.addMouseTrackListener(new StatusbarTextMouseTrackAdapter(
			getController(), Messages.getString(
				"dijkstra", "EdgeListComposite.Apply_description"))); //$NON-NLS-1$ //$NON-NLS-2$
		b.setLayoutData(new GridData(GridData.END));
		getController().addObserver(new EdgeListObserver(m_t));
	}
}