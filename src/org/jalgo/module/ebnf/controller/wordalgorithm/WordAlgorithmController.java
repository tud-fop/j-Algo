package org.jalgo.module.ebnf.controller.wordalgorithm;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.MainController;
import org.jalgo.module.ebnf.ModuleConnector;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.InitializationFailedException;
import org.jalgo.module.ebnf.gui.wordalgorithm.GuiController;
import org.jalgo.module.ebnf.model.syndia.*;
import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;
import org.jalgo.module.ebnf.util.*;
import java.util.List;
import java.util.Observer;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.JPanel;

/**
 * This controller manages the interaction between the Algorithm-GUI and the
 * Algorithm-Model.
 * 
 * It also controlls the algorithm by generating <code>AlgorithmAction</code>s.
 * 
 * @author Claas Wilke
 */
public class WordAlgorithmController {

	// ActionStack is used to push and pop Actions to have the possibility
	// of having steps undone and redone during algorithm.
	private ActionStack myActionStack;

	// Model during Algorithm (word, output, Stack etc)
	private WordAlgoModel myModel;

	private MainController mainController;

	private ModuleConnector moduleConnector;

	// Used during mouseOver events.
	// Needed to reconstruct the Explanations after MouseOver
	private String oldExplanation;

	private String oldWarning;

	private boolean oldStackHighlightStatus;

	// Must have this reference to beware the guiController is not
	// thrown into the GarbageCollector.
	@SuppressWarnings("unused")
	private GuiController guiController;

	// neded to parse Words end check if they only contain
	// TerminalSymbols.
	private WordParser myParser;

	/**
	 * Creates a new WordAlgorithmController
	 * 
	 * @param mainController
	 *            The <code>MainController</code> which controlles the
	 *            EBNF-Module. Should be the Creator of this GuiController.
	 * @param moduleConnector
	 *            The <code>ModuleConnector</code>.
	 * @param mySynDiaSystem
	 *            The SynDiaSystem which should used during the algorithm.
	 * @throws <code>InitiliazationFaildException</code> if an error occurs
	 *             during initialization.
	 */
	public WordAlgorithmController(MainController mainController,
			ModuleConnector moduleConnector, SynDiaSystem mySynDiaSystem)
			throws InitializationFailedException {
		// The Attributes are initialized.
		// If initialization fails, an Exception is thrown.
		try {
			myModel = new WordAlgoModel(mySynDiaSystem);
			initialize();
		} catch (InitializationFailedException e) {
			throw e;
		}

		this.mainController = mainController;
		this.moduleConnector = moduleConnector;
	}

