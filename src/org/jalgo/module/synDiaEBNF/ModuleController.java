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
 * Created on 20.04.2004
 */

package org.jalgo.module.synDiaEBNF;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.draw2d.Figure;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.main.InternalErrorException;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.main.gui.actions.SaveAction;
import org.jalgo.main.gui.actions.SaveAsAction;
import org.jalgo.module.synDiaEBNF.ebnf.EbnfDefinition;
import org.jalgo.module.synDiaEBNF.gui.BackTrackingAlgoGui;
import org.jalgo.module.synDiaEBNF.gui.CreateSynDiaClickGui;
import org.jalgo.module.synDiaEBNF.gui.EbnfInputGui;
import org.jalgo.module.synDiaEBNF.gui.Gui;
import org.jalgo.module.synDiaEBNF.gui.NormalViewEbnfGui;
import org.jalgo.module.synDiaEBNF.gui.NormalViewGui;
import org.jalgo.module.synDiaEBNF.gui.NormalViewSynDiaGui;
import org.jalgo.module.synDiaEBNF.gui.TransAlgorithmGui;
import org.jalgo.module.synDiaEBNF.gui.actions.AbortAlgoAction;
import org.jalgo.module.synDiaEBNF.gui.actions.CreateEbnfAction;
import org.jalgo.module.synDiaEBNF.gui.actions.CreateSynDiaAction;
import org.jalgo.module.synDiaEBNF.gui.actions.FirstAction;
import org.jalgo.module.synDiaEBNF.gui.actions.GenerateWordAction;
import org.jalgo.module.synDiaEBNF.gui.actions.LastAction;
import org.jalgo.module.synDiaEBNF.gui.actions.LeftAction;
import org.jalgo.module.synDiaEBNF.gui.actions.PerformAllAction;
import org.jalgo.module.synDiaEBNF.gui.actions.PerformNextAction;
import org.jalgo.module.synDiaEBNF.gui.actions.RecognizeWordAction;
import org.jalgo.module.synDiaEBNF.gui.actions.RightAction;
import org.jalgo.module.synDiaEBNF.gui.actions.TransAction;
import org.jalgo.module.synDiaEBNF.gui.actions.WizardAction;
import org.jalgo.module.synDiaEBNF.startWizard.StartWizard;
import org.jalgo.module.synDiaEBNF.startWizard.StartWizardDialog;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaSystem;

/**
 * The module controller is the central controlling unit of this module.
 *  It is responsible for initiating and disposing GUIs, starting and controlling 
 * algorithms and some other tasks. (Almost) everything concerning this module is initiated in this class.
 * 
 * The most important method is <code>setMode()</code>.
 * 
 * @author Michael Pradel
 */

public class ModuleController {

	final int NO_MODE_SET = 0;
	final int START_WITH_WIZARD = 1;
	final int NORMAL_VIEW_EMPTY = 2;
	final int CREATE_SYNDIA = 3;
	final int EBNF_INPUT = 4;
	final int TRANS_ALGO = 5;
	final int RECOGNIZE_WORD_ALGO = 6;
	final int GENERATE_WORD_ALGO = 7;
	final int NORMAL_VIEW_EBNF = 8;
	final int NORMAL_VIEW_SYNDIA = 9;

	private ModuleInfo moduleInfo;
	private ApplicationWindow appWin;
	private SubMenuManager menuManager;
	private SubToolBarManager toolBarManager;
	private SubStatusLineManager statusLineManager;
	private Composite comp;
	private Gui gui;
	private Figure synDias;

	private StartWizard startWizard;
	private StartWizardDialog startWizardDialog;

	// buttons created in main program
	private SaveAction saveAction;
	private SaveAsAction saveAsAction;

	// buttons to start algorithms or creation of ebnf/synDia
	private TransAction transAction;
	private GenerateWordAction generateWordAction;
	private RecognizeWordAction recognizeWordAction;
	private CreateSynDiaAction createSynDiaAction;
	private CreateEbnfAction createEbnfAction;

