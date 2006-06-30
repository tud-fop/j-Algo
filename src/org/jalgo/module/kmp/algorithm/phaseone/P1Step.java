package org.jalgo.module.kmp.algorithm.phaseone;

import org.jalgo.module.kmp.algorithm.Step;

/**
 * This class is the interface of all stepsclasses in phase one
 * 
 * @author Matthias Neubert
 */
public interface P1Step extends Step {

	/**
	 * Returns the compare index.
	 * 
	 * @return the compare index
	 */
	public int getVglInd();
}