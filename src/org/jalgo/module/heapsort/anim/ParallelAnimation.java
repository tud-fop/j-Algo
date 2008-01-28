/**
 * 
 */
package org.jalgo.module.heapsort.anim;

import java.util.LinkedList;
import java.util.List;

/**
 * Subclass this if your animations run in parallel.
 * Don't forget the super calls!
 * 
 * @author mbue
 */
public class ParallelAnimation implements Animation {
	private List<Animation> anim;
	private double duration;
	
	public ParallelAnimation(double duration) {
		anim = new LinkedList<Animation>();
		this.duration = duration;
	}
	
	protected void add(Animation a) {
		anim.add(a);
	}

	public void done() {
		for (Animation a: anim)
			a.done();
	}

	public double getDuration() {
		return duration;
	}

	public void init() {
		for (Animation a: anim)
			a.init();
	}

	public void update(double time) {
		for (Animation a: anim)
			a.update(time);
	}
}