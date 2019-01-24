/**
 * Class Controller
 * 
 * @author Tobias Reiher
 * @version 1.0
 */
package org.jalgo.module.lambda.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;
import java.util.Set;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.lambda.ModuleConnector;
import org.jalgo.module.lambda.ShortcutHandler;
import org.jalgo.module.lambda.model.Atom;
import org.jalgo.module.lambda.model.EAvailability;
import org.jalgo.module.lambda.model.EStepKind;
import org.jalgo.module.lambda.model.ITermHandler;
import org.jalgo.module.lambda.model.Shortcut;
import org.jalgo.module.lambda.view.GUIController;

public class Controller implements IController {

	private GUIController gui;
	private TermHistory termHistory;
	private ITermHandler termHandler;
	private IParseUnit parseUnit;
	private String workingPosition;

	/**
	 * initializes attributes and instantiates GUIController, TermHistory and ParseUnit
	 * 
	 * @param moduleConnector
	 */
	public Controller(ModuleConnector moduleConnector) {
		gui = new GUIController(moduleConnector, this);
		termHistory = new TermHistory(gui);
		parseUnit = new ParseUnit();
		workingPosition = "";
	}

	/**  
	 * install the welcome screen
	 */
	public void run() {
		gui.installWelcomeScreen();	
	}

	/**
	 * loads the current term and shortcuts from file
	 * 
	 * @param data
	 */
	public void loadData(ObjectInputStream in) {
		try {
			gui.installWorkScreen();
			gui.clearComment();

			Integer c = (Integer) in.readObject();			
			String s = (String) in.readObject();

			ShortcutHandler.getInstance().clearShortcuts();
			for (int i = 0; i < c.intValue(); i++)
				ShortcutHandler.getInstance().addShortcut((String) in.readObject(), (String) in.readObject(), false);
			gui.refreshShortcutList();
			
			gui.setInputTextField(s);
			processInputString(s, gui);
		} catch (IOException e) {
			JAlgoGUIConnector.getInstance().showErrorMessage(Messages.getString(
					"lambda", "ModuleConnector.Load_Error") + e.getMessage());
		} catch (ClassNotFoundException e) {
			JAlgoGUIConnector.getInstance().showErrorMessage(Messages.getString(
					"lambda", "ModuleConnector.Load_Error") +
					System.getProperty("line.separator") +
					Messages.getString("lambda", "ModuleConnector.File_damaged"));
		} catch (LambdaException e) {
			JAlgoGUIConnector.getInstance().showErrorMessage(e.getMessage());
		}
	}

	/**
	 * writes the current term and the self-defined shortcuts into file
	 * 
	 * @param out ObjectOutputStream
	 */
	public void saveData(ObjectOutputStream out) {
		try {
			List<Shortcut> shortcutList = ShortcutHandler.getInstance().getAllAvailabilShortCuts();

			int i = 0;
			for (Shortcut s : shortcutList) {
				if (!s.isPredefined()) {
					i++;
				}
			}

			out.writeObject(new Integer(i));            
			out.writeObject(termHistory.getFirstTerm());

			for (Shortcut s : shortcutList) {
				if (!s.isPredefined()) {
					out.writeObject(s.getRepresentation());
					out.writeObject(s.getRepresentedTerm().toString());
				}
			}
		} catch (IOException e) {
			JAlgoGUIConnector.getInstance().showErrorMessage(Messages.getString(
					"lambda", "ModuleConnector.Save_Error") + e.getMessage());
		}
	}

	/**  
	 * validates the input string
	 * 
	 * @param s input string
	 * @return null if input string is a valid term, otherwise a LambdaException
	 */
	public LambdaException validateInputString(String s) {
		try {
			parseUnit.parseString(s);
		} catch (LambdaException e) {
			return e;
		}
		return null;
	}

	/**  
	 * parses the input string and set the new term handler
	 * 
	 * @param s input string
	 * @param o observer 
	 * @return null if parsing successful, otherwise a LambdaException
	 */
	public LambdaException processString(String s, Observer o) {
		try {
			termHandler = parseUnit.parseString(s, o);
			workingPosition = "";
			return null;
		} catch (LambdaException e) {
			return e;
		}
	}

	/**
	 * save the input string in an empty CTerm, if term processing was successful
	 * 
	 * @param s input string
	 * @param o observer
	 * @return null if successful, otherwise a LambdaException
	 */
	public LambdaException processInputString(String s, Observer o) {
		termHistory.createTerm();
		LambdaException le = processString(s, o);
		if (le == null) {
			termHistory.addStep(s, "", 0);
		}
		return le;
	}