	// button to start wizard
	private WizardAction wizardAction;

	// buttons to navigate while perfoming an algorithm
	private FirstAction firstAction;
	private LeftAction leftAction;
	private RightAction rightAction;
	private LastAction lastAction;
	private PerformNextAction performNextAction;
	private PerformAllAction performAllAction;
	private AbortAlgoAction abortAlgoAction;

	private org.jalgo.module.synDiaEBNF.synDia.SynDiaSystem synDiaSystem;
	private EbnfDefinition ebnfDef;

	private IAlgorithm algo = null;
	// currently performed algorithm, or null, if no algorithm is performed right now

	private int mode = 0;
	// indicated what the module is doing at this moment (see modes-table in setMode() comment)

	// ------ public methods ----------------

	/**
	 * @param appWin the <code>ApplicationWindow</code> jAlgo is running in.
	 * @param comp the <code>Composite</code>, that should be filled by the module
	 * @param menuManager the modules <code>MenuManager</code>
	 * @param toolBarManager the modules <code>ToolBarManager</code>
	 * @param statusLineManager the modules <code>StatusLineManager</code>
	 */
	public ModuleController(
		ModuleInfo moduleInfo,
		ApplicationWindow appWin,
		Composite comp,
		SubMenuManager menuManager,
		SubToolBarManager toolBarManager,
		SubStatusLineManager statusLineManager) {

		this.moduleInfo = moduleInfo;
		this.appWin = appWin;
		this.comp = comp;

		// Create Menu
		this.menuManager = menuManager;

		//	get "save" and "save as" actions from toolbar
		saveAction = ((JalgoWindow) appWin).getSaveAction();
		saveAsAction = ((JalgoWindow) appWin).getSaveAsAction();

		// Create ToolBar
		this.toolBarManager = toolBarManager;
		createActions();
		createMenu();
		addWizardButton();
		addNavButtons();

		// Create StatusLine
		this.statusLineManager = statusLineManager;
		createStatusLine();
	}

	public void run() {
		//TODO: save in config-file, if user want's wizard	
		setMode(START_WITH_WIZARD);
	}

