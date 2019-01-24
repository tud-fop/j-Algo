package org.jalgo.module.unifikation.algo.controller;

import org.jalgo.module.unifikation.algo.view.Algo;

public class UndoBlockStepButtonAction extends IButtonActionAlgo {

	public UndoBlockStepButtonAction(Algo algo) {
		super(algo);
	}

	@Override
	public void onClick() {
		theAlgo.setErrorText("");
		if(theAlgo.getState().prev()){
			//State changed
			theAlgo.notifySelectionChanged();
			return;
		}
		//1 full step back
		if(theAlgo.getHistory().prevFull()){
			theAlgo.notifyStepChanged();
		}
	}

}
