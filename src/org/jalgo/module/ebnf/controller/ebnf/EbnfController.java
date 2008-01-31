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

package org.jalgo.module.ebnf.controller.ebnf;

import java.awt.Cursor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.MainController;
import org.jalgo.module.ebnf.ModuleConnector;
import org.jalgo.module.ebnf.controller.ebnf.parser.EbnfParser;
import org.jalgo.module.ebnf.controller.ebnf.parser.ParseException;
import org.jalgo.module.ebnf.gui.ebnf.GuiController;
import org.jalgo.module.ebnf.gui.ebnf.RedoAction;
import org.jalgo.module.ebnf.gui.ebnf.RenderConstants;
import org.jalgo.module.ebnf.gui.ebnf.UndoAction;
import org.jalgo.module.ebnf.model.ebnf.Definition;
import org.jalgo.module.ebnf.model.ebnf.DefinitionFormatException;
import org.jalgo.module.ebnf.model.ebnf.ESymbol;
import org.jalgo.module.ebnf.model.ebnf.ETerminalSymbol;
import org.jalgo.module.ebnf.model.ebnf.EVariable;
import org.jalgo.module.ebnf.model.ebnf.Rule;
import org.jalgo.module.ebnf.model.ebnf.Term;
import org.jalgo.module.ebnf.util.ActionStack;
import org.jalgo.module.ebnf.util.IAction;

/**
 * 
 * @author Tom Kazimiers, Johannes Mey
 * 
 */
public class EbnfController implements Observer {

	private EbnfParser parser;

	private ActionStack actionStack;

	private Definition definition;

	private MainController mainController;

	private ModuleConnector moduleConnector;

	private GuiController guiController;

	private JPanel contentPane;

	private boolean transActive;

	private JButton redoButton;

	private JButton undoButton;

	/**
	 * This Constructor creates an EBNF controller
	 * 
	 * @param mainController
	 * @param contentPane
	 * @param moduleConnector
	 */
	public EbnfController(MainController mainController, JPanel contentPane,
			ModuleConnector moduleConnector) {
		contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		transActive = false;

		this.definition = new Definition();
		this.mainController = mainController;
		this.moduleConnector = moduleConnector;
		this.contentPane = contentPane;
		this.actionStack = new ActionStack();

		// add this as an observer of the definition this is necessary to
		// display the save status
		definition.addObserver(this);
		actionStack.addObserver(this);
		guiController = new GuiController(this);

		installToolbar();

		contentPane.setCursor(Cursor.getDefaultCursor());
	}

	/**
	 * Sets up the toolbar.
	 */
	private void installToolbar() {
		JToolBar toolBar = JAlgoGUIConnector.getInstance().getModuleToolbar(
				moduleConnector);

		UndoAction undoAction = new UndoAction(this);
		undoButton = mainController.createToolbarButton(undoAction);
		undoButton.setEnabled(false);
		toolBar.addSeparator();
		toolBar.add(undoButton);
		RedoAction redoAction = new RedoAction(this);
		redoButton = mainController.createToolbarButton(redoAction);

		redoButton.setEnabled(false);
		toolBar.add(redoButton);

	}

	// -----------------------------------------------------------------------------
	// Modification of the EBNF definition
	// -----------------------------------------------------------------------------

	/**
	 * Adds a terminal symbol to the definition
	 * 
	 * @param name
	 *            is the name of the new symbol
	 * @throws Exception
	 */
	public void addTerminal(String name) throws Exception {

		ETerminalSymbol terminal = null;

		// try to create a new ETerminalSymbol; if this is not possible because
		// of an invalid name, false is returned
		terminal = new ETerminalSymbol(name);

		// try to add the the terminal symbol to the definition, if this fails
		// because of an existing symbol, false is returned
		IAction action = new AddTerminalAction(this, terminal);
		actionStack.perform(action);

	}

	/**
	 * Adds a variable to the definition
	 * 
	 * @param name
	 *            is the name of the new symbol
	 * @throws Exception
	 */
	public void addVariable(String name) throws Exception {

		// try to add the the variable to the definition, if this fails
		// because of an existing symbol an exception is thrown
		IAction action = new AddVariableAction(this, name);
		actionStack.perform(action);

	}

	/**
	 * Adds a rule to the definition
	 * 
	 * @param left
	 *            is the variable on the left side of the rule
	 * @param right
	 *            is the term on the right side of the rule
	 * @throws Exception
	 */
	public void addRule(EVariable left, String right) throws Exception {

		// Check if all brackets are correct
		checkBrackets(right);

		// parse the right side of the rule
		parser = new EbnfParser(this, right);
		Term term = null;
		try {
			term = parser.parse();
		} catch (ParseException ex) {
			throw new DefinitionFormatException(Messages.getString("ebnf", //$NON-NLS-1$
					"Ebnf.Error.ParseError")); //$NON-NLS-1$
		}

		// try to create the rule
		Rule rule = new Rule(left, term);

		// try to add the rule to the definition
		IAction action = new AddRuleAction(this, rule);
		actionStack.perform(action);

	}

