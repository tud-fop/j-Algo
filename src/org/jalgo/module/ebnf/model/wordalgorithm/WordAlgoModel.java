package org.jalgo.module.ebnf.model.wordalgorithm;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Stack;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.InconsistentDiagramSystemException;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.InitializationFailedException;
import org.jalgo.module.ebnf.model.syndia.Branch;
import org.jalgo.module.ebnf.model.syndia.Concatenation;
import org.jalgo.module.ebnf.model.syndia.ElementNotFoundException;
import org.jalgo.module.ebnf.model.syndia.Repetition;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.model.syndia.TerminalSymbol;
import org.jalgo.module.ebnf.model.syndia.Variable;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;

/**
 * This class represents the Model used by the WordAlgorithm The model has to be
 * in a seperate class because it is observable.
 * 
 * @author Claas Wilke
 * 
 */
public class WordAlgoModel extends Observable {

	// Specifies, if the algorithm is started yet.
	private boolean algorithmRunning;

	// Specifies, if the algorithm is finished yet.
	// (Successfully or not)
	private boolean algorithmFinished;

	// Specifies, if the algorithm should show warnings.
	private boolean withWarnings;

	// Specifies, if the algorithm performs a jumpt to a Diagram via a
	// Variable. Needed to decide wether or not a return to a Diagram
	// is possible when a Variable is the (actual) position.
	private boolean duringJumpToDiagram;

	// The word which should be generated during the algorithm
	private String word;

	// The word which will be generated during the algorithm
	private String output;

	// This String represents the explanations printed during the algorithm
	private String explanation;

	// This String contains the last warning thrown by the algorithm.
	// (Warnings are just generated if the Boolean withWarnings is true)
	private String warning;

	// The SynDiaSystem used during the algorithm
	private SynDiaSystem mySynDiaSystem;

	// The actual position in a SyntaxDiagram during the algorithm
	private SynDiaElem position;

	// Specifies, if the actaulPosition is behind or in front of the Element.
	private boolean positionBehindElem;

	// The Stack to push and pop Adresses during the algorithm //
	private Stack<Variable> myStack;

	private boolean stackHighlighted;

	// A Map of all the rsAdresses used during the algorithm Each Variable in
	// the SynDiaSystem has it's own adress (int).
	private Map<Variable, Number> adrNumbers;

	// This int is used in the recursive method generatedAdresses and therefore
	// it must be global.
	private int numberOfVariableInstances;

	// Specifies, if the end of a Diagram was reached.
	private boolean endReached;

	// Contains all Elements in the SynDiaSystem that can be reached
	// (Could be SynDiaElems or SyntaxDiagrams).
	private List<Object> allElemsReachable;

	// The Color, the Stack should be highlighte with
	private Color stackColor = RenderElement.HIGHLIGHT_BLUE;

	// Sepecifies, if the algorithm was finished successfully or not
	private boolean finishedWithSuccess;

	/**
	 * Constructor constructs a new WordAlgoModel. Throws
	 * <code>InitializationFailedException</code> if initialization fails.
	 * 
	 * @param aSynDiaSystem
	 *            The <code>SynDiaSystem</code>, the algorithm should work
	 *            with.
	 */
	public WordAlgoModel(SynDiaSystem aSynDiaSystem)
			throws InitializationFailedException {
		try {
			initialize(aSynDiaSystem);
		} catch (InitializationFailedException e) {
			throw e;
		}
	}

