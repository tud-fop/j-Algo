package org.jalgo.module.hoare.view.formula;

import javax.imageio.ImageIO;

import java.awt.Component;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.GradientPaint;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.jalgo.main.util.Messages;


public class Enten extends Component {
	//String entenFile = "/home/joker/programming/Java/SWP/E-klein.png";
	Image entenImage;
	List<Integer> Xs;
	private static final long serialVersionUID = -1;
	
	public Enten(){
		try {
			entenImage = ImageIO.read(Messages.getResourceURL("hoare","icon.duck"));
		} catch(IOException e){
			return;
		}
		Xs = new ArrayList<Integer>();
		Xs.add(Integer.valueOf(-entenImage.getWidth(null)));
	}
	
	public void paint(Graphics g){
		if( entenImage == null )
			return;
		
		int i = 0;
		for( int x : Xs ){
			g.drawImage(entenImage, entenImage.getWidth(null)+x, 0, 0, entenImage.getHeight(null), 0, 0, entenImage.getWidth(null)+x, entenImage.getHeight(null), null);
			x++;
			Xs.set(i, x);
			i++;
		}
		if( Xs.get(Xs.size()-1) >= (entenImage.getWidth(null)/2) ){
			Xs.add(-entenImage.getWidth(null)-(((Double)(Math.random()*1000)).intValue()%40));
		}
		if( Xs.get(0)-entenImage.getWidth(null) > g.getClipBounds().width ){
			Xs.remove(0);
		}
		((Graphics2D) g).setPaint(new GradientPaint(0,g.getClipBounds().height-(g.getClipBounds().height - entenImage.getHeight(null)), new Color(0.5f, 0.5f, 1f), 0, g.getClipBounds().height, Color.BLUE));
		((Graphics2D) g).fillRect(0, g.getClipBounds().height-(g.getClipBounds().height - entenImage.getHeight(null) + 3), g.getClipBounds().width, g.getClipBounds().height);
	}
}