	/**
	 * Checks if all brackets in the term are correct and if alternative bars
	 * occur only in parentheses
	 * 
	 * @param term
	 *            the term to be ckecked
	 * @throws DefinitionFormatException
	 */
	private void checkBrackets(String term) throws DefinitionFormatException {

		Stack<String> s = new Stack<String>();

		for (int i = 0; i < term.length(); i++) {
			String tempChar = term.substring(i, i + 1);
			if (tempChar.equals(String.valueOf(RenderConstants.LBRACE)))
				s.push(tempChar);
			else if (tempChar.equals(String.valueOf(RenderConstants.LBRACKET)))
				s.push(tempChar);
			else if (tempChar.equals(String
					.valueOf(RenderConstants.LPARENTHESES)))
				s.push(tempChar);

			else if (tempChar.equals(String.valueOf(RenderConstants.RBRACE))) {
				String stackString = new String();
				try {
					stackString = s.pop();
				} catch (Exception e) {
					throw new DefinitionFormatException(Messages.getString(
							"ebnf", "Ebnf.Error.BracketError")); //$NON-NLS-1$ //$NON-NLS-2$
				}

				if (!stackString.equals(String.valueOf(RenderConstants.LBRACE)))
					throw new DefinitionFormatException(Messages.getString(
							"ebnf", "Ebnf.Error.BracketError")); //$NON-NLS-1$ //$NON-NLS-2$

			} else if (tempChar
					.equals(String.valueOf(RenderConstants.RBRACKET))) {
				String stackString = new String();
				try {
					stackString = s.pop();
				} catch (Exception e) {
					throw new DefinitionFormatException(Messages.getString(
							"ebnf", "Ebnf.Error.BracketError")); //$NON-NLS-1$ //$NON-NLS-2$
				}

				if (!stackString.equals(String
						.valueOf(RenderConstants.LBRACKET)))
					throw new DefinitionFormatException(Messages.getString(
							"ebnf", "Ebnf.Error.BracketError"));  //$NON-NLS-1$//$NON-NLS-2$

			} else if (tempChar.equals(String
					.valueOf(RenderConstants.RPARENTHESES))) {
				String stackString = new String();
				try {
					stackString = s.pop();
				} catch (Exception e) {
					throw new DefinitionFormatException(Messages.getString(
							"ebnf", "Ebnf.Error.BracketError")); //$NON-NLS-1$ //$NON-NLS-2$
				}

				if (!stackString.equals(String
						.valueOf(RenderConstants.LPARENTHESES)))
					throw new DefinitionFormatException(Messages.getString(
							"ebnf", "Ebnf.Error.BracketError"));  //$NON-NLS-1$//$NON-NLS-2$

			} else if (tempChar.equals(String
					.valueOf(RenderConstants.ALTERNATIVE))) {

				String stackString = new String();
				try {
					stackString = s.lastElement();
				} catch (Exception e) {
					throw new DefinitionFormatException(Messages.getString(
							"ebnf", "Ebnf.Error.AlternativeError"));  //$NON-NLS-1$//$NON-NLS-2$
				}

				if (!stackString.equals(String
						.valueOf(RenderConstants.LPARENTHESES)))
					throw new DefinitionFormatException(Messages.getString(
							"ebnf", "Ebnf.Error.AlternativeError"));  //$NON-NLS-1$//$NON-NLS-2$
			}

		}

		if (!s.isEmpty())
			throw new DefinitionFormatException(Messages.getString("ebnf", //$NON-NLS-1$
					"Ebnf.Error.BracketError")); //$NON-NLS-1$

	}

	/**
	 * @param terminal
	 * @throws Exception
	 */
	public void deleteTerminal(ETerminalSymbol terminal) throws Exception {

		IAction action = new DeleteTerminalAction(this, terminal);
		actionStack.perform(action);

	}

	/**
	 * @param variable
	 * @throws Exception
	 */
	public void deleteVariable(EVariable variable) throws Exception {
		IAction action = new DeleteVariableAction(this, variable);
		actionStack.perform(action);

	}

	/**
	 * @param rule
	 * @throws Exception
	 */
	public void deleteRule(Rule rule) throws Exception {

		IAction action = new DeleteRuleAction(this, rule);
		actionStack.perform(action);

	}