	/**
	 * Set what the module should do, so the GUI is updated completely and algorithms are initiated if necessary.
	 * @param mode 
	 *   NO_MODE_SET - no mode set 
	 *   START_WITH_WIZARD - start with wizard
	 *   NORMAL_VIEW_EMPTY - normal view (nothing is displayed, user can choose what to do next)
	 *   NORMAL_VIEW_EBNF - normal view (ebnf is displayed, user can start trans() )
	 *   NORMAL_VIEW_SYNDIA - normal view (syndia is displayed, user can start generateWord() or recognizeWord() )
	 *   CREATE_SYNDIA - user creates syntactical diagram  
	 *   EBNF_INPUT - input of ebnf by user
	 *   TRANS_ALGO - ebnf definition is loaded; user performs trans-algorithm
	 *   RECOGNIZE_WORD_ALGO - syndia is loaded; user performs recognize-word-algorithm
	 *   GENERATE_WORD_ALGO -  syndia is loaded; user performs generate-word-algorithm
	 */
	public void setMode(int newMode) {
		// leave old mode (delete buttons etc) ...
		leaveMode(mode);

		// dispose old gui
		if (gui != null) {
			gui.dispose();
		}

		// ... and set new mode
		switch (newMode) {
			case NO_MODE_SET :
				mode = NO_MODE_SET;
				break;

			case START_WITH_WIZARD :
				mode = START_WITH_WIZARD;
				startWizard = new StartWizard(this, appWin);
				startWizardDialog =
					new StartWizardDialog(comp.getShell(), startWizard);
				startWizardDialog.open();
				break;

			case NORMAL_VIEW_EMPTY :
				mode = NORMAL_VIEW_EMPTY;
				addCreationButtons();
				gui = new NormalViewGui(comp);
				break;

			case NORMAL_VIEW_EBNF :
				mode = NORMAL_VIEW_EBNF;
				enableSaveButtons();
				addTransButton();
				gui = new NormalViewEbnfGui(comp);
				StyledText ebnfText =
					ebnfDef.styledText(((NormalViewEbnfGui) gui).getViewForm());
				((NormalViewEbnfGui) gui).setEbnfText(ebnfText);
				break;

			case NORMAL_VIEW_SYNDIA :
				mode = NORMAL_VIEW_SYNDIA;
				addBacktrackingButtons();
				gui = new NormalViewSynDiaGui(comp);
				StyledText tuple =
					synDiaSystem.getTuple(
						((NormalViewSynDiaGui) gui).getTupleForm());
				((NormalViewSynDiaGui) gui).setTuple(tuple);
				((NormalViewSynDiaGui) gui).setSynDia(synDias);
				break;

			case CREATE_SYNDIA :
				mode = CREATE_SYNDIA;
				gui = new CreateSynDiaClickGui(comp, this);
				break;

			case EBNF_INPUT :
				mode = EBNF_INPUT;
				gui = new EbnfInputGui(comp, this);
				break;

			case TRANS_ALGO :
				mode = TRANS_ALGO;
				gui = new TransAlgorithmGui(comp);
				// need to cast for having getFigure() and getText()
				algo =
					new TransAlgorithm(
						this,
						((Figure) ((TransAlgorithmGui) gui).getFigure()),
						((TransAlgorithmGui) gui).getText(),
						((TransAlgorithmGui) gui).getStackCanvas(),
						ebnfDef,
						true);
				/* leave last value out, if implemented, that user can choose,
				 *  where to go on indeterministic decissions; 
				 * now: auto-mode is always on */

				enableNavButtons();
				testAndSetNavButtons();
				addPerformAllButton();
				addAbortAlgoButton();
				break;

			case RECOGNIZE_WORD_ALGO :
				mode = RECOGNIZE_WORD_ALGO;
				gui = new BackTrackingAlgoGui(comp);
				((BackTrackingAlgoGui) gui).setFigure(synDias);
				algo =
					new RecognizeWord(
						this,
						((BackTrackingAlgoGui) gui).getFigure(),
						((BackTrackingAlgoGui) gui).getStackCanvas(),
						((BackTrackingAlgoGui) gui).getAlgoText(),
						((BackTrackingAlgoGui) gui).getWord(),
						synDiaSystem);
				enableNavButtons();
				testAndSetNavButtons();
				addAbortAlgoButton();
				break;

			case GENERATE_WORD_ALGO :
				mode = GENERATE_WORD_ALGO;
				gui = new BackTrackingAlgoGui(comp);
				((BackTrackingAlgoGui) gui).setFigure(synDias);
				algo =
					new GenerateWord(
						this,
						((BackTrackingAlgoGui) gui).getFigure(),
						((BackTrackingAlgoGui) gui)
							.getStackCanvas(),
						((BackTrackingAlgoGui) gui).getAlgoText(),
						((BackTrackingAlgoGui) gui).getWord(),
						synDiaSystem);
				enableNavButtons();
				testAndSetNavButtons();
				addAbortAlgoButton();
				break;

			default :
				}
		comp.layout();
	}

	/**
	 * Returns the current mode of the module.
	 * @return the current mode
	 * @see setMode(int)
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * Get the serialized data to store it in a file.
	 * @return the serialiazed data
	 */
	public ByteArrayOutputStream getSerializedData() {
		return serialize();
	}

	/**
	 * Set data. that was loaded from file. This data will be deserialized to restore the saved state.
	 * @param data earlier serialized data
	 */
	public void setSerializedData(ByteArrayInputStream data) {
		deserialize(data);
	}

