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
 * Created on 01.05.2004
 */

package org.jalgo.module.synDiaEBNF;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ToolbarLayout;
import org.jalgo.main.InternalErrorException;
import org.jalgo.main.gfx.MarkStyle;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.gui.TextCanvas;
import org.jalgo.main.gui.widgets.StackCanvas;
import org.jalgo.main.util.Messages;
import org.jalgo.module.synDiaEBNF.ebnf.EbnfAlternative;
import org.jalgo.module.synDiaEBNF.ebnf.EbnfConcatenation;
import org.jalgo.module.synDiaEBNF.ebnf.EbnfDefinition;
import org.jalgo.module.synDiaEBNF.ebnf.EbnfOption;
import org.jalgo.module.synDiaEBNF.ebnf.EbnfPrecedence;
import org.jalgo.module.synDiaEBNF.ebnf.EbnfRepetition;
import org.jalgo.module.synDiaEBNF.ebnf.EbnfSynVariable;
import org.jalgo.module.synDiaEBNF.ebnf.EbnfTerminal;
import org.jalgo.module.synDiaEBNF.gfx.AlternativeFigure;
import org.jalgo.module.synDiaEBNF.gfx.ConcatenationFigure;
import org.jalgo.module.synDiaEBNF.gfx.EmptyFigure;
import org.jalgo.module.synDiaEBNF.gfx.InitialFigure;
import org.jalgo.module.synDiaEBNF.gfx.RepetitionFigure;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaColors;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaException;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaSystemFigure;
import org.jalgo.module.synDiaEBNF.gfx.TerminalFigure;
import org.jalgo.module.synDiaEBNF.gfx.ToTransFigure;
import org.jalgo.module.synDiaEBNF.gfx.VariableFigure;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaAlternative;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaConcatenation;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaElement;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaEpsilon;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaInitial;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaRepetition;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaSystem;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaTerminal;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaToTrans;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaVariable;

/**
 * Represents the algorithm for transforming from an EBNF term to a syntactical
 * diagram. The algorithm is described in the A&D script of Prof. Vogler from
 * the 7th Oct. 2002 at page 141.
 * 
 * @author Marco Zimmerling
 * @author Benjamin Scholz
 */
