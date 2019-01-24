package org.jalgo.module.unifikation.algo.controller;

import org.jalgo.module.unifikation.algo.view.Algo;
import org.jalgo.module.unifikation.algo.model.History;
import org.jalgo.module.unifikation.algo.model.ProblemSet;
import org.jalgo.module.unifikation.algo.model.Rule;

/**
 * The Help mode that is used as a help to solve the problem
 * implements the singleton pattern
 * @author Alex
 *
 */
public class HelpMode {
	/** The singleton instance */
	private static HelpMode instance;
	
	/**
	 * private constructor
	 * part of the singleton pattern
	 */
	private HelpMode(){
	}
	
	/**
	 * Retrieves the singleton instance of <code>HelpMode</code>.
	 * 
	 * @return the singleton instance
	 */
	public static HelpMode getInstance() {
		if (instance == null) instance = new HelpMode();
		return instance;
	}
	
	/**
	 * Makes sure a pair is selected if possible and returns what was done
	 * @param theAlgo Algo view to use
	 * @return Action done
	 */
	private SelectPairResult selectPair(Algo theAlgo){
		ProblemSet set=theAlgo.getProblemSet();
		if(set.getSelectedPair()>=0 && set.getUsableRule()!=Rule.NoRule)
			return SelectPairResult.AlreadyOK;
		int newPair=set.getUsablePair();
		if(newPair<0){
			if(!theAlgo.getState().next()) return SelectPairResult.NothingDone;
			return SelectPairResult.NoPair;
		}
		set.setSelectedPair(newPair);
		return SelectPairResult.NewSelected;
	}

	/**
	 * Does a half step of the help
	 * @param theAlgo Algo view to use
	 * @return True if pair was just selected (so another half step can be applied)
	 */
	public boolean doHalfStep(Algo theAlgo){
		//1st step: Select a pair
		SelectPairResult sel=selectPair(theAlgo);
		if(sel==SelectPairResult.AlreadyOK){
			//1st step was done, do 2nd step: get and use Rule
			theAlgo.getProblemSet().useRule(theAlgo.getProblemSet().getUsableRule());
			theAlgo.notifySetChanged();
		}else if(sel!=SelectPairResult.NothingDone) theAlgo.notifySelectionChanged();
		return (sel==SelectPairResult.NewSelected);
	}
	
	/**
	 * Does a full step of the help
	 * @param theAlgo Algo view to use
	 * @return True
	 */	
	public boolean doFullStep(Algo theAlgo){
		//do just a half step and check if we can do the second after that
		if(doHalfStep(theAlgo)) doHalfStep(theAlgo);
		return true;
	}
	
	/**
	 * Does all steps of the help
	 * @param theAlgo Algo view to use
	 * @return True
	 */
	public boolean doAllSteps(Algo theAlgo){
		int newPair;
		boolean somethingDone=false;
		ProblemSet set=theAlgo.getProblemSet();
		History history=theAlgo.getHistory();
		while(true){
			newPair=set.getUsablePair();
			if(newPair<0) break;
			somethingDone=true;
			set.setSelectedPair(newPair);
			history.setSelected(newPair);
			set.useRule(set.getUsableRule());
			history.addStep(set);
		}
		if(theAlgo.getState().next()){
			somethingDone=true;
			theAlgo.getState().next();
		}
		if(somethingDone) theAlgo.notifyStepChanged();
		return true;
	}

}
