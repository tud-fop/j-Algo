/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/**
 * @author Frank Staudinger
 * 
 * The NodeListComposite provides a textfield an a button
 *  to edit the nodelist for the graph
 *
 */
package org.jalgo.module.dijkstra.gui;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.ApplyNodeListAction;
import org.jalgo.module.dijkstra.model.Graph;
import org.jalgo.module.dijkstra.util.DefaultExceptionHandler;

public class NodeListComposite extends ControllerComposite {
	protected class ApplyNodeListSelectionAdapter extends SelectionAdapter {
		private Controller m_Ctrl;

		private Text m_textNodeList;

		ApplyNodeListSelectionAdapter(Controller ctrl, Text t) {
			super();
			m_Ctrl = ctrl;
			this.m_textNodeList = t;
		}

		public void widgetSelected(SelectionEvent e) {
			String strText = this.m_textNodeList.getText();
			try {
				new ApplyNodeListAction(m_Ctrl, strText);
			} catch (ActionException exc) {
				new DefaultExceptionHandler(exc);
				this.m_textNodeList.setText(strText);
			}
		}
	}

	protected class NodeListObserver implements Observer {
		private Text m_textNodeList;

		public NodeListObserver(Text t) {
			m_textNodeList = t;
		}

		public void update(Observable o, Object arg) {
			Controller ctrl = null;
			ctrl = (Controller) o;
			if (ctrl == null)
				return;
			Graph gr = ctrl.getGraph();
			m_textNodeList.setText(gr.getNodeListText());
		}
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.gui.ControllerComposite
	 */
	public NodeListComposite(Controller ctrl, Composite arg0, int arg1) {
		super(ctrl, arg0, arg1);
		GridLayout l = new GridLayout();
		l.verticalSpacing = 5;
		l.marginHeight = 5;
		l.marginWidth = 5;
		l.numColumns = 2;
		setLayout(l);

		Text t = new Text(this, SWT.BORDER);

		t.setTextLimit(100);
		t.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		t.addMouseTrackListener(new StatusbarTextMouseTrackAdapter(getController(),
				"Textfeld zur Eingabe der durch Komma getrennten Knotenliste. Beispiel: 1, 2, 3, 4"));

		Button b = new Button(this, SWT.CENTER);
		b.setText("Anwenden");
		b.addSelectionListener(new ApplyNodeListSelectionAdapter(this.getController(), t));
		b.addMouseTrackListener(new StatusbarTextMouseTrackAdapter(getController(),
				"\u00C4nderungen an der Knotenliste \u00FCbernehmen und Graph neu darstellen."));

		b.setLayoutData(new GridData(GridData.END));
		getController().addObserver(new NodeListObserver(t));

	}

}
