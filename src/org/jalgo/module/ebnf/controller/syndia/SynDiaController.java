package org.jalgo.module.ebnf.controller.syndia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.MainController;
import org.jalgo.module.ebnf.ModuleConnector;
import org.jalgo.module.ebnf.gui.syndia.GuiController;
import org.jalgo.module.ebnf.model.syndia.Branch;
import org.jalgo.module.ebnf.model.syndia.NullElem;
import org.jalgo.module.ebnf.model.syndia.Repetition;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.model.syndia.TerminalSymbol;
import org.jalgo.module.ebnf.model.syndia.Variable;
import org.jalgo.module.ebnf.model.syndia.WordWrap;
import org.jalgo.module.ebnf.util.ActionStack;

/**
 * This class is the controller for syntax diagram input. The
 * add/remove-functions just create a new IAction, which perform the task. They
 * are called by the SynDiaMouseListener.<br>
 * The controller can save syntax diagram systems.
 * 
 * @author Michael Thiele
 */
public class SynDiaController implements Observer {

	private MainController mainController;

	private ModuleConnector connector;

	private GuiController guiController;

	private ActionStack actionStack;

	private SynDiaSystem synDiaSystem;

	/**
	 * Constructor that initializes the controller. A new
	 * <code>GuiController</code> and <code>ActionStack</code> are created.
	 * 
	 * @param mainController
	 *            the jalgo main controller
	 * @param connector
	 *            the jalgo module connector
	 * @param synDiaSystem
	 *            model for a syntax diagram system
	 * @param rootPane
	 *            the main panel from j-algo.
	 */
	public SynDiaController(MainController mainController,
			ModuleConnector connector, SynDiaSystem synDiaSystem,
			JPanel rootPane) {
		this.mainController = mainController;
		this.connector = connector;
		this.synDiaSystem = synDiaSystem;
		actionStack = new ActionStack();
		guiController = new GuiController(this, connector, rootPane);
		synDiaSystem.addObserver(guiController.getDrawPanel());
		synDiaSystem.addObserver(this);
	}

	/**
	 * Switches to syntax diagram view.
	 * 
	 */
	public void switchToSynDiaView() {
		synDiaSystem.removeNullElems();
		mainController.setSynDiaDisplayMode(synDiaSystem);
	}

	/**
	 * Performs an undo action.
	 * 
	 */
	public void undo() {
		if (actionStack.isUndoPossible()) {
			try {
				actionStack.undo();
				synDiaSystem.notifyObservers();
			} catch (Exception e) {
				mainController.showErrorDialog(Messages.getString("ebnf",
						"SynDiaEditor.Error_InternalError")
						+ Messages.getString("ebnf",
								"SynDiaEditor.Error_Appendix"), false);
			}
		}
	}

	/**
	 * Performs a redo action.
	 * 
	 */
	public void redo() {
		if (actionStack.isRedoPossible()) {
			try {
				actionStack.redo();
				synDiaSystem.notifyObservers();
			} catch (Exception e) {
				mainController.showErrorDialog(Messages.getString("ebnf",
						"SynDiaEditor.Error_InternalError")
						+ Messages.getString("ebnf",
								"SynDiaEditor.Error_Appendix"), false);
			}
		}
	}

	/**
	 * Adds a syntax diagram to the syntax diagram system.
	 * 
	 * @param name
	 *            the name of the new syntax diagram
	 * @param setStartDiagram
	 *            is <code>true</code> if new diagram will be start diagram
	 */
	public void addSyntaxDiagram(String name, boolean setStartDiagram) {
		try {
			actionStack.perform(new AddSynDiaAction(synDiaSystem, name,
					setStartDiagram));
			synDiaSystem.notifyObservers();
		} catch (Exception e) {
			mainController.showErrorDialog(
					Messages.getString("ebnf",
							"SynDiaEditor.Error_InternalError")
							+ Messages.getString("ebnf",
									"SynDiaEditor.Error_Appendix"), false);
		}
	}

	/**
	 * Renames a syntax diagram.
	 * 
	 * @param sd
	 *            the syntax diagram to rename
	 * @param newLabel
	 *            the new name for the syntax diagram
	 * @param setStartDiagram
	 *            <code>true</code> if the syntax diagram should be start
	 *            diagram
	 */
	public void renameSyntaxDiagram(SyntaxDiagram sd, String newLabel,
			boolean setStartDiagram) {
		try {
			actionStack.perform(new RenameSyntaxDiagramAction(sd, newLabel,
					setStartDiagram));
			synDiaSystem.notifyObservers();
		} catch (Exception e) {
			mainController.showErrorDialog(
					Messages.getString("ebnf",
							"SynDiaEditor.Error_InternalError")
							+ Messages.getString("ebnf",
									"SynDiaEditor.Error_Appendix"), false);
		}
	}

