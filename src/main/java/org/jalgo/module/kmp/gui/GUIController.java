package org.jalgo.module.kmp.gui;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import java.util.List;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.module.kmp.gui.component.WelcomeScreen;
import org.jalgo.module.kmp.gui.component.PhaseOneScreen;
import org.jalgo.module.kmp.gui.component.PhaseTwoScreen;
import org.jalgo.module.kmp.Constants;
import org.jalgo.module.kmp.Controller;
import org.jalgo.module.kmp.ModuleConnector;
import org.jalgo.module.kmp.algorithm.ExampleFileFilter;
import org.jalgo.module.kmp.algorithm.Step;
import org.jalgo.module.kmp.gui.event.AbortAction;
import org.jalgo.module.kmp.gui.event.BeamerModeAction;
import org.jalgo.module.kmp.gui.event.FastBackwardAction;
import org.jalgo.module.kmp.gui.event.BackwardAction;
import org.jalgo.module.kmp.gui.event.ForwardAction;
import org.jalgo.module.kmp.gui.event.FastForwardAction;
import org.jalgo.module.kmp.gui.event.FinishAction;
import org.jalgo.module.kmp.gui.event.WelcomeAction;
import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.main.gui.event.StatusLineUpdater;
import org.jalgo.module.kmp.algorithm.phasetwo.P2Step;
import org.jalgo.module.kmp.algorithm.phaseone.P1BeginForStep;
import org.jalgo.module.kmp.algorithm.phasetwo.P2EndStep;
import org.jalgo.module.kmp.algorithm.phaseone.P1InitStep;
import org.jalgo.module.kmp.algorithm.phasetwo.P2InitStep;

/**
 * The class <code>GUIController</code> is the main class of the GUI of the
 * KMP module. It initializes the layout of the different parts, offers methods
 * to set the state of buttons etc., to show several dialogs and messages, and
 * causes GUI-components to update when necessary.
 * 
 * @author Danilo Lisske
 */
public class GUIController implements GUIConstants {
	private ModuleConnector modconn;
	private Controller controller;
	
	private WelcomeScreen welcomeScreen;
	private PhaseOneScreen phaseonescreen;
	private PhaseTwoScreen phasetwoscreen;
	
	private AbstractAction abortAction;
	private JToolbarButton btabort;
	private AbstractAction fastBackwardAction;
	private JToolbarButton btfastback;
	private AbstractAction backwardAction;
	private JToolbarButton btback;
	private AbstractAction forwardAction;
	private JToolbarButton btforward;
	private AbstractAction fastForwardAction;
	private JToolbarButton btfastforward;
	private AbstractAction finishAction;
	private JToolbarButton btfinish;
	private WelcomeAction welcomeAction;
	
	private JPanel contentPane;
	private boolean beamermode;
	
	/**
	 * Constructs the <code>GUIController</code> instance for the current KMP
	 * module instance.<br>
	 * Initializes all layout components, especially the toolbar and the menu.
	 * Installs an AWT-Frame on the panel provided by the main program, so that
	 * GUI of the KMP module can be created on Swing base.
	 * 
	 * @param m the <code>ModuleConnector</code> of the KMP module
	 * @param c the <code>Controller</code> of the KMP module
	 */
	public GUIController(ModuleConnector m, Controller c) {
		controller = c;
		modconn = m;
		beamermode = false;
		
		JComponent rootPane = JAlgoGUIConnector.getInstance().getModuleComponent(modconn);
		rootPane.setLayout(new BorderLayout());
		contentPane = new JPanel();
		rootPane.add(contentPane, BorderLayout.CENTER);
		
		installToolbar();
		installMenu();
		
		welcomeScreen = new WelcomeScreen(this);
		phaseonescreen = new PhaseOneScreen(this);
		phasetwoscreen = new PhaseTwoScreen(this);
	}
	
