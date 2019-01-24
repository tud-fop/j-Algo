package org.jalgo.module.bfsdfs.gui;

import java.util.Calendar;

import org.jalgo.module.bfsdfs.gui.components.NodeStackView;

/**
 * This class is used to animate nodes that were recently removed from the
 * stack in {@linkplain NodeStackView}. <br>
 * The animation fades out the nodes in a certain time specified in
 * {@linkplain GUIConstants#NODE_ANIMATION_TIME}.<br>
 * It handles an own thread which calculates the appropriate alpha value of the
 * nodes. 
 * @author Florian Dornbusch
 */
public class RemoveNodeAnimation
implements GUIConstants, Runnable {

	/** The used thread. */
	private Thread animation = null;
	
	/** The start time of the animation */
	private long time;
	
	/** The current time of the animation */
	private long currentTime;
	
	/** 
	 * The used instance of {@linkplain NodeStackView} which started the
	 * animation.
	 */
	private NodeStackView n;
	
	/** 
	 * Constructor. Stores the used instance of {@linkplain NodeStackView}
	 * which started the animation to send information when the animation
	 * stops.
	 * @author Florian Dornbusch
	 */
	public RemoveNodeAnimation(NodeStackView n) {
		this.n=n;
	}
	
	/**
	 * Starts the animation. Also sets the current time and start time.
	 *  @author Florian Dornbusch
	 */
	public void start() {
		if ( animation == null ) {
            animation = new Thread( this );
            animation.start();
            time = Calendar.getInstance().getTimeInMillis();
            currentTime = time;
        }
	}
	
	/**
	 * Stops the animation and removes the recently removed nodes from the
	 * list in {@linkplain NodeStackView}.<br>
	 * If a queue was declared to be removed, this is executed now.<br>
	 * If the {@linkplain NodeStackView} represents a queue, the first visible
	 * node is removed.
	 * @author Florian Dornbusch
	 */
	public void stop() {
		if ( animation != null && animation.isAlive() )
            animation.interrupt();
        animation = null;
		n.clearRemovedNodes();
		if(n.getQueueRemoved()) n.removeQueue();
		if(!n.getSuccessorsVisible()) n.removeFirstQueueNode();
	}
	
	/**
	 * Runs the animation. Calculates the alpha value according to the
	 * percentage of time passed and repaints the {@linkplain NodeStackView}.
	 * @author Florian Dornbusch
	 */
	public void run() {
		int alpha;
		n.setAlphaRemove(ALPHA_100_PERCENT);
		n.repaint();
		while(currentTime-time<=NODE_ANIMATION_TIME && animation != null) {
			try {Thread.sleep(ANIMATION_REPAINT_DELAY);}
			catch (InterruptedException e) {}
			double a =  (double)(currentTime-time);
			double b =  (double)NODE_ANIMATION_TIME;
			double factor = 1 - a / b;
			alpha = (int)(ALPHA_100_PERCENT * factor);
			n.setAlphaRemove(alpha);
			n.repaint();
			currentTime = Calendar.getInstance().getTimeInMillis();
			while(currentTime == Calendar.getInstance().getTimeInMillis());
		}
		n.repaint();
		stop();
	}
}