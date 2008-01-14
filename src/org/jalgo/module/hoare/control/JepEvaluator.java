package org.jalgo.module.hoare.control;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import org.jalgo.module.hoare.model.Assertion;

public class JepEvaluator implements Evaluation {

	private int max_eval_time = Integer.MAX_VALUE;

	private ProgramControl programControl = null;

	private Map<Integer, EvaluationThread> threadTable = new HashMap<Integer, EvaluationThread>();
	private int base = 50;

	/**
	 * Constructor
	 * 
	 * @param time -
	 *            Time for evaluation
	 * @param base -
	 *            Range of numbers for testcases
	 * @param control -
	 *            ProgramControl to forward results and errors to
	 */
	public JepEvaluator(int time, int base, ProgramControl control) {
		this.max_eval_time = time;
		this.base = base;
		this.programControl = control;
	}

	/**
	 * Constructor
	 * 
	 * @param time -
	 *            Time for evaluation
	 * @param base -
	 *            Range of numbers for testcases
	 */
	public JepEvaluator(int time, int base) {
		this.max_eval_time = time;
		this.base = base;
	}

	public int getEvaluations() {
		return this.threadTable.size();
	}

	/**
	 * Starts a new thread with a given ID which evaluates an implication of two
	 * assertions. The thread will report its outcome by calling putResult(int,
	 * boolean).
	 * 
	 * @see EvaluationThread
	 * @see putResult(int, boolean)
	 * @param id -
	 *            the id for the new thread
	 * @param assertion_source -
	 *            left side of implication
	 * @param assertion_target -
	 *            right side of implication
	 * @throws RejectedExecutionException,
	 *             NullPointerException
	 */
	public void evaluate(int id, Assertion assertion_source,
			Assertion assertion_target) throws RejectedExecutionException,
			NullPointerException {
		if (max_eval_time == 0)
			return; // If evaluationtime is set to zero do nothing
		
		// If the threadTable already contains the id
		// the user called the avaluation twice on the same node (id)
		// -> canel the first evaluation
		if(threadTable.containsKey(id)) killThread(id);
			
		EvaluationThread my_thread = new EvaluationThread(assertion_source,
				assertion_target, this, id, max_eval_time, base);
		threadTable.put(id, my_thread); // Save ID and Thread for later killing
		my_thread.start();
	}

	/**
	 * Tries to nicely stop the thread of the given id
	 * @param id	-	Thread ID
	 */
	public void killThread(int id){
		if(threadTable.containsKey(id)){
			try {
				// if killThread is called after the thread has finished this could cause a NullPointerException
				threadTable.get(id).end();
			} catch (Exception e) {
				// Who gives a shit?
				System.out.println(e.getStackTrace());
			}
			threadTable.remove(id);
		}
	}
		
	public void setController(ProgramControl control) {
		this.programControl = control;
	}

	/**
	 * Set the time limt for evaluation in seconds.
	 * 
	 * @param max_eval_time -
	 *            e.g. 5
	 */
	public void setMax_eval_time(int max_eval_time) {
		this.max_eval_time = max_eval_time;
	}

	/**
	 * Adds a result to the result map consisting of thread-ID and result. Is
	 * called by the evaluation-thread.
	 * 
	 * @param id
	 * @param result
	 */
	public void putResult(int id, boolean result, Set<String> currentVars) {
		if(threadTable.containsKey(id))
			threadTable.remove(id);
		if (programControl != null)
			programControl.reportResult(id, result, currentVars.toString());
	}

	/**
	 * An evaluationThread will report a caught Exception. The Exception will be
	 * forwarded to ProgramControl.
	 * 
	 * @see setController(ProgramControl)
	 * @param id -
	 *            Thread id
	 * @param message -
	 *            Exception
	 */
	public void reportError(int id, String message) {
		if(threadTable.containsKey(id))
			threadTable.remove(id);
		if (programControl != null)
			programControl.reportError(id, message);
	}

	/**
	 * Set the range for evaluation e.g. 50
	 * Variables will be set to values from -base/2 to base/2
	 */
	public void setBase(int base) {
		this.base = base;

	};

}
