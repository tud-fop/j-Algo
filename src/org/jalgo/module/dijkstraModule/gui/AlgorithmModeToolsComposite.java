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
 * Created on 31.05.2005 18:13:57
 *
 */
package org.jalgo.module.dijkstraModule.gui;

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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jalgo.module.dijkstraModule.actions.GotoMacroStepAction;
import org.jalgo.module.dijkstraModule.actions.GotoMicroStepAction;
import org.jalgo.module.dijkstraModule.actions.GotoStepAction;
import org.jalgo.module.dijkstraModule.actions.SetStartNodeAction;
import org.jalgo.module.dijkstraModule.actions.StartAnimationAction;
import org.jalgo.module.dijkstraModule.actions.StopAnimationAction;
import org.jalgo.module.dijkstraModule.model.Node;
import org.jalgo.module.dijkstraModule.util.AlgoModeButtonAniObserver;
import org.jalgo.module.dijkstraModule.util.DefaultExceptionHandler;

/**
 * @author Frank Staudinger
 *
 * This composite provides the tools for the algorithm mode
 */
public class AlgorithmModeToolsComposite extends ControllerComposite {

	protected class ComboBoxObserver extends AlgoModeButtonAniObserver {
		Combo m_cbStartNode;

		public ComboBoxObserver(Combo cbStartNode) {
			super(cbStartNode);
			m_cbStartNode = cbStartNode;
		}

		/* (non-Javadoc)
		 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
		 */
		public void update(Observable arg0, Object arg1) {
			try {
				super.update(arg0, arg1);
				if (((Controller) arg0).getEditingMode() != Controller.MODE_ALGORITHM)
					return;
				int iSelectedIndex = m_cbStartNode.getSelectionIndex();
				int iMaxNode = (((Controller) arg0).getGraph().getNodeList() == null) ? 0 : ((Controller) arg0)
						.getGraph().getNodeList().size();
				m_cbStartNode.removeAll();
				for (int i = 0; i < iMaxNode; i++)
					m_cbStartNode.add("" + (i + 1), i);
				// if no node is selected as startnode, set the selection to node #1 and generate the states
				if (iSelectedIndex == -1) {
					Node node = ((Controller) arg0).getGraph().getStartNode();
					if (node != null) {
						m_cbStartNode.select(node.getIndex() - 1);
					} else {
						m_cbStartNode.select(0);
					}
				} else {
					Node node = ((Controller) arg0).getGraph().getStartNode();
					iSelectedIndex = (node == null) ? iSelectedIndex : (node.getIndex() - 1);
					m_cbStartNode.select(iSelectedIndex);
				}
			} catch (Exception exc) {
				new DefaultExceptionHandler(exc);
			}
		}
	}

	protected class TextBoxObserver extends AlgoModeButtonAniObserver {
		Text m_edit;

		Label m_label;

		public TextBoxObserver(Text textbox, Label label) {
			super(textbox);
			m_edit = textbox;
			m_label = label;
		}

		/* (non-Javadoc)
		 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
		 */
		public void update(Observable arg0, Object arg1) {
			super.update(arg0, arg1);
			if (((Controller) arg0).getEditingMode() != Controller.MODE_ALGORITHM)
				return;

			if ((((Controller) arg0).getCurrentStep() + 1) > 0)
				m_edit.setText("" + (((Controller) arg0).getCurrentStep() + 1));
			else
				m_edit.setText("");

			if ((((Controller) arg0).getStepCount()) > 0)
				m_label.setText("von " + (((Controller) arg0).getStepCount()));
			else
				m_label.setText(" ");

		}
	}

	protected class ButtonObserver extends AlgoModeButtonAniObserver {
		Button m_btnTarget;

		boolean m_bNext;

		boolean m_bMacroStep;

		public ButtonObserver(Button btnTarget, boolean bNext, boolean bMacroStep) {
			super(btnTarget);
			m_btnTarget = btnTarget;
			m_bNext = bNext;
			m_bMacroStep = bMacroStep;
		}

