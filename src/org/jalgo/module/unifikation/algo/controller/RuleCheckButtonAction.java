package org.jalgo.module.unifikation.algo.controller;

import javax.swing.JOptionPane;

import org.jalgo.module.unifikation.algo.view.Algo;

public class RuleCheckButtonAction extends IButtonActionAlgo {

	public RuleCheckButtonAction(Algo algo) {
		super(algo);
	}

	@Override
	public void onClick() {
		theAlgo.setErrorText("");
		if(theAlgo.getState().next())
		{
			theAlgo.notifySelectionChanged();
			JOptionPane.showMessageDialog(theAlgo.getContentPane(),"Korrekt. Es ist keine Regel mehr anwendbar. Sind die Ausgangsterme unifiziert oder nicht unifizerbar?");
		}
		else {
			JOptionPane.showMessageDialog(theAlgo.getContentPane(),"Es sind noch weitere Regeln anwendbar.");
		}
	}

}
