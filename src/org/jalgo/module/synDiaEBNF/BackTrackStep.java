/*
 * Created on 01.05.2004
 */
 
package org.jalgo.module.synDiaEBNF;

import java.io.Serializable;

import org.jalgo.main.util.Stack;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaElement;

/**
 * This class is a Data Structure which represents a step in the Backtracking Algorithm.
 *
 * @author Babett Schalitz
 */
public class BackTrackStep implements Serializable {

	private Stack currentStack; // the Stack configuration of this step
	private SynDiaElement currentElem; //the Element which is worked with in this step
	private String generatedWord;

	/**
	* @param currentStack  The Stack Configuration of this step 
	*/
	public BackTrackStep(Stack currentStack,SynDiaElement currentElem,String generatedWord) { 
		this.currentStack=currentStack;
		this.currentElem=currentElem;
		this.generatedWord=generatedWord;
	}

	/**
	 * @param currentElem	The Element which correspondent to the Step
	 */
	public void setElem(SynDiaElement currentElem) {
		this.currentElem = currentElem;
	}
	
	public SynDiaElement getElem() {
		return currentElem;
	}
	/**
	 * @return 				The Stack Configuration of this step
	 */
	public Stack getStackConfig() {
		return currentStack;
	}
		
	/**
	 * @param currentElem	The Element which correspondent to the Step
	 */
	public void setStackConfig(Stack currentStack) {
		this.currentStack = currentStack;
	}
	
	public String getGeneratedWord(){
		return generatedWord;
	}
	
	public void setGeneratedWord(String generatedWord){
		this.generatedWord=generatedWord;
	}
}
	
