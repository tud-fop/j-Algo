/*
 * Created on Jun 17, 2004
 */
package org.jalgo.module.synDiaEBNF.gfx;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jalgo.main.gfx.RoundedManhattanConnectionRouter;

/**
 * @author Cornelius Hald
 * @author Marco Zimmerling
 */
public class RepetitionFigure extends CompositeSynDiaFigure {

	private final int hSpacing = 30;
	private final int vSpacing = 15;
	private final int minNumOfInteriorFigures = 2;
	private SynDiaFigure topFigure, botFigure;
	private PolylineConnection toTop, fromTop, topToBot, botToTop;

	public RepetitionFigure() {

		// Always exactly 2 interiorFigures
		numOfInteriorFigures = 2;
		startGap = endGap = 0;

		initializeLayout();

		// Create the 4 main Figures
		startFigure = new EmptyFigure();
		add(startFigure);

		topFigure = new CloudFigure();
		add(topFigure);

		endFigure = new EmptyFigure();
		add(endFigure);

		botFigure = new CloudFigure();
		add(botFigure);

		// Add Connections
		toTop = new PolylineConnection();
		toTop.setConnectionRouter(new RoundedManhattanConnectionRouter());
		add(toTop);

		fromTop = new PolylineConnection();
		fromTop.setConnectionRouter(new RoundedManhattanConnectionRouter());
		add(fromTop);

		topToBot = new PolylineConnection();
		topToBot.setConnectionRouter(new RoundedManhattanConnectionRouter());
		add(topToBot);

		botToTop = new PolylineConnection();
		botToTop.setConnectionRouter(new RoundedManhattanConnectionRouter());
		add(botToTop);

		// Layout Figures and Connections
		reposition();

		// Add Figures to Collection
		interiorFigures.add(topFigure);
		interiorFigures.add(botFigure);

		// Add Connections to Collection
		connectionsToInteriorFigures.add(toTop);
		connectionsToInteriorFigures.add(topToBot);
		connectionsFromInteriorFigures.add(fromTop);
		connectionsFromInteriorFigures.add(botToTop);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int w, int h) {
		int width =
			startFigure.getPreferredSize().width
				+ startGap
				+ 2 * hSpacing
				+ Math.max(
					topFigure.getPreferredSize().width,
					botFigure.getPreferredSize().width
						+ endFigure.getPreferredSize().width)
				+ endGap;

		int height =
			vSpacing
				+ topFigure.getPreferredSize().height
				+ botFigure.getPreferredSize().height
				+ 2;

		return new Dimension(width, height);
	}

	public void replace(SynDiaFigure oldFigure, SynDiaFigure newFigure)
		throws SynDiaException {

		if (oldFigure.equals(topFigure)) {
			replace(newFigure, 0);
		} else if (oldFigure.equals(botFigure)) {
			replace(newFigure, 1);
		} else {
			throw new SynDiaException("The Figure you're trying to replace is not known."); //$NON-NLS-1$
		}
	}

	public void replace(SynDiaFigure newFigure, int index)
		throws SynDiaException {

		if (index == 0) {
			remove(topFigure);
			add(newFigure);
			topFigure = newFigure;

		} else if (index == 1) {
			remove(botFigure);
			add(newFigure);
			botFigure = newFigure;
		} else {
			throw new SynDiaException(
				"Index must be 0 or 1. Not " + index + " as you tried."); //$NON-NLS-1$ //$NON-NLS-2$
		}
		reposition();
	}

	/**
	 * This method is responsible for the whole visual layout - Call it to get everything right.
	 * It sets the positions of the sub-Figures and connects the Connections.
	 */
	public void reposition() {
		setPositions();
		// Recursive reposition of parents
		if (this.getParent() instanceof CompositeSynDiaFigure) {
			((CompositeSynDiaFigure) this.getParent()).reposition();
		}
	}

	private void setPositions() {
		// Initial constraints
		Rectangle startFigureRect = new Rectangle(0, 0, -1, -1);
		Rectangle topFigureRect = new Rectangle(0, 0, -1, -1);
		Rectangle botFigureRect = new Rectangle(0, 0, -1, -1);
		Rectangle endFigureRect = new Rectangle(0, 0, -1, -1);

		// Set X-Position
		// Check which Figure is wider then center the smaller one on the larger one.
		if (topFigure.getPreferredSize().width
			>= botFigure.getPreferredSize().width) {
			// topFigure is wider
			topFigureRect.x = hSpacing + startGap;
			botFigureRect.x =
				topFigureRect.x
					+ (topFigure.getPreferredSize().width
						- botFigure.getPreferredSize().width)
						/ 2;
			// set Position of endFigure
			endFigureRect.x =
				topFigureRect.x
					+ topFigure.getPreferredSize().width
					+ hSpacing
					+ endGap;
		} else {
			// botFigure is wider
			botFigureRect.x = hSpacing + startGap;
			topFigureRect.x =
				botFigureRect.x
					+ (botFigure.getPreferredSize().width
						- topFigure.getPreferredSize().width)
						/ 2;
			// set Position of endFigure
			endFigureRect.x =
				botFigureRect.x
					+ botFigure.getPreferredSize().width
					+ hSpacing
					+ endGap;
		}

		// Set Y-Position
		botFigureRect.y = topFigure.getPreferredSize().height + vSpacing;

		// Set Layout
		layout.setConstraint(startFigure, startFigureRect);
		layout.setConstraint(topFigure, topFigureRect);
		layout.setConstraint(botFigure, botFigureRect);
		layout.setConstraint(endFigure, endFigureRect);

		// Reset AnchorPoints
		toTop.setSourceAnchor(startFigure.getRightAnchor());
		toTop.setTargetAnchor(topFigure.getLeftAnchor());

		fromTop.setSourceAnchor(topFigure.getRightAnchor());
		fromTop.setTargetAnchor(endFigure.getLeftAnchor());

		topToBot.setSourceAnchor(topFigure.getRightAnchor());
		topToBot.setTargetAnchor(botFigure.getRightAnchor());

		botToTop.setSourceAnchor(botFigure.getLeftAnchor());
		botToTop.setTargetAnchor(topFigure.getLeftAnchor());

	}

	public void remove(SynDiaFigure figureToRemove) throws SynDiaException {
		super.remove(figureToRemove);

	}

	public void remove(int index) throws SynDiaException {
		// TODO Implement removing of interior Figures

	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#setStartGap(int)
	 */
	public void setStartGap(int startGap) {
		this.startGap = startGap;
		setPositions();
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#setEndGap(int)
	 */
	public void setEndGap(int endGap) {
		this.endGap = endGap;
		setPositions();
	}

	/**
	 * @return
	 */
	public SynDiaFigure getBotFigure() {
		return botFigure;
	}

	/**
	 * @return
	 */
	public SynDiaFigure getTopFigure() {
		return topFigure;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#highlight(boolean)
	 */
	public void highlight(boolean highlight) {
	}

}
