package org.jalgo.module.unifikation.algo.controller;

import org.jalgo.module.unifikation.Constants;
import org.jalgo.module.unifikation.algo.view.Algo;

public class ElimButtonAction extends IButtonActionAlgo {

	public ElimButtonAction(Algo algo) {
		super(algo);
	}

	public String getHoverText() {
		return "<b>Elimination:</b><br>Wenn <i>M</i> ein Paar (x, x) f" + Constants.lowercasedUE + "r eine Variable x enth" + Constants.lowercasedAE + "lt, dann l" + Constants.lowercasedOE + "sche dieses Paar aus <i>M</i>.";
	}

	public void onClick() {
		if (theAlgo.getProblemSet().getSelectedPPair() == null) theAlgo.setErrorText("Sie haben kein Paar ausgew"+ Constants.lowercasedAE +"hlt auf das Sie diese Regel anwenden wollen.");
		else theAlgo.setErrorText("");
	    if(theAlgo.getProblemSet().getSelectedPair()!=-1){ 				    
		    if(theAlgo.getProblemSet().elimination())
		    {
		    	theAlgo.notifySetChanged();
		    }
		    else {
		    	theAlgo.getProblemSet().updateLastRuleError();
		    	theAlgo.setErrorText(theAlgo.getProblemSet().getLastRuleErrorText());
		    }
	    } 
	}

}
