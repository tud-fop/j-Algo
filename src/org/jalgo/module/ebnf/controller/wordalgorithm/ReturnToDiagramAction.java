package org.jalgo.module.ebnf.controller.wordalgorithm;

import java.awt.Color;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.AlgorithmNotStartedException;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.CantPerformStepException;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.Variable;
import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;

/**
 * This class represents a step in the WordAlgorithm. The specific step is the
 * return from a <code>SyntaxDiagram</code> toanother pushed on the Stack
 * before.
 * 
 * @author Claas Wilke
 * 
 */
public class ReturnToDiagramAction extends WordAlgoAction {

	// need to have this step undone.
	private Variable oldVarOnStack;

	/**
	 * Constructor which sets the Model and the argument, the
	 * <code>WordAlgoAction</code> should work with.
	 * 
	 * @param myModel
	 *            The <code>WordAlgoAction</code> should work with.
	 * @param aVar
	 *            The <code>Variable</code> the algorithm should return to.
	 */
	public ReturnToDiagramAction(WordAlgoModel myModel, Variable aVar) {
		super(myModel, aVar);
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
			// Check if the end of the Diagram could be reached from
			// the actual Position.
			// The Variable must be left before. Checked by isJumpToDiagram
			// (true if not finished). Else throw an Exception
			if (myModel.isEndReached() && !myModel.isJumpToDiagram()) {
				// If End can be reached, perform step.
				Variable actualVariable = (Variable) arg;
				// Pop Variable from Stack
				oldVarOnStack = myModel.popFromStack();
				// Warning?
				if (actualVariable != oldVarOnStack) {
					myModel
							.setWarning(Messages.getString("ebnf",
							"WordAlgo.Warning_ReturnToDiagram"));
					myModel.enableStackHighlighted(Color.RED);
					// repush Adress
					myModel.pushToStack(oldVarOnStack);
					throw new CantPerformStepException(
							"The clicked Return adress doesn't euqal to the "
									+ "hightest Return adress on the Stack.");
				} else
					myModel.setWarning("");
				// Change Explanation
				myModel
						.setExplanation(Messages.getString("ebnf",
						"WordAlgo.Explanation_ReturnToDiagram"));
				myModel.disableStackHighlighted();
				// Change Position
				SynDiaElem positionNew = myModel
						.getPositionBehind(actualVariable);
				myModel.setPosition(positionNew);
				// Add second part to Explanation
				isOnlyExitReachable();
			} else
				throw new CantPerformStepException(
						"The clicked Variable is unreachable.");
		} else
			throw new AlgorithmNotStartedException(
					"The algorithm was not started before click on Variable.");
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
			// Change Stack
			myModel.pushToStack(oldVarOnStack);
			// Change Position
			myModel.setPosition(startPosition);
			// Warnings ?
			myModel.setWarning(oldWarning);
			// Change finished status
			myModel.disableAlgorithmFinished();
		} else
			throw new AlgorithmNotStartedException(
					"The algorithm was not started before redo click on Variable.");
	}
}
