package org.jalgo.module.ebnf.controller.wordalgorithm;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.AlgorithmNotStartedException;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.CantPerformStepException;
import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.TerminalSymbol;

/**
 * This class represents a step in the WordAlgorithm. The specific step is the
 * passing of a TerminalSymbol.
 * 
 * @author Claas Wilke
 * 
 */
public class TerminalAction extends WordAlgoAction {

	// This String saves the old output. Needed for undo-method.
	protected final String oldOutput;

	/**
	 * Constructor which sets the Model and the argument, the
	 * <code>WordAlgoAction</code> should work with.
	 * 
	 * @param myModel
	 *            The <code>WordAlgoAction</code> should work with.
	 */
	public TerminalAction(WordAlgoModel myModel, TerminalSymbol arg) {
		super(myModel, arg);
		this.oldOutput = myModel.getOutput();
	}

	/**
	 * This method performs the Action implemented by this
	 * <code>WordAlgoAction</code>.
	 */
	public void perform() throws AlgorithmNotStartedException,
			CantPerformStepException {
		// Step can be just performed, if the algorithm is running and wasn't
		// finished (successfully or not).
		if (myModel.isAlgorithmRunning() && !myModel.isAlgorithmFinished()) {
			TerminalSymbol actualTerminal = (TerminalSymbol) arg;
			// Test if TerminalSymbol can be reached from actual Position
			// Else throw Exception
			if (myModel.isElementReachable(actualTerminal)
					&& !myModel.isJumpToDiagram()) {
				// If TerminalSymbol can be reached, perform step.
				// Change Output
				String tempOutput = myModel.getOutput();
				tempOutput = tempOutput + actualTerminal.getLabel();
				myModel.setOutput(tempOutput);

				// Change Explanation
				myModel.setExplanation(Messages.getString("ebnf",
				"WordAlgo.Explanation_Terminal"));


				// Change Position
				SynDiaElem positionNew = myModel
						.getPositionBehind(actualTerminal);
				myModel.setPosition(positionNew);
				// Add second part toExplanation
				isOnlyExitReachable();

				// Warnings ?
				// If inputword is not null and Output != Beginn of Input
				// a warning is generated.
				if (!myModel.getWord().equals("")
						&& !myModel.getWord().startsWith(myModel.getOutput())) {
					myModel.setWarning(Messages.getString("ebnf",
					"WordAlgo.Warning_Terminal"));
					// The algorithm is finished unsuccessfully.
					myModel.disableAlgorithmRunning();
					myModel.enableAlgorithmFinished();
				} else
					myModel.setWarning("");

			} else
				throw new CantPerformStepException(
						"The clicked TerminalSymbol is unreachable.");
		} else
			throw new AlgorithmNotStartedException(
					"The algorithm was not started before click on TerminalSymbol.");
	}

	/**
	 * This method can make the Action undone performed by this
	 * <code>WordAlgoAction</code>.
	 */
	public void undo() throws AlgorithmNotStartedException {
		if (myModel.isAlgorithmRunning() || myModel.isAlgorithmFinished()) {
			// Undo running and finished status
			super.undoBooleans();
			// Change Output
			myModel.setOutput(oldOutput);
			// Change Explanation
			myModel.setExplanation(oldExplanation);
			// Warnings ?
			myModel.setWarning(oldWarning);
			// Change Position
			myModel.setPosition(startPosition);
		} else
			throw new AlgorithmNotStartedException(
					"The algorithm was not started before redo click on Terminal.");
	}

}
