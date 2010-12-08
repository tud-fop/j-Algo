package org.jalgo.module.bfsdfs.gui.components;

import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.gui.GUIConstants;
import org.jalgo.module.bfsdfs.gui.GraphDrawing;

/**
 * This class represents an area to scroll the {@linkplain SuccessorChooser}.
 * There is a scroll area to scroll to the left and to the right. As long as
 * the mouse is inside this area, the scrolling is done automatically.
 * @author Florian Dornbusch
 */
public class ScrollArea
extends JPanel
implements MouseListener, GUIConstants {
	private static final long serialVersionUID = 134960763689527085L;
	
	/** The used Thread that scrolls the {@linkplain SuccessorChooser}. */
	private ScrollThread st;
	
	/** The used {@linkplain AlgoTab}. */
	private AlgoTab algoTab;
	
	/** Determines if this class scrolls to the left or to the right. */
	private boolean left;
	
	/** 
	 * The horizontal scroll bar that is used to scroll the
	 * {@linkplain SuccessorChooser} but is invisble because these two scroll
	 * areas scroll it.
	 */
	private JScrollBar sb;
	
	/**
	 * Constructor. Adds a {@linkplain MouseListener} to itself to enable and
	 * disable scrolling.
	 * @author Florian Dornusch
	 */
	public ScrollArea(JScrollBar sb, boolean left, AlgoTab algoTab) {
		setEnabled(false);
		addMouseListener(this);
		
		this.left = left;
		this.algoTab = algoTab;
		this.sb = sb;
		
		st = new ScrollThread();
	}
	
	/**
	 * If the mouse is inside this component, it updates the cursor and the
	 * status message and starts the scrolling thread.
	 * @author Florian Dornbusch
	 */
	public void mouseEntered(MouseEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		JAlgoGUIConnector.getInstance().setStatusMessage(
				Messages.getString("bfsdfs", "ScrollArea.status"));
		
		st.start();
		algoTab.getLeftScrollArea().repaint();
		algoTab.getRightScrollArea().repaint();
	}
	
	/**
	 * If the mouse is outside this component, this method resets the status
	 * message and stops the current scrolling thread.
	 * @author Florian Dornbusch
	 */
	public void mouseExited(MouseEvent e) {	
		JAlgoGUIConnector.getInstance().setStatusMessage(null);
		
		st.stop();
		algoTab.getLeftScrollArea().repaint();
		algoTab.getRightScrollArea().repaint();
	}
	
	/** Unused observer method. */
	public void mouseClicked(MouseEvent e) {}
	
	/** Unused observer method. */
	public void mousePressed(MouseEvent e) {}
	
	/** Unused observer method. */
	public void mouseReleased(MouseEvent e) {}
	
	/**
	 * Paints only if this panel is enabled and paints differently dependent
	 * on {@linkplain #left}.
	 * @author Florian Dornbusch
	 */
	@Override
	public void paint(Graphics gr) {
		super.paint(gr);
		Graphics2D g = (Graphics2D) gr;
		if(!this.isEnabled()) return;
		GraphDrawing.enableAntiAliasing(g);
		if(left) drawLeft(g);
		else drawRight(g);
	}
	
	/**
	 * Draws a triangle which points to the left with a gradient.
	 * @author Florian Dornbusch
	 */
	private void  drawLeft(Graphics2D g) {
		Paint gp = new GradientPaint(0, 0, NODESTACKVIEW_OWNER_COLOR_RIGHT,
				getWidth(), 0, NODESTACKVIEW_OWNER_COLOR_LEFT);
		g.setPaint(gp);
		int xpoints[] = {getWidth(), 0, getWidth()};
		int ypoints[] = {0, getHeight() / 2, getHeight()};
		g.fillPolygon(xpoints, ypoints, 3);
	}
	
	/**
	 * Draws a triangle which points to the right with a gradient.
	 * @author Florian Dornbusch
	 */
	private void  drawRight(Graphics2D g) {
		Paint gp = new GradientPaint(0, 0, NODESTACKVIEW_OWNER_COLOR_LEFT,
				getWidth(), 0, NODESTACKVIEW_OWNER_COLOR_RIGHT);
		g.setPaint(gp);
		int xpoints[] = {0, getWidth(), 0};
		int ypoints[] = {0, getHeight() / 2, getHeight()};
		g.fillPolygon(xpoints, ypoints, 3);
	}
	
	
	/**
	 * Inner class of {@linkplain ScrollArea} that handles the scrolling.
	 * @author Florian Dornbusch
	 */
	private class ScrollThread implements Runnable, GUIConstants {

		/** The used thread. */
		private Thread runner=null;
		
		/**
		 * Starts the thread and enables the other scroll area.
		 * @author Florian Dornbusch
		 */
		public void start() {
			if ( runner == null ) {
	            runner = new Thread( this );
	            
	            ScrollArea other;
	    		if(left) other = algoTab.getRightScrollArea();
	    		else other = algoTab.getLeftScrollArea();
	            
	            if(isEnabled()) {
	                other.setEnabled(true);
	            	runner.start();
	            }
	        }
		}
		
		/**
		 * Stops the current thread and repaints both {@linkplain ScrollArea}s.
		 * @author Florian Dornbusch
		 */
		public void stop() {
			if ( runner != null && runner.isAlive() )
	            runner.interrupt();
	        runner = null;
			algoTab.getLeftScrollArea().repaint();
			algoTab.getRightScrollArea().repaint();
		}
		
		/**
		 * Runs the thread. This method computes the actual scrolling.
		 * Scrolling differs dependent on {@linkplain ScrollArea#left}.
		 * @author Florian Dornbusch
		 */
		public void run() {
			if(left) runleft();
			else runright();
		}
		
		/**
		 * While the invisible scroll bar of the {@linkplain SuccessorChooser}
		 * is not leftmost, this method moves the scroll bar an amount of pixel
		 * in every loop. <br>
		 * Disables itself if it is finished to be no longer drawn.
		 * @author Florian Dornbusch
		 */
		private void runleft() {
			int value = sb.getValue();
			
			while(value > 0 && runner != null) {
				value -= SCROLL_FACTOR;
				sb.setValue(value);
				try {
					Thread.sleep(ANIMATION_REPAINT_DELAY);
				}
				catch ( InterruptedException ex ) {}
			}
			if(runner != null) setEnabled(false);
			stop();
		}
		
		/**
		 * While the invisible scroll bar of the {@linkplain SuccessorChooser}
		 * is not rightmost, this method moves the scroll bar an amount of
		 * pixel in every loop. <br>
		 * Disables itself if it is finished to be no longer drawn.
		 * @author Florian Dornbusch
		 */
		private void runright() {
			int value = sb.getValue();
			int maximum = value + SCROLL_FACTOR;
			
			while(value < maximum && runner != null) {
				sb.setValue(maximum);
				if(sb.getValue() > value) {
					maximum += SCROLL_FACTOR;
				}
				value += SCROLL_FACTOR;
				
				try {
					Thread.sleep(ANIMATION_REPAINT_DELAY);
				}
				catch ( InterruptedException ex ) {}
			}
			if(runner != null) setEnabled(false);
			stop();
		}
	}
}