package org.jalgo.module.unifikation.algo.controller;

import org.jalgo.module.unifikation.algo.view.Algo;

public class DoStepButtonAction extends IButtonActionAlgo {

	public DoStepButtonAction(Algo algo) {
		super(algo);
	}

	@Override
	public void onClick() {
		theAlgo.setErrorText("");
		//1 half step forward
		if(theAlgo.getHistory().next()){
			theAlgo.notifyStepChanged();
		}else{
			//auto step
			HelpMode.getInstance().doHalfStep(theAlgo);
		}
	}

}