	/**
	 * Renames a terminal symbol.
	 * 
	 * @param terminalSymbol
	 *            the terminal symbol to rename
	 * @param newLabel
	 *            the new label for the terminal symbol
	 */
	public void renameTerminal(TerminalSymbol terminalSymbol, String newLabel) {
		try {
			actionStack.perform(new RenameTerminalAction(terminalSymbol,
					newLabel));
			synDiaSystem.notifyObservers();
		} catch (Exception e) {
			mainController.showErrorDialog(
					Messages.getString("ebnf",
							"SynDiaEditor.Error_InternalError")
							+ Messages.getString("ebnf",
									"SynDiaEditor.Error_Appendix"), false);
		}
	}

	/**
	 * Adds a terminal symbol at the given position.
	 * 
	 * @param nullElem
	 *            is needed to locate where to insert
	 * @param label
	 *            the label of the terminal symbol
	 */
	public void addTerminal(NullElem nullElem, String label) {
		try {
			actionStack.perform(new AddTerminalAction(nullElem, label));
			synDiaSystem.notifyObservers();
		} catch (Exception e) {
			mainController.showErrorDialog(
					Messages.getString("ebnf",
							"SynDiaEditor.Error_InternalError")
							+ Messages.getString("ebnf",
									"SynDiaEditor.Error_Appendix"), false);
		}
	}

	/**
	 * Renames a variable.
	 * 
	 * @param variable
	 *            the variable to rename
	 * @param newLabel
	 *            the new label for the variable
	 */
	public void renameVariable(Variable variable, String newLabel) {
		try {
			actionStack.perform(new RenameVariableAction(variable, newLabel));
			synDiaSystem.notifyObservers();
		} catch (Exception e) {
			mainController.showErrorDialog(
					Messages.getString("ebnf",
							"SynDiaEditor.Error_InternalError")
							+ Messages.getString("ebnf",
									"SynDiaEditor.Error_Appendix"), false);
		}
	}

	/**
	 * Adds a variable at the given position.
	 * 
	 * @param nullElem
	 *            is needed to locate where to insert
	 * @param label
	 *            the label of the variable
	 */
	public void addVariable(NullElem nullElem, String label) {
		try {
			actionStack.perform(new AddVariableAction(nullElem, label));
			synDiaSystem.notifyObservers();
		} catch (Exception e) {
			mainController.showErrorDialog(
					Messages.getString("ebnf",
							"SynDiaEditor.Error_InternalError")
							+ Messages.getString("ebnf",
									"SynDiaEditor.Error_Appendix"), false);
		}
	}

	/**
	 * Adds a repetition at the given position.
	 * 
	 * @param nullElemBegin
	 *            is needed to locate where to insert the begin of the
	 *            repetition
	 * @param nullElemEnd
	 *            is needed to locate where to insert the end of the repetition
	 */
	public void addRepetition(NullElem nullElemBegin, NullElem nullElemEnd) {
		try {
			actionStack.perform(new AddRepetitionAction(nullElemBegin,
					nullElemEnd));
			synDiaSystem.notifyObservers();
		} catch (Exception e) {
			mainController.showErrorDialog(
					Messages.getString("ebnf",
							"SynDiaEditor.Error_InternalError")
							+ Messages.getString("ebnf",
									"SynDiaEditor.Error_Appendix"), false);
		}
	}

	/**
	 * Adds a branch at the given position.
	 * 
	 * @param nullElemBegin
	 *            is needed to locate where to insert the begin of the branch
	 * @param nullElemEnd
	 *            is needed to locate where to insert the end of the branch
	 */
	public void addBranch(NullElem nullElemBegin, NullElem nullElemEnd) {
		try {
			actionStack
					.perform(new AddBranchAction(nullElemBegin, nullElemEnd));
			synDiaSystem.notifyObservers();
		} catch (Exception e) {
			mainController.showErrorDialog(
					Messages.getString("ebnf",
							"SynDiaEditor.Error_InternalError")
							+ Messages.getString("ebnf",
									"SynDiaEditor.Error_Appendix"), false);
		}
	}

	/**
	 * Adds a manual word wrap at the given position.
	 * 
	 * @param nullElem
	 *            is needed to locate where to insert the word wrap
	 */
	public void addWordWrap(NullElem nullElem) {
		try {
			actionStack.perform(new AddWordWrapAction(nullElem));
			synDiaSystem.notifyObservers();
		} catch (Exception e) {
			mainController.showErrorDialog(
					Messages.getString("ebnf",
							"SynDiaEditor.Error_InternalError")
							+ Messages.getString("ebnf",
									"SynDiaEditor.Error_Appendix"), false);
		}
	}

	/**
	 * Removes a syntax diagram of a syntax diagram system.
	 * 
	 * @param syntaxDiagram
	 *            the syntax diagram to remove
	 * @throws Exception
	 */
	public void removeSyntaxDiagram(SyntaxDiagram syntaxDiagram)
			throws Exception {
		actionStack.perform(new DeleteSyntaxDiagramAction(syntaxDiagram));
		synDiaSystem.notifyObservers();
	}

