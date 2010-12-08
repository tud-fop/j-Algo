package org.jalgo.module.bfsdfs.gui.components;

import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jalgo.module.bfsdfs.algorithms.BFS;
import org.jalgo.module.bfsdfs.algorithms.DFS;
import org.jalgo.module.bfsdfs.algorithms.stack.NodeStatus;
import org.jalgo.module.bfsdfs.algorithms.stack.StackObserver;
import org.jalgo.module.bfsdfs.gui.AddNodeAnimation;
import org.jalgo.module.bfsdfs.gui.ComponentUtility;
import org.jalgo.module.bfsdfs.gui.GUIConstants;
import org.jalgo.module.bfsdfs.gui.GraphDrawing;
import org.jalgo.module.bfsdfs.gui.RemoveNodeAnimation;

/**
 * This class represents a Stack with list elements. These lists contain
 * integers representing the nodes. <br>
 * <code>NodeStackView</code> is needed to visualize the queue in
 * {@linkplain BFS} and the Stack in {@linkplain DFS}, whereas the queue
 * is a special stack with one element list.
 * 
 * @author Florian Dornbusch
 */
public class NodeStackView
extends JPanel
implements GUIConstants, StackObserver {
	private static final long serialVersionUID = 7442575235221720865L;
	
	// general references
	private AddNodeAnimation addNodeAnimation = null;
	private RemoveNodeAnimation removeNodeAnimation = null;
	
	/** True for DFS, false for BFS. */
	private boolean successorsVisible;
	
	/**
	 * True if a queue was recently removed from the stack and currently
	 * fades out. 
	 */
	private boolean queueRemoved = false;
	
	/** True if animations are enabled meaning the nodes fade in and out. */
	private boolean animationsEnabled = true;
	
	/**
	 *  A size that changes depending on the
	 * {@linkplain ComponentUtility#BEAMER_MODE}.
	 * */
	private int nodeSize = NODE_SIZE, borderSize = NODE_BORDER_SIZE;
	
	/** Alpha value that changes during animations. */
	private int alphaAdd, alphaRemove;
	
	/**
	 * Index of the node which was recently removed from the queue and
	 * currently fades out. if it is <b>-1</b>, no node was removed.
	 */
	private int removedNodeIndex=-1;
	
	/** 
	 * The used font. Changes depending on the
	 * {@linkplain ComponentUtility#BEAMER_MODE}.
	 */
	private Font font = NODE_FONT;
	
	/** The used stack data type. */
	private Stack<List<Integer>> stack;
	
	/** 
	 * In this map, every node in the stack gets an own
	 * {@linkplain NodeStatus}.
	 * */
	private HashMap<Integer, NodeStatus> map;
	
	/** Contains nodes that are currently animated. */
	private List<Integer> addedNodes, removedNodes;
	
	/**
	 * Constructor. Creates the stack and a map that matches each node with a
	 * certain {@linkplain NodeStatus}. Depending on the status, the node is
	 * drawn differently. <br>
	 * Also creates lists that contain nodes that are currently added to
	 * or removed from the stack, respectively.<br>
	 * Lastly creates an {@linkplain AddNodeAnimation}and a
	 * {@linkplain RemoveNodeAnimation}.
	 * @author Florian Dornbusch
	 */
	public NodeStackView() {
		setBackground(NODESTACKVIEW_BACKGROUND_COLOR);
		
		stack = new Stack<List<Integer>>();
		map = new HashMap<Integer, NodeStatus>();
		
		addedNodes = new LinkedList<Integer>();
		removedNodes = new LinkedList<Integer>();
		
		addNodeAnimation = new AddNodeAnimation(this);
		removeNodeAnimation = new RemoveNodeAnimation(this);
	}
	
	/**
	 * Draws the data as a stack or as a queue depending on
	 * the boolean value {@link #successorsVisible}.
	 * @author Florian Dornbusch
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);		
		GraphDrawing.enableAntiAliasing(g);
		if(successorsVisible) {
			try {drawStack(g);}
			catch(ConcurrentModificationException e){}
			catch( NullPointerException a){}
		}
		else {
			try{drawQueue(g);}
			catch(ConcurrentModificationException e){}
		}
	}
	
	/**
	 * Basically, this method is used to decide if this class draws a queue
	 * or a stack. In a queue the possible successors of a node are not shown.
	 * @author Florian Dornbusch
	 */
	public void setSuccessorsVisible(boolean successorsVisible) {
		this.successorsVisible = successorsVisible;
	}
	
	/**
	 * Sets the alpha value of the currently added nodes.
	 * @author Florian Dornbusch
	 */
	public void setAlphaAdd(int alpha) {
		alphaAdd = alpha;
	}
	
	/**
	 * Sets the alpha value of the currently removed nodes.
	 * @author Florian Dornbusch
	 */
	public void setAlphaRemove(int alpha) {
		alphaRemove = alpha;
	}
	
	/**
	 * This method is invoked when a {@linkplain RemoveNodeAnimation} in the
	 * queue has finished. It updates the status of the removed node to
	 * {@linkplain NodeStatus}.FINISHED.<br>
	 * <b>Note:</b> Technically the removed node is not the first element, it
	 * is just the first that is visible because the prior finished
	 * nodes stay in the stack but are not drawn.
	 * @author Florian Dornbusch
	 */
	public void removeFirstQueueNode() {
		if(stack.isEmpty() || removedNodeIndex == -1 ||
				stack.peek().size() <= removedNodeIndex) return;
		map.put(stack.peek().get(removedNodeIndex), NodeStatus.FINISHED);
		removedNodeIndex = -1;
	}
	
	/**
	 * Returns {@linkplain #successorsVisible} indicating whether this is a
	 * stack or a queue.
	 * @author Florian Dornbusch
	 */
	public boolean getSuccessorsVisible() {
		return successorsVisible;
	}
	
	/**
	 * Starts new {@linkplain AddNodeAnimation} and
	 * {@linkplain RemoveNodeAnimation}.
	 * @author Florian Dornbusch
	 */
	public void startAnimation() {
		if(!addedNodes.isEmpty()) addNodeAnimation.start();
		if(!animationsEnabled) addNodeAnimation.stop();
		
		if(!removedNodes.isEmpty()) removeNodeAnimation.start();
		if(!animationsEnabled) removeNodeAnimation.stop();
	}
	
	/**
	 * Stops the current {@linkplain AddNodeAnimation} and
	 * {@linkplain RemoveNodeAnimation}.
	 * @author Florian Dornbusch
	 */
	public void stopAnimation() {
		addNodeAnimation.stop();
		removeNodeAnimation.stop();
	}
	
	/**
	 * This method is invoked after the {@linkplain RemoveNodeAnimation} of a
	 * list has finished. It removes the list from the stack.<br>
	 * Also scrolls to the current list.
	 * @author Florian Dornbusch
	 */
	public void removeQueue() {
		if(!stack.isEmpty()) {
			stack.pop();
			queueRemoved = false;
			
			JScrollPane sp = (JScrollPane)(getParent().getParent());
	    	int posY = getHeight()-((nodeSize+SPACE_SMALL)*(stack.size()-1));
	    	sp.getVerticalScrollBar().setValue(posY);
	    	
	    	repaint();
		}
	}
	
	/**
	 * This method clears the stack.
	 * @author Florian Dornbusch
	 */
	public void clearStack() {
		if(!stack.isEmpty())stack.clear();
		queueRemoved = false;
		repaint();
	}
	
	/**
	 * This method is invoked after an {@linkplain AddNodeAnimation} has
	 * finished. It removes the animated nodes from its list.
	 * @author Florian Dornbusch
	 */
	public void clearAddedNodes() {
		addedNodes.clear();
		repaint();
	}
	
	/**
	 * This method is invoked after an {@linkplain RemoveNodeAnimation} has
	 * finished. It removes the animated nodes from its list.
	 * @author Florian Dornbusch
	 */
	public void clearRemovedNodes() {
		removedNodes.clear();
		repaint();
	}
	
	/**
	 * Returns if a queue was recently removed and fades out.
	 * @author Florian Dornbusch
	 */
	public boolean getQueueRemoved() {
		return queueRemoved;
	}
	
	public void setAnimationsEnabled(boolean enabled) {
		animationsEnabled = enabled;
	}
	
	/**
	 * Adjusts the content of this component depending on the
	 * <code>BEAMER_MODE<code>.
	 * @author Florian Dornbusch
	 */
	public void toggleBeamerMode() {
    	if(ComponentUtility.BEAMER_MODE) {
    		font = NODE_BEAMER_FONT;
    		nodeSize = NODE_BEAMER_SIZE;
    		borderSize = NODE_BEAMER_BORDER_SIZE;
    	}
    	else {
    		font = NODE_FONT;
    		nodeSize = NODE_SIZE;
    		borderSize = NODE_BORDER_SIZE;
    	}
	}
	
	/**
	 * When a queue is added to the stack, this method checks if the related
	 * owner is already in the stack.<br>
	 * If not, a new list is created and added to the stack.<br>
	 * Also scrolls to the current list.
	 * @author Florian Dornbusch
	 */
	public void onQueueAdded(int owner) {
		// search the node
		boolean nodeFound = false;
		for(List<Integer> l:stack) {
			if(l.get(0) == owner) nodeFound = true;
		}
		
		// if it was not found, a new stack line is added
		if(!nodeFound){
			List<Integer> list = new LinkedList<Integer>();
			if(successorsVisible) list.add(owner);
			stack.push(list);
			
			// scroll to the current stack line
			JScrollPane sp = (JScrollPane)(getParent().getParent());
	    	int posY = getHeight()-((nodeSize+SPACE_SMALL)*(stack.size()-1));
	    	sp.getVerticalScrollBar().setValue(posY);
		}
		
		repaint();
	}
	
	/**
	 * Apart from invoking {@linkplain #onQueueAdded(int)} this method also
	 * clears the stack.
	 * @author Florian Dornbusch
	 */
	public void onFirstQueueAdded(int owner) {
		stack.clear();
		onQueueAdded(owner);
	}
	
	/**
	 * If the status of a node changes, the node is searched in the stack. If
	 * the node is not found, it will be added to the topmost stack line.<br>
	 * Regardless of whether the node was found or not, the changed status
	 * is saved.
	 * @author Florian Dornbusch
	 */
	public void onStatusChanged(int node, NodeStatus status) {
		if(stack.isEmpty()) return;
		
		// search the node
		boolean nodeFound = false;
		for(List<Integer> l:stack) {
			if(l.contains(node)) nodeFound = true;
		}
		
		// if it was not found, add the node to the topmost stack line
		List<Integer> list = stack.peek();
		if(!nodeFound){
			list.add(node);
		}
		
		// if a node was set finished in the queue it has to be animated
		if(status == NodeStatus.FINISHED && !successorsVisible) {
			// calculate the index of this node
			for(int i=0; i<list.size(); i++) {
				if(list.get(i) == node) removedNodeIndex = i;
			}
			// add this node to the removed nodes that will be animated
			// the status will be updated in the map after the animation
			if(removedNodeIndex != -1) {
				removedNodes.add(list.get(removedNodeIndex));
			}
		}else {
			// update the new status in the map
			map.put(node, status);
			// add this node to the added nodes that will be animated
			addedNodes.add(node);
		}
				
		repaint();
	}
	
	/**
	 * This method adds nodes to the topmost stack line and their
	 * {@linkplain NodeStatus} is set <code>UNTOUCHED</code>.
	 * @author Florian Dornbusch
	 */
	public void onNodesAdded(List<Integer> nodes){
		if(nodes == null || nodes.isEmpty()) return;
		if(stack.isEmpty()) return;
		if(queueRemoved) return;
		if(stack.peek().containsAll(nodes)) return;
		
		stack.peek().addAll(nodes);
		for(int i:nodes) {
			map.put(i, NodeStatus.UNTOUCHED);
			addedNodes.add(i);
		}
		
		repaint();
	}
	
	/**
	 * This method is invoked when the successor order of the current node has
	 * changed. <br>
	 * Provided that these successors are in the topmost stack line,
	 * the old ones are removed and the new ones are added.
	 * @author Florian Dornbusch
	 */
	public void onUntouchedReplaced(List<Integer> o, List<Integer> n) {
		if(o == null || o.isEmpty()) return;
		if(n != null && o.equals(n)) return;
		if(n != null && o.size() != n.size()) return;
		
		List<Integer> list = stack.peek();
		
		stopAnimation();
		
		list.removeAll(o);	
		if(n!=null){
			list.addAll(n);
			for(int i:n) {
				map.put(i, NodeStatus.UNTOUCHED);
			}
			addedNodes.addAll(n);
		}
		
		startAnimation();
	}
	
	/**
	 * Adds all nodes that are to be removed to the corresponding list to fade
	 * them out. Also declares that a queue was removed.
	 * @author Florian Dornbusch
	 */
	public void onTopQueueRemoved() {
		if(stack.isEmpty()) return;
		
		removedNodes.addAll(stack.peek());
		queueRemoved = true;
		repaint();
	}
	
	/** Unused method from {@linkplain StackObserver}. */
	public void onAllQueuesRemoved() {}
	
	/** Unused method from {@linkplain StackObserver}. */
	public void onAllNodesFinished() {}
	
	/** Unused method from {@linkplain StackObserver}. */
	public void onCurrentNodeChanged(int i) {}
		
	/**
	 * If the nodes are too large to be fully drawn,
	 * the size of this panel is matched dynamically.
	 * @param posX : The x-coordinate of the current drawn node
	 * @param posY : The y-coordinate of the current drawn node.
	 * @author Florian Dornbusch
	 */
	private void matchSize(int posX, int posY) {
    	if(posY<0) {
			setSize(getWidth(), getHeight()- posY);
			setPreferredSize(getSize());
		}
    	
    	int x = posX+nodeSize+SPACE_SMALL;
		if(x>getWidth() || !successorsVisible) {
			setSize(x, getHeight());
			setPreferredSize(getSize());
		}
	}
	
	/**
	 * Draws a successor in the current stack line.
	 * @param g : The used Graphics.
	 * @param e : The number / label of the node.
	 * @param posX : The x-coordinate of the current drawn node
	 * @param posY : The y-coordinate of the current drawn node.
	 * @author Florian Dornbusch
	 */
	private void drawSuccessor(Graphics g, int e, int posX, int posY) {
		int alphaSuccessor = ALPHA_100_PERCENT;
		
		// decide which alpha value this node gets
		boolean b = removedNodes.contains(e);
		// if this is a stack, animate only the nodes in topmost line with a
		// remove animation
		if(successorsVisible) b = b
			&& (posY == getHeight()-((nodeSize+SPACE_SMALL)*(stack.size()-1)));
		if(b && animationsEnabled) alphaSuccessor = alphaRemove;
		if(addedNodes.contains(e) && animationsEnabled)
			alphaSuccessor = alphaAdd;
		
		try {
		// draw the node
		ComponentUtility.drawNode(g, posX, posY, e,
				map.get(e), nodeSize, borderSize,
				font, alphaSuccessor);
		}
		catch (IllegalArgumentException exc) {}
	}
	
	/**
	 * This method draws the stack in a vertical style. The first element in
	 * each line is the owner. It is separated from the possible successors
	 * through a border.
	 * @param i : The position of a node in the stack. i=1 is the lowest point.
	 * @param posY : The actual height in pixel of the node in pixel.
	 * @param j : The position of a node in the current line. j=0 is the first
	 * 			  node after the owner.
	 * @author Florian Dornbusch
	 */
	private void drawStack(Graphics g) {
		int i=1, j=-1, posY=getHeight();
		
		// highlight the owner column
		Graphics2D g2 = (Graphics2D)g;
		Paint gp = new GradientPaint(25, 0, NODESTACKVIEW_OWNER_COLOR_LEFT,
				SPACE_SMALL + nodeSize + SPACE_BIG / 2, 0,
				NODESTACKVIEW_OWNER_COLOR_RIGHT);
		g2.setPaint(gp);
		g2.fillRect(0, 0, SPACE_SMALL + nodeSize + SPACE_BIG / 2,
				posY+nodeSize+SPACE_SMALL);
		
	    // in every cycle of the loop one line is drawn
	    // a line represents the owner, its possible successors and a border
	    for(List<Integer> list:stack) {
	    	// draw not the first line because it is only used for
	    	// initialization
	    	if(list.get(0)!=0) {		
	    		// calculate the y-coordinate of the current line
	    		posY = getHeight()-((nodeSize+SPACE_SMALL)*(i));
	    		
	    		// match the new size
				matchSize(0, posY);
				
	    		// check the alpha value of the owner
		    	int alphaOwner = ALPHA_100_PERCENT;
		    	if(addedNodes.contains(list.get(0)) && animationsEnabled)
		    		alphaOwner = alphaAdd;
		    	if(removedNodes.contains(list.get(0)) && animationsEnabled
		    			&& alphaRemove > 0 && queueRemoved) {
		    		alphaOwner = alphaRemove;
		    	}
		    	
		    	// draw the owner
				ComponentUtility.drawNode(g,SPACE_SMALL, posY, list.get(0), 
						NodeStatus.WAITING, nodeSize, borderSize,
						font, alphaOwner);
				
				// in every cycle of this loop, one successor is drawn
				for(Integer e:list) {
					// leave out the owner because it is already drawn
					if(j>-1) {
						// calculate the x-coordinate of the node
						int posX = SPACE_SMALL + nodeSize + SPACE_BIG
						+j*(nodeSize+SPACE_SMALL);
						
						// match the new size
						matchSize(posX, posY);
						
						// draw the node
						drawSuccessor(g, e, posX, posY);
					}
					j++;
				}
		    	i++;
		    	j=-1;
	    	}
		}
	}
	
	/**
	 * This method draws the queue in a horizontal style.
	 * @param i : 	 the position of a node in the stack.
	 * 			  	 i=1 is the lowest point
	 * @param posY : is the actual height of the node in pixel
	 * @param j :	 the position of a node in the current line
	 * 				 j=0 is the first node after the owner
	 * @author Florian Dornbusch
	 */
	private void drawQueue(Graphics g) {
		Integer i=0;
		
		// calculate the height of the nodes, so that they are centered
		int posY = getHeight() / 2 - nodeSize / 2;
		
	    if(!stack.isEmpty()) {
	    	// draw only the top line (this should be the only line in the
	    	// stack). in every cycle, one node is drawn.
		    for(Integer e:stack.peek()) {
		    	// calculate the x-coordinate of the node
		    	int posX = SPACE_SMALL + i*(nodeSize+SPACE_SMALL);
		    	
		    	// match the new size
		    	matchSize(posX,posY);
		    	
		    	// do not draw nodes that are already finished
		    	if(map.get(e) != NodeStatus.FINISHED) {
		    		// draw the node
		    		drawSuccessor(g,e,posX,posY);
		    		i++;
		    	}
			}
	    }
	}
}