		public void update(Observable arg0, Object arg1) {
			super.update(arg0, arg1);
			if (((Controller) arg0).getEditingMode() != Controller.MODE_ALGORITHM)
				return;
			Controller ctrl = (Controller) arg0;

			boolean bEnable = false;

			if (m_bMacroStep == true) {
				bEnable = ((m_bNext == true) ? ctrl.hasNextMacroStep(ctrl.getCurrentStep()) : ctrl
						.hasPrevMacroStep(ctrl.getCurrentStep()));
			} else {
				bEnable = ((m_bNext == true) ? ctrl.hasNextStep(ctrl.getCurrentStep()) : ctrl.hasPrevStep(ctrl
						.getCurrentStep()));
			}
			if (ctrl.getAnimationMillis() > 0 && ctrl.getEditingMode() == Controller.MODE_ALGORITHM)
				bEnable = false;
			m_btnTarget.setEnabled(bEnable);
		}
	}

	protected class AniButtonObserver implements Observer {
		Button m_btnTarget;

		public AniButtonObserver(Button btnTarget) {
			m_btnTarget = btnTarget;
		}

		public void update(Observable arg0, Object arg1) {

			if (((Controller) arg0).getEditingMode() != Controller.MODE_ALGORITHM)
				return;
			Controller ctrl = (Controller) arg0;
			m_btnTarget.setSelection(ctrl.getAnimationMillis() > 0);
		}
	}

	protected class GotoButtonObserver extends AlgoModeButtonAniObserver {
		protected Text m_txt;

		public GotoButtonObserver(Control control, Text txt) {
			super(control);
			m_txt = txt;
		}

		public void update(Observable arg0, Object arg1) {
			super.update(arg0, arg1);
			if (m_control.getEnabled() == true) {
				m_control.setEnabled(m_txt.getText().length() > 0);
			}
		}
	}

	protected class ButtonSelectionAdapter extends SelectionAdapter {
		Controller m_Ctrl;

		boolean m_bNext;

		boolean m_bMacroStep;

		ButtonSelectionAdapter(Controller Ctrl, boolean bNext, boolean bMacroStep) {
			m_Ctrl = Ctrl;
			m_bNext = bNext;
			m_bMacroStep = bMacroStep;
		}

		public void widgetSelected(SelectionEvent e) {
			try {
				if (m_bMacroStep == true) {
					new GotoMacroStepAction(m_Ctrl, m_bNext);
				} else {
					new GotoMicroStepAction(m_Ctrl, m_bNext);
				}
			} catch (Exception exc) {
				new DefaultExceptionHandler(exc);
			}
		}
	}

	protected class GotoButtonSelectionAdapter extends SelectionAdapter {
		Controller m_Ctrl;

		Text m_edit;

		GotoButtonSelectionAdapter(Controller Ctrl, Text edit) {
			m_Ctrl = Ctrl;
			m_edit = edit;
		}

		public void widgetSelected(SelectionEvent e) {
			try {
				new GotoStepAction(m_Ctrl, Integer.valueOf(m_edit.getText()).intValue() - 1, true);

			} catch (Exception exc) {
				new DefaultExceptionHandler(exc);
			}
		}
	}

	protected class ComboSelectionAdapter extends SelectionAdapter {
		Controller m_Ctrl;

		ComboSelectionAdapter(Controller Ctrl) {
			m_Ctrl = Ctrl;
		}

		public void widgetSelected(SelectionEvent e) {
			try {
				Combo cb = (Combo) e.widget;
				int iSelIndex = cb.getSelectionIndex();
				new SetStartNodeAction(m_Ctrl, iSelIndex + 1);
			} catch (Exception exc) {
				new DefaultExceptionHandler(exc);
			}
		}
	}

	protected class GotoTextFieldKeyAdapter extends KeyAdapter {
		Button m_btn;

		public GotoTextFieldKeyAdapter(Button btn) {
			m_btn = btn;
		}

		public void keyPressed(KeyEvent e) {
			// On key press, accept digits 0 through 9, and control keys (tab, arrows, delete ...).
			// Do not accept entry of digits if text field already has two characters.
			int ascii = e.character;
			boolean isDigit = (ascii >= 48) && (ascii <= 57);
			boolean isControl = (ascii == 0) || (ascii == 8) || (ascii == 127);
			e.doit = (isDigit) || isControl;
		}