	/**
	 * @param oldRule
	 * @param left
	 * @param right
	 * @throws Exception
	 */
	public void modifyRule(Rule oldRule, EVariable left, String right)
			throws Exception {

		// Check if all brackets are correct
		checkBrackets(right);

		parser = new EbnfParser(this, right);
		Term term;

		try {
			term = parser.parse();
		} catch (ParseException ex) {
			throw new DefinitionFormatException(Messages.getString("ebnf", //$NON-NLS-1$
					"Ebnf.Error.ParseError")); //$NON-NLS-1$
		}

		Rule rule = new Rule(left, term);

		IAction action = new ModifyRuleAction(this, oldRule, rule);
		actionStack.perform(action);
	}

	/**
	 * @param oldTerminal
	 * @param newTerminal
	 * @throws Exception
	 */
	public void modifyTerminal(ETerminalSymbol oldTerminal, String newTerminal)
			throws Exception {

		IAction action = new ModifyTerminalAction(this, oldTerminal,
				newTerminal);
		actionStack.perform(action);

	}

	/**
	 * @param oldVariable
	 * @param newVariable
	 * @throws Exception
	 */
	public void modifyVariable(EVariable oldVariable, String newVariable)
			throws Exception {
		IAction action = new ModifyVariableAction(this, oldVariable,
				newVariable);
		actionStack.perform(action);

	}

	public void switchToChoiceGUI(boolean transActive) {
		this.transActive = transActive;
		if (transActive) {
			moduleConnector.setSaveStatus(SaveStatus.NO_CHANGES);
		}
		guiController.showChoiceGUI(transActive);
	}

	public void switchToTransGUI() {

		mainController.removeCustomMenu();
		mainController.addMenu(mainController.getMenu());
		mainController.setTransMode(definition, !transActive);
	}

	public void setStartVariable(EVariable startVar) throws Exception {

		IAction action = new SetStartVariableAction(this, startVar);
		actionStack.perform(action);

	}

	// -----------------------------------------------------------------------------
	// File Management
	// -----------------------------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.AbstractModuleConnector#setDataFromFile(java.io.ByteArrayInputStream)
	 */
	public void loadDefinition(ObjectInputStream in) {
		try {

			definition = (Definition) in.readObject();
			definition.addObserver(this);
			guiController.reloadObservers();

		} catch (Exception e) {
			JAlgoGUIConnector.getInstance().showErrorMessage(
					Messages.getString(
							"ebnf", "Ebnf.Error.LoadErrorInvalidDef")); //$NON-NLS-1$ //$NON-NLS-2$

		}
	}

