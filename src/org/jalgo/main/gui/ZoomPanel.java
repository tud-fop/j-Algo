package org.jalgo.main.gui;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;


public class ZoomPanel extends Figure {

	{
	}

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