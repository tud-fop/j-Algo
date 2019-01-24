package org.jalgo.module.unifikation.algo.controller;

import org.jalgo.module.unifikation.Constants;
import org.jalgo.module.unifikation.algo.view.Algo;

public class SubButtonAction extends IButtonActionAlgo {

	public SubButtonAction(Algo algo) {
		super(algo);
	}

	public String getHoverText() {
		return "<b>Substitution:</b><br>Wenn <i>M</i> das Paar (x, t) enth" + Constants.lowercasedAE + "lt und x kommt in t nicht vor (occur check), dann ersetze in jedem anderen Paar von <i>M</i> die Variable x durch den Term t.";
	}

	public void onClick() {
		if (theAlgo.getProblemSet().getSelectedPPair() == null) theAlgo.setErrorText("Sie haben kein Paar ausgew"+ Constants.lowercasedAE +"hlt auf das Sie diese Regel anwenden wollen.");
		else theAlgo.setErrorText("");
		if(theAlgo.getProblemSet().getSelectedPair()!=-1){ 				    
		    if(theAlgo.getProblemSet().substitution())
		    {
		    	theAlgo.notifySetChanged();
		    }
		    else {
		    	theAlgo.setErrorText(theAlgo.getProblemSet().getLastRuleErrorText());
		    }
	    } 
	}

}
