package org.jalgo.module.ebnf.controller.wordalgorithm;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.AlgorithmNotStartedException;
import org.jalgo.module.ebnf.controller.wordalgorithm.exceptions.CantPerformStepException;
import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;
import org.jalgo.module.ebnf.model.syndia.Concatenation;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.model.syndia.Variable;

/**
 * This class represents a step in the WordAlgorithm. The specific step is a
 * jump to another SyntaxDiagram.
 * 
 * @author Claas Wilke
 * 
 */
public class JumpToDiagramAction extends WordAlgoAction {

	// Needed to rebuilt status after undo()
	private boolean oldIsJumpToDiagram;

	/**
	 * Constructor which sets the Model and the argument, the
	 * <code>WordAlgoAction</code> should work with.
	 * 
	 * @param myModel
	 *            The <code>WordAlgoAction</code> should work with.
	 */
	public JumpToDiagramAction(WordAlgoModel myModel, SyntaxDiagram arg) {
		super(myModel, arg);

		// Save status
		oldIsJumpToDiagram = myModel.isJumpToDiagram();

	}

	/**
	 * This method performs the Action implemented by this
	 * <code>WordAlgoAction</code>.
	 */
	public void perform() throws AlgorithmNotStartedException,
			CantPerformStepException {
		if (myModel.isAlgorithmRunning()) {
			// Test if Diagram can be reached from actual Position.
			// The Action warns if the user jumps to the wrong Diagram.
			// (Just if warnings are enabled.)
			if (myModel.getPosition() instanceof Variable
					&& myModel.isJumpToDiagram()
					&& !myModel.isPositionBehindElem()) {
				// If Diagram can be reached, perform step.
				SyntaxDiagram actualDiagram = (SyntaxDiagram) arg;
				// Warning?
				if (!actualDiagram.getName().equals(
						((Variable) startPosition).getLabel())) {
					myModel.setWarning(Messages.getString("ebnf",
							"WordAlgo.Warning_JumpToDiagram"));
					throw new CantPerformStepException(
							"The clicked Diagram not euqals to the name of "
									+ "the Variable left.");
				} else
					myModel.setWarning("");
				// Specifies, that the JumpToTheDiagram is finished.
				// The algorithm can return to the Variable now.
				myModel.disableJumpToDiagram();
				myModel.disableStackHighlighted();
				// Change Explanation
				myModel.setExplanation(Messages.getString("ebnf",
						"WordAlgo.Explanation_JumpToDiagram"));
				// Change Position
				Concatenation rootConcat = actualDiagram.getRoot();
				// Checkout if rootConcat contains elements.
				// If true, new position is first Element in root Concat
				if (rootConcat.getNumberOfElems() > 0) {
					myModel.disablePositionBehind();
					myModel.setPosition(rootConcat.getSynDiaElem(0));
				} else {
					// Else the positions stays the same but is now behind the
					// Concatenation.
					myModel.enablePositionBehind();
					myModel.setPosition(rootConcat);
				}
			} else
				throw new CantPerformStepException(
						"The clicked Diagram can not be reached.");
		} else
			throw new AlgorithmNotStartedException(
					"The algorithm was not started before click on Diagram.");
	}

	/**
	 * This method can make the Action undone performed by this
	 * <code>WordAlgoAction</code>.
	 */
	public void undo() throws AlgorithmNotStartedException {
		if (myModel.isAlgorithmRunning()) {
			// Undo running and finished status
			super.undoBooleans();
			// Change Explanation
			myModel.setExplanation(oldExplanation);
			myModel.enableStackHighlighted();
			// Warnings ?
			myModel.setWarning(oldWarning);
			// Change Position
			myModel.setPosition(startPosition);
			if (myModel.isJumpToDiagram() != oldIsJumpToDiagram) {
				if (oldIsJumpToDiagram)
					myModel.enableJumpToDiagram();
				else
					myModel.disableJumpToDiagram();
			}
		} else
			throw new AlgorithmNotStartedException(
					"The algorithm was not started before redo click on Diagram.");
	}
}
