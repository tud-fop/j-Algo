package org.jalgo.module.unifikation.algo.controller;

import org.jalgo.module.unifikation.algo.view.Algo;

public class UndoAllButtonAction extends IButtonActionAlgo {
	
	public UndoAllButtonAction(Algo algo) {
		super(algo);
	}
	
	public void onClick() {
		theAlgo.setErrorText("");
		boolean somethingDone=false;
		while(theAlgo.getState().prev()){
			somethingDone=true;
		}
		//jumps to first step
		if(theAlgo.getHistory().prevAll()){
			somethingDone=true;
		}
		if(somethingDone) theAlgo.notifyStepChanged();
	}

}
