package org.jalgo.module.ebnf.controller.wordalgorithm;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.AlgorithmNotStartedException;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.CantPerformStepException;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.model.syndia.Variable;
import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;

/**
 * This class represents a step in the WordAlgorithm. The specific step is the
 * leaving of a <code>SyntaxDiagram</code>.
 * 
 * @author Claas Wilke
 * 
 */
public class LeaveDiagramAction extends WordAlgoAction {

	/**
	 * Constructor which sets the Model and the argument, the
	 * <code>WordAlgoAction</code> should work with.
	 * 
	 * @param myModel
	 *            The <code>WordAlgoAction</code> should work with.
	 */
	public LeaveDiagramAction(WordAlgoModel myModel, Variable arg) {
		super(myModel, arg);
	}

	/**
	 * This method performs the Action implemented by this
	 * <code>WordAlgoAction</code>.
	 */
	public void perform() throws AlgorithmNotStartedException, Exception {
		if (myModel.isAlgorithmRunning()) {
			// Check if the end of the Diagram could be reached from
			// the actual Position.
			// if noElementReachable, leaving Diagram was already performed
			if (myModel.isEndReached() && !myModel.isNoElementReachable()) {
				// If End can be reached, perform step.
				// Check if there is a return adress on the Stack
				if (!myModel.isStackEmpty()) {
					// If true, perform step leave a Diagram.
					// Change Explanation
					myModel
							.setExplanation(Messages.getString("ebnf",
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
							myModel
									.setExplanation(Messages.getString("ebnf",
									"WordAlgo.Explanation_LeaveDiagram"));
							myModel.enableFinishedWithSuccess();
							myModel
									.setWarning(Messages.getString("ebnf",
									"WordAlgo.Explanation_LeaveDiagramFinishWithSuccess"));
						}
						// Else terminate also unsuccessfully
						else {
							myModel
									.setExplanation(Messages.getString("ebnf",
									"WordAlgo.Explanation_LeaveDiagram"));
							myModel.disableFinishedWithSuccess();
							myModel
									.setWarning(Messages.getString("ebnf",
									"WordAlgo.Explanation_LeaveDiagramFinishWithoutSuccess2"));
						}
					}
					// Else the algorithm terminates with a fault.
					else {
						myModel
								.setExplanation(Messages.getString("ebnf",
								"WordAlgo.Explanation_LeaveDiagram"));
						myModel.disableFinishedWithSuccess();
						myModel
								.setWarning(Messages.getString("ebnf",
								"WordAlgo.Explanation_LeaveDiagramFinishWithoutSuccess"));
					}
				}
				// Change Position
				myModel.enablePositionBehind();
				myModel.setPosition(myModel.getPosition().getMySyntaxDiagram()
						.getRoot());
				// The Observers must be informed, that model has changed.
				myModel.notifyObservers();
			} else
				throw new CantPerformStepException(
						"The end of the actual Diagram is unreachable.");
		} else
			throw new AlgorithmNotStartedException(
					"The algorithm was not started before leaving Diagram.");
	}

	/**
	 * This method can make the Action undone performed by this
	 * <code>WordAlgoAction</code>.
	 */
	public void undo() throws AlgorithmNotStartedException {
		if (myModel.isAlgorithmRunning() || myModel.isAlgorithmFinished()) {
			// Undo running and finished status
			super.undoBooleans();
			// Enable algorithm (could be finished during perform()).
			myModel.enableAlgorithmRunning();
			myModel.disableAlgorithmFinished();
			// Change Explanation
			myModel.setExplanation(oldExplanation);
			// Change Warning
			myModel.setWarning(oldWarning);
			// Change Position
			myModel.setPosition(startPosition);
			// Change algorithm Status
			myModel.disableAlgorithmFinished();
		} else
			throw new AlgorithmNotStartedException(
					"The algorithm was not started or finished before redo leaving Diagram.");
	}

}
