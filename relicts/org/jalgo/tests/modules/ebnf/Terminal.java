package org.jalgo.tests.ebnf;

import java.awt.*;
import java.awt.geom.*;

public class Terminal extends SynDiaElem {

	private static final long serialVersionUID = 1L;

	protected RoundRectangle2D.Double kreis;
	
	
	
	public Terminal(String val) {

		this.strValue = val;
		
		//this.color = new Color(0, 200, 0);
		font = new Font("Courier", Font.BOLD, 17);
		
		fm = getFontMetrics(font); 
		width = fm.stringWidth(strValue) + 15;
		
		
		if (width<HEIGHT) width = HEIGHT;
		
		
		//el = new Ellipse2D.Double(10, 10, 10, 10);
		this.setSize(width+5, HEIGHT+5);
		this.setEnabled(true);
		
		kreis = new RoundRectangle2D.Double(0, 0, width, HEIGHT, HEIGHT, HEIGHT);
		
	}
	
	public void update() {
		
		width = fm.stringWidth(strValue) + 15;
		
		if (width<HEIGHT) width = HEIGHT;
		
		this.setSize(width+5, HEIGHT+5);
				
		kreis = new RoundRectangle2D.Double(0, 0, width, HEIGHT, HEIGHT, HEIGHT);
		
		repaint();
		
		
	}
	
	
	public void paintComponent(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));
		
		g2d.setColor(Color.BLACK);
		kreis.x += 3;
		kreis.y += 3;
		
		g2d.setComposite(makeComposite(0.4F));
		g2d.fill(kreis);
		
		kreis.x -= 3;
		kreis.y -= 3;
		
		g2d.setComposite(makeComposite(1F));
		
		g2d.setColor(Color.WHITE);
		g2d.fill(kreis);
		
		g2d.setColor(Color.BLACK);
		g2d.setFont(font);
		g2d.drawString(strValue,(int) kreis.getX() + 9, (int) kreis.getY() + 20);
		
		g2d.draw(kreis);
		
		
	}

	public boolean contains(Point p) {
		return kreis.contains(p.getX(), p.getY());
	}
	
	public String sayHello() {
		
		return "\t\t"+strValue;
		
	}
	public String contentToString() {
		
		return strValue;
		
	}
	
	
}
