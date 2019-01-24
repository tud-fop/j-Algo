package org.jalgo.module.unifikation.algo.controller;

import org.jalgo.module.unifikation.algo.view.Algo;

public class UndoStepButtonAction extends IButtonActionAlgo {

	public UndoStepButtonAction(Algo algo) {
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
		//1 half step back
		if(theAlgo.getHistory().prev()){
			theAlgo.notifyStepChanged();
		}
	}

}
