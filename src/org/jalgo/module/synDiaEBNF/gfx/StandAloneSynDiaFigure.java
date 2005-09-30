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
 * Created on 15.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gfx;

import org.eclipse.draw2d.PolylineConnection;

/**
 * Because of many similarities between the grafical representation of a syntactical variable (VariableFigure) and
 * a terminal symbol (TerminalFigure) this class implements all functionalities for those figures. It is not possible to
 * instantiate this class which wouldn't make sense since there should be a VariableFigure and a TerminalFigure in
 * order to provide objects according to the abstract ones.  
 * 
 * @author Marco Zimmerling
 */
public class StandAloneSynDiaFigure extends SynDiaFigure {

	private static final long serialVersionUID = 1932335076118213879L;
	private String label;
	private PolylineConnection incomingConnection;
	private PolylineConnection exitingConnection;

	/**
	 * Constructor which will only be executed by TerminalFigure and VariableFigure (protected) in order to
	 * assure that there is no instace of this class (see remark above).
	 * 
	 * @param label					text which will appear inside the figure
	 * @param isTerminal		true in case of a TerminalFigure, false in case of a VariableFigure
	 */
	protected StandAloneSynDiaFigure(String label, boolean isTerminal) {
		this.label = label;
	}

	/**
	 * Returns the text inside the figure.
	 * 
	 * @return		text inside the figure
	 */
	public final String getLabel() {
		return label;
	}

	/**
	 * Highlights the incoming connection (from startFigure to the figure's border) by
	 * dyeing with SynDiaColors.figureAlongPath.
	 * 
	 * @param highlight		true to highlight, false to reset
	 */
	public void highlightIncomingConnection(boolean highlight) {
		if (highlight)
			incomingConnection.setForegroundColor(SynDiaColors.figureAlongPath);
		else
			incomingConnection.setForegroundColor(SynDiaColors.figureAlongPath);
	}

	/**
	 * Highlights the exiting connection (from the figure's border to endFigure) by
	 * dyeing with SynDiaColors.figureAlongPath.
	 * 
	 * @param highlight		true to highlight, false to reset
	 */
	public void highlightExitingConnection(boolean highlight) {
		if (highlight)
			exitingConnection.setForegroundColor(SynDiaColors.figureAlongPath);
		else
			exitingConnection.setForegroundColor(SynDiaColors.figureAlongPath);
	}

	/**
	 * Removes the entire figure.
	 */
	public void remove() throws SynDiaException {
		((CompositeSynDiaFigure) getParent()).remove(this);
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#setStartGap(int)
	 */
	public void setStartGap(int startGap) {
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#setEndGap(int)
	 */
	public void setEndGap(int endGap) {
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#highlight(boolean)
	 */
	public void highlight(boolean highlight) {
		if (highlight) {
			setForegroundColor(SynDiaColors.highlightEntireFigure);
		} else {
			setForegroundColor(SynDiaColors.normal);
		}
	}
}

