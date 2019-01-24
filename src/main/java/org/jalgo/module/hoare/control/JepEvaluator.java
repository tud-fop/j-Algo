package org.jalgo.module.hoare.control;

import java.util.Map;
import java.util.HashMap;

import org.jalgo.module.hoare.constants.TextStyle;
import org.jalgo.module.hoare.model.VerificationFormula;

/**
 * implementation of the interface Evaluation for djep.
 * manages all <code>EvaluationThread</code>s and forwards the messages send by them to the <code>Controller</code>
 *
 * @author Johannes
 */
public class JepEvaluator implements Evaluation {
	
	private int base = 50;
	private int maxEvalTime = 15;
	private Controller controller;
	private Map<Integer, EvaluationThread> threadTable = new HashMap<Integer, EvaluationThread>();

	/**
	 * creates a new instance of JepEvaluator
	 *
	 * @param base
	 *		number of ciphers to test
	 * @param maxEvalTime
	 *		maximum time an evaluation may last in seconds
	 * @param controller
	 *		the <code>Controller</code>, that results and errors will be send to
	 */
	public JepEvaluator(int base, int maxEvalTime, Controller controller){
		this.base = base;
		this.maxEvalTime = maxEvalTime;
		this.controller = controller;
	}

	/**
	 * will send results and errors to the <code>Controller</code>
	 *
	 * @param id
	 *		index of the node/<code>VerificationFormula</code> that was evaluated
	 * @param result
	 * 		<code>false</code> for errors or evaluation failed
	 * @param message
	 *		the message for the error or result
	 */
	public void report(int id, boolean result, String message){
		// just to be sure it's one of our threads ...
		if( threadTable.containsKey(id) ){
			// forward message and result to controller
			controller.report( id, result, message );

			// thread should finish after this -> remove from threadTable
			threadTable.remove(id);
		}
	}

	/**
	 * will stop an evaluation with the given id.
	 * this will call end() of the thread and remove it from the threadTable
	 *
	 * @param id
	 *		this evaluation should be stopped
	 */
	public void killThread(int id){
		if( threadTable.containsKey(id) ){
			try {
				threadTable.get(id).end();
			} catch( NullPointerException e ) {
				// do nothing
			}
			threadTable.remove(id);
		}
	}

	/**
	 * will start an evaluaton of the given VerificationFormula
	 *
	 * @param vf
	 *		the VerificationFormula to evaluate
	 */
	public void evaluate(VerificationFormula vf){
		if( vf == null )
			return;
		
		// check if this vf already gets evaluated -> kill this evaluation
		if( threadTable.containsKey( vf.getId() ) ){
			killThread( vf.getId() );
		}
		
		//TODO: Pr�fen ob es korrekt ist ? sieht zu einfach aus
		if (vf.isImplication()){
			//System.out.println(vf.getPreAssertion(TextStyle.SOURCE)+"=>"+vf.getPostAssertion(TextStyle.SOURCE));

			EvaluationThread thread = new EvaluationThread(vf.getId(),maxEvalTime, 
		 		                                            base,this,
		 		                                            vf.getPreAssertion(TextStyle.SOURCE),
		 		                                            vf.getPostAssertion(TextStyle.SOURCE));  
			threadTable.put(vf.getId(),thread);

			thread.start();
		}
		
		
/*		// create new thread
		EvaluationThread thread;
		if( vf.getParent().getAppliedRule() == Rule.STRONGPRE ){
			thread = new EvaluationThread( vf.getId(), maxEvalTime, base, this, vf.getParent().getPreAssertion(TextStyle.SOURCE), vf.getPreAssertion(TextStyle.SOURCE) );
		}
		else if( vf.getParent().getAppliedRule() == Rule.WEAKPOST ){
			thread = new EvaluationThread( vf.getId(), maxEvalTime, base, this, vf.getPostAssertion(TextStyle.SOURCE), vf.getParent().getPostAssertion(TextStyle.SOURCE) );
		}
		else {
				return;
		}

		// save id => thread for later killing
		threadTable.put( vf.getId(), thread );

		thread.start();*/
	}

}
