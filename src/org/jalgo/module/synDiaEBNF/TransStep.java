/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
 * Created on 03.05.2004
 */
 
package org.jalgo.module.synDiaEBNF;

import java.io.Serializable;

import org.jalgo.module.synDiaEBNF.synDia.SynDiaElement;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaToTrans;

/**
 * This class is a Data Structure which represents a step in the Trans Algorithm.
 * 
 * @author Michael Pradel
 * @author Benjamin Scholz
 */
public class TransStep implements Serializable{
	private static final long serialVersionUID = -5680214355111699453L;
	
	private String textMarker;
	// the position of the Text which is marked in this Step
	private Object transformed;
	// EbnfElement, which was selected by the user (or jAlgo) in this step to be transformed
	private SynDiaElement elemInFrontOfTransformed;
	// element of syntactical diagramm, which is placed in front of the element "transformed" (see above)
	private SynDiaElement currentToTransSynDiaElem;
	// points the EbnfSynVariable from toTransVariables which was performed at the history-moment

	public TransStep() {
		this(null, null, null, null);
	}
	/**
	 * @param transformed SynDiaToTrans-element, which was selected by the user 
	 *                    (or jAlgo) in this step to be transformed
	 */
	public TransStep(SynDiaToTrans transformed) {
		this(transformed, null, null, null);
	}

	/**
	 * @param transformed              SynDiaToTrans-element, which was selected by 
	 *                                 the user (or jAlgo) in this step to be 
	 *                                 transformed
	 * @param elemInFrontOfTransformed element of syntactical diagram, which is 
	 *                                 placed in front of the element "transformed" 
	 */
	public TransStep(
		SynDiaToTrans transformed,
		SynDiaElement elemInFrontOfTransformed) {
		this(transformed, elemInFrontOfTransformed, null, null);
	}

	/**
	 * @param transformed              SynDiaToTrans-element, which was selected by 
	 *                                 the user (or jAlgo) in this step to be 
	 *                                 transformed
	 * @param elemInFrontOfTransformed element of syntactical diagram, which is 
	 *                                 placed in front of the element "transformed" 
	 * @param textMarker               the position of the Text which is marked 
	 *                                 in this Step
	 */
	public TransStep(
		SynDiaToTrans transformed,
		SynDiaElement elemInFrontOfTransformed,
		String textMarker) {
		this(transformed, elemInFrontOfTransformed, textMarker, null);
	}

	/**
	 * @param transformed              SynDiaToTrans-element, which was selected by 
	 *                                 the user (or jAlgo) in this step to be 
	 *                                 transformed
	 * @param elemInFrontOfTransformed element of syntactical diagramm, which is 
	 *                                 placed in front of the element "transformed" 
	 * @param textMarker               the position of the Text which is marked 
	 *                                 in this Step
	 * @param currentToTransSynDiaElem   points the EbnfSynVariable from 
	 *                                 toTransVariables which was performed at 
	 *                                 the history-moment
	 */
	public TransStep(
		SynDiaToTrans transformed,
		SynDiaElement elemInFrontOfTransformed,
		String textMarker,
		SynDiaElement currentToTransSynDiaElem) {
		this.transformed = transformed;
		this.elemInFrontOfTransformed = elemInFrontOfTransformed;
		this.textMarker = textMarker;
		this.currentToTransSynDiaElem = currentToTransSynDiaElem;
	}

	/**
	 * @param transformed the EbnfElement which is transformed in this Step
	 */
	public void setTransformed(Object transformed) {
		this.transformed = transformed;
	}

	/**
	 * @param elemInFrontOfTransformed the EbnfElement which is in Front of the 
	 *                                 EbnfElement which is transformed in this Step
	 */
	public void setElemInFrontOfTransformed(SynDiaElement elemInFrontOfTransformed) {
		this.elemInFrontOfTransformed = elemInFrontOfTransformed;
	}

	/**
	 * @param textMarker the position of the Text which is marked in this Step
	 */
	public void setTextMarker(String textMarker) {
		this.textMarker = textMarker;
	}

	/**
	 * @param currentToTransSynDiaElem points to the ToTransElem which is 
	 *                               transformed in this Step 
	 */
	public void setCurrentToTransSynDiaElem(SynDiaElement currentToTransSynDiaElem) {
		this.currentToTransSynDiaElem = currentToTransSynDiaElem;
	}

	/**
	 * @return the EbnfElement which is transformed in this Step
	 */
	public Object getTransformed() {
		return transformed;
	}

	/**
	 * @return the EbnfElement which is in Front of the EbnfElement which is transformed 
	 *         in this Step
	 */
	public SynDiaElement getElemInFrontOfTransformed() {
		return elemInFrontOfTransformed;
	}

	/**
	 * @return the position of the Text which is marked in this Step 
	 */
	public String getTextMarker() {
		return textMarker;
	}

	/**
	 * @return points to the ToTransElem which is transformed in this Step
	 */
	public SynDiaElement getCurrentToTransSynDiaElem() {
		return currentToTransSynDiaElem;
	}
}