package org.jalgo.module.ebnf.controller.wordalgorithm;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.AlgorithmNotStartedException;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.CantPerformStepException;
import org.jalgo.module.ebnf.model.syndia.Variable;
import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;

/**
 * This class represents a step in the WordAlgorithm. The specific step is the
 * passing of a Variable.
 * 
 * @author Claas Wilke
 * 
 */
public class VariableAction extends WordAlgoAction {

	private boolean oldIsJumpToDiagram;

	/**
	 * Constructor which sets the Model and the argument, the
	 * <code>WordAlgoAction</code> should work with.
	 * 
	 * @param myModel
	 *            The <code>WordAlgoAction</code> should work with.
	 */
	public VariableAction(WordAlgoModel myModel, Variable arg) {
		super(myModel, arg);

		oldIsJumpToDiagram = myModel.isJumpToDiagram();
	}

	/**
	 * This method performs the Action implemented by this
	 * <code>WordAlgoAction</code>.
	 */
	public void perform() throws AlgorithmNotStartedException,
			CantPerformStepException {
		// Step can be just performed, if the algorithm is running and wasn't
		// finished (successfully or not).
		if (myModel.isAlgorithmRunning() && !myModel.isAlgorithmFinished()
				&& !myModel.isJumpToDiagram()) {
			Variable actualVariable = (Variable) arg;
			// Test if Variable can be reached from actual Position
			// Else throw Exception
			if (myModel.isElementReachable(actualVariable)) {
				// If VariableSymbol can be reached, perform step.
				// Change Explanation
				myModel.setExplanation(Messages.getString("ebnf",
				"WordAlgo.Explanation_Variable"));
				// Change Stack
				myModel.pushToStack(actualVariable);
				myModel.enableStackHighlighted();
				// Change Position
				myModel.setPosition(actualVariable);
				myModel.disablePositionBehind();
				// Specifies, that the JumpToTheDiagram is not finished yet.
				// The algorithm can't return to the Variable yet.
				myModel.enableJumpToDiagram();
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
			// Change Warning
			myModel.setWarning(oldWarning);
			// Change Stack
			myModel.popFromStack();
			myModel.disableStackHighlighted();
			// Specifies, that the JumpToTheDiagram is not finished yet.
			// The algorithm can't return to the Variable yet.
			if (myModel.isJumpToDiagram() != oldIsJumpToDiagram) {
				if (oldIsJumpToDiagram)
					myModel.enableJumpToDiagram();
				else
					myModel.disableJumpToDiagram();
			}
			// Change Position
			myModel.setPosition(startPosition);
		} else
			throw new AlgorithmNotStartedException(
					"The algorithm was not started before redo click on Variable.");
	}
}
