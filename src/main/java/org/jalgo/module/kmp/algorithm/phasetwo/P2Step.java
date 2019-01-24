package org.jalgo.module.kmp.algorithm.phasetwo;

import org.jalgo.module.kmp.algorithm.Step;

/**
 * This class is the interface for all stepclasses from phase 2.
 *
 * @author Danilo Lisske, Elisa Böhl
 */
public interface P2Step extends Step {
	
	/**
	 * Returns the text position.
	 * 
	 * @return the text position
	 */
	public int getTextPos();
	
}