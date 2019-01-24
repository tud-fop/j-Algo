package org.jalgo.module.unifikation.algo.controller;

import org.jalgo.module.unifikation.Constants;
import org.jalgo.module.unifikation.algo.view.Algo;

public class SwapButtonAction extends IButtonActionAlgo {

	public SwapButtonAction(Algo algo) {
		super(algo);
	}

	public String getHoverText() {
		return "<b>Vertauschung:</b><br>Wenn <i>M</i> ein Paar (t, x) enth" + Constants.lowercasedAE + "lt und t ist keine Variable, dann l" + Constants.lowercasedOE + "sche dieses Paar aus <i>M</i> und f" + Constants.lowercasedUE + "ge das Paar (x, t) hinzu.";
	}

	public void onClick() {
		if (theAlgo.getProblemSet().getSelectedPPair() == null) theAlgo.setErrorText("Sie haben kein Paar ausgew"+ Constants.lowercasedAE +"hlt auf das Sie diese Regel anwenden wollen.");
		else theAlgo.setErrorText("");
	    if(theAlgo.getProblemSet().getSelectedPair()!=-1){ 				    
		    if(theAlgo.getProblemSet().swap())
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