	/**
	 * Initializes the WordAlgorithmModel. Used by the Constructor.
	 * 
	 * @param mySynDiaSystem
	 *            The SynDiaSystem which should used during the algorithm.
	 */
	private void initialize(SynDiaSystem aSynDiaSystem)
			throws InitializationFailedException {

		algorithmRunning = false;

		algorithmFinished = false;

		withWarnings = true;

		duringJumpToDiagram = false;

		word = "";

		output = "";

		explanation = Messages.getString("ebnf",
				"WordAlgo.Explanation_BeforeStart");

		warning = "";

		positionBehindElem = false;

		stackHighlighted = false;

		finishedWithSuccess = false;

		mySynDiaSystem = aSynDiaSystem;

		// Gets the first Concatenation of the StartDiagram.
		// If getting Concatenation fails, an Exception is thrown.
		try {
			String startDiagramName = mySynDiaSystem.getStartDiagram();
			SyntaxDiagram myStartDiagram = mySynDiaSystem
					.getSyntaxDiagram(startDiagramName);
			Concatenation startRootConcat = myStartDiagram.getRoot();
			position = startRootConcat;
			// Update Set with all Elements which are reachable
			allElemsReachable = new ArrayList<Object>();
			addAllElemsReachable(startRootConcat);
		} catch (ElementNotFoundException exception) {
			throw new InitializationFailedException(
					"Error during initialization of WordAlgoModel: "
							+ "DiagramSystem: StartDiagram not found.");
		}

		myStack = new Stack<Variable>();

		// Now an adress mark for each Variable in the Diagrams is generated.
		// Using the private Method generateAdresses.
		// If generating adresses fails, an Exception is thrown.
		adrNumbers = new HashMap<Variable, Number>();
		numberOfVariableInstances = 0;
		try {
			generateAdresses();
		} catch (InconsistentDiagramSystemException e) {
			throw new InitializationFailedException(e.getMessage());
		}
	}

	/**
	 * This method generates an adress for each Variable in the SynDiaSystem and
	 * enters the adress into the Map myAdresses.
	 */
	/*
	 * It uses the private method addAdressOfAnElement, wich parses the
	 * SynDiaSystem recursive.
	 */
	public void generateAdresses() throws InconsistentDiagramSystemException {
		List<String> diagramList = mySynDiaSystem.getLabelsOfVariables();
		for (String aVariable : diagramList) {
			try {
				Concatenation aConcat = mySynDiaSystem.getSyntaxDiagram(
						aVariable).getRoot();
				addAdressOfAnElement(aConcat);
			} catch (Exception e) {
				throw new InconsistentDiagramSystemException(
						"Error during generation of "
								+ "return adresses. SynDiaSystem inconsistent.");
			}

		}
	}

	/**
	 * This mehtod generates a new Adress for a SynDiaElem and puts the adress
	 * into the Map adrNumbers.
	 * 
	 * @param anElement
	 */
	private void addAdressOfAnElement(SynDiaElem anElement) {
		// If the element is a Concatenation, the mehtod is used recursive for
		// all the elements inside the Concatenation.
		if (anElement instanceof Concatenation) {
			int n = ((Concatenation) anElement).getNumberOfElems();
			for (int i = 0; i < n; i++) {
				addAdressOfAnElement(((Concatenation) anElement)
						.getSynDiaElem(i));
			}
		}
		// If the element is a Branch, the method is used recursive for the left
		// and the right path of the Branch.
		else if (anElement instanceof Branch) {
			addAdressOfAnElement(((Branch) anElement).getLeft());
			addAdressOfAnElement(((Branch) anElement).getRight());
		}
		// If the element is a Repetition, the method is used recursive for the
		// first and the second part of the Repetition.
		else if (anElement instanceof Repetition) {
			addAdressOfAnElement(((Repetition) anElement).getLeft());
			addAdressOfAnElement(((Repetition) anElement).getRight());
		}
		// If the Element is a Variable, a new ReturnAdress is genereated
		else if (anElement instanceof Variable) {
			// Increment to avoid that the number was used before.
			numberOfVariableInstances++;
			adrNumbers.put((Variable) anElement, numberOfVariableInstances);
		}

	}

	/**
	 * This method returns the running status of the algorithm.
	 * 
	 * @return True if Algorithm is already started.
	 */
	public boolean isAlgorithmRunning() {
		return algorithmRunning;
	}