	/**
	 * Sets up the toolbar.
	 * 
	 */
	private void installToolbar() {
		JToolBar toolBar = JAlgoGUIConnector.getInstance().getModuleToolbar(modconn);
		welcomeAction = new WelcomeAction(this);
		JToolbarButton btwelcome = new JToolbarButton(
			(Icon)welcomeAction.getValue(Action.SMALL_ICON),null,null);
		btwelcome.setAction(welcomeAction);
		btwelcome.setText("");
		btwelcome.addMouseListener(StatusLineUpdater.getInstance());
		toolBar.add(btwelcome);
		toolBar.addSeparator();
		abortAction = new AbortAction(this);
		btabort = new JToolbarButton(
			(Icon)abortAction.getValue(Action.SMALL_ICON),null, null);
		btabort.setAction(abortAction);
		btabort.setText("");
		btabort.addMouseListener(StatusLineUpdater.getInstance());
		toolBar.add(btabort);
		fastBackwardAction = new FastBackwardAction(this);
		btfastback = new JToolbarButton(
			(Icon)fastBackwardAction.getValue(Action.SMALL_ICON),null, null);
		btfastback.setAction(fastBackwardAction);
		btfastback.setText("");
		btfastback.addMouseListener(StatusLineUpdater.getInstance());
		toolBar.add(btfastback);
		backwardAction = new BackwardAction(this);
		btback = new JToolbarButton(
			(Icon)backwardAction.getValue(Action.SMALL_ICON),null, null);
		btback.setAction(backwardAction);
		btback.setText("");
		btback.addMouseListener(StatusLineUpdater.getInstance());
		toolBar.add(btback);
		forwardAction = new ForwardAction(this);
		btforward = new JToolbarButton(
			(Icon)forwardAction.getValue(Action.SMALL_ICON),null, null);
		btforward.setAction(forwardAction);
		btforward.setText("");
		btforward.addMouseListener(StatusLineUpdater.getInstance());
		toolBar.add(btforward);
		fastForwardAction = new FastForwardAction(this);
		btfastforward = new JToolbarButton(
			(Icon)fastForwardAction.getValue(Action.SMALL_ICON),null, null);
		btfastforward.setAction(fastForwardAction);
		btfastforward.setText("");
		btfastforward.addMouseListener(StatusLineUpdater.getInstance());
		toolBar.add(btfastforward);
		finishAction = new FinishAction(this);
		btfinish = new JToolbarButton(
			(Icon)finishAction.getValue(Action.SMALL_ICON),null, null);
		btfinish.setAction(finishAction);
		btfinish.setText("");
		btfinish.addMouseListener(StatusLineUpdater.getInstance());
		toolBar.add(btfinish);
	}
	
	/**
	 * Sets up the menu.
	 * 
	 */
	private void installMenu() {
		JMenu menu = JAlgoGUIConnector.getInstance().getModuleMenu(modconn);
		menu.add(welcomeAction);
		menu.addSeparator();
		menu.add(abortAction);
		menu.add(backwardAction);
		menu.add(fastBackwardAction);
		menu.add(forwardAction);
		menu.add(fastForwardAction);
		menu.add(finishAction);
		menu.addSeparator();
		BeamerModeAction beamerAction = BeamerModeAction.getInstance(this);
		menu.add(beamerAction);
	}
	
	/**
	 * Installs the welcomescreen to the main panel.
	 * 
	 */
	public void installWelcomeScreen() {
		contentPane.removeAll();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(welcomeScreen, BorderLayout.CENTER);
		modconn.setSaveStatus(SaveStatus.NOTHING_TO_SAVE);
		setAllControlButton(false);
		controller.clearData();
		contentPane.updateUI();
		contentPane.validate();
	}
	
	/**
	 * Installs the phaseonescreen to the main panel.
	 *
	 */
	public void installPhaseOneScreen() {
		controller.startPhaseOne();
		contentPane.removeAll();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(phaseonescreen, BorderLayout.CENTER);
		modconn.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
		
		setAllControlButton(false);
		phaseonescreen.infotabbedPane.setCode(Constants.CODE_PHASE_ONE);
		phaseonescreen.infotabbedPane.setDescription("",false);
		phaseonescreen.phaseoneinputPane.setPatternEnabled(false);
		phaseonescreen.phaseoneinputPane.setAddPatternEnabled(false);
		phaseonescreen.phaseoneinputPane.setBtGoOnVisible(false);
		if(controller.isPatternSet()) {
			phaseonescreen.phaseoneinputPane.setTfPatternText(controller.getPattern().getPattern());
			initPhaseOne();
		}
		else {
			phaseonescreen.phaseoneinputPane.setTfPatternText("");
			phaseonescreen.phaseoneinputPane.setTfAddPatternText("");
			phaseonescreen.phaseoneshowPane.setPattern(null);
		}
		
		contentPane.updateUI();
		contentPane.validate();
	}
	