	/**
	 * If an algorithm is running currently, the next step is performed.
	 * @throws InternalErrorException Will be thrown if no algorithm is running.
	 */
	public void performNextStep() throws InternalErrorException {
		// we are not performing any algorithm; should never be reached
		if (mode < 5 || mode > 7) {
			throw new InternalErrorException("trying to perfom next step in ModuleController, when no algorithm is running; buttons should be disabled!"); //$NON-NLS-1$
		}

		try {
			algo.performNextStep();
		} catch (IndexOutOfBoundsException ioobE) {
			java.lang.System.err.println(ioobE);
		}
		testAndSetNavButtons();
	}

	/**
	 * If trans-algorithm is running currently, all steps are performed.
	 * @throws InternalErrorException Will be thrown if trans-algorithm is not running.
	 */
	public void performAll() throws InternalErrorException {
		if (mode != TRANS_ALGO)
			throw new InternalErrorException("EBNF_SynDia; ModuleController: performAll() was called, when trans-algorithm was not running"); //$NON-NLS-1$

		 ((TransAlgorithm) algo).autoTransAll();
		testAndSetNavButtons();
	}

	/**
	 * If an algorithm is running currently, the previous step in history is reloaded.
	 * @throws InternalErrorException Will be thrown if no algorithm is running.
	 */
	public void previousHistStep() throws InternalErrorException {
		// we are not performing any algorithm; should never be reached
		if (mode < 5 || mode > 7) {
			throw new InternalErrorException("trying to go to previous hist step in ModuleController, when no algorithm is running; buttons should be disabled!"); //$NON-NLS-1$
		}

		try {
			algo.previousHistStep();
		} catch (IndexOutOfBoundsException e) {
			java.lang.System.err.println(e);
		}
		testAndSetNavButtons();
	}

	/**
	 * If an algorithm is running, goes back in history as far as possible - usually to the beginning of the algorithm.
	 * @throws InternalErrorException Will be thrown if no algorithm is running.
	 */
	public void goToFirstStep() throws InternalErrorException {
		//		we are not performing any algorithm; should never be reached
		if (mode < 5 || mode > 7) {
			throw new InternalErrorException("trying to go to first history step in ModuleController, when no algorithm is running; buttons should be disabled!"); //$NON-NLS-1$
		}

		disableNavButtons();
		while (algo.hasPreviousHistStep()) {
			algo.previousHistStep();
		}
		testAndSetNavButtons();
	}

	/**
	 * If an algorithm is running, goes forward in history as far as possible - usually to the end of the steps, the user has performed up to now.
	 * @throws InternalErrorException Will be thrown if no algorithm is running.
	 */
	public void goToLastStep() throws InternalErrorException {
		//		we are not performing any algorithm; should never be reached
		if (mode < 5 || mode > 7) {
			throw new InternalErrorException("trying to go to last history step in ModuleController, when no algorithm is running; buttons should be disabled!"); //$NON-NLS-1$
		}

		disableNavButtons();
		while (algo.hasNextHistStep()) {
			algo.nextHistStep();
		}
		testAndSetNavButtons();
	}

	/**
	 * Set automatic execution of the trans algorithm, that means the user will not be asked what to transform next.
	 * @param auto automatic execution of trans algorithm
	 */
	public void setAuto(boolean auto) {
		//		we are not performing any algorithm; should never be reached
		if (mode != 5) {
			throw new InternalErrorException("trying to set automatically algorithm performance, when trans-algorithm isn't running; checkbox should be invisible!"); //$NON-NLS-1$
		}

		TransAlgorithm transAlgo = (TransAlgorithm) algo;
		transAlgo.setAuto(auto);
	}

	/**
	 * Starts the wizard.
	 *
	 */
	public void startWizard() {
		setMode(START_WITH_WIZARD);
	}

	/**
	 * Set the abstract EBNF definition. This method should be used after the input of a new EBNF.
	 * @param ebnfDef the new EBNF
	 */
	public void setEbnfDef(EbnfDefinition ebnfDef) {
		this.ebnfDef = ebnfDef;
	}

	/**
	 * Get the menu-manager of this module instance. It includes all module specific menu items.
	 * @return the menu-manager
	 */
	public SubMenuManager getMenuManager() {
		return menuManager;
	}