	/**
	 * Removes a terminal symbol of the syntax diagram system.
	 * 
	 * @param deletedTerminal
	 *            the terminal symbol to delete
	 */
	public void removeTerminal(TerminalSymbol deletedTerminal) {
		try {
			actionStack.perform(new DeleteTerminalAction(deletedTerminal));
			synDiaSystem.notifyObservers();
		} catch (Exception e) {
			mainController.showErrorDialog(
					Messages.getString("ebnf",
							"SynDiaEditor.Error_InternalError")
							+ Messages.getString("ebnf",
									"SynDiaEditor.Error_Appendix"), false);
		}
	}

	/**
	 * Removes a variable of the syntax diagram system.
	 * 
	 * @param deletedVariable
	 *            the variable to delete
	 */
	public void removeVariable(Variable deletedVariable) {
		try {
			actionStack.perform(new DeleteVariableAction(deletedVariable,
					synDiaSystem));
			synDiaSystem.notifyObservers();
		} catch (Exception e) {
			mainController.showErrorDialog(
					Messages.getString("ebnf",
							"SynDiaEditor.Error_InternalError")
							+ Messages.getString("ebnf",
									"SynDiaEditor.Error_Appendix"), false);
		}
	}

	/**
	 * Removes a manual word wrap of the syntax diagram system.
	 * 
	 * @param deletedWordWrap
	 *            the word wrap to delete
	 */
	public void removeWordWrap(WordWrap deletedWordWrap) {
		try {
			actionStack.perform(new DeleteWordWrapAction(deletedWordWrap));
			synDiaSystem.notifyObservers();
		} catch (Exception e) {
			mainController.showErrorDialog(
					Messages.getString("ebnf",
							"SynDiaEditor.Error_InternalError")
							+ Messages.getString("ebnf",
									"SynDiaEditor.Error_Appendix"), false);
		}
	}

	/**
	 * Removes a repetition of the syntax diagram system.
	 * 
	 * @param deletedRepetition
	 *            the repetition to delete
	 * @param left
	 *            <code>true</code> if the left (upper) path has to be
	 *            preserved
	 * @param right
	 *            <code>true</code> if the right (lower) path has to be
	 *            preserved
	 */
	public void removeRepetition(Repetition deletedRepetition, boolean left,
			boolean right) {
		try {
			actionStack.perform(new DeleteRepetitionAction(deletedRepetition,
					left, right));
			synDiaSystem.notifyObservers();
		} catch (Exception e) {
			mainController.showErrorDialog(
					Messages.getString("ebnf",
							"SynDiaEditor.Error_InternalError")
							+ Messages.getString("ebnf",
									"SynDiaEditor.Error_Appendix"), false);
		}
	}

	/**
	 * Removes a branch of the syntax diagram system.
	 * 
	 * @param deletedBranch
	 *            the branch to delete
	 * @param left
	 *            <code>true</code> if the left (upper) path has to be
	 *            preserved
	 * @param right
	 *            <code>true</code> if the right (lower) path has to be
	 *            preserved
	 */
	public void removeBranch(Branch deletedBranch, boolean left, boolean right) {
		try {
			actionStack.perform(new DeleteBranchAction(deletedBranch, left,
					right));
			synDiaSystem.notifyObservers();
		} catch (Exception e) {
			mainController.showErrorDialog(
					Messages.getString("ebnf",
							"SynDiaEditor.Error_InternalError")
							+ Messages.getString("ebnf",
									"SynDiaEditor.Error_Appendix"), false);
		}
	}

	/**
	 * Returns the syntax diagram system.
	 * 
	 * @return the syntax diagram system
	 */
	public SynDiaSystem getSynDiaSystem() {
		return synDiaSystem;
	}

	/**
	 * Returns the main controller.
	 * 
	 * @return the main controller
	 */
	public MainController getMainController() {
		return mainController;
	}

	/**
	 * Returns the action stack.
	 * 
	 * @return the action stack
	 */
	public ActionStack getActionStack() {
		return actionStack;
	}

	/**
	 * Saves the actual syntax diagram system into a file.
	 * 
	 * @return a <code>ByteArrayOutputStream</code>
	 */
	public ByteArrayOutputStream saveSystem() {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeBoolean(false);
			objOut.writeObject(synDiaSystem);
			objOut.close();
			connector.setSaveStatus(SaveStatus.NO_CHANGES);
		} catch (IOException e) {
			JAlgoGUIConnector.getInstance().showErrorMessage(
					Messages.getString("ebnf", "SynDia.Error.SaveError"));
		}

		return out;
	}

	public void update(Observable o, Object arg) {
		if (o instanceof SynDiaSystem) {
			connector.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
		}
	}

}
