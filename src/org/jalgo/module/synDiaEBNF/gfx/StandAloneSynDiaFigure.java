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

	private String label;
	private boolean isTerminal;
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
		this.isTerminal = isTerminal;
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

