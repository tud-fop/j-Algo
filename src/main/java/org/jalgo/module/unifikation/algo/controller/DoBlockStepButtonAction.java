package org.jalgo.module.unifikation.algo.controller;

import org.jalgo.module.unifikation.algo.view.Algo;

public class DoBlockStepButtonAction extends IButtonActionAlgo {

	public DoBlockStepButtonAction(Algo algo) {
		super(algo);
	}

	@Override
	public void onClick() {
		theAlgo.setErrorText("");
		//1 full step forward
		if(theAlgo.getHistory().nextFull()){
			theAlgo.notifyStepChanged();
		}else{
			//auto step
			HelpMode.getInstance().doFullStep(theAlgo);
		}
	}

}
