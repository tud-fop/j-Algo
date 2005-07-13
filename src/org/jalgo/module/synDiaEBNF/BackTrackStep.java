/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and platform
 * independant. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/*
 * Created on 01.05.2004
 */

package org.jalgo.module.synDiaEBNF;

import java.io.Serializable;
import java.util.Stack;

import org.jalgo.module.synDiaEBNF.synDia.SynDiaElement;

/**
 * This class is a data structure which represents a step in the backtracking
 * algorithm.
 * 
 * @author Babett Schalitz
 */
public class BackTrackStep
implements Serializable {

	private static final long serialVersionUID = -315723239856067064L;

	// the Stack configuration of this step
	private Stack currentStack;

	// the Element which is worked with in this step
	private SynDiaElement currentElem;

	private String generatedWord;

	/**
	 * @param currentStack The Stack Configuration of this step
	 */
	public BackTrackStep(Stack currentStack, SynDiaElement currentElem,
		String generatedWord) {
		this.currentStack = currentStack;
		this.currentElem = currentElem;
		this.generatedWord = generatedWord;
	}

	/**
	 * @param currentElem The Element which correspondent to the Step
	 */
	public void setElem(SynDiaElement currentElem) {
		this.currentElem = currentElem;
	}

	public SynDiaElement getElem() {
		return currentElem;
	}

	/**
	 * @return The Stack Configuration of this step
	 */
	public Stack getStackConfig() {
		return currentStack;
	}

	public void setStackConfig(Stack currentStack) {
		this.currentStack = currentStack;
	}

	public String getGeneratedWord() {
		return generatedWord;
	}

	public void setGeneratedWord(String generatedWord) {
		this.generatedWord = generatedWord;
	}
}
