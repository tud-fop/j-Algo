/*
 * Created on 10.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gfx;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.jalgo.main.gfx.FixPointAnchor;

/**
 * @author Michael Pradel
 * @author Marco Zimmerling
 * @author Cornelius Hald
 */
public class EmptyFigure extends SynDiaFigure {

	public EmptyFigure() {
		
		// Add a inner connection cause width is >= 2
		PolylineConnection con = new PolylineConnection();
		con.setSourceAnchor(new FixPointAnchor(this, SWT.LEFT));
		con.setTargetAnchor(new FixPointAnchor(this, SWT.RIGHT));
		add(con);
	}

	public FixPointAnchor getLeftAnchor() {
		return new FixPointAnchor(this, SWT.LEFT);
	}

	public FixPointAnchor getRightAnchor() {
		return new FixPointAnchor(this, SWT.RIGHT);
	}

	/**
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int arg0, int arg1) {
		if ((startGap + endGap) < 2 ) {
			return new Dimension(2, 36);
		} else {
			return new Dimension(startGap + endGap, 36);
		}
	}

	/**
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#setStartGap(int)
	 */
	public void setStartGap(int startGap) {
		this.startGap = startGap;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#setEndGap(int)
	 */
	public void setEndGap(int endGap) {
		this.endGap = endGap;
	}

	public void highlight(boolean highlight) {
	}
}
