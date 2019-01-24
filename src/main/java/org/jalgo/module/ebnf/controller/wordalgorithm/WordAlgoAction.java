package org.jalgo.module.ebnf.controller.wordalgorithm;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.model.syndia.ElementNotFoundException;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;
import org.jalgo.module.ebnf.util.IAction;

/**
 * The class <code>WordAlgoAction</code> represents an abstract Action
 * performed during the WordAlgorithm. Each Action can be undone. Each
 * <code>WordAlgoAction</code> has to know its model to perform changes.
 * 
 * @author Claas Wilke
 * 
 */
public abstract class WordAlgoAction implements IAction {

	protected WordAlgoModel myModel;

	// The Position at which the algorihm was in the Diagram before this
	// Action was performed.
	protected final SynDiaElem startPosition;

	protected final boolean wasPositionBehindElem;

	protected final boolean oldRunningStatus;

	protected final boolean oldFinishedStatus;
	protected final boolean oldFinishedWithSuccessStatus;

	// This String saves the old explanation. Needed for undo-method.
	protected final String oldExplanation;

	// This String saves the old warning. Needed for undo-method.
	protected final String oldWarning;

	// argument can be a SyntaxDiagram, a Variable or a TerminalSymbol.
	protected Object arg;

	/**
	 * The Constructor constructs a new <code>WordAlgoAction</code>. Each
	 * <code>WordAlgoAction</code> has to know its model to perform changes.
	 * 
	 * @param myModel
	 *            The model, the <code>WordAlgoAction</code> will know.
	 * @param arg
	 *            The argument, the <code>WordAlgoAction</code> should work
	 *            with. Argument can be a <code>SyntaxDiagram</code>, a
	 *            <code>Variable</code> or <code>a TerminalSymbol</code>.
	 */
	public WordAlgoAction(WordAlgoModel myModel, Object arg) {
		this.myModel = myModel;
		this.arg = arg;
		// Saves some old values which can be resetted during undo()
		this.startPosition = myModel.getPosition();
		this.oldExplanation = myModel.getExplanation();
		this.oldWarning = myModel.getWarning();
		this.wasPositionBehindElem = myModel.isPositionBehindElem();
		this.oldRunningStatus = myModel.isAlgorithmRunning();
		this.oldFinishedStatus = myModel.isAlgorithmFinished();
		this.oldFinishedWithSuccessStatus = myModel.isFinishedWithSuccess();

	}

	/**
	 * The operation that should be performed by the <code>WordAlgoAction</code>.
	 * 
	 */
	public void perform() throws Exception {
	}

	/**
	 * The operation that can do the perform operation undone.
	 * 
	 */
	public void undo() throws Exception {
	}

	/**
	 * Method can be used to reset some booleans during undo. Booleans undone:
	 * algorithmRunngin status algorithmFinished status positionBehind element
	 * status
	 * 
	 */
	public void undoBooleans() {
		if (this.oldRunningStatus)
			myModel.enableAlgorithmRunning();
		else
			myModel.disableAlgorithmRunning();

		if (this.oldFinishedStatus)
			myModel.enableAlgorithmFinished();
		else
			myModel.disableAlgorithmFinished();

		if (this.wasPositionBehindElem)
			myModel.enablePositionBehind();
		else
			myModel.disablePositionBehind();

		if (this.oldFinishedWithSuccessStatus)
			myModel.enableFinishedWithSuccess();
		else
			myModel.disableFinishedWithSuccess();
}

	/**
	 * Method checks if only the exit of a Diagram can be reached from the
	 * actual position. If true, the leaving of the diagram is performed. A
	 * second part ist added to the Explanation, wether or not the Diagram must
	 * be left.
	 * 
	 */
	protected void isOnlyExitReachable() {
		if (myModel.isNoElementReachable() && myModel.isEndReached()) {
			try {
				// Perform a leaving of the diagram.
				// Check if there is a return adress on the Stack
				if (!myModel.isStackEmpty()) {
					// If true, perform step leave a Diagram.
					// Change Explanation
					myModel.getExplanation();
					myModel
							.setExplanation(myModel.getExplanation()
									+ "\n"
									+ Messages.getString("ebnf",
									"WordAlgo.Explanation_LeaveDiagramWithAdress"));
				} else {
					// If there is no adress on the Stack, the algorithm
					// terminates.
					// Terminate Algorithm
					myModel.disableAlgorithmRunning();
					myModel.enableAlgorithmFinished();
					// , check if
					// actual position is in StartDiagram.
					SyntaxDiagram startDiagram = myModel
							.getSynDiaSystem()
							.getSyntaxDiagram(
									myModel.getSynDiaSystem().getStartDiagram());
					// If the actual Element is in the StartDiagram, the
					// algorithm terminates
					// successfully.
					if (myModel.isElementInDiagram(myModel.getPosition(),
							startDiagram)) {
						// Checkout if Word is completly generated.
						if (myModel.getWord().equals("")
								|| myModel.getWord()
										.equals(myModel.getOutput())) {
							myModel.enableFinishedWithSuccess();
							myModel
									.setWarning(Messages.getString("ebnf",
									"WordAlgo.Explanation_LeaveDiagramFinishWithSuccess"));
						}
						// Else the algorithm terminates unsuccessfully.
						else {
							myModel.disableFinishedWithSuccess();
							myModel
									.setWarning(Messages.getString("ebnf",
									"WordAlgo.Explanation_LeaveDiagramFinishWithoutSuccess2"));
						}
					}
					// Else the algorithm terminates with a fault.
					else {
						myModel.disableFinishedWithSuccess();
						myModel
								.setWarning(Messages.getString("ebnf",
								"WordAlgo.Explanation_LeaveDiagramFinishWithoutSuccess"));
					}
				}
			} catch (ElementNotFoundException e) {
				// This exception is not handled. Cant happen during algorithm
				// If the StartDiagram can't be getted, everything must have
				// been wrong before.
			}

		} else {
			myModel.setExplanation(myModel.getExplanation() + "\n"
					+ Messages.getString("ebnf",
					"WordAlgo.Explanation_ContinueDiagram"));
		}
	}

}