	/**
	 * This method returns the finished status of the algorithm.
	 * 
	 * @return True if Algorithm is already finished. (Successfully or not)
	 */
	public boolean isAlgorithmFinished() {
		return algorithmFinished;
	}

	/**
	 * This method returns the warnings status of the algorithm.
	 * 
	 * @return True if warnings are enabled.
	 */
	public boolean isWarningsOn() {
		return withWarnings;
	}

	/**
	 * This method returns true, if the algorithm is during a jump to another
	 * <code>SyntaxDiagram</code>. Needed to decide wether or not a return to
	 * a Diagram is possible when a Variable is the (actual) position.
	 */
	public boolean isJumpToDiagram() {
		return duringJumpToDiagram;
	}

	/**
	 * This mehtod returns true if the Stack is empty.
	 * 
	 * @return True if the Stack is empty.
	 */
	public boolean isStackEmpty() {
		return myStack.empty();
	}

	/**
	 * This mehtod returns true if the actual position should be behind the
	 * actual SynDiaElem which represents the actual position.
	 * 
	 * @return True if the position is behind.
	 */
	public boolean isPositionBehindElem() {
		return this.positionBehindElem;
	}

	/**
	 * This mehtod returns true if the highest adress on the Stack should be
	 * highlighted.
	 * 
	 * @return true if the highest adress on the Stack should be highlighted.
	 */
	public boolean isStackHighlighted() {
		return this.stackHighlighted;
	}

