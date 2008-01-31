/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and
 * platform independent. j-Algo is developed with the help of Dresden
 * University of Technology.
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
package org.jalgo.module.ebnf.gui.ebnf;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.controller.ebnf.EbnfController;
import org.jalgo.module.ebnf.gui.EbnfFont;
import org.jalgo.module.ebnf.gui.FontNotInitializedException;
import org.jalgo.module.ebnf.gui.GUIConstants;
import org.jalgo.module.ebnf.model.ebnf.DefinitionFormatException;
import org.jalgo.module.ebnf.model.ebnf.ETerminalSymbol;
import org.jalgo.module.ebnf.model.ebnf.EVariable;
import org.jalgo.module.ebnf.model.ebnf.Rule;

/** The GUIController is responsible for the creation of the EBNF-GUI and for
 * handeling input
 *  
 * @author Tom Kazimiers, Johannes Mey
 * 
 */
public class GuiController implements Observer{

	
// -----------------------------------------------------------------------------
// class variables
// -----------------------------------------------------------------------------
	
	
	private static final long serialVersionUID = 1L;

	// GUI Components
	private JScrollPane definitionHeaderViewScrollPanel;

	private JScrollPane definitionRulesViewScrollPanel;

	private JSplitPane definitionViewPanel;

	private ChoicePanel choicePanel;

	private RuleViewPanel ruleViewPanel;

	private HeaderViewPanel headerViewPanel;

	private InputPanel inputPanel;

	private org.jdesktop.layout.GroupLayout contentPaneLayout;

	private EbnfController ebnfController;

	private JPanel contentPane;

	private Rule currentlyEditedRule;

	private TermPanel currentlyEditedRulePanel;

	private EVariable currentlyEditedVariable;

	private ETerminalSymbol currentlyEditedTerminal;

	private TermPanel currentlyEditedVariablePanel;

	private TermPanel currentlyEditedTerminalPanel;

	// state variables
	private boolean newUnknownSymbolAdded;

	private boolean editMode;

	private boolean strictMode;

	private Font ebnfFont; // stores our later initlized EbnfSans.TTF Font

	// Action Listeners
	EditRuleActionListener editRuleActionListener;

	ModifyDefinitionActionListener modifyDefinitionActionListener;

	EditDefinitionActionListener editDefinitionActionListener;

	ChoicePanelActionListener choicePanelActionListener;

	ViewPanelSizeListener viewPanelSizeListener;

	private JMenu editMenu;

	private JMenu insertMenu;

	private JMenuItem menuUndo;

	private JMenuItem menuParenthLeft;

	private JMenuItem menuParenthRight;

	private JMenuItem menuParenthBar;

	private JMenuItem menuBracketLeft;

	private JMenuItem menuBracketRight;

	private JMenuItem menuBraceLeft;

	private JMenuItem menuBraceRight;

	private JMenuItem menuLess;
	
	private JMenuItem menuMore;

//	 -----------------------------------------------------------------------------
//	 constructor
//	 -----------------------------------------------------------------------------
			
	
	/**
	 * 
	 * @param ebnfController
	 */
	public GuiController(EbnfController ebnfController) {

		this.ebnfController = ebnfController;
		this.contentPane = ebnfController.getContentPane();
		editMode = true;

		try {
			this.ebnfFont = EbnfFont.getFont();
		} catch (FontNotInitializedException e) {
			// if the right font could not be found use the "sans" font
			this.ebnfFont = new Font("Sans", Font.PLAIN, 18); //$NON-NLS-1$
			e.printStackTrace();
		}

		// initialize the ActionListeners
		editRuleActionListener = new EditRuleActionListener(this);
		modifyDefinitionActionListener = new ModifyDefinitionActionListener(
				this);
		editDefinitionActionListener = new EditDefinitionActionListener(this);
		choicePanelActionListener = new ChoicePanelActionListener(this);
		viewPanelSizeListener = new ViewPanelSizeListener(this);

		// create panels
		inputPanel = new InputPanel(this);
		ruleViewPanel = new RuleViewPanel(this);
		headerViewPanel = new HeaderViewPanel(this);
		choicePanel = new ChoicePanel(this);

		// initalize panels

		inputPanel.setEbnfFont(ebnfFont);
		installMenu();
		initComponents(false);
		reloadObservers();
	}

// -----------------------------------------------------------------------------
//	 initialization methods
// -----------------------------------------------------------------------------
		
	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents(boolean transActive) {

		contentPaneLayout = new org.jdesktop.layout.GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);

