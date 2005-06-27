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

package org.jalgo.main.gui;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;


public class ZoomPanel extends Figure {

	private float zoom;

	public float getZoom() {
		return zoom;
	}

	public Rectangle getClientArea(Rectangle rect) {
		super.getClientArea(rect);
		rect.width /= zoom;
		rect.height /= zoom;
		return rect;
	}

	public Dimension getPreferredSize(int wHint, int hHint) {
		Dimension d = super.getPreferredSize(wHint, hHint);
		int w = getInsets().getWidth();
		int h = getInsets().getHeight();
		return d.scale(zoom).expand(w, h);
	}

	protected void paintClientArea(Graphics graphics) {
		if (getChildren().isEmpty())
			return;

		boolean optimizeClip = getBorder() == null || getBorder().isOpaque();
		ScaledGraphics g = new ScaledGraphics(graphics);

		
		if (!optimizeClip)
			g.clipRect(getBounds().getCropped(getInsets()));
		g.translate(
			getBounds().x + getInsets().left,
			getBounds().y + getInsets().top);
		g.scale(zoom);
		g.pushState();
		paintChildren(g);
		g.popState();
		g.dispose();
		
		graphics.restoreState();
	
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
		this.revalidate();
	}

	public void translateToParent(Translatable t) {
		t.performScale(zoom);
		super.translateToParent(t);
	}
	public void translateFromParent(Translatable t) {
		super.translateFromParent(t);
		t.performScale(1 / zoom);
	}

	protected boolean useLocalCoordinates() {
		return false;
	}
}