/*
 * Created on 16.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gfx;

import java.util.LinkedList;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * Represents an abstact syntactical diagram, including its name and the diagram.
 * 
 * @author Marco Zimmerling
 * @author Michael Pradel
 * @author Cornelius Hald
 */
public class InitialFigure extends CompositeSynDiaFigure {

	private ConcatenationFigure concat;
	private Label label;
	
	private boolean startFigure; // auszeichnung, das dieses Startdiagramm

	public InitialFigure(String label) {
		ToolbarLayout tbl = new ToolbarLayout(true);
		tbl.setSpacing(10);
		setLayoutManager(tbl);
		concat = new ConcatenationFigure(1);
		this.label = new Label(label);
		add(this.label);
		add(concat);
	
		startFigure = false;
	}

	public String getLabel() {
		return label.getText();
	}

	public void setLabel(String label) {
		this.label.setText(label);
	}

	public SynDiaFigure getSynDia() {
		return (SynDiaFigure) concat.getInteriorFigures().get(0);
	}

	public void setStartFigure() {
		startFigure = true;
	}

	public boolean isStartFigure() {
		return startFigure;
	}

	public void replace(SynDiaFigure newFigure, int index) {
		concat.replace(newFigure, index);
	}

	public void replace(SynDiaFigure oldFigure, SynDiaFigure newFigure) {
		concat.replace(oldFigure, newFigure);
		}

	public void reposition() {
		// do nothing, because InitialFigure has no parent
	}

	public void setStartGap(int startGap) {
		concat.setStartGap(startGap);
	}

	public void setEndGap(int endGap) {
		concat.setEndGap(endGap);
	}

	public Dimension getPreferredSize(int arg0, int arg1) {
		return concat.getPreferredSize(arg0, arg1);
	}

	public LinkedList getInteriorFigures() {
		return concat.getInteriorFigures();
	}

	public void highlightConnectionTo(
		SynDiaFigure targetFigure,
		boolean selection)
		throws SynDiaException {

	}

	public void highlightConnectionTo(int index, boolean selection)
		throws SynDiaException {

	}

	public void highlightConnectionFrom(
		SynDiaFigure sourceFigure,
		boolean selection)
		throws SynDiaException {

	}

	public void highlightConnectionFrom(int index, boolean selection)
		throws SynDiaException {

	}
	
	public void remove(int index) throws SynDiaException {
		
	}
	
	public void highlight(boolean highlight) {
		
	}
}
