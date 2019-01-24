package org.jalgo.module.unifikation.algo.controller;

import org.jalgo.module.unifikation.algo.view.Algo;

public abstract class IButtonActionAlgo extends IButtonAction {
	protected final Algo theAlgo;

	public IButtonActionAlgo(Algo algo) {
		super(algo);
		this.theAlgo=algo;
	}

}
