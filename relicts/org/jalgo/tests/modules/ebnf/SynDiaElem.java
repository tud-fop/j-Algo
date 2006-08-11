package org.jalgo.tests.ebnf;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.RoundRectangle2D;


import javax.swing.JPanel;

public abstract class SynDiaElem  extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int HEIGHT = 30;
	
	protected String strValue = "";
	protected Color color;
	protected int width;
	protected Font font;
	protected FontMetrics fm;
	
	public boolean transformed = false;

	public abstract String sayHello();
	public abstract String contentToString();
	protected abstract void update();
	
	protected int getLength() {
		
		return (int) Math.round(this.getSize().getWidth());
		
	}
	
	protected AlphaComposite makeComposite(float alpha) {
	    int type = AlphaComposite.SRC_OVER;
	    return(AlphaComposite.getInstance(type, alpha));
	}
	
	protected void setText(String text) {
		
		strValue = text;
		update();
		
		
	}
	
	
	
	protected String getText() {
		
		return strValue;
		
	}
	
	protected void setWidth(int w) {}
}
