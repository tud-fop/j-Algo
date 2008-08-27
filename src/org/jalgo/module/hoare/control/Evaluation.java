package org.jalgo.module.hoare.control;

import org.jalgo.module.hoare.model.VerificationFormula;

/**
 * an interface for Evaluation.
 * has only the needed functions for evaluation, stopping it and reporting things
 *
 * @author Johannes
 */
public interface Evaluation {
	/**
	 * will stop an evaluation with the given id
	 *
	 * @param id
	 *		this evaluation should be stopped
	 */
	public void killThread(int id);

	/**
	 * will start an evaluaton of the given VerificationFormula
	 *
	 * @param vf
	 *		the VerificationFormula to evaluate
	 */
	public void evaluate(VerificationFormula vf);

}
