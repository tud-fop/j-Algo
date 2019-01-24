package org.jalgo.module.kmp;

import org.jalgo.module.kmp.algorithm.History;
import org.jalgo.module.kmp.algorithm.Pattern;
import org.jalgo.module.kmp.algorithm.StepFactory;
import org.jalgo.module.kmp.algorithm.Step;
import org.jalgo.module.kmp.algorithm.phaseone.P1Step;
import org.jalgo.module.kmp.algorithm.phasetwo.P2Step;

import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * The class <code>Controller</code> is an important connection between the
 * GUI an the working algorithm. <br>
 * It provides several functions like: to start a new algorithm, to check if the
 * working algorithm is ready and to get the results of the active algorithm.
 * 
 * @author Danilo Lisske
 */
public class Controller implements Constants {
	private Pattern pattern;
	private String searchtext;
	private History history;
	private StepFactory stepfactory;
	private int phase;
	private boolean isload;
	
	/**
	 * The constructor of the controller. It creates the <code>StepFactory</code>
	 * and initialises some variables.
	 * 
	 * @param p the pattern
	 * @param s the searchtext
	 * @param h the history
	 */
	public Controller(Pattern p, String s, History h) {
		pattern = p;
		searchtext = s;
		history = h;
		stepfactory = new StepFactory();
		phase = 0;
		isload = false;
	}
	
	/**
	 * The method is called when a j-Algo-KMP-module file has to be loaded.
	 * 
	 * @param p the pattern
	 * @param s the searchtext
	 * @param h the history
	 * @return nothing (0)
	 */
	public int loadData(String p, String s, History h) {
		pattern.setPattern(p);
		searchtext = s;
		history = h;
		isload = true;
		Step step = null;
		if (history.getAktStepNo() == -1) step = history.getNextStep();
		else step = history.getCurrentStep();
		if (step instanceof P1Step) return 1;
		if (step instanceof P2Step) return 2;
		return 0;
	}
	
	/**
	 * The method is called when a user wants to save his actual session.
	 * 
	 * @param out the output stream
	 */
	public void saveData(ByteArrayOutputStream out) {
		try {
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(pattern.getPattern());
			objOut.writeObject(searchtext);
			objOut.writeObject(history);
			objOut.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * The method starts the phase one by setting the phase-variable to 1.
	 *
	 */
	public void startPhaseOne() {
		phase = 1;
	}
	
	/**
	 * The method initialises the phase one by initialising the <code>StepFactory</code>.
	 *
	 */
	public void initPhaseOne() {
		phase = 1;
		if (!isload) stepfactory.startPhaseOne(pattern,history);
	}
	
	/**
	 * The method is called when loading a session or expanding the pattern in phase one.
	 *
	 */
	public void restorePhaseOne() {
		phase = 1;
		int oldstepno = history.getAktStepNo();
		initPhaseOne();
		if (oldstepno == -1) oldstepno++;
		history.setAktStepNo(oldstepno - 1);
	}
	
	/**
	 * The method starts the phase two by setting the phase-variable to 2.
	 *
	 */
	public void startPhaseTwo() {
		phase = 2;
	}
	
	/**
	 * The method initialises the phase two by initialising the <code>StepFactory</code>.
	 *
	 */
	public void initPhaseTwo() {
		phase = 2;
		if (!isload) stepfactory.startPhaseTwo(pattern,searchtext,history);
	}
	
	/**
	 * The method is called when loading a session or expanding the pattern in phase two.
	 *
	 */
	public void restorePhaseTwo() {
		phase = 2;
		int oldstepno = history.getAktStepNo();
		initPhaseTwo();
		history.setAktStepNo(oldstepno - 1);
	}
	
	/**
	 * The method is called if a session was loaded.
	 * 
	 * @param value if the actual session was loaded
	 */
	public void setLoad(boolean value) {
		isload = value;
	}
	
	/**
	 * The method is called to get the pattern.
	 * 
	 * @return the pattern
	 */
	public Pattern getPattern() {
		return pattern;
	}
	
	/**
	 * The method is called to check if the pattern is set.
	 * 
	 * @return if the pattern is set
	 */
	public boolean isPatternSet() {
		return pattern.isPatternSet();
	}
	
	/**
	 * The method is called to check if the pattern is expandable.
	 * 
	 * @return if the pattern is expandable
	 */
	public boolean isPatternExpandable() {
		return pattern.isPatternExpandable();
	}
	
	/**
	 * The method is called to get the searchtext.
	 * 
	 * @return the searchtext
	 */
	public String getSearchText() {
		return searchtext;
	}
	
	/**
	 * The method is called to check if the searchtext is set.
	 * 
	 * @return if the searchtext is set
	 */
	public boolean isSearchTextSet() {
		return !(searchtext.equals("") || searchtext == null);
	}
	
	/**
	 * The method is called to set the searchtext.
	 * 
	 * @param s the searchtext
	 */
	public void setSearchText(String s) {
		searchtext = s;
	}
	
	/**
	 * The method is called to check in which phase the user is.
	 * 
	 * @return the actual phase
	 */
	public int getPhase() {
		return phase;
	}
	
	/**
	 * The method is called when the next step should be performed.
	 * 
	 * @return the next step
	 */
	public Step doNextStep() {
		Step nextstep = history.getNextStep();
		return nextstep;
	}
	
	/**
	 * The method is called when the next big step should be performed.
	 * 
	 * @return the next big step
	 */
	public Step doNextBigStep() {
		Step nextbigstep = history.getNextBigStep();
		return nextbigstep;
	}
	
	/**
	 * The method is called when the algorithm should be finished.
	 * 
	 * @return the end step
	 */
	public Step doEndStep() {
		Step endstep = history.getEndStep();
		return endstep;
	}
	
	/**
	 * The method is called when the previous step should be performed.
	 * 
	 * @return the previous step
	 */
	public Step doPreviousStep() {
		Step previousstep = history.getPreviousStep();
		return previousstep;
	}
	
	/**
	 * The method is called when the previous big step should be performed.
	 * 
	 * @return the previous big step
	 */
	public Step doPreviousBigStep() {
		Step previousbigstep = history.getPreviousBigStep();
		return previousbigstep;
	}
	
	/**
	 * The method is called when the algorithm should be rewinded.
	 * 
	 * @return the start step
	 */
	public Step doStartStep() {
		Step startstep = history.getStartStep();
		return startstep;
	}
	
	/**
	 * The method is called to clear all sensible data.
	 *
	 */
	public void clearData() {
		pattern.setPattern("");
		history.clear();
		searchtext = "";
	}
}