		public void keyReleased(KeyEvent e) {
			boolean bEnabled = ((Text) e.widget).getText().length() > 0;
			m_btn.setEnabled(bEnabled);
		}

	}

	protected class AniButtonSelectionAdapter extends SelectionAdapter {
		Controller m_Ctrl;

		AniButtonSelectionAdapter(Controller Ctrl) {
			m_Ctrl = Ctrl;

		}

		public void widgetSelected(SelectionEvent e) {
			try {
				Button b = (Button) e.widget;
				if (b.getSelection() == true) {
					new StartAnimationAction(m_Ctrl, 750);
				} else {
					new StopAnimationAction(m_Ctrl);
				}
			} catch (Exception exc) {
				new DefaultExceptionHandler(exc);
			}
		}
	}

	/**
	 * @param ctrl Current Controller for this composite
	 * @param cmpParent parent Composite
	 * @param nStyle Style for this window
	 */
	public AlgorithmModeToolsComposite(Controller ctrl, Composite cmpParent, int nStyle) {
		super(ctrl, cmpParent, nStyle);
		GridLayout l = new GridLayout();
		l.verticalSpacing = 5;
		l.marginHeight = 5;
		l.marginWidth = 5;
		l.numColumns = 11;
		l.makeColumnsEqualWidth = false;
		this.setLayout(l);

		Label label = new Label(this, SWT.LEFT);
		label.setText("Startknoten:");
		label.setLayoutData(new GridData(GridData.END | GridData.VERTICAL_ALIGN_CENTER));

		Combo cb = new Combo(this, SWT.READ_ONLY | SWT.DROP_DOWN);
		cb.setLayoutData(new GridData(GridData.CENTER | GridData.VERTICAL_ALIGN_CENTER));
		cb.addSelectionListener(new ComboSelectionAdapter(getController()));
		getController().addObserver(new ComboBoxObserver(cb));

		label = new Label(this, SWT.LEFT);
		label.setText("Algorithmusschritt:");
		label.setLayoutData(new GridData(GridData.END | GridData.VERTICAL_ALIGN_CENTER));

		Text textbox = new Text(this, SWT.BORDER);
		textbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));

		label = new Label(this, SWT.LEFT);
		label.setText("          ");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		getController().addObserver(new TextBoxObserver(textbox, label));

		Button b = new Button(this, SWT.CENTER);
		b.setText("Gehe zu");
		b.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		b.addSelectionListener(new GotoButtonSelectionAdapter(getController(), textbox));
		b.setEnabled(false);
		textbox.addKeyListener(new GotoTextFieldKeyAdapter(b));
		getController().addObserver(new GotoButtonObserver(b, textbox));

		b = new Button(this, SWT.CENTER);
		b.setText("<<");
		b.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		b.addSelectionListener(new ButtonSelectionAdapter(getController(), false, true));
		getController().addObserver(new ButtonObserver(b, false, true));

		b = new Button(this, SWT.CENTER);
		b.setText("<");
		b.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		b.addSelectionListener(new ButtonSelectionAdapter(getController(), false, false));
		getController().addObserver(new ButtonObserver(b, false, false));

		b = new Button(this, SWT.CENTER);
		b.setText(">");
		b.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		b.addSelectionListener(new ButtonSelectionAdapter(getController(), true, false));
		getController().addObserver(new ButtonObserver(b, true, false));

		b = new Button(this, SWT.CENTER);
		b.setText(">>");
		b.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		b.addSelectionListener(new ButtonSelectionAdapter(getController(), true, true));
		getController().addObserver(new ButtonObserver(b, true, true));

		b = new Button(this, SWT.CENTER | SWT.TOGGLE);
		b.setText("Animation starten");
		b.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		b.addSelectionListener(new AniButtonSelectionAdapter(getController()));
		getController().addObserver(new AniButtonObserver(b));
		/*
		 try {
		 BrowserLauncher.openURL("http://browserlauncher.sourceforge.net/");
		 } catch (IOException e) {
		 
		 e.printStackTrace();
		 }
		 */
	}

}
