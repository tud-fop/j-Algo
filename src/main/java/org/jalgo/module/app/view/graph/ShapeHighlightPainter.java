package org.jalgo.module.app.view.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

abstract class ShapeHighlightPainter implements Highlighter.HighlightPainter {

	protected Color lineColor, fillColor;
	protected boolean hasBorder, hasFilling;
	protected int strokeWidth;
	
	protected abstract Shape calculateShape(int x, int y, int length, int height, int fontSize);
	
	public void paint(Graphics output, int startOffset, int endOffset, Shape bounds, JTextComponent component) {
		Rectangle startRect, endRect;
		Shape shape;
		Graphics2D g;
		
		g = (Graphics2D)output;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						   RenderingHints.VALUE_ANTIALIAS_ON
						  );
		
		// Paint underline
		try {
			startRect = component.modelToView(startOffset);
			endRect = component.modelToView(endOffset);
		}
		 catch (BadLocationException e) {
			 return;
		}
		
		shape = calculateShape((int)startRect.getX(), 
						 	   (int)startRect.getY(), 
							   (int)(endRect.getX() + endRect.getWidth() - startRect.getX()),
							   (int)(endRect.getHeight()),
							   component.getFont().getSize()
							  );

		g.setStroke(new BasicStroke(strokeWidth));
		
		if (hasBorder) {
			g.setColor(lineColor);
			g.draw(shape);
		}
		
		if (hasFilling) {			
			g.setColor(fillColor);
			g.fill(shape);
		}
	}			
	
}