	/**
	* Get the subtoolbar-manager of this module instance. It includes all module specific buttons.
	* @return the subtoolbar-manager
	*/
	public SubToolBarManager getToolBarManager() {
		return toolBarManager;
	}

	/**
	* Get the statusline-manager of this module instance. Right now, the statusbar is unused.
	* @return the statusline-manager
	*/
	public SubStatusLineManager getStatusLineManager() {
		return statusLineManager;
	}

	/**
	 * A running algorithm can call this method, when it has finished and wishes to be disposed.
	 *
	 */
	public void algoFinished() {
		if (algo instanceof RecognizeWord || algo instanceof GenerateWord) {
			setMode(NORMAL_VIEW_SYNDIA);
		} else if (algo instanceof TransAlgorithm) {
			synDiaSystem = ((TransAlgorithm) algo).getSynDiaSystem();
			synDias = ((TransAlgorithm) algo).getSynDiaFigure();
			setMode(NORMAL_VIEW_SYNDIA);
		} else {
			throw new InternalErrorException("EBNFSynDia-ModuleController: someone calls algoFinished(), but no known algorithm is running"); //$NON-NLS-1$
		}
	}

	/**
	 * This method is called, when the user has finished the creation of syntactical diagrams with the mouse.
	 *
	 */
	public void createSynDiaFinished() {
		synDias = ((CreateSynDiaClickGui) gui).getSynDiaPanel();
		TransformSynDia tsd = new TransformSynDia(synDias);
		synDiaSystem = tsd.getSynDiaSystem();
		setMode(NORMAL_VIEW_SYNDIA);
	}

	/**
	 * Aborts the currently running algorithm, and switches back to 
	 * normal view of the syntactical diagram system or EBNF.
	 *
	 */
	public void abortAlgo() {
		if (algo instanceof TransAlgorithm) {
			setMode(NORMAL_VIEW_EBNF);
		} else if (algo instanceof RecognizeWord) {
			((RecognizeWord) algo).finalTasks();
			setMode(NORMAL_VIEW_SYNDIA);
		} else if (algo instanceof GenerateWord) {
			((GenerateWord) algo).finalTasks();
			setMode(NORMAL_VIEW_SYNDIA);
		} else {
			throw new InternalErrorException("SynDiaEBNF, ModuleController: someone calls abortAlgo() while no algorithm is running"); //$NON-NLS-1$
		}
	}

	//------- private methods --------------

	private void leaveMode(int oldMode) {
		switch (oldMode) {
			case NORMAL_VIEW_EMPTY :
				removeCreationButtons();
				break;
			
			case NORMAL_VIEW_EBNF :
				removeTransButton();
				disableSaveButtons();
				break;

			case NORMAL_VIEW_SYNDIA :
				removeBacktrackingButtons();
				break;

			case TRANS_ALGO :
				disableNavButtons();
				removePerformAllButton();
				removeAbortAlgoButton();
				break;

			case RECOGNIZE_WORD_ALGO :
				disableNavButtons();
				removeAbortAlgoButton();
				break;

			case GENERATE_WORD_ALGO :
				disableNavButtons();
				removeAbortAlgoButton();
				break;

			default :

				}
	}

	private void createMenu() {
		MenuManager menu = new MenuManager("&" + this.moduleInfo.getName()); //$NON-NLS-1$
		// start wizard
		menu.add(wizardAction);
		menu.add(new Separator());
		// create synDia or EBNF
		menu.add(createEbnfAction);
		menu.add(createSynDiaAction);
		menu.add(new Separator());
		// starting and aborting algorithms
		menu.add(transAction);
		menu.add(generateWordAction);
		menu.add(recognizeWordAction);

		menuManager.insertBefore("help", menu); //$NON-NLS-1$
	}

