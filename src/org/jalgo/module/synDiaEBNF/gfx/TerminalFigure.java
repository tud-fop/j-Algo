/*
 * Created on 15.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gfx;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.jalgo.main.gfx.EllipseLabel;
import org.jalgo.main.gfx.FixPointAnchor;

/**
 * Grafical representation of a terminal symbol.
 * 
 * @author Marco Zimmerling
 * @author Cornelius Hald
 */
public class TerminalFigure extends StandAloneSynDiaFigure {

	private EllipseLabel interiorFigure;
	private EmptyFigure startFigure, endFigure;

	/**
	 * Constructor that creates a circular figure labeled with label.
	 * 
	 * @param label		terminal symbol
	 */
	public TerminalFigure(String label, Font font) {
		super(label, true);

		// StackLayout acts like a FillLayout in SWT
		initializeLayout();
		startGap = endGap = 0;

		// Create startFigure
		startFigure = new EmptyFigure();
		add(startFigure);

		// Create interiorFigure
		interiorFigure = new EllipseLabel();
		interiorFigure.setFont(font);
		add(interiorFigure);
		interiorFigure.setText(label);
		interiorFigure.setIndexVisible(false);

		// Create endFigure
		endFigure = new EmptyFigure();
		add(endFigure);

		// Create Connections
		PolylineConnection toIn = new PolylineConnection();
		toIn.setSourceAnchor(startFigure.getRightAnchor());
		toIn.setTargetAnchor(new FixPointAnchor(interiorFigure, SWT.LEFT));
		add(toIn);

		PolylineConnection toOut = new PolylineConnection();
		toOut.setSourceAnchor(new FixPointAnchor(interiorFigure, SWT.RIGHT));
		toOut.setTargetAnchor(endFigure.getLeftAnchor());
		add(toOut);

		reposition();
	}

	/*
	 * TODO: Remove, as soon as all classes are migrated to the new construcor
	 */
	public TerminalFigure(String label) {
		super(label, true);

		// StackLayout acts like a FillLayout in SWT
		setLayoutManager(new StackLayout());
		startGap = endGap = 0;

		startFigure = new EmptyFigure();
		add(startFigure);

		interiorFigure = new EllipseLabel();
		//ell.setFont(font);
		add(interiorFigure);
		interiorFigure.setText(label);

		endFigure = new EmptyFigure();
		add(endFigure);

		reposition();
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#getLeftAnchor()
	 */
	public FixPointAnchor getLeftAnchor() {
		return new FixPointAnchor(this, SWT.LEFT);
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#getRightAnchor()
	 */
	public FixPointAnchor getRightAnchor() {
		return new FixPointAnchor(this, SWT.RIGHT);
	}

	// methods from EllipseLabel:
	/**
	 * @return The text from the mainLabel
	 */
	public String getText() {
		return interiorFigure.getText();
	}

	public void setTextBackground(Color color) {
		interiorFigure.setTextBackground(color);
	}

	/**
	 * @param bool Enable or disable a line under the mainLabel
	 */
	public void setTextUnderline(boolean bool) {
		interiorFigure.setTextUnderline(bool);
	}

	public void setFont(Font font) {
		interiorFigure.setFont(font);
	}

	/**
	 * Set text on the indexLabel
	 * @param text
	 */
	public void setIndexText(String text) {
		interiorFigure.setIndexText(text);
	}

	public void setStartGap(int startGap) {
		this.startGap = startGap;
		reposition();
	}

	public void setEndGap(int endGap) {
		this.endGap = endGap;
		reposition();
	}

	private void reposition() {
		int x = 0;
		layout.setConstraint(startFigure, new Rectangle(x, 0, -1, -1));
		x = startFigure.getPreferredSize().width + startGap;
		layout.setConstraint(interiorFigure, new Rectangle(x, 0, -1, -1));
		x += interiorFigure.getPreferredSize().width + endGap;
		layout.setConstraint(endFigure, new Rectangle(x, 0, -1, -1));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int arg0, int arg1) {

		int width =
			startFigure.getPreferredSize().width
				+ startGap
				+ interiorFigure.getPreferredSize().width
				+ endGap
				+ endFigure.getPreferredSize().width
				+ 4;

		int heigth = interiorFigure.getPreferredSize().height;

		return new Dimension(width, heigth);
	}

	public void setIndexVisible(boolean visible) {
		interiorFigure.setIndexVisible(visible);
	}

	public void highlight(boolean highlight) {
		if (highlight) {
			interiorFigure.setForegroundColor(
				SynDiaColors.highlightEntireFigure);
			interiorFigure.getMainLabel().setForegroundColor(
				SynDiaColors.normal);
			interiorFigure.getIndexLabel().setForegroundColor(
				SynDiaColors.normal);
		} else {
			interiorFigure.setForegroundColor(SynDiaColors.normal);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#setBackgroundColor(org.eclipse.swt.graphics.Color)
	 */
	public void setBackgroundColor(Color arg0) {
		interiorFigure.setBackgroundColor(arg0);
	}
}
