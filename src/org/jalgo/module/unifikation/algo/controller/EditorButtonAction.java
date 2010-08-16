package org.jalgo.module.unifikation.algo.controller;

import org.jalgo.module.unifikation.algo.view.Algo;

public class EditorButtonAction extends IButtonActionAlgo {

	public EditorButtonAction(Algo algo) {
		super(algo);
	}
	
	@Override
	public void onClick() {
		theAlgo.setErrorText("");
		theAlgo.onDoneClick();
	}

}