	/**  
	 * creates the term representation of the selected shortcut and saves it in the current CTerm
	 * 
	 * @return true if successful
	 */	
	public boolean eliminateShortcut() {
		if (termHandler.eliminateShortcut(workingPosition)) {
			termHistory.addStep(termHandler.toString(), "=", 0);
			workingPosition = "";
			return true;
		}
		return false;
	}

	/**  
	 * creates the term representation of all shortcuts at working position and saves it in the current CTerm
	 * 
	 * @return true if successful
	 */	
	public boolean eliminateAllShortcuts() {
		termHandler.eliminateAllShortcuts(workingPosition);
		if (!termHistory.getCurrentTerm().equals(termHandler.toString())) {
			termHistory.addStep(termHandler.toString(), "=", 0);
			workingPosition = "";
			return true;
		}
		return false;
	}

	/**  
	 * matches the selected term with its shortcut representation and save it in the current CTerm
	 * 
	 * @return true if successful
	 */		
	public boolean matchShortcut() {
		if (termHandler.matchShortcut(workingPosition)) {
			termHistory.addStep(termHandler.toString(), "=", 0);
			workingPosition = "";
			return true;
		}
		return false;
	}

	/**  
	 * matches all selected terms with their shortcut representations and save the result in the current CTerm
	 * 
	 * @return true if successful
	 */		
	public boolean makeAllShortcuts() {
		termHandler.makeAllShortcuts(workingPosition);
		if (!termHistory.getCurrentTerm().equals(termHandler.toString())) {
			termHistory.addStep(termHandler.toString(), "=", 0);
			workingPosition = "";
			return true;
		}
		return false;
	}

	/**
	 * does an alpha conversion at working position and save the result in the current CTerm
	 * 
	 * @param subst substitution variable
	 * @return true if successful, false if no conversion possible at working position
	 */
	public boolean doAlphaConversion(String subst) {
		if (!termHandler.alphaConvert(workingPosition, subst))
			return false;
		termHistory.addStep(termHandler.toString(), "\u03b1", 1);
		workingPosition = "";
		return true;
	}

	/**
	 * does an beta reduction at working position and save the result in the current CTerm
	 * 
	 * @return availability of beta reduction at working position
	 */
	public EAvailability doBetaReduction() {
		EAvailability availability = termHandler.betaReduce(workingPosition);
		if (availability == EAvailability.AVAILABLE) {
			termHistory.addStep(termHandler.toString(), "\u03b2", 1);
		}
		return availability;
	}

	/**
	 * does next useful step at working position and save the result in the current CTerm
	 * 
	 * @return applied operation as unicode character
	 */
	public String doStep() {
		eliminateAllShortcuts();
		String operation;
		EStepKind stepKind = termHandler.doLowLevelAutoStep(workingPosition);
		if (stepKind == EStepKind.ALPHA) {
			operation = "\u03b1";
		} else if (stepKind == EStepKind.BETA) {
			operation = "\u03b2";
		} else
			return null;
		termHistory.addStep(termHandler.toString(), operation, 1);
		workingPosition = "";
		return operation;
	}

	/**
	 * does a shortcut step at working position and save the result in the current CTerm
	 * 
	 * @return applied operation as unicode character
	 */
	public String doShortcutStep() {
		String operation;
		EStepKind stepKind = termHandler.doHighLevelAutoStep(workingPosition);
		if (stepKind == EStepKind.SHORTCUT) {
			operation = "*";
		} else
			return null;
		termHistory.addStep(termHandler.toString(), operation, 1);
		workingPosition = "";
		return operation;
	}

