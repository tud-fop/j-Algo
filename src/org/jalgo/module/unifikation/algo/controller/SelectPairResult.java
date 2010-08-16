package org.jalgo.module.unifikation.algo.controller;

/**
 * Results for the selectPair function of auto-mode
 * @author Alex
 *
 */
public enum SelectPairResult {
	/**
	 * There is a pair currently selected and a rule can be applied
	 */
	AlreadyOK,
	/**
	 * A new pair has been selected and a rule can be applied
	 */
	NewSelected,
	/**
	 * No pair was found, where a rule can be applied
	 */
	NoPair,
	/**
	 * Nothing was done
	 */
	NothingDone;
}