	/**
	 * Creates a new WordAlgorithmController with a GuiController.
	 * 
	 * If initilization fails, the WordAlgorithmController is switching back to
	 * SynDiaView.
	 * 
	 * @param mainController
	 *            The <code>MainController</code> which controlles the
	 *            EBNF-Module. Should be the Creator of this
	 *            <code>GuiController</code>.
	 * @param moduleConnector
	 *            The <code>ModuleConnector</code>.
	 * @param contentPane
	 *            The <code>JPanel</code> the <code>GuiController</code>
	 *            should use to paint its output.
	 * @param mySynDiaSystem
	 *            The SynDiaSystem which should used during the algorithm.
	 */
	public WordAlgorithmController(MainController mainController,
			ModuleConnector moduleConnector, JPanel contentPane,
			SynDiaSystem mySynDiaSystem) {

		// Curser is changed to waiting
		contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// The Attributes are initialized.
		// If initialization fails, an Exception is thrown.
		try {
			myModel = new WordAlgoModel(mySynDiaSystem);
			initialize();
		} catch (InitializationFailedException e) {
			System.out.println(e.getMessage());
			System.out.println("Switch back to SynDiaView.");
			guiController.switchToSynDiaView();
		}

		this.mainController = mainController;
		this.moduleConnector = moduleConnector;

		// Disable saveSettings
		this.moduleConnector.setSaveStatus(SaveStatus.NOTHING_TO_SAVE);

		// Create GuiController
		this.guiController = new GuiController(contentPane, mySynDiaSystem,
				this);

		// Rechange Curser to default after initilization
		contentPane
				.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * Initializes the WordAlgorithmController. Used by the Constructor.
	 * 
	 */
	private void initialize() {
		// Creates a new ActionStack used during algorithm.
		myActionStack = new ActionStack();

		myParser = new WordParser(myModel);

		oldExplanation = "";
		oldWarning = "";
		oldStackHighlightStatus = false;
	}

	/**
	 * This method returns the running status of the algorithm.
	 * 
	 * @return True if Algorithm is already started.
	 */
	public Boolean isAlgorithmRunning() {
		return myModel.isAlgorithmRunning();
	}

	/**
	 * This method returns the finished status of the algorithm.
	 * 
	 * @return True if Algorithm is already finished.
	 */
	public Boolean isAlgorithmFinished() {
		return myModel.isAlgorithmFinished();
	}

	/**
	 * This method returns the warnings status of the algorithm.
	 * 
	 * @return True if warnings are enabled.
	 */
	public Boolean isWarningsOn() {
		return myModel.isWarningsOn();
	}

	/**
	 * This method returns true, if a redo is possible.
	 * 
	 * @return True if a redo is possible.
	 */
	public Boolean isRedoPossible() {
		return myActionStack.isRedoPossible();
	}

	/**
	 * This method returns true, if a undo is possible.
	 * 
	 * @return True if a undo is possible.
	 */
	public Boolean isUndoPossible() {
		return myActionStack.isUndoPossible();
	}

	/**
	 * This method returns true, if the algorithm is during a Jump to a Diagram.
	 * 
	 * @return True if the algorithm is during a Jumpt to a Diagram.
	 */
	public Boolean isJumpToDiagram() {
		return myModel.isJumpToDiagram();
	}

	/**
	 * This method returns true, if the algorithm can reach the end of a
	 * Diagram.
	 * 
	 * @return True if the algorithm can reach the end of a Diagram.
	 */
	public Boolean isEndReached() {
		return myModel.isEndReached();
	}

	/**
	 * This method returns true, if the algorithm can only reach the end of a
	 * Diagram.
	 * 
	 * @return True if the algorithm can only reach the end of a Diagram.
	 */
	public Boolean isOnlyEndReachable() {
		return (myModel.isEndReached() && myModel.isNoElementReachable());
	}

	/**
	 * This method returns true, if the algorithm can reach a
	 * <code>SynDiaElem</code>.
	 * 
	 * @param aSynDiaElem
	 *            The <code>SynDiaElem</code> that should be reached
	 * @return True, if the algorithm can reach a <code>SynDiaElem</code>.
	 */
	public Boolean isElementReachable(SynDiaElem aSynDiaElem) {
		return myModel.isElementReachable(aSynDiaElem);
	}

	/**
	 * This method returns true, if the <code>aSynDiaElem</code> is in
	 * <code>aDiagram</code>.
	 * 
	 * @param aSynDiaElem
	 *            The <code>SynDiaElem</code> that should be in a
	 *            <code>aDiagram</code>
	 * @param aDiagram
	 *            The <code>SyntaxDiagram</code>, <code>aSynDiaElem</code>
	 *            should be in.
	 * @return True, if the algorithm can reach a <code>SynDiaElem</code>.
	 */
	public Boolean isElementInDiagram(SynDiaElem aSynDiaElem,
			SyntaxDiagram aDiagram) {
		return myModel.isElementInDiagram(aSynDiaElem, aDiagram);
	}

	/**
	 * This method returns true, if the actual position in behind an Element.
	 * 
	 * @return True if the actual position in behind an Element.
	 */
	public Boolean isPositionBehindElem() {
		return myModel.isPositionBehindElem();
	}

	/**
	 * This method returns true, if <code>aWord</code> is a String which only
	 * contains valid <code>TerminalSymbols</code> of the Controllers
	 * <code>WordAlgoModel</code>.
	 * 
	 * @param word
	 *            The String which should be checked.
	 * @return True if <code>aWord</code> only contains valid
	 *         <code>TerminalSymbols</code>.
	 */
	public Boolean isWordValid(String aWord) {
		return myParser.isWordValid(aWord);
	}

	/**
	 * This method returns the word which should be generated by the algorithm.
	 * 
	 * @return The word which should be generated
	 */
	public String getWord() {
		return myModel.getWord();
	}

	/**
	 * This method returns the word which was already generated by the algoritm.
	 * 
	 * @return The word which was already generated.
	 */
	public String getOutput() {
		return myModel.getOutput();
	}

	/**
	 * This method returns the explanation of the actual algorithm step.
	 * 
	 * @return The explanation of the actual step.
	 */
	public String getExplanation() {
		return myModel.getExplanation();
	}

	/**
	 * This method returns the last warning generated by the algorithm. (Only if
	 * warnings are enabled).
	 * 
	 * @return Last warning generated during algorithm.
	 */
	public String getWarning() {
		return myModel.getWarning();
	}

	/**
	 * This method returns the actual position in a SyntaxDiagram during
	 * algorithm.
	 * 
	 * @return Actual algorithm position in a SyntaxDiagram.
	 */
	public SynDiaElem getPosition() {
		return myModel.getPosition();
	}

	/**
	 * This method returns the SynDiaSystem used by the algorithm.
	 * 
	 * @return The SynDiaSystem used by the algorithm.
	 */
	public SynDiaSystem getSynDiaSystem() {
		return myModel.getSynDiaSystem();
	}

	/**
	 * This method returns the return marks of all Variables pushed on the Stack
	 * starting with the oldest.
	 * 
	 * @return List containing return marks (int).
	 */
	public List getAdressNumbersFromStack() {
		return myModel.getAdressNumbersFromStack();
	}

	/**
	 * This method returns the adress number for a specific Variable used as
	 * return adress during the algorithm.
	 * 
	 * @param var
	 *            The Variable which return adress should be returned.
	 */
	public Number getAdressNumber(Variable var) {
		return myModel.getAdressNumber(var);
	}

	/**
	 * Returns the Font installed by the MainController to print
	 * EBNF-Definitions.
	 * 
	 * @return Returns the Font installed by the MainController to print
	 *         EBNF-Definitions.
	 */
	public Font getEbnfFont() {
		return mainController.getEbnfFont();
	}

	/**
	 * Returns the ModuelConnector.
	 * 
	 * @return Returns the ModuelConnector.
	 */
	public ModuleConnector getModuleConnector() {
		return moduleConnector;
	}

	/**
	 * Returns the MainController.
	 * 
	 * @return Returns the MainController.
	 */
	public MainController getMainController() {
		return mainController;
	}

	/**
	 * This method enables warnings during algorithm.
	 */
	public void enableWarnings() {
		myModel.enableWarnings();
	}

	/**
	 * This method disables warnings during algorithm.
	 * 
	 */
	public void disableWarnings() {
		myModel.disableWarnings();
	}

	/**
	 * This method sets the word which should be generated by the algorithm.
	 * 
	 * @param word
	 *            The word which should be generated.
	 */
	public void setWord(String word) {
		myModel.setWord(word);
	}

	/**
	 * This method adds an <code>Observer</code> to the ObserverList of the
	 * <code>ActionStack</code>.
	 * 
	 * @param anObserver
	 *            The <code>Observer</code> that should be added.
	 */
	public void addActionStackObserver(Observer anObserver) {
		myActionStack.addObserver(anObserver);
	}

	/**
	 * This method adds an <code>Observer</code> to the ObserverList of the
	 * Model used during algorithm.
	 * 
	 * @param anObserver
	 *            The <code>Observer</code> that should be added.
	 */
	public void addModelObserver(Observer anObserver) {
		myModel.addObserver(anObserver);
	}

	/**
	 * This method makes a step in the algorithm undone if possible.
	 * 
	 * @return True, if Undo was peformed successfully.
	 */
	public boolean undo() {
		try {
			myActionStack.undo();
			myModel.notifyObservers();
			return true;
		}
		// Exceptions are thrown by the ActionStack if undo is not possible.
		// These Exceptions arent handled.
		catch (Exception e) {
			myModel.notifyObservers();
			return false;
		}
	}

	/**
	 * This method redoes a step in the algorithm if possible.
	 * 
	 * @return True, if redo was peformed successfully.
	 */
	public boolean redo() {
		try {
			myActionStack.redo();
			myModel.notifyObservers();
			return true;
		}
		// Exceptions are thrown by the ActionStack if undo is not possible.
		// These Exceptions arent handled.
		catch (Exception e) {
			myModel.notifyObservers();
			return false;
		}
	}

	/**
	 * This mehtod performs the firs step in the algorithm. It starts the
	 * algorihm
	 * 
	 * @return True, if Step was peformed successfully.
	 */
	public boolean startAlgorithm() {
		try {
			AlgorithmStartAction anAction = new AlgorithmStartAction(myModel,
					null);
			myActionStack.perform(anAction);
			// The Observers must be informed, that model has changed.
			myModel.notifyObservers();
			return true;
		}
		// Exceptions are thrown by the ActionStack if startAlgorithm is not
		// possible.
		// These Exceptions arent handled.
		catch (Exception e) {
			myModel.notifyObservers();
			return false;
		}
	}

	/**
	 * This method performs a jump back to a Diagram left before. The user can
	 * also return to a Diagram not pushed onto the Stack before. The algorithm
	 * will warn the user if warnings were enabled.
	 * 
	 * @param aVariable
	 *            The Variable the algorithm should return to.
	 * @return True, if Step was peformed successfully.
	 */
	public boolean returnToDiagram(Variable aVariable) {
		try {
			ReturnToDiagramAction anAction = new ReturnToDiagramAction(myModel,
					aVariable);
			myActionStack.perform(anAction);
			// The Observers must be informed, that model has changed.
			myModel.notifyObservers();
			return true;
		}
		// Exceptions are thrown by the ActionStack if startAlgorithm is not
		// possible.
		// These Exceptions arent handled.
		catch (Exception e) {
			myModel.notifyObservers();
			return false;
		}
	}

	/**
	 * This method performs a jump during the algorithm from one Diagram
	 * toanother. The Action is pushed onto the <code>ActionStack</code> and
	 * can be made undone. The Action is just performed, if a jump is possible.
	 * 
	 * @param aDiagram
	 *            The Diagram, the algorithm should jump to.
	 * @return True, if Step was peformed successfully.
	 */
	public boolean jumpToDiagram(SyntaxDiagram aDiagram) {
		try {
			JumpToDiagramAction anAction = new JumpToDiagramAction(myModel,
					aDiagram);
			myActionStack.perform(anAction);
			// The Observers must be informed, that model has changed.
			myModel.notifyObservers();
			return true;
		}
		// Exceptions are thrown by the ActionStack if startAlgorithm is not
		// possible.
		// These Exceptions arent handled.
		catch (Exception e) {
			myModel.notifyObservers();
			return false;
		}
	}

	/**
	 * This method implements the exit from a Diagram, if the actual Position is
	 * the end of a Diagram.
	 * 
	 * @return True, if Step was peformed successfully.
	 */
	public boolean leaveDiagram() {
		try {
			LeaveDiagramAction anAction = new LeaveDiagramAction(myModel, null);
			myActionStack.perform(anAction);
			// The Observers must be informed, that model has changed.
			myModel.notifyObservers();
			return true;
		}
		// Exceptions are thrown by the ActionStack if startAlgorithm is not
		// possible.
		// These Exceptions arent handled.
		catch (Exception e) {
			myModel.notifyObservers();
			return false;
		}
	}

	/**
	 * This method performs the passing of a <code>Variable</code> during the
	 * Algorithm. The Action is pushed onto the <code>ActionStack</code> and
	 * can be made undone. The Action is just performed, if the Variable could
	 * be reached.
	 * 
	 * @param aVar
	 *            The <code>Variable</code> the algorithm should pass.
	 * @return True, if Step was peformed successfully.
	 */
	public boolean gotoVariable(Variable aVar) {
		try {
			VariableAction anAction = new VariableAction(myModel, aVar);
			myActionStack.perform(anAction);
			// OldExplanation must be changed
			// To prevent wrong Explanation after MouseOver
			this.oldExplanation = myModel.getExplanation();
			// The Observers must be informed, that model has changed.
			myModel.notifyObservers();
			return true;
		}
		// Exceptions are thrown by the ActionStack if startAlgorithm is not
		// possible.
		// These Exceptions arent handled.
		catch (Exception e) {
			myModel.notifyObservers();
			return false;
		}
	}

	/**
	 * This method performs the passing of a <code>TerminalSymbol</code>
	 * during the algorithm. The Action is pushed onto the
	 * <code>ActionStack</code> and can be made undone. The Action is just
	 * performed, if the Variable could be reached.
	 * 
	 * @param aTerminal
	 *            The <code>TerminalSymbol</code> the algorithm should pass.
	 * @return True, if Step was peformed successfully.
	 */
	public boolean gotoTerminal(TerminalSymbol aTerminal) {
		try {
			TerminalAction anAction = new TerminalAction(myModel, aTerminal);
			myActionStack.perform(anAction);
			// OldExplanation must be changed
			// To prevent wrong Explanation after MouseOver
			this.oldExplanation = myModel.getExplanation();
			// The Observers must be informed, that model has changed.
			myModel.notifyObservers();
			return true;
		}
		// Exceptions are thrown by the ActionStack if startAlgorithm is not
		// possible.
		// These Exceptions arent handled.
		catch (Exception e) {
			myModel.notifyObservers();
			return false;
		}
	}

	/**
	 * This method performs the passing of a Concatenation in a
	 * <code>Branch</code> or <code>Repetition</code> during the algorithm.
	 * The Action is pushed onto the <code>ActionStack</code> and can be made
	 * undone. The Action is just performed, if the Variable could be reached.
	 * 
	 * @param aConcat
	 *            The <code>Concatenation</code> clicked on.
	 * @return True, if Step was peformed successfully.
	 */
	public boolean gotoSplit(Concatenation aConcat) {
		try {
			SplitAction anAction = new SplitAction(myModel, aConcat);
			myActionStack.perform(anAction);
			// OldExplanation must be changed
			// To prevent wrong Explanation after MouseOver
			this.oldExplanation = myModel.getExplanation();
			// The Observers must be informed, that model has changed.
			myModel.notifyObservers();
			return true;
		}
		// Exceptions are thrown by the ActionStack if startAlgorithm is not
		// possible.
		// These Exceptions arent handled.
		catch (Exception e) {
			myModel.notifyObservers();
			return false;
		}
	}

	/**
	 * This method performs an MouseOverEvent over a <code>Variable</code> a
	 * <code>TerminalSymbol</code> or a
	 * <code>Concatenation>. (The Entry onto the Element) Changes
	 * the Explanation.
	 * 
	 * @param myCaller
	 *            The <code>SynDiaElem</code> which caused the MouseOverEvent.
	 * @param alternativeMode
	 *            If true, if myCaller is a <code>Variable</code>, the
	 *            MouseOverEvent is handled as a Event over a ReturnAdress.
	 * @return true if the Element of the MouseOverEvent could be reached.
	 *
	 */
	public boolean overElementEntry(SynDiaElem myCaller, boolean alternativeMode) {

		// Save oldExplanation for method overElementExit
		oldExplanation = myModel.getExplanation();
		oldWarning = myModel.getWarning();
		oldStackHighlightStatus = myModel.isStackHighlighted();

		boolean result = false;

		if (myCaller instanceof Variable) {
			if (myModel.isAlgorithmRunning() || myModel.isAlgorithmFinished()) {
				// If it is no return adress event
				if (!alternativeMode) {
					// If Element can be reached.
					if (myModel.isElementReachable(myCaller)
							&& !myModel.isJumpToDiagram()) {
						myModel.setExplanation(Messages.getString("ebnf",
								"WordAlgo.MouseOver_Variable"));
						result = true;
					}
				} else {
					if (isOnlyEndReachable() && !myModel.isJumpToDiagram()) {
						myModel.setExplanation(Messages.getString("ebnf",
								"WordAlgo.MouseOver_ReturnAdress"));
						myModel.enableStackHighlighted();
						result = true;
					}
				}
			}
		}

		if (myCaller instanceof TerminalSymbol) {
			if (myModel.isAlgorithmRunning() || myModel.isAlgorithmFinished()) {
				// If Element can be reached.
				if (myModel.isElementReachable(myCaller)) {
					myModel.setExplanation(Messages.getString("ebnf",
							"WordAlgo.MouseOver_Terminal"));
					result = true;
				}
			}
		}

		if (myCaller instanceof Concatenation) {
			if (myModel.isAlgorithmRunning() || myModel.isAlgorithmFinished()) {
				Concatenation aConcat = (Concatenation) myCaller;
				// If Element can be reached.
				if (myModel.isElementReachable(myCaller)
						&& aConcat.getNumberOfElems() == 0) {
					myModel.setExplanation(Messages.getString("ebnf",
							"WordAlgo.MouseOver_Split"));
					result = true;
				}
			}
		}
		myModel.notifyObservers();
		return result;

	}

	/**
	 * This method performs an MouseOverEvent over a <code>SyntaxDiagram</code>.
	 * (The Entry onto the Element) Changes the Explanation.
	 * 
	 * @param myCaller
	 *            The <code>SyntaxDiagram</code> which caused the
	 *            MouseOverEvent.
	 * @param alternativeMode
	 *            If true, if myCaller is a <code>Variable</code>, the
	 *            MouseOverEvent is handled as a Event over a ReturnAdress.
	 * @return true if the Element of the MouseOverEvent could be reached.
	 * 
	 */
	@SuppressWarnings("static-access")
	public boolean overElementEntry(SyntaxDiagram myCaller,
			boolean alternativeMode) {

		// Save oldExplanation for method overElementExit
		oldExplanation = myModel.getExplanation();
		oldWarning = myModel.getWarning();
		oldStackHighlightStatus = myModel.isStackHighlighted();

		boolean result = false;

		if (myCaller instanceof SyntaxDiagram) {
			if (myModel.isAlgorithmRunning()) {
				myModel.setExplanation(Messages.getString("ebnf",
						"WordAlgo.MouseOver_Diagram"));
				result = true;
			}
		}

		myModel.notifyObservers();
		return result;

	}

	/**
	 * This method performs an MouseOverEvent over the Exit of a
	 * <code>SyntaxDiagram</code>. (The Entry onto the Element) Changes the
	 * Explanation.
	 * 
	 * @param myCaller
	 *            The <code>SyntaxDiagram</code> which caused the
	 *            MouseOverEvent.
	 * @return true if the Element of the MouseOverEvent can be reached.
	 */
	public boolean overSynDiaExitEntry(SyntaxDiagram myCaller) {

		// Save oldExplanation for method overElementExit
		oldExplanation = myModel.getExplanation();

		boolean result = false;

		// Algorithm must be runngin, end must be reachable but isn't reached
		// yet
		// and it must be the end of the diagram in which is the actual
		// position.
		if (myModel.isEndReached() && !isOnlyEndReachable()) {
			if (myModel.isAlgorithmRunning()
					&& myModel.isElementInDiagram(myModel.getPosition(),
							myCaller)) {
				myModel.setExplanation(Messages.getString("ebnf",
						"WordAlgo.MouseOver_DiagramExit"));
				result = true;
			}
		}

		myModel.notifyObservers();
		return result;

	}

	/**
	 * This method performs an MouseOverEvent over a <code>Variable</code> or
	 * a <code>TerminalSymbol</code>, a
	 * <code>Concatenation> or a <code>SyntaxDiagram</code>. 
	 * (The Exit from the Element) Changes the Explanation.
	 * 	 *
	 */
	public void overElementExit() {

		myModel.setExplanation(oldExplanation);
		myModel.setWarning(oldWarning);
		if (oldStackHighlightStatus)
			myModel.enableStackHighlighted();
		else
			myModel.disableStackHighlighted();
		myModel.notifyObservers();

	}

	/**
	 * This method performs an MouseOverEvent over the end of a
	 * <code>SyntaxDiagram</code>. (The Exit from the Element) Changes the
	 * Explanation.
	 * 
	 */
	public void overSynDiaExitExit() {

		myModel.setExplanation(oldExplanation);
		myModel.notifyObservers();

	}

	/**
	 * This method resets the algorithm. Throws InitializationFailedException if
	 * reinitialization fails.
	 * 
	 * @throws InitializationFailedException
	 *             if reinitialization fails.
	 */
	public void resetAlgorithm() throws InitializationFailedException {
		try {
			// ActionStack is cleared
			myActionStack.clear();
			// and Model is reseted too.
			myModel.reset();
			myModel.notifyObservers();
		} catch (InitializationFailedException e) {
			System.out.println(e.getMessage());
			System.out
					.println("Error occured during reset of Algorithm. Switch back to SynDiaView.");
			guiController.switchToSynDiaView();
		}
	}

	/**
	 * Performs a reswitch to SynDiaGui.
	 * 
	 * <b>Attention</b>: This method does not reset the GUI. To reset the Gui,
	 * calle the <code>GuiController</code> which controlls the gui via his
	 * methos <code>switchToSynDiaView()</code>.
	 * 
	 */
	public void switchToSynDiaView() {
		// Switch to SynDiaView
		mainController.setSynDiaDisplayMode(myModel.getSynDiaSystem());
	}

	/**
	 * Method can be used to set the Model status changed and notify its
	 * observers.
	 * 
	 */
	public void notifyModelObservers() {
		myModel.setModelChanged();
		myModel.notifyObservers();
	}

}
