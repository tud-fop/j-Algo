/*
 * Created on 17.05.2004
 */
 
package org.jalgo.module.synDiaEBNF.synDia;

import java.io.Serializable;

import org.jalgo.module.synDiaEBNF.gfx.RepetitionFigure;

/**
 * @author Michael Pradel
 */
public class SynDiaRepetition extends SynDiaComposition implements Serializable {

	private SynDiaElement straightAheadElem;
	private SynDiaElement repeatedElem;
	private RepetitionFigure gfx;

	/**	
	 * True  if the straightAheadElem is already done in the Back Tracking Algorithm
	 * False else and by default 
	 */
	private boolean straightAheadElemDone = false;  

	public SynDiaRepetition() {
		
	}

	public SynDiaRepetition(RepetitionFigure gfx, SynDiaElement straightAheadElem, SynDiaElement repeatedElem){
		this.gfx=gfx;
		this.straightAheadElem = straightAheadElem;
		this.repeatedElem =repeatedElem;
	}


	public SynDiaRepetition(SynDiaElement straightAheadElem, SynDiaElement repeatedElem){
		this.straightAheadElem = straightAheadElem;
		this.repeatedElem =repeatedElem;
	}

	public SynDiaElement getStraightAheadElem() {
		return straightAheadElem;
	}
	
	public SynDiaElement getRepeatedElem() {
		return repeatedElem;
	}


	/**
	 * @return True  if the straightAheadElem is already done in the Back Tracking Algorithm
	 *         False else and by default
	 */
	public boolean isStraightAheadElemDone() {
		return straightAheadElemDone;
	}

	/**
	 * @param straightAheadElemDone True  if the straightAheadElem is already done 
	 *                                    in the Back Tracking Algorithm
	 *                              False else and by default
	 */
	public void setStraightAheadElemDone(boolean straightAheadElemDone) {
		this.straightAheadElemDone = straightAheadElemDone;
	}

	/**
	 * @param repeatedElem
	 */
	public void setRepeatedElem(SynDiaElement repeatedElem) {
		this.repeatedElem = repeatedElem;
	}

	/**
	 * @param straightAheadElem
	 */
	public void setStraightAheadElem(SynDiaElement straightAheadElem) {
		this.straightAheadElem = straightAheadElem;
	}

	/**
	 * @return
	 */
	public RepetitionFigure getGfx() {
		return gfx;
	}

	/**
	 * @param figure
	 */
	public void setGfx(RepetitionFigure figure) {
		gfx = figure;
	}

}
