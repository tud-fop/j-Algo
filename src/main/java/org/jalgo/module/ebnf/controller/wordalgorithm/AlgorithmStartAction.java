package org.jalgo.module.ebnf.controller.wordalgorithm;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.AlgorithmAlreadyStartedException;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.AlgorithmNotStartedException;
import org.jalgo.module.ebnf.model.syndia.Concatenation;
import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;

/**
 * This class represents a step in the WordAlgorithm. The specific step is the
 * begin of the algorithm. It's the first step.
 * 
 * @author Claas Wilke
 * 
 */
public class AlgorithmStartAction extends WordAlgoAction {

	/**
	 * Constructor which sets the Model and the argument, the
	 * <code>WordAlgoAction</code> should work with.
	 * 
	 * @param myModel
	 *            The <code>WordAlgoAction</code> should work with.
	 */
	public AlgorithmStartAction(WordAlgoModel myModel, Boolean arg) {
		super(myModel, arg);
	}

	/**
	 * This method performs the Action implemented by this
	 * <code>WordAlgoAction</code>.
	 */
	public void perform() throws AlgorithmAlreadyStartedException {
		if (!myModel.isAlgorithmRunning() && !myModel.isAlgorithmFinished()) {
			// Start Algorithm
			myModel.enableAlgorithmRunning();
			// Change Explanation
			myModel.setExplanation(Messages.getString("ebnf",
					"WordAlgo.Explanation_AlgorithmStart"));
			// Change Position (if Diagram contains Elements)
			if (myModel.getPosition() instanceof Concatenation) {
				Concatenation actualConcat = (Concatenation) myModel
						.getPosition();
				if (actualConcat.getNumberOfElems() > 0) {
					myModel.setPosition(actualConcat.getSynDiaElem(0));
					myModel.disablePositionBehind();
				} else {
					// Else the positions stays the same but is now behind the
					// Concatenation.
					myModel.setPosition(actualConcat);
					myModel.enablePositionBehind();
				}
			}
			// Add second part toExplanation
			isOnlyExitReachable();
		} else
			throw new AlgorithmAlreadyStartedException(
					"The Algorithm was already started.");
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
		} else
			throw new AlgorithmNotStartedException(
					"The algorithm was not started before undo of "
							+ "AlgorithmStartAction.");
	}

}
