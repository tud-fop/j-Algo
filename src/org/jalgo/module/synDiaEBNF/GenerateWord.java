/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and platform
 * independant. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/*
 * Created on 30.04.2004
 */

package org.jalgo.module.synDiaEBNF;

import java.io.Serializable;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.jalgo.main.gfx.MarkStyle;
import org.jalgo.main.gui.DialogConstants;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.gui.TextCanvas;
import org.jalgo.main.gui.widgets.StackCanvas;
import org.jalgo.main.util.Messages;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaColors;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaAlternative;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaConcatenation;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaElement;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaEpsilon;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaInitial;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaRepetition;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaSystem;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaTerminal;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaVariable;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaVariableBack;

/**
 * Offers the methods to generate a word, which is recognized by a language
 * shown by a syntactical diagram system. Within the possibility to go step by
 * the through the system, even backwards. The algorithm can be aborted all the
 * time.
 * 
 * @author Babett Schaliz
 * @author Benjamin Scholz
 * @author Marco Zimmerling
 * @author Michael Pradel
 * @version %I%, %G%
 */
public class GenerateWord
extends SynDiaBacktracking
implements IAlgorithm, SynDiaColors, Serializable {

	private static final long serialVersionUID = -4764721879157297522L;

	/**
	 * Constructor gets the Figure, Stack, the StackCanavas, the TextCanvases
	 * and also the syntaxtical diagram SynDiaSystem, to work with. It also fill
	 * the TextCanvas and show the backtracking labels, set the startelement.
	 * 
	 * @param figure The <code> Figure </code> include the diagram system
	 * @param stackCanvas The <code> StackCanvas </code> of the graphical stack
	 * @param algoTxtCanvas The <code> TextCanvas </code> where the algorithm is
	 *            displayed
	 * @param generatedWordCanvas The <code> TextCanvas </code> to display the
	 *            generated word
	 * @param synDiaDef The <code> SynDiaSystem </code> to work with
	 */
	public GenerateWord(ModuleController moduleController, Figure figure,
		StackCanvas stackCanvas, TextCanvas algoTxtCanvas,
		TextCanvas generatedWordCanvas, SynDiaSystem synDiaDef) {

		super(moduleController, figure, stackCanvas, algoTxtCanvas,
			generatedWordCanvas, synDiaDef);

		// algorithm written on page 22 in the script
		algoTxtCanvas.setTextSegments(new String[] {
			Messages.getString("synDiaEBNF", "GenerateWord.Algo_title_2"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_1_3") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_2_4"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_3_5"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_4_6") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_5_7") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_6_8"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_7_9") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_8_10") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_9_11"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_10_12") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_11_13") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_12_14") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_13_15") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_14_16"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_15_17") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_16_18") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_17_19"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_18_20") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_19_21"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "GenerateWord.Algo_description_20_22")}); //$NON-NLS-1$
		algoTxtCanvas.markFirst();
		algoTxtCanvas.setMarkStyle(new MarkStyle(normal, diagramNormal, 2));
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.setMarkStyle(new MarkStyle(textHighlight, diagramNormal, 3));

		outputCanvas.setTextSegments(new String[] {Messages.getString(
			"synDiaEBNF", "GenerateWord.The_word_which_was_generated_is__n_23")}); //$NON-NLS-1$
	}

	/**
	 * This method is called if the forwardInHistoryButton on the GUI is pushed,
	 * it restore the next step with the history.
	 * 
	 * @exception IndexOutOfBoundsException if there is no further step to go
	 */
	public void nextHistStep()
	throws IndexOutOfBoundsException {
		if (!hasNextHistStep()) { throw new IndexOutOfBoundsException(
			"there is no further history step to go"); //$NON-NLS-1$
		}
		restoreStep(history.getNextHistoryStep());
		refreshGeneratedWord(generatedWord);
		// MAKE ready and controll if it works
		if (currentElement instanceof SynDiaTerminal) {
			redoNextTerm((SynDiaTerminal)currentElement);
		}
		else if (currentElement instanceof SynDiaVariable) { // SynDiaVariable
			redoNextVariable((SynDiaVariable)currentElement);
		}
		else if (currentElement instanceof SynDiaVariableBack) {
			// SynDiaVariable
			redoNextVariableBack((SynDiaVariableBack)currentElement);
		}
	}

	/**
	 * this method is called if the backwardButton on the GUI is pushed and
	 * should restore the last saved step of the visualisation
	 * 
	 * @exception IndexOutOfBoundsException if there is no previous step to go
	 */
	public void previousHistStep()
	throws IndexOutOfBoundsException {
		if (!hasPreviousHistStep()) { throw new IndexOutOfBoundsException(
			"there is no further history step to go back"); //$NON-NLS-1$
		}
		restoreStep(history.getPreviousHistoryStep());
		refreshGeneratedWord(generatedWord);

		stack.push(currentElement);

		// mark the right algorithm Text-field
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.mark(ALGO_DEF_FINDWAY);

		if (currentElement instanceof SynDiaTerminal) {
			((SynDiaTerminal)currentElement).unmarkObject(false);
			refreshGeneratedWord(generatedWord);
		}
		else if (currentElement instanceof SynDiaVariable) {
			// SynDiaVariable Jump in in the next step
			((SynDiaVariable)currentElement).unmarkObject(false);

			// remove correspondent backtracking label on StackCanvas
			stackCanvas.pop();

			// restore Backtracking diagram to set Background
			colorTheDiagram(((SynDiaVariable)currentElement).getHelpCopy()
			.getParentInitial().getGfx());

			// MAKE first Connection???
		}
		else if (currentElement instanceof SynDiaVariableBack) {
			// SynDiaVariableBackjump out!

			// display correspondent backtracking label on StackCanvas
			if (((SynDiaVariableBack)currentElement).getOriginal() != null) {
				stackCanvas.push("" //$NON-NLS-1$
					+ (((SynDiaVariableBack)currentElement).getOriginal()
					.getBacktrackingLabel()));
			}
		}
		else {
			// previousHistStep();
		}
	}

	/**
	 * this method is called if the "do algorithm" button on the GUI is pushed
	 * realize the backtracking algorithm has to find the next element in the
	 * syntactical diagram and...
	 */
	public void performNextStep() {
		// check, if it is actually possible to perform a further step
		if (hasPreviousHistStep()) {
			unmark(history.getStepElem(history.getPointer() - 1), true);
		}

		if (stack.peek() != null) {
			// fetch the new SynDiaElement to work with
			currentElement = stack.pop();

			// detect type of currentElem and go on accordingly
			if (currentElement instanceof SynDiaInitial) {
				doNextInitial((SynDiaInitial)currentElement);
			}
			else if (currentElement instanceof SynDiaEpsilon) {
				// go on Stack, this is an Epsilon f.e. in a Alternative,
				performNextStep();
			}
			else if (currentElement instanceof SynDiaTerminal) {
				// initialize BackTrackStep to save and restore later!
				history.addNewPosStep(stack, currentElement, generatedWord);
				doNextTerm((SynDiaTerminal)currentElement);
			}
			else if (currentElement instanceof SynDiaVariable) {
				// initialize BackTrackStep to save and restore later!
				history.addNewPosStep(stack, currentElement, generatedWord);
				doNextVariable((SynDiaVariable)currentElement);
			}
			else if (currentElement instanceof SynDiaVariableBack) {
				// initialize BackTrackStep to save and restore later!
				history.addNewPosStep(stack, currentElement, generatedWord);
				doNextVariableBack((SynDiaVariableBack)currentElement);
			}
			else { // Composite
				algoTxtCanvas.demarkAll();
				algoTxtCanvas.mark(ALGO_DEF_FINDWAY);
				if (currentElement instanceof SynDiaRepetition) {
					doNextRepetition((SynDiaRepetition)currentElement);
				}
				else if (currentElement instanceof SynDiaAlternative) {
					doNextAlternative((SynDiaAlternative)currentElement);
				}
				else if (currentElement instanceof SynDiaConcatenation) {
					doNextConcatenation((SynDiaConcatenation)currentElement);
				}
			}
		}
		else { // if null on stack
			stack.pop();
			performNextStep();
		}
		if (!hasNextStep()) {
			// mark the right algorithm text field
			algoTxtCanvas.demarkAll();
			algoTxtCanvas.mark(ALGO_DEF_FINDWAY);
			// dialog that the Algorithmen is Empty
			readyDialog();
		}
	}

	// ----------------------PerformNextStep()-----------------------------------

	private void doNextInitial(SynDiaInitial currentElem) {
		colorTheDiagram(currentElem.getGfx());

		// actualize StackConfiguration
		stack.push(currentElem.getInnerElem());
	}

	private void doNextTerm(SynDiaTerminal currentElem) {
		// mark the right algorithmen Text-field
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.mark(ALGO_DEF_TERM);

		// mark the SynDiaTerminal
		currentElem.markObject();

		// refresh the generatedWord
		generatedWord += currentElem.getLabel();
		refreshGeneratedWord(generatedWord);
	}

	private void doNextVariable(SynDiaVariable currentElem) { // jump in
		// mark the right algorithmen Text-field
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.mark(ALGO_DEF_VAR);

		// mark the currentElem
		currentElem.markObject();

		// set internal Stack config
		stack.push(currentElem.getHelpCopy()); // save to go back
		stack.push(currentElem.getStartElem());

		// display correspondent backtracking label on StackCanvas
		stackCanvas.push("" + currentElem.getBacktrackingLabel()); //$NON-NLS-1$

		// change diagram colors in Initial
	}

	private void doNextVariableBack(SynDiaVariableBack currentElem) { //jump out
		// mark the right algorithmen Text-field
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.mark(ALGO_DEF_DIAGRAM_FINISHED);

		// color last connection in the left diagram

		// restore Backtracking diagram to set Background
		colorTheDiagram(currentElem.getParentInitial().getGfx());

		// set new internal StackConfig
		// Variable removed in pop...

		// remove correspondent backtracking label on StackCanvas
		stackCanvas.pop();
	}

	private void doNextRepetition(SynDiaRepetition currentElem) {
		if (!currentElem.isStraightAheadElemDone()) {
			// set new Stack Configuration
			currentElem.setStraightAheadElemDone(true);
			stack.push(currentElem);
			stack.push(currentElem.getStraightAheadElem());
		}
		else { // StraightAheadElem already done
			currentElem.setStraightAheadElemDone(false);
			if (repetionDialog(currentElem)) {
				// go into the Repetition
				stack.push(currentElem);
				stack.push(currentElem.getRepeatedElem());
			}
		}
		performNextStep();
	}

	private void doNextAlternative(SynDiaAlternative currentElem) {
		stack.push(currentElem.getOption(alternativeDialog(currentElem)));
		performNextStep();
	}

	private void doNextConcatenation(SynDiaConcatenation currentElem) {
		List<SynDiaElement> list = currentElem.getContent();
		for (int i = list.size() - 1; i >= 0; i--) {
			stack.push(list.get(i));
		}
		performNextStep();
	}

	private void redoNextTerm(SynDiaTerminal currentElem) {
		// mark the right algorithmen Text-field
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.mark(ALGO_DEF_TERM);

		// mark the SynDiaTerminal
		currentElem.markObject();

		// refresh the generatedWord
		generatedWord += currentElem.getLabel();
		refreshGeneratedWord(generatedWord);
	}

	private void redoNextVariable(SynDiaVariable currentElem) {
		// jump in
		// mark the right algorithmen Text-field
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.mark(ALGO_DEF_VAR);

		// mark the currentElem
		currentElem.markObject();

		// set internal Stack config
		stack.push(currentElem); // save to go back
		stack.push(currentElem.getStartElem());

		// display correspondent backtracking label on StackCanvas
		stackCanvas.push(currentElem.getLabel());

		// change diagram colors in Initial
	}

	private void redoNextVariableBack(SynDiaVariableBack currentElem) {
		// jump out
		// mark the right algorithmen Text-field
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.mark(ALGO_DEF_DIAGRAM_FINISHED);

		// color last connection in the left diagramm

		// restore Backtracking diagram to set Background
		colorTheDiagram(currentElem.getParentInitial().getGfx());

		// remove correspondent backtracking label on StackCanvas
		stackCanvas.pop();
	}

	private void refreshGeneratedWord(String output) {
		outputCanvas.addSegment(Messages.getString("synDiaEBNF",
			"GenerateWord.The_word_which_was_generated_is__n_29") + output); //$NON-NLS-1$
	}

	private void readyDialog() {
		if (JAlgoGUIConnector.getInstance().showConfirmDialog(
			Messages.getString("synDiaEBNF", "GenerateWord.The_algorithm_has_finished_!_The_generated_Word_is__31") + //$NON-NLS-1$
				generatedWord +
				Messages.getString("synDiaEBNF", "GenerateWord.._Should_the_algorithm_be_closed__32"), //$NON-NLS-1$
			DialogConstants.YES_NO_OPTION) == DialogConstants.YES_OPTION) {
			finalTasks();
			moduleController.algoFinished();
		}
	}
}
