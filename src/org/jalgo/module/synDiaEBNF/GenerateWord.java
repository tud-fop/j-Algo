/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on 30.04.2004
 */
 
package org.jalgo.module.synDiaEBNF;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.jalgo.main.gfx.MarkStyle;
import org.jalgo.main.gui.TextCanvas;
import org.jalgo.main.gui.widgets.StackCanvas;
import org.jalgo.main.util.GfxUtil;
import org.jalgo.main.util.Stack;
import org.jalgo.module.synDiaEBNF.gfx.InitialFigure;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaColors;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure;
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
 * the through the system, even backwards.
 * The algorithm can be aborted all the time. 
 * 
 * @author Babett Schaliz
 * @author Benjamin Scholz
 * @author Marco Zimmerling
 * @author Michael Pradel
 * @version %I%, %G%
 */
public class GenerateWord
	extends SynDiaBacktracking
	implements SynDiaColors, Serializable {
	private ModuleController moduleController;
	private StackCanvas stackCanvas; // the Canvas of the graphical stack
	private TextCanvas algoTxtCanvas; // this Canvas display the algorithm
	private TextCanvas outputCanvas; // this Canvas display the generated word
	private Figure synDiaCanvas;

	private Stack stack; // the internal stack
	private String generatedWord = ""; // the generated word //$NON-NLS-1$
	private SynDiaSystem synDiaDef; // the SynDiaSystem to work with

	private BackTrackHistory history; // save the steps

	private SynDiaElement currentElement;
	// current SynDiaElement to go through
	private SynDiaInitial currentInitial;
	// help diagram which worked at the moment

	/**
	* Constructor gets the Figure, Stack, the StackCanavas, the TextCanvases and
	* also the syntaxtical diagram SynDiaSystem, to work with. It also fill the 
	* TextCanvas and show the backtracking labels, set the startelement. 
	* @param figure	The <code> Figure </code> include the diagram system
	* @param stackCanvas	The <code> StackCanvas </code> of the graphical stack
	* @param algoTxtCanvas	The <code> TextCanvas </code> where the algorithm is displayed
	* @param generatedWordCanvas	The <code> TextCanvas </code> to display the generated word
	* @param synDiaDef	The <code> SynDiaSystem </code> to work with
	*/
	public GenerateWord(
		ModuleController moduleController,
		Figure figure,
		StackCanvas stackCanvas,
		TextCanvas algoTxtCanvas,
		TextCanvas generatedWordCanvas,
		SynDiaSystem synDiaDef) {

		this.moduleController = moduleController;
		this.stackCanvas = stackCanvas;
		this.algoTxtCanvas = algoTxtCanvas;
		outputCanvas = generatedWordCanvas;
		this.synDiaDef = synDiaDef;
		synDiaCanvas = figure;
		stack = new Stack();

		//trick to set right backgrounds
		SynDiaVariableBack help =
			new SynDiaVariableBack(null, synDiaDef.getStartElem());
		stack.push(help);

		// go to first SynDiaElement to work with
		// lay backtracking labels on stack 
		stack.push(synDiaDef.getStartElem());

		// algorithm written on page 22 in the script
		algoTxtCanvas.setTextSegments(
			new String[] {
				Messages.getString("GenerateWord.Algo_title_2"), //$NON-NLS-1$
				Messages.getString("GenerateWord.Algo_description_1_3") //$NON-NLS-1$
					+ Messages.getString("GenerateWord.Algo_description_2_4"), //$NON-NLS-1$
				Messages.getString("GenerateWord.Algo_description_3_5"), //$NON-NLS-1$
				Messages.getString("GenerateWord.Algo_description_4_6") //$NON-NLS-1$
					+ Messages.getString("GenerateWord.Algo_description_5_7") //$NON-NLS-1$
					+ Messages.getString("GenerateWord.Algo_description_6_8"), //$NON-NLS-1$
				Messages.getString("GenerateWord.Algo_description_7_9") //$NON-NLS-1$
					+ Messages.getString("GenerateWord.Algo_description_8_10") //$NON-NLS-1$
					+ Messages.getString("GenerateWord.Algo_description_9_11"), //$NON-NLS-1$
				Messages.getString("GenerateWord.Algo_description_10_12") //$NON-NLS-1$
					+ Messages.getString("GenerateWord.Algo_description_11_13") //$NON-NLS-1$
					+ Messages.getString("GenerateWord.Algo_description_12_14") //$NON-NLS-1$
					+ Messages.getString("GenerateWord.Algo_description_13_15") //$NON-NLS-1$
					+ Messages.getString("GenerateWord.Algo_description_14_16"), //$NON-NLS-1$
				Messages.getString("GenerateWord.Algo_description_15_17") //$NON-NLS-1$
					+ Messages.getString("GenerateWord.Algo_description_16_18") //$NON-NLS-1$
					+ Messages.getString("GenerateWord.Algo_description_17_19"), //$NON-NLS-1$
				Messages.getString("GenerateWord.Algo_description_18_20") //$NON-NLS-1$
					+ Messages.getString("GenerateWord.Algo_description_19_21"), //$NON-NLS-1$
				Messages.getString("GenerateWord.Algo_description_20_22") }); //$NON-NLS-1$
		algoTxtCanvas.markFirst();
		algoTxtCanvas.setMarkStyle(new MarkStyle(normal, diagramNormal, 2));
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.setMarkStyle(
			new MarkStyle(textHighlight, diagramNormal, 3));

		outputCanvas.setTextSegments(
			new String[] { Messages.getString("GenerateWord.The_word_which_was_generated_is__n_23") }); //$NON-NLS-1$

		//show backtracking labels
		for (int k = 0; k < this.synDiaDef.getInitialDiagrams().size(); k++) {
			BacktrackingLabels(synDiaDef.getInitialDiagram(k), true);
		}
		history = new BackTrackHistory();
	}

	/**
	* Test if there is a valid previous element in the history, to go there.
	* @return 	true, if there is a previous element in the history, so you can go a step back;
	* 			false if not
	*/
	public boolean hasPreviousHistStep() {
		return (history.getPointer() > 0);
	}

	/**
	* Test if there is a valid next element in the history, to go there
	* @return 	true, if there is a next element, so you can go a step forward in history; 
	* 			false if not
	*/
	public boolean hasNextHistStep() {
		return (history.getPointer() < history.getSize());
	}

	/**
	* Test if there is a valid next step, to go there. Else the algorithm is ready.
	* @return 	true if there is a next element, so you can go a NextStep();
	* 			false if not, therefore the algorithm is ready
	*/
	public boolean hasNextStep() {
		return !stack.isEmpty();
	}

	/**
	* This method is called if the forwardInHistoryButton on the GUI is pushed, 
	* it restore the next step with the history.
	* 
	* @exception IndexOutOfBounds   if there is no further step to go
	*/
	public void nextHistStep() throws IndexOutOfBoundsException {
		if (!hasNextHistStep()) {
			throw new IndexOutOfBoundsException("there is no further history step to go"); //$NON-NLS-1$
		}
		restoreStep(history.getNextHistoryStep());
		refreshGeneratedWord(generatedWord);
		//MAKE ready and controll if it works
		if (currentElement instanceof SynDiaTerminal) {
			redoNextTerm((SynDiaTerminal) currentElement);
		} else if (
			currentElement instanceof SynDiaVariable) { //SynDiaVariable
			redoNextVariable((SynDiaVariable) currentElement);
		} else if (currentElement instanceof SynDiaVariableBack) {
			//SynDiaVariable
			redoNextVariableBack((SynDiaVariableBack) currentElement);
		}
	}

	/**
	* this method is called if the backwardButton on the GUI is pushed
	* and should restore the last saved step of the visualisation
	* 
	* @exception IndexOutOfBounds   if there is no previous step to go
	*/
	public void previousHistStep() throws IndexOutOfBoundsException {
		if (!hasPreviousHistStep()) {
			throw new IndexOutOfBoundsException("there is no further history step to go back"); //$NON-NLS-1$
		}
		restoreStep(history.getLastHistoryStep());
		refreshGeneratedWord(generatedWord);

		stack.push(currentElement);

		//mark the right algorithm Text-field
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.mark(3); //legal way

		if (currentElement instanceof SynDiaTerminal) {
			// mark the SynDiaTerminal
			 ((SynDiaTerminal) currentElement).unmarkObject(false);

			// refresh the generatedWord
			refreshGeneratedWord(generatedWord);
		} else if (currentElement instanceof SynDiaVariable) {
			//SynDiaVariable Jump in in the next step

			// mark the currentElem
			 ((SynDiaVariable) currentElement).unmarkObject(false);

			// remove correspondent backtracking label on StackCanvas
			stackCanvas.pop();

			// restore Backtracking diagram to set Background
			colorTheDiagram(
				((SynDiaVariable) currentElement)
					.getHelpCopy()
					.getParentInitial()
					.getGfx());

			// MAKE first Connection??? 
		} else if (currentElement instanceof SynDiaVariableBack) {
			//SynDiaVariableBackjump out!

			// display correspondent backtracking label on StackCanvas
			if (((SynDiaVariableBack) currentElement).getOriginal()
				!= null) {
				stackCanvas.push(
					"" //$NON-NLS-1$
						+ (((SynDiaVariableBack) currentElement)
							.getOriginal()
							.getBacktrackingLabel()));
			}
		} else {
			//previousHistStep();
		}
	}

	/**
	 * this method is called if the "do algorithm" button on the GUI is pushed
	 * realize the backtracking algorithm
	 * has to find the next element in the syntactical diagram and...
	 */
	public void performNextStep() {
		// check, if it is actually possible to perform a further step
		if (hasPreviousHistStep()) {
			unmark(history.getStepElem(history.getPointer() - 1), true);
		}

		if (stack.peak() != null) {

			//fetch the new SynDiaElement to work with
			currentElement = (SynDiaElement) stack.pop();

			//detect type of currentElem and go on accordingly
			if (currentElement instanceof SynDiaInitial) {
				doNextInitial((SynDiaInitial) currentElement);
			} else if (currentElement instanceof SynDiaEpsilon) {
				//go on Stack, this is an Epsilon f.e. in a Alternative,
				performNextStep();
			} else if (currentElement instanceof SynDiaTerminal) {
				//initialize BackTrackStep to save and restore later!
				history.addNewPosStep(stack, currentElement, generatedWord);
				doNextTerm((SynDiaTerminal) currentElement);
			} else if (currentElement instanceof SynDiaVariable) {
				//initialize BackTrackStep to save and restore later!
				history.addNewPosStep(stack, currentElement, generatedWord);
				doNextVariable((SynDiaVariable) currentElement);
			} else if (currentElement instanceof SynDiaVariableBack) {
				//initialize BackTrackStep to save and restore later!
				history.addNewPosStep(stack, currentElement, generatedWord);
				doNextVariableBack((SynDiaVariableBack) currentElement);
			} else { // Composite
				algoTxtCanvas.demarkAll();
				algoTxtCanvas.mark(3); //Search the right Way
				if (currentElement instanceof SynDiaRepetition) { //repetition?
					doNextRepetition((SynDiaRepetition) currentElement);
				} else if (currentElement instanceof SynDiaAlternative) {
					doNextAlternative((SynDiaAlternative) currentElement);
				} else if (currentElement instanceof SynDiaConcatenation) {
					doNextConcatenation((SynDiaConcatenation) currentElement);
				}
			}
		} else { // if null on stack
			stack.pop();
			performNextStep();
		}
		if (!hasNextStep()) {
			//mark the right algorithm text field
			algoTxtCanvas.demarkAll();
			algoTxtCanvas.mark(3);
			// dialog that the Algorithmen is Empty
			readyDialog();
		}
	}

	/**
	* this method is called if the algorithm is closed, aborted or finished
	* and should hide the no longer needful Backtrackinglabels
	*/
	public void hideBacktrackingLabels() {
		//hide backtracking labels
		for (int k = 0; k < this.synDiaDef.getInitialDiagrams().size(); k++) {
			BacktrackingLabels(synDiaDef.getInitialDiagram(k), false);
		}
	}

	private void BacktrackingLabels(SynDiaElement help, boolean bool) {
		unmark(help, !bool);
		if (help instanceof SynDiaInitial) {
			currentInitial = (SynDiaInitial) help;
			BacktrackingLabels(((SynDiaInitial) help).getInnerElem(), bool);
		}
		if (help instanceof SynDiaVariable) {
			SynDiaVariableBack elem =
				new SynDiaVariableBack((SynDiaVariable) help, currentInitial);
			((SynDiaVariable) help).setHelpCopy(elem);
			((SynDiaVariable) help).getGfx().setIndexText(
				"" + ((SynDiaVariable) help).getBacktrackingLabel()); //$NON-NLS-1$
			((SynDiaVariable) help).getGfx().setIndexVisible(bool);
		}
		if (help instanceof SynDiaRepetition) {
			BacktrackingLabels(
				((SynDiaRepetition) help).getStraightAheadElem(),
				bool);
			BacktrackingLabels(
				((SynDiaRepetition) help).getRepeatedElem(),
				bool);
		}
		if (help instanceof SynDiaAlternative) {
			for (int i = 0;
				i < (((SynDiaAlternative) help).getNumOfOptions());
				i++) {
				BacktrackingLabels(
					((SynDiaAlternative) help).getOption(i),
					bool);
			}
		}
		if (help instanceof SynDiaConcatenation) {
			for (int j = 0;
				j < ((SynDiaConcatenation) help).getNumOfElements();
				j++) {
				BacktrackingLabels(
					((SynDiaConcatenation) help).getContent(j),
					bool);
			}
		}
	}

	private void colorTheDiagram(InitialFigure current) {
		// reset all diagrams white
		List list = this.synDiaDef.getGfx().getSynDias();

		for (int k = 0; k < list.size(); k++) {
			((SynDiaFigure) list.get(k)).setBackgroundColor(diagramNormal);
		}

		//set the one
		current.setBackgroundColor(diagramHighlight);
	}

	private void unmark(SynDiaElement markobj, boolean bool) {
		if (markobj instanceof SynDiaTerminal) {
			((SynDiaTerminal) markobj).unmarkObject(bool);
		} else if (markobj instanceof SynDiaVariable) { //SynDiaVariable
			 ((SynDiaVariable) markobj).unmarkObject(bool);
		} else if (markobj instanceof SynDiaVariableBack) {
			//SynDiaVariable
			 ((SynDiaVariableBack) markobj).unmarkObject(bool);
		}
	}

	//----------------------PerformNextStep()-----------------------------------

	private void doNextInitial(SynDiaInitial currentElem) {
		colorTheDiagram(currentElem.getGfx());

		//actualize StackConfiguration
		stack.push(currentElem.getInnerElem());
	}

	private void doNextTerm(SynDiaTerminal currentElem) {
		// mark the right algorithmen Text-field
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.mark(4); //SynDiaTerminal

		// mark the SynDiaTerminal
		 currentElem.markObject();

		// refresh the generatedWord
		generatedWord += currentElem.getLabel();
		refreshGeneratedWord(generatedWord);
	}

	private void doNextVariable(SynDiaVariable currentElem) { //jump in
		// mark the right algorithmen Text-field
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.mark(5); //SynDiaVariable

		// mark the currentElem
		currentElem.markObject();

		// set internal Stack config 
		stack.push(currentElem.getHelpCopy()); // save to go back
		stack.push(currentElem.getStartElem());

		// display correspondent backtracking label on StackCanvas
		stackCanvas.push(
			"" + currentElem.getBacktrackingLabel()); //$NON-NLS-1$

		// change diagram colors in Initial

	}

	private void doNextVariableBack(SynDiaVariableBack currentElem) { //jump out
		// mark the right algorithmen Text-field
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.mark(6); //Backtracking

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
		} else { //StraightAheadElem already done
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
		stack.push(
			currentElem.getOption(
				alternativeDialog(currentElem)));
		performNextStep();
	}

	private void doNextConcatenation(SynDiaConcatenation currentElem) {
		LinkedList list = currentElem.getContent();
		for (int i = list.size() - 1; i >= 0; i--) {
			stack.push(list.get(i));
		}
		performNextStep();
	}

	//----------------------------redoNextTerm()--------------------------------

	private void redoNextTerm(SynDiaTerminal currentElem) {
		// mark the right algorithmen Text-field
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.mark(4); //SynDiaTerminal

		// mark the SynDiaTerminal
		 currentElem.markObject();

		// refresh the generatedWord
		generatedWord =
			generatedWord + currentElem.getLabel();
		refreshGeneratedWord(generatedWord);
	}

	private void redoNextVariable(SynDiaVariable currentElem) {
		//jump in
		// mark the right algorithmen Text-field
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.mark(5); //SynDiaVariable

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
		//jump out
		// mark the right algorithmen Text-field
		algoTxtCanvas.demarkAll();
		algoTxtCanvas.mark(6); //backtracking

		// color last connection in the left diagramm

		// restore Backtracking diagram to set Background
		colorTheDiagram(currentElem.getParentInitial().getGfx());

		// remove correspondent backtracking label on StackCanvas
		stackCanvas.pop();
	}

	private void refreshGeneratedWord(String output) {
		outputCanvas.addSegment(Messages.getString("GenerateWord.The_word_which_was_generated_is__n_29") + output); //$NON-NLS-1$
	}

	private void readyDialog() {
		boolean result =
			MessageDialog.openQuestion(
				stackCanvas.getShell(),
				Messages.getString("GenerateWord.Algorithm_is_ready_30"), //$NON-NLS-1$
				Messages.getString("GenerateWord.The_algorithm_has_finished_!_The_generated_Word_is__31") //$NON-NLS-1$
					+ generatedWord
					+ Messages.getString("GenerateWord.._Should_the_algorithm_be_closed__32")); //$NON-NLS-1$
		if (result) {
			finalTasks();
			moduleController.algoFinished();
		}
	}

	private int alternativeDialog(SynDiaAlternative alternative) {
		LinkedList list = alternative.getOptions();
		int way = list.size(); // int of possible ways
		// ask the user, which way to go on
		//return the list index of the choosen way
		int result = 0;

		while (result == 0) {
			InputDialog inDialog =
				new InputDialog(
					GfxUtil.getAppShell(),
					Messages.getString("GenerateWord.Alternative_Dialog_33"), //$NON-NLS-1$
					Messages.getString("GenerateWord.The_ways_are_numbered_form_top_to_bottom._There_are__34") //$NON-NLS-1$
						+ way
						+ Messages.getString("GenerateWord._ways_to_go_!_Which_one_do_you_want_to_go__35"), //$NON-NLS-1$
					"", //$NON-NLS-1$
					null);
			if (inDialog.open() != Window.CANCEL) {
				try {
					result = (Integer.valueOf(inDialog.getValue())).intValue();
				} catch (NumberFormatException e) {
					//				MessageDialog.openError(
					//					null,
					//					"Warning",
					//					"Please use a integer value. Using default value now: 1.");
					result = 0;
				}
			}
			if ((result > 0) && (result <= way)) {
				return result - 1;
			}
			MessageDialog.openError(
				null,
				Messages.getString("GenerateWord.Warning_37"), //$NON-NLS-1$
				Messages.getString("GenerateWord.Please_use_a_value_between_1_and__38") + way + "."); //$NON-NLS-1$ //$NON-NLS-2$
			result = 0;
		}
		return 0;
	}

	private boolean repetionDialog(SynDiaRepetition repetition) {
		// ask the user, if the repetition should makes!
		// return boolean if or not
		return MessageDialog.openQuestion(
			GfxUtil.getAppShell(),
			Messages.getString("GenerateWord.Repetition_Dialog_40"), //$NON-NLS-1$
			Messages.getString("GenerateWord.Do_you_want_to_go_through_the_repetition__41")); //$NON-NLS-1$
	}

	private void restoreStep(BackTrackStep step) {
		generatedWord = step.getGeneratedWord();
		stack = step.getStackConfig();
		currentElement = step.getElem();
	}

	public void finalTasks() {
		hideBacktrackingLabels();
		Iterator it = synDiaDef.getInitialDiagrams().iterator();
		while (it.hasNext()) 
			((SynDiaInitial)it.next()).getGfx().setBackgroundColor(diagramNormal);
	}

}