	/**
	 * does fully reduce the term at working position and save the result in the current CTerm
	 * breakes processing after 100 steps or if the term hasn't changed as an infinte loop protection
	 * 
	 * @return applied operation as unicode character
	 */
	public String doAllSteps() {
		int i = 0;
		String operation = null;
		EStepKind stepKind, oldStepKind = EStepKind.NONE;

		stepKind = termHandler.doHighLevelAutoStep(workingPosition);
		while (stepKind != EStepKind.NONE) {
			if (termHandler.toString().equals(termHistory.getCurrentTerm()))
				break;
			if (i >= 100)
				break;
			i++;
			oldStepKind = stepKind;
			stepKind = termHandler.doHighLevelAutoStep(workingPosition);			
		}

		termHandler.eliminateAllShortcuts(workingPosition);

		stepKind = termHandler.doLowLevelAutoStep(workingPosition);
		while (stepKind != EStepKind.NONE) {
			if (termHandler.toString().equals(termHistory.getCurrentTerm()))
				break;
			if (i >= 100)
				break;
			i++;
			oldStepKind = stepKind;
			stepKind = termHandler.doLowLevelAutoStep(workingPosition);
		}

		if (oldStepKind == EStepKind.ALPHA) {
			operation = "\u03b1";
		} else if (oldStepKind == EStepKind.BETA) {
			operation = "\u03b2";
		} 
		if (oldStepKind == EStepKind.SHORTCUT || i > 1) {
			operation = "*";
		}

		if (i > 0) {
			termHistory.addStep(termHandler.toString(), operation, i);
			workingPosition = "";
		}

		if (makeAllShortcuts()) {
			operation = "=";
		}

		return operation;
	}

	/**
	 * @return true if term is normalized
	 */
	public boolean isNormalized() {
		if (termHandler.getPossibleBetaReductions("").isEmpty() && termHandler.getNeededAlphaConversions("").isEmpty())
			return true;
		return false;
	}

	/**
	 * @return true if undo step is possible
	 */
	public boolean isUndoStepPossible() {
		return termHistory.isUndoStepPossible();
	}

	/**
	 * @return true if redo step is possible
	 */
	public boolean isRedoStepPossible() {
		return termHistory.isRedoStepPossible();
	}

	/**
	 * undo last step
	 * 
	 * @param o observer
	 * @return applied operation as unicode character
	 */
	public String undoStep(Observer o) {
		if (isUndoStepPossible()) {
			termHistory.undoStep();
			processString(termHistory.getCurrentTerm(), o);
			return termHistory.getCurrentOperation();
		}
		return null;
	}

	/**
	 * undo all steps
	 * 
	 * @param o observer
	 * @return applied operation as unicode character
	 */
	public String undoAllSteps(Observer o) {
		if (isUndoStepPossible()) {
			termHistory.undoAllSteps();
			processString(termHistory.getCurrentTerm(), o);
			return termHistory.getCurrentOperation();
		}
		return null;
	}

	/** 
	 * redo step
	 * 
	 * @param o observer
	 * @return applied operation as unicode character
	 */
	public String redoStep(Observer o) {
		if (isRedoStepPossible()) {
			termHistory.redoStep();
			processString(termHistory.getCurrentTerm(), o);
			return termHistory.getCurrentOperation();
		}
		return null;
	}

	/**
	 * redo all steps
	 * 
	 * @param o observer
	 * @return applied operation as unicode character
	 */
	public String redoAllSteps(Observer o) {
		if (isRedoStepPossible()) {
			termHistory.redoAllSteps();
			processString(termHistory.getCurrentTerm(), o);
			return termHistory.getCurrentOperation();
		}
		return null;
	}

	/** 
	 * sets a new working position
	 * 
	 * @param pos working position
	 */
	public void selectTerm(String pos) {
		workingPosition = pos;
	}

	/** 
	 * @return list of unused variables
	 */
	public Set<String> getAllUnusedVars() {
		return termHandler.getAllUnusedVars();
	}

	/**
	 * returns a list of positions of free vars in the selected subterm
	 * 
	 * @return list of positions of free vars in the selected subterm
	 */
	public List<String> getFreeVars() {
		List<String> l = new LinkedList<String>();

		//BindingIDs für Teilterm berechnen
		termHandler.getSubTerm(workingPosition).recalculateBindingIDs();

		for (Atom t : termHandler.getSubTerm(workingPosition).getFreeVarOccurences())
			l.add(t.getPosition());

		//BindingIDs für root-Term wiederherstellen
		termHandler.getSubTerm("").recalculateBindingIDs();

		return l;
	}

	/**
	 * returns a list of positions of bound vars in the selected subterm
	 * 
	 * @return list of positions of bound vars in the selected subterm
	 */
	public List<String> getBoundVars() {
		List<String> l = new LinkedList<String>();

		//BindingIDs für Teilterm berechnen
		termHandler.getSubTerm(workingPosition).recalculateBindingIDs();

		for (Atom t : termHandler.getSubTerm(workingPosition).getBoundVarOccurences())
			l.add(t.getPosition());

		//BindingIDs für root-Term wiederherstellen
		termHandler.getSubTerm("").recalculateBindingIDs();

		return l;
	}

	/**
	 * @return current term
	 */
	public String toString() {
		return termHandler.toString();
	}

}