	private void addCreationButtons() {
		createSynDiaAction.setId("createSynDia"); //$NON-NLS-1$
		toolBarManager.add(createSynDiaAction);
		createSynDiaAction.setEnabled(true);
		createEbnfAction.setId("createEbnf"); //$NON-NLS-1$
		toolBarManager.add(createEbnfAction);
		createEbnfAction.setEnabled(true);
		toolBarManager.add(new Separator());
	}

	private void removeCreationButtons() {
		createSynDiaAction.setEnabled(false);
		createEbnfAction.setEnabled(false);
		toolBarManager.remove(createSynDiaAction.getId());
		toolBarManager.remove(createEbnfAction.getId());
		toolBarManager.update(false);
		menuManager.update(false);
	}

	private void addTransButton() {
		transAction.setId("transAlgo"); //$NON-NLS-1$
		toolBarManager.add(transAction);
		transAction.setEnabled(true);
		toolBarManager.update(false);
		menuManager.update(false);
	}

	private void removeTransButton() {
		transAction.setEnabled(false);
		toolBarManager.remove(transAction.getId());
		toolBarManager.update(false);
		menuManager.update(false);
	}

	private void addPerformAllButton() {
		toolBarManager.add(performAllAction);
		performAllAction.setEnabled(true);
		toolBarManager.update(false);
	}

	private void removePerformAllButton() {
		toolBarManager.remove(performAllAction.getId());
		performAllAction.setEnabled(false);
		toolBarManager.update(false);
	}

	private void addBacktrackingButtons() {
		generateWordAction.setId("generateWord"); //$NON-NLS-1$
		toolBarManager.add(generateWordAction);
		generateWordAction.setEnabled(true);
		recognizeWordAction.setId("recognizeWord"); //$NON-NLS-1$
		toolBarManager.add(recognizeWordAction);
		recognizeWordAction.setEnabled(true);
		toolBarManager.update(false);
		menuManager.update(false);
	}

	private void removeBacktrackingButtons() {
		generateWordAction.setEnabled(false);
		recognizeWordAction.setEnabled(false);
		toolBarManager.remove(generateWordAction.getId());
		toolBarManager.remove(recognizeWordAction.getId());
		toolBarManager.update(false);
		menuManager.update(false);
	}

	private void addAbortAlgoButton() {
		abortAlgoAction.setId("abortAlgo"); //$NON-NLS-1$
		toolBarManager.add(abortAlgoAction);
		abortAlgoAction.setEnabled(true);
		toolBarManager.update(false);
	}

	private void removeAbortAlgoButton() {
		toolBarManager.remove(abortAlgoAction.getId());
		abortAlgoAction.setEnabled(false);
		toolBarManager.update(false);
	}

	private void addWizardButton() {
		toolBarManager.add(wizardAction);
		toolBarManager.update(false);
		menuManager.update(false);
	}

	private void addNavButtons() {
		toolBarManager.add(firstAction);
		toolBarManager.add(leftAction);
		toolBarManager.add(rightAction);
		toolBarManager.add(lastAction);
		toolBarManager.add(new Separator());
		toolBarManager.add(performNextAction);
		toolBarManager.add(new Separator());
	}

	private void enableSaveButtons() {
		saveAction.setEnabled(true);
		saveAsAction.setEnabled(true);
		menuManager.update(false);
	}

	private void disableSaveButtons() {
		saveAction.setEnabled(false);
		saveAsAction.setEnabled(false);
	}

	private void disableNavButtons() {
		firstAction.setEnabled(false);
		leftAction.setEnabled(false);
		rightAction.setEnabled(false);
		lastAction.setEnabled(false);
		performNextAction.setEnabled(false);
	}

	private void enableNavButtons() {
		firstAction.setEnabled(true);
		leftAction.setEnabled(true);
		rightAction.setEnabled(true);
		lastAction.setEnabled(true);
		performNextAction.setEnabled(true);
	}