	/**
	 * The method is called to initialise the phase one.
	 *
	 */
	public void initPhaseOne() {
		phaseonescreen.phaseoneinputPane.setTfAddPatternText("");
		phaseonescreen.phaseoneinputPane.setPatternEnabled(false);
		phaseonescreen.phaseoneinputPane.setAddPatternEnabled(false);
		phaseonescreen.phaseoneshowPane.setPattern(controller.getPattern());
		phaseonescreen.phaseoneshowPane.update((Step)null);
		phaseonescreen.infotabbedPane.setDescription("",false);
		setForwardControlButton(true);
		controller.initPhaseOne();
		
		phaseonescreen.updateUI();
	}
	
	/**
	 * The method is called when loading a session or expanding the pattern in phase one.
	 *
	 */
	public void restorePhaseOne() {
		if (controller.isPatternSet()) {
			controller.getPattern().setPattern(phaseonescreen.phaseoneinputPane.getTfPatternText());
			controller.restorePhaseOne();
			phaseonescreen.phaseoneshowPane.setPattern(controller.getPattern());
			phaseonescreen.infotabbedPane.setDescription("",false);
			doNextStep();
			phaseonescreen.updateUI();
		}
	}
	
	/**
	 * Install the phasetwoscreen to the main panel.
	 *
	 */
	public void installPhaseTwoScreen() {
		if (controller.getPhase() == 1)
			phasetwoscreen.setScaleFactor(phaseonescreen.getScaleFactor());
		controller.startPhaseTwo();
		if (beamermode) doBeamerMode(true);
		contentPane.removeAll();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(phasetwoscreen, BorderLayout.CENTER);
		modconn.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
		
		setAllControlButton(false);
		phasetwoscreen.infotabbedPane.setCode(Constants.CODE_PHASE_TWO);
		phasetwoscreen.infotabbedPane.setDescription("",false);
		phasetwoscreen.phasetwoinputPane.setPatternEnabled(false);
		phasetwoscreen.phasetwoinputPane.setLabelResultVisible(false, false);
		phasetwoscreen.phasetwoinputPane.setGenerateSearchTextEnabled(controller.isPatternSet());
		if (controller.isSearchTextSet()) {
			phasetwoscreen.infotabbedPane.setSearchText(controller.getSearchText());
			phasetwoscreen.phasetwoshowPane.setSearchText(controller.getSearchText());
		}
		else {
			phasetwoscreen.phasetwoshowPane.setSearchText(null);
		}
		if (controller.isPatternSet()) {
			phasetwoscreen.phasetwoinputPane.setTfPatternText(controller.getPattern().getPattern());
			phasetwoscreen.phasetwoshowPane.setPattern(controller.getPattern());
		}
		else {
			phasetwoscreen.phasetwoinputPane.setTfPatternText("");
			phasetwoscreen.phasetwoshowPane.setPattern(null);
		}
		if (controller.isPatternSet() && controller.isSearchTextSet()) initPhaseTwo();
		scaleScreen(phaseonescreen.getScaleFactor() * 0.1);
		contentPane.updateUI();
		contentPane.validate();
	}
	
	/**
	 * The method is called to initialise the phase two.
	 *
	 */
	public void initPhaseTwo() {
		phasetwoscreen.phasetwoinputPane.setLabelResultVisible(false,false);
		if(controller.isPatternSet() && controller.isSearchTextSet()) {
			phasetwoscreen.phasetwoshowPane.setPattern(controller.getPattern());
			phasetwoscreen.phasetwoshowPane.setSearchText(controller.getSearchText());
			phasetwoscreen.phasetwoshowPane.update((Step)null,false);
			controller.initPhaseTwo();
			setForwardControlButton(true);
			phasetwoscreen.updateUI();
		}
		if(controller.isPatternSet()) {
			phasetwoscreen.phasetwoinputPane.setGenerateSearchTextEnabled(true);
			phasetwoscreen.phasetwoshowPane.setPattern(controller.getPattern());
			phasetwoscreen.updateUI();
		}
		if(controller.isSearchTextSet()) {
			phasetwoscreen.phasetwoshowPane.setSearchText(controller.getSearchText());
			phasetwoscreen.infotabbedPane.setSearchText(controller.getSearchText());
			phasetwoscreen.updateUI();
		}
		phasetwoscreen.infotabbedPane.setDescription("",false);
	}
	
