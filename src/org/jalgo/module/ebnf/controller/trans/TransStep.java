/**
 * 
 */
package org.jalgo.module.ebnf.controller.trans;

import org.jalgo.module.ebnf.util.IAction;
import org.jalgo.module.ebnf.model.trans.TransMap;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.ebnf.Term;


import java.util.Map;



/**
 * @author Andre
 *
 */
public class TransStep implements IAction {
	
	/**
	 * Represents the element to be transformed
	 */
	SynDiaElem transElem;
	/**
	 * Represents the term associated to the syntax diagram element
	 */
	Term transTerm;
	/**
	 * the map where these associations are held
	 */
	TransMap transMap;
	
	
	/** Initializes the paramaters
	 * @param sde A syntax diagram element
	 * @param transMap The transMap
	 */
	public TransStep(SynDiaElem sde, TransMap transMap) {
		
		this.transElem = sde;
		this.transMap = transMap;
		this.transTerm = transMap.get(sde);
	}
	
	public void perform() {
		
		transMap.put(transElem, null);
		
	}
	
	public void undo() {
		
		transMap.put(transElem, transTerm);
		
	}
}
