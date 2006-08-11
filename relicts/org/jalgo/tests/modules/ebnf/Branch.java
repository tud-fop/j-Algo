package org.jalgo.tests.ebnf;

import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;


public class Branch extends SynDiaElem {

	/** Das ist erstmal die einfachste Version eines Branches, es muss im Prinzip noch
	 *  möglich sein dass der Branch
	 */
	private static final long serialVersionUID = -377359652483309748L;
	private List<SynDiaElem> synelems;
	private Concat left;
	private Concat right;
	
	private Arc2D branch1;
	private Arc2D branch2;
	private Line2D line;
	protected RoundRectangle2D.Double kreis;
	
	/**
	 * 
	 * @param type could be "open", "close", "full" - choose between rightopened, leftopened or full branch
	 */public Branch(String type) {
		
		//synelems = new ArrayList<SynDiaElem>();
		left = new Concat();
	    right = new Concat();
		this.color = new Color(0, 200, 0);
		this.setSize(2*HEIGHT + 1, 2*HEIGHT + 1);
		kreis = new RoundRectangle2D.Double(0, 0, width, HEIGHT, HEIGHT, HEIGHT);	
		this.setEnabled(true);
		
		
	}
	
	public void addElem(SynDiaElem el) {
		
		//synelems.add(el);
		
	}
	
	protected void paintComponent( Graphics g )
	{
	
	  Graphics2D g2d = (Graphics2D) g;
	  g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));
	  //g2d.clearRect(0,0,this.getWidth(), this.getHeight());
	  g2d.setColor( Color.WHITE );
	  g2d.fill(kreis);
	  g2d.setColor( Color.BLACK );
	  g2d.draw(kreis);
	
	}
	
	
	public void update() {
				
		this.setSize(width+2, HEIGHT+2);
		kreis = new RoundRectangle2D.Double(0, 0, width, HEIGHT, HEIGHT, HEIGHT);
		
	}
	
	public boolean contains(Point p) {
		return branch1.contains(p.getX(), p.getY());
	}
	
	public String sayHello() {
		
		String ausgabe = new String("");
		ausgabe += "--> Branch\n";
		for (SynDiaElem elem : synelems) {
			
			ausgabe += "\t\t"+elem.sayHello()+"\n";
			
		}
		return ausgabe;
		
	}
	public String contentToString() {
		
		String ausgabe = new String("");
		ausgabe += "(";
		for (SynDiaElem elem : synelems) {
			
			ausgabe += elem.contentToString()+"|";
			
			
		}
		ausgabe = ausgabe.substring(0, ausgabe.length()-1);
		ausgabe += ")";
		return ausgabe;
		
	}
	
	public void setWidth(int w) {
		
		this.width = w;
		
		this.update();
		
	}
	

}
