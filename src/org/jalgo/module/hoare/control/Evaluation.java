package org.jalgo.module.hoare.control;

import java.util.Set;
import java.util.concurrent.RejectedExecutionException;

import org.jalgo.module.hoare.model.Assertion;

public interface Evaluation {

	/**
	 * Evaluate a given implication problem consisting of source, target and
	 * problem id. The evaluationThread will call putResult(id, result) to
	 * report the outcome.
	 * 
	 * @param id
	 *            The id-number of the evauluation task
	 * @param source
	 *            Left side of the implication
	 * @param target
	 *            Right side of the implication
	 * @throws RejectedExecutionException
	 *             If the evaluationthred could not be started
	 * @throws NullPointerException
	 * @see putResult(int id, boolean result)
	 */
	public void evaluate(int id, Assertion source, Assertion target)
			throws RejectedExecutionException, NullPointerException;

	/**
	 * Set the maximum acceptable time to take for one implication problem.
	 * After the time is up the thread will report its current outcome by
	 * calling putResult(id, result).
	 * 
	 * @param max_eval_time
	 *            Time to live for an evaluationthread.
	 */
	public void setMax_eval_time(int max_eval_time);

	/**
	 * Reported results and errors will be forwarded to the ProgramControl (if
	 * given)
	 * 
	 * @param control
	 *            ProgramControl to forward to.
	 * @see putResult(int id, boolean result)
	 * @see reportError(int id, Exception message)
	 */
	public void setController(ProgramControl control);

	/**
	 * Will be called by an evaluationThread to report its result. The result
	 * will be forwarded to the ProgramControl if given.
	 * 
	 * @param id
	 *            Id of the evaluation task.
	 * @param result
	 *            True/False
	 * @param currentVars
	 *            Map of Variables and their current values
	 * @see setController(ProgramControl control)
	 */
	public void putResult(int id, boolean result, Set<String> currentVars);

	/**
	 * If an exception is caught by an evaluationthread during evaluation it
	 * will report the error message and also report false as the result. The
	 * exception message will be forwarded to ProgramControl if given.
	 * 
	 * @param id
	 *            Id of the evaluation task.
	 * @param message
	 *            errorMessage
	 * @see setController(ProgramControl control)
	 */
	public void reportError(int id, String message);

	/**
	 * An evaluation problem will 'evaluated' by testing. All (free) variables
	 * in an implication will be allocated by different sets of integer values.
	 * setBase(int base) will set the positive range for these integer values.
	 * The negative values will be add automatically.
	 * 
	 * Example: setBase(25); Variables will be allocated from -25 to +25
	 * 
	 * @param base
	 */
	public void setBase(int base);

	/**
	 * Returns the number of currently running evaluation threads.
	 * 
	 * @return Number of threads.
	 */
	public int getEvaluations();
	
	/**
	 * Kills a evaluationThread by the given id
	 * @param id	-	id
	 */
	public void killThread(int id);
}