	/**
	 * This mehtod returns true if the actual position is inside a Repetition
	 * 
	 * @return true if the actual position is inside a Repetition.
	 */
	public boolean isPositionInRepetition() {
		SynDiaElem tempPosition = position;
		SynDiaElem oldPosition = null;
		while (tempPosition != null) {
			oldPosition = tempPosition;
			tempPosition = tempPosition.getParent();
			// If a position is found checkout, if the actual position
			// is in the right concatenation
			if (tempPosition instanceof Repetition) {
				Repetition tempRepetition = (Repetition) tempPosition;
				if (oldPosition == tempRepetition.getRight()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This mehtod returns true if the end of a Diagram was reached.
	 * 
	 * @return true if the end of a Diagram was reached.
	 */
	public boolean isEndReached() {
		return this.endReached;
	}

	/**
	 * This mehtod returns true if the algorithm was finished successfully.
	 * 
	 * @return true if the algorithm was finished successfully.
	 */
	public boolean isFinishedWithSuccess() {
		return this.finishedWithSuccess;
	}

	/**
	 * This method returns the word which should be generated by the algorithm.
	 * 
	 * @return The world which should be generated
	 */
	public String getWord() {
		return word;
	}

	/**
	 * This method returns the word which was already generated by the algoritm.
	 * 
	 * @return The word which was already generated.
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * This method returns the explanation of the actual algorithm step.
	 * 
	 * @return The explanation of the actual step.
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * This method returns the last warning generated by the algorithm. (Only if
	 * warnings are enabled).
	 * 
	 * @return Last warning generated during algorithm.
	 */
	public String getWarning() {
		if (isWarningsOn())
			return warning;
		return "";
	}

	/**
	 * This method returns the actual position in a SyntaxDiagram during
	 * algorithm.
	 * 
	 * @return Actual algorithm position in a SyntaxDiagram.
	 */
	public SynDiaElem getPosition() {
		return position;
	}

	/**
	 * This method returns the SynDiaSystem used by the algorithm.
	 * 
	 * @return The SynDiaSystem used by the algorithm
	 * 
	 */
	public SynDiaSystem getSynDiaSystem() {
		return mySynDiaSystem;
	}

	/**
	 * This method returns the return marks of all Variables pushed on the Stack
	 * starting with the oldest.
	 * 
	 * @return List containing return marks (int).
	 */
	public List getAdressNumbersFromStack() {
		// varList contains the Variables pushed onto the Stack.
		LinkedList<Variable> varList = new LinkedList<Variable>();
		for (int i = 0; i < myStack.size(); i++) {
			Variable aVar = myStack.get(i);
			varList.add(aVar);
		}
		// adrList should contain the return marks of the Variables pushed onto
		// the Stack.
		LinkedList<Integer> adrList = new LinkedList<Integer>();
		for (Variable aVariable : varList) {
			adrList.add((Integer) getAdressNumber(aVariable));
		}
		return adrList;
	}

	/**
	 * This method returns the adress number for a specific Variable used as
	 * return adress during the algorithm.
	 * 
	 * @param var
	 *            The Variable which return adress should be returned.
	 */
	public Number getAdressNumber(Variable var) {
		return adrNumbers.get(var);
	}

	/**
	 * This mehtod returns the first Repetition which is around the actual
	 * position. If there is now parent repetition, null is returned.
	 * 
	 * @return the first Repetition which is around the actual position.
	 */
	public Repetition getFirstParentRepetition() {
		SynDiaElem tempPosition = position;
		SynDiaElem oldPosition = null;
		while (tempPosition != null) {
			oldPosition = tempPosition;
			tempPosition = tempPosition.getParent();
			// If a position is found checkout, if the actual position
			// is in the right concatenation
			if (tempPosition instanceof Repetition) {
				Repetition tempRepetition = (Repetition) tempPosition;
				if (oldPosition == tempRepetition.getRight()) {
					return tempRepetition;
				}
			}
		}
		return null;
	}

	/**
	 * This mehtod returns the color, the stack should be highlighted with
	 */
	public Color getStackColor() {
		return this.stackColor;
	}

	/**
	 * This method sets the algorihm status running.
	 * 
	 */
	public void enableAlgorithmRunning() {
		if (!algorithmRunning) {
			setChanged();
			algorithmRunning = true;
		}
	}

	/**
	 * This method stops the algorithm. <b>Attention:</b> Algorithm Status is
	 * lost if algorithm is disabled.
	 * 
	 */
	public void disableAlgorithmRunning() {
		if (algorithmRunning) {
			setChanged();
			algorithmRunning = false;
		}
	}

	/**
	 * This method sets the algorihm status finished.
	 * 
	 */
	public void enableAlgorithmFinished() {
		if (!algorithmFinished) {
			setChanged();
			algorithmFinished = true;
		}
	}

	/**
	 * This method resets the algorithm status to not finished.
	 * 
	 */
	public void disableAlgorithmFinished() {
		if (algorithmFinished) {
			setChanged();
			algorithmFinished = false;
		}
	}

	/**
	 * This method enables warnings during algorithm.
	 */
	public void enableWarnings() {
		if (!withWarnings) {
			setChanged();
			withWarnings = true;
		}
	}

	/**
	 * This method disables warnings during algorithm.
	 * 
	 */
	public void disableWarnings() {
		if (withWarnings) {
			setChanged();
			withWarnings = false;
		}
	}

	/**
	 * This method enables that the algorithm is during a jump to a Diagram.
	 * Needed to decide wether or not a return to a Diagram is possible when a
	 * Variable is the (actual) position.
	 */
	public void enableJumpToDiagram() {
		if (!duringJumpToDiagram) {
			duringJumpToDiagram = true;
			setChanged();
		}
	}

	/**
	 * This method disables that the algorithm is during a jump to a Diagram.
	 * Needed to decide wether or not a return to a Diagram is possible when a
	 * Variable is the (actual) position.
	 * 
	 */
	public void disableJumpToDiagram() {
		if (duringJumpToDiagram) {
			duringJumpToDiagram = false;
			setChanged();
		}
	}

	/**
	 * Sets the value that the actual Position is behind the
	 * <code>SynDiaElem</code> which represents the actualPosition true.
	 * 
	 */
	public void enablePositionBehind() {
		if (!positionBehindElem) {
			this.positionBehindElem = true;
			setChanged();
		}
	}

	/**
	 * Sets the value that the actual Position is behind the
	 * <code>SynDiaElem</code> which represents the actualPosition false.
	 * 
	 */
	public void disablePositionBehind() {
		if (positionBehindElem) {
			this.positionBehindElem = false;
			setChanged();
		}
	}

	/**
	 * This mehtod returns sets the highest adress on the Stack highlighted.
	 * Stack is highlighted with its default color.
	 */
	public void enableStackHighlighted() {
		if (!stackHighlighted) {
			this.stackHighlighted = true;
			this.stackColor = RenderElement.HIGHLIGHT_BLUE;
			setChanged();
		}
	}

	/**
	 * This mehtod returns sets the highest adress on the Stack highlighted.
	 * Stack is highlighte with the color aColor.
	 * 
	 * @param aColor
	 *            The Color the Stack should be highlighted in.
	 */
	public void enableStackHighlighted(Color aColor) {
		this.stackHighlighted = true;
		this.stackColor = aColor;
		setChanged();
	}

	/**
	 * This mehtod returns sets the highest adress on the Stack dehighlighted.
	 */
	public void disableStackHighlighted() {
		if (stackHighlighted) {
			this.stackHighlighted = false;
			setChanged();
		}
	}

	/**
	 * Sets the value that the Algorithm was finished correctly true.
	 * 
	 */
	public void enableFinishedWithSuccess() {
		if (!finishedWithSuccess) {
			this.finishedWithSuccess = true;
			setChanged();
		}
	}

	/**
	 * Sets the value that the Algorithm was finished correctly false.
	 * 
	 */
	public void disableFinishedWithSuccess() {
		if (finishedWithSuccess) {
			this.finishedWithSuccess = false;
			setChanged();
		}
	}

	/**
	 * This method sets the word which should be generated by the algorithm.
	 * 
	 * @param word
	 *            The word which should be generated.
	 */
	public void setWord(String word) {
		this.word = word;
		setChanged();
	}

	/**
	 * This method sets the output which is generated by the algorithm.
	 * 
	 * @param output
	 *            The output generated.
	 */
	public void setOutput(String output) {
		this.output = output;
		setChanged();
	}

	/**
	 * This method sets the explanation which is generated by the algorithm.
	 * 
	 * @param explanation
	 *            The explanation generated.
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
		setChanged();
	}

	/**
	 * This method sets the warning which is generated by the algorithm.
	 * (Warning is just thrown if warnings are enabled).
	 * 
	 * @param warning
	 *            The warning generated.
	 */
	public void setWarning(String warning) {
		this.warning = warning;
		setChanged();
	}

	/**
	 * This method changes the position in a <code>SyntaxDiagram</code>.
	 * 
	 * @param anElem
	 *            New position in the <code>SyntaxDiagram</code>.
	 */
	public void setPosition(SynDiaElem anElem) {
		position = anElem;
		// Update the list of all Elems reachable
		setAllElemsReachable();
		setChanged();
	}

	/**
	 * This method pushes a <code>Variable</code> on the stack.
	 * 
	 * @param aVar
	 *            The <code>Variable</code> which should be pushed onto the
	 *            stack.
	 */
	public void pushToStack(Variable aVar) {
		myStack.push(aVar);
		setChanged();
	}

	/**
	 * This method pops a <code>Variable</code> from the Stack.
	 * 
	 * @return The <code>Variable</code> popped from the Stack. Is
	 *         <code>null</code> if Stack is empty.
	 */
	public Variable popFromStack() {
		if (!myStack.isEmpty()) {
			return myStack.pop();
		}
		return null;
	}

	/**
	 * This method empties the Stack.
	 * 
	 */
	public void emptyStack() {
		myStack.clear();
	}

	/**
	 * This model reinitializes the model.
	 */
	public void reset() throws InitializationFailedException {
		try {
			initialize(mySynDiaSystem);
			setChanged();
		} catch (InitializationFailedException e) {
			throw e;
		}
	}

	/**
	 * This method checks, if an <code>SynDiaElem</code> is in a
	 * <code>SyntaxDiagram</code>.
	 * 
	 * @param anElem
	 *            The Element which should be in the Diagram.
	 * @param aDiagram
	 *            The Diagram the Element should be in.
	 * @return True if <code>anElem</code> is in <code>aDiagram</code>.
	 */
	public boolean isElementInDiagram(SynDiaElem anElem, SyntaxDiagram aDiagram) {
		return (anElem.getMySyntaxDiagram().getRoot() == aDiagram.getRoot());
	}

	/**
	 * Searches for the next position in the Diagram and returns it.
	 * 
	 * @param anElem
	 *            The <code>SynDiaElem</code> which is before the new position
	 * @return The new position (a <code>SynDiaElem</code>).
	 */
	public SynDiaElem getPositionBehind(SynDiaElem anElem) {
		if (anElem instanceof TerminalSymbol) {
			return getPositionBehindInConcatenation(anElem);
		}
		if (anElem instanceof Variable) {
			if (this.isJumpToDiagram()) {
				this.disablePositionBehind();
				return anElem;
			} else {
				return getPositionBehindInConcatenation(anElem);
			}
		}
		if (anElem instanceof Concatenation) {
			Concatenation aConcat = (Concatenation) anElem;
			return getPositionBehindConcatenation(aConcat, anElem);
		}
		this.disablePositionBehind();
		return anElem;
	}

	/**
	 * Searches for a Position behind an <code>SynDiaElem</code> in a
	 * Concatenation
	 * 
	 * @param anElem
	 *            The <code>SynDiaElem</code> where behind this method should
	 *            use.
	 * 
	 * @return The new position (a <code>SynDiaElem</code>).
	 */
	private SynDiaElem getPositionBehindInConcatenation(SynDiaElem anElem) {
		// Get the parentConcat
		if (anElem.getParent() instanceof Concatenation) {
			Concatenation parentConcat = (Concatenation) anElem.getParent();
			int parentConcatSize = parentConcat.getNumberOfElems();
			if (parentConcatSize > 1) {
				for (int i = 0; i < parentConcatSize - 1; i++) {
					// Test if Element i is actual element
					if (parentConcat.getSynDiaElem(i) == anElem) {
						// return the following element
						this.disablePositionBehind();
						return getPositionToElem(parentConcat
								.getSynDiaElem(i + 1));
					}
				}
			}
			// Else Element must be behind Concatenation
			return getPositionBehindConcatenation(parentConcat, anElem);
		} else
			return null;
	}

	/**
	 * Searches for a Position behind a <code>Concatenation</code>.
	 * 
	 * @param aConcat
	 *            The <code>Concatenation</code> the new position should be
	 *            behind.
	 * 
	 * @param anElem
	 *            The <code>SynDiaElem</code> the new position should be
	 *            behind.
	 * 
	 * @return The new position (a <code>SynDiaElem</code>).
	 */
	private SynDiaElem getPositionBehindConcatenation(Concatenation aConcat,
			SynDiaElem anElem) {
		// get the Concatenations Parent
		SynDiaElem concatsParent = aConcat.getParent();
		// Check the Type of the Concat's parent.
		// 1. A Branch
		// ***************************************************************
		if (concatsParent instanceof Branch) {
			// The next decision must be behind the Branch.
			return getPositionBehindInConcatenation(concatsParent);
		}
		// 2. A Repetition
		// ***********************************************************
		if (concatsParent instanceof Repetition) {
			Repetition aRepetition = (Repetition) concatsParent;
			// Checkout if Concat is left or right concatenation
			if (aConcat == aRepetition.getLeft()) {
				// If left Concat contains Elements, position is Behind last
				// Element in Concat.
				if (aConcat.getNumberOfElems() != 0) {
					this.enablePositionBehind();
					return aConcat
							.getSynDiaElem(aConcat.getNumberOfElems() - 1);
				} else {
					return anElem;
				}
			} else {
				// if its the right branch, the decision is in front of the
				// first
				// Element in the left Concatenation.
				this.disablePositionBehind();
				return aRepetition.getLeft();
			}
		}
		// 3. NULL
		// *******************************************************************
		// There is no Element behind the actual Element.
		// So the decision is behind the actual Element
		this.enablePositionBehind();
		this.endReached = true;
		return anElem;
	}

	/**
	 * Gets the position to a specific <code>SynDiaElem</code>, if the elem
	 * should be the new position.
	 * 
	 * @param anElem
	 *            The Element, which should be the new position.
	 * @return The new position.
	 */
	private SynDiaElem getPositionToElem(SynDiaElem anElem) {
		if (anElem instanceof Repetition) {
			Repetition aRepetition = (Repetition) anElem;
			this.disablePositionBehind();
			return aRepetition.getLeft();
		}
		this.disablePositionBehind();
		return anElem;
	}

	/**
	 * Adds all the elements which could be reached from aSynDiaElem to the List
	 * allElemsReachable. <b>Attention:</b> this method does not clear the List
	 * allElemsReachable before.
	 * 
	 * @param aSynDiaElem
	 *            the SynDiaElem from where Elems should be reached.
	 */
	private void addAllElemsReachable(SynDiaElem aSynDiaElem) {
		// Checkout which type of SynDiaElem aSynDiaElem is.
		// If its a TerminalSymbol
		if (aSynDiaElem instanceof TerminalSymbol) {
			allElemsReachable.add(aSynDiaElem);
		}
		// If its a Variable
		if (aSynDiaElem instanceof Variable) {
			if (this.isJumpToDiagram()) {
				addAllDiagramsReachable();
			} else
				allElemsReachable.add(aSynDiaElem);
		}
		// If its a Repetition
		else if (aSynDiaElem instanceof Repetition) {
			// The Elements of the left Concatenation are reachable:
			Repetition aRepetition = (Repetition) aSynDiaElem;
			addAllElemsReachable(aRepetition.getLeft());
		}
		// If its a Branch
		else if (aSynDiaElem instanceof Branch) {
			// Tehe Elements of the left and the Right Concatenation are
			// reachable:
			Branch aBranch = (Branch) aSynDiaElem;
			addAllElemsReachable(aBranch.getLeft());
			addAllElemsReachable(aBranch.getRight());
		}
		// If its a Concatenation
		else if (aSynDiaElem instanceof Concatenation) {
			Concatenation aConcat = (Concatenation) aSynDiaElem;
			// If Concat contains Elements, add the first
			if (aConcat.getNumberOfElems() > 0) {
				addAllElemsReachable(aConcat.getSynDiaElem(0));
			}
			// Else add the elements Reachable behind
			else {
				// If the Element is not the root Element, it is reachable
				// itself
				if (aConcat.getParent() != null) {
					allElemsReachable.add(aConcat);
				}
				addAllElemsReachableBehind(aConcat);
			}
		}
	}

	/**
	 * Adds all Elems reachable behind a SynDiaElem to the Reachable Element
	 * Set.
	 * 
	 * <b>Attention:</b> this method does not clear the List allElemsReachable
	 * before.
	 * 
	 * @param aSynDiaElem
	 *            the SynDiaElem from where Elems should be reached.
	 */
	private void addAllElemsReachableBehind(SynDiaElem aSynDiaElem) {
		// Checkout which type of SynDiaElem aSynDiaElem is
		if (aSynDiaElem instanceof Variable
				|| aSynDiaElem instanceof TerminalSymbol
				|| aSynDiaElem instanceof Repetition
				|| aSynDiaElem instanceof Branch) {
			Concatenation parentConcat = (Concatenation) aSynDiaElem
					.getParent();
			boolean elemFound = false;
			for (int i = 0; i < (parentConcat.getNumberOfElems() - 1); i++) {
				// If the Element is at position i in the Concatenation,
				// the Element the method has to look for must be behind that
				// Element.
				if (parentConcat.getSynDiaElem(i) == aSynDiaElem) {
					addAllElemsReachable(parentConcat.getSynDiaElem(i + 1));
					elemFound = true;
					break;
				}
			}
			if (!elemFound) {
				addAllElemsReachableBehind(parentConcat);
			}
		}
		// If its a Concatenation
		if (aSynDiaElem instanceof Concatenation) {
			// Checkout if Concat has a parent
			Concatenation aConcat = (Concatenation) aSynDiaElem;
			SynDiaElem parentElem = aConcat.getParent();
			if (parentElem != null) {
				// Checkout which type of parent parentElem is
				if (parentElem instanceof Branch) {
					// Add Elems Behind
					addAllElemsReachableBehind(parentElem);
				} else if (parentElem instanceof Repetition) {
					// Checkout if Concat is left or right Concat
					Repetition parentRep = (Repetition) parentElem;
					if (aConcat == parentRep.getLeft()) {
						// Add Elems in RightConcat and Behind Repetition
						addAllElemsReachableBehind(parentRep);
						// Checkout if RightConcat is empty
						if (parentRep.getRight().getNumberOfElems() > 0) {
							// If not, add its Elements.
							addAllElemsReachable(parentRep.getRight());
						}
						// Else if left Concat is not empty, add its Elements
						else if (parentRep.getLeft().getNumberOfElems() > 0) {
							addAllElemsReachable(parentRep.getLeft());
						}
					}
				}
			}
			// Else the end of the diagram was reached
			else {
				this.endReached = true;
			}
		}
	}

	/**
	 * Adds all <code>SyntaxDiagrams</code> to the Set of all reachable
	 * Elements.
	 * 
	 */
	private void addAllDiagramsReachable() {
		List<String> diagramList = mySynDiaSystem.getLabelsOfVariables();
		for (String aDiagramName : diagramList) {
			try {
				allElemsReachable.add(mySynDiaSystem
						.getSyntaxDiagram(aDiagramName));
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out
						.println("Error in WordAlgoModel: Incosistent SynDiaSystem. Exit");
				System.exit(-1);
			}
		}
	}

	/**
	 * Sets all the Elements which are reachable from the actual position in the
	 * SynDiaSystem
	 */
	public void setAllElemsReachable() {
		this.endReached = false;
		this.allElemsReachable.clear();
		if (this.positionBehindElem) {
			addAllElemsReachableBehind(position);
		} else {
			addAllElemsReachable(position);
		}
	}

	/**
	 * Return true if the Element could be reached from the actual Position
	 * 
	 * @param aSynDiaElem
	 *            The Element which should be reached.
	 */
	public boolean isElementReachable(SynDiaElem aSynDiaElem) {
		Iterator it = allElemsReachable.iterator();
		while (it.hasNext()) {
			Object anObject = it.next();
			if (anObject == aSynDiaElem) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return true if the Diagram could be reached from the actual Position
	 * 
	 * @param aSyntaxDiagram
	 *            The SyntaxDiagram which should be reached.
	 */
	public boolean isElementReachable(SyntaxDiagram aSyntaxDiagram) {
		Iterator it = allElemsReachable.iterator();
		while (it.hasNext()) {
			Object anObject = it.next();
			if (anObject == aSyntaxDiagram) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if List of all Elements which are reachable from actual
	 * position is empty
	 */
	public boolean isNoElementReachable() {
		return allElemsReachable.isEmpty();
	}

	/**
	 * Sets this Model changed, so that a notifyObservers() will notify the
	 * <code>Observer</code>s.
	 * 
	 */
	public void setModelChanged() {
		this.setChanged();
	}

}
