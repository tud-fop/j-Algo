package org.jalgo.module.unifikation.algo.controller;

import org.jalgo.module.unifikation.Constants;
import org.jalgo.module.unifikation.algo.view.Algo;

public class DecompButtonAction extends IButtonActionAlgo {

	public DecompButtonAction(Algo algo) {
		super(algo);
	}

	public String getHoverText() {
		return "<b>Dekompositionsregel:</b><br>Wenn <i>M</i> ein Paar der Form (\u03B4(s<sub>1</sub>, ..., s<sub>k</sub>), \u03B4(t<sub>1</sub>, ..., t<sub>k</sub>)) enth" + Constants.lowercasedAE + "lt, wobei \u03B4 \u03B5 \u03A3 ein k-stelliger Konstruktor ist und s<sub>1</sub>, ..., s<sub>k</sub>, t<sub>1</sub>, ..., t<sub>k</sub> Terme " + Constants.lowercasedUE + "ber Konstruktoren und Variablen sind, dann l" + Constants.lowercasedOE + "sche das Paar \n(\u03B4(s<sub>1</sub>, ..., s<sub>k</sub>),\u03B4(t<sub>1</sub>, ..., t<sub>k</sub>)) aus <i>M</i> und f" + Constants.lowercasedUE + "ge die Paare (s<sub>1</sub>, t<sub>1</sub>), ..., (s<sub>k</sub>, t<sub>k</sub>) hinzu.";
	}

	public void onClick() {
		if (theAlgo.getProblemSet().getSelectedPPair() == null) theAlgo.setErrorText("Sie haben kein Paar ausgew"+ Constants.lowercasedAE +"hlt auf das Sie diese Regel anwenden wollen.");
		else theAlgo.setErrorText(""); 
		if(theAlgo.getProblemSet().getSelectedPair()!=-1){
	    if(theAlgo.getProblemSet().decomposition()){
		   	theAlgo.notifySetChanged();
		}
		else {
		   	theAlgo.getProblemSet().updateLastRuleError();
		   	theAlgo.setErrorText(theAlgo.getProblemSet().getLastRuleErrorText());
		}
	    }
	}

}