	/**
	 * The method is called when loading a session in phase two.
	 *
	 */
	public void restorePhaseTwo() {
		phasetwoscreen.phasetwoinputPane.setLabelResultVisible(false,false);
		if(controller.isPatternSet() && controller.isSearchTextSet()) {
			controller.getPattern().setPattern(phasetwoscreen.phasetwoinputPane.getTfPatternText());
			phasetwoscreen.infotabbedPane.setSearchText(controller.getSearchText());
			phasetwoscreen.infotabbedPane.setDescription("",false);
			controller.restorePhaseTwo();
			phasetwoscreen.phasetwoshowPane.setPattern(controller.getPattern());
			phasetwoscreen.phasetwoshowPane.setSearchText(controller.getSearchText());
			doNextStep();
			phasetwoscreen.updateUI();
		}
	}
	
	/**
	 * The method is called to set the pattern.
	 *
	 */
	public void setPattern() {
		controller.setLoad(false);
		modconn.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
		if (controller.getPhase() == 1) {
			controller.getPattern().setPattern(phaseonescreen.phaseoneinputPane.getTfPatternText());
			initPhaseOne();
		}
		if (controller.getPhase() == 2) {
			controller.getPattern().setPattern(phasetwoscreen.phasetwoinputPane.getTfPatternText());
			phasetwoscreen.phasetwoinputPane.setGenerateSearchTextEnabled(true);
			initPhaseTwo();
		}
	}
	
	/**
	 * The method is called to get the searchtext.
	 * 
	 * @return the searchtext
	 */
	public String getSearchText() {
		return controller.getSearchText();
	}
	
	/**
	 * The method is called to set the searchext.
	 * 
	 * @param searchtext the searchtext
	 */
	public void setSearchText(String searchtext) {
		if(searchtext.equals("") || searchtext == null) phasetwoscreen.phasetwoshowPane.setSearchText("");
		controller.setLoad(false);
		modconn.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
		controller.setSearchText(searchtext);
		phasetwoscreen.infotabbedPane.setSearchText(controller.getSearchText());
		if(controller.getPhase() == 2) initPhaseTwo();
	}
	
	/**
	 * Creates a random pattern by a number of characters and a length.
	 * 
	 * @param alphabet the size of the alphabet
	 * @param length the length of the random generated pattern
	 */
	public void createRandomPattern(int alphabet, int length) {
		String newpattern = "";
		Random rand = new Random();
		for(int i = 0; i < length; i++) {
			newpattern += (char)(rand.nextInt(alphabet) + 97);
		}
		if(controller.getPhase() == 1) {
			phaseonescreen.phaseoneinputPane.setTfPatternText(newpattern);
			setPattern();
		}
		if(controller.getPhase() == 2) {
			phasetwoscreen.phasetwoinputPane.setTfPatternText(newpattern);
			setPattern();
		}
	}
	
	/**
	 * Creates a random searchtext.
	 *
	 */
	public void createRandomSearchText() {
		String searchtext = "";
		List<Character> charset = new LinkedList<Character>();
		for(int i = 0; i < controller.getPattern().getPattern().length(); i++) 
			charset.add(controller.getPattern().getPattern().charAt(i));
		Random rand = new Random();
		int patposinst = rand.nextInt(Constants.RANDOM_SEARCHTEXT_LENGTH - controller.getPattern().getPattern().length()) + 1;
		for(int i = 0; i < Constants.RANDOM_SEARCHTEXT_LENGTH; i++) {
			if(i == patposinst) {
				searchtext += controller.getPattern().getPattern();
				i += controller.getPattern().getPattern().length() - 1;
			}
			else searchtext += charset.get(rand.nextInt(charset.size()));
		}
		setSearchText(searchtext);
	}
	