		if (transActive) {
			choicePanel.checkIfDefIsStrict();
			contentPaneLayout.setHorizontalGroup(contentPaneLayout
					.createParallelGroup(
							org.jdesktop.layout.GroupLayout.LEADING).add(
							choicePanel,
							org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
							org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
							Short.MAX_VALUE).add(definitionViewPanel,
							org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 795,
							Short.MAX_VALUE));

			contentPaneLayout
					.setVerticalGroup(contentPaneLayout
							.createParallelGroup(
									org.jdesktop.layout.GroupLayout.LEADING)
							.add(
									contentPaneLayout
											.createSequentialGroup()
											.add(
													choicePanel,
													org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
													org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
													org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(
													org.jdesktop.layout.LayoutStyle.UNRELATED)
											.add(
													definitionViewPanel,
													org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
													org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
													Short.MAX_VALUE)));
		} else {
			definitionViewPanel = new JSplitPane();
			definitionViewPanel.addComponentListener(viewPanelSizeListener);

			definitionHeaderViewScrollPanel = new JScrollPane(
					ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			definitionRulesViewScrollPanel = new JScrollPane();

			definitionViewPanel.setDividerLocation(95);
			definitionViewPanel
					.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
			definitionViewPanel
					.setTopComponent(definitionHeaderViewScrollPanel);
			definitionViewPanel
					.setBottomComponent(definitionRulesViewScrollPanel);

			contentPaneLayout.setHorizontalGroup(contentPaneLayout
					.createParallelGroup(
							org.jdesktop.layout.GroupLayout.LEADING).add(
							inputPanel,
							org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
							org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
							Short.MAX_VALUE).add(definitionViewPanel,
							org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 795,
							Short.MAX_VALUE));

			contentPaneLayout
					.setVerticalGroup(contentPaneLayout
							.createParallelGroup(
									org.jdesktop.layout.GroupLayout.LEADING)
							.add(
									contentPaneLayout
											.createSequentialGroup()
											.add(
													inputPanel,
													org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
													org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
													org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(
													org.jdesktop.layout.LayoutStyle.UNRELATED)
											.add(
													definitionViewPanel,
													org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
													org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
													Short.MAX_VALUE)));
		}
		definitionHeaderViewScrollPanel.setViewportView(headerViewPanel);
		definitionRulesViewScrollPanel.setViewportView(ruleViewPanel);

		contentPane.validate();
		contentPane.repaint();

	}

//	 -----------------------------------------------------------------------------
//	 gui control methods
//	 -----------------------------------------------------------------------------
			
	/**
	 * Installs our own Menu in the j-Algo MenuBar
	 * 
	 */
	private void installMenu() {
		ebnfController.getMainController().removeCustomMenu();
		editMenu = new JMenu(Messages.getString("ebnf", "Ebnf.Edit")); //$NON-NLS-1$ //$NON-NLS-2$
		insertMenu = new JMenu(Messages.getString("ebnf", "Ebnf.Insert")); //$NON-NLS-1$ //$NON-NLS-2$
		ebnfController.getMainController().addMenu(editMenu);
		ebnfController.getMainController().addMenu(insertMenu);

		menuUndo = new JMenuItem(Messages.getString("ebnf", "Ebnf.Undo")); //$NON-NLS-1$ //$NON-NLS-2$
		editMenu.add(menuUndo);

		menuParenthLeft = new JMenuItem("(   - Alt+U"); //$NON-NLS-1$
		menuParenthLeft.setActionCommand("ebnfParentLeft"); //$NON-NLS-1$
		menuParenthLeft.addActionListener(editRuleActionListener);
		insertMenu.add(menuParenthLeft);

		menuParenthRight = new JMenuItem(")   - Alt+O"); //$NON-NLS-1$
		menuParenthRight.setActionCommand("ebnfParentRight"); //$NON-NLS-1$
		menuParenthRight.addActionListener(editRuleActionListener);
		insertMenu.add(menuParenthRight);

		menuParenthBar = new JMenuItem("|   - Alt+I"); //$NON-NLS-1$
		menuParenthBar.setActionCommand("ebnfBar"); //$NON-NLS-1$
		menuParenthBar.addActionListener(editRuleActionListener);
		insertMenu.add(menuParenthBar);

		menuBracketLeft = new JMenuItem("[   - Alt+8"); //$NON-NLS-1$
		menuBracketLeft.setActionCommand("ebnfBracketLeft"); //$NON-NLS-1$
		menuBracketLeft.addActionListener(editRuleActionListener);
		insertMenu.add(menuBracketLeft);

		menuBracketRight = new JMenuItem("]   - Alt+9"); //$NON-NLS-1$
		menuBracketRight.setActionCommand("ebnfBracketRight"); //$NON-NLS-1$
		menuBracketRight.addActionListener(editRuleActionListener);
		insertMenu.add(menuBracketRight);

		menuBraceLeft = new JMenuItem("{   - Alt+7"); //$NON-NLS-1$
		menuBraceLeft.setActionCommand("ebnfBraceLeft"); //$NON-NLS-1$
		menuBraceLeft.addActionListener(editRuleActionListener);
		insertMenu.add(menuBraceLeft);

		menuBraceRight = new JMenuItem("}   - Alt+0"); //$NON-NLS-1$
		menuBraceRight.setActionCommand("ebnfBraceRight"); //$NON-NLS-1$
		menuBraceRight.addActionListener(editRuleActionListener);
		insertMenu.add(menuBraceRight);

//		JMenu menu = new JMenu(Messages.getString("ebnf", "Ebnf.FurtherChars")); //$NON-NLS-1$ //$NON-NLS-2$
//
//		menuLess = new JMenuItem("<"); //$NON-NLS-1$
//		menuLess.setActionCommand("ebnfLess"); //$NON-NLS-1$
//		menuLess.addActionListener(editRuleActionListener);
//		menu.add(menuLess);
//		
//		menuMore = new JMenuItem(">"); //$NON-NLS-1$
//		menuMore.setActionCommand("ebnfMore"); //$NON-NLS-1$
//		menuMore.addActionListener(editRuleActionListener);
//		menu.add(menuMore);
//
//		insertMenu.add(menu);
	}


	/**
	 * Adds the Observers in the GUI to thedefinition again. Thisis necessary
	 * when a new Definition is loaded; additionally the Observers are updated
	 */
	public void reloadObservers() {
		ebnfController.getDefinition().addObserver(inputPanel);
		ebnfController.getDefinition().addObserver(headerViewPanel);
		ebnfController.getDefinition().addObserver(ruleViewPanel);
		ebnfController.getDefinition().addObserver(this);
		
		this.update(null,null);
		inputPanel.update(null, null);
		headerViewPanel.update(null, null);
		ruleViewPanel.update(null, null);
	}


	/** 
	 * @param transActive
	 */
	public void showChoiceGUI(boolean transActive) {

		ruleViewPanel.setFactor(0.2);
		ruleViewPanel.setDisplayName("display"); //$NON-NLS-1$

		if (!transActive) {
			if (!showWarningsDialog(false, false))
				return;
			editMode = false;
			//System.out.println("Showing ChoiceGUI"); //$NON-NLS-1$
			choicePanel.checkIfDefIsStrict();
			choicePanel.setTransButtonText();
			contentPaneLayout.replace(inputPanel, choicePanel);
			contentPane.repaint();
			contentPane.validate();
		} else {
			//System.out.println("Showing ChoiceGUI [coming from trans()]"); //$NON-NLS-1$
			setStrictMode(true);
			choicePanel.setTransButtonText();
			installMenu();
			if (editMode) {
				editMode = false;
				contentPaneLayout.replace(inputPanel, choicePanel);
				choicePanel.checkIfDefIsStrict();
				getRuleViewPanel().removeAll();
				getRuleViewPanel().drawRules();
				contentPane.repaint();
				contentPane.validate();
			} else {
				editMode = false;
				initComponents(true);
			}
		}

	}

	/**
	 * 
	 * 
	 */
	public void showInputGUI() {
		editMode = true;
		ruleViewPanel.setFactor(0.22);
		ruleViewPanel.setDisplayName("editor"); //$NON-NLS-1$
		// System.out.println("Showing InputGUI"); //$NON-NLS-1$
		contentPaneLayout.replace(choicePanel, inputPanel);
		contentPane.repaint();
	}

	public boolean isEditMode() {
		return editMode;
	}

	/**
	 * The method <code>repositionDivider</code> sets the divider position of
	 * the split pane to the given value
	 * 
	 * @param pos
	 */
	void repositionDivider(int pos) {
		definitionViewPanel.setDividerLocation(pos);
	}

	/**
	 * 
	 * @return
	 */
	public boolean getStrictMode() {

		return strictMode;
	}

	ChoicePanel getChoicePanel() {
		return choicePanel;
	}

	HeaderViewPanel getHeaderViewPanel() {
		return headerViewPanel;
	}

	public InputPanel getInputPanel() {
		return inputPanel;
	}

	RuleViewPanel getRuleViewPanel() {
		return ruleViewPanel;
	}

	void setRuleViewPanel(RuleViewPanel ruleViewPanel) {
		this.ruleViewPanel = ruleViewPanel;
	}

	public EbnfController getEbnfController() {
		return ebnfController;
	}

	JPanel getContentPane() {
		return contentPane;
	}


	void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	/**
	 * @param strictMode
	 *            The strictMode to set.
	 */
	void setStrictMode(boolean strictMode) {
		this.strictMode = strictMode;
		choicePanel.checkIfDefIsStrict();
	}

	// popup menu functions for rules
	void editRule(Rule rule, TermPanel tPanel) {
		if (currentlyEditedRule == null) {
			inputPanel.setEditRuleActionCommand(true);
			currentlyEditedRule = rule;
			currentlyEditedRulePanel = tPanel;
			tPanel.setHighlighted(true);
			inputPanel.editRule(rule);
			ruleViewPanel.editRule(tPanel);
		}
	}

	void cancelRule() {
		if (currentlyEditedRule != null) {
			inputPanel.setEditRuleActionCommand(false);
			currentlyEditedRulePanel.setHighlighted(false);
			inputPanel.cancelRule();
			currentlyEditedRule = null;
			currentlyEditedRulePanel = null;
			inputPanel.update(null, null);
		}
	}

	void changeRule() {
		if (currentlyEditedRule != null) {
			try {
				ebnfController.modifyRule(currentlyEditedRule,
						currentlyEditedRule.getName(),
						inputPanel.addRuleRightSideTextbox.getText());
			} catch (Exception e) {
				inputPanel.showInfo(e.getMessage(), true);
			}
			inputPanel.setEditRuleActionCommand(false);
			try {
			currentlyEditedRulePanel.setHighlighted(false);
			} catch (Exception e ) {
				// no idea why there was an exception here
			}
			inputPanel.cancelRule();
			currentlyEditedRule = null;
			currentlyEditedRulePanel = null;
			inputPanel.update(null, null);
		}
	}

	// popup menu functions for terminalsymbols
	void editTerminal(ETerminalSymbol terminal, TermPanel tPanel) {
		if (currentlyEditedTerminal == null) {
			inputPanel.setEditTerminalActionCommand(true);
			currentlyEditedTerminal = terminal;
			currentlyEditedTerminalPanel = tPanel;
			tPanel.setHighlighted(true);
			inputPanel.editTerminal(terminal);
		}
	}

	void cancelTerminal() {
		if (currentlyEditedTerminal != null) {
			inputPanel.setEditTerminalActionCommand(false);
			currentlyEditedTerminalPanel.setHighlighted(false);
			inputPanel.cancelTerminal();
			currentlyEditedTerminal = null;
			currentlyEditedTerminalPanel = null;
		}
	}

	void changeTerminal() {

		if (currentlyEditedTerminal != null) {
			try {
				ebnfController.modifyTerminal(currentlyEditedTerminal,
						inputPanel.addTerminalTextField.getText());
			} catch (Exception e) {
				inputPanel.showInfo(e.getMessage(), true);
			}
			inputPanel.setEditTerminalActionCommand(false);
			currentlyEditedTerminalPanel.setHighlighted(false);
			inputPanel.cancelTerminal();
			currentlyEditedTerminal = null;
			currentlyEditedTerminalPanel = null;
		}
	}

	// popup menu functions for variables
	void editVariable(EVariable variable, TermPanel tPanel) {
		if (currentlyEditedVariable == null) {
			inputPanel.setEditVariableActionCommand(true);
			currentlyEditedVariable = variable;
			currentlyEditedVariablePanel = tPanel;
			tPanel.setHighlighted(true);
			inputPanel.editVariable(variable);
		}
	}

	void cancelVariable() {
		if (currentlyEditedVariable != null) {
			inputPanel.setEditVariableActionCommand(false);
			currentlyEditedVariablePanel.setHighlighted(false);
			inputPanel.cancelVariable();
			currentlyEditedVariable = null;
			currentlyEditedVariablePanel = null;
		}
	}

	void changeVariable() {
		if (currentlyEditedVariable != null) {
			try {
				ebnfController.modifyVariable(currentlyEditedVariable,
						inputPanel.addVariableTextField.getText());
			} catch (Exception e) {
				inputPanel.showInfo(e.getMessage(), true);
			}
			inputPanel.setEditVariableActionCommand(false);
			currentlyEditedVariablePanel.setHighlighted(false);
			inputPanel.cancelVariable();
			currentlyEditedVariable = null;
			currentlyEditedVariablePanel = null;
		}
	}

	
	
	public boolean showUnknownSymbolDialog(String unknownSymbols) {

		JDialog unknownSymbolDialog = new JDialog((JFrame) contentPane
				.getTopLevelAncestor(), true);
		JButton terminalButton = new javax.swing.JButton();
		JButton variableButton = new javax.swing.JButton();
		JButton cancelButton = new javax.swing.JButton();
		JLabel infoTextLabel = new javax.swing.JLabel();
		JLabel symbolLabel = new javax.swing.JLabel();
		JTextField newSymbolTextField = new javax.swing.JTextField();

		unknownSymbolDialog
				.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		newSymbolTextField.setText("jTextField1"); //$NON-NLS-1$

		infoTextLabel
				.setText("<html>"+Messages.getString("ebnf", "Ebnf.symbol")+ " <font color=blue>" //$NON-NLS-1$
						+ unknownSymbols
						+ "</font> "+Messages.getString("ebnf", "Ebnf.isUnknown")+ "</html>"); //$NON-NLS-1$

		terminalButton.setText(Messages.getString("ebnf", "Ebnf.terminal")); //$NON-NLS-1$ //$NON-NLS-2$

		variableButton.setText(Messages.getString("ebnf", "Ebnf.variable")); //$NON-NLS-1$ //$NON-NLS-2$

		cancelButton.setText(Messages.getString("ebnf", "Ebnf.cancel")); //$NON-NLS-1$ //$NON-NLS-2$

		symbolLabel.setText(Messages.getString("ebnf", "Ebnf.33")); //$NON-NLS-1$ //$NON-NLS-2$

		newSymbolTextField.setText(unknownSymbols);

		// reset the unknownSymbolAdded-switch
		this.newUnknownSymbolAdded = false;

		// add the actionlisteners
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				((JButton) e.getSource()).getTopLevelAncestor().setVisible(
						false);

			}
		});

