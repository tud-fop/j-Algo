/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

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
			}
		}
		historyPointer++;
	}
}
