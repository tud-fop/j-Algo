package org.jalgo.module.kmp.algorithm;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jalgo.module.kmp.algorithm.phaseone.P1SmallStep;
import org.jalgo.module.kmp.algorithm.phasetwo.P2SmallStep;

/**
 * The <code>History</code> includes all <code>Step</code>s which are necessary
 * for the current algorithm session.
 * 
 * @author Danilo Lisske, Nadine Grabow
 */
public class History implements Serializable {
	private static final long serialVersionUID = 5319401850744447321L;
	
	private List<Step> history;
	private int aktstepno;
	
	/**
	 * The constructor of the <code>History</code>.
	 *
	 */
	public History() {
		history = new LinkedList<Step>();
		aktstepno = -1;
	}
	
	/**
	 * Returns the actual step number.
	 * 
	 * @return the actual step number
	 */
	public int getAktStepNo() {
		return aktstepno;
	}
	
	/**
	 * Sets the actual step number.
	 * 
	 * @param newaktstepno the new actual step number
	 */
	public void setAktStepNo(int newaktstepno) {
		aktstepno = newaktstepno;
	}
	
	/**
	 * Returns the size of the history.
	 * 
	 * @return the size of the history
	 */
	public int getGesStepNo() {
		return history.size() - 1;
	}
	
	/**
	 * Adds a given <code>Step</code> to the <code>History</code>.
	 * 
	 * @param step the step
	 */
	public void addStep(Step step) {
		history.add(step);
	}
	
	/**
	 * Returns the current <code>Step</code>.
	 * 
	 * @return the current <code>Step</code>
	 */
	public Step getCurrentStep() {
		return history.get(aktstepno);
	}
	
	/**
	 * Returns the next <code>Step</code>.
	 * 
	 * @return the next <code>Step</code>
	 */
	public Step getNextStep() {
		if (aktstepno < getGesStepNo())	return history.get(++aktstepno);
		else return null;
	}
	
	/**
	 * Returns the next big <code>Step</code>.
	 * 
	 * @return the next big <code>Step</code>
	 */
	public Step getNextBigStep() {
		if (aktstepno < getGesStepNo()) {
			Step step = history.get(++aktstepno);
	        while((step instanceof P1SmallStep || step instanceof P2SmallStep) 
	        		&& (aktstepno < getGesStepNo())) step = history.get(++aktstepno);
			return step;
		}
		else return null;
	}
	
	/**
	 * Returns the end <code>Step</code>.
	 * 
	 * @return the end <code>Step</code>
	 */
	public Step getEndStep() {
		if (getGesStepNo() > 0) {
			aktstepno = getGesStepNo();
			return history.get(getGesStepNo());
		}
		else return null;
	}
	
	/**
	 * Returns the previous <code>Step</code>.
	 * 
	 * @return the previous <code>Step</code>
	 */
	public Step getPreviousStep() {
		if (aktstepno > 0) return history.get(--aktstepno);
		else return null;
	}
	
	/**
	 * Returns the previous big <code>Step</code>.
	 * 
	 * @return the previous big <code>Step</code>
	 */
	public Step getPreviousBigStep() {
		if (aktstepno > 0) {
			Step step = history.get(--aktstepno);
	        while((step instanceof P1SmallStep || step instanceof P2SmallStep) 
	        		&& (aktstepno > 0)) step = history.get(--aktstepno);
			return step;
		}
		else return null;
	}
	
	/**
	 * Returns the start <code>Step</code>.
	 * 
	 * @return the start <code>Step</code>
	 */
	public Step getStartStep() {
		if (getGesStepNo() > 0) {
			aktstepno = 0;
			return history.get(0);
		}
		else return null;
	}
	
	/**
	 * Clears the <code>History</code>.
	 *
	 */
	public void clear() {
		aktstepno = -1;
		history.clear();
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(aktstepno);
		out.writeObject(history);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		aktstepno = in.readInt();
		history = (List<Step>)in.readObject();
	}
}