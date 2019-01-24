package org.jalgo.module.unifikation.algo.controller;

import javax.swing.JOptionPane;

import org.jalgo.module.unifikation.algo.view.Algo;

public class NotUnifiableButtonAction extends IButtonActionAlgo {

	public NotUnifiableButtonAction(Algo algo) {
		super(algo);
	}

	@Override
	public void onClick() {
		theAlgo.setErrorText("");
		if(theAlgo.getProblemSet().getPairNotFinal() != -1)
		{
			theAlgo.getState().next();
			theAlgo.notifySelectionChanged();
			JOptionPane.showMessageDialog(theAlgo.getContentPane(),"Korrekt, die Terme sind nicht unifizierbar.");
		} 
		else {
			JOptionPane.showMessageDialog(theAlgo.getContentPane(),"Leider falsch.");
		}
	}
	

}