	/**
	 * The method is called when the next step should be performed.
	 * 
	 */
	public void doNextStep() {
		Step nextstep = controller.doNextStep();
		if (nextstep == null) setForwardControlButton(false);
		else {
			modconn.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
			setAllControlButton(true);
			if (controller.getPhase() == 1) {
				if (nextstep instanceof P1BeginForStep) {
					if (((P1BeginForStep)nextstep).isLastForStep()) {
						phaseonescreen.phaseoneinputPane.setBtGoOnVisible(true);
						setForwardControlButton(false);
					}
				}
				phaseonescreen.phaseoneshowPane.update(nextstep);
				phaseonescreen.infotabbedPane.setKMPHighlighter(nextstep.getKMPHighlighter());
				phaseonescreen.infotabbedPane.setDescription(nextstep.getDescriptionText(),true);
				phaseonescreen.updateUI();
			}
			if (controller.getPhase() == 2) {
				if (nextstep instanceof P2EndStep) {
					setForwardControlButton(false);
					phasetwoscreen.phasetwoinputPane.setLabelResultVisible(true,((P2EndStep)nextstep).isPatternFound());
				}
				phasetwoscreen.phasetwoshowPane.update(nextstep,true);
				phasetwoscreen.infotabbedPane.setKMPHighlighter(nextstep.getKMPHighlighter());
				int start = ((P2Step)nextstep).getTextPos() - nextstep.getPatPos();
				phasetwoscreen.infotabbedPane.setSearchTextHighlighter(start, 
					start + controller.getPattern().getPattern().length());
				phasetwoscreen.infotabbedPane.setDescription(nextstep.getDescriptionText(),true);
				phasetwoscreen.updateUI();
			}
		}
	}
	
	/**
	 * The method is called when the next big step should be performed.
	 *
	 */
	public void doNextBigStep() {
		Step nextbigstep = controller.doNextBigStep();
		if (nextbigstep == null) setForwardControlButton(false);
		else {
			modconn.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
			setAllControlButton(true);
			if (controller.getPhase() == 1) {
				if (nextbigstep instanceof P1BeginForStep) {
					if (((P1BeginForStep)nextbigstep).isLastForStep()) {
						phaseonescreen.phaseoneinputPane.setBtGoOnVisible(true);
						setForwardControlButton(false);
					}
				}
				phaseonescreen.phaseoneshowPane.update(nextbigstep);
				phaseonescreen.infotabbedPane.setKMPHighlighter(nextbigstep.getKMPHighlighter());
				phaseonescreen.infotabbedPane.setDescription(nextbigstep.getDescriptionText(),true);
				phaseonescreen.updateUI();
			}
			if (controller.getPhase() == 2) {
				if (nextbigstep instanceof P2EndStep) {
					setForwardControlButton(false);
					phasetwoscreen.phasetwoinputPane.setLabelResultVisible(true,((P2EndStep)nextbigstep).isPatternFound());
				}
				phasetwoscreen.phasetwoshowPane.update(nextbigstep,true);
				phasetwoscreen.infotabbedPane.setKMPHighlighter(nextbigstep.getKMPHighlighter());
				int start = ((P2Step)nextbigstep).getTextPos() - nextbigstep.getPatPos();
				phasetwoscreen.infotabbedPane.setSearchTextHighlighter(start, 
					start + controller.getPattern().getPattern().length());
				phasetwoscreen.infotabbedPane.setDescription(nextbigstep.getDescriptionText(),true);
				phasetwoscreen.updateUI();
			}
		}
	}
	
	/**
	 * The method is called when the algorithm should be finished.
	 *
	 */
	public void doEndStep() {
		Step endstep = controller.doEndStep();
		if (endstep == null) setAllControlButton(false);
		else {
			modconn.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
			setBackControlButton(true);
			setForwardControlButton(false);
			if (controller.getPhase() == 1) {
				phaseonescreen.phaseoneshowPane.update(endstep);
				phaseonescreen.infotabbedPane.setKMPHighlighter(endstep.getKMPHighlighter());
				phaseonescreen.infotabbedPane.setDescription(endstep.getDescriptionText(),true);
				phaseonescreen.phaseoneinputPane.setBtGoOnVisible(true);
				phaseonescreen.updateUI();
			}
			if (controller.getPhase() == 2) {
				phasetwoscreen.phasetwoinputPane.setLabelResultVisible(true,((P2EndStep)endstep).isPatternFound());
				phasetwoscreen.phasetwoshowPane.update(endstep,true);
				phasetwoscreen.infotabbedPane.setKMPHighlighter(endstep.getKMPHighlighter());
				int start = ((P2Step)endstep).getTextPos() - endstep.getPatPos();
				phasetwoscreen.infotabbedPane.setSearchTextHighlighter(start, 
					start + controller.getPattern().getPattern().length());
				phasetwoscreen.infotabbedPane.setDescription(endstep.getDescriptionText(),true);
				phasetwoscreen.updateUI();
			}
		}
	}
	
