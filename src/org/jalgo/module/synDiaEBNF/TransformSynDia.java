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
 * Created on 20.06.2004
 */
 
package org.jalgo.module.synDiaEBNF;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.jalgo.module.synDiaEBNF.gfx.AlternativeFigure;
import org.jalgo.module.synDiaEBNF.gfx.ConcatenationFigure;
import org.jalgo.module.synDiaEBNF.gfx.EmptyFigure;
import org.jalgo.module.synDiaEBNF.gfx.InitialFigure;
import org.jalgo.module.synDiaEBNF.gfx.RepetitionFigure;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaSystemFigure;
import org.jalgo.module.synDiaEBNF.gfx.TerminalFigure;
import org.jalgo.module.synDiaEBNF.gfx.VariableFigure;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaAlternative;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaConcatenation;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaElement;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaEpsilon;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaInitial;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaRepetition;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaSystem;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaTerminal;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaVariable;

/**
 * @author Babett Schaliz
 */
public class TransformSynDia {

	private LinkedList synVariables= new LinkedList(); // List of SynDiaInitial
	private HashSet synVariablesSet= new HashSet(); // Set of Labels
	private HashSet terminalSymbols= new HashSet(); // Strings
	private Figure panel;
	private SynDiaSystem def;

	//	ruft Rekursion auf + textcanvas und Definition erstellen
	public TransformSynDia(Figure panel) {
		this.panel = panel;

		//get childs
		List list = panel.getChildren();
		def = new SynDiaSystem(new SynDiaSystemFigure(list));
		for (int i = 0; i < list.size(); i++) {
			// transform the parts of the diagram
			SynDiaInitial InitialElem =	transformSynDia((InitialFigure) list.get(i));
			def.addInitialDiagram(InitialElem);
		}
		
		// die Startelemente eines jeden Objektes 
		//noch rausfinden und in den SynVar setzen 
		for (int i = 0; i < def.getInitialDiagrams().size(); i++) {
			for (int j = 0; j < synVariables.size(); j++) {
				if (def.getInitialDiagram(i).getGfx().getLabel().equals(((SynDiaVariable) synVariables.get(j)).getLabel())) {
					((SynDiaVariable) synVariables.get(j)).setStartElem(def.getInitialDiagram(i));
				}
			}
		}
		
		//copy labels of the SynDiaVarialbes in the list into the HashSet
		for (int i = 0; i < synVariables.size(); i++) {
			synVariablesSet.add(
				((SynDiaVariable) synVariables.get(i)).getLabel());
		}

		// every partdiagram is performed
		def.setSynVariables(synVariablesSet);
		def.setTerminalSymbols(terminalSymbols);
	}

	public SynDiaSystem getSynDiaSystem() {
		return def;
	}

	private SynDiaElement searchTypAndTransform(Figure currentFigure) {
		if (currentFigure instanceof EmptyFigure) {
			return transformEmptyFigure((EmptyFigure) currentFigure);
		} else if (currentFigure instanceof TerminalFigure) {
			return transformTerminalFigure((TerminalFigure) currentFigure);
		} else if (currentFigure instanceof VariableFigure) {
			return transformSynDiaFigure((VariableFigure) currentFigure);
		} else if (currentFigure instanceof TerminalFigure) {
			return transformTerminalFigure((TerminalFigure) currentFigure);
		} else if (currentFigure instanceof RepetitionFigure) {
			return transformRepetitionFigure((RepetitionFigure) currentFigure);
		} else if (currentFigure instanceof AlternativeFigure) {
			return transformAlternativeFigure((AlternativeFigure) currentFigure);
		} else if (currentFigure instanceof ConcatenationFigure) {
			return transformConcatenationFigure((ConcatenationFigure) currentFigure);
		} return null;
	}

	private SynDiaInitial transformSynDia(InitialFigure diagram) {
		SynDiaElement startElem = searchTypAndTransform(diagram.getSynDia());

		SynDiaInitial elem = new SynDiaInitial(diagram, startElem);
		// set StartElement in Definition
		if (diagram.isStartFigure()) {
			this.def.setStartElem(elem);
		}
		
		// store the variables, ther could exsist Diagrams, which are as SynVar,
		// possible especially for the startdiagram
		synVariablesSet.add(elem.getGfx().getLabel());
		return elem;
	}

	private SynDiaRepetition transformRepetitionFigure(RepetitionFigure figure) {
		//TODO in geraden Tiefen eigentlich die Concatenationen umdrehen!
		
		SynDiaElement straightAheadElem = searchTypAndTransform(figure.getTopFigure());
		SynDiaElement repetedElem = searchTypAndTransform(figure.getBotFigure());

		return new SynDiaRepetition(figure, straightAheadElem, repetedElem);
	}

	private SynDiaAlternative transformAlternativeFigure(AlternativeFigure figure) {
		LinkedList inputList = figure.getInteriorFigures();
		LinkedList outputList = new LinkedList();

		for (int i = 0; i < inputList.size(); i++) {
			SynDiaElement listElem =
				searchTypAndTransform((Figure) inputList.get(i));
			outputList.add(listElem);
		}
		return new SynDiaAlternative(figure, outputList);
	}

	private SynDiaConcatenation transformConcatenationFigure(ConcatenationFigure figure) {
		LinkedList inputList = figure.getInteriorFigures();
		LinkedList outputList = new LinkedList();
		for (int i = 0; i < inputList.size(); i++) {
			SynDiaElement listElem =
				searchTypAndTransform((Figure) inputList.get(i));
			outputList.add(listElem);
		}
		return new SynDiaConcatenation(figure, outputList);
	}

	private SynDiaEpsilon transformEmptyFigure(EmptyFigure figure) {
		return new SynDiaEpsilon(figure);
	}

	private SynDiaTerminal transformTerminalFigure(TerminalFigure figure) {
		//store Label in the list
		terminalSymbols.add(figure.getLabel());
		// create new abstract Terminal
		return new SynDiaTerminal(figure);
	}

	private SynDiaVariable transformSynDiaFigure(VariableFigure figure) {
		// create new abstract Variable
		SynDiaVariable elem = new SynDiaVariable(figure);
		//store Variable in list
		synVariables.add(elem);
		return elem;
	}
}