	public ByteArrayOutputStream saveDefinition() {
		if (!guiController.isEditMode() && guiController.getStrictMode()) {
			if (guiController.showSaveStrictModeDialog(false) == 1) {
				setDefinitionStrict();
			}
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeBoolean(true);
			objOut.writeObject(definition);
			objOut.close();
			moduleConnector.setSaveStatus(SaveStatus.NO_CHANGES);
		} catch (IOException ex) {
			JAlgoGUIConnector.getInstance().showErrorMessage(
					Messages.getString("ebnf", "Ebnf.Error.SaveError")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return out;
	}

	// -----------------------------------------------------------------------------
	// actionStack
	// -----------------------------------------------------------------------------

	public void redo() {
		try {
			actionStack.redo();
		} catch (Exception e) {
			JAlgoGUIConnector.getInstance().showErrorMessage(
					Messages.getString("ebnf", "Ebnf.Error.RedoImpossible"));   //$NON-NLS-1$//$NON-NLS-2$
			e.printStackTrace();
			actionStack.toString();
			actionStack.clear();
		}
	}

	public void undo() {
		try {
			actionStack.undo();
		} catch (Exception e) {
			JAlgoGUIConnector.getInstance().showErrorMessage(
					Messages.getString("ebnf", "Ebnf.Error.UndoImpossible"));   //$NON-NLS-1$//$NON-NLS-2$
			e.printStackTrace();
			actionStack.toString();
			actionStack.clear();
		}
	}

	// -----------------------------------------------------------------------------
	// Definition Check
	// -----------------------------------------------------------------------------

	/**
	 * Checks if a Definition is complete and gives additional warnings
	 * 
	 * @return a String including the errors and warnings as HTML
	 */
	public String checkDefinition(boolean includeWarnings) {

		String checkResult = "<html>";

		// check if the definition has a start variable
		if (definition.getStartVariable() == null) {
			checkResult += Messages.getString("ebnf",
					"Ebnf.Controller_DefinitionCheck_NoStartVar");
			includeWarnings = false;
		}

		// check if the definition has a rule for every Variable
		for (EVariable var : definition.getVariables()) {
			if (definition.getRule(var) == null) {
				checkResult += Messages.getString("ebnf",
						"Ebnf.Controller_DefinitionCheck_NoRuleForVar_1")
						+ " "
						+ var
						+ " "
						+ Messages
								.getString("ebnf",
										"Ebnf.Controller_DefinitionCheck_NoRuleForVar_2");
				includeWarnings = false;
			}
		}

		if (includeWarnings) {
			// check if all Variables are used
			for (EVariable var : definition.getVariables()) {
				if ((!occursInRightSide(var))
						&& (!definition.getStartVariable().equals(var))) {
					checkResult += Messages.getString("ebnf",
							"Ebnf.Controller_DefinitionCheck_NoTermWithVar_1")
							+ " "
							+ var
							+ " "
							+ Messages
									.getString("ebnf",
											"Ebnf.Controller_DefinitionCheck_NoTermWithVar_2");
				}
			}

			// check if all TerminalSymbols are used
			for (ETerminalSymbol term : definition.getTerminals()) {
				if (!occursInRightSide(term)) {
					checkResult += Messages.getString("ebnf",
							"Ebnf.Controller_DefinitionCheck_NoTermWithTS_1")
							+ " "
							+ term
							+ " "
							+ Messages
									.getString("ebnf",
											"Ebnf.Controller_DefinitionCheck_NoTermWithTS_2");
				}
			}

			// Warshall-Algorithm
			int dim = definition.getVariables().size();

			boolean matrix[][] = new boolean[dim][dim];
			int startvar = -1;

			// building the distance matrix
			for (int from = 0; from < dim; from++) {
				if (definition.getVariables().get(from).equals(
						definition.getStartVariable()))
					startvar = from;
				for (int to = 0; to < dim; to++)
					try {
						matrix[from][to] = definition.getRule(
								definition.getVariables().get(from)).contains(
								definition.getVariables().get(to));
					} catch (Exception e) {
						matrix[from][to] = from == to;
					}
			}

			// the actual algorithm
			for (int k = 0; k < dim; k++)
				for (int i = 0; i < dim; i++)
					for (int j = 0; j < dim; j++)
						if (!matrix[i][j])
							matrix[i][j] = matrix[i][k] && matrix[k][j];

			// print matrix (for test purposes only
			// for (int i = 0;i<dim;i++) {
			// System.out.print(definition.getVariables().get(i) + ": ");
			// for (int j = 0;j<dim;j++)
			// System.out.print(((Boolean)matrix[i][j]).toString() + " ");
			// System.out.println();
			// }

			// reading the results
			for (int to = 0; (to < dim) && startvar >= 0; to++) {
				if (!matrix[startvar][to]
						&& definition
								.getRule(definition.getVariables().get(to)) != null) {
					checkResult += Messages
							.getString("ebnf",
									"Ebnf.Controller_DefinitionCheck_RuleNotReachable_1")
							+ " "
							+ definition.getVariables().get(to)
							+ " "
							+ Messages
									.getString("ebnf",
											"Ebnf.Controller_DefinitionCheck_RuleNotReachable_2");
				}
			}
		}

		return checkResult + "</html>";
	}

	/**
	 * This is a helper method for the definition check. It checks if any rule
	 * of the definition contains the given symbol <code>symbol</code>
	 * 
	 * @param symbol
	 * @return true if the defintion contains the symbol in a term of a rule
	 */
	public boolean occursInRightSide(ESymbol symbol) {
		for (Rule rule : definition.getRules()) {
			if (rule.getTerm().contains(symbol))
				return true;
		}
		return false;
	}

	// -----------------------------------------------------------------------------
	// getters
	// -----------------------------------------------------------------------------

	public Definition getDefinition() {
		return definition;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public MainController getMainController() {
		return mainController;
	}

	public GuiController getGuiController() {
		return guiController;
	}

	// -----------------------------------------------------------------------------
	// Observer Pattern
	// -----------------------------------------------------------------------------

	public void update(Observable o, Object arg) {
		if (o.getClass().equals(Definition.class)) {
			moduleConnector.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
			guiController.getEbnfController().setTransActive(false);
		} else if (o.getClass().equals(ActionStack.class)) {
			undoButton.setEnabled(actionStack.isUndoPossible());
			redoButton.setEnabled(actionStack.isRedoPossible());
		}

	}

	public boolean isTransActive() {
		return transActive;
	}

	public void setTransActive(boolean transActive) {
		this.transActive = transActive;
	}

	public void setDefinitionStrict() {
		try {
			this.definition = this.definition.getStrict();
			moduleConnector.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
		} catch (DefinitionFormatException e) {
			guiController.getInputPanel().showInfo(
					Messages.getString("ebnf",
							"Ebnf.Controller_MakeDefinitionStrictError"), true);
		}
		guiController.reloadObservers();
		definition.addObserver(this);
	}
}