	/**
	 * The method is called when the previous step should be performed.
	 *
	 */
	public void doPreviousStep() {
		Step previousstep = controller.doPreviousStep();
		phaseonescreen.phaseoneinputPane.setBtGoOnVisible(false);
		if (previousstep == null) setBackControlButton(false);
		else {
			modconn.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
			setAllControlButton(true);
			if (controller.getPhase() == 1) {
				if (previousstep instanceof P1InitStep)  
					setBackControlButton(false);
				phaseonescreen.phaseoneshowPane.update(previousstep);
				phaseonescreen.infotabbedPane.setKMPHighlighter(previousstep.getKMPHighlighter());
				phaseonescreen.infotabbedPane.setDescription(previousstep.getDescriptionText(),true);
				phaseonescreen.updateUI();
			}
			if (controller.getPhase() == 2) {
				if (previousstep instanceof P2InitStep) 
					setBackControlButton(false);
				phasetwoscreen.phasetwoinputPane.setLabelResultVisible(false,false);
				phasetwoscreen.phasetwoshowPane.update(previousstep,false);
				phasetwoscreen.infotabbedPane.setKMPHighlighter(previousstep.getKMPHighlighter());
				int start = ((P2Step)previousstep).getTextPos() - previousstep.getPatPos();
				phasetwoscreen.infotabbedPane.setSearchTextHighlighter(start, 
					start + controller.getPattern().getPattern().length());
				phasetwoscreen.infotabbedPane.setDescription(previousstep.getDescriptionText(),true);
				phasetwoscreen.updateUI();
			}
		}
	}
	
	/**
	 * The method is called when the previous big step should be performed.
	 *
	 */
	public void doPreviousBigStep() {
		Step previousbigstep = controller.doPreviousBigStep();
		phaseonescreen.phaseoneinputPane.setBtGoOnVisible(false);
		if (previousbigstep == null) setBackControlButton(false);
		else {
			modconn.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
			setAllControlButton(true);
			if (controller.getPhase() == 1) {
				if (previousbigstep instanceof P1InitStep)  
					setBackControlButton(false);
				phaseonescreen.phaseoneshowPane.update(previousbigstep);
				phaseonescreen.infotabbedPane.setKMPHighlighter(previousbigstep.getKMPHighlighter());
				phaseonescreen.infotabbedPane.setDescription(previousbigstep.getDescriptionText(),true);
				phaseonescreen.updateUI();
			}
			if (controller.getPhase() == 2) {
				if (previousbigstep instanceof P2InitStep) 
					setBackControlButton(false);
				phasetwoscreen.phasetwoinputPane.setLabelResultVisible(false,false);
				phasetwoscreen.phasetwoshowPane.update(previousbigstep,false);
				phasetwoscreen.infotabbedPane.setKMPHighlighter(previousbigstep.getKMPHighlighter());
				int start = ((P2Step)previousbigstep).getTextPos() - previousbigstep.getPatPos();
				phasetwoscreen.infotabbedPane.setSearchTextHighlighter(start, 
					start + controller.getPattern().getPattern().length());
				phasetwoscreen.infotabbedPane.setDescription(previousbigstep.getDescriptionText(),true);
				phasetwoscreen.updateUI();
			}
		}
	}
	
