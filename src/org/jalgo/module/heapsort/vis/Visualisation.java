package org.jalgo.module.heapsort.vis;

import org.jalgo.module.heapsort.anim.Animation;
import org.jalgo.module.heapsort.model.Action;
import org.jalgo.module.heapsort.model.State;

/**
 * Implementing classes will be provided with a
 * root canvas entity and a canvas entity factory
 * used to display everything.
 * 
 * @author mbue
 */
public interface Visualisation {
	/**
	 * Configures root canvas entity and children for state <code>q</code>.
	 * @param q
	 */
	void setupState(State q);
	
	/**
	 * Remove everything from canvas.
	 */
	void teardown();
	
	/**
	 * Configures root canvas entity and children for transition from
	 * state <code>q</code> to <code>q1</code> and returns animation
	 * object.
	 * 
	 * @param q
	 * @param a
	 * @param q1
	 * @return
	 */
	Animation[] setupAnimation(State q, Action a, State q1);
	
	Animation setupAnimationLecture(State q, Action a, State q1);
	Animation setupAnimationMacro(State q, Action a, State q1);
	Animation setupAnimationBack(State q, Action a, State q1);
	
}