		class AddVariableListener implements ActionListener {

			EbnfController ebnfController;

			JTextField symbolText;

			GuiController guiController;

			public AddVariableListener(EbnfController ebnfController,
					JTextField symbolText, GuiController controller) {
				this.ebnfController = ebnfController;
				this.symbolText = symbolText;
				this.guiController = controller;
			}

			public void actionPerformed(ActionEvent e) {
				try {
					ebnfController.addVariable(symbolText.getText());
				} catch (DefinitionFormatException ex) {
					guiController.getInputPanel().showInfo(ex.getMessage(), true);
				} catch (Exception ex) {
					Toolkit.getDefaultToolkit().beep();
				}


				guiController.newUnknownSymbolAdded = true;

				((JButton) e.getSource()).getTopLevelAncestor().setVisible(
						false);

			}
		}

		class AddTerminalListener implements ActionListener {

			EbnfController ebnfController;

			JTextField symbolText;

			GuiController guiController;

			public AddTerminalListener(EbnfController ebnfController,
					JTextField symbolText, GuiController controller) {
				this.ebnfController = ebnfController;
				this.symbolText = symbolText;
				this.guiController = controller;
			}

			public void actionPerformed(ActionEvent e) {

				try {
					ebnfController.addTerminal(symbolText.getText());
				} catch (DefinitionFormatException ex) {
					guiController.getInputPanel().showInfo(ex.getMessage(), true);
				} catch (Exception ex) {
					Toolkit.getDefaultToolkit().beep();
				}

				guiController.newUnknownSymbolAdded = true;

				((JButton) e.getSource()).getTopLevelAncestor().setVisible(
						false);

			}
		}