	/**
	 * The method is called when the algorithm should be rewinded.
	 *
	 */
	public void doStartStep() {
		Step startstep = controller.doStartStep();
		phaseonescreen.phaseoneinputPane.setBtGoOnVisible(false);
		if (startstep == null) setAllControlButton(false);
		else {
			modconn.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
			setForwardControlButton(true);
			setBackControlButton(false);
			if (controller.getPhase() == 1) {
				phaseonescreen.phaseoneshowPane.update(startstep);
				phaseonescreen.infotabbedPane.setKMPHighlighter(startstep.getKMPHighlighter());
				phaseonescreen.infotabbedPane.setDescription(startstep.getDescriptionText(),false);
				phaseonescreen.updateUI();
			}
			if (controller.getPhase() == 2) {
				phasetwoscreen.phasetwoinputPane.setLabelResultVisible(false,false);
				phasetwoscreen.phasetwoshowPane.update(startstep,false);
				phasetwoscreen.infotabbedPane.setKMPHighlighter(startstep.getKMPHighlighter());
				int start = ((P2Step)startstep).getTextPos() - startstep.getPatPos();
				phasetwoscreen.infotabbedPane.setSearchTextHighlighter(start, 
					start + controller.getPattern().getPattern().length());
				phasetwoscreen.infotabbedPane.setDescription(startstep.getDescriptionText(),false);
				phasetwoscreen.updateUI();
			}
		}
	}
	
	/**
	 * The method is called to expand the pattern.
	 *
	 */
	public void addPattern() {
		modconn.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
		phaseonescreen.phaseoneinputPane.setTfPatternText(controller.getPattern().getPattern() 
			+ phaseonescreen.phaseoneinputPane.getTfAddPatternText());
		phaseonescreen.phaseoneinputPane.setAddPatternEnabled(false);
		phaseonescreen.phaseoneinputPane.setTfAddPatternText("");
		phaseonescreen.phaseoneinputPane.setBtGoOnVisible(false);
		restorePhaseOne();
	}
	
	/**
	 * The method is called when a key is typed on the pattern input field.
	 *
	 */
	public void keyOnTfPattern() {
		if (controller.getPhase() == 1) {
			if(phaseonescreen.phaseoneinputPane.getTfPatternLength() > 0 && 
					phaseonescreen.phaseoneinputPane.getTfPatternLength() <= Constants.MAX_PAT_LENGTH)
				phaseonescreen.phaseoneinputPane.setPatternEnabled(true);
			else phaseonescreen.phaseoneinputPane.setPatternEnabled(false);
		}
		if (controller.getPhase() == 2) {
			if(phasetwoscreen.phasetwoinputPane.getTfPatternLength() > 0 && 
					phasetwoscreen.phasetwoinputPane.getTfPatternLength() <= Constants.MAX_PAT_LENGTH) {
				phasetwoscreen.phasetwoinputPane.setPatternEnabled(true);
			}
			else phasetwoscreen.phasetwoinputPane.setPatternEnabled(false);
		}
	}
	
	/**
	 * The method is called when a key is typed on the expand-pattern input field.
	 *
	 */
	public void keyOnTfAddPattern() {
		if(phaseonescreen.phaseoneinputPane.getTfAddPatternLength() > 0 && 
				phaseonescreen.phaseoneinputPane.getTfAddPatternLength() < 2 && 
				controller.getPattern().isPatternExpandable())
			phaseonescreen.phaseoneinputPane.setAddPatternEnabled(true);
		else phaseonescreen.phaseoneinputPane.setAddPatternEnabled(false);
	}
	
