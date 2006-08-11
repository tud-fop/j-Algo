package org.jalgo.tests.ebnf;

import java.awt.*;
import java.awt.geom.*;


public class Variable extends SynDiaElem {

	private static final long serialVersionUID = 1L;

	protected Rectangle2D.Double rect;
	
	
	
	
	private int width;
	private Font font;
	

	public Variable(String val) {
		
		font = new Font("Courier", Font.BOLD, 17);
		
		this.strValue = val;
		
		
		fm = getFontMetrics(font); 
		width = fm.stringWidth(strValue) + 15;
				
		this.color = new Color(0, 200, 0);
				
		this.setSize(width+5, HEIGHT+5);
		rect = new Rectangle2D.Double(0, 0, width, HEIGHT);
		

		this.setEnabled(true);
	}

	public void update() {
		
		width = fm.stringWidth(strValue) + 15;
		
		if (width<HEIGHT) width = HEIGHT;
		
		this.setSize(width+5, HEIGHT+5);
	
		rect = new Rectangle2D.Double(0, 0, width, HEIGHT);
		
		repaint();
		
		
	}
	
	
	
	public void paintComponent(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));
		
		g2d.setColor(Color.BLACK);
		rect.x += 3;
		rect.y += 3;
		
		g2d.setComposite(makeComposite(0.4F));
		g2d.fill(rect);
		
		rect.x -= 3;
		rect.y -= 3;
		
		g2d.setComposite(makeComposite(1F));
		
		
		g2d.setColor(Color.WHITE);
		g2d.fill(rect);
		g2d.setColor(Color.BLACK);
		g2d.setFont(font);

		g2d.drawString(strValue,(int) rect.getX() + 9, (int) rect.getY() + 20);
		g2d.draw(rect);
	}

	public boolean contains(Point p) {
		return rect.contains(p.getX(), p.getY());
	}
	
	public String sayHello() {
		
		return "\t\t"+strValue;
		
	}
	public String contentToString() {
		
		return strValue;
		
	}
	

}
