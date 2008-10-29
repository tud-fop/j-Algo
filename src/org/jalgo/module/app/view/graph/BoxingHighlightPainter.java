package org.jalgo.module.app.view.graph;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

public class BoxingHighlightPainter extends ShapeHighlightPainter {
	
	public BoxingHighlightPainter(Color lineColor, Color fillColor) {
		this.lineColor = Color.WHITE; //lineColor;
		this.fillColor = fillColor;
		this.hasBorder = true;
		this.hasFilling = true;
		this.strokeWidth = 2;
	}
	
	protected Shape calculateShape(int x, int y, int length, int height, int fontSize) {
		RoundRectangle2D box;
		
		box = new RoundRectangle2D.Float((float)x, (float)y, (float)length, (float)height, 8f, 8f);
		
		return box;
	}

}
