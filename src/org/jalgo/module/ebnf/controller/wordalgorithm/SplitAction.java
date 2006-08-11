package org.jalgo.module.ebnf.controller.wordalgorithm;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.AlgorithmNotStartedException;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.CantPerformStepException;
import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;
import org.jalgo.module.ebnf.model.syndia.Concatenation;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;

/**
 * This class represents a step in the WordAlgorithm. The specific step is a
 * Click on an empty Concatenation in a Branch or Repetition.
 * 
 * @author Claas Wilke
 * 
 */
public class SplitAction extends WordAlgoAction {

	/**
	 * Constructor which sets the Model and the argument, the
	 * <code>WordAlgoAction</code> should work with.
	 * 
	 * @param myModel
	 *            The <code>WordAlgoAction</code> should work with.
	 * @param parentElem
	 *            The parenElem of the Concatenation clicked on. Could be a
	 *            <code>Repetition</code> or a <code>Branch</code>.
	 * @param aConcat
	 *            The <code>Concatenation</code> clicked on.
	 */
	public SplitAction(WordAlgoModel myModel, Concatenation aConcat) {
		super(myModel, aConcat);
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
			Concatenation actualConcat = (Concatenation) arg;
			// Test if Concatenation can be reached from actual Position
			// and contains no SynDiaElems.
			// Else throw Exception
			if (myModel.isElementReachable(actualConcat)
					&& !myModel.isJumpToDiagram()
					&& actualConcat.getNumberOfElems() == 0) {
				// If Concatenation can be reached, perform step.

				// Change Explanation
				myModel.setExplanation(Messages.getString("ebnf",
				"WordAlgo.Explanation_Split"));

				// Change Position
				SynDiaElem positionNew = myModel
						.getPositionBehind(actualConcat);
				myModel.setPosition(positionNew);
				// Add second part toExplanation
				isOnlyExitReachable();
			} else
				throw new CantPerformStepException(
						"The clicked Concatenation is unreachable or is not empty.");
		} else
			throw new AlgorithmNotStartedException(
					"The algorithm was not started before click on Concatenation.");
	}

	/**
	 * This method can make the Action undone performed by this
	 * <code>WordAlgoAction</code>.
	 */
	public void undo() throws AlgorithmNotStartedException {
		if (myModel.isAlgorithmRunning() || myModel.isAlgorithmFinished()) {
			// Undo running and finished status
			super.undoBooleans();
			// Change Explanation
			myModel.setExplanation(oldExplanation);
			// Change Warning
			myModel.setWarning(oldWarning);
			// Change Position
			myModel.setPosition(startPosition);
			// Change FinishedStatus
			myModel.disableAlgorithmFinished();
		} else
			throw new AlgorithmNotStartedException(
					"The algorithm was not started before redo click on Concatenation.");
	}

}
