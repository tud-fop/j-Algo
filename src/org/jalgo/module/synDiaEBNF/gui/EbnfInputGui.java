/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004 j-Algo-Team, j-algo-development@lists.sourceforge.net
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

/*
 * Created on 03.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.jalgo.main.gui.widgets.CustomViewForm;
import org.jalgo.main.gui.widgets.Splitter;
import org.jalgo.module.synDiaEBNF.ModuleController;
import org.jalgo.module.synDiaEBNF.ebnf.EbnfDefinition;
import org.jalgo.module.synDiaEBNF.ebnf.EbnfParseException;
import org.jalgo.module.synDiaEBNF.ebnf.EbnfParser;

/**
 * @author Michael Pradel
 * @author Christopher Friedrich
 */
public class EbnfInputGui extends Gui implements Serializable {

	private Composite mainComp;
	private Composite rulesOuterContainer;
	private Composite rulesInnerContainer;

	private Text variablesSet;
	private Text terminalSymbolsSet;
	private Text startVar;
	private ArrayList variables;
	// list of text (String) with variables in rule-lines
	private ArrayList terms;
	// list of text (String) with terms in rule-lines
	private int termsIter = 0;

	public EbnfInputGui(Composite parent, ModuleController mc) {

		super(parent);

		final ModuleController modcon = mc;

		Splitter sash = new Splitter(parent, SWT.HORIZONTAL);
		CustomViewForm form = new CustomViewForm(sash, SWT.BORDER);
		form.setText(Messages.getString("EbnfInputGui.Ebnf_Input_1")); //$NON-NLS-1$
		form.setImage(new Image(parent.getDisplay(), "./pix/new.gif")); //$NON-NLS-1$

		mainComp = new Composite(form, SWT.FLAT);

		form.setContent(mainComp);

		GridLayout gridLayout = new GridLayout(3, false);
		mainComp.setLayout(gridLayout);

		variables = new ArrayList();
		terms = new ArrayList();

		// Row 1

		GridData data1_1 = new GridData();
		Label label1 = new Label(mainComp, SWT.NULL);
		label1.setText(Messages.getString("EbnfInputGui.Variables_3")); //$NON-NLS-1$
		label1.setLayoutData(data1_1);

		GridData data1_2 = new GridData(GridData.FILL_HORIZONTAL);
		Text text1 = new Text(mainComp, SWT.BORDER);
		text1.setToolTipText(
			Messages.getString("EbnfInputGui.Set_of_SynVars_4") + Messages.getString("EbnfInputGui.Colon_separated_5")); //$NON-NLS-1$ //$NON-NLS-2$
		text1.setLayoutData(data1_2);

		GridData data1_3 = new GridData();
		Label label1b = new Label(mainComp, SWT.NULL);
		label1b.setText(Messages.getString("EbnfInputGui.e.g._A,B_6")); //$NON-NLS-1$
		label1b.setLayoutData(data1_3);

		// Row 2

		GridData data2_1 = new GridData();
		Label label2 = new Label(mainComp, SWT.NULL);
		label2.setText(Messages.getString("EbnfInputGui.Terminalsymbols__7")); //$NON-NLS-1$
		label2.setLayoutData(data2_1);

		GridData data2_2 = new GridData(GridData.FILL_HORIZONTAL);
		Text text2 = new Text(mainComp, SWT.BORDER);
		text2.setToolTipText(
			Messages.getString("EbnfInputGui.Set_of_terminalsymbols_n_8") + Messages.getString("EbnfInputGui.Colon_separated_9")); //$NON-NLS-1$ //$NON-NLS-2$
		text2.setLayoutData(data2_2);

		GridData data2_3 = new GridData();
		Label label2b = new Label(mainComp, SWT.NULL);
		label2b.setText(Messages.getString("EbnfInputGui.e.g._a,b_10")); //$NON-NLS-1$
		label2b.setLayoutData(data2_3);

		// Row 3

		GridData data3_1 = new GridData();
		Label label3 = new Label(mainComp, SWT.NULL);
		label3.setText(Messages.getString("EbnfInputGui.Startvariable__11")); //$NON-NLS-1$
		label3.setLayoutData(data3_1);

		GridData data3_2 = new GridData();
		data3_2.widthHint = 20;
		Text text3 = new Text(mainComp, SWT.BORDER);
		text3.setLayoutData(data3_2);

		GridData data3_3 = new GridData();
		Label label3b = new Label(mainComp, SWT.NULL);
		label3b.setText(Messages.getString("EbnfInputGui.e.g._S_12")); //$NON-NLS-1$
		label3b.setLayoutData(data3_3);

		// Row 4_1

		GridData data4_1 = new GridData();
		Label label4 = new Label(mainComp, SWT.NULL);
		label4.setText(Messages.getString("EbnfInputGui.Specialchar__13")); //$NON-NLS-1$
		label4.setLayoutData(data4_1);

		// Row 4_2

		GridData data4_2 = new GridData(GridData.FILL_HORIZONTAL);
		Composite data4_2_container = new Composite(mainComp, SWT.NULL);
		GridLayout data4_2_layout = new GridLayout(7, false);
		data4_2_layout.marginHeight = 0;
		data4_2_layout.marginWidth = 0;
		data4_2_container.setLayout(data4_2_layout);

		// Row 4_2_1

		GridData data4_2_1 = new GridData();
		Button button4_2_1 = new Button(data4_2_container, SWT.PUSH);
		button4_2_1.setText("\\^("); //$NON-NLS-1$
		button4_2_1.setLayoutData(data4_2_1);

		// Row 4_2_2

		GridData data4_2_2 = new GridData();
		Button button4_2_2 = new Button(data4_2_container, SWT.PUSH);
		button4_2_2.setText("\\^)"); //$NON-NLS-1$
		button4_2_2.setLayoutData(data4_2_2);

		// Row 4_2_3

		GridData data4_2_3 = new GridData();
		Button button4_2_3 = new Button(data4_2_container, SWT.PUSH);
		button4_2_3.setText("\\^["); //$NON-NLS-1$
		button4_2_3.setLayoutData(data4_2_3);

		// Row 4_2_4

		GridData data4_2_4 = new GridData();
		Button button4_2_4 = new Button(data4_2_container, SWT.PUSH);
		button4_2_4.setText("\\^]"); //$NON-NLS-1$
		button4_2_4.setLayoutData(data4_2_4);

		// Row 4_2_5

		GridData data4_2_5 = new GridData();
		Button button4_2_5 = new Button(data4_2_container, SWT.PUSH);
		button4_2_5.setText("\\^{"); //$NON-NLS-1$
		button4_2_5.setLayoutData(data4_2_5);

		// Row 4_2_6

		GridData data4_2_6 = new GridData();
		Button button4_2_6 = new Button(data4_2_container, SWT.PUSH);
		button4_2_6.setText("\\^}"); //$NON-NLS-1$
		button4_2_6.setLayoutData(data4_2_6);

		// Row 4_2_7

		GridData data4_2_7 = new GridData();
		Button button4_2_7 = new Button(data4_2_container, SWT.PUSH);
		button4_2_7.setText("\\^|"); //$NON-NLS-1$
		button4_2_7.setLayoutData(data4_2_7);

		// Row 4_3

		GridData data4_3 = new GridData();
		Label label4b = new Label(mainComp, SWT.NULL);
		label4b.setText(""); //$NON-NLS-1$
		label4b.setLayoutData(data4_3);

		// Row 5_1

		GridData data5_1 = new GridData();
		Label label5 = new Label(mainComp, SWT.NULL);
		label5.setText(Messages.getString("EbnfInputGui.Rules__22")); //$NON-NLS-1$
		label5.setLayoutData(data5_1);

		// Row 5_2

		GridData data5_2 = new GridData(GridData.FILL_HORIZONTAL);
		Composite data5_2_container = new Composite(mainComp, SWT.NULL);
		GridLayout data5_2_layout = new GridLayout(3, false);
		data5_2_layout.marginHeight = 0;
		data5_2_layout.marginWidth = 0;
		data5_2_container.setLayout(data5_2_layout);

		// Row 5_2_1

		GridData data5_2_1 = new GridData();
		final Text text5a = new Text(data5_2_container, SWT.BORDER);
		text5a.setToolTipText(
			Messages.getString("EbnfInputGui.Name_of_SynDiaVar_23")); //$NON-NLS-1$
		data5_2_1.widthHint = 100;
		text5a.setLayoutData(data5_2_1);

		// Row 5_2_2

		GridData data5_2_2 = new GridData();
		Label label5_2 = new Label(data5_2_container, SWT.NULL);
		label5_2.setText(" ::= "); //$NON-NLS-1$

		// Row 5_2_3

		GridData data5_2_3 = new GridData();
		final Text text5b = new Text(data5_2_container, SWT.BORDER);
		text5b.setToolTipText(
			Messages.getString("EbnfInputGui.Rules_in_latex_notation__n_25") //$NON-NLS-1$
				+ Messages.getString("EbnfInputGui.__^_converts_the_following_character_26")); //$NON-NLS-1$

		//TODO set width to 680 for presentation
		data5_2_3.widthHint = 400;
		text5b.setLayoutData(data5_2_3);

		// Row 5_3

		GridData data5_3 = new GridData();
		final Button button5 = new Button(mainComp, SWT.PUSH);
		button5.setEnabled(false);
		button5.setText(Messages.getString("EbnfInputGui.add_27")); //$NON-NLS-1$
		button5.setToolTipText(Messages.getString("EbnfInputGui.Add_new_line._28")); //$NON-NLS-1$
		button5.setLayoutData(data5_3);

		// Row 6

		GridData data6_1 = new GridData();
		Label label6 = new Label(mainComp, SWT.NULL);
		label6.setText(""); //$NON-NLS-1$
		label6.setLayoutData(data6_1);

		GridData data6_2 = new GridData(GridData.FILL_HORIZONTAL);
		data6_2.heightHint = 100;
		final List list6 = new List(mainComp, SWT.BORDER | SWT.V_SCROLL);
		list6.setLayoutData(data6_2);

		GridData data6_3 = new GridData();
		final Button button6 = new Button(mainComp, SWT.PUSH);
		button6.setEnabled(false);
		button6.setText(Messages.getString("EbnfInputGui.Delete_30")); //$NON-NLS-1$
		button6.setToolTipText(Messages.getString("EbnfInputGui.Delete_line._31")); //$NON-NLS-1$
		button6.setLayoutData(data6_3);

		// Row 7

		GridData data7_1 = new GridData();
		Label label7 = new Label(mainComp, SWT.NULL);
		label7.setText(""); //$NON-NLS-1$
		label7.setLayoutData(data7_1);

		GridData data7_2 = new GridData();
		final Button button7 = new Button(mainComp, SWT.PUSH);
		button7.setEnabled(false);
		button7.setText(Messages.getString("EbnfInputGui.Apply_33")); //$NON-NLS-1$
		button7.setLayoutData(data7_2);

		GridData data7_3 = new GridData();
		Label label7b = new Label(mainComp, SWT.NULL);
		label7b.setText(""); //$NON-NLS-1$
		label7b.setLayoutData(data7_3);

		// Listener

		text1.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				button7.setEnabled(validateForm());
			}
		});

		text2.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				button7.setEnabled(validateForm());
			}
		});

		text3.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				button7.setEnabled(validateForm());
			}
		});

		text5a.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				button5.setEnabled(
					validateRule(text5a.getText(), text5b.getText()));
			}
		});

		text5b.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				button5.setEnabled(
					validateRule(text5a.getText(), text5b.getText()));
			}
		});

		button4_2_1.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
			}
			public void widgetSelected(SelectionEvent event) {
				text5b.insert("\\^("); //$NON-NLS-1$
				text5b.setFocus();
			}
		});

		button4_2_2.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
			}
			public void widgetSelected(SelectionEvent event) {
				text5b.insert("\\^)"); //$NON-NLS-1$
				text5b.setFocus();
			}
		});

		button4_2_3.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
			}
			public void widgetSelected(SelectionEvent event) {
				text5b.insert("\\^["); //$NON-NLS-1$
				text5b.setFocus();
			}
		});

		button4_2_4.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
			}
			public void widgetSelected(SelectionEvent event) {
				text5b.insert("\\^]"); //$NON-NLS-1$
				text5b.setFocus();
			}
		});

		button4_2_5.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
			}
			public void widgetSelected(SelectionEvent event) {
				text5b.insert("\\^{"); //$NON-NLS-1$
				text5b.setFocus();
			}
		});

		button4_2_6.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
			}
			public void widgetSelected(SelectionEvent event) {
				text5b.insert("\\^}"); //$NON-NLS-1$
				text5b.setFocus();
			}
		});

		button4_2_7.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
			}
			public void widgetSelected(SelectionEvent event) {
				text5b.insert("\\^|"); //$NON-NLS-1$
				text5b.setFocus();
			}
		});

		button5.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
			}
			public void widgetSelected(SelectionEvent event) {
				//if (!variables.contains(text5a.getText())) {
				variables.add(text5a.getText());
				terms.add(text5b.getText());
				list6.add(text5a.getText() + " ::= " + text5b.getText()); //$NON-NLS-1$
				button7.setEnabled(validateForm());
				text5a.setText(""); //$NON-NLS-1$
				text5b.setText(""); //$NON-NLS-1$
				//}
			}
		});

		button6.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
			}
			public void widgetSelected(SelectionEvent event) {
				int index = list6.getSelectionIndex();
				variables.remove(index);
				terms.remove(index);
				list6.remove(index);
				button7.setEnabled(validateForm());
				button6.setEnabled(false);
				text5a.setText(""); //$NON-NLS-1$
				text5b.setText(""); //$NON-NLS-1$
			}
		});

		list6.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				//int index = list6.getSelectionIndex();
				//text5a.setText((String) variables.get(index));
				//text5b.setText((String) terms.get(index));
				button6.setEnabled(true);
			}
		});

		button7.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {

				EbnfParser p =
					new EbnfParser(
						getName(),
						getVars(),
						getAlphabet(),
						getStartVar(),
						getRules());
				EbnfDefinition def;
				try {
					def = p.analyse();
				} catch (EbnfParseException e) {

					System.err.println(Messages.getString("EbnfInputGui.Error_analyzing_EBNF_47")); //$NON-NLS-1$

					// open pop-up with error message
					MessageDialog.openWarning(
						mainComp.getShell(),
						Messages.getString("EbnfInputGui.Error_analyzing_EBNF_48"), //$NON-NLS-1$
						e.getMessage());
					return;
				}
				// ebnf could be analysed correctly

				modcon.setEbnfDef(def);
				modcon.setMode(8);
			}
		});

		// "Globale" Textfelder

		variablesSet = text1;
		terminalSymbolsSet = text2;
		startVar = text3;
	}

	// ---------------- private functions ---------------------------------------------------------

	private String getName() {
		return "\u03b5"; //$NON-NLS-1$
	}

	private String getVars() {
		return variablesSet.getText();
	}

	private String getAlphabet() {
		return terminalSymbolsSet.getText();
	}

	private String getStartVar() {
		return startVar.getText();
	}

	private HashMap getRules() {
		HashMap rules = new HashMap();
		Iterator varIter = variables.iterator();
		Iterator termsIter = terms.iterator();
		while (varIter.hasNext() && termsIter.hasNext()) {
			String var = new String();
			String term = new String();
			var = ((String) varIter.next());
			term = ((String) termsIter.next());
			rules.put(var, term);
		}
		return rules;
	}

	private boolean validateForm() {
		if ((variablesSet.getText() != "") //$NON-NLS-1$
			&& (startVar.getText() != "") //$NON-NLS-1$
			&& (variables.size() > 0)
			&& (terms.size() > 0)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean validateRule(String var, String term) {
		if ((var != "") && (term != "")) { //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		} else {
			return false;
		}
	}

}
