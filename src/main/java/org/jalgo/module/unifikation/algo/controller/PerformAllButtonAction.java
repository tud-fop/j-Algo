package org.jalgo.module.unifikation.algo.controller;

import org.jalgo.module.unifikation.algo.view.Algo;

public class PerformAllButtonAction extends IButtonActionAlgo {
	
	public PerformAllButtonAction(Algo algo) {
		super(algo);
	}
	
	public void onClick() {
		theAlgo.setErrorText("");
		
		//shows all steps
		if(theAlgo.getHistory().nextAll()){
			theAlgo.notifyStepChanged();
		}else{
			//auto step
			HelpMode.getInstance().doAllSteps(theAlgo);
		}		
	}
}
