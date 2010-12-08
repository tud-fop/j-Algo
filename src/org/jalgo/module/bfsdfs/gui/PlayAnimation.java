package org.jalgo.module.bfsdfs.gui;

import org.jalgo.module.bfsdfs.algorithms.Algo;
import org.jalgo.module.bfsdfs.gui.components.NodeStackView;
import org.jalgo.module.bfsdfs.gui.components.TabContainer;

/**
 * This class is used to play the algorithm. It executed a new step after the
 * {@linkplain GUIConstants#ANIMATION_SPEED} has expired. This is done in an
 * own thread.
 * @author Florian Dornbusch
 */
public class PlayAnimation implements GUIConstants, Runnable {
	
	/** The used thread. */
	private Thread animation = null;
	
	/** The {@linkplain TabContainer} */
	
	/** The algorithm from which the animation was started. */
	private Algo algo;
	
	/** 
	 * The used instance of {@linkplain NodeStackView} which started the
	 * animation.
	 */
	private NodeStackView n;
	
	/** 
	 * Constructor. Stores the used instance of {@linkplain NodeStackView},
	 * {@linkplain Algo} and {@linkplain TabContainer} from which the animation
	 * is started to send information while animating.
	 */
	public PlayAnimation(NodeStackView n, Algo algo) {
		this.n = n;
		this.algo = algo;
	}
	
	/**
	 * Starts the animation.
	 */
	public void start() {
		if ( animation == null ) {
            animation = new Thread( this );
            animation.start();
        }
	}
	
	/**
	 * Stops the animation.
	 */
	public void stop() {
		if ( animation != null && animation.isAlive() )
            animation.interrupt();
        animation = null;
	}
	
	/**
	 * Runs the animation. In every cycle, checks if the random check box is
	 * active and sends a random permutation to the algorithm, stops possibly
	 * running animations in the {@linkplain NodeStackView}, executes a new
	 * step and starts new animations.
	 */
	public void run() {
		while(animation != null && !algo.isFinished()) {
			
			n.stopAnimation();
			algo.step();
			n.startAnimation();
			
			n.repaint();
			
			try {Thread.sleep(PLAY_ANIMATION);}
			catch (InterruptedException e) {}
		}
		stop();
	}
	
	/**
	 * Returns true, if this animation is currently running.
	 * @author Florian Dornbusch
	 */
	public boolean isRunning() {
		return animation != null;
	}
}
