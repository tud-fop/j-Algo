/*
 * Created on 27.06.2004
 */
 
package org.jalgo.module.synDiaEBNF;

import java.util.LinkedList;

import org.jalgo.main.util.Stack;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaElement;

/**
 * @author Babett Schalitz
 */
public class BackTrackHistory {

	private LinkedList history;
	private int historyPointer;

	public BackTrackHistory() {
		history = new LinkedList();
		historyPointer=0;
	}

	public int getSize() {
		return history.size();
	}
	
	public int getPointer(){
		return historyPointer;
	}

	public BackTrackStep getNextHistoryStep() {
		historyPointer++;
		return (BackTrackStep) history.get(historyPointer);
	}

	public BackTrackStep getLastHistoryStep() {
		historyPointer--;
		return (BackTrackStep) history.get(historyPointer);
	}
	
	public SynDiaElement getStepElem(int num){
	return ((BackTrackStep) history.get(num)).getElem();
	}

	public void addNewPosStep(Stack currentStack, SynDiaElement currentElem, String generatedWord) {
		BackTrackStep help = new BackTrackStep(currentStack, currentElem, generatedWord);
		//new Step
		if (historyPointer == history.size()) {
			history.add(help);
		} 
		if (historyPointer < history.size()) {
			//another forwardStep already exists, if they are not equal (new Way) remove the rest
			if (help.getElem().hashCode() ==(((BackTrackStep) history.get(historyPointer)).getElem()).hashCode()) {
				// everything stay in history
			}else{
				// go back until currentElement is Rep or Alt
				LinkedList newList = new LinkedList();
				for (int i = 0; i < historyPointer; i++) {
					newList.add(history.get(i));
				}
				history= newList;
				history.add(help);
			};
		}
		historyPointer++;
	}
}