public class TransAlgorithm
implements IAlgorithm, Serializable {

	private static final long serialVersionUID = -279980842844233688L;
	// position in txtCanvas for EbnfSynVariable
	private final int ALGO_POS_VARIABLE = 1;
	// position in txtCanvas for EbnfTerminal
	private final int ALGO_POS_TERMINAL= 2;
	// position in txtCanvas for the case of one alpha
	private final int ALGO_POS_ONE_ALPHA = 3;
	// position in txtCanvas for EbnfRepetition
	private final int ALGO_POS_REPETITION = 4;
	// position in txtCanvas for EbnfOption
	private final int ALGO_POS_OPTION = 5;
	// position in txtCanvas for EbnfPrecedence
	private final int ALGO_POS_PRECEDENCE = 6;
	// position in txtCanvas for the case of two alphas
	private final int ALGO_POS_TWO_ALPHAS = 7;
	// position in txtCanvas for EbnfConcatenation
	private final int ALGO_POS_CONCATENATION = 8;
	// position in txtCanvas for EbnfAlternative
	private final int ALGO_POS_ALTERNATIVE = 9;

	// instance of current module controller
	private ModuleController moduleController;
	// canvas to display the algorithm as described in the lecture
	private TextCanvas txtCanvas;
	// canvas to display the grafical figures
	private Figure gfxCanvas;
	// canvas to display which syntactical variables still have to be transformed
	private StackCanvas toTransCanvas;

	// abstract representation of the generated synDiaSystem which also
	// holds all graphical figures
	private SynDiaSystem synDiaSystem;

	// holds EbnfElements which still have to be transformed
	private Stack<SynDiaElement> stillToTrans;
	private Stack<SynDiaElement> parentToTrans;
	// holds all EbnfSynVariables which still have to be transfromed
	private Stack<EbnfSynVariable> toTransVariables;
	// holds all SynDiaVariables (key->this, value->label) which are
	// already created but have no appropriate reference to its startElem
	private List<SynDiaVariable> alreadyCreatedVariables;
	// holds all SynDiaVariables which represent a complete syntactical
	// diagram, befor finishing the elements of alreadyCreatedVariables have
	// to be finalized where its startElem has to reference the startElem of
	// the appropriate element in alreadyCreatedDias
	private List<SynDiaVariable> alreadyCreatedDias;

	// true if the algorithm should perform automatically, otherwise false
	private boolean auto;

	/**
	 * Is a data structure, which contains all SynDiaElements which have to be
	 * deleted, when the user goes another way forward, than written in the
	 * history (with performNextStep() ). <p>
	 * has to be updated as following: <p>
	 *   - in previousHistStep(): add elements, which are replaced by SynDiaToTrans 
	 *         (that means between lastStep.getElementInFrontOfTransformed() 
	 *         and lastStep.getTransformed.next() ) <p>
	 *   - in nextHistStep(): remove elements, which were created replacing 
	 * SynDiaToTrans (that means all elements between
	 * currentStep.getElementInFrontOfTransformed() and
	 *         currentStep.getTransformed.next() ) <p>
	 *   - in performNextStep(): remove all elements
	 * @see TransAlgorithm#previousHistStep()
	 * @see TransAlgorithm#nextHistStep()
	 * @see TransAlgorithm#performNextStep()
	 */
	// private Set trash;
	/**
	 * If you use this constructor auto by default is false.
	 * 
	 * @param ebnfDef the Ebnf EbnfDefinition which should be transformed
	 */
	public TransAlgorithm(
		ModuleController moduleController,
		Figure gfxCanvas,
		TextCanvas txtCanvas,
		StackCanvas toTransCanvas,
		EbnfDefinition ebnfDef) {
		this(moduleController, gfxCanvas, txtCanvas, toTransCanvas, ebnfDef,
			false);
	}

	/**
	 * Constructor which finally starts the trans algorithm.
	 * 
	 * @param ebnfDef the Ebnf EbnfDefinition which should be transformed
	 * @param auto true if the Algorithm should perform automatically
	 */
	public TransAlgorithm(ModuleController moduleController, Figure gfxCanvas,
		TextCanvas txtCanvas, StackCanvas toTransCanvas,
		EbnfDefinition ebnfDef, boolean auto) {
		this.moduleController = moduleController;
		this.gfxCanvas = gfxCanvas;
		this.txtCanvas = txtCanvas;
		this.toTransCanvas = toTransCanvas;
		this.auto = auto;

		// inititialize the txtCanvas, display the written trans algorithm
		txtCanvas.setTextSegments(new String[] {
			Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_1_1") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_2_2") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_3_3") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_4_4") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_5_5") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_6_6"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_7_7") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_8_8"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_9_9") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_10_10"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_11_11"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_12_12") //$NON-NLS-1$
				+ Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_13_13"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_14_14"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_15_15"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_16_16"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_17_17"), //$NON-NLS-1$
			Messages.getString("synDiaEBNF", "TransAlgorithm.Algo_Description_18_18") //$NON-NLS-1$
		});
		txtCanvas.setMarkStyle(new MarkStyle(SynDiaColors.textHighlight,
			SynDiaColors.diagramNormal, 3));

		// initializes the gfxCanvas
		ToolbarLayout layout = new ToolbarLayout();
		layout.setSpacing(10);
		gfxCanvas.setLayoutManager(layout);

		// write all variables from the EbnfDef onto the stack
		// (toTransVariables), S will be on top of the stack afterwards
		LinkedList<EbnfSynVariable> tVarList = new LinkedList<EbnfSynVariable>(
			ebnfDef.getVariables()); // get them from the ebnfDef
		toTransVariables = new Stack<EbnfSynVariable>();
		
		// S already at bottom?
		if (tVarList.getLast() == ebnfDef.getStartVariable()) {
			Collections.reverse(tVarList);
		}
		// S anywhere else
		else  if (tVarList.getFirst() != ebnfDef.getStartVariable()) {
			EbnfSynVariable startVar = ebnfDef.getStartVariable();
			tVarList.set(tVarList.indexOf(startVar), tVarList.getFirst());
			tVarList.set(0, startVar);
		}
		toTransVariables.addAll(tVarList);
		// also put the variables onto the toTransCanvas apart from S
		for (int i = toTransVariables.size() - 1; i >= 1; i--)
			toTransCanvas.push(toTransVariables.get(i).getLabel());

		// get next varibale from stack
		EbnfSynVariable newEbnfSynDia = toTransVariables.pop();
		// create new SynDiaToTrans by passing the startElement and its grafical
		// object
		SynDiaToTrans newTrans = new SynDiaToTrans(newEbnfSynDia.getStartElem());
		ToTransFigure newTransFigure = new ToTransFigure(newEbnfSynDia
		.getStartElem().toString(), gfxCanvas.getFont());
		newTrans.setGfx(newTransFigure);
		// create new InitialFigure labeled with the name of the new syntactical
		// diagram
		InitialFigure newInitialFigure = new InitialFigure(newEbnfSynDia
		.getLabel());
		newInitialFigure.replace(newTransFigure, 0);
		// create new abstract initial element by passing its figure and its
		// inner element
		SynDiaInitial newInitial = new SynDiaInitial(newInitialFigure, newTrans);
		// create new abstract syntactical variable (diagram)
		SynDiaVariable newSynDia = new SynDiaVariable(newEbnfSynDia.getLabel(),
			newInitial, gfxCanvas.getFont());

		stillToTrans = new Stack<SynDiaElement>();
		// put the initial SynDiaToTrans element on the stack
		stillToTrans.push(newSynDia.getStartElem().getInnerElem());
		 
		parentToTrans = new Stack<SynDiaElement>();
		// put the initial SynDiaInitial on the stack
		parentToTrans.push(newSynDia.getStartElem());  

		alreadyCreatedVariables = new LinkedList<SynDiaVariable>();

		alreadyCreatedDias = new LinkedList<SynDiaVariable>();
		alreadyCreatedDias.add(newSynDia); // add it to the list

		gfxCanvas.add(newSynDia.getStartElem().getGfx()); // display it on the
		// gfxCanvas

		highlightTxtAndElem(); // highlight corresponding part of the written
		// algorithm

		// initialize the SynDiaSystem which has to be created by this
		// algorithm, the terminals and variables are already set here
		synDiaSystem = new SynDiaSystem();
		HashSet<String> variables = new HashSet<String>();
		for (EbnfSynVariable ebnfSynVariable : ebnfDef.getVariables()) {
			variables.add(ebnfSynVariable.getLabel());
		}
		synDiaSystem.setSynVariables(variables);
		HashSet<String> alphabet = new HashSet<String>();
		for (EbnfTerminal ebnfTerminal : ebnfDef.getAlphabet()) {
			alphabet.add(ebnfTerminal.getLabel());
		}
		synDiaSystem.setTerminalSymbols(alphabet);
	}

	/**
	 * @return true if the Algorithm should perform automatically
	 */
	public boolean getAuto() {
		return auto;
	}

	/**
	 * @param auto true if the Algorithm should perform automatically
	 */
	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	/**
	 * @return true if you can perform the next step in the algorithm, so you
	 *         can call performNextStep() false if not
	 */
	public boolean hasNextStep() {
		return (!stillToTrans.isEmpty() || !toTransVariables.isEmpty());
	}

	/**
	 * @return true if there is a next element, so you can go a step forward in
	 *         history; false if not
	 */
	public boolean hasNextHistStep() {
		return false;
	}

	/**
	 * This method is called if the forwardButton on the GUI is pushed. It
	 * restores the next saved step of the visualisation.
	 * 
	 * @exception IndexOutOfBoundsException if there is no previous step to go
	 */
	public void nextHistStep()
	throws IndexOutOfBoundsException {}

	/**
	 * @return true if there is a previous element, so you can go a step back in
	 *         history false if not
	 */
	public boolean hasPreviousHistStep() {
		return false;
	}

	/**
	 * This method is called if the backwardButton on the GUI is pushed. It
	 * restores the last saved step of the visualisation.
	 * 
	 * @exception IndexOutOfBoundsException if there is no previous step to go
	 */
	public void previousHistStep()
	throws IndexOutOfBoundsException {}

	/**
	 * Perfoms the whole Algorithm automatically. Nondeterministic decissions
	 * are made randomly.
	 */
	public void autoTransAll() {
		boolean rememberAuto = auto;
		setAuto(true);
		while (hasNextStep()) {
			try {
				performNextStep();
			}
			catch (SynDiaException exc) {}
		}
		setAuto(rememberAuto);
	}

	public void setModuleController(ModuleController mc) {
		moduleController = mc;
	}

	public SynDiaSystem getSynDiaSystem() {
		return synDiaSystem;
	}

	private void showDialogFinished() {
		JAlgoGUIConnector.getInstance().showInfoMessage(
			Messages.getString("synDiaEBNF", //$NON-NLS-1$
				"TransAlgorithm.The_algorithm_has_been_completed_!_20")); //$NON-NLS-2$
	}

	private void highlightTxtAndElem() {
		txtCanvas.demarkAll();

		SynDiaToTrans trans = (SynDiaToTrans)stillToTrans.peek();
		trans.getGfx().highlight(true);

		if (trans.getEbnf() instanceof EbnfSynVariable) {
			txtCanvas.mark(ALGO_POS_VARIABLE);
			return;
		}

		if (trans.getEbnf() instanceof EbnfTerminal) {
			txtCanvas.mark(ALGO_POS_TERMINAL);
			return;
		}

		if (trans.getEbnf() instanceof EbnfRepetition) {
			txtCanvas.mark(ALGO_POS_ONE_ALPHA);
			txtCanvas.mark(ALGO_POS_REPETITION);
			return;
		}

		if (trans.getEbnf() instanceof EbnfOption) {
			txtCanvas.mark(ALGO_POS_ONE_ALPHA);
			txtCanvas.mark(ALGO_POS_OPTION);
			return;
		}

		if (trans.getEbnf() instanceof EbnfPrecedence) {
			txtCanvas.mark(ALGO_POS_ONE_ALPHA);
			txtCanvas.mark(ALGO_POS_PRECEDENCE);
			return;
		}

		if (trans.getEbnf() instanceof EbnfConcatenation) {
			txtCanvas.mark(ALGO_POS_TWO_ALPHAS);
			txtCanvas.mark(ALGO_POS_CONCATENATION);
			return;
		}

		if (trans.getEbnf() instanceof EbnfAlternative) {
			txtCanvas.mark(ALGO_POS_TWO_ALPHAS);
			txtCanvas.mark(ALGO_POS_ALTERNATIVE);
			return;
		}
	}

	public Figure getSynDiaFigure() {
		return gfxCanvas;
	}

	/**
	 * This method is called if the "do algorithm" button on the GUI is pushed.
	 * It realises the transAlgorithm.
	 * <p>
	 * If there is a non-deterministic decission (stillToTrans contains more
	 * than one element) the auto Boolean decides wether the user is asked or
	 * one is choosen randomly.
	 * <p>
	 * If stillToTrans is empty the Iterator will be increased and the first
	 * EbnfElement from the next toTransVariable will be written in stillToTrans
	 * 
	 * @exception IndexOutOfBoundsException if there is no further step to go
	 * @see TransAlgorithm#stillToTrans
	 */
	// FIXME: fix this bloat (Stephan)
	public void performNextStep()
	throws IndexOutOfBoundsException, SynDiaException {
		if (stillToTrans.isEmpty()) { // syntactical variable finished?
			EbnfSynVariable newEbnfSynDia = toTransVariables.pop();
			
			System.out.println("to trans pop: " + newEbnfSynDia.getLabel());
			SynDiaToTrans newTrans = new SynDiaToTrans(newEbnfSynDia
			.getStartElem());
			ToTransFigure newTransFigure = new ToTransFigure(newEbnfSynDia
			.getStartElem().toString(), gfxCanvas.getFont());
			newTrans.setGfx(newTransFigure);
			
			InitialFigure newInitialFigure = new InitialFigure(newEbnfSynDia
			.getLabel());
			newInitialFigure.replace(newTransFigure, 0);
			
			SynDiaInitial newInitial = new SynDiaInitial(newInitialFigure,
				newTrans);

			SynDiaVariable newSynDia = new SynDiaVariable(newEbnfSynDia
			.getLabel(), newInitial, gfxCanvas.getFont());

			stillToTrans.push(newSynDia.getStartElem().getInnerElem());
			parentToTrans.push(newSynDia.getStartElem());

			alreadyCreatedDias.add(newSynDia); // add it to the list
			System.out.println("add: " + newSynDia.getLabel());

			gfxCanvas.add(newSynDia.getStartElem().getGfx()); // display it on
			// the gfxCanvas
			toTransCanvas.pop(); // also refresh display of variables to be
			// transformed
			highlightTxtAndElem();
		}

		SynDiaToTrans trans = (SynDiaToTrans)stillToTrans.pop();

		// *** EbnfSynVariable ***
		if (trans.getEbnf() instanceof EbnfSynVariable) {
			// create new abstract variable and its corresponding grafical
			// object
			SynDiaVariable newVariable = new SynDiaVariable(
				((EbnfSynVariable)trans.getEbnf()).getLabel(), gfxCanvas
				.getFont());
			newVariable.setGfx(new VariableFigure(((EbnfSynVariable)trans
			.getEbnf()).getLabel(), gfxCanvas.getFont()));
			// store this variable to set its startElem befor finishing the
			// algorithm
			alreadyCreatedVariables.add(newVariable);

			if (parentToTrans.peek() instanceof SynDiaInitial) {
				SynDiaInitial topElem = (SynDiaInitial)parentToTrans.pop();
				topElem.setInnerElem(newVariable);
				topElem.getGfx().replace(newVariable.getGfx(), 0);
			}
			else if (parentToTrans.peek() instanceof SynDiaAlternative) {
				SynDiaAlternative topElem = (SynDiaAlternative)parentToTrans
				.peek();
				int index = 0;
				if (topElem.getOption(0) != trans) index = 1;
				if (index == 1
					|| (topElem.getOption(1) instanceof SynDiaEpsilon)) topElem = (SynDiaAlternative)parentToTrans
				.pop();
				topElem.setOption(index, newVariable);
				topElem.getGfx().replace(newVariable.getGfx(), index);
			}
			else if (parentToTrans.peek() instanceof SynDiaConcatenation) {
				SynDiaConcatenation topElem = (SynDiaConcatenation)parentToTrans
				.peek();
				int index = topElem.getContent().indexOf(trans);
				if (index == topElem.getNumOfElements() - 1) topElem = (SynDiaConcatenation)parentToTrans
				.pop();
				topElem.setContent(index, newVariable);
				topElem.getGfx().replace(newVariable.getGfx(), index);
			}
			else if (parentToTrans.peek() instanceof SynDiaRepetition) {
				SynDiaRepetition topElem = (SynDiaRepetition)parentToTrans
				.pop();
				topElem.setRepeatedElem(newVariable);
				topElem.getGfx().replace(newVariable.getGfx(), 1);
			}
			else throw new InternalErrorException(
				"no path in the Algorithm matches"); //$NON-NLS-1$
		}
		// *** EbnfTerminal ***
		else if (trans.getEbnf() instanceof EbnfTerminal) {
			// create a new abstract terminal and its corresponding grafical
			// object
			SynDiaTerminal newTerminal = new SynDiaTerminal(
				((EbnfTerminal)trans.getEbnf()).getLabel(), gfxCanvas.getFont());
			newTerminal.setGfx(new TerminalFigure(newTerminal.getLabel(),
				gfxCanvas.getFont()));

			if (parentToTrans.peek() instanceof SynDiaInitial) {
				SynDiaInitial topElem = (SynDiaInitial)parentToTrans.pop();
				topElem.setInnerElem(newTerminal);
				topElem.getGfx().replace(newTerminal.getGfx(), 0);
			}
			else if (parentToTrans.peek() instanceof SynDiaAlternative) {
				SynDiaAlternative topElem = (SynDiaAlternative)parentToTrans
				.peek();
				int index = 0;
				if (topElem.getOption(0) != trans) index = 1;
				if (index == 1
					|| (topElem.getOption(1) instanceof SynDiaEpsilon)) topElem = (SynDiaAlternative)parentToTrans
				.pop();
				topElem.setOption(index, newTerminal);
				topElem.getGfx().replace(newTerminal.getGfx(), index);
			}
			else if (parentToTrans.peek() instanceof SynDiaConcatenation) {
				SynDiaConcatenation topElem = (SynDiaConcatenation)parentToTrans
				.peek();
				int index = topElem.getContent().indexOf(trans);
				if (index == topElem.getNumOfElements() - 1) topElem = (SynDiaConcatenation)parentToTrans
				.pop();
				topElem.setContent(index, newTerminal);
				topElem.getGfx().replace(newTerminal.getGfx(), index);
			}
			else if (parentToTrans.peek() instanceof SynDiaRepetition) {
				SynDiaRepetition topElem = (SynDiaRepetition)parentToTrans
				.pop();
				topElem.setRepeatedElem(newTerminal);
				topElem.getGfx().replace(newTerminal.getGfx(), 1);
			}
			else throw new InternalErrorException(
				"no path in the Algorithm matches"); //$NON-NLS-1$
		}
		// *** EbnfRepetition ***
		else if (trans.getEbnf() instanceof EbnfRepetition) {
			// create new SynDiaToTrans element as the repeated element and set
			// its grafical representation (ToTransFigure)
			SynDiaToTrans newTrans = new SynDiaToTrans(((EbnfRepetition)trans
			.getEbnf()).getContent());
			ToTransFigure newTransFigure = new ToTransFigure(newTrans
			.getLabel(), gfxCanvas.getFont());
			newTrans.setGfx(newTransFigure);
			// create the corresponding grafical representation of the new
			// SynDiaToTrans, straightAheadElem is an EmptyFigure
			RepetitionFigure newRepetitionFigure = new RepetitionFigure();
			newRepetitionFigure.replace(new EmptyFigure(), 0);
			newRepetitionFigure.replace(newTransFigure, 1);
			// create a new abstract repetition by passing its grafical
			// representation and its abstract elements
			SynDiaRepetition newRepetition = new SynDiaRepetition(
				newRepetitionFigure, new SynDiaEpsilon(), newTrans);
			stillToTrans.push(newRepetition.getRepeatedElem());

			if (parentToTrans.peek() instanceof SynDiaInitial) {
				SynDiaInitial topElem = (SynDiaInitial)parentToTrans.pop();
				topElem.setInnerElem(newRepetition);
				topElem.getGfx().replace(newRepetition.getGfx(), 0);
				parentToTrans.push(topElem.getInnerElem());
			}
			else if (parentToTrans.peek() instanceof SynDiaAlternative) {
				SynDiaAlternative topElem = (SynDiaAlternative)parentToTrans
				.peek();
				int index = 0;
				if (topElem.getOption(0) != trans) index = 1;
				if (index == 1
					|| (topElem.getOption(1) instanceof SynDiaEpsilon)) topElem = (SynDiaAlternative)parentToTrans
				.pop();
				topElem.setOption(index, newRepetition);
				topElem.getGfx().replace(newRepetition.getGfx(), index);
				parentToTrans.push(topElem.getOption(index));
			}
			else if (parentToTrans.peek() instanceof SynDiaConcatenation) {
				SynDiaConcatenation topElem = (SynDiaConcatenation)parentToTrans
				.peek();
				int index = topElem.getContent().indexOf(trans);
				if (index == topElem.getNumOfElements() - 1) topElem = (SynDiaConcatenation)parentToTrans
				.pop();
				topElem.setContent(index, newRepetition);
				topElem.getGfx().replace(newRepetition.getGfx(), index);
				parentToTrans.push(topElem.getContent(index));
			}
			else if (parentToTrans.peek() instanceof SynDiaRepetition) {
				SynDiaRepetition topElem = (SynDiaRepetition)parentToTrans
				.pop();
				topElem.setRepeatedElem(newRepetition);
				topElem.getGfx().replace(newRepetition.getGfx(), 1);
				parentToTrans.push(topElem.getRepeatedElem());
			}
			else throw new InternalErrorException(
				"no path in the Algorithm matches"); //$NON-NLS-1$
		}
		// *** EbnfOption ***
		else if (trans.getEbnf() instanceof EbnfOption) {
			SynDiaToTrans newTrans = new SynDiaToTrans(((EbnfOption)trans
			.getEbnf()).getContent());
			ToTransFigure newTransFigure = new ToTransFigure(newTrans
			.getLabel(), gfxCanvas.getFont());
			newTrans.setGfx(newTransFigure);
			// create the corresponding grafical representation of the new
			// SynDiaToTrans, repeated element is an EmptyFigure
			AlternativeFigure newAlternativeFigure = new AlternativeFigure(2);
			newAlternativeFigure.replace(newTransFigure, 0);
			newAlternativeFigure.replace(new EmptyFigure(), 1);
			// create a new abstract repetition by passing its grafical
			// representation and its abstract elements
			List<SynDiaElement> elements = new LinkedList<SynDiaElement>();
			elements.add(newTrans);
			elements.add(new SynDiaEpsilon());
			SynDiaAlternative newAlternative = new SynDiaAlternative(
				newAlternativeFigure, elements);
			stillToTrans.push(newAlternative.getOption(0));

			if (parentToTrans.peek() instanceof SynDiaInitial) {
				SynDiaInitial topElem = (SynDiaInitial)parentToTrans.pop();
				topElem.setInnerElem(newAlternative);
				topElem.getGfx().replace(newAlternative.getGfx(), 0);
				parentToTrans.push(topElem.getInnerElem());
			}
			else if (parentToTrans.peek() instanceof SynDiaAlternative) {
				SynDiaAlternative topElem = (SynDiaAlternative)parentToTrans
				.peek();
				int index = 0;
				if (topElem.getOption(0) != trans) index = 1;
				if (index == 1
					|| (topElem.getOption(1) instanceof SynDiaEpsilon)) topElem = (SynDiaAlternative)parentToTrans
				.pop();
				topElem.setOption(index, newAlternative);
				topElem.getGfx().replace(newAlternative.getGfx(), index);
				parentToTrans.push(topElem.getOption(index));
			}
			else if (parentToTrans.peek() instanceof SynDiaConcatenation) {
				SynDiaConcatenation topElem = (SynDiaConcatenation)parentToTrans
				.peek();
				int index = topElem.getContent().indexOf(trans);
				if (index == topElem.getNumOfElements() - 1) topElem = (SynDiaConcatenation)parentToTrans
				.pop();
				topElem.setContent(index, newAlternative);
				topElem.getGfx().replace(newAlternative.getGfx(), index);
				parentToTrans.push(topElem.getContent(index));
			}
			else if (parentToTrans.peek() instanceof SynDiaRepetition) {
				SynDiaRepetition topElem = (SynDiaRepetition)parentToTrans
				.pop();
				topElem.setRepeatedElem(newAlternative);
				topElem.getGfx().replace(newAlternative.getGfx(), 1);
				parentToTrans.push(topElem.getRepeatedElem());
			}
			else throw new InternalErrorException(
				"no path in the Algorithm matches"); //$NON-NLS-1$
		}
		// *** EbnfPrecedence ***
		else if (trans.getEbnf() instanceof EbnfPrecedence) {
			SynDiaToTrans newTrans = new SynDiaToTrans(((EbnfPrecedence)trans
			.getEbnf()).getContent());
			newTrans.setGfx(new ToTransFigure(newTrans.getEbnf().toString(),
				gfxCanvas.getFont()));
			stillToTrans.push(newTrans);

			if (parentToTrans.peek() instanceof SynDiaInitial) {
				SynDiaInitial topElem = (SynDiaInitial)parentToTrans.pop();
				topElem.setInnerElem(newTrans);
				topElem.getGfx().replace(newTrans.getGfx(), 0);
				parentToTrans.push(topElem);
			}
			else if (parentToTrans.peek() instanceof SynDiaAlternative) {
				SynDiaAlternative topElem = (SynDiaAlternative)parentToTrans
				.peek();
				int index = 0;
				if (topElem.getOption(0) != trans) index = 1;
				topElem.setOption(index, newTrans);
				topElem.getGfx().replace(newTrans.getGfx(), index);
			}
			else if (parentToTrans.peek() instanceof SynDiaConcatenation) {
				SynDiaConcatenation topElem = (SynDiaConcatenation)parentToTrans
				.peek();
				int index = topElem.getContent().indexOf(trans);
				topElem.setContent(index, newTrans);
				topElem.getGfx().replace(newTrans.getGfx(), index);
			}
			else if (parentToTrans.peek() instanceof SynDiaRepetition) {
				SynDiaRepetition topElem = (SynDiaRepetition)parentToTrans
				.pop();
				topElem.setRepeatedElem(newTrans);
				topElem.getGfx().replace(newTrans.getGfx(), 1);
				parentToTrans.push(topElem.getRepeatedElem());
			}
			else throw new InternalErrorException(
				"no path in the Algorithm matches"); //$NON-NLS-1$
		}
		// *** EBNFConcatenation ***
		else if (trans.getEbnf() instanceof EbnfConcatenation) {
			List<SynDiaElement> elements = new LinkedList<SynDiaElement>(); // list
			// of
			// new
			// SynDiaToTrans-elements
			ConcatenationFigure newConcatenationFigure = new ConcatenationFigure(
				((EbnfConcatenation)trans.getEbnf()).getNumOfElements()); // corresponding
			// figure
			// create a new SynDiaToTrans for each element in the
			// EbnfConcatenation (trans.getEbnf())
			// create and set the corresponding ToTransFigure with its label
			// representing the EBNF-term to trans
			// also replace the corresponding CloudFigure by a ToTransFigure
			// add the new SynDiaToTrans to the list
			for (int i = 0; i < ((EbnfConcatenation)trans.getEbnf())
			.getNumOfElements(); i++) {
				SynDiaToTrans newTrans = new SynDiaToTrans(
					((EbnfConcatenation)trans.getEbnf()).getContent(i));
				ToTransFigure newTransFigure = new ToTransFigure(newTrans
				.getLabel(), gfxCanvas.getFont());
				newTrans.setGfx(newTransFigure);
				newConcatenationFigure.replace(newTransFigure, i);
				elements.add(newTrans);
			}
			
			SynDiaConcatenation newConcatenation = new SynDiaConcatenation(
				newConcatenationFigure, elements);
			// push all new SynDiaToTrans elements of the new abstract
			// concatenation onto the stack, the first will be ontop afterwards
			for (int i = newConcatenation.getNumOfElements() - 1; i >= 0; i--)
				stillToTrans.push(newConcatenation.getContent(i));

			if (parentToTrans.peek() instanceof SynDiaInitial) {
				SynDiaInitial topElem = (SynDiaInitial)parentToTrans.pop();
				topElem.setInnerElem(newConcatenation);
				topElem.getGfx().replace(newConcatenation.getGfx(), 0);
				parentToTrans.push(topElem.getInnerElem());
			}
			else if (parentToTrans.peek() instanceof SynDiaAlternative) {
				SynDiaAlternative topElem = (SynDiaAlternative)parentToTrans
				.peek();
				int index = 0;
				if (topElem.getOption(0) != trans) index = 1;
				if (index == 1
					|| (topElem.getOption(1) instanceof SynDiaEpsilon)) topElem = (SynDiaAlternative)parentToTrans
				.pop();
				topElem.setOption(index, newConcatenation);
				topElem.getGfx().replace(newConcatenation.getGfx(), index);
				parentToTrans.push(topElem.getOption(index));
			}
			else if (parentToTrans.peek() instanceof SynDiaConcatenation) {
				SynDiaConcatenation topElem = (SynDiaConcatenation)parentToTrans
				.peek();
				int index = topElem.getContent().indexOf(trans);
				if (index == topElem.getNumOfElements() - 1) topElem = (SynDiaConcatenation)parentToTrans
				.pop();
				topElem.setContent(index, newConcatenation);
				topElem.getGfx().replace(newConcatenation.getGfx(), index);
				parentToTrans.push(topElem.getContent(index));
			}
			else if (parentToTrans.peek() instanceof SynDiaRepetition) {
				SynDiaRepetition topElem = (SynDiaRepetition)parentToTrans
				.pop();
				topElem.setRepeatedElem(newConcatenation);
				topElem.getGfx().replace(newConcatenation.getGfx(), 1);
				parentToTrans.push(topElem.getRepeatedElem());
			}
			else throw new InternalErrorException(
				"no path in the Algorithm matches"); //$NON-NLS-1$
		}
		// *** EbnfAlternative ***
		else if (trans.getEbnf() instanceof EbnfAlternative) {
			// create a new abstract alternative with two new SynDiaToTrans
			// elements together with its grafical objects
			SynDiaAlternative newAlternative = new SynDiaAlternative();
			// abstract left with grafical object
			SynDiaToTrans newTransLeft = new SynDiaToTrans(
				((EbnfAlternative)trans.getEbnf()).getLeft());
			ToTransFigure newTransFigureLeft = new ToTransFigure(newTransLeft
			.getLabel(), gfxCanvas.getFont());
			newTransLeft.setGfx(newTransFigureLeft);
			newAlternative.addOption(newTransLeft);
			// abstract right with grafical object
			SynDiaToTrans newTransRight = new SynDiaToTrans(
				((EbnfAlternative)trans.getEbnf()).getRight());
			ToTransFigure newTransFigureRight = new ToTransFigure(newTransRight
			.getLabel(), gfxCanvas.getFont());
			newTransRight.setGfx(newTransFigureRight);
			newAlternative.addOption(newTransRight);
			// entire abstract alternative
			AlternativeFigure newAlternativeFigure = new AlternativeFigure(2);
			newAlternativeFigure.replace(newTransFigureLeft, 0);
			newAlternativeFigure.replace(newTransFigureRight, 1);
			// don't forget to set the grafical object
			newAlternative.setGfx(newAlternativeFigure);

			// push all new SynDiaToTrans element on the stack
			for (int i = newAlternative.getNumOfOptions() - 1; i >= 0; i--)
				stillToTrans.push(newAlternative.getOption(i));

			if (parentToTrans.peek() instanceof SynDiaInitial) {
				SynDiaInitial topElem = (SynDiaInitial)parentToTrans.pop();
				topElem.setInnerElem(newAlternative);
				topElem.getGfx().replace(newAlternative.getGfx(), 0);
				parentToTrans.push(topElem.getInnerElem());
			}
			else if (parentToTrans.peek() instanceof SynDiaAlternative) {
				SynDiaAlternative topElem = (SynDiaAlternative)parentToTrans
				.peek();
				int index = 0;
				if (topElem.getOption(0) != trans) index = 1;
				if (index == 1
					|| (topElem.getOption(1) instanceof SynDiaEpsilon)) topElem = (SynDiaAlternative)parentToTrans
				.pop();
				topElem.setOption(index, newAlternative);
				topElem.getGfx().replace(newAlternative.getGfx(), index);
				parentToTrans.push(topElem.getOption(index));
			}
			else if (parentToTrans.peek() instanceof SynDiaConcatenation) {
				SynDiaConcatenation topElem = (SynDiaConcatenation)parentToTrans
				.peek();
				int index = topElem.getContent().indexOf(trans);
				if (index == topElem.getNumOfElements() - 1) topElem = (SynDiaConcatenation)parentToTrans
				.pop();
				topElem.setContent(index, newAlternative);
				topElem.getGfx().replace(newAlternative.getGfx(), index);
				parentToTrans.push(topElem.getContent(index));
			}
			else if (parentToTrans.peek() instanceof SynDiaRepetition) {
				SynDiaRepetition topElem = (SynDiaRepetition)parentToTrans
				.pop();
				topElem.setRepeatedElem(newAlternative);
				topElem.getGfx().replace(newAlternative.getGfx(), 1);
				parentToTrans.push(topElem.getRepeatedElem());
			}
			else throw new InternalErrorException(
				"no path in the Algorithm matches"); //$NON-NLS-1$
		}
		else {
			throw new InternalErrorException("no path in the Algorithm matches"); //$NON-NLS-1$
		}

		// gfxCanvas.add(((SynDiaInitial)currentSynDia.getStartElem()).getGfx());
		if (!stillToTrans.isEmpty()) highlightTxtAndElem();

		if (!hasNextStep()) { // transformation completly finished?
			// find SynDiaVariables' corresponding SynDia
			for (SynDiaVariable synDiaVariable : alreadyCreatedVariables) {
				for (int i = 0; synDiaVariable.getStartElem() == null; i++) {
					if (i >= alreadyCreatedDias.size()) throw new InternalErrorException(
						"no path in the Algorithm matches"); //$NON-NLS-1$
					if (synDiaVariable.getLabel().equals(alreadyCreatedDias.get(i).getLabel()))
						synDiaVariable.setStartElem(alreadyCreatedDias.get(i).getStartElem());
				}
			}
			// set entiere start element
			synDiaSystem.setStartElem(alreadyCreatedDias.get(0)
			.getStartElem());
			// set the SynDiaInitial's
			List<SynDiaInitial> initialDias = new LinkedList<SynDiaInitial>();
			for (SynDiaVariable variable : alreadyCreatedDias)
				initialDias.add(variable.getStartElem());
			synDiaSystem.setInitialDiagrams(initialDias);
			List<InitialFigure> initialFigures = new LinkedList<InitialFigure>();
			for (SynDiaInitial synDiaInitial : initialDias)
				initialFigures.add(synDiaInitial.getGfx());
			synDiaSystem.setGfx(new SynDiaSystemFigure(initialFigures));

			showDialogFinished();
			moduleController.algoFinished();
		}
	}

	/**
	 * Writes all necessary objects to a stream for serialization. When
	 * chanching here also change readObject() !
	 */
	private synchronized void writeObject(ObjectOutputStream s)
	throws IOException {
		s.writeObject(gfxCanvas);
		s.writeObject(synDiaSystem);
		s.writeBoolean(auto);
		s.writeObject(stillToTrans);
		s.writeObject(toTransVariables);
		s.writeObject(packedTxtCanvas());
		// as long as 'label' of StackCanvas is unused this should be enough
		s.writeObject(toTransCanvas.getStack());
	}

	/**
	 * Reads ealier serialized objects from stream and restores class. When
	 * chanching here also change writeObject() !
	 */
	@SuppressWarnings("unchecked")
	private synchronized void readObject(ObjectInputStream s)
	throws IOException, ClassNotFoundException {
		try {
			gfxCanvas = (Figure)s.readObject();
			synDiaSystem = (SynDiaSystem)s.readObject();
			auto = s.readBoolean();
			stillToTrans = (Stack<SynDiaElement>)s.readObject();
			toTransVariables = (Stack<EbnfSynVariable>)s.readObject();
			unpackTxtCanvas((Object[])s.readObject());
			toTransCanvas.setStack((Stack<String>)s.readObject());
			// moduleController will be set from moduleController when
			// deserializing
		}
		catch (ClassCastException cce) {
			throw new ClassNotFoundException();
		}
	}

	private Object[] packedTxtCanvas() {
		Object ptc[] = new Object[3];
		ptc[0] = new Integer(txtCanvas.getPosition());
		ptc[1] = txtCanvas.getText();
		ptc[2] = new Integer(txtCanvas.getMode());
		return ptc;
	}

	private void unpackTxtCanvas(Object ptc[]) {
		txtCanvas.setPosition(((Integer)ptc[0]).intValue());
		txtCanvas.setTextSegments((String[])ptc[1]);
		txtCanvas.setMode(((Integer)ptc[2]).intValue());
	}
}
