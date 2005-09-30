/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
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
 * Created on 17.05.2004
 */

package org.jalgo.module.synDiaEBNF.synDia;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.jalgo.module.synDiaEBNF.gfx.AlternativeFigure;

/**
 * @author Michael Pradel
 */
public class SynDiaAlternative
	extends SynDiaComposition
	implements Serializable {

	private static final long serialVersionUID = 9111449357455734662L;
	private List<SynDiaElement> options;
	private AlternativeFigure gfx;

	public SynDiaAlternative() {
		options = new LinkedList<SynDiaElement>();
	}

	public SynDiaAlternative(AlternativeFigure gfx, List<SynDiaElement> options) {
		this.options = options;
		this.gfx = gfx;
	}

	public int getNumOfOptions() {
		return options.size();
	}

	public List<SynDiaElement> getOptions() {
		return options;
	}

	public SynDiaElement getOption(int num) {
		return options.get(num);
	}

	public void setOption(int num, SynDiaElement newOption) {
		options.set(num, newOption);
	}

	public void setOptions(List<SynDiaElement> options) {
		this.options = options;
	}

	public void addOption(SynDiaElement option) {
		options.add(option);
	}

	public boolean removeOption(SynDiaElement optionToRemove) {
		return options.remove(optionToRemove);

	}

	public AlternativeFigure getGfx() {
		return gfx;
	}

	public void setGfx(AlternativeFigure figure) {
		gfx = figure;
	}
}