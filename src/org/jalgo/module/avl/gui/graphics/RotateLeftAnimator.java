/* Created on 09.06.2005 */
package org.jalgo.module.avl.gui.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.lang.Math;

import org.jalgo.module.avl.datastructure.Node;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.gui.Settings;

/**
 * @author Alexander Claus
 */
public class RotateLeftAnimator
extends Animator {

	private final PaintArea pa;
	private final Graphics2D offG;
	private final Node pivot;

	private Rectangle boundingRect;
	public RotateLeftAnimator(PaintArea pa, Graphics2D offG, Node pivot) {
		this.pa = pa;
		this.offG = offG;
		this.pivot = pivot;
		boundingRect = new Rectangle();
		boundingRect.x = pa.getXFor(SearchTree.getMinimumNode(pivot)) -
			NODE_DIAMETER[Settings.getDisplayMode()]/2 - 1;
		boundingRect.y = pa.getYFor(pivot) -
			NODE_DIAMETER[Settings.getDisplayMode()]/2 - 1;
		boundingRect.width = pa.getXFor(SearchTree.getMaximumNode(pivot)) -
			pa.getXFor(SearchTree.getMinimumNode(pivot)) +
			NODE_DIAMETER[Settings.getDisplayMode()] + 2;
		boundingRect.height = (pivot.getHeight()-1)*
			Y_DIST_NODES[Settings.getDisplayMode()] +
			NODE_DIAMETER[Settings.getDisplayMode()] + 2;
	}

	public void run() {
		
		double xd = 0;
		double l_anf = 0;
		double l_ende = 0;
		double l_diff = 0;
	
		
		if (pivot.getRightChild().getLeftChild()!= null){
		
		// Abstand zwischen rechten Knoten und linken Knoten des Knotens, der umgehangen wird (für animierte Linie)
		xd = ((float)pa.getXFor(pivot.getRightChild()) - (float)pa.getXFor(pivot))
					/(float)Y_DIST_NODES[Settings.getDisplayMode()];
		// Berechnung der Länge der animierten Linie zum alten parent-node und zum neuen parent-node
		l_anf = Math.sqrt( Math.pow((pa.getXFor(pivot.getRightChild())-pa.getXFor(pivot.getRightChild().getLeftChild())),2) 
				+ Math.pow(Y_DIST_NODES[Settings.getDisplayMode()],2) );
		l_ende =  Math.sqrt( Math.pow((pa.getXFor(pivot.getRightChild().getLeftChild())-pa.getXFor(pivot)),2) 
				+ Math.pow(Y_DIST_NODES[Settings.getDisplayMode()],2) );
		l_diff = (l_anf - l_ende)/Y_DIST_NODES[Settings.getDisplayMode()];
		}
		
			
		for (int y=0; y<=Y_DIST_NODES[Settings.getDisplayMode()]; y++){
			offG.setColor(Color.WHITE);
			offG.fillRect(
				boundingRect.x,
				boundingRect.y,
				boundingRect.width,
				boundingRect.height);
			pa.drawTree(pivot.getRightChild().getRightChild(), 0, -y);
			pa.drawTree(pivot.getLeftChild(), 0, y);
								
			//zeichnet pivot RightChild.LeftChild (Umhängen des Knoten) und Teilbäume
			if (pivot.getRightChild().getLeftChild()!= null){
				
				double xd_y = (xd*y);
				int y_l = (int)Math.sqrt(Math.pow(l_anf-(l_diff*y),2)
							-Math.pow( (pa.getXFor(pivot.getRightChild())-pa.getXFor(pivot.getRightChild().getLeftChild()))-(int)xd_y,2));
				int y_l_hoehe = pa.getYFor(pivot.getRightChild().getLeftChild())-y_l;		
				// Falls der Y-Wert der umgehängten Linie aus dem Viereck herausragt
				if (y_l_hoehe <= (pa.getYFor(pivot)-NODE_DIAMETER[Settings.getDisplayMode()]/2-1))
					y_l_hoehe = pa.getYFor(pivot)-NODE_DIAMETER[Settings.getDisplayMode()]/2;
					
				pa.drawTree(pivot.getRightChild().getLeftChild().getRightChild(), 0, 0);
				pa.drawTree(pivot.getRightChild().getLeftChild().getLeftChild(), 0, 0);
			
				pa.drawLine(pa.getXFor(pivot.getRightChild().getLeftChild()),
						pa.getYFor(pivot.getRightChild().getLeftChild()),
						pa.getXFor(pivot.getRightChild())-(int)xd_y,
						y_l_hoehe ,pivot);

/*				pa.drawLine(pa.getXFor(pivot.getRightChild().getLeftChild()),
						pa.getYFor(pivot.getRightChild().getLeftChild()),
						pa.getXFor(pivot.getRightChild())-xd2,
						pa.getYFor(pivot.getRightChild()),pivot);
*/				
				pa.drawNode(pa.getXFor(pivot.getRightChild().getLeftChild()),
						pa.getYFor(pivot.getRightChild().getLeftChild()),
						pivot.getRightChild().getLeftChild());
			}
			
			pa.drawLine(pa.getXFor(pivot.getRightChild()),
					pa.getYFor(pivot.getRightChild())-y,
					pa.getXFor(pivot),pa.getYFor(pivot)+y,pivot);
			pa.drawNode(
				pa.getXFor(pivot.getRightChild()),
				pa.getYFor(pivot.getRightChild())-y,
				pivot.getRightChild());
			pa.drawNode(pa.getXFor(pivot), pa.getYFor(pivot)+y, pivot);
			pa.repaint();
			try {Thread.sleep(30);}
			catch (InterruptedException e) {}
		}
		animStopped();
	}
}