	/**
	 * Opens a <code>FileChooser</code> to load a searchtext from an ASCII formated file.
	 * 
	 * @return the searchtext
	 */
	public String openSearchTextFileChooser() {
		JFileChooser fcsearchtext = new JFileChooser();
		ExampleFileFilter filter = new ExampleFileFilter();
	    filter.addExtension("txt");
	    filter.addExtension("dat");
	    filter.setDescription("ASCII-Text-Dateien");
	    fcsearchtext.setFileFilter(filter);
		int returnVal = fcsearchtext.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				BufferedReader in = new BufferedReader(
					new FileReader(fcsearchtext.getCurrentDirectory().getAbsolutePath() 
					+ Constants.FILE_SEPARATOR + fcsearchtext.getSelectedFile().getName()));
				String searchtext = "";
				String line = in.readLine();
				while(line != null) {
					searchtext += line + Constants.SEPARATOR;
					line = in.readLine();
				}
				in.close();
				return searchtext;
			}
			catch(Exception err) {
				err.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Enables or disables the control buttons.
	 * 
	 * @param value the value
	 */
	public void setAllControlButton(boolean value) {
		setBackControlButton(value);
		setForwardControlButton(value);
	}
	
	/**
	 * Enables or disables the backward control buttons.
	 * 
	 * @param value the value
	 */
	public void setBackControlButton(boolean value) {
		abortAction.setEnabled(value);
		fastBackwardAction.setEnabled(value);
		backwardAction.setEnabled(value);
	}
	
	/**
	 * Enables or disables the forward control buttons.
	 * 
	 * @param value the value
	 */
	public void setForwardControlButton(boolean value) {
		forwardAction.setEnabled(value);
		fastForwardAction.setEnabled(value);
		finishAction.setEnabled(value);
	}
	
	/**
	 * The method is called when loading a learning example.
	 * 
	 * @param pattern the pattern
	 * @param searchtext the searchtext
	 */
	public void loadLearningExample(String pattern, String searchtext) {
		controller.setLoad(false);
		controller.getPattern().setPattern(pattern);
		controller.setSearchText(searchtext);
	}
	
	/**
	 * The method is called to set the visiblity of the cycles in phase one on or off.
	 *
	 */
	public void setCycles() {
		phaseonescreen.phaseoneshowPane.showCycles(phaseonescreen.phaseoneinputPane.isCycleSelected());
		if(phaseonescreen.phaseoneinputPane.isCycleSelected() && beamermode)
			phaseonescreen.setDividerLocation(phaseonescreen.getDividerLocation() + GUIConstants.CYCLE_TAB_SIZE);
		if((!phaseonescreen.phaseoneinputPane.isCycleSelected()) && beamermode)
			phaseonescreen.setDividerLocation(phaseonescreen.getDividerLocation() - GUIConstants.CYCLE_TAB_SIZE);
		phaseonescreen.updateUI();
	}
	
	/**
	 * Clears the pattern and the searchtext, not the history.
	 *
	 */
	public void clearValues() {
		modconn.setSaveStatus(SaveStatus.NOTHING_TO_SAVE);
		controller.getPattern().setPattern(null);
		controller.setSearchText(null);
		if (controller.getPhase() == 1) phaseonescreen.phaseoneshowPane.update((Step)null);
		if (controller.getPhase() == 2) phasetwoscreen.phasetwoshowPane.update((Step)null, false);
	}
	
	/**
	 * The method is called to scale the viewingscreen. 
	 * 
	 * @param scalefactor the scalefactor
	 */
	public void scaleScreen(double scalefactor) {
		if(controller.getPhase() == 1) {
			phaseonescreen.phaseoneshowPane.incFontSize(scalefactor);
			phaseonescreen.infotabbedPane.scaleScreen(scalefactor);
			phaseonescreen.updateUI();
		}
		if(controller.getPhase() == 2) {
			phasetwoscreen.phasetwoshowPane.incFontSize(scalefactor);
			phasetwoscreen.infotabbedPane.scaleScreen(scalefactor);
			phasetwoscreen.updateUI();
		}
	}
	
	/**
	 * The method is called to set the beamer-mode.
	 * 
	 * @param value the value
	 */
	public void doBeamerMode(boolean value) {
		beamermode = value;
		if(value) {
			if(controller.getPhase() == 1) {
				phaseonescreen.setDividerLocation(GUIConstants.DIVIDER_LOCATION);
				phaseonescreen.setSliderPosition((int)(GUIConstants.SCALEFACTOR * 10));
				phaseonescreen.infotabbedPane.setTabFocus(1);
				scaleScreen(GUIConstants.SCALEFACTOR);
			}
			if(controller.getPhase() == 2) {
				phasetwoscreen.setDividerLocation(GUIConstants.DIVIDER_LOCATION);
				phasetwoscreen.setSliderPosition((int)(GUIConstants.SCALEFACTOR * 10));
				phasetwoscreen.infotabbedPane.setTabFocus(1);
				scaleScreen(GUIConstants.SCALEFACTOR);
			}
		}
		else {
			if(controller.getPhase() == 1) {
				phaseonescreen.setSliderPosition(10);
				scaleScreen(1);
			}
			if(controller.getPhase() == 2) {
				phasetwoscreen.setSliderPosition(10);
				scaleScreen(1);
			}
		}
	}
}