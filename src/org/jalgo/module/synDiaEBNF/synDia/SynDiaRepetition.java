/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
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
	 * True if the straightAheadElem is already done in the Back Tracking
	 * Algorithm False else and by default
	 */
	private boolean straightAheadElemDone = false;

	public SynDiaRepetition() {

	}

	public SynDiaRepetition(RepetitionFigure gfx,
			SynDiaElement straightAheadElem, SynDiaElement repeatedElem) {
		this.gfx = gfx;
		this.straightAheadElem = straightAheadElem;
		this.repeatedElem = repeatedElem;
	}

	public SynDiaRepetition(SynDiaElement straightAheadElem,
			SynDiaElement repeatedElem) {
		this.straightAheadElem = straightAheadElem;
		this.repeatedElem = repeatedElem;
	}

	public SynDiaElement getStraightAheadElem() {
		return straightAheadElem;
	}

	public SynDiaElement getRepeatedElem() {
		return repeatedElem;
	}

	/**
	 * @return True if the straightAheadElem is already done in the Back
	 *               Tracking Algorithm False else and by default
	 */
	public boolean isStraightAheadElemDone() {
		return straightAheadElemDone;
	}

	/**
	 * @param straightAheadElemDone
	 *                   True if the straightAheadElem is already done in the Back
	 *                   Tracking Algorithm False else and by default
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