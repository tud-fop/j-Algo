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
import java.util.Stack;

import org.eclipse.draw2d.Figure;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.jalgo.main.gui.DialogConstants;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.gui.TextCanvas;
import org.jalgo.main.gui.widgets.StackCanvas;
import org.jalgo.main.util.GfxUtil;
import org.jalgo.main.util.Messages;
import org.jalgo.module.synDiaEBNF.gfx.InitialFigure;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaColors;
import org.jalgo.module.synDiaEBNF.synDia.ReadingOrder;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaAlternative;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaConcatenation;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaElement;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaInitial;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaRepetition;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaSystem;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaTerminal;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaVariable;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaVariableBack;

/**
 * Basic class to manage similar methods of the algorithmus to generate a word
 * or to recognize a word by a syntax diagram system.
 * 
 * @author Michael Pradel
 * @author Babett Schaliz
 * @author Benjamin Scholz
 */
public abstract class SynDiaBacktracking
implements IAlgorithm, Serializable, IAlgoDefConstants, SynDiaColors {

	protected ModuleController moduleController;
	protected StackCanvas stackCanvas; // the Canvas of the graphical stack
	protected TextCanvas algoTxtCanvas; // this Canvas display the algorithm
	protected TextCanvas outputCanvas;
	// this Canvas display the generated word
	protected Figure synDiaCanvas;

	protected String generatedWord = "";

	protected SynDiaSystem synDiaDef; // the SynDiaSystem to work with
	protected SynDiaElement currentElement = null;
	// current SynDiaElement to go through
	protected SynDiaInitial currentInitial;
	// help diagram which worked at the moment

	protected Stack<SynDiaElement> stack; // the internal stack

	protected BackTrackHistory history; // save the steps

	/**
	 * Constructor
	 */
	public SynDiaBacktracking(ModuleController moduleController, Figure figure,
		StackCanvas stackCanvas, TextCanvas algoTxtCanvas,
		TextCanvas generatedWordCanvas, SynDiaSystem synDiaDef) {
		this.moduleController = moduleController;
		this.stackCanvas = stackCanvas;
		this.algoTxtCanvas = algoTxtCanvas;
		outputCanvas = generatedWordCanvas;
		this.synDiaDef = synDiaDef;
		synDiaCanvas = figure;
		stack = new Stack<SynDiaElement>();

		// set correct reading order for all diagrams
		checkReadingOrder(synDiaDef);

		// trick to set right backgrounds
		SynDiaVariableBack help = new SynDiaVariableBack(null, synDiaDef
		.getStartElem());
		stack.push(help);

		// go to first SynDiaElement to work with
		// lay backtracking labels on stack
		stack.push(synDiaDef.getStartElem());

		// show backtracking labels
		for (SynDiaInitial item : synDiaDef.getInitialDiagrams()) {
			backtrackingLabels(item, true);
		}
		
		history = new BackTrackHistory();
	}

	/**
	 * @return true, if there is a previous element, so you can go a step back
	 *         in history; false if not
	 */
	public boolean hasPreviousHistStep() {
		return true;
	}

	/**
	 * @return true, if there is a next element, so you can go a step forward in
	 *         history; false if not
	 */
	public boolean hasNextHistStep() {
		return true;
	}

	/**
	 * Tests if there is a valid next step to go.
	 * 
	 * @return true, if you can perform the next step in the algorithm, so you
	 *         can call performNextStep() false if not
	 */
	public boolean hasNextStep() {
		return !stack.isEmpty();
	}

	/**
	 * this method is called if the "do algorithm" button on the GUI is pushed
	 * realise the backtrackingAlgorithm has to find the next element in the
	 * syntactical diagram and...
	 * 
	 * @exception IndexOutOfBoundsException if there is now further step to go
	 */
	public abstract void performNextStep()
	throws IndexOutOfBoundsException;

	/**
	 * This method is called if the forwardButton on the GUI is pushed and
	 * should restore the next saved step of the visualisation
	 * 
	 * @exception IndexOutOfBoundsException if there is now previous step to go
	 */
	public abstract void nextHistStep()
	throws IndexOutOfBoundsException;

	/**
	 * this method is called if the backwardButton on the GUI is pushed and
	 * should restore the last saved step of the visualisation
	 * 
	 * @exception IndexOutOfBoundsException if there is now previous step to go
	 */
	public abstract void previousHistStep()
	throws IndexOutOfBoundsException;

	/**
	 * Checks and corrects the reading order of all diagrams of the system
	 * <code>synDiaDef</code>.
	 * 
	 * @param synDiaDef The <code>SynDiaSystem</code> to check.
	 */
	protected void checkReadingOrder(SynDiaSystem synDiaDef) {
		for (SynDiaInitial item : synDiaDef.getInitialDiagrams()) {
			item.getInnerElem().checkReadingOrder(ReadingOrder.LEFT_TO_RIGHT);
		}
	}

	/**
	 * Recursively goes through <code>help</code> and its inner elements and
	 * (i) unmarks them, if they are marked,
	 * (ii) adds his backtracking label to each <code>SynDiaVariable</code> and
	 * makes a copy of it, which saves the information where to jump back to, after 
	 * the diagram it is pointing to was executed.
	 * <p>
	 * This function is called at the beginning of the algorithm, in order to
	 * set the backtracking labels.
	 * 
	 * @param help
	 * @param bool
	 */
	protected void backtrackingLabels(SynDiaElement help, boolean bool) {
		unmark(help, !bool);
		if (help instanceof SynDiaInitial) {
			currentInitial = (SynDiaInitial)help;
			backtrackingLabels(((SynDiaInitial)help).getInnerElem(), bool);
		}
		if (help instanceof SynDiaVariable) {
			SynDiaVariableBack elem = new SynDiaVariableBack(
				(SynDiaVariable)help, currentInitial);
			((SynDiaVariable)help).setHelpCopy(elem);
			((SynDiaVariable)help).getGfx().setIndexText(
				"" + ((SynDiaVariable)help).getBacktrackingLabel()); //$NON-NLS-1$
			((SynDiaVariable)help).getGfx().setIndexVisible(bool);
		}
		if (help instanceof SynDiaRepetition) {
			backtrackingLabels(((SynDiaRepetition)help).getStraightAheadElem(),
				bool);
			backtrackingLabels(((SynDiaRepetition)help).getRepeatedElem(), bool);
		}
		if (help instanceof SynDiaAlternative) {
			for (SynDiaElement item : ((SynDiaAlternative) help).getOptions())
				backtrackingLabels(item, bool);
		}
		if (help instanceof SynDiaConcatenation) {
			for (SynDiaElement item : ((SynDiaConcatenation) help).getContent())
				backtrackingLabels(item, bool);
		}
	}

	protected void unmark(SynDiaElement markobj, boolean bool) {
		if (markobj instanceof SynDiaTerminal) {
			((SynDiaTerminal)markobj).unmarkObject(bool);
		}
		else if (markobj instanceof SynDiaVariable) { // SynDiaVariable
			((SynDiaVariable)markobj).unmarkObject(bool);
		}
		else if (markobj instanceof SynDiaVariableBack) {
			// SynDiaVariable
			((SynDiaVariableBack)markobj).unmarkObject(bool);
		}
	}

	/**
	 * Should be called when the algorithm is finshed or aborted in order to
	 * hide the backtracking labels and unmark highlighted elements.
	 * 
	 */
	public void finalTasks() {
		hideBacktrackingLabels();

		for (SynDiaInitial synDiaInitial : synDiaDef.getInitialDiagrams())
			// reset background color to normal color
			synDiaInitial.getGfx().setBackgroundColor(diagramNormal);
	}

	/**
	 * This method is called if the algorithm is closed, aborted or finished and
	 * should hide the no longer used backtrackinglabels.
	 */
	public void hideBacktrackingLabels() {
		// hide backtracking labels
		for (SynDiaInitial synDiaInitial : synDiaDef.getInitialDiagrams())
			backtrackingLabels(synDiaInitial, false);
	}
	
	protected void restoreStep(BackTrackStep step) {
		generatedWord = step.getGeneratedWord();
		stack = step.getStackConfig();
		currentElement = step.getElem();
	}
	
	/**
	 * ask the user, whether the repetition should repeated
	 * @param repetition the repetition
	 * @return the user answer
	 */
	protected boolean repetionDialog(SynDiaRepetition repetition) {
		return JAlgoGUIConnector.getInstance().showConfirmDialog(
			Messages.getString("synDiaEBNF", 
				"GenerateWord.Do_you_want_to_go_through_the_repetition__41"), //$NON-NLS-1$
			DialogConstants.YES_NO_OPTION) == DialogConstants.YES_OPTION;
	}
	
	protected int alternativeDialog(SynDiaAlternative alternative) {
		List list = alternative.getOptions();
		int way = list.size(); // int of possible ways
		// ask the user, which way to go on
		// return the list index of the choosen way
		int result = 0;

		while (result == 0) {
			InputDialog inDialog = new InputDialog(
				GfxUtil.getAppShell(),
				Messages.getString("synDiaEBNF", "GenerateWord.Alternative_Dialog_33"), //$NON-NLS-1$
				Messages.getString("synDiaEBNF", "GenerateWord.The_ways_are_numbered_form_top_to_bottom._There_are__34") //$NON-NLS-1$
					+ way
					+ Messages.getString("synDiaEBNF", "GenerateWord._ways_to_go_!_Which_one_do_you_want_to_go__35"), //$NON-NLS-1$
				"", //$NON-NLS-1$
				null);
			if (inDialog.open() != Window.CANCEL) {
				try {
					result = (Integer.valueOf(inDialog.getValue())).intValue();
				}
				catch (NumberFormatException e) {
					result = 0;
				}
			}
			if ((result > 0) && (result <= way)) return result - 1;
			JAlgoGUIConnector.getInstance().showWarningMessage(
				Messages.getString("synDiaEBNF",
					"GenerateWord.Please_use_a_value_between_1_and__38") + //$NON-NLS-1$
					way + "."); //$NON-NLS-2$
			result = 0;
		}
		return 0;
	}
	
	protected void colorTheDiagram(InitialFigure current) {
		// reset all diagrams white
		for (InitialFigure initialFigure : synDiaDef.getGfx().getSynDias())
			initialFigure.setBackgroundColor(diagramNormal);

		// set the one
		current.setBackgroundColor(diagramHighlight);
	}
}
