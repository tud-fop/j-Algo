/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on 28.04.2005
 */
package org.jalgo.module.avl.gui.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JComponent;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.datastructure.AVLNode;
import org.jalgo.module.avl.datastructure.Node;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.datastructure.Visualizable;
import org.jalgo.module.avl.datastructure.WorkNode;
import org.jalgo.module.avl.gui.DisplayModeChangeable;
import org.jalgo.module.avl.gui.GUIController;
import org.jalgo.module.avl.gui.Settings;

/**
 * The class <code>PaintArea</code> represents a component, where the "really
 * important thing" of the AVL module is located. Here the instance of the
 * <code>SearchTree</code> is displayed and algorithms are visualized.<br>
 * How to paint the nodes in different styles is realized with flags, which are
 * set to each node. For details see <code>Visualizable</code>.<br>
 * Future versions may support animations between single algorithm steps, but
 * currently this is not implemented in order to release a stable version.
 * 
 * @author Sebastian Pape, Alexander Claus
 * @see org.jalgo.module.avl.datastructure.Visualizable
 */
public class PaintArea
extends JComponent
implements DisplayModeChangeable, GraphicsConstants {

	//general references
	private GUIController gui;
	private SearchTree tree;
	private Controller controller;

	//the offscreen context
	private BufferedImage offI;
	private Graphics2D offG;
	
	//layout variables
	private int width;
	private int height;

	//the content of the tree in several traversions
	private List<Node> nodeInOrder;
	private List<Node> nodePostOrder;

	//display tuning
	private int xDiff;
	private FontMetrics FM_KEY_FONT;
	private FontMetrics FM_BALANCE_FONT;
	private FontMetrics FM_BALANCE_FONT_RED;
	private FontMetrics FM_ROTATION_ARROW_FONT;
	private int KEY_HEIGHT;
	private int BALANCE_RED_HEIGHT;

	//animation stuff, currently deactivated in order to release a stable version
//	private boolean animMode = false;
//	private Animator animator;
//	private boolean doAnimIfNecessary;

	/**
	 * Constructs a <code>PaintArea</code> object with the given references.
	 * 
	 * @param gui the <code>GUIController</code> instance of the AVL module
	 * @param tree the <code>SearchTree</code> instance to be displayed
	 * @param controller the <code>Controller</code> instance of the AVL module
	 */
	public PaintArea(final GUIController gui, SearchTree tree,
		Controller controller) {
		this.gui = gui;
		this.tree = tree;
		this.controller = controller;

		//displayModeChanged() is not called here because of nullpointerexception
		//when calling the first time
		FM_KEY_FONT = getFontMetrics(KEY_FONT[Settings.getDisplayMode()]);
		FM_BALANCE_FONT = getFontMetrics(BALANCE_FONT[Settings.getDisplayMode()]);
		FM_BALANCE_FONT_RED = getFontMetrics(
			BALANCE_FONT_RED[Settings.getDisplayMode()]);
		FM_ROTATION_ARROW_FONT = getFontMetrics(
			ROTATION_ARROW_FONT[Settings.getDisplayMode()]);
		KEY_HEIGHT = FM_KEY_FONT.getAscent();
		BALANCE_RED_HEIGHT = FM_BALANCE_FONT_RED.getAscent();

		//initiale gr�sse bewusst sehr gross gew�hlt, um nicht zu oft offscreen
		//image zu vergr�ssern -> performance
		updateOffscreenSize(2000, 2000);

		//the status line updater
		addMouseListener(new MouseAdapter() {
			public void mouseExited(MouseEvent e) {
				JAlgoGUIConnector.getInstance().setStatusMessage(null);
			}
			public void mouseEntered(MouseEvent e) {
				JAlgoGUIConnector.getInstance().setStatusMessage(
					Messages.getString("avl", "PaintArea.Status_message")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});
	}

	/**
	 * This method is invoked, if the size of the paint area has changed or
	 * possibly has changed. The size of the offscreen context is increased, if
	 * necessary. Because of this is a slow operation, the offscreen context
	 * initially has been sized large enough for most trees.
	 * 
	 * @param w the new minimum width of the offscreen context
	 * @param h the new minimum height of the offscreen context
	 */
	private void updateOffscreenSize(int w, int h) {
		if (w <= width && h <= height) return;
		offI = new BufferedImage(
			Math.max(width, w), 
			Math.max(height, h),
			BufferedImage.TYPE_INT_RGB);
		offG = offI.createGraphics();
		offG.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
	}

	/**
	 * Calls <code>paint()</code>.
	 */
	public void update(Graphics g) {
		paint(g);
	}
	
	/**
	 * Paints the tree and the working node to the offscreen context and paints
	 * the offscreen image to the screen <code>graphics</code> object.
	 */
	public void paint(Graphics g) {
/*		if (animMode) {
			if (animator.isRunning()) {
				g.drawImage(offI, 0, 0, this);
				return;
			}
			animMode = false;
			animator = null;
		}*/

		offG.setColor(Color.WHITE);
		offG.fillRect(0, 0, width, height);
		drawTree();
		drawWorkingNode();
				
		g.drawImage(offI, 0, 0, this);

/*		if (animMode) {
			animator.start();
		}*/
	}
	
	/**
	 * Retrieves the x coordinate of the given node.
	 * 
	 * @param node the node to be painted.
	 * 
	 * @return the x coordinate of the given node.
	 */
	public int getXFor(Node node) {
		return MARGIN_LEFT[Settings.getDisplayMode()] +
			X_DIST_NODES[Settings.getDisplayMode()]*nodeInOrder.indexOf(node) +
			xDiff;		
	}

	/**
	 * Retrieves the y coordinate of the given node.
	 * 
	 * @param node the node to be painted.
	 * 
	 * @return the y coordinate of the given node.
	 */
	public int getYFor(Node node){
		return MARGIN_TOP[Settings.getDisplayMode()]+
			(tree.getLevelFor(node)-1)*Y_DIST_NODES[Settings.getDisplayMode()];
	}

	/**
	 * Iterates over the nodes of the tree in post order and draws the nodes and
	 * node decorations.
	 * 
	 * @see #drawNode(int, int, Node)
	 * @see #drawNodeDecorations(int, int, Node)
	 */
	private void drawTree() {
		if (tree.getRoot() == null) return;

		int x;
		int y;
		Node currentNode;

		for (int i=0; i<nodePostOrder.size(); i++) {
			currentNode = nodePostOrder.get(i);
			x=getXFor(currentNode);
			y=getYFor(currentNode);
			if (currentNode != tree.getRoot())
				drawLine(x, y,
						getXFor(currentNode.getParent()),
						getYFor(currentNode.getParent()),currentNode);
			drawNode(x, y, currentNode);
			drawNodeDecorations(x, y, currentNode);
		}
	}

	/**
	 * Draws a the partial tree with the given root node and coordinate offsets.
	 * Currently this method is unused, because of deactivation of animations.
	 * 
	 * @param node the root of the tree to be painted
	 * @param dx the offset in x axis
	 * @param dy the offset in y axis
	 */
	public void drawTree(Node node, int dx, int dy) {
		if (node == null) return;
		drawTree(node.getLeftChild(), dx, dy);
		drawTree(node.getRightChild(), dx, dy);
		int x = getXFor(node);
		int y = getYFor(node);
		if (node != tree.getRoot())
			drawLine(x+dx, y+dy,
				getXFor(node.getParent())+dx,
				getYFor(node.getParent())+dy,
				node);
		drawNode(x+dx, y+dy, node);
	}

	/**
	 * Draws the given node and its key at the given position. How to paint the
	 * node can be read from the flags returned by
	 * <code>getVisualizationStatus()</code>.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param node the node to be painted
	 * 
	 * @see Visualizable
	 */
	public void drawNode(int x, int y, Node node) {
		int key = node.getKey();
		String key_string = String.valueOf(key);
		int key_width;
		
		// Zeichnen des Knotens
		offG.setColor(Color.BLACK);
		offG.fillOval(x-NODE_DIAMETER[Settings.getDisplayMode()]/2, y-NODE_DIAMETER[Settings.getDisplayMode()]/2, NODE_DIAMETER[Settings.getDisplayMode()], NODE_DIAMETER[Settings.getDisplayMode()]);
		if ((node.getVisualizationStatus()&Visualizable.NORMAL) != 0)
			offG.setColor(Color.YELLOW);
		else
			offG.setColor(COLOR_FOCUSED_NODE);
		
		offG.fillOval(x-(NODE_DIAMETER[Settings.getDisplayMode()]-4)/2, y-(NODE_DIAMETER[Settings.getDisplayMode()]-4)/2, NODE_DIAMETER[Settings.getDisplayMode()]-4, NODE_DIAMETER[Settings.getDisplayMode()]-4);
		
		// Beschriftung des Knotens
		offG.setColor(Color.BLACK);
		offG.setFont(KEY_FONT[Settings.getDisplayMode()]);
		key_width = FM_KEY_FONT.stringWidth(key_string);
		offG.drawString(key_string, x-(key_width/2),y+(KEY_HEIGHT/4)+2);
	}

	/**
	 * Draws the balance of the node, if the tree is an AVL tree. Draws the arrows
	 * for rotation, if necessary.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param node the node, for which decorations have to be painted
	 */
	private void drawNodeDecorations(int x, int y, Node node) {
		// Beschriftung der Balance
		if (controller.isAVLMode()){
			int balance = ((AVLNode)node).getBalance();
			String balance_string = String.valueOf(balance);
			if ((node.getVisualizationStatus()&Visualizable.BALANCE) != 0){
				int balance_width = FM_BALANCE_FONT_RED.stringWidth(balance_string);
				offG.setColor(Color.RED);
				offG.setFont(BALANCE_FONT_RED[Settings.getDisplayMode()]);
				offG.drawString(balance_string,x-(balance_width)/2, y-NODE_DIAMETER[Settings.getDisplayMode()]/2-2);
				}
			else {
				int balance_width = FM_BALANCE_FONT.stringWidth(balance_string);
				offG.setColor(Color.BLUE);
				offG.setFont(BALANCE_FONT[Settings.getDisplayMode()]);
				offG.drawString(balance_string,x-(balance_width)/2, y-NODE_DIAMETER[Settings.getDisplayMode()]/2-2);
				}
			}
		
		//Zeichnen der Rotationspfeile
		if ((node.getVisualizationStatus()&Visualizable.ROTATE_LEFT_ARROW) != 0) {
			drawLeftRotationArrow(x, y, node);
//			if (doAnimIfNecessary) {
//				animMode = true;
				//TODO: register animator in guicontroller for stopping
//				animator = new RotateLeftAnimator(this, offG, node);
//				doAnimIfNecessary = false;
//			}
		}
		else {
			if ((node.getVisualizationStatus()&Visualizable.ROTATE_RIGHT_ARROW) != 0)
				drawRightRotationArrow(x, y, node);
		}
	}
	
	/**
	 * Calculates the location of the working node and draws it. Draws insertion
	 * arrows, if necessary.
	 */
	private void drawWorkingNode() {
		WorkNode wnode;
		if ((wnode = controller.getWorkNode()) == null) return;

		int x;
		int y;
		Node nextToWnode = wnode.getNextToMe();
		if (nextToWnode == null) {
			x = width/2-X_DIST_WN[Settings.getDisplayMode()];
			y = MARGIN_TOP[Settings.getDisplayMode()];
		}
		else {
			x = getXFor(nextToWnode)-X_DIST_WN[Settings.getDisplayMode()];
			y = getYFor(nextToWnode);
		}
		int key = wnode.getKey();
		String key_string = String.valueOf(key);
		int key_width;
		int visualstat = wnode.getVisualizationStatus();
		// if visualstatus == 1 then visible else unvisible
		if ((visualstat&Visualizable.NORMAL) != 0) {
			// Zeichnen des Arbeitsknotens
			offG.setColor(Color.BLACK);
			offG.fillOval(
				x-NODE_DIAMETER[Settings.getDisplayMode()]/2,
				y-NODE_DIAMETER[Settings.getDisplayMode()]/2,
				NODE_DIAMETER[Settings.getDisplayMode()],
				NODE_DIAMETER[Settings.getDisplayMode()]);
			offG.setColor(COLOR_FOCUSED_WNODE[Settings.getDisplayMode()]);
			offG.fillOval(
				x-(NODE_DIAMETER[Settings.getDisplayMode()]-4)/2,
				y-(NODE_DIAMETER[Settings.getDisplayMode()]-4)/2,
				NODE_DIAMETER[Settings.getDisplayMode()]-4,
				NODE_DIAMETER[Settings.getDisplayMode()]-4);
			// Beschriftung des Arbeitsknotens
			offG.setColor(Color.WHITE);
			offG.setFont(KEY_FONT[Settings.getDisplayMode()]);
			key_width = FM_KEY_FONT.stringWidth(key_string);
			offG.drawString(key_string, x-(key_width/2),y+(KEY_HEIGHT/4)+2);
		}
		// Zeichnen der Pfeile
		if ((visualstat&Visualizable.LEFT_ARROW) != 0)
			drawLeftArrow(
				(x-NODE_DIAMETER[Settings.getDisplayMode()]/2)+
					X_DIST_WN[Settings.getDisplayMode()],
				y+NODE_DIAMETER[Settings.getDisplayMode()]/2);
		else if ((visualstat&Visualizable.RIGHT_ARROW) != 0)
				drawRightArrow(
					(x+NODE_DIAMETER[Settings.getDisplayMode()]/2)+
						X_DIST_WN[Settings.getDisplayMode()],
					y+NODE_DIAMETER[Settings.getDisplayMode()]/2);
	}

	/**
	 * Draws the line between the given node and its parent node. The style of the
	 * line is defined by the flags of the node.
	 * 
	 * @param x1 the start x coordinate
	 * @param y1 the start y coordinate
	 * @param x2 the end x coordinate
	 * @param y2 the end y coordinate
	 * @param currentNode the child node in this connection
	 */
	public void drawLine(int x1, int y1, int x2, int y2, Node currentNode){
		if (currentNode == null) return;
		int visualstat = currentNode.getVisualizationStatus(); 
		if ((visualstat&Visualizable.FOCUSED) != 0){
			if((visualstat&Visualizable.LINE_NORMAL) != 0)
				offG.setColor(Color.BLACK);
			else 
				offG.setColor(COLOR_FOCUSED_ARROW);
		}
		else
			offG.setColor(Color.BLACK);
			
		
		offG.setStroke( new BasicStroke(2) );
		offG.drawLine(x1, y1, x2, y2);
	}

	/**
	 * Draws an arrow for inserting a new node as left child.
	 * 
	 * @param x the x coordinate of the startpoint
	 * @param y the y coordinate of the startpoint
	 */
	private void drawLeftArrow(int x,int y) {
		double theta2; 
		int headLength = 10;
		int headwidth = 7;
		int x2 = x-19;
		int y2 = y+30; 
		int    deltaX = (x2-x);    
		int    deltaY = (y2-y); 
		double theta  = Math.atan((double)(deltaY)/(double)(deltaX));
		if (deltaX < 0.0) theta2 = theta+Math.PI; 
			else          theta2 = theta; 
		int lengthdeltaX =- (int)(Math.cos(theta2)*headLength);   
		int lengthdeltaY =- (int)(Math.sin(theta2)*headLength);  
		int widthdeltaX  =  (int)(Math.sin(theta2)*headwidth); 
		int widthdeltaY  =  (int)(Math.cos(theta2)*headwidth);   
		offG.setColor(COLOR_FOCUSED_ARROW);
		offG.setStroke(new BasicStroke(2));
		offG.drawLine(x,y,x2,y2);  
		offG.drawLine(x2,y2,
			x2+lengthdeltaX+widthdeltaX,y2+lengthdeltaY-widthdeltaY);  
		offG.drawLine(x2,y2,
			x2+lengthdeltaX-widthdeltaX,y2+lengthdeltaY+widthdeltaY);
	}

	/**
	 * Draws an arrow for inserting a new node as right child.
	 * 
	 * @param x the x coordinate of the startpoint
	 * @param y the y coordinate of the startpoint
	 */
	private void drawRightArrow(int x,int y) {
		double theta2; 
		int headLength = 10;
		int headwidth = 7;
		int x2 = x+20;
		int y2 = y+32; 
		int    deltaX = (x2-x);    
		int    deltaY = (y2-y); 
		double theta  = Math.atan((double)(deltaY)/(double)(deltaX));
		if (deltaX < 0.0) theta2 = theta+Math.PI; 
			else          theta2 = theta; 
		int lengthdeltaX =- (int)(Math.cos(theta2)*headLength);   
		int lengthdeltaY =- (int)(Math.sin(theta2)*headLength);  
		int widthdeltaX  =  (int)(Math.sin(theta2)*headwidth); 
		int widthdeltaY  =  (int)(Math.cos(theta2)*headwidth);
		offG.setColor(COLOR_FOCUSED_ARROW);
		offG.setStroke(new BasicStroke(2));
		offG.drawLine(x,y,x2,y2);  
		offG.drawLine(x2,y2,
			x2+lengthdeltaX+widthdeltaX,y2+lengthdeltaY-widthdeltaY);  
		offG.drawLine(x2,y2,
			x2+lengthdeltaX-widthdeltaX,y2+lengthdeltaY+widthdeltaY);
	}
	
	/**
	 * Draws an arrow indicating that a left rotation will be performed.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	private void drawLeftRotationArrow(int x, int y, Node currentnode){
		int arc_width = 80;
		offG.setColor(Color.RED);
		offG.setStroke(new BasicStroke(3));
		offG.drawArc(
			x-arc_width/2,
			y-NODE_DIAMETER[Settings.getDisplayMode()]/2-BALANCE_RED_HEIGHT-2,
			arc_width, 50, 45, 90);
		int x_p[] = {x-33, x-26, x-19};
		int y_p[] = {
			y-NODE_DIAMETER[Settings.getDisplayMode()]/2-BALANCE_RED_HEIGHT+7,
			y-NODE_DIAMETER[Settings.getDisplayMode()]/2-BALANCE_RED_HEIGHT-6,
			y-NODE_DIAMETER[Settings.getDisplayMode()]/2-BALANCE_RED_HEIGHT+10};
		offG.fillPolygon(x_p, y_p, 3);
		offG.setFont(ROTATION_ARROW_FONT[Settings.getDisplayMode()]);
		offG.drawString("L("+currentnode.getKey()+")",x+NODE_DIAMETER[Settings.getDisplayMode()]/2+2,y);
	}
	
	/**
	 * Draws an arrow indicating that a right rotation will be performed.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	private void drawRightRotationArrow(int x, int y, Node currentnode){
		int arc_width = 80;
		offG.setColor(Color.RED);
		offG.setStroke(new BasicStroke(3));
		offG.drawArc(
			x-arc_width/2,
			y-NODE_DIAMETER[Settings.getDisplayMode()]/2-BALANCE_RED_HEIGHT-2,
			arc_width, 50, 45, 90);
		int x_p[] = {x+31, x+24, x+17};
		int y_p[] = {
			y-NODE_DIAMETER[Settings.getDisplayMode()]/2-BALANCE_RED_HEIGHT+7,
			y-NODE_DIAMETER[Settings.getDisplayMode()]/2-BALANCE_RED_HEIGHT-6,
			y-NODE_DIAMETER[Settings.getDisplayMode()]/2-BALANCE_RED_HEIGHT+10};
		offG.fillPolygon(x_p, y_p, 3);
		offG.setFont(ROTATION_ARROW_FONT[Settings.getDisplayMode()]);
		String string = "R("+currentnode.getKey()+")";
		int string_width = FM_ROTATION_ARROW_FONT.stringWidth(string);
		offG.drawString(string,x-NODE_DIAMETER[Settings.getDisplayMode()]/2-string_width-2,y);
	}
	
	/**
	 * Sets the preferred size of this component. The given size is the bound,
	 * which the parent component (e.g. a scrollpane) expects, when content is
	 * larger, the given values are ignored.
	 */
	public void setPreferredSize(Dimension size) {
		int nodes = tree.getWeight();
		int newWidth = Math.max(
			(nodes-1)*X_DIST_NODES[Settings.getDisplayMode()] +
				MARGIN_LEFT[Settings.getDisplayMode()] +
				MARGIN_RIGHT[Settings.getDisplayMode()],
			size.width);
		int newHeight = Math.max(
			(tree.getHeight()-1)*Y_DIST_NODES[Settings.getDisplayMode()] +
				MARGIN_TOP[Settings.getDisplayMode()] +
				MARGIN_BOTTOM[Settings.getDisplayMode()],
			size.height);
		updateOffscreenSize(newWidth, newHeight);
		width = newWidth;
		height = newHeight;
		super.setPreferredSize(new Dimension(width, height));
		// berechnen der x-verschiebung zur zentrierung des baumes
		xDiff = (width -
				MARGIN_LEFT[Settings.getDisplayMode()] -
				MARGIN_RIGHT[Settings.getDisplayMode()] -
				(nodes-1)*X_DIST_NODES[Settings.getDisplayMode()])/2;
	}

	/**
	 * Returns a scaled image of the content of this <code>PaintArea</code>.
	 * This method is necessary e.g. for previewing and navigating.
	 * 
	 * @param scale the scale for the transformation
	 * 
	 * @return an <code>Image</code> which is a scaled version of the content
	 */
	public Image getScaledImage(double scale) {
		return offI.getSubimage(0, 0, width, height).getScaledInstance(
			(int)(scale*width),
			(int)(scale*height), Image.SCALE_SMOOTH);
	}

	/**
	 * Scrolls the working node as good as possible to the center of the area.
	 */
	public void scrollWorkNodeToCenter() {
		if (tree.getHeight() == 0) return;
		Rectangle visibleRect = getVisibleRect();
		visibleRect.x = getXFor(controller.getWorkNode().getNextToMe())-
			visibleRect.width/2;
		visibleRect.y = getYFor(controller.getWorkNode().getNextToMe())-
			visibleRect.height/2;
		scrollRectToVisible(visibleRect);
	}

	/**
	 * Notifies this <code>PaintArea</code> object, that content has changed.
	 * Prepares some calculations for displaying and calls repaint().
	 */
	public void update() {
		nodeInOrder = tree.exportInOrder();
		nodePostOrder = tree.exportPostOrder();
		scrollWorkNodeToCenter();
//		doAnimIfNecessary = true;
		repaint();
	}

	/**
	 * This method is called, when the display mode has changed between pc mode
	 * and beamer mode. As a result of this the size of the fonts, the size of
	 * the nodes and some colors are changed.
	 */
	public void displayModeChanged() {
		FM_KEY_FONT = getFontMetrics(KEY_FONT[Settings.getDisplayMode()]);
		FM_BALANCE_FONT = getFontMetrics(BALANCE_FONT[Settings.getDisplayMode()]);
		FM_BALANCE_FONT_RED = getFontMetrics(
			BALANCE_FONT_RED[Settings.getDisplayMode()]);
		FM_ROTATION_ARROW_FONT = getFontMetrics(ROTATION_ARROW_FONT[Settings.getDisplayMode()]);
		KEY_HEIGHT = FM_KEY_FONT.getAscent();
		BALANCE_RED_HEIGHT = FM_BALANCE_FONT_RED.getAscent();
		gui.doLayout();
		//TODO: find out, why scrolling does not work when switching to beamer mode
		scrollWorkNodeToCenter();
	}
}