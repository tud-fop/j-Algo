/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

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
 * @author Alexander Claus, Sebastian Pape
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
		boundingRect.x = pa.getXFor(SearchTree.getMinimumNode(pivot))
			- NODE_DIAMETER[Settings.getDisplayMode()] / 2 - 1;
		boundingRect.y = pa.getYFor(pivot)
			- NODE_DIAMETER[Settings.getDisplayMode()] / 2 - 1;
		boundingRect.width = pa.getXFor(SearchTree.getMaximumNode(pivot))
			- pa.getXFor(SearchTree.getMinimumNode(pivot))
			+ NODE_DIAMETER[Settings.getDisplayMode()] + 2;
		boundingRect.height = (pivot.getHeight() - 1)
			* Y_DIST_NODES[Settings.getDisplayMode()]
			+ NODE_DIAMETER[Settings.getDisplayMode()] + 2;
	}

	public void run() {

		double xd = 0;
		double l_anf = 0;
		double l_ende = 0;
		double l_diff = 0;

		if (pivot.getRightChild().getLeftChild() != null) {

			// Abstand zwischen rechten Knoten und linken Knoten des Knotens,
			// der umgehangen wird (für animierte Linie)
			xd = (float)(pa.getXFor(pivot.getRightChild()) - pa.getXFor(pivot))
				/ Y_DIST_NODES[Settings.getDisplayMode()];
			// Berechnung der Länge der animierten Linie zum alten parent-node
			// und zum neuen parent-node
			l_anf = Math.sqrt(Math.pow(
				(pa.getXFor(pivot.getRightChild()) -
					pa.getXFor(pivot.getRightChild().getLeftChild())),
				2)
				+ Math.pow(Y_DIST_NODES[Settings.getDisplayMode()], 2));
			l_ende = Math.sqrt(Math.pow(
				(pa.getXFor(pivot.getRightChild().getLeftChild()) -
					pa.getXFor(pivot)),
				2)
				+ Math.pow(Y_DIST_NODES[Settings.getDisplayMode()], 2));
			l_diff = (l_anf - l_ende) / Y_DIST_NODES[Settings.getDisplayMode()];
		}

		for (int y = 0; y <= Y_DIST_NODES[Settings.getDisplayMode()]; y++) {
			offG.setColor(Color.WHITE);
			offG.fillRect(boundingRect.x, boundingRect.y, boundingRect.width,
				boundingRect.height);
			pa.drawTree(pivot.getRightChild().getRightChild(), 0, -y);
			pa.drawTree(pivot.getLeftChild(), 0, y);

			// zeichnet pivot RightChild.LeftChild (Umhängen des Knoten) und
			// Teilbäume
			if (pivot.getRightChild().getLeftChild() != null) {
				double xd_y = (xd * y);
				int y_l = (int)Math.sqrt(
					Math.pow(l_anf - (l_diff * y), 2) -
					Math.pow((pa.getXFor(pivot.getRightChild()) -
						pa.getXFor(pivot.getRightChild().getLeftChild())) -
						(int)xd_y,
					2));
				int y_l_hoehe =
					pa.getYFor(pivot.getRightChild().getLeftChild()) - y_l;
				// Falls der Y-Wert der umgehängten Linie aus dem Viereck
				// herausragt
				if (y_l_hoehe <= (pa.getYFor(pivot)
					- NODE_DIAMETER[Settings.getDisplayMode()] / 2 - 1))
					y_l_hoehe = pa.getYFor(pivot)
					- NODE_DIAMETER[Settings.getDisplayMode()] / 2;

				pa.drawTree(
					pivot.getRightChild().getLeftChild().getRightChild(), 0, 0);
				pa.drawTree(
					pivot.getRightChild().getLeftChild().getLeftChild(), 0, 0);

				pa.drawLine(
					pa.getXFor(pivot.getRightChild().getLeftChild()),
					pa.getYFor(pivot.getRightChild().getLeftChild()),
					pa.getXFor(pivot.getRightChild()) - (int)xd_y,
					y_l_hoehe,
					pivot);

				/*
				 * pa.drawLine(pa.getXFor(pivot.getRightChild().getLeftChild()),
				 * pa.getYFor(pivot.getRightChild().getLeftChild()),
				 * pa.getXFor(pivot.getRightChild())-xd2,
				 * pa.getYFor(pivot.getRightChild()),pivot);
				 */
				pa.drawNode(pa.getXFor(pivot.getRightChild().getLeftChild()),
					pa.getYFor(pivot.getRightChild().getLeftChild()), pivot
					.getRightChild().getLeftChild());
			}

			pa.drawLine(
				pa.getXFor(pivot.getRightChild()),
				pa.getYFor(pivot.getRightChild()) - y,
				pa.getXFor(pivot),
				pa.getYFor(pivot) + y,
				pivot);
			pa.drawNode(
				pa.getXFor(pivot.getRightChild()),
				pa.getYFor(pivot.getRightChild()) - y, pivot.getRightChild());
			pa.drawNode(pa.getXFor(pivot), pa.getYFor(pivot) + y, pivot);
			pa.repaint();
			try {
				Thread.sleep(30);
			}
			catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		animStopped();
	}
}