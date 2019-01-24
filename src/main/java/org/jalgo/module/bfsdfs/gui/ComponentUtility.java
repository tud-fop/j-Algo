package org.jalgo.module.bfsdfs.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.module.bfsdfs.algorithms.stack.NodeStatus;

/**
 * abstract class that contains methods which are used in multiple GUI classes.
 * @author Florian Dornbusch
 */
public abstract class ComponentUtility implements GUIConstants {
	
	/** if true, changes all attributes from normal sizes and fonts to bigger
	 * beamer mode sizes and fonts.
	 */
	public static boolean BEAMER_MODE = false;
	
	/**
	 * Draws a node at certain coordinates. A node is a circle with a border
	 * and a label inside. Uses {@linkplain GraphDrawing} utility method.
	 * @param g the used Graphics 
	 * @param x x-coordinate of the node
	 * @param y y-coordinate of the node
	 * @param label the label that is drawn on the node
	 * @param status the NodeStatus which decides how the node is drawn
	 * @param nodeSize determines how large the node is. this value is twice
	 * 		  the radius of the node
	 * @param nodeBorderSize the size of the border in pixel
	 * @param font the used font
	 * @param alpha the used alpha value that determines the transparency
	 * @author Florian Dornbusch
	 * @author Anselm Schmidt
	 */
	public static void drawNode(Graphics g, int x, int y,
			int label, NodeStatus status, int nodeSize,
			int nodeBorderSize, Font font, int alpha) {
		Color topColor = NODE_COLOR_TOP;
		Color bottomColor = NODE_COLOR_BOTTOM;
		Color labelColor = NODE_LABEL_COLOR;
		
		switch(status) {
		case UNTOUCHED:
			topColor = NODE_COLOR_UNTOUCHED_TOP;
			bottomColor = NODE_COLOR_UNTOUCHED_BOTTOM;
			break;
			
		case WAITING:
			topColor = NODE_COLOR_WAITING_TOP;
			bottomColor = NODE_COLOR_WAITING_BOTTOM;
			break;
			
		case FINISHED:
			topColor = NODE_COLOR_FINISHED_TOP;
			bottomColor = NODE_COLOR_FINISHED_BOTTOM;
			labelColor = NODE_LABEL_COLOR_FINISHED;
			break;
		}
		
		int radius = (int) Math.round((double) nodeSize / 2);
		
		GraphDrawing.drawNode(g, Integer.toString(label),
				new Point(x + radius, y + radius),
				font, nodeSize, nodeBorderSize,
				NODE_BORDER_COLOR, labelColor, topColor,
				bottomColor, alpha, NODE_GRADIENT_HEIGHT);
	}
	
	
	/**
	 * Utility method to create a {@linkplain JToolbarButton}.
	 * @param a The action for this button.
	 * @return The created Button.
	 * @author Florian Dornbusch
	 */
	public static JButton createToolbarButton(Action a) {
		JToolbarButton button = new JToolbarButton(
			(Icon)a.getValue(Action.SMALL_ICON),
			null, null);
		button.setAction(a);
		button.setText("");
		button.setFocusable(false);
		String status = button.getAction().getClass().getSimpleName()
				+".tooltip";
		button.addMouseListener(new StatusMouseAdapter(status));
		return button;
	}
	
	/**
	 * Utility method to create a {@linkplain JButton}.
	 * @param a The action for this button.
	 * @return The created Button.
	 * @author Florian Dornbusch
	 */
	public static JButton createButton(Action a) {
		JButton button = new JButton();
		button.setAction(a);
		button.setText("");
		button.setFocusable(false);
		String status = button.getAction().getClass().getSimpleName()
				+".tooltip";
		button.addMouseListener(new StatusMouseAdapter(status));
		return button;
	}
	

	/**
	 * Utility method to create a {@linkplain JToggleButton}.
	 * @param a The action for this button.
	 * @return The created Button.
	 * @author Florian Dornbusch
	 */
	public static JToggleButton createToolbarToggleButton(Action a) {
		JToggleButton button = new JToggleButton();
		button.setAction(a);
		button.setText("");
		button.setFocusable(false);
		String status = button.getAction().getClass().getSimpleName()+".tooltip";
		button.addMouseListener(new StatusMouseAdapter(status));
		return button;
	}	
}