	/*
	 * only to use while algorihtm is performed!
	 * tests for each navigation button using has***() and enables or disables it 
	 */
	private void testAndSetNavButtons() {
		performNextAction.setEnabled(algo.hasNextStep());

		//		history not implemented for Trans-Algo so far ...  START
		if (algo instanceof TransAlgorithm) {
			rightAction.setEnabled(false);
			leftAction.setEnabled(false);
			return;
		}
		//		history not implemented for Trans-Algo so far ...  END

		rightAction.setEnabled(algo.hasNextHistStep());
		lastAction.setEnabled(algo.hasNextHistStep());

		leftAction.setEnabled(algo.hasPreviousHistStep());
		firstAction.setEnabled(algo.hasPreviousHistStep());
	}

	private void createStatusLine() {
	}

	private void createActions() {
		abortAlgoAction = new AbortAlgoAction(this);
		abortAlgoAction.setEnabled(false);
		createEbnfAction = new CreateEbnfAction(this, comp);
		createEbnfAction.setEnabled(false);
		createSynDiaAction = new CreateSynDiaAction(this, comp);
		createSynDiaAction.setEnabled(false);
		firstAction = new FirstAction(this);
		firstAction.setEnabled(false);
		generateWordAction = new GenerateWordAction(this);
		generateWordAction.setEnabled(false);
		lastAction = new LastAction(this);
		lastAction.setEnabled(false);
		leftAction = new LeftAction(this);
		leftAction.setEnabled(false);
		performAllAction = new PerformAllAction(this);
		performAllAction.setEnabled(false);
		performNextAction = new PerformNextAction(this);
		performNextAction.setEnabled(false);
		recognizeWordAction = new RecognizeWordAction(this);
		recognizeWordAction.setEnabled(false);
		rightAction = new RightAction(this);
		rightAction.setEnabled(false);
		transAction = new TransAction(this);
		transAction.setEnabled(false);
		wizardAction = new WizardAction(this, comp);
		wizardAction.setEnabled(true);

		saveAction.setEnabled(false);
		saveAsAction.setEnabled(false);
	}

	private ByteArrayOutputStream serialize() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream serializedObjects =
				new ObjectOutputStream(outputStream);

			serializedObjects.writeInt(mode);

			switch (mode) {
				case NORMAL_VIEW_EBNF :
					serializedObjects.writeObject(ebnfDef);
					break;

				case NORMAL_VIEW_SYNDIA :
					serializedObjects.writeObject(synDiaSystem);
					serializedObjects.writeObject(synDias);
					break;

				default :
					throw new InternalErrorException("EBNFSynDia-ModuleController: serialize() called, when save-buttons should be disabled"); //$NON-NLS-1$
			}

		} catch (IOException IOExc) {
			java.lang.System.err.println(IOExc);
			throw new InternalErrorException(IOExc.getMessage());
		}

		return outputStream;
	}

	private void deserialize(ByteArrayInputStream data) {
		try {
			ObjectInputStream serializedObjects = new ObjectInputStream(data);

			int newMode = serializedObjects.readInt();

			switch (newMode) {
				case NORMAL_VIEW_EBNF :
					ebnfDef = (EbnfDefinition) serializedObjects.readObject();
					setMode(NORMAL_VIEW_EBNF);
					break;

				case NORMAL_VIEW_SYNDIA :
					synDiaSystem =
						(SynDiaSystem) serializedObjects.readObject();
					synDias = (Figure) serializedObjects.readObject();
					setMode(NORMAL_VIEW_SYNDIA);
					break;

				default :
					throw new InternalErrorException("EBNFSynDia-ModuleController: surprisingly finding mode while deserializing, which should not be serialized"); //$NON-NLS-1$
			}

		} catch (IOException IOExc) {
			java.lang.System.err.println(IOExc);
			throw new InternalErrorException(IOExc.getMessage());

		} catch (ClassNotFoundException cnfExc) {
			java.lang.System.err.println("Error while loading, please see error.log for more details."); //$NON-NLS-1$
			throw new InternalErrorException(cnfExc.getMessage());
		}

	}

}
