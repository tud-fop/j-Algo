package org.jalgo.module.bfsdfs.gui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jalgo.module.bfsdfs.gui.GUIConstants;
import org.jalgo.module.bfsdfs.gui.GUIController;
import org.jalgo.module.bfsdfs.gui.GraphDrawing;

/**
 * This class represents a panel where the current successors are drawn.
 * One can change the order of the successors by using drag & drop.<br>
 * Once a new order is set, it is sent to the {@linkplain GUIController}.
 * 
 * @author Florian Dornbusch
 */
public class SuccessorChooser
extends JPanel
implements GUIConstants, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = -4310412523478515344L;
	
	/** The {@linkplain GUIController}. */
	private GUIController gui;
	
	/** The current cursor position */
	private Point cursorPosition;
	
	/**
	 * The id of the node that is currently dragged.<br>
	 * This attribute is <b>0</b> if no node is dragged.
	 */
	private int cursorNode = 0;
	
	/** True if the mouse is inside this component. */
	private boolean mouseInside;
	
	/** True if the mouse button is currently pressed. */
	private boolean mousePressed;
	
	/** True if this component was resized due to too much */
	private boolean resized = false;
	
	/** This list contains all currently available successors. */
	private List<Integer> nodes;
	
	/**
	 * Maps properties to every node. The first entry in this array holds the
	 * coordinates and the second indicate if the node is highlighted.
	 */
	private Map<Integer, Object[]> properties;
	
	/**
	 * The {@linkplain AlgoTab}. It is used by this class to update the two
	 * {@linkplain ScrollArea}s.
	 */
	private AlgoTab algoTab;
	
	/**
	 * Constructor.
	 * @author Florian Dornbusch
	 */
	public SuccessorChooser(GUIController gui, AlgoTab algoTab) {		
		this.gui = gui;
		this.algoTab = algoTab;
		
		cursorPosition = new Point();
		nodes = new LinkedList<Integer>();
		properties = new HashMap<Integer, Object[]>();
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	/**
	 * Additionally changes the background color of this component.
	 * @author Florian Dornbusch
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if(enabled) setBackground(Color.white);
		else setBackground(getParent().getBackground());
	}
	
	/**
	 * Paints the current successors in a horizontal style.
	 * @author Florian Dornbusch
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(!isEnabled() || nodes == null || nodes.isEmpty()) return;
		GraphDrawing.enableAntiAliasing(g);
		
		// in each cycle of the loop one node is drawn
		for(int n:nodes) {
			// set a new size if necessary
			matchSize(n);
			
			// if this node is highlighted, it is drawn with a different color
			Color b;
			Color l;
			if((Boolean)properties.get(n)[1]) {
				b = NODE_BORDER_COLOR_FOCUSED;
				l = NODE_LABEL_COLOR_FOCUSED;
			} else {
				b = NODE_BORDER_COLOR;
				l = NODE_LABEL_COLOR;
			}
			
			// draw the node
			if(n != cursorNode || !mousePressed) {
				GraphDrawing.drawNode(g, Integer.toString(n),
						(Point)properties.get(n)[0],
						NODE_FONT, NODE_SIZE, NODE_BORDER_SIZE, b,
						l, NODE_COLOR, NODE_COLOR,
						ALPHA_100_PERCENT, 0);
			}
		}
		
		// draw the cursor if a dragging is currently performed
		if(mouseInside && mousePressed && cursorNode != 0) {
			GraphDrawing.drawNode(g, Integer.toString(cursorNode),
					cursorPosition,
					NODE_FONT, NODE_SIZE, NODE_BORDER_SIZE,NODE_BORDER_COLOR,
					NODE_LABEL_COLOR, NODE_COLOR, NODE_COLOR,
					160, 0);
		}
	}
	
	/**
	 * This method is invoked  if new successors are available. It clears all
	 * current data, sets the new nodes and maps coordinates to them.
	 * @author Florian Dornbusch
	 */
	public void fill(List<Integer> nodes) {
		
		resized = false;
		
		this.nodes.clear();
		properties.clear();
		
		cursorNode = 0;
		
		this.nodes.addAll(nodes);
		
		for(int i = 0; i < nodes.size(); i++) {
			int x = SPACE_SMALL + i*(NODE_SIZE + SPACE_SMALL)+ NODE_SIZE / 2;
			int y = getHeight() / 2;
			Object[] p = {new Point(x,y), false};
			properties.put(nodes.get(i), p);
		}
		
		repaint();
	}
	
	/** Unused observer method. */
	public void mouseClicked(MouseEvent e) {}
	
	/**
	 * If the mouse enters this component, this state is saved.
	 * @author Florian Dornbusch
	 */
	public void mouseEntered(MouseEvent e) {
		mouseInside = true;
		repaint();
	}
	
	/**
	 * If the mouse exits this component, no node is highlighted.
	 * @author Florian Dornbusch
	 */
	public void mouseExited(MouseEvent e) {
		mouseInside = false;	
		clearHighlighted();
		repaint();
	}
	
	/**
	 * If a mouse button is pressed, this state is saved and the rest of
	 * this method is similar to {@linkplain #mouseMoved(MouseEvent)}.
	 * @author Florian Dornbusch
	 */
	public void mousePressed(MouseEvent e) {
		if(mousePressed) return;
		mousePressed = true;
		
		mouseMoved(e);
		matchCoords();
	}
	
	/**
	 * If a mouse button is released, a new successor order is set and is sent
	 * to the {@linkplain GUIController}. 
	 * @author Florian Dornbusch
	 */
	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
		if(cursorNode != 0 && mouseInside) {
			validateSuccessors();
			gui.setSuccessorOrder(nodes);
		} else {
			// readjust the coordinates of the nodes
			List<Integer> temp = new LinkedList<Integer>();
			temp.addAll(nodes);
			fill(temp);
			temp = null;
		}
		repaint();
	}
	
	/**
	 * If the mouse is dragged, the new position of the mouse cursor is saved.
	 * @author Florian Dornbusch
	 */
	public void mouseDragged(MouseEvent e) {
		cursorPosition = e.getPoint();
		
		repaint();
	}
	
	/**
	 * If the mouse is moved, this method sets the highlighted property for
	 * the node that is under the mouse and changes the mouse cursor if the
	 * mouse is over a node.
	 * @author Florian Dornbusch
	 */
	public void mouseMoved(MouseEvent e) {
		cursorPosition = e.getPoint();
		
		boolean hitAnything = false;
		
		for (Entry<Integer, Object[]> entry : properties.entrySet()) {
            if(isCursorInsideNode((Point)entry.getValue()[0])) {
            	cursorNode = entry.getKey();
            	hitAnything = true;
            	
            	Object[] p = {(Point)entry.getValue()[0], true};
            	properties.put(entry.getKey(), p);
            } else {
            	Object[] p = {(Point)entry.getValue()[0], false};
            	properties.put(entry.getKey(), p);
            }
        }
		
		if(!hitAnything) {
			cursorNode = 0;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} else {
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		
		repaint();
	}
	
	/**
	 * This method checks the mouse cursor is over a given node represented by
	 * its center position.
	 * @author Florian Dornbusch
	 */
	private boolean isCursorInsideNode(Point node) {
		
		int x = Math.abs(cursorPosition.x- node.x);
		int y = Math.abs(cursorPosition.y- node.y);
		
		double distance = Math.sqrt(x * x + y * y);
		
		if(distance > NODE_SIZE / 2) return false;
		return true;
	}
	
	/**
	 * This method checks if a node is at the right side of the currently
	 * dragged node and changes sets its new position one to the left.
	 * @author Florian Dornbusch
	 */
	private void matchCoords() {
		int cursorX = ((Point)properties.get(cursorNode)[0]).x;
		for (Entry<Integer, Object[]> entry : properties.entrySet()) {
			int x = ((Point)entry.getValue()[0]).x;
			if(x > cursorX) {
				Point newPoint = ((Point)entry.getValue()[0]);
				newPoint.x -= NODE_SIZE + SPACE_SMALL;
				Object[] p = {newPoint, ((Boolean)entry.getValue()[1])};
				properties.put(entry.getKey(), p);
           }
        }
	}
	
	/**
	 * This method sets the size of this component according to the amount of
	 * successors.
	 * @param n : The id of the node that is currently checked.
	 * @author Florian Dornbusch
	 */
	private void matchSize(int n) {
		
		int x = ((Point)properties.get(n)[0]).x;
		// if there are too many successors
		if(x > getWidth()) {
			// set the new size
			setSize(x + NODE_SIZE / 2 + SPACE_SMALL,getHeight());
			setPreferredSize(getSize());
			
			// sets the new values for the two scroll areas. the left one
			// is only shown if the invisible scroll bar is not leftmost.
			boolean b = ((JScrollPane)getParent().getParent()).
					getHorizontalScrollBar().getValue() != 0;
			algoTab.getLeftScrollArea().setEnabled(b);
			algoTab.getRightScrollArea().setEnabled(true);
			
			resized = true;
		}
		if(!resized) {
			// set the normal size
			setSize(getParent().getParent().getWidth(),getHeight());
			setPreferredSize(getSize());
			
			// disable the scroll areas
			algoTab.getLeftScrollArea().setEnabled(false);
			algoTab.getRightScrollArea().setEnabled(false);
		}
	}
	
	/**
	 * This method creates a list of all nodes and inserts the currently
	 * dragged node at the right position.
	 * @author Florian Dornbusch 
	 */
	private void validateSuccessors() {
		List<Integer> result = new LinkedList<Integer>();
		
		int leftNeighborX=0, leftNeighbor=0;
		
		for (Entry<Integer, Object[]> entry : properties.entrySet()) {
			if(entry.getKey() != cursorNode) {
				// get the left neighbor of the mouse
				int x = ((Point)entry.getValue()[0]).x;
				if(leftNeighborX <= x && x <= cursorPosition.x) {
					leftNeighborX = x;
					leftNeighbor = entry.getKey();
				}
			}
        }
		
		// if the dragged node is the most left one
		if(leftNeighbor == 0) result.add(cursorNode);
		
		for(int i=0;i<nodes.size();i++) {
			if(nodes.get(i) != cursorNode) result.add(nodes.get(i));
			if(nodes.get(i) == leftNeighbor) result.add(cursorNode);
		}
		
		// update the nodes with the new order
		fill(result);
	}
	
	/**
	 * Removes the highlighted property from all nodes.
	 * @author Florian Dornbusch
	 */
	private void clearHighlighted() {
		for (Entry<Integer, Object[]> entry : properties.entrySet()) {
			Object[] p = {((Point)entry.getValue()[0]), false};
			properties.put(entry.getKey(), p);
        }
	}
}