		variableButton.addActionListener(new AddVariableListener(
				ebnfController, newSymbolTextField, this));
		terminalButton.addActionListener(new AddTerminalListener(
				ebnfController, newSymbolTextField, this));

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				unknownSymbolDialog.getContentPane());
		unknownSymbolDialog.getContentPane().setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																newSymbolTextField,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																301,
																Short.MAX_VALUE)
														.add(infoTextLabel)
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				variableButton)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				terminalButton)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED,
																				28,
																				Short.MAX_VALUE)
																		.add(
																				cancelButton))
														.add(symbolLabel))
										.addContainerGap()));
		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(infoTextLabel)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(symbolLabel)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												newSymbolTextField,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(terminalButton)
														.add(variableButton)
														.add(cancelButton))
										.addContainerGap()));

		unknownSymbolDialog.pack();

		
		unknownSymbolDialog.setLocation(contentPane.getTopLevelAncestor().getX() + (contentPane.getTopLevelAncestor().getWidth()-unknownSymbolDialog.getWidth())/2,
				contentPane.getTopLevelAncestor().getX() + (contentPane.getTopLevelAncestor().getHeight()-unknownSymbolDialog.getHeight())/2);
		unknownSymbolDialog.setAlwaysOnTop(true);
		unknownSymbolDialog.setVisible(true);

		return this.newUnknownSymbolAdded;
	}

	public int showSaveStrictModeDialog(boolean withCancelButton) {
		int ok;

		if (withCancelButton)
			ok = JOptionPane.showConfirmDialog(null, Messages.getString("ebnf", //$NON-NLS-1$
					"Ebnf.ChoicePanel_SaveStrictMode"), Messages.getString( //$NON-NLS-1$
					"ebnf", "Ebnf.ChoicePanel_StrictMode"), //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.YES_NO_CANCEL_OPTION);
		else
			ok = JOptionPane.showConfirmDialog(null, Messages.getString("ebnf", //$NON-NLS-1$
					"Ebnf.ChoicePanel_SaveStrictMode"), Messages.getString( //$NON-NLS-1$
					"ebnf", "Ebnf.ChoicePanel_StrictMode"), //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.YES_NO_OPTION);
		if (ok == JOptionPane.YES_OPTION)
			return 1;
		if (ok == JOptionPane.NO_OPTION)
			return 0;
		return -1;
	}
	
	public boolean showWarningsDialog(boolean includeWarnings,
			boolean showWinIfCorrect) {

		JDialog warningDialog = new JDialog();
		JButton okButton = new javax.swing.JButton();
		JLabel infoTextLabel = new javax.swing.JLabel();
		JLabel warningLabel = new JLabel();
		JPanel warningTextPane = new javax.swing.JPanel();
		JScrollPane scrollPane = new JScrollPane();

		warningDialog
				.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		warningTextPane.setAutoscrolls(true);
		warningTextPane.setBackground(GUIConstants.STANDARD_COLOR_BACKGROUND);
		warningDialog.setModal(true);

		scrollPane.setViewportView(warningTextPane);

		infoTextLabel
				.setText("<html>"+Messages.getString("ebnf", "Ebnf.Error.StillErrorsInDef")+"</html>");
		

		okButton.setText("Ok");

		String warnings = ebnfController.checkDefinition(includeWarnings);

		// add the actionlisteners
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				((JButton) e.getSource()).getTopLevelAncestor().setVisible(
						false);

			}
		});

		warningDialog.setLayout(new BorderLayout(0, 0));

		warningDialog.add(infoTextLabel, BorderLayout.NORTH);
		warningDialog.add(scrollPane, BorderLayout.CENTER);
		warningDialog.add(okButton, BorderLayout.SOUTH);

		warningDialog.setLocationByPlatform(true);

		if (warnings.equals("<html></html>")) { //$NON-NLS-1$
			if (!showWinIfCorrect)
				return true;
			infoTextLabel
					.setText(Messages.getString("ebnf", "Ebnf.RuleIsCorrect")); //$NON-NLS-1$ //$NON-NLS-2$
			warningTextPane.setPreferredSize(new Dimension(300, warningLabel
					.getHeight()));
			warningDialog.setPreferredSize(new Dimension(400, 100));

			scrollPane.setVisible(false);
			warningDialog.pack();
			
			warningDialog.setLocation(contentPane.getTopLevelAncestor().getX() + (int) (contentPane.getTopLevelAncestor().getWidth()-warningDialog.getWidth())/2,
					contentPane.getTopLevelAncestor().getX() + (int)(contentPane.getTopLevelAncestor().getHeight()-warningDialog.getHeight())/2);
			
			
			warningDialog.setVisible(true);
			warningDialog.setAlwaysOnTop(true);

			warningDialog.validate();
			return true;
		} else {
			warningTextPane.setVisible(true);
			warningLabel.setText(warnings);
			warningTextPane.setPreferredSize(new Dimension(warningLabel
					.getPreferredSize().width,
					warningLabel.getPreferredSize().height + 10));
			warningTextPane.add(warningLabel);
			warningDialog.setSize(400, 150);
			warningDialog.pack();
			
			warningDialog.setLocation(contentPane.getTopLevelAncestor().getX() + (int) (contentPane.getTopLevelAncestor().getWidth()-warningDialog.getWidth())/2,
					contentPane.getTopLevelAncestor().getX() + (int)(contentPane.getTopLevelAncestor().getHeight()-warningDialog.getHeight())/2);
			
			warningDialog.setVisible(true);
			warningDialog.setAlwaysOnTop(true);
			warningDialog.validate();
			return false;
		}

	}

	Rule getCurrentlyEditedRule() {
		return currentlyEditedRule;
	}

	ETerminalSymbol getCurrentlyEditedTerminal() {
		return currentlyEditedTerminal;
	}

	EVariable getCurrentlyEditedVariable() {
		return currentlyEditedVariable;
	}

	public void update(Observable o, Object arg) {
		
		if (!ebnfController.getDefinition().getRules().contains(currentlyEditedRule))
			cancelRule();
		if (!ebnfController.getDefinition().getVariables().contains(currentlyEditedVariable))
			cancelVariable();
		if (!ebnfController.getDefinition().getTerminals().contains(currentlyEditedTerminal))
			cancelTerminal();
		
	}
}
