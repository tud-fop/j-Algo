package org.jalgo.module.app.view.graph;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.GeneralPath;


public class UnderliningHighlightPainter extends ShapeHighlightPainter {
	
		public UnderliningHighlightPainter(Color lineColor) {
			this.lineColor = lineColor;
			this.hasBorder = true;
			this.hasFilling = false;
			this.strokeWidth = 1;
		}
		
		protected Shape calculateShape(int x, int y, int length, int height, int fontSize) {
			GeneralPath underline;
			int step, wave;
			
			step = fontSize / 6;
			wave = fontSize / 8;
			y += wave + fontSize;
			
			underline = new GeneralPath();
			
			underline.moveTo(x, y);
			
			for (int i = 0; i < length / step; i ++, x += step) {
				int sign;
				
				if ((i % 2) == 0)
					sign = -1;
				else
					sign = 1;
									
				underline.quadTo(x + wave, y + (wave * sign), x + step, y);
			}
			
			return underline;
		}
		
	}